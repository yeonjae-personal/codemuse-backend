package com.lgcns.svcp.prod.mapper;

import org.mapstruct.Mapper;

import com.lgcns.svcp.prod.entity.MultiLangLabelEntity;
import com.lgcns.svcp.prod.ui.prod.dto.label.MultiLangLabelDto;

@Mapper(componentModel = "spring")
public interface LabelMapper {
	
	MultiLangLabelDto entityToDto(MultiLangLabelEntity entity);
	
}
