package com.lgcns.svcp.prod.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lgcns.svcp.prod.entity.DsbdRecentlyWorkEntity;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdRecentlyWorkExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.RecentWorkDetailViewDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.RecentWorkSimpleViewDto;

@Mapper(componentModel = "spring")
public interface DsbdRecentWorkMapper {
	
	@Mapping(target = "workTypeCode", source = "entity.workType")
	RecentWorkSimpleViewDto entityToSimpleViewDto(DsbdRecentlyWorkEntity entity);
	
	@Mapping(target = "category", source = "entity.lctgrItemName")
	@Mapping(target = "workTypeCode", source = "entity.workType")
	@Mapping(target = "responsibleDept", source = "entity.updUserDeptName")
	@Mapping(target = "responsibleUser", source = "entity.attrValUpdUser")
	RecentWorkDetailViewDto entityToDetailViewDto(DsbdRecentlyWorkEntity entity);
	
	@Mapping(target = "category", source = "entity.lctgrItemName")
	@Mapping(target = "responsibleDept", source = "entity.updUserDeptName")
	@Mapping(target = "responsibleUser", source = "entity.attrValUpdUser")
	DsbdRecentlyWorkExportDto entityToExportExcelDto(DsbdRecentlyWorkEntity entity);
}
