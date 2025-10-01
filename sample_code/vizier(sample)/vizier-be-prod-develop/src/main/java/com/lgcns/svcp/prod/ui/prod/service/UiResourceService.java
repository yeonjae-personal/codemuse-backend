package com.lgcns.svcp.prod.ui.prod.service;

import static com.lgcns.svcp.prod.constant.SystemConstant.ITEM_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.LANG_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.OBJ_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID;
import static com.lgcns.svcp.prod.constant.SystemConstant.VALID_END_DTM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.mapper.ResourceMapper;
import com.lgcns.svcp.prod.ui.prod.dto.common.AdditionalMultiValueDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.search.SearchAdvancedReq;
import com.lgcns.svcp.prod.ui.prod.dto.export.MultiEntityExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.export.ResourceExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.Item;
import com.lgcns.svcp.prod.ui.prod.dto.item.ItemReq;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.EntityItemRelResDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.OfferCreateInfo;
import com.lgcns.svcp.prod.ui.prod.dto.resource.AdditionalBlngResMDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.AdditionalServiceResMDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.GeneralBlngResMDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.GeneralServiceResMDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.GroupedBlngResMDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.GroupedServiceResMDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.RawBlngResMDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.RawServiceResMDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.ResourceSearchReq;
import com.lgcns.svcp.prod.ui.prod.dto.resource.create.ResourceCreateInfoRes;
import com.lgcns.svcp.prod.ui.prod.dto.resource.create.ResourceCreateReq;
import com.lgcns.svcp.prod.ui.prod.dto.resource.list.ResourceSearchDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.search.ResoureSearchRes;
import com.lgcns.svcp.prod.ui.prod.dto.resource.update.ResourceUpdateReq;
import com.lgcns.svcp.prod.ui.prod.service.admin.UiTableService;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.ResourceExcelHelper;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.UuidUtil;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service("uiResourceService")
@RequiredArgsConstructor
public class UiResourceService {
	
	private final CommonDao commonDao;
	private final ResourceMapper resourceMapper;
	private final UiCommonService uiCommonService;
	private final UIMultiEntityService uiMultiEntityService;
	private final UIHistoryService uiHistoryService;
	private final UiTableService uiTableService;
	private final ResourceExcelHelper excelHelper;
	private final MessageSource messageSource;


	public GroupedServiceResMDto retrieveGroupedServiceResM(RawServiceResMDto rawServiceResMDto) {

		RawServiceResMDto raw = commonDao.select("Ui-resource.retrieveServiceResM", rawServiceResMDto);
		GeneralServiceResMDto general = new GeneralServiceResMDto(raw);
		AdditionalServiceResMDto additional = new AdditionalServiceResMDto(raw);

		return GroupedServiceResMDto.builder().general(general).additional(additional).build();
	}

	public GroupedBlngResMDto retrieveGroupedBlngResM(RawBlngResMDto rawBlngResMDto) {

		RawBlngResMDto raw = commonDao.select("Ui-resource.retrieveBlngResM", rawBlngResMDto);
		GeneralBlngResMDto general = new GeneralBlngResMDto(raw);
		AdditionalBlngResMDto additional = new AdditionalBlngResMDto(raw);

		return GroupedBlngResMDto.builder().general(general).additional(additional).build();
	}

	public List<Item> getAllItem(ItemReq itemReq) {

		return commonDao.selectList("Ui-resource.getAllItem", itemReq);
	}

	public ResourceCreateInfoRes getResourceForm(String itemCode) {
		Map<String, String> param = Collections.singletonMap(ITEM_CODE, itemCode);

		ResourceCreateInfoRes response = new ResourceCreateInfoRes();
		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-item.retrieveAdditional", param);
		uiTableService.populateTableColumnKeyValues(additional);

		response.setGeneral(general);
		response.setAdditional(additional);

		List<EntityItemRelResDto> entityItemInfos = commonDao.selectList("Ui-multiEntity.retrieveItemRelationInfo",
				param);
		response.setEntityItemInfos(entityItemInfos);
		return response;
	}

	@Transactional
	public Map<String, String> createResource(ResourceCreateReq req) {
		return createResourceTransactional(req);
	}

