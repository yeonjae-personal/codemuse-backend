package com.lgcns.svcp.prod.ui.prod.dto.extend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationGridViewDto {
	
	private Integer no;
	private String leaderCode;
	private String leaderName;
	private String followerCode;
	private String followerName;
	private String relationCode;
	private String relationName;
	private String relationStartDate;
	private String relationEndDate;
	private String groupCode;
	private String groupName;
	private String groupStartDate;
	private String groupEndDate;
	private String largeItemCode;
}
