package com.lgcns.svcp.prod.ui.prod.dto.dashboard;

import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;
import com.lgcns.svcp.prod.util.excel.annotation.Value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CustomTitleHeader
public class DsbdOfferSubCntExportDto {
	
	@Value(name ="dashboard.subscribertop10.excel.offercode")
	private String code;
	
	@Value(name ="dashboard.subscribertop10.excel.offertype")
	private String type;
	
	@Value(name ="dashboard.subscribertop10.excel.offername")
	private String name;
	
	@Value(name ="dashboard.subscribertop10.excel.noofsubscriber")
	private Integer subscriber;
	
	@Value(name ="dashboard.subscribertop10.excel.status")
	private String status;
	
	@Value(name ="dashboard.subscribertop10.excel.startdate")
	private String startDate;
	
	@Value(name ="dashboard.subscribertop10.excel.enddate")
	private String endDate;
	
	@Value(name ="dashboard.subscribertop10.excel.duration")
	private Long duration;
}