	@Transactional
	public Map<String, String> createResourceTransactional(ResourceCreateReq req) {
		Map<String, String> generalParams = req.getGeneralParam();
		String objUuid = UuidUtil.generateRandomUUID();
		String objCode = uiCommonService.generateNextItemCode(generalParams);

		generalParams.put(OBJ_UUID, objUuid);
		generalParams.put(OBJ_CODE, objCode);

		int itemMpngResult = commonDao.insert("Ui-item.insertItemMapping", generalParams);
		if (itemMpngResult < 1) {
			throw new RuntimeException("Create Resource general fail");
		}

		List<AdditionalDetailDto> additional = req.getAdditional();
		if (additional != null) {
			additional.forEach(add -> add.setObjUuid(objUuid));
			saveAdditional(additional);
		}

		uiMultiEntityService.insertObjRelation(objUuid, req.getInsertEntityObjRels());
		Map<String, String> response = new HashMap<>();
		response.put(OBJ_UUID, objUuid);
		response.put("code", objCode);
		return response;
	}

	@Transactional
	public void updateResource(ResourceUpdateReq req) {
		Map<String, String> generalParams = req.getGeneralParam();
		List<AdditionalDetailDto> additional = req.getAdditional();

		// Save history

		long workNo = DateUtil.generateWorkNo();
		String objUuid = generalParams.get(OBJ_UUID);
		String language = null;
		ItemMappingDetailDto oldResource = getResourceDetail(objUuid, language);
		uiHistoryService.saveHistoryGeneral(workNo, generalParams, oldResource);
		uiHistoryService.saveHistoryAdditional(workNo, req, oldResource.getAdditional());

		String endDate = null;
		if (generalParams.containsKey(VALID_END_DTM)) {
			endDate = generalParams.get(VALID_END_DTM);
		}
		if (endDate != null) {
			Date curEndDdate = commonDao.select("Ui-common.getEndDateByObjUUID", generalParams);
			Date reqEndDate = DateUtil.convertToDateByDatabaseFormat(endDate);
			if (curEndDdate == null || curEndDdate.compareTo(reqEndDate) != 0) {
				commonDao.update("Ui-resource.updateResourceComponentRelationDate", generalParams);
			}
		}

		int generalItemMpngResult = commonDao.update("Ui-item.updateItemMapping", generalParams);
		if (generalItemMpngResult < 1) {
			throw new BusinessException("Resource not found!");
		}

		if (additional != null) {
			additional.forEach(add -> add.setObjUuid(objUuid));
			saveAdditional(additional);
		}

		uiMultiEntityService.insertObjRelation(objUuid, req.getInsertEntityObjRels());
		uiMultiEntityService.updateObjRelation(objUuid, req.getUpdateEntityObjRels());
	}

