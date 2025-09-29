package com.lgcns.svcp.prod.ui.prod.dto.extend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationGridViewEntity {
	
	private Integer no;
	private String leaderName;
	private String leaderCode;
	private String followerName;
	private String followerCode;
	private String relationName;
	private String relationCode;
	private String relationStartDate;
	private String relationEndDate;
	private String groupName;
	private String groupCode;
	private String groupStartDate;
	private String groupEndDate;
	private String followerCodeGroup;
	private String followerNameGroup;
	private String largeItemCode;
	private String offerGroupUuid;
}
