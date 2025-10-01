package com.lgcns.svcp.prod.ui.prod.dto.export;

import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;
import com.lgcns.svcp.prod.util.excel.annotation.Value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CustomTitleHeader
public class ComponentStructureExportDto {
	
	@Value(name ="component.export.no")
	private Integer no;

	@Value(name ="component.export.code")
	private String componentCode;
	
	@Value(name ="component.export.name")
	private String componentName;


	@Value(name ="component.export.resourcecode")
	private String resourceCode;

	@Value(name ="component.export.resourcename")
	private String resourceName;
	
	@Value(name ="component.export.relationstartdate")
	private String relationStartDate;
	
	@Value(name ="component.export.relationenddate")
	private String relationEndDate;
}
