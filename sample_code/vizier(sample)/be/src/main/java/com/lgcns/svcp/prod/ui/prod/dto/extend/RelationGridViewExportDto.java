package com.lgcns.svcp.prod.ui.prod.dto.extend;

import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;
import com.lgcns.svcp.prod.util.excel.annotation.Value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CustomTitleHeader
public class RelationGridViewExportDto {
	
	@Value(name ="relationmanager.excel.no")
	private Integer no;
	
	@Value(name ="relationmanager.excel.leadercode")
	private String leaderCode;
	
	@Value(name ="relationmanager.excel.leadername")
	private String leaderName;
	
	@Value(name ="relationmanager.excel.followercode")
	private String followerCode;
	
	@Value(name ="relationmanager.excel.followername")
	private String followerName;
	
	@Value(name ="relationmanager.excel.relationcode")
	private String relationCode;
	
	@Value(name ="relationmanager.excel.relationname")
	private String relationName;
	
	@Value(name ="relationmanager.excel.relationstartdate")
	private String relationStartDate;
	
	@Value(name ="relationmanager.excel.relationenddate")
	private String relationEndDate;
	
	@Value(name ="relationmanager.excel.groupcode")
	private String groupCode;
	
	@Value(name ="relationmanager.excel.groupname")
	private String groupName;
	
	@Value(name ="relationmanager.excel.groupstartdate")
	private String groupStartDate;
	
	@Value(name ="relationmanager.excel.groupenddate")
	private String groupEndDate;
}
