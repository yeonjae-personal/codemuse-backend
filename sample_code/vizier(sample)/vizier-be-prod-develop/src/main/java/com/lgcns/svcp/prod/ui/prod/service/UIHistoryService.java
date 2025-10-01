package com.lgcns.svcp.prod.ui.prod.service;

import static com.lgcns.svcp.prod.constant.SystemConstant.CHG_DEPT_NAME;
import static com.lgcns.svcp.prod.constant.SystemConstant.CHG_USER;
import static com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID;
import static com.lgcns.svcp.prod.constant.SystemConstant.RGST_DTM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.detail.AttributeChangeDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.detail.AttributeChangedResDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.detail.ChangedDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.detail.ChangedResDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.detail.EventDateDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.detail.HistoryDetailResDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.detail.ListChanged;
import com.lgcns.svcp.prod.ui.prod.dto.history.detail.StructureChangeDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.detail.StructureChangeResDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.save.AdditonalAttributeHistDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.save.FirstAddAttrReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.save.GeneralAttributeHistDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.save.OfferGroupRelHistDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.save.OfferStrcHistDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.ItemCodeInfo;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.StringUtilCustom;

import lombok.RequiredArgsConstructor;

@Service("uiHistoryService")
@RequiredArgsConstructor
@Transactional
public class UIHistoryService {

    private final CommonDao commonDao;

    public HistoryDetailResDto retrieveHistoryDetail(String objUuid) {
        // Get item info
        ItemCodeInfo itemCodeInfo = commonDao.select("Ui-item.retrieveItemCodeInfoByUuid", objUuid);

        // Get history info
        EventDateDto created = commonDao.select("Ui-history.retrieveCreatedDate", itemCodeInfo);
        EventDateDto ended = commonDao.select("Ui-history.retrieveEndedDate", itemCodeInfo);

        List<AttributeChangeDto> addtionalChanged = commonDao.selectList("Ui-history.retrieveAddtionalChanged",
                itemCodeInfo);
        List<AttributeChangeDto> generalChanged = commonDao.selectList("Ui-history.retrieveGeneralChanged",
                itemCodeInfo);
        generalChanged.forEach(dto -> {
            if ("ctgrNodeUuid".equals(dto.getLabelId())) {
		        Map<String, String> paramBefore = Collections.singletonMap(OBJ_UUID, dto.getBeforeValue());
                dto.setBeforeValue(commonDao.select("Ui-category.retrieveCtgrName", paramBefore));
		        Map<String, String> paramAfter = Collections.singletonMap(OBJ_UUID, dto.getAfterValue());
                dto.setAfterValue(commonDao.select("Ui-category.retrieveCtgrName", paramAfter));
            }
        });
        List<StructureChangeDto> offerStructureChanged = null;
        if ("G".equals(itemCodeInfo.getLctgrItemCode())) {
            offerStructureChanged = commonDao.selectList("Ui-history.retrieveStructureOfferGroupChanged", itemCodeInfo);
        } else {
            offerStructureChanged = commonDao.selectList("Ui-history.retrieveStructureOfferChanged", itemCodeInfo);
        }

        List<ChangedDto> changedInDB = new ArrayList<>();
        if (addtionalChanged != null) {
            changedInDB.addAll(addtionalChanged);
        }
        if (generalChanged != null) {
            changedInDB.addAll(generalChanged);
        }

        if (offerStructureChanged != null) {
            changedInDB.addAll(offerStructureChanged);
        }

        // Convert to Response Step 1: Group by workDate -> type+user ->
        // List<Object>
        Map<String, Map<String, List<ChangedDto>>> result = changedInDB.stream()
                // Outer key: workDate
                .collect(Collectors.groupingBy(ChangedDto::getWorkDate,
                        // Inner key: type+user
                        Collectors.groupingBy(history -> history.getChangeTypeName() + history.getChgUser(),
                                // Values: List of WorkLog objects
                                Collectors.toList())));

        // Step 2: Map result to a List<WorkDateGroup>
        List<ListChanged> changeDataConverted = result.entrySet().stream().map(workDateEntry -> {
            String workDate = workDateEntry.getKey();
            List<ChangedResDto> typeUserGroups = workDateEntry.getValue().entrySet().stream()
                    .map(changeTypeAndUserEntry -> {
                        List<ChangedDto> histories = changeTypeAndUserEntry.getValue();
                        List<AttributeChangedResDto> fields = histories.stream()
                                .filter(AttributeChangeDto.class::isInstance)
                                .map(attr -> new AttributeChangedResDto(attr.getWorkNo(),
                                        ((AttributeChangeDto) attr).getLabelId(),
                                        ((AttributeChangeDto) attr).getCommGroupCode(),
                                        ((AttributeChangeDto) attr).getFieldTypeCode(),
                                        ((AttributeChangeDto) attr).getBeforeValue(),
                                        ((AttributeChangeDto) attr).getAfterValue()))
                                .collect(Collectors.toList());
                        fields.sort(Comparator.comparingLong(AttributeChangedResDto::getWorkNo));

                        List<StructureChangeResDto> structures = histories.stream()
                                .filter(StructureChangeDto.class::isInstance)
                                .map(strc -> new StructureChangeResDto(strc.getWorkNo(),
                                        ((StructureChangeDto) strc).getWorkTypeCode(),
                                        ((StructureChangeDto) strc).getMctgrItemName(),
                                        ((StructureChangeDto) strc).getItemCodeName(),
                                        ((StructureChangeDto) strc).getObjName()))
                                .collect(Collectors.toList());
                        structures.sort(Comparator.comparingLong(StructureChangeResDto::getWorkNo));

                        String attrValUpdUser = histories.get(0).getChgUser();
                        String changeTypeName = histories.get(0).getChangeTypeName();
                        String updUserDeptName = histories.get(0).getChgDeptName();

                        return new ChangedResDto(changeTypeName, updUserDeptName, attrValUpdUser, fields, structures);
                    }).collect(Collectors.toList());
            return new ListChanged(workDate, typeUserGroups);
        }).collect(Collectors.toList());

        changeDataConverted.sort(Comparator.comparing(ListChanged::getWorkDate));
        HistoryDetailResDto historyDetail = new HistoryDetailResDto();
        historyDetail.setCreated(created);
        historyDetail.setChanged(changeDataConverted);
        historyDetail.setEnded(ended);

        return historyDetail;
    }

