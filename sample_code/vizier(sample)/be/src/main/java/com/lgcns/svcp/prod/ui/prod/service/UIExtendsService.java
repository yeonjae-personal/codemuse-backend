package com.lgcns.svcp.prod.ui.prod.service;

import static com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.mapper.RelationManagerMapper;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.CountTargetResDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.ItemOffrResDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.OfferDpdcRelDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.OffrDpdcReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.OffrGrpResDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.RelationGridViewDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.RelationGridViewEntity;
import com.lgcns.svcp.prod.ui.prod.dto.extend.RelationGridViewExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.RelationGridViewSearchDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.RelationViewReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.RelationViewResDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.SaveTargetReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.TargetReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.TargetResDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.save.OfferStrcHistDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.service.admin.UiTableService;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.RelationManagerExcelHelper;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.StringUtilCustom;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service("uiExtendsService")
@RequiredArgsConstructor
@Transactional
public class UIExtendsService {

	private final CommonDao commonDao;
	private final UIHistoryService uiHistoryService;
	private final UiTableService uiTableService;
	private final RelationManagerExcelHelper excelHelper;
	private final RelationManagerMapper managerMapper;
	private final MessageSource messageSource;

	private static final String OFFER_GROUP_UUIDS_KEY = "offerGroupUuids";
	private static final String REFERENCE_UUIDS_KEY = "referenceUuids";

	public TargetResDto getTarget(TargetReqDto req) {
		List<OffrGrpResDto> leaderGroups = commonDao.selectList("Ui-extends.getTargetLeader", req);
		List<OffrGrpResDto> followerGroups = commonDao.selectList("Ui-extends.getTargetFollower", req);

		// For-each leaderGroups
		for (OffrGrpResDto group : leaderGroups) {
			RelationViewReqDto relationViewReqDto = new RelationViewReqDto();
			relationViewReqDto.setTargetUuid(group.getOfferGroupUuid());
			relationViewReqDto.setIncludeGroup(req.isIncludeGroup());
			relationViewReqDto.setOnlyValidDtm(req.isOnlyValidDtm());
			List<RelationViewResDto> followers = getFollower(relationViewReqDto);
			group.setFollowerList(followers);
		}

		// For-each followerGroups
		for (OffrGrpResDto group : followerGroups) {
			RelationViewReqDto relationViewReqDto = new RelationViewReqDto();
			relationViewReqDto.setTargetUuid(group.getOfferGroupUuid());
			relationViewReqDto.setIncludeGroup(req.isIncludeGroup());
			relationViewReqDto.setOnlyValidDtm(req.isOnlyValidDtm());
			List<RelationViewResDto> leaders = getLeader(relationViewReqDto);
			group.setLeaderList(leaders);
		}

		return new TargetResDto(leaderGroups, followerGroups);
	}

	public List<RelationViewResDto> getLeader(RelationViewReqDto req) {
		// Fetch main results
		List<RelationViewResDto> results = commonDao.selectList("Ui-extends.getLeaderView", req);
		if (results == null || results.isEmpty()) {
			return List.of();
		}

		// Extract unique UUIDs
		List<String> offerGroupUuids = results.stream().map(RelationViewResDto::getOfferGroupUuid)
				.filter(Objects::nonNull).distinct().toList();

		List<String> referenceUuids = results.stream().map(RelationViewResDto::getReferenceUuid)
				.filter(Objects::nonNull).distinct().toList();

		// Fetch offer data if offerGroupUuids is not empty
		List<ItemOffrResDto> itemOffers = offerGroupUuids.isEmpty() ? List.of()
				: commonDao.selectList("Ui-group.getItemOffrs", Map.of(OFFER_GROUP_UUIDS_KEY, offerGroupUuids));

		// Fetch dependency data if referenceUuids is not empty
		List<OfferDpdcRelDto> dpdcRelations = referenceUuids.isEmpty() ? List.of()
				: commonDao.selectList("Ui-extends.getTargetUuids", Map.of(REFERENCE_UUIDS_KEY, referenceUuids));

		// Group data by UUID
		Map<String, List<ItemOffrResDto>> offerMap = itemOffers.stream()
				.collect(Collectors.groupingBy(ItemOffrResDto::getOfferGroupUuid));

		Map<String, List<String>> dpdcMap = dpdcRelations.stream().collect(Collectors.groupingBy(
				OfferDpdcRelDto::getBaseUuid, Collectors.mapping(OfferDpdcRelDto::getTrgtUuid, Collectors.toList())));

		// Enrich results with childOffr and referenceUuids
		results.forEach(result -> {
			Optional.ofNullable(result.getOfferGroupUuid()).filter(offerMap::containsKey)
					.ifPresent(uuid -> result.setChildOffr(offerMap.get(uuid)));

			Optional.ofNullable(result.getReferenceUuid()).filter(dpdcMap::containsKey)
					.ifPresent(uuid -> result.setReferenceUuids(dpdcMap.get(uuid)));
		});

		return results;
	}

