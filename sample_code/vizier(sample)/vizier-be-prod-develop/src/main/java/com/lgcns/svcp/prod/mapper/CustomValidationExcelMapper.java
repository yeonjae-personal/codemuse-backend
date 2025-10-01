package com.lgcns.svcp.prod.mapper;

import org.mapstruct.Mapper;

import com.lgcns.svcp.prod.entity.CustomValidationExcelEntity;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationExcelDto;

@Mapper(componentModel = "spring")
public interface CustomValidationExcelMapper {
	
	CustomValidationExcelDto entityToDto(CustomValidationExcelEntity entity);
}
