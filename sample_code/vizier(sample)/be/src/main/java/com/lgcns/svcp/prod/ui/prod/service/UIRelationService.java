package com.lgcns.svcp.prod.ui.prod.service;

import static com.lgcns.svcp.prod.constant.SystemConstant.ITEM_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.LANG_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.OBJ_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.ui.prod.dto.common.AdditionalMultiValueDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.search.SearchAdvancedReq;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.service.admin.UiTableService;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.UuidUtil;
import com.lgcns.svcp.prod.util.paging.PageResult;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UIRelationService {
	private final CommonDao commonDao;
	private final UiCommonService uiCommonService;
	private final UIExtendsService uiExtendsService;
	private final UIHistoryService uiHistoryService;
	private final UiTableService uiTableService;

	public PageResult<?> retrieveRelationsAdvanced(SearchAdvancedReq searchAdvancedReq) {
		return commonDao.selectPagedList("Ui-relation.searchRelationsAdvanced", searchAdvancedReq);
	}

	public ItemMappingDetailDto retrieveRelationCreateInfo(String itemCode, String langCode) {
		Map<String, String> param = Map.of(ITEM_CODE, itemCode, LANG_CODE, langCode);

		ItemMappingDetailDto response = new ItemMappingDetailDto();
		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-item.retrieveAdditional", param);

		uiTableService.populateTableColumnKeyValues(additional);
		response.setGeneral(general);
		response.setAdditional(additional);
		return response;

	}

	@Transactional
	public synchronized String createRelation(ItemMappingDetailDto req) {
		return createRelationTransactional(req);
	}

	@Transactional
	public String createRelationTransactional(ItemMappingDetailDto req) {
		Map<String, String> generalParams = req.getGeneralParam();
		String objUuid = UuidUtil.generateRandomUUID();
		String objCode = uiCommonService.generateNextItemCode(generalParams);

		generalParams.put(OBJ_UUID, objUuid);
		generalParams.put(OBJ_CODE, objCode);

		int itemMpngResult = commonDao.insert("Ui-item.insertItemMapping", generalParams);
		if (itemMpngResult < 1) {
			throw new BusinessException("Create Relation general fail");
		}

		List<AdditionalDetailDto> additional = req.getAdditional();
		if (additional != null) {
			additional.forEach(add -> add.setObjUuid(objUuid));
			saveAdditional(additional);
		}
		return objUuid;
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
		commonDao.batchUpdate("Ui-relation.saveRelationAdditional", fullAdditional);

		commonDao.batchDelete("Ui-common.deleteOldAdditionalMultiValue", multiDelList);
		commonDao.batchInsert("Ui-common.insertAdditionalMultiValue", multiValList);
	}

	@Transactional
	public void updateRelation(ItemMappingDetailDto req) {
		Map<String, String> generalParams = req.getGeneralParam();
		List<AdditionalDetailDto> additional = req.getAdditional();

		// Save history
		long workNo = DateUtil.generateWorkNo();
		String objUuid = generalParams.get(OBJ_UUID);
		ItemMappingDetailDto oldRelation = uiExtendsService.retrieveDpdcRelDef(objUuid);
		uiHistoryService.saveHistoryGeneral(workNo, generalParams, oldRelation);
		uiHistoryService.saveHistoryAdditional(workNo, req, oldRelation.getAdditional());


		int generalResultItemMpng = commonDao.update("Ui-item.updateItemMapping", generalParams);

		if (generalResultItemMpng < 1) {
			throw new BusinessException("Update relation general fail!");
		}

		if (additional != null) {
			additional.forEach(add -> add.setObjUuid(objUuid));
			saveAdditional(additional);
		}
	}

}
