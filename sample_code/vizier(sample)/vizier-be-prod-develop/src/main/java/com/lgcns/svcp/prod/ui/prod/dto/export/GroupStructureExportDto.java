package com.lgcns.svcp.prod.ui.prod.dto.export;

import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;
import com.lgcns.svcp.prod.util.excel.annotation.Value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CustomTitleHeader
public class GroupStructureExportDto {
	
	@Value(name ="group.export.no")
	private Integer no;
	
	@Value(name ="group.export.code")
	private String groupCode;
	
	@Value(name ="group.export.name")
	private String groupName;

	@Value(name ="group.export.offercode")
	private String offerCode;
	
	@Value(name ="group.export.offername")
	private String offerName;
	
	@Value(name ="group.export.startdate")
	private String startDate;
	
	@Value(name ="group.export.finishdate")
	private String finishDate;
}
