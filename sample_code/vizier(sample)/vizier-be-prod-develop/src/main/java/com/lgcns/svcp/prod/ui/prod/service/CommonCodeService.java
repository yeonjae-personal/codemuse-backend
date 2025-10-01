package com.lgcns.svcp.prod.ui.prod.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.ui.prod.dto.GeneralAttribute;
import com.lgcns.svcp.prod.ui.prod.dto.common.Attribute;
import com.lgcns.svcp.prod.ui.prod.dto.common.RequestCommonCodeDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.RequestSelectOptionsDto;
import com.lgcns.svcp.prod.ui.prod.util.AttributeUtil;

@Component
public class CommonCodeService {

	private static final String ITEM_CODE = "itemCode";
	private static final String PROPERTIES = "properties";

	@Autowired
	private CommonDao commonDao;

	public Attribute getGeneralCommonCodeDescription(String codeType, String codeValue) {
		RequestCommonCodeDto request = new RequestCommonCodeDto();
		request.setColumnName(codeType); request.setColumnValue(codeValue);
		
        String description = commonDao.select("UI.getGeneralCommonCodeDescription", request);
      
        return new Attribute(codeValue, description);
    }
	
	public String getAdditionalCommonCodeDescription(String attrUuid, String prodUuid) {
		RequestCommonCodeDto request = new RequestCommonCodeDto();
		request.setAttrUuid(attrUuid); request.setProdUuid(prodUuid);
        String description = commonDao.select("UI.getAdditionalCommonCodeDescription", request);
      
        return description;
    }
	
	public List<String> getSelectOptions(String columnName) {		
        return commonDao.selectList("UI.getSelectOption", columnName);
    }
	
	public List<Attribute> getSelectOptionList(String columnName) {
		return commonDao.selectList("UI.getSelectOptionList", columnName);
	}
	
	public List<String> getAdditionalSelectOptions(String attrUuid) {
		RequestSelectOptionsDto request = new RequestSelectOptionsDto();
		request.setAttrUuid(attrUuid);
        return commonDao.selectList("UI.getAdditionalSelectOption", request);
    }
	
	public List<Attribute> getAdditionalSelectOptionList(String attrUuid) {
		RequestSelectOptionsDto request = new RequestSelectOptionsDto();
		request.setAttrUuid(attrUuid);
        return commonDao.selectList("UI.getAdditionalSelectOptionList", request);
    }

	public Map<String, Object> objectToAttrMap(Object general, String itemCode) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> gnrlMap = mapper.convertValue(general, new TypeReference<>() {
		});
		Map<String, Object> resultMap = new HashMap<>(gnrlMap);
		List<String> keyList = gnrlMap.keySet().stream().map(AttributeUtil::camelToSnake).toList();
		Map<String, Object> param = new HashMap<>(Collections.singletonMap("properties", keyList));
		param.put(ITEM_CODE, itemCode);
		List<GeneralAttribute> attributes = commonDao.selectList("Ui-component.getGeneralAttribute", param);
		attributes.forEach(attr -> attr.setName(AttributeUtil.snakeToCamel(attr.getName())));
		Map<String, GeneralAttribute> attrMap = attributes.stream().collect(Collectors.toMap(GeneralAttribute::getName, Function.identity()));
		attrMap.entrySet().stream()
				.filter(entry -> attrMap.containsKey(entry.getKey()))
				.forEach(entry -> resultMap.put(entry.getKey(), entry.getValue()));
		resultMap.entrySet().stream()
				.filter(entry -> entry.getValue() instanceof GeneralAttribute)
				.forEach(entry -> ((GeneralAttribute) entry.getValue()).setValue(gnrlMap.get(entry.getKey())));
		return resultMap;
	}
}
