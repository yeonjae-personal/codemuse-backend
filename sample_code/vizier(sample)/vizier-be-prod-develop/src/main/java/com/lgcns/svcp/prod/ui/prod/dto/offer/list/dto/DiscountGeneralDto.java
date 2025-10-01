package com.lgcns.svcp.prod.ui.prod.dto.offer.list.dto;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.offer.detail.OfferGeneralDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.list.DcCfgrtDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.list.DcRateDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DiscountGeneralDto extends OfferGeneralDto {
	private String dcTypeCode;
	private String pnltOcrcYn;
	private String dcPriorRank;
	private String dcOvwCntn;
	private List<DcRateDto> dcRateList;
	private List<DcCfgrtDto> dcCfgrtList;
}
