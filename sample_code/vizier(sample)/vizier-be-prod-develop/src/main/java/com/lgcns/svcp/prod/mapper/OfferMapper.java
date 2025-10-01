package com.lgcns.svcp.prod.mapper;

import java.text.DecimalFormat;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.lgcns.svcp.prod.ui.prod.dto.export.OfferExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.search.OfferSearchRes;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OfferMapper {

    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);
    DecimalFormat df = new DecimalFormat("0.00");

    @Mapping(source = "general", target = "general")
    @Mapping(source = "additional", target = "additional")
    OfferExportDto offerToExportDto(OfferSearchRes dto);
}
