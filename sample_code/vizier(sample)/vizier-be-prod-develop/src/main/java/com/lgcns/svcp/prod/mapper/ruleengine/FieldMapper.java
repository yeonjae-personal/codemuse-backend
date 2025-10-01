package com.lgcns.svcp.prod.mapper.ruleengine;

import java.util.List;

import org.mapstruct.Mapper;
import com.lgcns.svcp.prod.ruleengine.dto.field.FieldDto;
import com.lgcns.svcp.prod.ruleengine.entity.FieldEntity;

@Mapper(componentModel = "spring")
public interface FieldMapper {
	
	FieldDto convertEntityToDto(FieldEntity entity);
	FieldEntity convertDtoToEntity(FieldDto dto);
	List<FieldDto> convertListEntityToDto(List<FieldEntity> entities);	
}
