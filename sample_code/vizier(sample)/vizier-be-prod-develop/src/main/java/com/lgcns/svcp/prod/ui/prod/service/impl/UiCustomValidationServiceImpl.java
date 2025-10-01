package com.lgcns.svcp.prod.ui.prod.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.lgcns.svcp.prod.context.RequestContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.entity.CustomValidationExcelEntity;
import com.lgcns.svcp.prod.entity.MiddleItemEntity;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.mapper.CustomValidationExcelMapper;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.AttributeDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.AttributeMultipleDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.AttributeNewDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.BoxAndAttrRequest;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.BoxAndAttrRespone;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CellBoxDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustValidAttrHistoryDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustValidValHistoryDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationComponentItem;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationExcelDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationItemResponse;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationMainRespone;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationPagingResponse;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationRestDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.MultipleAttrDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.SaveCusSearchDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.SearchValidation;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.history.AttributeChangeDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.history.HistoryChangeDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.history.HistoryChangeResDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.history.HistoryResponse;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.history.ValueChangeDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.detail.EventDateDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.Item;
import com.lgcns.svcp.prod.ui.prod.service.UiCustomValidationService;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.CustomValidationExcelHelper;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UiCustomValidationServiceImpl implements UiCustomValidationService {
	
	@Value("${external.api.comm}")
    private String commUrl;

	@Autowired
	private RestTemplate restTemplate;
	
	private final CommonDao commonDao;
	private final CustomValidationExcelMapper customValidationExcelMapper;
	private final CustomValidationExcelHelper excelHelper;
		
	@Override
	public List<CustomValidationMainRespone> getListMain(String item, String type, String subType, String attrUuid) {
		List<CustomValidationMainRespone> results = new ArrayList<>();
		Map<String, Object> paramsGetList = new HashMap<>();
		if (item.equals("O") || item.equals("R")) {
			paramsGetList.put("itemCode", type);
		} else if (item.equals("C")) {
			paramsGetList.put("itemCode", subType);
		}
		List<AttributeDto> entities = null;
		if (StringUtils.isNotBlank(attrUuid)) {
			paramsGetList.put("attrUuid", attrUuid);
			entities = commonDao.selectList("ui-cust-valid-m.findByProperties", paramsGetList);
			for (AttributeDto item1: entities) {
				CustomValidationMainRespone result = new CustomValidationMainRespone();
				List<AttributeDto> listAttrUuid = new ArrayList<>();
				int conditionCount = 0;
				int actionCount = 0;
				Map<String, Object> params = new HashMap<>();
				params.put("validCode", item1.getValidCode());
				params.put("attrUuid", item1.getAttrUuid());
				AttributeDto attributeEndValid = commonDao.select("ui-cust-valid-val-d.findAttributeEndValid", params);
				if (attributeEndValid != null) {
					List<AttributeDto> attributeDtos = commonDao.selectList("ui-cust-valid-val-d.findAttributeEndValidMain", attributeEndValid.getValidCode());
					for (AttributeDto attributeDto: attributeDtos) {
						if (StringUtils.isNotBlank(attributeDto.getFieldTypeCode()) && (attributeDto.getFieldTypeCode().equals("DM") || attributeDto.getFieldTypeCode().equals("DL"))) {
							Map<String, Object> paramsMultiple = new HashMap<>();
							paramsMultiple.put("validCode", attributeDto.getValidCode());
							paramsMultiple.put("attrUuid", attributeDto.getAttrUuid());
							List<String> multipleAttr = commonDao.selectList("ui-cust-valid-multi-val-d.find", paramsMultiple);
							if (multipleAttr != null && !multipleAttr.isEmpty()) {
								attributeDto.setMultipleValues(multipleAttr.toArray(new String[] {}));
							}
						}
						if (StringUtils.isNotBlank(attributeDto.getCondType()) && attributeDto.getCondType().equals("A")) {
							if (StringUtils.isNotBlank(attributeDto.getActionItemCode())) {
								Map<String, Object> yyy = new HashMap<>();
								yyy.put("itemCode", attributeDto.getActionItemCode());
								MiddleItemEntity entity = commonDao.select("Ui-item.getMiddleItem", yyy);
								attributeDto.setItemCodeName(entity.getItemCodeName());
								attributeDto.setLargeItemCode(entity.getLctgrItemCode());
							}
						}
						if (attributeDto.getCondType().equals("C")) {
							conditionCount++;
						}
						if (attributeDto.getCondType().equals("A")) {
							actionCount++;
						}
						listAttrUuid.add(attributeDto);
					}
					if (conditionCount != 0 && actionCount != 0) {
						result.setValidCode(item1.getValidCode());
						result.setSeqNo(item1.getSeqNo());
						result.setDatas(listAttrUuid);
						results.add(result);
					}
				}
			}
		} else {
			entities = commonDao.selectList("ui-cust-valid-m.findByProperties", paramsGetList);
			if (entities != null && !entities.isEmpty()) {
				Map<String, List<AttributeDto>> mapEn = entities.stream().collect(Collectors.groupingBy(AttributeDto::getValidCode));
				List<String> validCodes = new ArrayList<>();
				for (Map.Entry<String, List<AttributeDto>> entry : mapEn.entrySet()) {
				    String validCode = entry.getKey();
				    validCodes.add(validCode);
				}
				Map<String, Object> paramsValidCode = new HashMap<>();
				paramsValidCode.put("validCodes", validCodes);
				List<AttributeDto> entitiesV2 = commonDao.selectList("ui-cust-valid-m.findByPropertiesByValidCode", paramsValidCode);
				Map<String, List<AttributeDto>> mapEntities = entitiesV2.stream().collect(Collectors.groupingBy(AttributeDto::getValidCode));
				for(Map.Entry<String, List<AttributeDto>> entry : mapEntities.entrySet()) {
					CustomValidationMainRespone result = new CustomValidationMainRespone();
					List<AttributeDto> listAttrUuid = new ArrayList<>();
					int conditionCount = 0;
					int actionCount = 0;
					for (AttributeDto attributeDto: entry.getValue()) {
						if (StringUtils.isNotBlank(attributeDto.getFieldTypeCode()) && (attributeDto.getFieldTypeCode().equals("DM") || attributeDto.getFieldTypeCode().equals("DL"))) {
							Map<String, Object> paramsMultiple = new HashMap<>();
							paramsMultiple.put("validCode", attributeDto.getValidCode());
							paramsMultiple.put("attrUuid", attributeDto.getAttrUuid());
							List<String> multipleAttr = commonDao.selectList("ui-cust-valid-multi-val-d.find", paramsMultiple);
							if (multipleAttr != null && !multipleAttr.isEmpty()) {
								attributeDto.setMultipleValues(multipleAttr.toArray(new String[] {}));
							}
						}
						if (StringUtils.isNotBlank(attributeDto.getCondType()) && attributeDto.getCondType().equals("A")) {
							Map<String, Object> yyy = new HashMap<>();
							yyy.put("itemCode", attributeDto.getActionItemCode());
							MiddleItemEntity entity = commonDao.select("Ui-item.getMiddleItem", yyy);
							attributeDto.setItemCodeName(entity.getItemCodeName());
							attributeDto.setLargeItemCode(entity.getLctgrItemCode());
						}
						Map<String, Object> params = new HashMap<>();
						params.put("validCode", attributeDto.getValidCode());
						params.put("attrUuid", attributeDto.getAttrUuid());
						AttributeDto attributeEndValid = commonDao.select("ui-cust-valid-val-d.findAttributeEndValid", params);
						if (attributeEndValid != null) {
							if (attributeEndValid.getCondType().equals("C")) {
								conditionCount++;
							}
							if (attributeEndValid.getCondType().equals("A")) {
								actionCount++;
							}
							listAttrUuid.add(attributeDto);
						}
					}
					if (conditionCount != 0 && actionCount != 0) {
						result.setValidCode(entry.getKey());
						result.setSeqNo(entry.getValue().get(0).getSeqNo());
						result.setDatas(listAttrUuid);
						results.add(result);
					}
				}
			}
			
		}
		results = results.stream().sorted(Comparator.comparingInt(CustomValidationMainRespone::getSeqNo)).toList();
		return results;
	}
	
	@Override
	public BoxAndAttrRespone getListAdmin(String item, String type, String subType, String action) {
		BoxAndAttrRespone result = new BoxAndAttrRespone();
		List<AttributeNewDto> attrConditions = new ArrayList<>();
		List<AttributeNewDto> attrActions = new ArrayList<>();
		List<SaveCusSearchDto> validations = new ArrayList<>();
		String mctgrItemCode = null;
		String itemCode = null;
		if (item.equals("O") || item.equals("R")) {
			itemCode = type;
		} else if (item.equals("C")) {
			mctgrItemCode = type;
			itemCode = subType;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("lctgrItemCode", item);
		params.put("mctgrItemCode", mctgrItemCode);
		params.put("itemCode", itemCode);
		if (action.equals("condition")) {
			Map<String, Object> paramAttributes = new HashMap<>();
			paramAttributes.put("itemCode", itemCode);
			List<AttributeDto> attrValidations = commonDao.selectList("ui-cust-valid-m.findByPropertiesAdmin", paramAttributes);
			Map<String, List<AttributeDto>> mapEntities = attrValidations.stream().collect(Collectors.groupingBy(AttributeDto::getValidCode));
			for(Map.Entry<String, List<AttributeDto>> entry : mapEntities.entrySet()) { 
				if (!entry.getValue().isEmpty()) {
					for (AttributeDto attributeDto: entry.getValue()) {
						if (StringUtils.isNotBlank(attributeDto.getCondType()) && attributeDto.getCondType().equals("A")) {
							Map<String, Object> paramsSearch = new HashMap<>();
							paramsSearch.put("itemCode", attributeDto.getActionItemCode());
							MiddleItemEntity entity = commonDao.select("Ui-item.getMiddleItem", paramsSearch);
							attributeDto.setItemCodeName(entity.getItemCodeName());
							attributeDto.setLargeItemCode(entity.getLctgrItemCode());
						}
						if (StringUtils.isNotBlank(attributeDto.getFieldTypeCode()) && (attributeDto.getFieldTypeCode().equals("DM") || attributeDto.getFieldTypeCode().equals("DL"))) {
							Map<String, Object> paramsMultiple = new HashMap<>();
							paramsMultiple.put("validCode", attributeDto.getValidCode());
							paramsMultiple.put("attrUuid", attributeDto.getAttrUuid());
							List<String> multipleAttr = commonDao.selectList("ui-cust-valid-multi-val-d.find", paramsMultiple);
							if (multipleAttr != null && !multipleAttr.isEmpty()) {
								attributeDto.setMultipleValues(multipleAttr.toArray(new String[] {}));
							}
						}
					}
				}
				SaveCusSearchDto boxAndAttrRequest = new SaveCusSearchDto();
				CellBoxDto cellBoxDto = commonDao.select("ui-cust-valid-m.findById", entry.getKey());
				boxAndAttrRequest.setValidCode(cellBoxDto.getValidCode());
				boxAndAttrRequest.setCondItemCode(cellBoxDto.getCondItemCode());
				boxAndAttrRequest.setSeqNo(cellBoxDto.getSeqNo());
				boxAndAttrRequest.setValidCntn(cellBoxDto.getValidCntn());
				boxAndAttrRequest.setValidEndDtm(cellBoxDto.getValidEndDtm());
				boxAndAttrRequest.setValidStartDtm(cellBoxDto.getValidStartDtm());
				if (!entry.getValue().isEmpty() && entry.getValue().get(0).getAttrUuid() != null) {
					boxAndAttrRequest.setAttributes(entry.getValue());
				}
				validations.add(boxAndAttrRequest);
			}
			params.put("useYn", "Y");
			attrConditions = commonDao.selectList("ui-add-attr-header-m.findByProperties", params);
			for (AttributeNewDto attributeDtoCondition: attrConditions) {
				List<String> types = new ArrayList<>();
				Map<String, Object> paramsCondition = new HashMap<>();
				paramsCondition.put("condType", "C");
				paramsCondition.put("attrUuid", attributeDtoCondition.getAttrUuid());
				int isExistsCondition = commonDao.select("ui-cust-valid-val-d.countByCondTypeAndAttribute", paramsCondition);
				Map<String, Object> paramsAction = new HashMap<>();
				paramsAction.put("condType", "A");
				paramsAction.put("attrUuid", attributeDtoCondition.getAttrUuid());
				int isExistsAction = commonDao.select("ui-cust-valid-val-d.countByCondTypeAndAttribute", paramsAction);
				if (isExistsCondition > 0) {
					types.add("C");
				}
				if (isExistsAction > 0) {
					types.add("A");
				}
				attributeDtoCondition.setTypes(types.toArray(new String[] {}));
			}
		} else if (action.equals("action")) {
			params.put("useYn", "Y");
			attrActions = commonDao.selectList("ui-add-attr-header-m.findByProperties", params);
			for (AttributeNewDto attributeDtoAction: attrActions) {
				List<String> types = new ArrayList<>();
				Map<String, Object> paramsCondition = new HashMap<>();
				paramsCondition.put("condType", "C");
				paramsCondition.put("attrUuid", attributeDtoAction.getAttrUuid());
				int isExistsCondition = commonDao.select("ui-cust-valid-val-d.countByCondTypeAndAttribute", paramsCondition);
				Map<String, Object> paramActions = new HashMap<>();
				paramActions.put("condType", "A");
				paramActions.put("attrUuid", attributeDtoAction.getAttrUuid());
				int isExistsAction = commonDao.select("ui-cust-valid-val-d.countByCondTypeAndAttribute", paramActions);
				if (isExistsCondition > 0) {
					types.add("C");
				}
				if (isExistsAction > 0) {
					types.add("A");
				}
				attributeDtoAction.setTypes(types.toArray(new String[] {}));
			}
		}
		result.setAttrConditions(attrConditions);
		result.setAttrActions(attrActions);
		result.setValidations(validations);
		return result;
	}

	@Override
	@Transactional
	public synchronized List<SaveCusSearchDto> save(@Valid BoxAndAttrRequest boxAndAttrRequest) {		
		return saveWithSynchronized(boxAndAttrRequest);		
	}
	
	@SuppressWarnings("unchecked")
	private List<SaveCusSearchDto> saveWithSynchronized(@Valid BoxAndAttrRequest boxAndAttrRequest) {
		List<SaveCusSearchDto> validations = new ArrayList<>();
		List<String> boxCreates = new ArrayList<>();
		String item = boxAndAttrRequest.getItem();
		String type = boxAndAttrRequest.getType();
		String mctgrItemCode = null;
		String itemCode = null;
		if (item.equals("O") || item.equals("R")) {
			itemCode = type;
		} else if (item.equals("C")) {
			mctgrItemCode = type;
			itemCode = boxAndAttrRequest.getSubType();
		} 
		Map<String, Object> params = new HashMap<>();
		params.put("lctgrItemCode", item);
		params.put("mctgrItemCode", mctgrItemCode);
		params.put("itemCode", itemCode);
		
		//start check duplicate seq
		List<CellBoxDto> existCellBoxs = commonDao.selectList("ui-cust-valid-m.findByItemCode", itemCode);
		for (CellBoxDto cellBoxDto: existCellBoxs) {
			for (SaveCusSearchDto request: boxAndAttrRequest.getDatas()) {
				if (request.getValidCode() != null && request.getValidCode().equals(cellBoxDto.getValidCode())) {
					cellBoxDto.setSeqNo(request.getSeqNo());
				}
			}
		}
		for (SaveCusSearchDto request: boxAndAttrRequest.getDatas()) {
			if (request.getValidCode() == null) {
				CellBoxDto newCellBoxDto = new CellBoxDto();
				newCellBoxDto.setSeqNo(request.getSeqNo());
				existCellBoxs.add(newCellBoxDto);
			}
		}
		Set<Integer> checkSeq = new HashSet<>();
		if (existCellBoxs != null && !existCellBoxs.isEmpty()) {
			for (CellBoxDto dto : existCellBoxs) {
				checkSeq.add(dto.getSeqNo());           
		    }
		}
		if (checkSeq.size() < existCellBoxs.size()) {
			throw new BusinessException("error seq! Please Reload page");
		}
		//end check duplicate seq
		
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		for (SaveCusSearchDto request: boxAndAttrRequest.getDatas()) {
			request.setCondItemCode(itemCode);
			if (StringUtils.isNotBlank(request.getValidCode())) {
				CellBoxDto boxDto = new CellBoxDto();
				boxDto.setValidCode(request.getValidCode());
				boxDto.setSeqNo(request.getSeqNo());
				boxDto.setValidCntn(request.getValidCntn());
				boxDto.setValidStartDtm(request.getValidStartDtm());
				boxDto.setValidEndDtm(request.getValidEndDtm());
				
				//check box enabled or expired
				boolean isEnabledOrExpired = false;
				CellBoxDto existBox = commonDao.select("ui-cust-valid-m.findById", request.getValidCode());
				Date validEndDateBox = existBox.getValidEndDtmOrigin();
				//check enabled box
				if (validEndDateBox != null && validEndDateBox.compareTo(currentDate) <= 0) {
					if (request.getValidEndDtm() == null) {
						isEnabledOrExpired = true;
					} else {
						Date validEndDtmInput = DateUtil.convertToDateByDatabaseFormat(request.getValidEndDtm());
						if (validEndDtmInput.compareTo(currentDate) > 0) {
							isEnabledOrExpired = true;
						}
					}
				}
				//check expired box
				if (validEndDateBox == null || validEndDateBox.compareTo(currentDate) > 0) {
					if (request.getValidEndDtm() != null) {
						Date validEndDtmInput = DateUtil.convertToDateByDatabaseFormat(request.getValidEndDtm());						
						if (validEndDtmInput.compareTo(currentDate) <= 0) {
							isEnabledOrExpired = true;
						}						
					}
				}
				commonDao.update("ui-cust-valid-m.update", boxDto);
				//start update attr in box
				if (!isEnabledOrExpired) {
					if (request.getAttributes() != null && !request.getAttributes().isEmpty()) {
						List<AttributeDto> attributes = request.getAttributes();
						List<AttributeDto> attributeInserts = new ArrayList<>();
						List<AttributeMultipleDto> attrMultiples = new ArrayList<>();
						List<CustValidAttrHistoryDto> attrHistoryDtos = new ArrayList<>();
						List<CustValidValHistoryDto> custValidValHistories = new ArrayList<>();
						for (AttributeDto attributeDto: attributes) {
							attributeDto.setValidCode(request.getValidCode());
							if (attributeDto.getCondType().equals("A")) {
								Map<String, Object> paramAttrAction = new HashMap<>();
								paramAttrAction.put("attrUuid", attributeDto.getAttrUuid());
								AttributeNewDto attributeNewDto = commonDao.select("ui-add-attr-header-m.findByProperties", paramAttrAction);
								attributeDto.setActionItemCode(attributeNewDto.getItemCode());
							} else {
								attributeDto.setActionItemCode(request.getCondItemCode());
							}
							if (attributeDto.getRangeStartDtmStr() != null && attributeDto.getRangeStartDtmStr().equals("")) {
								attributeDto.setRangeStartDtmStr(null);
							}
							if (attributeDto.getRangeEndDtmStr() != null && attributeDto.getRangeEndDtmStr().equals("")) {
								attributeDto.setRangeEndDtmStr(null);
							}
							Map<String, Object> mapCounts = new HashMap<>();
							mapCounts.put("validCode", request.getValidCode());
							mapCounts.put("attrUuid", attributeDto.getAttrUuid());
							AttributeDto isExists = commonDao.select("ui-cust-valid-val-d.findByValidCodeAndAttrUuid", mapCounts);
							if (isExists == null) {
								//build data attr history
								AttributeDto existsAttr = attributeDto;
								CustValidAttrHistoryDto rowAttr = buildAttrHistory(existsAttr, attributeDto);
								rowAttr.setWorkNo(Long.valueOf(DateUtil.generateWorkNo()).toString());
								rowAttr.setWorkTypeCode("01");
								attrHistoryDtos.add(rowAttr);
								attributeInserts.add(attributeDto);
							} else {
								//build data history								
								Date validEndDate = isExists.getValidEndDtmOrigin();					
								//save history enabled
								if (validEndDate != null && validEndDate.compareTo(currentDate) <= 0) { 
									if (attributeDto.getValidEndDtm() == null) {
										//process
										CustValidAttrHistoryDto rowAttr = buildAttrHistory(isExists, attributeDto);
										rowAttr.setWorkNo(Long.valueOf(DateUtil.generateWorkNo()).toString());
										rowAttr.setWorkTypeCode("02");
										attrHistoryDtos.add(rowAttr);
									} else {
											Date validEndDtmInput = DateUtil.convertToDateByDatabaseFormat(attributeDto.getValidEndDtm());
											if (validEndDtmInput.compareTo(currentDate) > 0) {
												//process
												CustValidAttrHistoryDto rowAttr = buildAttrHistory(isExists, attributeDto);
												rowAttr.setWorkNo(Long.valueOf(DateUtil.generateWorkNo()).toString());
												rowAttr.setWorkTypeCode("02");
												attrHistoryDtos.add(rowAttr);
											}										
									}
								}
								//save history expired
								if (validEndDate == null || validEndDate.compareTo(currentDate) > 0) {								
									if (attributeDto.getValidEndDtm() != null) {
										CustValidAttrHistoryDto rowAttr = buildAttrHistory(isExists, attributeDto);
										rowAttr.setWorkNo(Long.valueOf(DateUtil.generateWorkNo()).toString());
										rowAttr.setWorkTypeCode("03");
										attrHistoryDtos.add(rowAttr);
									}
								}
								//save history change																
								String fieldTypeCode = attributeDto.getFieldTypeCode();
								if (fieldTypeCode.equals("TF") || fieldTypeCode.equals("TA")) {
									if (StringUtils.isNotBlank(attributeDto.getTextCntn())) {
										if (!attributeDto.getTextCntn().equals(isExists.getTextCntn())) {
											updateHistory(isExists, attributeDto, custValidValHistories, isExists.getTextCntn(), attributeDto.getTextCntn());
										}
									}
								}								
								if (fieldTypeCode.equals("NF") || fieldTypeCode.equals("RF")) {
									boolean isChange = false;
									List<Integer> existValues = new ArrayList<>();
									Integer existRangeStartVal = isExists.getRangeStartVal();
									Integer existRangeEndVal = isExists.getRangeEndVal();
									if (existRangeStartVal != null) {
										existValues.add(existRangeStartVal);
									}
									if (existRangeEndVal != null) {
										existValues.add(existRangeEndVal);
									}									
									List<Integer> newValues = new ArrayList<>();
									Integer newRangeStartVal = attributeDto.getRangeStartVal();
									Integer newRangeEndVal = attributeDto.getRangeEndVal();
									if (newRangeStartVal != null) {
										newValues.add(newRangeStartVal);
									}
									if (newRangeEndVal != null) {
										newValues.add(newRangeEndVal);
									}
									if (existRangeStartVal != null) {
										if (newRangeStartVal != null && newRangeStartVal.compareTo(existRangeStartVal) != 0) {
											isChange = true;
										}
										if (newRangeStartVal == null) {											
											isChange = true;
										}
									} else {
										if (newRangeStartVal != null) {
											isChange = true;
										}
									}
									if (existRangeEndVal != null) {
										if (newRangeEndVal != null && newRangeEndVal.compareTo(existRangeEndVal) != 0) {
											isChange = true;
										}
										if (newRangeEndVal == null) {
											isChange = true;
										}
									} else {
										if (newRangeEndVal != null) {
											isChange = true;
										}
									}
									if (isChange) {										
										String oldValue = null;
										String newValue = null;
										if (newRangeStartVal != null && newRangeEndVal != null) {
											newValue = newValues.stream().map(String::valueOf).collect(Collectors.joining(" ~ "));
										} else {
											if (newRangeStartVal != null) {
												newValue = newRangeStartVal + " ~ ";
											}
											if (newRangeEndVal != null) {
												newValue = " ~ "+newRangeEndVal;
											}
										}
										if (existRangeStartVal != null && existRangeEndVal != null) {
											oldValue = existValues.stream().map(String::valueOf).collect(Collectors.joining(" ~ "));
										} else {
											if (existRangeStartVal != null) {
												oldValue = existRangeStartVal + " ~ ";
											}
											if (existRangeEndVal != null) {
												oldValue = " ~ "+existRangeEndVal;
											}
										}
										updateHistory(isExists, attributeDto, custValidValHistories, oldValue, newValue);
									}
								}
								if (fieldTypeCode.equals("DP")) {
									boolean isChange = false;
									List<String> existValues = new ArrayList<>();
									Date existRangeStartDtm = isExists.getRangeStartDtm();
									Date existRangeEndDtm = isExists.getRangeEndDtm();
									if (existRangeStartDtm != null) {
										String existRangeStartDtmStr = DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM_SS, existRangeStartDtm);
										existValues.add(existRangeStartDtmStr);
									}
									if (existRangeEndDtm != null) {
										String existRangeEndDtmStr = DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM_SS, existRangeEndDtm);
										existValues.add(existRangeEndDtmStr);
									}									
									List<String> newValues = new ArrayList<>();									
									String newRangeStartValStr = attributeDto.getRangeStartDtmStr();
									String newRangeEndValStr = attributeDto.getRangeEndDtmStr();
									if (StringUtils.isNotBlank(newRangeStartValStr)) {										
										newValues.add(newRangeStartValStr);
									}
									if (StringUtils.isNotBlank(newRangeEndValStr)) {										
										newValues.add(newRangeEndValStr);
									}
									if (existRangeStartDtm != null) {
										String existRangeStartDtmStr = DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM_SS, existRangeStartDtm);
										if (StringUtils.isNotBlank(newRangeStartValStr) && !newRangeStartValStr.equals(existRangeStartDtmStr)) {
											isChange = true;
										}
										if (newRangeStartValStr == null || newRangeStartValStr.isEmpty()) {										
											isChange = true;
										}
									} else {
										if (StringUtils.isNotBlank(newRangeStartValStr)) {
											isChange = true;
										}
									}
									if (existRangeEndDtm != null) {
										String existRangeEndDtmStr = DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM_SS, existRangeEndDtm);
										if (StringUtils.isNotBlank(newRangeEndValStr) && !newRangeEndValStr.equals(existRangeEndDtmStr)) {
											isChange = true;
										}
										if (newRangeEndValStr == null || newRangeEndValStr.isEmpty()) {											
											isChange = true;
										}
									} else {
										if (StringUtils.isNotBlank(newRangeEndValStr)) {
											isChange = true;
										}
									}
									if (isChange) {										
										String oldValue = null;
										String newValue = null;
										if (StringUtils.isNotBlank(newRangeStartValStr) && StringUtils.isNotBlank(newRangeEndValStr)) {
											newValue = StringUtils.join(newValues, " ~ ");
										} else {
											if (StringUtils.isNotBlank(newRangeStartValStr)) {
												newValue = newRangeStartValStr + " ~ ";
											}
											if (StringUtils.isNotBlank(newRangeEndValStr)) {
												newValue = " ~ "+newRangeEndValStr;
											}
										}										
										if (existRangeStartDtm != null && existRangeEndDtm != null) {
											oldValue = StringUtils.join(existValues, " ~ ");
										} else {
											if (existRangeStartDtm != null) {
												String existRangeStartDtmStr = DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM_SS, existRangeStartDtm);
												oldValue = existRangeStartDtmStr + " ~ ";
											}
											if (existRangeEndDtm != null) {
												String existRangeEndDtmStr = DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM_SS, existRangeEndDtm);
												oldValue = " ~ "+existRangeEndDtmStr;
											}
										}
										updateHistory(isExists, attributeDto, custValidValHistories, oldValue, newValue);
									}
								}
								//DL, DM
								if (fieldTypeCode.equals("DL") || fieldTypeCode.equals("DM")) {
									boolean isChange = false;
									List<String> multipleNewAttr = new ArrayList<>();
									List<String> multipleNewAttrV2 = new ArrayList<>();
									if (attributeDto.getMultipleValues().length > 0) {
										multipleNewAttr = Arrays.asList(attributeDto.getMultipleValues());										
									}
									Map<String, Object> paramsMultiple = new HashMap<>();
									paramsMultiple.put("validCode", attributeDto.getValidCode());
									paramsMultiple.put("attrUuid", attributeDto.getAttrUuid());
									List<String> multipleExistAttr = commonDao.selectList("ui-cust-valid-multi-val-d.find", paramsMultiple);
									List<String> multipleExistAttrV2 = new ArrayList<>();
									for (String multipleItem: multipleExistAttr) {
										if (!multipleNewAttr.contains(multipleItem)) {
											isChange = true;
										}
									}
									for (String multipleItem: multipleNewAttr) {
										if (!multipleExistAttr.contains(multipleItem)) {
											isChange = true;
										}
									}
									
									List<MultipleAttrDto> multipleAttr = commonDao.selectList("ui-cust-valid-multi-val-d.findMultiplaAttr", paramsMultiple);								
									if (multipleAttr != null && !multipleAttr.isEmpty()) {
										String groupCode = multipleAttr.get(0).getCommGroupCode();
//										RestTemplate restTemplate = new RestTemplate();
										String fooResourceUrl = commUrl+"/comm/cmcd/v1/cmcdCmcdGrpIdSearch";
										CustomValidationRestDto customValidationRestDto = new CustomValidationRestDto();
										String[] arrays = new String[] {groupCode}; 
										customValidationRestDto.setCmcdGrpIds(arrays);
										HttpEntity<CustomValidationRestDto> httpRequestCustomValidation = new HttpEntity<>(customValidationRestDto);
										Map<String, Object> mapObject = (Map<String, Object>) restTemplate.postForObject(fooResourceUrl, httpRequestCustomValidation, Object.class);
										Map<String, Object> data = null; 
										if (mapObject.get("data") != null) {
											data = (Map<String, Object>) mapObject.get("data");
										}
										List<Map<String, String>> cmcdGrpDTOs = null;
										Map<String, String> cmcdGrpDTO = null;
										if (data != null && data.get(groupCode) != null) {
											try {
												cmcdGrpDTOs =  (List<Map<String, String>>) data.get(groupCode);
											} catch (Exception e) {												
												cmcdGrpDTO = (Map<String, String>) data.get(groupCode);
											}						
										}																													
										for (String mulExistAttr: multipleExistAttr) {											
											if (cmcdGrpDTOs != null) {
												for (Map<String, String> mapAbc: cmcdGrpDTOs) {
													String cmcdDetlId = mapAbc.get("cmcdDetlId");
													if (mulExistAttr.equals(cmcdDetlId)) {
														multipleExistAttrV2.add(mapAbc.get("cmcdDetlNm"));
													}
												}
											} else {
												if (cmcdGrpDTO != null) {
													String cmcdDetlId = cmcdGrpDTO.get("cmcdDetlId");
													if (mulExistAttr.equals(cmcdDetlId)) {
														multipleExistAttrV2.add(cmcdGrpDTO.get("cmcdDetlNm"));
													}
												}
											}
										}																				
									}
									if (attributeDto.getMultipleValues().length > 0) {
										AttributeDto attribute = commonDao.select("ui-cust-valid-m.getCommGroupCode", attributeDto.getAttrUuid());
										String groupCode = attribute.getCommGroupCode();
//										RestTemplate restTemplate = new RestTemplate();
										String fooResourceUrl = commUrl+"/comm/cmcd/v1/cmcdCmcdGrpIdSearch";
										CustomValidationRestDto customValidationRestDto = new CustomValidationRestDto();
										String[] arrays = new String[] {groupCode}; 
										customValidationRestDto.setCmcdGrpIds(arrays);
										HttpEntity<CustomValidationRestDto> httpRequestCustomValidation = new HttpEntity<>(customValidationRestDto);
										Map<String, Object> mapObject = (Map<String, Object>) restTemplate.postForObject(fooResourceUrl, httpRequestCustomValidation, Object.class);
										Map<String, Object> data = null; 
										if (mapObject.get("data") != null) {
											data = (Map<String, Object>) mapObject.get("data");
										}
										List<Map<String, String>> cmcdGrpDTOs = null;
										Map<String, String> cmcdGrpDTO = null;
										if (data != null && data.get(groupCode) != null) {
											try {
												cmcdGrpDTOs =  (List<Map<String, String>>) data.get(groupCode);
											} catch (Exception e) {												
												cmcdGrpDTO = (Map<String, String>) data.get(groupCode);
											}						
										}																			
										for (String mulNewAttr: multipleNewAttr) {											
											if (cmcdGrpDTOs != null) {
												for (Map<String, String> mapAbc: cmcdGrpDTOs) {
													String cmcdDetlId = mapAbc.get("cmcdDetlId");
													if (mulNewAttr.equals(cmcdDetlId)) {
														multipleNewAttrV2.add(mapAbc.get("cmcdDetlNm"));
													}
												}
											} else {
												if (cmcdGrpDTO != null) {
													String cmcdDetlId = cmcdGrpDTO.get("cmcdDetlId");
													if (mulNewAttr.equals(cmcdDetlId)) {
														multipleNewAttrV2.add(cmcdGrpDTO.get("cmcdDetlNm"));
													}
												}
											}
										}										
									}
									if (isChange) {
										String oldValue = StringUtils.join(multipleExistAttrV2, ",");
										String newValue = StringUtils.join(multipleNewAttrV2, ",");										
										updateHistory(isExists, attributeDto, custValidValHistories, oldValue, newValue);
									}
								}
							}
							if (attributeDto.getFieldTypeCode().equals("DM") || attributeDto.getFieldTypeCode().equals("DL")) {
								Map<String, Object> paramsDeleteFieldDm = new HashMap<>();
								paramsDeleteFieldDm.put("validCode", request.getValidCode());
								paramsDeleteFieldDm.put("attrUuid", attributeDto.getAttrUuid());
								commonDao.delete("ui-cust-valid-multi-val-d.delete", paramsDeleteFieldDm);							
								if (attributeDto.getMultipleValues().length > 0) {			
									int seq = createSeqAttrMultipleValue(request.getValidCode(), attributeDto.getAttrUuid());
									for (String multipleValue: attributeDto.getMultipleValues()) {									
										AttributeMultipleDto attributeMultipleDto = new AttributeMultipleDto();
										attributeMultipleDto.setAttrUuid(attributeDto.getAttrUuid());
										attributeMultipleDto.setAttrSeq(seq++);
										attributeMultipleDto.setValidCode(request.getValidCode());
										attributeMultipleDto.setAttrVal(multipleValue);
										attrMultiples.add(attributeMultipleDto);
									}
								}
							}
						}
						commonDao.batchUpdate("ui-cust-valid-val-d.update", attributes);
						commonDao.batchInsert("ui-cust-valid-val-d.insert", attributeInserts);
						if (attrMultiples != null && !attrMultiples.isEmpty()) {
							commonDao.batchInsert("ui-cust-valid-multi-val-d.insert", attrMultiples);
						}
						//save history					
						commonDao.batchInsert("ui-cust-valid-attr-h.insert", attrHistoryDtos);
						commonDao.batchInsert("ui-cust-valid-val-h.insert", custValidValHistories);
						
					}
				}
				//end start update attr in box
			} else {
				String validCode = createValidCode();
				RequestContextHolder.setCode(validCode);
				CellBoxDto boxDto = new CellBoxDto();
				boxDto.setValidCode(validCode);
				boxDto.setCondItemCode(request.getCondItemCode());
				boxDto.setSeqNo(request.getSeqNo());
				boxDto.setValidCntn(request.getValidCntn());
				boxDto.setValidStartDtm(request.getValidStartDtm());
				boxDto.setValidEndDtm(request.getValidEndDtm());
				boxCreates.add(validCode);
				commonDao.insert("ui-cust-valid-m.insert", boxDto);
				if (request.getAttributes() != null && !request.getAttributes().isEmpty()) {
					List<AttributeDto> attributes = request.getAttributes();
					List<AttributeMultipleDto> attrMultiples = new ArrayList<>();
					for (AttributeDto attributeDto: attributes) {
						if (attributeDto.getRangeStartDtmStr() != null && attributeDto.getRangeStartDtmStr().equals("")) {
							attributeDto.setRangeStartDtmStr(null);
						}
						if (attributeDto.getRangeEndDtmStr() != null && attributeDto.getRangeEndDtmStr().equals("")) {
							attributeDto.setRangeEndDtmStr(null);
						}						
						attributeDto.setValidCode(validCode);
						if (attributeDto.getCondType().equals("A")) {
							Map<String, Object> paramAttrAction = new HashMap<>();
							paramAttrAction.put("attrUuid", attributeDto.getAttrUuid());
							AttributeNewDto attributeNewDto = commonDao.select("ui-add-attr-header-m.findByProperties", paramAttrAction);
							attributeDto.setActionItemCode(attributeNewDto.getItemCode());		
						} else {
							attributeDto.setActionItemCode(request.getCondItemCode());
						}
						if (attributeDto.getFieldTypeCode().equals("DM") || attributeDto.getFieldTypeCode().equals("DL")) {
							if (attributeDto.getMultipleValues().length > 0) {
								int seq = createSeqAttrMultipleValue(validCode, attributeDto.getAttrUuid());
								for (String multipleValue: attributeDto.getMultipleValues()) {
									AttributeMultipleDto attributeMultipleDto = new AttributeMultipleDto();
									attributeMultipleDto.setAttrUuid(attributeDto.getAttrUuid());
									attributeMultipleDto.setAttrSeq(seq++);
									attributeMultipleDto.setValidCode(validCode);
									attributeMultipleDto.setAttrVal(multipleValue);
									attrMultiples.add(attributeMultipleDto);
								}
							}
						}
					}
					commonDao.batchInsert("ui-cust-valid-val-d.insert", attributes);
					if (attrMultiples != null && !attrMultiples.isEmpty()) {
						commonDao.batchInsert("ui-cust-valid-multi-val-d.insert", attrMultiples);
					}
				}
			}
		}
		Map<String, Object> paramAttributes = new HashMap<>();
		paramAttributes.put("itemCode", itemCode);
		List<AttributeDto> attrValidations = commonDao.selectList("ui-cust-valid-m.findByPropertiesAdmin", paramAttributes);
		Map<String, List<AttributeDto>> mapEntities = attrValidations.stream().collect(Collectors.groupingBy(AttributeDto::getValidCode));
		for(Map.Entry<String, List<AttributeDto>> entry : mapEntities.entrySet()) { 
			SaveCusSearchDto cusSearchDto = new SaveCusSearchDto();
			CellBoxDto cellBoxDto = commonDao.select("ui-cust-valid-m.findById", entry.getKey());
			cusSearchDto.setValidCode(cellBoxDto.getValidCode());
			cusSearchDto.setCondItemCode(cellBoxDto.getCondItemCode());
			cusSearchDto.setSeqNo(cellBoxDto.getSeqNo());
			cusSearchDto.setValidCntn(cellBoxDto.getValidCntn());
			cusSearchDto.setValidEndDtm(cellBoxDto.getValidEndDtm());
			cusSearchDto.setValidStartDtm(cellBoxDto.getValidStartDtm());
			if (!boxCreates.isEmpty() && boxCreates.contains(cellBoxDto.getValidCode())) {
				cusSearchDto.setCreated(true);
			}
			if (!entry.getValue().isEmpty() && entry.getValue().get(0).getAttrUuid() != null) {
				cusSearchDto.setAttributes(entry.getValue());
			}
			validations.add(cusSearchDto);
		}
		return validations;
		
	}

	private void updateHistory(AttributeDto isExists, AttributeDto attributeDto, List<CustValidValHistoryDto> custValidValHistories, String oldAttrVal, String newAttrVal) {
		Map<String, Object> mapAbc = new HashMap<>();
		mapAbc.put("validCode", isExists.getValidCode());
		mapAbc.put("attrUuid", isExists.getAttrUuid());
		int existHistory = commonDao.select("ui-cust-valid-val-h.countByValidCodeAndAttrUuid", mapAbc);
		if (existHistory == 0) {
			//add 2 row																						
			CustValidValHistoryDto row1 = buildValHistory(isExists);
			row1.setWorkNo(Long.valueOf(DateUtil.generateWorkNo()).toString());
			row1.setAttrVal(oldAttrVal);
			custValidValHistories.add(row1);
			CustValidValHistoryDto row2 = buildValHistory(isExists);
			row2.setWorkNo(Long.valueOf(DateUtil.generateWorkNo() + 1).toString());
			row2.setAttrVal(newAttrVal);
			custValidValHistories.add(row2);
		} else {
			//add 1 row
			CustValidValHistoryDto rowXyz = buildValHistory(isExists);
			rowXyz.setWorkNo(Long.valueOf(DateUtil.generateWorkNo()).toString());
			rowXyz.setAttrVal(newAttrVal);
			custValidValHistories.add(rowXyz);
		}
	}

	private CustValidValHistoryDto buildValHistory(AttributeDto oldAttr) {
		CustValidValHistoryDto result = new CustValidValHistoryDto();
		result.setValidCode(oldAttr.getValidCode());
		result.setCondType(oldAttr.getCondType());
		result.setAttrUuid(oldAttr.getAttrUuid());
		result.setUpdUserDeptName("cns");
		result.setAttrValUpdUser("comm");
		return result;
	}

	private CustValidAttrHistoryDto buildAttrHistory(AttributeDto oldAttr, AttributeDto newAttr) {
		CustValidAttrHistoryDto result = new CustValidAttrHistoryDto();
		result.setValidCode(oldAttr.getValidCode());
		result.setCondType(oldAttr.getCondType());
		result.setItemCode(oldAttr.getActionItemCode());
		result.setAttrUuid(oldAttr.getAttrUuid());
		result.setValidStartDtm(newAttr.getValidStartDtm());
		result.setValidEndDtm(newAttr.getValidEndDtm());
		result.setUpdUserDeptName("cns");
		result.setAttrValUpdUser("comm");
		return result;
	}

	private String createValidCode() {
		String resultStr = "000001";
		List<String> validCodes = commonDao.selectList("ui-cust-valid-m.findValidCodes");
		if (validCodes != null && !validCodes.isEmpty()) {
			int result = Integer.parseInt(validCodes.get(0).substring(4)) + 1;
		    if (result >= 1000000) {
		        throw new BusinessException("Valid code is limit");
		    }
		    resultStr = String.format("%06d", result);
		}
		return "VALD"+resultStr;
	}

	@Override
	public HistoryResponse getHistory(String validCode) {
		CellBoxDto cellBoxDtoCreate = commonDao.select("ui-cust-valid-m.findCellBoxCreate", validCode);
		CellBoxDto cellBoxDtoEnd = commonDao.select("ui-cust-valid-m.findCellBoxEnd", validCode);
		HistoryResponse result = new HistoryResponse();
		
		EventDateDto created = new EventDateDto();
		created.setWorkDate(cellBoxDtoCreate.getValidStartDtm());
		created.setChgUser("comm");
		created.setChgDeptName("cns");
		result.setCreated(created);
		
		EventDateDto ended = new EventDateDto();
		if (cellBoxDtoEnd != null) {
			Calendar calendar = Calendar.getInstance();
			Date currentDate = calendar.getTime();
			if (cellBoxDtoEnd.getValidEndDtmOrigin().compareTo(currentDate) <= 0) {
				ended.setWorkDate(cellBoxDtoEnd.getValidEndDtm());
				ended.setChgUser("comm");
				ended.setChgDeptName("cns");
				result.setEnded(ended);
			}
		}
		List<String> attrHistories = commonDao.selectList("ui-cust-valid-attr-h.findDate", validCode);
		List<String> attrValHistories = commonDao.selectList("ui-cust-valid-val-h.findDate", validCode);
		attrHistories.addAll(attrValHistories);
		List<HistoryChangeDto> changed = new ArrayList<>();
		Map<String, Long> mapAttrs = attrHistories.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
        );
		for(Map.Entry<String, Long> entry : mapAttrs.entrySet()) {
			Map<String, Object> maps = new HashMap<>();
			maps.put("validCode", validCode);
			maps.put("date", entry.getKey());
			List<CustValidAttrHistoryDto> attrHistoryDtos = commonDao.selectList("ui-cust-valid-attr-h.findHistory", maps);
			HistoryChangeDto historyChangeDto = new HistoryChangeDto();
			historyChangeDto.setWorkDate(entry.getKey());
			List<HistoryChangeResDto> changeResDtos = new ArrayList<>();				
			List<AttributeChangeDto> attributes = new ArrayList<>();
			for (CustValidAttrHistoryDto custValidAttrHistoryDto: attrHistoryDtos) {
				AttributeChangeDto attributeChangeDto = new AttributeChangeDto();
				if (custValidAttrHistoryDto.getWorkTypeCode().equals("01")) {
					attributeChangeDto.setWorkTypeCode("product_platform.added");
				} else if (custValidAttrHistoryDto.getWorkTypeCode().equals("02")) {
					attributeChangeDto.setWorkTypeCode("product_platform.enabled");
				} else if (custValidAttrHistoryDto.getWorkTypeCode().equals("03")) {
					attributeChangeDto.setWorkTypeCode("product_platform.expired");
				}
				attributeChangeDto.setCondType(custValidAttrHistoryDto.getCondType());
				attributeChangeDto.setLabelId(custValidAttrHistoryDto.getLabelId());
				attributeChangeDto.setValidCode(custValidAttrHistoryDto.getValidCode());
				attributeChangeDto.setWorkNo(custValidAttrHistoryDto.getWorkNo());
				attributeChangeDto.setItemCodeName(custValidAttrHistoryDto.getItemCodeName());
				attributes.add(attributeChangeDto);
			}
			if (attributes != null && !attributes.isEmpty()) {
				HistoryChangeResDto changeAttribute = new HistoryChangeResDto();
				changeAttribute.setChangeTypeName("Attribute");
				changeAttribute.setChgUser("comm");
				changeAttribute.setChgDeptName("cns");
				changeAttribute.setAttributes(attributes);
				changeResDtos.add(changeAttribute);
			}
			
			Map<String, Object> mapsHistory = new HashMap<>();
			mapsHistory.put("validCode", validCode);
			mapsHistory.put("workNo", entry.getKey());
			List<ValueChangeDto> values = commonDao.selectList("ui-cust-valid-val-h.findHistory", mapsHistory);
			if (values != null && !values.isEmpty()) {
				HistoryChangeResDto changeValue = new HistoryChangeResDto();
				changeValue.setChangeTypeName("Value");
				changeValue.setChgUser("comm");
				changeValue.setChgDeptName("cns");
				changeValue.setValues(values);
				changeResDtos.add(changeValue);
			}
			historyChangeDto.setRecords(changeResDtos);
			changed.add(historyChangeDto);
		}
		List<HistoryChangeDto> newCchanged = changed.stream()
				.sorted(Comparator.comparing(HistoryChangeDto::getWorkDate))
				.collect(Collectors.toList());
		result.setChanged(newCchanged);
		return result;
	}

	@Override
	public CustomValidationPagingResponse getData(int page, int size, String searchBy, String item, String type, String subType, String language) {
		String largeItemCode = null;
		String middleItemCode = null;
		String itemCode = null;
		if (StringUtils.isNotBlank(item)) {
			largeItemCode = item;
			if (item.equals("O") || item.equals("R")) {
				if (StringUtils.isNotBlank(type)) {
					itemCode = type;
				}
			} else if (item.equals("C")) {
				if (StringUtils.isNotBlank(type)) {
					middleItemCode = type;
				}
				if (StringUtils.isNotBlank(subType)) {
					itemCode = subType;
				}
			}
		}
		List<Map<String, Object>> mapResults = new ArrayList<>();
		SearchValidation pagingEntity = new SearchValidation();
		pagingEntity.setItemCode(itemCode);
		pagingEntity.setLargeItemCode(largeItemCode);
		pagingEntity.setMiddleItemCode(middleItemCode);
		pagingEntity.setPage(page);
		pagingEntity.setSize(size);
		List<AttributeDto> boxs = new ArrayList<>();
		int totalItem = 0;
		if (searchBy.equals("C")) {
			PageResult<AttributeDto> pageResult = commonDao.selectPagedList("ui-cust-valid-m.getData", pagingEntity);
			boxs = pageResult.getElements();
			totalItem = (int) pageResult.getTotalElements();
		} else if (searchBy.equals("A")) {
			PageResult<AttributeDto> pageResult = commonDao.selectPagedList("ui-cust-valid-m.findInListViewWithTypeAction", pagingEntity);
			boxs = pageResult.getElements();
			totalItem = (int) pageResult.getTotalElements();
		}
		for (AttributeDto box: boxs) {
			Map<String, Object> maps = new HashMap<>();
			maps.put("validCode", box.getValidCode());
			maps.put("language", language);
			List<CustomValidationExcelEntity> entities = commonDao.selectList("ui-cust-valid-val-d.getData", maps);
			if (entities != null && !entities.isEmpty()) {
				Map<String, Object> results = new HashMap<>();
				List<CustomValidationExcelDto> cntDtos = convertToDto(entities);
				results.put("cellBox", box.getValidCode());
				results.put("rgstUser", box.getRgstUser());
				results.put("rgstDtm", box.getRgstDtm());
				results.put("updUser", box.getUpdUser());
				results.put("updDtm", box.getUpdDtm());
				results.put("data", cntDtos);
				mapResults.add(results);
			}
		}
		return new CustomValidationPagingResponse(totalItem, mapResults);
	}
	
	@SuppressWarnings("unchecked")
	private List<CustomValidationExcelDto> convertToDto(List<CustomValidationExcelEntity> entities) {
		return entities.stream().map(item -> {
			CustomValidationExcelDto dto = customValidationExcelMapper.entityToDto(item);
			String value = null;
			if (item.getFieldTypeCode().equals("DM") || item.getFieldTypeCode().equals("DL")) {
				Map<String, Object> paramsMultiple = new HashMap<>();
				paramsMultiple.put("validCode", item.getValidCode());
				paramsMultiple.put("attrUuid", item.getAttrUuid());
				List<MultipleAttrDto> multipleAttr = commonDao.selectList("ui-cust-valid-multi-val-d.findMultiplaAttr", paramsMultiple);
				List<String> multipleAttrNews = new ArrayList<>();
				if (multipleAttr != null && !multipleAttr.isEmpty()) {
					String groupCode = multipleAttr.get(0).getCommGroupCode();
//					RestTemplate restTemplate = new RestTemplate();
					String fooResourceUrl = commUrl+"/comm/cmcd/v1/cmcdCmcdGrpIdSearch";
					CustomValidationRestDto validationRestDto = new CustomValidationRestDto();
					String[] arrays = new String[] {groupCode}; 
					validationRestDto.setCmcdGrpIds(arrays);
					HttpEntity<CustomValidationRestDto> request = new HttpEntity<>(validationRestDto);
					Map<String, Object> mapObject = (Map<String, Object>) restTemplate.postForObject(fooResourceUrl, request, Object.class);
					Map<String, Object> data = null; 
					if (mapObject.get("data") != null) {
						data = (Map<String, Object>) mapObject.get("data");
					}
					List<Map<String, String>> cmcdGrpDTOs = null;
					Map<String, String> cmcdGrpDTO = null;
					if (data != null && data.get(groupCode) != null) {
						try {
							cmcdGrpDTOs =  (List<Map<String, String>>) data.get(groupCode);							
						} catch (Exception e) {							
							cmcdGrpDTO = (Map<String, String>) data.get(groupCode);
						}						
					}
					for (MultipleAttrDto mulAttr: multipleAttr) {
						if (cmcdGrpDTOs != null) {
							for (Map<String, String> mapAbc: cmcdGrpDTOs) {
								String cmcdDetlId = mapAbc.get("cmcdDetlId");
								if (mulAttr.getAttrVal().equals(cmcdDetlId)) {
									multipleAttrNews.add(mapAbc.get("cmcdDetlNm"));
								}
							}
						} else {
							if (cmcdGrpDTO != null) {
								String cmcdDetlId = cmcdGrpDTO.get("cmcdDetlId");
								if (mulAttr.getAttrVal().equals(cmcdDetlId)) {
									multipleAttrNews.add(cmcdGrpDTO.get("cmcdDetlNm"));
								}
							}
						}
					}
				}
				if (multipleAttrNews != null && !multipleAttrNews.isEmpty()) {
					value = String.join(",", multipleAttrNews);
				}
			} else if (item.getFieldTypeCode().equals("DP")) {
				String startDtm = null;
				String endDtm = null;
				if (item.getRangeStartDtm() != null) {
					startDtm = DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM_SS, item.getRangeStartDtm());
				}
				if (item.getRangeEndDtm() != null) {
					endDtm = DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM_SS, item.getRangeEndDtm());
				}
				if (item.getRangeStartDtm() != null || item.getRangeEndDtm() != null) {
					if (item.getRangeStartDtm() != null && item.getRangeEndDtm() != null) {
						value = startDtm + " ~ " +endDtm;
					} else {
						if (item.getRangeStartDtm() != null) {
							value = startDtm + " ~ ";
						}
						if (item.getRangeEndDtm() != null) {
							value = " ~ "+endDtm;
						}
					}
 				}
			} else if (item.getFieldTypeCode().equals("NF") || item.getFieldTypeCode().equals("RF")) {
				if (item.getRangeStartVal() != null || item.getRangeEndVal() != null) {
					if (item.getRangeStartVal() != null && item.getRangeEndVal() != null) {
						value = item.getRangeStartVal().toString() + " ~ " +item.getRangeEndVal().toString();
					} else {
						if (item.getRangeStartVal() != null) {
							value = item.getRangeStartVal().toString() + " ~ ";
						}
						if (item.getRangeEndVal() != null) {
							value = " ~ " + item.getRangeEndVal().toString();
						}
					}
 				}
			} else {
				value = item.getTextCntn();
			}
			dto.setAttrValue(value);
			return dto;
		}).toList();
	}
	
	private int createSeqAttrMultipleValue(String validCode, String attrUuid) {
		int seqNo = 0;
		Map<String, Object> maps = new HashMap<>();
		maps.put("validCode", validCode);
		maps.put("attrUuid", attrUuid);
		List<Integer> seqs = commonDao.selectList("ui-cust-valid-multi-val-d.findAttrSeq", maps);
		if (seqs != null && !seqs.isEmpty()) {
			seqNo = seqs.get(0);
		}
		return seqNo;
	}

	@Override
	public void exportExcel(String searchBy, String item, String type, String subType, String language, HttpServletResponse response) {
		String largeItemCode = null;
		String middleItemCode = null;
		String itemCode = null;
		if (StringUtils.isNotBlank(item)) {
			largeItemCode = item;
			if (item.equals("O") || item.equals("R")) {
				if (StringUtils.isNotBlank(type)) {
					itemCode = type;
				}
			} else if (item.equals("C")) {
				if (StringUtils.isNotBlank(type)) {
					middleItemCode = type;
				}
				if (StringUtils.isNotBlank(subType)) {
					itemCode = subType;
				}
			}
		}
		Map<String, Object> search = new HashMap<>();
		search.put("itemCode", itemCode);
		search.put("largeItemCode", largeItemCode);
		search.put("middleItemCode", middleItemCode);
		List<AttributeDto> boxs = new ArrayList<>();
		if (searchBy.equals("C")) {
			boxs = commonDao.selectList("ui-cust-valid-m.getData", search);
		} else if (searchBy.equals("A")) {
			boxs = commonDao.selectList("ui-cust-valid-m.findInListViewWithTypeAction", search);
		} 
		Integer number = 0;
		Map<Integer, List<CustomValidationExcelDto>> results = new HashMap<>();
		for (AttributeDto box: boxs) {
			Map<String, Object> maps = new HashMap<>();
			maps.put("validCode", box.getValidCode());
			maps.put("language", language);
			List<CustomValidationExcelEntity> entities = commonDao.selectList("ui-cust-valid-val-d.getData", maps);
			List<CustomValidationExcelDto> cntDtos = convertToDto(entities);		
			number++;
			results.put(number, cntDtos);
		}
		excelHelper.of(CustomValidationExportDto.class).downloadExcel(buildExcelInput(results), response, false);
	}

	private ExcelInput buildExcelInput(Map<Integer, List<CustomValidationExcelDto>> maps) {
		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName("CustomValidation");
		excelInput.setSheetName("Validation-Rule");
		excelInput.setFormatDate(DateConstant.YYYYMMDD);
		excelInput.setObject(maps);
		return excelInput;
	}

	@Override
	public List<CustomValidationItemResponse> getItem(String item, String type, String subType) {
		List<CustomValidationItemResponse> results = new ArrayList<>();		
		String itemCode = null;
		if (item.equals("O") || item.equals("R")) {
			itemCode = type;
		} else if (item.equals("C")) {			
			itemCode = subType;
		}
		Item itemEntity = commonDao.select("ui-cust-valid-m.getItem", itemCode);
		CustomValidationItemResponse result1 = new CustomValidationItemResponse();
		result1.setLargeItemCode(itemEntity.getLargeItemCode());
		result1.setLargeItemName(itemEntity.getLargeItemName());
		if (item.equals("C")) {
			result1.setMiddleItemCode(itemEntity.getMiddleItemCode());
			result1.setMiddleItemName(itemEntity.getMiddleItemName());				
		}
		result1.setItemCode(itemEntity.getItemCode());
		result1.setItemName(itemEntity.getItemName());
		results.add(result1);
		
		List<MiddleItemEntity> middleItemEntities = commonDao.selectList("ui-cust-valid-m.getSecondItem", itemCode);
		if (item.equals("C") || item.equals("R")) {			
			if (middleItemEntities != null && !middleItemEntities.isEmpty()) {
				CustomValidationItemResponse itemResponse = new CustomValidationItemResponse();
				MiddleItemEntity middleItemEntity01 = middleItemEntities.get(0);
				itemResponse.setLargeItemCode(middleItemEntity01.getLargeItemCodeTrgt());
				itemResponse.setLargeItemName(middleItemEntity01.getLargeItemNameTrgt());
				List<Item> types = new ArrayList<>();				
				for (MiddleItemEntity middleItemEntity: middleItemEntities) {
					Item item2 = new Item();
					item2.setItemCode(middleItemEntity.getItemCode());
					item2.setItemName(middleItemEntity.getItemCodeName());
					types.add(item2);
				}
				itemResponse.setTypes(types);
				results.add(itemResponse);
			}
		} else if (item.equals("O")) {
			CustomValidationItemResponse itemResponse = new CustomValidationItemResponse();						
			if (middleItemEntities != null && !middleItemEntities.isEmpty()) {
				itemResponse.setLargeItemCode("C");
				itemResponse.setLargeItemName("Component");
				List<CustomValidationComponentItem> componentItems = new ArrayList<>();
				Map<String, List<MiddleItemEntity>> mapEntities = middleItemEntities.stream().collect(Collectors.groupingBy(MiddleItemEntity::getMctgrItemCode));		
				for(Map.Entry<String, List<MiddleItemEntity>> entry : mapEntities.entrySet()) { 
					CustomValidationComponentItem componentItem = new CustomValidationComponentItem();
					componentItem.setItemCode(entry.getKey());
					componentItem.setSortNo(entry.getValue().get(0).getMiddleSort());
					Item middle = commonDao.select("ui-cust-valid-m.getMiddle", entry.getKey());
					componentItem.setItemName(middle.getMiddleItemName());
					List<Item> types = new ArrayList<>();					
					List<MiddleItemEntity> values = entry.getValue().stream().sorted(Comparator.comparingInt(MiddleItemEntity::getItemSort)).toList();
					for (MiddleItemEntity entity: values) {
						Item itemXyz = new Item();
						itemXyz.setItemCode(entity.getItemCode());
						itemXyz.setItemName(entity.getItemCodeName());
						types.add(itemXyz);
					}
					componentItem.setTypes(types);
					componentItems.add(componentItem);
				}				
				componentItems = componentItems.stream().sorted(Comparator.comparingInt(CustomValidationComponentItem::getSortNo)).toList();
				itemResponse.setComponentItem(componentItems);
			}
			results.add(itemResponse);
		}
		return results;
	}
}
