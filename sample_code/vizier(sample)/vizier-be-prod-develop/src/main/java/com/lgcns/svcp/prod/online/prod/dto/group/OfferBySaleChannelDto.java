package com.lgcns.svcp.prod.online.prod.dto.group;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;
import lombok.Data;

@Data
public class OfferBySaleChannelDto extends BasePaginationDto{
	private String prodItemCd;
	private String saleChnlCd;
	private String valdStrtDtm;
	private String valdEndDtm;
}
