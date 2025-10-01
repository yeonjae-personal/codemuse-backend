package com.lgcns.svcp.prod.ui.prod.service;

import static com.lgcns.svcp.prod.constant.SystemConstant.CHG_DEPT_NAME;
import static com.lgcns.svcp.prod.constant.SystemConstant.CHG_USER;
import static com.lgcns.svcp.prod.constant.SystemConstant.ITEM_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.LANG_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.OBJ_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.mapper.GroupMapper;
import com.lgcns.svcp.prod.ui.prod.dto.common.AdditionalMultiValueDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.export.GroupExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.export.GroupStructureExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.GroupDetailResDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.ItemOffrResDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.SearchGroupReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.group.InsertGroupOfferDto;
import com.lgcns.svcp.prod.ui.prod.dto.group.SaveGroupReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.group.search.GroupSearchRes;
import com.lgcns.svcp.prod.ui.prod.dto.history.save.OfferGroupRelHistDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.Item;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.OfferCreateInfo;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.OfferStructureDetailRes;
import com.lgcns.svcp.prod.ui.prod.service.admin.UiTableService;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.GroupExcelHelper;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.UuidUtil;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service("uiGroupService")
@RequiredArgsConstructor
public class UIGroupService {
	private static final String OFFER_GROUP_UUID = "offerGroupUuid";

	private final CommonDao commonDao;
	private final UIHistoryService uiHistoryService;
	private final UiCommonService uiCommonService;
	private final UiTableService uiTableService;
	private final GroupExcelHelper excelHelper;
	private final GroupMapper groupMapper;
	private final MessageSource messageSource;


	public Object searchOfferGroup(SearchGroupReqDto searchOffrGrpReqDto) {
		if (searchOffrGrpReqDto.isPaged()) {
			return commonDao.selectPagedList("Ui-group.retrieveOffrGrpList", searchOffrGrpReqDto);
		}
		return commonDao.selectList("Ui-group.retrieveOffrGrpList", searchOffrGrpReqDto);

	}

	@Transactional
	public void updateGroup(SaveGroupReqDto req) {
		Map<String, String> generalParams = req.getGeneralParam();
		List<AdditionalDetailDto> additional = req.getAdditional();
		String objUuid = generalParams.get(OBJ_UUID);

		// Save history
		long workNo = DateUtil.generateWorkNo();
		String language = null;
		GroupDetailResDto oldGroup = retrieveGroupDetail(objUuid, language);
		uiHistoryService.saveHistoryGeneral(workNo, generalParams, oldGroup);
		uiHistoryService.saveHistoryAdditional(workNo, req, oldGroup.getAdditional());
		saveHistoryRel(workNo, req);

		commonDao.insert("Ui-item.updateItemMapping", generalParams);
		insertOfferGroupRel(objUuid, req.getOfferRel());

		if (additional != null) {
			additional.forEach(add -> add.setObjUuid(objUuid));
			saveAdditional(additional);
		}
	}

	@Transactional
	public synchronized Map<String, String> createGroup(SaveGroupReqDto req) {
		return createGroupTransactional(req);
	}

	@Transactional
	public Map<String, String> createGroupTransactional(SaveGroupReqDto req) {
		Map<String, String> generalParams = req.getGeneralParam();
		String objUuid = UuidUtil.generateRandomUUID();
		String objCode = uiCommonService.generateNextItemCode(generalParams);

		generalParams.put(OBJ_UUID, objUuid);
		generalParams.put(OBJ_CODE, objCode);

		int itemMpngResult = commonDao.insert("Ui-item.insertItemMapping", generalParams);
		if (itemMpngResult < 1) {
			throw new RuntimeException("Create Group general fail");
		}

		List<AdditionalDetailDto> additional = req.getAdditional();
		if (additional != null) {
			additional.forEach(add -> add.setObjUuid(objUuid));
			saveAdditional(additional);
		}
		insertOfferGroupRel(objUuid, req.getOfferRel());
		return generalParams;
	}

