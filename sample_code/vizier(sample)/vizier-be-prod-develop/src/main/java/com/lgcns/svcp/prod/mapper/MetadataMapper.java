package com.lgcns.svcp.prod.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lgcns.svcp.prod.entity.MiddleItemEntity;
import com.lgcns.svcp.prod.ui.prod.dto.item.MiddleItemDto;

@Mapper(componentModel = "spring")
public interface MetadataMapper {
	
	@Mapping(target = "code", source = "entity.itemCode")
	@Mapping(target = "name", source = "entity.itemCodeName")
	MiddleItemDto middleItemEntityToDto(MiddleItemEntity entity);
}
