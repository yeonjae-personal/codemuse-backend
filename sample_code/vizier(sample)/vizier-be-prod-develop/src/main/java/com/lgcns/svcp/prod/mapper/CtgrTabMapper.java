package com.lgcns.svcp.prod.mapper;

import org.mapstruct.Mapper;

import com.lgcns.svcp.prod.entity.CtgrTabEntity;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.TreeViewDto;

@Mapper(componentModel = "spring")
public interface CtgrTabMapper {
	
	CtgrTabEntity treeViewDtoToEntity(TreeViewDto dto);
}