	public void saveHistoryAdditional(long workNo, ItemMappingDetailDto req, List<AdditionalDetailDto> oldAdditional) {
        Map<String, String> generalParams = req.getGeneralParam();
        List<AdditionalDetailDto> additional = req.getAdditional();
        if (CollectionUtils.isEmpty(additional)) {
            return;
        }

        String objUuid = generalParams.get(OBJ_UUID);
        String updUserDeptName = generalParams.get(CHG_DEPT_NAME);
        String attrValUpdUser = generalParams.get(CHG_USER);

        List<AdditionalDetailDto> changedAdditional = new ArrayList<AdditionalDetailDto>();

        for (AdditionalDetailDto attr : additional) {
            boolean isChanged = false;
            for (AdditionalDetailDto oldAttr : oldAdditional) {
                if (StringUtilCustom.equals(attr.getAttrUuid(), oldAttr.getAttrUuid())) {
                    if (UIHistoryService.isChangedValue(attr.getAttrVal(), oldAttr.getAttrVal())) {
                        isChanged = true;
                    }
                    break;
                }
            }
            if (isChanged) {
                changedAdditional.add(attr);
            }
        }

        List<AdditonalAttributeHistDto> insertAdditonalAttributeList = changedAdditional.stream()
                .map(attr -> new AdditonalAttributeHistDto(workNo, objUuid, attr.getAttrUuid(), attr.getAttrVal(),
                        updUserDeptName, attrValUpdUser))
                .collect(Collectors.toList());

        saveHistoryAdditoinalAttribute(insertAdditonalAttributeList);
    }

    private void saveHistoryAdditoinalAttribute(List<AdditonalAttributeHistDto> insertAddAttrs) {
        if (CollectionUtils.isEmpty(insertAddAttrs)) {
            return;
        }

        String objUuid = insertAddAttrs.get(0).getObjUuid();
        ItemCodeInfo itemCodeInfo = commonDao.select("Ui-item.retrieveItemCodeInfoByUuid", objUuid);

        List<FirstAddAttrReqDto> firstAddAttrReqList = insertAddAttrs.stream()
                .map(source -> new FirstAddAttrReqDto(source.getObjUuid(), source.getAttrUuid(),
                        itemCodeInfo.getItemCode(), itemCodeInfo.getMctgrItemCode(), itemCodeInfo.getLctgrItemCode()))
                .collect(Collectors.toList());

        List<AdditonalAttributeHistDto> firstData = commonDao.selectList("Ui-history.retrieveFirstAdditonalAttribute",
                Collections.singletonMap("filters", firstAddAttrReqList));

        if (firstData != null) {
            firstData.forEach(data -> data.setAttrVal(convertOldDate(data.getAttrVal())));
            insertAddAttrs.addAll(firstData);
        }

        insertAddAttrs.sort(Comparator.comparing(AdditonalAttributeHistDto::getWorkNo));
        commonDao.batchInsert("Ui-history.insertAdditonalAttributeHist", insertAddAttrs);
    }

    public List<GeneralAttributeHistDto> retrieveGeneralNotInHistory(
            List<GeneralAttributeHistDto> generalAttributeList) {
        if (CollectionUtils.isEmpty(generalAttributeList)) {
            return null;
        }

        return commonDao.selectList("Ui-history.retrieveGeneralNotInHistory",
                Collections.singletonMap("list", generalAttributeList));
    }

