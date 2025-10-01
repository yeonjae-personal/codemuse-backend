package com.lgcns.svcp.prod.mapper;

import org.mapstruct.Mapper;

import com.lgcns.svcp.prod.entity.DsbdViewPstEntity;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.DsbdViewPstRequest;

@Mapper(componentModel = "spring")
public interface DsbdViewPstMapper {
	DsbdViewPstEntity requestToEntity(DsbdViewPstRequest request);
}
