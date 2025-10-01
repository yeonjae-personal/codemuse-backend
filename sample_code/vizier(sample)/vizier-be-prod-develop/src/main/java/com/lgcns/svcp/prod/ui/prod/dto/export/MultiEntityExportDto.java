package com.lgcns.svcp.prod.ui.prod.dto.export;

import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;
import com.lgcns.svcp.prod.util.excel.annotation.Value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CustomTitleHeader
public class MultiEntityExportDto {
	
	@Value(name ="multi.export.no")
	private Integer no;

	@Value(name ="multi.export.itemcode")
	private String itemCode;
	
	@Value(name ="multi.export.itemname")
	private String itemName;

	@Value(name ="multi.export.entitycode")
	private String entityCode;

	@Value(name ="multi.export.entityname")
	private String entityName;
	
	@Value(name ="multi.export.relationstartdate")
	private String relationStartDate;
	
	@Value(name ="multi.export.relationenddate")
	private String relationEndDate;
}
