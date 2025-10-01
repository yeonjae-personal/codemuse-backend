package com.lgcns.svcp.prod.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.lgcns.svcp.prod.entity.DsbdMonthlyUserGroupOfferEntity;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.DsbdMonthlyUserGroupOfferResponse;

@Mapper(componentModel = "spring")
public interface DsbdMonthlyUserGroupOfferMapper {
	
	List<DsbdMonthlyUserGroupOfferResponse> convertListEntityToResponse(List<DsbdMonthlyUserGroupOfferEntity> entity);
}
