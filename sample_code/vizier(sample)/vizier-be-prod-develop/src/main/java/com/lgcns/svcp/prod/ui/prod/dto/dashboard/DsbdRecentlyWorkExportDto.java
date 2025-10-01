package com.lgcns.svcp.prod.ui.prod.dto.dashboard;

import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;
import com.lgcns.svcp.prod.util.excel.annotation.Value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CustomTitleHeader
public class DsbdRecentlyWorkExportDto {
	
	@Value(name ="dashboard.recentlywork.excel.category")
	private String category;
	
	@Value(name ="dashboard.recentlywork.excel.type")
	private String type;
	
	@Value(name ="dashboard.recentlywork.excel.objname")
	private String objName;
	
	@Value(name ="dashboard.recentlywork.excel.objcode")
	private String objCode;
	
	@Value(name ="dashboard.recentlywork.excel.worktypename")
	private String workTypeName;
	
	@Value(name ="dashboard.recentlywork.excel.responsibledept")
	private String responsibleDept;
	
	@Value(name ="dashboard.recentlywork.excel.responsibleuser")
	private String responsibleUser;
	
	@Value(name ="dashboard.recentlywork.excel.workdate")
	private String workDate;
}