	public List<RelationViewResDto> getFollower(RelationViewReqDto req) {
		// Fetch main results
		List<RelationViewResDto> results = commonDao.selectList("Ui-extends.getFollowerView", req);
		if (results == null || results.isEmpty()) {
			return List.of();
		}

		// Extract unique UUIDs
		List<String> offerGroupUuids = results.stream().map(RelationViewResDto::getOfferGroupUuid)
				.filter(Objects::nonNull).distinct().toList();

		List<String> referenceUuids = results.stream().map(RelationViewResDto::getReferenceUuid)
				.filter(Objects::nonNull).distinct().toList();

		// Fetch offer data if offerGroupUuids is not empty
		List<ItemOffrResDto> itemOffers = offerGroupUuids.isEmpty() ? List.of()
				: commonDao.selectList("Ui-group.getItemOffrs", Map.of(OFFER_GROUP_UUIDS_KEY, offerGroupUuids));

		// Fetch dependency data if referenceUuids is not empty
		List<OfferDpdcRelDto> dpdcRelations = referenceUuids.isEmpty() ? List.of()
				: commonDao.selectList("Ui-extends.getBaseUuids", Map.of(REFERENCE_UUIDS_KEY, referenceUuids));

		// Group data by UUID
		Map<String, List<ItemOffrResDto>> offerMap = itemOffers.stream()
				.collect(Collectors.groupingBy(ItemOffrResDto::getOfferGroupUuid));

		Map<String, List<String>> dpdcMap = dpdcRelations.stream().collect(Collectors.groupingBy(
				OfferDpdcRelDto::getTrgtUuid, Collectors.mapping(OfferDpdcRelDto::getBaseUuid, Collectors.toList())));

		// Enrich results with childOffr and referenceUuids
		results.forEach(result -> {
			Optional.ofNullable(result.getOfferGroupUuid()).filter(offerMap::containsKey)
					.ifPresent(uuid -> result.setChildOffr(offerMap.get(uuid)));

			Optional.ofNullable(result.getReferenceUuid()).filter(dpdcMap::containsKey)
					.ifPresent(uuid -> result.setReferenceUuids(dpdcMap.get(uuid)));
		});

		return results;
	}

	@Transactional
	public void saveTarget(SaveTargetReqDto saveTargetReqDto) {
		// save history
		// long workNo = DateUtil.generateWorkNo();
		// saveHistoryStruc(workNo, saveTargetReqDto);

		commonDao.batchInsert("Ui-group.insertOfferGroupRel", saveTargetReqDto.getInsertGroupOfferLst());
		commonDao.batchInsert("Ui-extends.insertOfferDpdc", saveTargetReqDto.getAddOffrDpdcLst());
		commonDao.batchUpdate("Ui-extends.updateOfferDpdc", saveTargetReqDto.getUpdateOffrDpdcLst());
	}

//	private void saveHistoryStruc(long workNo, SaveTargetReqDto updateReq) {
//		List<OffrDpdcReqDto> addOffrDpdcLst = updateReq.getAddOffrDpdcLst();
//		List<OffrDpdcReqDto> updateOffrDpdcLst = updateReq.getUpdateOffrDpdcLst();
//
//		List<OffrDpdcReqDto> mergedList = new ArrayList<>();
//		if (addOffrDpdcLst != null) {
//			mergedList.addAll(addOffrDpdcLst);
//		}
//		if (updateOffrDpdcLst != null) {
//			mergedList.addAll(updateOffrDpdcLst);
//		}
//		if (CollectionUtils.isEmpty(mergedList)) {
//			return;
//		}
//		if (updateOffrDpdcLst != null) {
//			String updUserDeptName = updateReq.getChgDeptName();
//			String attrValUpdUser = updateReq.getChgUser();
//
//			List<OfferStrcHistDto> insertOfferGroupRelList = updateOffrDpdcLst.stream()
//					.map(rel -> new OfferStrcHistDto(workNo, rel.getDpdcRelUuid(), rel.getTrgtUuid(),
//							rel.getValidStartDtm(), rel.getValidEndDtm(), rel.getWorkTypeCode(), updUserDeptName,
//							attrValUpdUser))
//					.collect(Collectors.toList());
//
//			uiHistoryService.saveHistoryOfferStruc(insertOfferGroupRelList);
//		}
//	}