	public void saveHistoryGeneral(long workNo, Map<String, String> generalParams, ItemMappingDetailDto oldOffer) {
        List<GeneralAttributeHistDto> insertGnrlAttrs = new ArrayList<>();
        String objUuid = generalParams.get(OBJ_UUID);
        String chgDeptName = generalParams.get(CHG_DEPT_NAME);
        String chgUser = generalParams.get(CHG_USER);

        Map<String, String> oldGeneral = oldOffer.getGeneralParam();
        String oldChgDeptName = oldGeneral.get(CHG_DEPT_NAME);
        String oldChgUser = oldGeneral.get(CHG_USER);

        for (Map.Entry<String, String> entry : oldOffer.getGeneralEditYParam().entrySet()) {
            String key = entry.getKey();
            String oldValue = entry.getValue();

            addGnrlAttrDto(key, oldValue, workNo, objUuid, chgDeptName, chgUser, insertGnrlAttrs, generalParams);
        }

        long oldWorkNo = DateUtil.convertStringToWorkNo(oldGeneral.get(RGST_DTM));
        saveHistoryGeneralAttribute(insertGnrlAttrs, oldWorkNo, objUuid, oldChgDeptName, oldChgUser);
    }

    private void saveHistoryGeneralAttribute(List<GeneralAttributeHistDto> insertGnrlAttrs, long oldWorkNo,
            String objUuid, String oldChgDeptName, String oldChgUser) {
        if (CollectionUtils.isEmpty(insertGnrlAttrs)) {
            return;
        }
        // if not already inserted into history
        List<GeneralAttributeHistDto> generalNotInHistory = retrieveGeneralNotInHistory(insertGnrlAttrs);
        if (!CollectionUtils.isEmpty(generalNotInHistory)) {
            for (GeneralAttributeHistDto general : generalNotInHistory) {
                String attrCode = general.getAttrCode();
                String attrVal = general.getFirstAttrVal();

                general.setWorkNo(oldWorkNo);
                general.setObjUuid(objUuid);
                general.setAttrCode(attrCode);
                general.setAttrVal(convertOldDate(attrVal));
                general.setUpdUserDeptName(oldChgDeptName);
                general.setAttrValUpdUser(oldChgUser);

                insertGnrlAttrs.add(general);
            }
        }

        insertGnrlAttrs.sort(Comparator.comparing(GeneralAttributeHistDto::getWorkNo));
        commonDao.batchInsert("Ui-history.insertGeneralAttributeHist", insertGnrlAttrs);
    }

    public void saveHistoryOfferGroupRel(List<OfferGroupRelHistDto> insertOfferGroupRelList) {
        if (CollectionUtils.isEmpty(insertOfferGroupRelList)) {
            return;
        }
        commonDao.batchInsert("Ui-history.insertOfferGroupRelHist", insertOfferGroupRelList);
    }

    public void saveHistoryOfferStruc(List<OfferStrcHistDto> insertOfferStrcList) {
        if (CollectionUtils.isEmpty(insertOfferStrcList)) {
            return;
        }
        commonDao.batchInsert("Ui-history.insertOfferStrcHist", insertOfferStrcList);
    }

    public static boolean isChangedValue(String newValue, String oldValue) {
        if (StringUtilCustom.equals(newValue, oldValue)) {
            return false;
        }
        String regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
        if (oldValue != null && oldValue.matches(regex)) {
            String dateValue = DateUtil.formatDate(oldValue);
            return !StringUtilCustom.equals(newValue, dateValue);
        }
        return true;

    }

    private String convertOldDate(String oldValue) {
        try {
            String regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
            if (oldValue != null && oldValue.matches(regex)) {
                return DateUtil.formatDate(oldValue);
            }
            return oldValue;
        } catch (Exception e) {
            return oldValue;
        }
    }

    private void addGnrlAttrDto(String labelName, String oldValue, long workNo, String objUuid, String chgDeptName,
            String chgUser, List<GeneralAttributeHistDto> insertGnrlAttrs, Map<String, String> generalParam) {
        if (generalParam.containsKey(labelName)) {
            String newValue = generalParam.get(labelName);
            if (UIHistoryService.isChangedValue(newValue, oldValue)) {
                GeneralAttributeHistDto general = new GeneralAttributeHistDto();
                general.setWorkNo(workNo);
                general.setObjUuid(objUuid);
                general.setAttrCode(labelName);
                general.setAttrVal(newValue);
                general.setUpdUserDeptName(chgDeptName);
                general.setAttrValUpdUser(chgUser);
                general.setFirstAttrVal(oldValue);

                insertGnrlAttrs.add(general);
            }
        }
    }

}
