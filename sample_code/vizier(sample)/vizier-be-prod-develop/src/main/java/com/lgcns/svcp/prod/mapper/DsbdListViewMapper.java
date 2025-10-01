package com.lgcns.svcp.prod.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lgcns.svcp.prod.entity.external.DsbdListViewEntity;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdListViewDto;

@Mapper(componentModel = "spring")
public interface DsbdListViewMapper {
	
	@Mapping(target = "dsbdViewDscrCntn", source = "entity.dsbdViewDscr")
	DsbdListViewDto entityToDto(DsbdListViewEntity entity);
}
