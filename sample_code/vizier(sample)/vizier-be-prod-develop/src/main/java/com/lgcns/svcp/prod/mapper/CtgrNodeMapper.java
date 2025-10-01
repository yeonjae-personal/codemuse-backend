package com.lgcns.svcp.prod.mapper;

import org.mapstruct.Mapper;

import com.lgcns.svcp.prod.entity.CtgrNodeEntity;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.TreeViewDto;

@Mapper(componentModel = "spring")
public interface CtgrNodeMapper {
	
	CtgrNodeEntity treeViewDtoToEntity(TreeViewDto dto);
}
