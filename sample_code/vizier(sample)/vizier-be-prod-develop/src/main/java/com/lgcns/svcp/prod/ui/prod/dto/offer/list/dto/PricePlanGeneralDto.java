package com.lgcns.svcp.prod.ui.prod.dto.offer.list.dto;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.offer.detail.OfferGeneralDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.list.BsfDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PricePlanGeneralDto extends OfferGeneralDto {
	private String custTypeCode;
	private String ageDivCode;
	private String ppOvwCntn;
	private String saleValidStartDtm;
	private String saleValidEndDtm;
	private String saleCorpName;
	private List<String> markCd;;
	private List<BsfDto> bsfList;
}
