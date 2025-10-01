package com.lgcns.svcp.prod.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.lgcns.svcp.prod.entity.DsbdMonthlyOfferEntity;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.DsbdMonthlyOfferResponse;

@Mapper(componentModel = "spring")
public interface DsbdMonthlyOfferMapper {
	
	List<DsbdMonthlyOfferResponse> convertListEntityToResponse(List<DsbdMonthlyOfferEntity> entity);
}