	@Transactional
	public void updateOffrDpdc(OffrDpdcReqDto reqDto) {
		// save history
		long workNo = DateUtil.generateWorkNo();
		String trgtUuid = reqDto.getTrgtUuid();
		String dpdcRelUuid = reqDto.getDpdcRelUuid();
		String validStartDtm = reqDto.getValidStartDtm();
		String validEndDtm = reqDto.getValidEndDtm();
		String updUserDeptName = reqDto.getChgDeptName();
		String attrValUpdUser = reqDto.getChgUser();
		String workTypeCode = reqDto.getWorkTypeCode();

		OfferStrcHistDto strcHistDto = new OfferStrcHistDto(workNo, dpdcRelUuid, trgtUuid, validStartDtm, validEndDtm,
				workTypeCode, updUserDeptName, attrValUpdUser);

		List<OfferStrcHistDto> insertOfferGroupRelList = new ArrayList<>();
		insertOfferGroupRelList.add(strcHistDto);
		uiHistoryService.saveHistoryOfferStruc(insertOfferGroupRelList);

		// update offer dpdc
		commonDao.update("Ui-extends.updateOfferDpdc", reqDto);
	}

	public ItemMappingDetailDto retrieveDpdcRelDef(String objUuid) {
		Map<String, String> param = Collections.singletonMap(OBJ_UUID, objUuid);

		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-extends.retrieveAdditional", param);
		uiTableService.populateTableColumnKeyValues(additional);
		ItemMappingDetailDto response = new ItemMappingDetailDto();
		response.setGeneral(general);
		response.setAdditional(additional);
		return response;
	}

	public CountTargetResDto countTarget(RelationViewReqDto req) {
		int leaderCount = getLeader(req).size();
		int followerCount = getFollower(req).size();

		CountTargetResDto countTargetResDto = new CountTargetResDto();
		countTargetResDto.setLeaderNumber(leaderCount);
		countTargetResDto.setFollowerNumber(followerCount);

		return countTargetResDto;
	}

	public void downloadRelationManager(String uuid, String category, String type, String value,
			HttpServletResponse response) {
		RelationGridViewSearchDto properties = buildPropertiesDownload(uuid, category, type, value);
		List<RelationGridViewEntity> entities = commonDao.selectList("Ui-extends.getFollowerViewGridView", properties);
		List<RelationGridViewExportDto> dtos = convertDataRelationGridViewDownload(entities);

		excelHelper.of(RelationGridViewExportDto.class).downloadExcel(buildExcelInput(dtos), response, false);
	}

	private List<RelationGridViewExportDto> convertDataRelationGridViewDownload(List<RelationGridViewEntity> elements) {
		List<RelationGridViewExportDto> results = new ArrayList<>();
		int i = 1;
		for (RelationGridViewEntity item : elements) {
			RelationGridViewExportDto dto = managerMapper.convertToExcelDto(item);
			if (item.getOfferGroupUuid() != null) {
				dto.setFollowerCode(item.getFollowerCodeGroup());
				dto.setFollowerName(item.getFollowerNameGroup());
			}
			dto.setNo(i);
			results.add(dto);
			i++;
		}
		return results;
	}

