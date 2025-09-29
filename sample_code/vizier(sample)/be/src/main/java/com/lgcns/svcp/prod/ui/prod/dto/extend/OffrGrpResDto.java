package com.lgcns.svcp.prod.ui.prod.dto.extend;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OffrGrpResDto {
	private String offerGroupUuid;
	private String offerGroupCode;
	private String offerGroupName;
    private List<RelationViewResDto> leaderList;
    private List<RelationViewResDto> followerList;
}
