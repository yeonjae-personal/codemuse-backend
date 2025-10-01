package com.lgcns.svcp.prod.ui.prod.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lgcns.svcp.prod.ui.prod.enums.ComponentType;
import com.lgcns.svcp.prod.ui.prod.enums.OfferType;
import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemsMDto extends BasePaginationDto {
	private String prodUuid;
	private String prodItemCd;
	private String prodItemNm;
	private ComponentType componentType;
	private String itemTypeNm;
	private String itemDetlTypeCd;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	@JsonIgnore
	private OfferType offerType;
	@JsonIgnore
	private String baseUuid;
}
