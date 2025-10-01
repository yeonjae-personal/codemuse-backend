package com.lgcns.svcp.prod.ui.prod.dto.dashboard.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsbdMonthlyOfferResponse {
	
	public DsbdMonthlyOfferResponse() {
		
	}
	
	public DsbdMonthlyOfferResponse(String yearMonth, Integer offerCnt) {
		this.yearMonth = yearMonth;
		this.offerCnt = offerCnt;
	}
	
	private String yearMonth;
	private Integer offerCnt;
}
