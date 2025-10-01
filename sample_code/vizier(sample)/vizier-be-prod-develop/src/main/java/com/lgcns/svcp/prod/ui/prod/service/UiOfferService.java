package com.lgcns.svcp.prod.ui.prod.service;

import static com.lgcns.svcp.prod.constant.SystemConstant.CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.CTGR_NODE_UUID;
import static com.lgcns.svcp.prod.constant.SystemConstant.ITEM_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.LANG_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.OBJ_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID;
import static com.lgcns.svcp.prod.constant.SystemConstant.VALID_END_DTM;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.mapper.OfferMapper;
import com.lgcns.svcp.prod.ui.prod.dto.ProdStruDWithItemsMDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.AdditionalMultiValueDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.ColumnMetaDataDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.RequestCommonCodeNameDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.search.SearchAdvancedReq;
import com.lgcns.svcp.prod.ui.prod.dto.export.OfferExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.export.OfferStructureExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.save.OfferStrcHistDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.Item;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.OfferCreateInfo;
import com.lgcns.svcp.prod.ui.prod.dto.offer.ProdMDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.duplicate.SaveCategoryReqDtq;
import com.lgcns.svcp.prod.ui.prod.dto.offer.list.OfferSearchDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.list.OfferSearchReq;
import com.lgcns.svcp.prod.ui.prod.dto.offer.search.OfferSearchRes;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.OfferComponentStructureRes;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.OfferStructureComponent;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.OfferStructureDetailReq;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.OfferStructureDetailRes;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.listAddComponent.ComponentPagingDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.listAddComponent.ComponentSearch;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.update.OfferComponentStructureReq;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.update.OfferStructureUpdateReq;
import com.lgcns.svcp.prod.ui.prod.service.admin.UiTableService;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.OfferListExcelHelper;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.UuidUtil;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UiOfferService {
	private final static String FILE_NAME = "Offer Details";

	private final CommonDao commonDao;
	private final CommonCodeService commonCodeService;
	private final UiCommonService uiCommonService;
	private final OfferMapper offerMapper;
	private final UIHistoryService uiHistoryService;
	private final UiTableService uiTableService;	

	private final OfferListExcelHelper excelHelper;
	
	@Autowired
	private MessageSource messageSource;

	public ProdMDto retrieveProdM(ProdMDto prodMDto) {
		return commonDao.select("Ui-offer.retrieveProdM", prodMDto);
	}

	public List<ProdStruDWithItemsMDto> retrieveProdStruDWithItemsMListWithPartiotion(
			ProdStruDWithItemsMDto prodStruDWithItemsMDto) {
		return commonDao.selectList("Ui-offer.retrieveProdStruDWithItemsMListWithPartiotion", prodStruDWithItemsMDto);
	}


	public String retrieveCmmCdNm(String tableName, String columnName, String columnValue) {
		RequestCommonCodeNameDto request = new RequestCommonCodeNameDto(tableName, columnName, columnValue);
		return commonDao.select("Ui-offer.retriveCmmCdNm", request);
	}


	public List<ColumnMetaDataDto> retrieveColumnMetaData() {

		return commonDao.selectList("UI.retrieveColumnMetaData");
	}

	public String getNewProdCd() {
		int count = commonDao.select("Ui-offer.getPricePlanCount");
		return String.format("PPMS%05d", count + 1);
	}

	public String retrieveTypeByProdItemCd(String prodItemCd) {
		return commonDao.select("UI.retrieveTypeByProdItemCd", prodItemCd);
	}

	public PageResult<?> getOffers(OfferSearchReq req) {
		OfferSearchDto dto = new OfferSearchDto();
		dto.setObjCode(req.getObjCode());
		dto.setObjName(req.getObjName());
		dto.setItemCode(req.getItemCode());
		dto.setOnlyValidDtm(req.isOnlyValidDtm());
		dto.setSize(req.getSize());
		dto.setPage(req.getPage());
		return commonDao.selectPagedList("Ui-offer.getAllOffers", dto);
	}

	public void exportToExcel(SearchAdvancedReq req, HttpServletResponse response) {
		List<OfferSearchRes> offers = commonDao.selectList("Ui-offer.searchOffersAdvanced", req);

		List<String> objUuids = offers.stream().map(OfferSearchRes::getObjUuid).toList();
		Map<String, Object> param = Map.of("objUuids", objUuids, LANG_CODE, req.getLanguage());

		List<GeneralDetailDto> generals = commonDao.selectList("Ui-item.retrieveGenerals", param);
		
		for (String uuid: objUuids) {
			String ctgrNodeUuid = commonDao.select("Ui-category.retrieveCtgrOfOffer", uuid);
			GeneralDetailDto ctgrField = new GeneralDetailDto();
			if (ctgrNodeUuid != null) {
				List<String> categoryValues = commonDao.selectList("Ui-category.retrievePathInfo", ctgrNodeUuid);
				if (categoryValues != null && !categoryValues.isEmpty()) {
					ctgrField.setObjUuid(uuid);
					ctgrField.setColName("ctgr_node_uuid");
					ctgrField.setEditYn("Y");
					ctgrField.setFieldTypeCode("TF");
					ctgrField.setLabelId("categoryNode");
					ctgrField.setAttrVal(categoryValues.get(categoryValues.size() - 1));
				} else {
					ctgrField.setObjUuid(uuid);
					ctgrField.setColName("ctgr_node_uuid");
					ctgrField.setEditYn("Y");
					ctgrField.setFieldTypeCode("TF");
					ctgrField.setLabelId("categoryNode");
					ctgrField.setAttrVal("");
				}
			} else {
				ctgrField.setObjUuid(uuid);
				ctgrField.setColName("ctgr_node_uuid");
				ctgrField.setEditYn("Y");
				ctgrField.setFieldTypeCode("TF");
				ctgrField.setLabelId("categoryNode");
				ctgrField.setAttrVal("");
			}
			generals.add(ctgrField);
		}
		
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-offer.getOfferAdditionalDetails", param);

		Map<String, List<GeneralDetailDto>> generalMap = generals.parallelStream().filter(g -> g.getObjUuid() != null)
				.collect(Collectors.groupingBy(GeneralDetailDto::getObjUuid));

		Map<String, List<AdditionalDetailDto>> additionalMap = additional.parallelStream()
				.filter(a -> a.getObjUuid() != null).collect(Collectors.groupingBy(AdditionalDetailDto::getObjUuid));

		offers.forEach(offer -> {
			String objUuid = offer.getObjUuid();
			offer.setGeneral(generalMap.getOrDefault(objUuid, List.of()));
			offer.setAdditional(additionalMap.getOrDefault(objUuid, List.of()));
		});

		List<OfferExportDto> results = IntStream.range(0, offers.size()).parallel().mapToObj(i -> {
			OfferExportDto exportDto = offerMapper.offerToExportDto(offers.get(i));
			exportDto.setIndex(i + 1);
			return exportDto;
		}).toList();

		ItemMappingDetailDto createInfo = getOfferCreateInfo(req.getItemCode(),
				req.getLanguage());
		OfferExportDto header = new OfferExportDto();
		header.setGeneral(createInfo.getGeneral());
		header.setAdditional(createInfo.getAdditional());
		List<OfferStructureExportDto> structures = commonDao.selectList("Ui-offer.getComponentForOfferStructureDetails", param);
		if (structures != null && !structures.isEmpty()) {
			int i = 0;
			for (OfferStructureExportDto dto: structures) {
				dto.setNumber(i + 1);
				i++;
			}
		} else {
			structures = null;
		}
		Item item = commonDao.select("Ui-item.getItemByItemCode", req.getItemCode());
		excelHelper.downloadExcel(buildExcelInput(results, header, structures, item.getItemName()), response, false);
	}

	private ExcelInput buildExcelInput(List<?> datas, OfferExportDto header, List<OfferStructureExportDto> structures, String itemName) {
		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName(FILE_NAME);
		excelInput.setSheetName("Offer");
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
		// add category field
		GeneralDetailDto ctgrField = new GeneralDetailDto();
		ctgrField.setColName("ctgr_node_uuid");
		ctgrField.setFieldTypeCode("TF");
		ctgrField.setLabelId("categoryNode");
		if (langCode.equals("en")) {
			ctgrField.setLabelName("Category Node");
		} else if (langCode.equals("ko")) {
			ctgrField.setLabelName("카테고리 노드");
		}
		general.add(ctgrField);

		response.setGeneral(general);
		response.setAdditional(additional);
		response.setStructures(structures);

		return response;

	}

	@Transactional
	public synchronized Map<String, String> createOffer(ItemMappingDetailDto req) {
		return createOfferTransactional(req);
	}

	@Transactional
	public Map<String, String> createOfferTransactional(ItemMappingDetailDto req) {
		Map<String, String> generalParams = req.getGeneralParam();
		String objUuid = UuidUtil.generateRandomUUID();
		String objCode = uiCommonService.generateNextItemCode(generalParams);

		generalParams.put(OBJ_UUID, objUuid);
		generalParams.put(OBJ_CODE, objCode);

		int itemMpngResult = commonDao.insert("Ui-item.insertItemMapping", generalParams);
		if (itemMpngResult < 1) {
			throw new BusinessException("Create Offer general fail");
		}

		List<AdditionalDetailDto> additional = req.getAdditional();
		if (additional != null) {
			additional.forEach(add -> add.setObjUuid(objUuid));
			saveAdditional(additional);
		}

		String ctgrNodeUuid = generalParams.get(CTGR_NODE_UUID);
		SaveCategoryReqDtq saveCategoryReqDtq = new SaveCategoryReqDtq();
		saveCategoryReqDtq.setCtgrNodeUuid(ctgrNodeUuid);
		saveCategoryReqDtq.setObjUuid(objUuid);
		saveCategoryReqDtq.setValidEndDtm(generalParams.get(VALID_END_DTM));
		commonDao.insert("Ui-category.insertCategoryRel", saveCategoryReqDtq);

		Map<String, String> response = new HashMap<>();
		response.put(CODE, objCode);
		response.put(OBJ_UUID, objUuid);
		return response;
	}

	public ItemMappingDetailDto getOfferDetail(String objUuid, String language) {

		Map<String, String> param = new HashMap<>();
		param.put(OBJ_UUID, objUuid);
		if (language != null) {
			param.put(LANG_CODE, language);
		}
		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-offer.getOfferAdditionalDetail", param);
		uiTableService.populateTableColumnKeyValues(additional);

		// add category field
		String ctgrNodeUuid = commonDao.select("Ui-category.retrieveCtgrOfOffer", param);
		GeneralDetailDto ctgrField = new GeneralDetailDto();
		ctgrField.setColName("ctgr_node_uuid");
		ctgrField.setEditYn("Y");
		ctgrField.setFieldTypeCode("TF");
		ctgrField.setLabelId("categoryNode");
		ctgrField.setLabelDscr("");
		ctgrField.setAttrVal(ctgrNodeUuid);
		general.add(ctgrField);

		ItemMappingDetailDto response = new ItemMappingDetailDto();
		response.setGeneral(general);
		response.setAdditional(additional);

		return response;
	}

	@Transactional
	public void editOffer(ItemMappingDetailDto req) {
		Map<String, String> generalParams = req.getGeneralParam();
		List<AdditionalDetailDto> additional = req.getAdditional();

		// Save history
		long workNo = DateUtil.generateWorkNo();
		String objUuid = generalParams.get(OBJ_UUID);
		String language = null;
		ItemMappingDetailDto oldOffer = getOfferDetail(objUuid, language);
		uiHistoryService.saveHistoryGeneral(workNo, generalParams, oldOffer);
		uiHistoryService.saveHistoryAdditional(workNo, req, oldOffer.getAdditional());

		String endDate = null;
		if (generalParams.containsKey(VALID_END_DTM)) {
			endDate = generalParams.get(VALID_END_DTM);
		}

		if (endDate != null) {
			Date curEndDdate = commonDao.select("Ui-common.getEndDateByObjUUID", generalParams);
			Date reqEndDate = DateUtil.convertToDateByDatabaseFormat(endDate);
			if (curEndDdate == null || curEndDdate.compareTo(reqEndDate) != 0) {
				commonDao.update("Ui-offer.updateOfferComponentRelationDate", generalParams);
			}
		}

		SaveCategoryReqDtq saveCategoryReqDtq = new SaveCategoryReqDtq();
		saveCategoryReqDtq.setObjUuid(objUuid);
		saveCategoryReqDtq.setCtgrNodeUuid(generalParams.get(CTGR_NODE_UUID));
		saveCategoryReqDtq.setValidEndDtm(generalParams.get(VALID_END_DTM));

		String oldCtgrNodeUuid = commonDao.select("Ui-category.retrieveCtgrOfOffer", saveCategoryReqDtq);
		if (oldCtgrNodeUuid != null) {
			commonDao.update("Ui-category.updateCategoryRelByOfferUuid", saveCategoryReqDtq);
		} else {
			commonDao.insert("Ui-category.insertCategoryRel", saveCategoryReqDtq);
		}

		int generalResultItemMpng = commonDao.update("Ui-item.updateItemMapping", generalParams);

		if (generalResultItemMpng < 1) {
			throw new BusinessException("Update offer general fail!");
		}

		if (additional != null) {
			additional.forEach(add -> add.setObjUuid(objUuid));
			saveAdditional(additional);
		}
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
		commonDao.batchUpdate("Ui-offer.saveOfferAdditional", fullAdditional);

		commonDao.batchDelete("Ui-common.deleteOldAdditionalMultiValue", multiDelList);
		commonDao.batchInsert("Ui-common.insertAdditionalMultiValue", multiValList);
	}

	public List<OfferStructureDetailRes> getOfferStructure(OfferStructureDetailReq req) {
		List<OfferStructureComponent> offerStructureComponent = commonDao
				.selectList("Ui-offer.getComponentForOfferStructureDetail", req);

		List<OfferStructureDetailRes> res = new ArrayList<OfferStructureDetailRes>();

		for (OfferStructureComponent struc : offerStructureComponent) {
			OfferStructureDetailRes resItem = res.stream()
					.filter(obj -> struc.getMctgrItemCode().equals(obj.getMctgrItemCode())).findFirst().orElse(null);

			if (resItem == null) {
				resItem = new OfferStructureDetailRes();
				resItem.setMctgrItemCode(struc.getMctgrItemCode());
				resItem.setMctgrItemName(struc.getMctgrItemName());
				res.add(resItem);
			}

			if ("M".equalsIgnoreCase(struc.getStrcTypeCode())) {
				List<String> multipleAcceptCode = resItem.getMultipleAcceptCode();
				if (multipleAcceptCode == null) {
					multipleAcceptCode = new ArrayList<>();
					resItem.setMultipleAcceptCode(multipleAcceptCode);
				}
				if (!multipleAcceptCode.contains(struc.getItemCode())) {
					multipleAcceptCode.add(struc.getItemCode());
				}

			} else if ("S".equalsIgnoreCase(struc.getStrcTypeCode())) {
				List<String> singleAcceptCode = resItem.getSingleAcceptCode();
				if (singleAcceptCode == null) {
					singleAcceptCode = new ArrayList<>();
					resItem.setSingleAcceptCode(singleAcceptCode);
				}
				if (!singleAcceptCode.contains(struc.getItemCode())) {
					singleAcceptCode.add(struc.getItemCode());
				}
			}
			if (struc.getObjUuid() != null) {
				List<OfferComponentStructureRes> components = resItem.getComponentList();
				if (components == null) {
					components = new ArrayList<>();
					resItem.setComponentList(components);
				}
				OfferComponentStructureRes component = new OfferComponentStructureRes();
				component.setObjUuid(struc.getObjUuid());
				component.setObjCode(struc.getObjCode());
				component.setObjName(struc.getObjName());
				component.setItemCode(struc.getItemCode());
				component.setItemCodeName(struc.getItemCodeName());
				component.setValidStartDtm(struc.getValidStartDtm());
				component.setValidEndDtm(struc.getValidEndDtm());
				component.setRelationValidStartDtm(struc.getRelationValidStartDtm());
				component.setRelationValidEndDtm(struc.getRelationValidEndDtm());
				components.add(component);
			}
		}

		for (OfferStructureDetailRes struc : res) {
			if (struc.getComponentList() != null) {
				struc.getComponentList().sort(Comparator.comparing(OfferComponentStructureRes::getObjCode));
			}
		}

		return res;
	}

	@Transactional
	public void updateOfferStructure(OfferStructureUpdateReq updateReq) throws ParseException {
		String objUuid = updateReq.getOfferUuid();
		List<OfferComponentStructureReq> componentList = updateReq.getComponentList();

		if (componentList != null && componentList.size() > 0) {
			componentList.forEach(component -> component.setOfferUuid(objUuid));

			// Save history
			OfferStructureDetailReq strucReq = new OfferStructureDetailReq();
			strucReq.setObjUuid(objUuid);
			List<OfferStructureDetailRes> structures = getOfferStructure(strucReq);

			boolean isNotEmptyComponent = structures.stream()
					.anyMatch(strc -> strc.getComponentList() != null && !strc.getComponentList().isEmpty());
			if (isNotEmptyComponent) {
				long workNo = DateUtil.generateWorkNo();
				saveHistoryStruc(workNo, updateReq);
			}

			// update component in offer to database
			commonDao.batchInsert("Ui-offer.saveOfferComponentRelation", componentList);
		}
	}

	private void saveHistoryStruc(long workNo, OfferStructureUpdateReq updateReq) {
		List<OfferComponentStructureReq> componentRel = updateReq.getComponentList();
		if (CollectionUtils.isEmpty(componentRel)) {
			return;
		}
		String objUuid = updateReq.getOfferUuid();
		String updUserDeptName = updateReq.getChgDeptName();
		String attrValUpdUser = updateReq.getChgUser();

		List<OfferStrcHistDto> insertOfferGroupRelList = componentRel.stream()
				.map(rel -> new OfferStrcHistDto(workNo, objUuid, rel.getObjUuid(), rel.getRelationValidStartDtm(),
						rel.getRelationValidEndDtm(), rel.getWorkTypeCode(), updUserDeptName, attrValUpdUser))
				.collect(Collectors.toList());
		uiHistoryService.saveHistoryOfferStruc(insertOfferGroupRelList);
	}

	public PageResult<?> getStructureComponentListAdd(ComponentSearch req) {
		ComponentPagingDto searchDto = new ComponentPagingDto();

		searchDto.setSize(req.getSize());
		searchDto.setPage(req.getPage());

		searchDto.setItemCode(req.getItemCode());

		searchDto.setObjName(req.getName());
		searchDto.setObjCode(req.getCode());
		searchDto.setComponentType(req.getComponentType());
		searchDto.setOfferUUID(req.getOfferUUID());

		return commonDao.selectPagedList("Ui-offer.getComponentListAddForOffer", searchDto);
	}

	public PageResult<?> retrieveOffersAdvanced(SearchAdvancedReq searchAdvancedReq) {
		return commonDao.selectPagedList("Ui-offer.retrieveOffersAdvanced", searchAdvancedReq);
	}

	public PageResult<?> retrieveOffersAdvancedWithDetail(SearchAdvancedReq req) {
//		return commonDao.selectPagedList("Ui-offer.retrieveOffersAdvancedWithDetail", searchAdvancedReq);
		PageResult<OfferSearchRes> result = commonDao.selectPagedList("Ui-offer.retrieveOffersAdvancedWithDetail", req);

		List<OfferSearchRes> offers = result != null ? result.getElements() : Collections.emptyList();
		//category node
		for (OfferSearchRes offerSearchRes: offers) {
			List<GeneralDetailDto> generals = offerSearchRes.getGeneral();
			String ctgrNodeUuid = commonDao.select("Ui-category.retrieveCtgrOfOffer", offerSearchRes.getObjUuid());
			GeneralDetailDto ctgrField = new GeneralDetailDto();
			ctgrField.setColName("ctgr_node_uuid");
			ctgrField.setEditYn("Y");
			ctgrField.setFieldTypeCode("TF");
			ctgrField.setLabelId("categoryNode");
			String attrVal = null;
			if (ctgrNodeUuid != null) {
				List<String> categoryValues = commonDao.selectList("Ui-category.retrievePathInfo", ctgrNodeUuid);
				if (categoryValues != null && !categoryValues.isEmpty()) {
					attrVal = categoryValues.get(categoryValues.size() - 1);
				}
			}
			ctgrField.setAttrVal(attrVal);
			generals.add(ctgrField);
		}
		List<AdditionalDetailDto> additional = offers.stream().filter(Objects::nonNull)
				.map(OfferSearchRes::getAdditional).filter(Objects::nonNull).flatMap(List::stream)
				.filter(Objects::nonNull).collect(Collectors.toList());
		uiTableService.populateTableColumnKeyValues(additional);
		return result;
	}

}