	public GroupDetailResDto retrieveGroupDetail(String objUuid, String language) {
		Map<String, String> param = new HashMap<>();
		param.put(OBJ_UUID, objUuid);
		if (language != null) {
			param.put(LANG_CODE, language);
		}

		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-group.retrieveAdditional", param);
		uiTableService.populateTableColumnKeyValues(additional);
		GroupDetailResDto response = new GroupDetailResDto();
		response.setGeneral(general);
		response.setAdditional(additional);

		List<ItemOffrResDto> childOffr = commonDao.selectList("Ui-group.getItemOffr",
				Collections.singletonMap(OFFER_GROUP_UUID, objUuid));
		response.setChildOffr(childOffr);

		return response;
	}

	private void saveHistoryRel(long workNo, SaveGroupReqDto saveGroupReqDto) {
		List<ItemOffrResDto> offerRel = saveGroupReqDto.getOfferRel();
		if (CollectionUtils.isEmpty(offerRel)) {
			return;
		}
		Map<String, String> generalParams = saveGroupReqDto.getGeneralParam();
		String offerGroupUuid = generalParams.get(OBJ_UUID);
		String updUserDeptName = generalParams.get(CHG_DEPT_NAME);
		String attrValUpdUser = generalParams.get(CHG_USER);

		List<OfferGroupRelHistDto> insertOfferGroupRelList = offerRel.stream()
				.map(rel -> new OfferGroupRelHistDto(workNo, offerGroupUuid, rel.getOffrUuid(), rel.getValidStartDtm(),
						rel.getValidEndDtm(), rel.getWorkTypeCode(), updUserDeptName, attrValUpdUser))
				.collect(Collectors.toList());

		uiHistoryService.saveHistoryOfferGroupRel(insertOfferGroupRelList);
	}

	private void insertOfferGroupRel(String groupUuid, List<ItemOffrResDto> offerRel) {
		if (CollectionUtils.isEmpty(offerRel)) {
			return;
		}

		List<InsertGroupOfferDto> groupOfferList = offerRel.stream().map(offer -> new InsertGroupOfferDto(groupUuid,
				offer.getOffrUuid(), offer.getValidStartDtm(), offer.getValidEndDtm())).collect(Collectors.toList());

		commonDao.batchInsert("Ui-group.insertOfferGroupRel", groupOfferList);
	}

	public ItemMappingDetailDto retrieveCreateInfo(String itemCode) {
		Map<String, String> param = Collections.singletonMap(ITEM_CODE, itemCode);

		ItemMappingDetailDto response = new ItemMappingDetailDto();
		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-item.retrieveAdditional", param);
		uiTableService.populateTableColumnKeyValues(additional);
		response.setGeneral(general);
		response.setAdditional(additional);
		return response;
	}

	private void saveAdditional(List<AdditionalDetailDto> fullAdditional) {
		List<AdditionalDetailDto> dmList = fullAdditional.stream().filter(obj -> "DM".equals(obj.getFieldTypeCode()))
				.collect(Collectors.toList());

		List<AdditionalMultiValueDto> multiValList = new ArrayList<>();
		List<AdditionalMultiValueDto> multiDelList = new ArrayList<>();

		for (AdditionalDetailDto obj : dmList) {
			List<AdditionalMultiValueDto> multiValues = AdditionalMultiValueDto.parseVals(obj.getObjUuid(),
					obj.getAttrUuid(), obj.getAttrVal());
			if (multiValues != null) {
				multiValList.addAll(multiValues);
			}
			multiDelList.add(new AdditionalMultiValueDto(obj.getObjUuid(), obj.getAttrUuid(), 0, null));
		}

		fullAdditional.forEach(e -> {
			if ("DM".equals(e.getFieldTypeCode())) {
				e.setAttrVal(null);
			}
		});
		commonDao.batchUpdate("Ui-group.saveAdditionalMetadata", fullAdditional);

		commonDao.batchDelete("Ui-common.deleteOldAdditionalMultiValue", multiDelList);
		commonDao.batchInsert("Ui-common.insertAdditionalMultiValue", multiValList);
	}

