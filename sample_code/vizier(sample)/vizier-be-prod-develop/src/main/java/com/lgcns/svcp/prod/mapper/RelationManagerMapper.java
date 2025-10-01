package com.lgcns.svcp.prod.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lgcns.svcp.prod.ui.prod.dto.extend.RelationGridViewDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.RelationGridViewEntity;
import com.lgcns.svcp.prod.ui.prod.dto.extend.RelationGridViewExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.RelationViewResDto;

@Mapper(componentModel = "spring")
public interface RelationManagerMapper {
	
	@Mapping(target = "relationCode", source = "relationViewResDto.dpdcRelCode")
	@Mapping(target = "relationName", source = "relationViewResDto.dpdcRelName")
	@Mapping(target = "followerCode", source = "relationViewResDto.targetCode")
	@Mapping(target = "followerName", source = "relationViewResDto.targetName")
	RelationGridViewDto viewResToGridViewDto(RelationViewResDto relationViewResDto);
	
	RelationGridViewExportDto convertToExcelExport(RelationGridViewDto dtos);
	RelationGridViewDto convertToDto(RelationGridViewEntity entity);
	RelationGridViewExportDto convertToExcelDto(RelationGridViewEntity entity);
}
