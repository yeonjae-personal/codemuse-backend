package com.lgcns.svcp.prod.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lgcns.svcp.prod.entity.DsbdViewEntity;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdViewDto;

@Mapper(componentModel = "spring")
public interface DsbdViewMapper {
	@Mapping(target = "sortValue", source = "entity.dsbdViewSortNo")
	@Mapping(target = "dsbdViewDscrCntn", source = "entity.dsbdViewDscr")
	DsbdViewDto entityToDto(DsbdViewEntity entity);
}
