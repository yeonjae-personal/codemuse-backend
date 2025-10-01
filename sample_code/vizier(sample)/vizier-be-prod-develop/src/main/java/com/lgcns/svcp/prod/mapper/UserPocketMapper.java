package com.lgcns.svcp.prod.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lgcns.svcp.prod.entity.external.UserPocketCustomEntity;
import com.lgcns.svcp.prod.ui.prod.dto.userpocket.UserPocketDto;

@Mapper(componentModel = "spring")
public interface UserPocketMapper {
	
	@Mapping(target = "name", source = "entity.objName")
	@Mapping(target = "code", source = "entity.objCode")
	@Mapping(target = "type", source = "entity.itemCode")
	@Mapping(target = "uuid", source = "entity.objUuid")
	@Mapping(target = "middleType", source = "entity.mctgrItemCode")
	UserPocketDto customEntityToDto(UserPocketCustomEntity entity);
}