	private void saveAdditional(List<AdditionalDetailDto> fullAdditional) {
		List<AdditionalDetailDto> dmList = fullAdditional.stream().filter(obj -> "DM".equals(obj.getFieldTypeCode()))
				.collect(Collectors.toList());

		List<AdditionalMultiValueDto> multiValList = new ArrayList<>();
		List<AdditionalMultiValueDto> multiDelList = new ArrayList<>();

		for (AdditionalDetailDto obj : dmList) {
			List<AdditionalMultiValueDto> multiValues = AdditionalMultiValueDto.parseVals(obj.getObjUuid(),
					obj.getAttrUuid(), obj.getAttrVal());
			for (AdditionalMultiValueDto additionalMultiValueDto : multiValues) {

			}
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
		commonDao.batchUpdate("Ui-resource.saveResourceAdditional", fullAdditional);

		commonDao.batchDelete("Ui-common.deleteOldAdditionalMultiValue", multiDelList);
		commonDao.batchInsert("Ui-common.insertAdditionalMultiValue", multiValList);
	}

	public PageResult<?> getResources(ResourceSearchReq searchReq) {
		ResourceSearchDto searchDto = resourceMapper.resourceSearchToDto(searchReq);
		searchDto.setOnlyValidDtm(searchReq.isOnlyValidDtm());

		searchDto.setSize(searchReq.getSize());
		searchDto.setPage(searchReq.getPage());

		return commonDao.selectPagedList("Ui-resource.getResourceGeneralForComponentDetail", searchDto);
	}

	public ItemMappingDetailDto getResourceDetail(String objUuid, String language) {
		Map<String, String> param = new HashMap<>();
		param.put(OBJ_UUID, objUuid);
		if (language != null) {
			param.put(LANG_CODE, language);
		}

		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-resource.getResourceDetailAdditional", param);
		uiTableService.populateTableColumnKeyValues(additional);

		ItemMappingDetailDto response = new ItemMappingDetailDto();
		response.setGeneral(general);
		response.setAdditional(additional);

		return response;
	}

	public PageResult<?> retrieveResourcesAdvanced(SearchAdvancedReq searchAdvancedReq) {
		return commonDao.selectPagedList("Ui-resource.retrieveResourcesAdvanced", searchAdvancedReq);
	}

	public PageResult<?> retrieveResourcesAdvancedDetail(SearchAdvancedReq searchAdvancedReq) {
		return commonDao.selectPagedList("Ui-resource.retrieveResourcesAdvancedDetail", searchAdvancedReq);
	}

	public void export(SearchAdvancedReq req, HttpServletResponse response) {
		List<ResoureSearchRes> resources = commonDao.selectList("Ui-resource.retrieveResourcesAdvanced", req);
		List<String> objUuids = resources.stream().map(ResoureSearchRes::getObjUuid).toList();
		Map<String, Object> param = Map.of("objUuids", objUuids, LANG_CODE, req.getLanguage());

		List<GeneralDetailDto> generals = commonDao.selectList("Ui-item.retrieveGenerals", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-resource.getResourceDetailAdditionals", param);

		Map<String, List<GeneralDetailDto>> generalMap = generals.parallelStream().filter(g -> g.getObjUuid() != null)
				.collect(Collectors.groupingBy(GeneralDetailDto::getObjUuid));

		Map<String, List<AdditionalDetailDto>> additionalMap = additional.parallelStream()
				.filter(a -> a.getObjUuid() != null).collect(Collectors.groupingBy(AdditionalDetailDto::getObjUuid));

		resources.forEach(offer -> {
			String objUuid = offer.getObjUuid();
			offer.setGeneral(generalMap.getOrDefault(objUuid, List.of()));
			offer.setAdditional(additionalMap.getOrDefault(objUuid, List.of()));
		});

		List<ResourceExportDto> results = IntStream.range(0, resources.size()).parallel().mapToObj(i -> {
			ResourceExportDto exportDto = resourceMapper.resourceToExportDto(resources.get(i));
			exportDto.setIndex(i + 1);
			return exportDto;
		}).toList();

		ItemMappingDetailDto createInfo = getResourceCreateInfo(req.getItemCode(), req.getLanguage());
		ResourceExportDto header = new ResourceExportDto();
		header.setGeneral(createInfo.getGeneral());
		header.setAdditional(createInfo.getAdditional());
		List<MultiEntityExportDto> multiEntities = new ArrayList<>();
		if (req.getItemCode().equalsIgnoreCase("SE")) {
			for (String uuid: objUuids) {
				List<EntityItemRelResDto> dtos = commonDao.selectList("Ui-multiEntity.retrieveItemRelationExcel", uuid);
				for (EntityItemRelResDto entityItemRelResDto: dtos) {
					if (entityItemRelResDto.getMultiEntityExportDtos() != null && !entityItemRelResDto.getMultiEntityExportDtos().isEmpty()) {
						multiEntities.addAll(entityItemRelResDto.getMultiEntityExportDtos());
					}
				}
			}
			if (multiEntities != null && !multiEntities.isEmpty()) {
				int i = 0;
				for (MultiEntityExportDto multiEntityExportDto: multiEntities) {
					multiEntityExportDto.setNo(i + 1);
					i++;
				}
			} else {
				multiEntities = null;
			}
		}
		Item item = commonDao.select("Ui-item.getItemByItemCode", req.getItemCode());
		excelHelper.downloadExcel(buildExcelInput(results, header, req.getItemCode(), multiEntities, item.getItemName()), response, false);
	}
	
	private ExcelInput buildExcelInput(List<ResourceExportDto> datas, ResourceExportDto header, String itemCode, List<MultiEntityExportDto> multiEntities, String itemName) {
		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName("resource");
		excelInput.setSheetName("Resource");
		excelInput.setFormatDate(DateConstant.YYYYMMDD);
		excelInput.setDatas(datas);
		Object[] objectArray = new Object[4];
        objectArray[0] = header;
        objectArray[1] = multiEntities;
        objectArray[2] = itemCode;
        objectArray[3] = itemName;
		excelInput.setObject(objectArray);
		return excelInput;
	} 
	
	public OfferCreateInfo getResourceCreateInfo(String itemCode, String langCode) {
		Map<String, String> param = Map.of(ITEM_CODE, itemCode, LANG_CODE, langCode);

		OfferCreateInfo response = new OfferCreateInfo();
		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-item.retrieveAdditional", param);
		
		response.setGeneral(general);
		response.setAdditional(additional);

		return response;
	}
}
