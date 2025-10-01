package com.lgcns.svcp.prod.ui.prod.dto.dashboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsbdOfferSubCntDto {
	
	private String code;
	private String name;
	private String type;
	private Integer subscriber;
	private String startDate;
	private String endDate;
	private Long duration;
	private boolean status;
}