	private RelationGridViewSearchDto buildPropertiesDownload(String uuid, String category, String type, String value) {
		RelationGridViewSearchDto result = new RelationGridViewSearchDto();
		List<OffrGrpResDto> groups = commonDao.selectList("Ui-extends.getTargetLeaderGridViewRelation", uuid);
		List<String> objUuids = new ArrayList<>();
		objUuids.add(uuid);
		if (groups != null && !groups.isEmpty()) {
			objUuids.addAll(groups.stream().map(OffrGrpResDto::getOfferGroupUuid).toList());
		}
		result.setObjUuids(objUuids);
		if (StringUtilCustom.isNotBlank(value) && StringUtilCustom.isNotBlank(category)) {
			if (category.equalsIgnoreCase("offer")) {
				switch (type) {
				case "code" -> result.setOfferCode(value.trim().toLowerCase());
				case "name" -> result.setOfferName(value.trim().toLowerCase());
				}
			} else if (category.equalsIgnoreCase("group")) {
				switch (type) {
				case "code" -> result.setGroupCode(value.trim().toLowerCase());
				case "name" -> result.setGroupName(value.trim().toLowerCase());
				}
			} else if (category.equalsIgnoreCase("relation")) {
				switch (type) {
				case "code" -> result.setRelationCode(value.trim().toLowerCase());
				case "name" -> result.setRelationName(value.trim().toLowerCase());
				}
			}
		} else if (StringUtilCustom.isNotBlank(value)) {
			switch (type) {
			case "code" -> result.setSearchByCode(value.trim().toLowerCase());
			case "name" -> result.setSearchByName(value.trim().toLowerCase());
			}
		}
		return result;
	}

	private ExcelInput buildExcelInput(List<RelationGridViewExportDto> entities) {
		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName("relationmanager");
		excelInput.setSheetName(
				messageSource.getMessage("excel.sheet.relation.manager", null, LocaleContextHolder.getLocale()));
		excelInput.setFormatDate(DateConstant.YYYYMMDD);
		excelInput.setDatas(entities);
		return excelInput;
	}

	public PageResult<RelationGridViewDto> getRelationGridView(String uuid, String category, String type, String value,
			Integer page, Integer size) {
		RelationGridViewSearchDto properties = buildProperties(uuid, category, type, value, page, size);
		PageResult<RelationGridViewEntity> pageResult = commonDao.selectPagedList("Ui-extends.getFollowerViewGridView",
				properties);
		List<RelationGridViewDto> dtos = convertDataRelationGridView(pageResult.getElements());
		return new PageResult<RelationGridViewDto>(dtos, pageResult.getPage(), pageResult.getSize(),
				pageResult.getTotalElements());
	}

	private List<RelationGridViewDto> convertDataRelationGridView(List<RelationGridViewEntity> elements) {
		List<RelationGridViewDto> results = new ArrayList<>();
		int i = 1;
		for (RelationGridViewEntity item : elements) {
			RelationGridViewDto dto = managerMapper.convertToDto(item);
			if (item.getOfferGroupUuid() != null) {
				dto.setFollowerCode(item.getFollowerCodeGroup());
				dto.setFollowerName(item.getFollowerNameGroup());
			}
			dto.setNo(i);
			results.add(dto);
			i++;
		}
		return results;
	}

	private RelationGridViewSearchDto buildProperties(String uuid, String category, String type, String value,
			Integer page, Integer size) {
		RelationGridViewSearchDto result = new RelationGridViewSearchDto();
		result.setPage(page);
		result.setSize(size);
		List<OffrGrpResDto> groups = commonDao.selectList("Ui-extends.getTargetLeaderGridViewRelation", uuid);
		List<String> objUuids = new ArrayList<>();
		objUuids.add(uuid);
		if (groups != null && !groups.isEmpty()) {
			objUuids.addAll(groups.stream().map(OffrGrpResDto::getOfferGroupUuid).toList());
		}
		result.setObjUuids(objUuids);
		if (StringUtilCustom.isNotBlank(value) && StringUtilCustom.isNotBlank(category)) {
			if (category.equalsIgnoreCase("offer")) {
				switch (type) {
				case "code" -> result.setOfferCode(value.trim().toLowerCase());
				case "name" -> result.setOfferName(value.trim().toLowerCase());
				}
			} else if (category.equalsIgnoreCase("group")) {
				switch (type) {
				case "code" -> result.setGroupCode(value.trim().toLowerCase());
				case "name" -> result.setGroupName(value.trim().toLowerCase());
				}
			} else if (category.equalsIgnoreCase("relation")) {
				switch (type) {
				case "code" -> result.setRelationCode(value.trim().toLowerCase());
				case "name" -> result.setRelationName(value.trim().toLowerCase());
				}
			}
		} else if (StringUtilCustom.isNotBlank(value)) {
			switch (type) {
			case "code" -> result.setSearchByCode(value.trim().toLowerCase());
			case "name" -> result.setSearchByName(value.trim().toLowerCase());
			}
		}
		return result;
	}
}