	public List<ItemOffrResDto> retrieveOfferGroupRel(String objUuid) {
		Map<String, String> param = Collections.singletonMap(OFFER_GROUP_UUID, objUuid);
		return commonDao.selectList("Ui-group.getItemOffr", param);
	}

	public void updateOfferGroupRel(List<InsertGroupOfferDto> groupOfferList) {
		commonDao.batchInsert("Ui-group.insertOfferGroupRel", groupOfferList);
	}

	public PageResult<?> retrieveGroupsAdvancedDetail(SearchGroupReqDto reqDto) {
		return commonDao.selectPagedList("Ui-group.retrieveGroupsAdvancedDetail", reqDto);
	}

	public void export(SearchGroupReqDto req, HttpServletResponse response) {
		List<GroupSearchRes> groups = commonDao.selectList("Ui-group.retrieveOffrGrpListExcel", req);

		List<String> objUuids = groups.stream().map(GroupSearchRes::getObjUuid).toList();
		Map<String, Object> param = Map.of("objUuids", objUuids, LANG_CODE, req.getLanguage());

		List<GeneralDetailDto> generals = commonDao.selectList("Ui-item.retrieveGenerals", param);
		
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-group.retrieveAdditionals", param);

		Map<String, List<GeneralDetailDto>> generalMap = generals.parallelStream().filter(g -> g.getObjUuid() != null)
				.collect(Collectors.groupingBy(GeneralDetailDto::getObjUuid));

		Map<String, List<AdditionalDetailDto>> additionalMap = additional.parallelStream()
				.filter(a -> a.getObjUuid() != null).collect(Collectors.groupingBy(AdditionalDetailDto::getObjUuid));

		groups.forEach(group -> {
			String objUuid = group.getObjUuid();
			group.setGeneral(generalMap.getOrDefault(objUuid, List.of()));
			group.setAdditional(additionalMap.getOrDefault(objUuid, List.of()));
		});

		List<GroupExportDto> results = IntStream.range(0, groups.size()).parallel().mapToObj(i -> {
			GroupExportDto exportDto = groupMapper.groupToExportDto(groups.get(i));
			exportDto.setIndex(i + 1);
			return exportDto;
		}).toList();

		ItemMappingDetailDto createInfo = getOfferCreateInfo(req.getItemCode(), req.getLanguage());
		GroupExportDto header = new GroupExportDto();
		header.setGeneral(createInfo.getGeneral());
		header.setAdditional(createInfo.getAdditional());
		List<GroupStructureExportDto> structures = commonDao.selectList("Ui-group.getItemOffrStructureExcel", param);
		if (structures != null && !structures.isEmpty()) {
			int i = 0;
			for (GroupStructureExportDto dto: structures) {
				dto.setNo(i + 1);
				i++;
			}
		} else {
			structures = null;
		}
		Item item = commonDao.select("Ui-item.getItemByItemCode", req.getItemCode());
		excelHelper.downloadExcel(buildExcelInput(results, header, structures, item.getItemName()), response, false);
	}
	
	private ExcelInput buildExcelInput(List<?> datas, GroupExportDto header, List<GroupStructureExportDto> structures, String itemName) {
		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName("group");
		excelInput.setSheetName("Group");
		excelInput.setFormatDate(DateConstant.YYYYMMDD);
		excelInput.setDatas(datas);
		Object[] objectArray = new Object[3];
        objectArray[0] = header;
        objectArray[1] = structures;
        objectArray[2] = itemName;
		excelInput.setObject(objectArray);
		return excelInput;
	}

	public OfferCreateInfo getOfferCreateInfo(String itemCode, String langCode) {
		Map<String, String> param = Map.of(ITEM_CODE, itemCode, LANG_CODE, langCode);

		OfferCreateInfo response = new OfferCreateInfo();
		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-item.retrieveAdditional", param);
		List<OfferStructureDetailRes> structures = commonDao.selectList("Ui-offer.retreiveStructureInfo", param);

		uiTableService.populateTableColumnKeyValues(additional);

		response.setGeneral(general);
		response.setAdditional(additional);
		response.setStructures(structures);

		return response;

	}
}
