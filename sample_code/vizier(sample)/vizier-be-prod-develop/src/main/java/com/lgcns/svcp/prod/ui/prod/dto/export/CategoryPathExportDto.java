package com.lgcns.svcp.prod.ui.prod.dto.export;

import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;
import com.lgcns.svcp.prod.util.excel.annotation.Value;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@CustomTitleHeader
public class CategoryPathExportDto {
	
	@Value(name ="category.offers.excel.index")
	private Integer index;
	
	@Value(name ="category.offers.excel.level1")
	private String level1;
	
	@Value(name ="category.offers.excel.level2")
	private String level2;
	
	@Value(name ="category.offers.excel.level3")
	private String level3;
	
	@Value(name ="category.offers.excel.level4")
	private String level4;
	
	@Value(name ="category.offers.excel.level5")
	private String level5;
	
	@Value(name ="category.offers.excel.offercode")
	private String offerCd;
	
	@Value(name ="category.offers.excel.offername")
	private String offerNm;

}
