package com.lgcns.svcp.prod.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lgcns.svcp.prod.entity.DsbdOfferSubCntEntity;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdOfferSubCntDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdOfferSubCntExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.SubscribeTopSimpleViewResponse;


@Mapper(componentModel = "spring")
public interface DsbdOfferSubCntMapper {
	
	@Mapping(target = "name", source = "entity.offerName")
	@Mapping(target = "subscriber", source = "entity.subCnt")
	SubscribeTopSimpleViewResponse entityToSimpleViewResponse(DsbdOfferSubCntEntity entity);
	
	@Mapping(target = "name", source = "entity.offerName")
	@Mapping(target = "code", source = "entity.offerCode")
	@Mapping(target = "type", source = "entity.offerTypeName")
	@Mapping(target = "subscriber", source = "entity.subCnt")
	@Mapping(target = "startDate", source = "entity.saleValidStartDtm")
	@Mapping(target = "endDate", source = "entity.saleValidEndDtm")
	DsbdOfferSubCntDto entityToDto(DsbdOfferSubCntEntity entity);
	
	@Mapping(target = "name", source = "entity.offerName")
	@Mapping(target = "code", source = "entity.offerCode")
	@Mapping(target = "type", source = "entity.offerTypeName")
	@Mapping(target = "subscriber", source = "entity.subCnt")
	@Mapping(target = "startDate", source = "entity.saleValidStartDtm")
	@Mapping(target = "endDate", source = "entity.saleValidEndDtm")
	DsbdOfferSubCntExportDto entityToExportDto(DsbdOfferSubCntEntity entity);
}
