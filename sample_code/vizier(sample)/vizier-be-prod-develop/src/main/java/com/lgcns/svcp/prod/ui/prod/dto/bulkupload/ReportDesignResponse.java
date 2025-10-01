package com.lgcns.svcp.prod.ui.prod.dto.bulkupload;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDesignResponse {
	
	private Integer itemQuantity;
	private Integer itemQuantitySucess;
	private Integer itemQuantityFail;
	private String fileName;
	private List<ItemReportData> datas;
}
