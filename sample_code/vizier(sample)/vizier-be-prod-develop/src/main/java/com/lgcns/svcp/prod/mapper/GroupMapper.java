package com.lgcns.svcp.prod.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.lgcns.svcp.prod.ui.prod.dto.export.GroupExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.group.search.GroupSearchRes;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {
	
	@Mapping(source = "general", target = "general")
    @Mapping(source = "additional", target = "additional")
    GroupExportDto groupToExportDto(GroupSearchRes dto);
}
