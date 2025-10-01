package com.lgcns.svcp.prod.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.lgcns.svcp.prod.ui.prod.dto.export.ResourceExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.ResourceSearchReq;
import com.lgcns.svcp.prod.ui.prod.dto.resource.create.ResourceGeneralRes;
import com.lgcns.svcp.prod.ui.prod.dto.resource.list.ResourceSearchDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.search.ResoureSearchRes;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceMapper {

    List<ResourceGeneralRes> resourceDtoListToRes(List<ResourceSearchDto> generalDtoList);

    ResourceSearchDto resourceSearchToDto(ResourceSearchReq searchReq);
    
    @Mapping(source = "general", target = "general")
    @Mapping(source = "additional", target = "additional")
    ResourceExportDto resourceToExportDto(ResoureSearchRes dto);
}
