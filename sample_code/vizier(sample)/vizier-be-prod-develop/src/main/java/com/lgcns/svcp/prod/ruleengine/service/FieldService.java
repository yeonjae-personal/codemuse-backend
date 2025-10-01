package com.lgcns.svcp.prod.ruleengine.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.mapper.ruleengine.FieldMapper;
import com.lgcns.svcp.prod.ruleengine.dto.field.FieldDto;
import com.lgcns.svcp.prod.ruleengine.dto.field.FieldSearchPagingDto;
import com.lgcns.svcp.prod.ruleengine.entity.FieldEntity;
import com.lgcns.svcp.prod.util.paging.PageResult;

import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class FieldService {
	
	private final CommonDao commonDao;
	private final FieldMapper fieldMapper;
	
	@Autowired
	public MessageSource messageSource;
	
	public PageResult<FieldDto> retrieveAllFields(FieldSearchPagingDto params) {
		FieldSearchPagingDto properties = buildProperties(params);
		PageResult<FieldEntity> pageResult = commonDao.selectPagedList("Rule-Field.retrieveAllFields", properties);
		List<FieldDto> dtos = fieldMapper.convertListEntityToDto(pageResult.getElements());
		return new PageResult<FieldDto>(dtos, pageResult.getPage(), pageResult.getSize(), pageResult.getTotalElements());
	}

	private FieldSearchPagingDto buildProperties(FieldSearchPagingDto params) {
		if (StringUtils.isNotBlank(params.getType()) && StringUtils.isNotBlank(params.getValue())) {
			switch (params.getType()) {
        		case "key" -> params.setFieldKeyName(params.getValue().trim().toLowerCase());
        		case "name" -> params.setFieldDispName(params.getValue().trim().toLowerCase());
			}
		}
		return params;
	}

	public void save(FieldDto fieldDto) {
		if (StringUtils.isNotBlank(fieldDto.getFieldUuid())) {
			FieldEntity updateField = fieldMapper.convertDtoToEntity(fieldDto);
			commonDao.update("Rule-Field.update", updateField);
		} else {
			FieldEntity fieldEntity = commonDao.select("Rule-Field.getFieldByKey", fieldDto.getFieldKeyName());
			if (fieldEntity != null) {
				throw new BusinessException(messageSource.getMessage("field.duplicate", null, LocaleContextHolder.getLocale()));
			}
			fieldDto.setUseYn("Y");
			FieldEntity newField = fieldMapper.convertDtoToEntity(fieldDto);
			commonDao.insert("Rule-Field.insert", newField);
		}
	}
}
