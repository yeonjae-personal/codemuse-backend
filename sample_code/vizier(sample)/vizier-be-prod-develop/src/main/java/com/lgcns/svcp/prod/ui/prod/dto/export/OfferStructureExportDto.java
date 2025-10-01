package com.lgcns.svcp.prod.ui.prod.dto.export;

import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;
import com.lgcns.svcp.prod.util.excel.annotation.Value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CustomTitleHeader
public class OfferStructureExportDto {
	
	@Value(name ="offer.export.number")
	private Integer number;
	
	@Value(name ="offer.export.offercode")
	private String offerCode;

	@Value(name ="offer.export.offername")
	private String offerName;

	@Value(name ="offer.export.componentcode")
	private String componentCode;

	@Value(name ="offer.export.componentname")
	private String componentName;
	
	@Value(name ="offer.export.startdate")
	private String startDate;
	
	@Value(name ="offer.export.finishdate")
	private String finishDate;
}
