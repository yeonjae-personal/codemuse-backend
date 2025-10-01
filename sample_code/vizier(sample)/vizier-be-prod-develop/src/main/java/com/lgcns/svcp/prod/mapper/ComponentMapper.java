package com.lgcns.svcp.prod.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.lgcns.svcp.prod.ui.prod.dto.component.addResource.ComponentAddResourceDto;
import com.lgcns.svcp.prod.ui.prod.dto.component.addResource.ComponentAddResourceRes;
import com.lgcns.svcp.prod.ui.prod.dto.component.search.ComponentSearchRes;
import com.lgcns.svcp.prod.ui.prod.dto.export.ComponentExportDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ComponentMapper {
    List<ComponentAddResourceRes> dtoToResList(List<ComponentAddResourceDto> componentAddResourceDtoList);
    
    @Mapping(source = "general", target = "general")
    @Mapping(source = "additional", target = "additional")
    ComponentExportDto componentToExportDto(ComponentSearchRes dto);
}
