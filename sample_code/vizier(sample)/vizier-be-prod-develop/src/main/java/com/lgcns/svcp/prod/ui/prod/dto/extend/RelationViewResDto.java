package com.lgcns.svcp.prod.ui.prod.dto.extend;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationViewResDto {
    private String parentUuid;
	private String dpdcRelUuid;
	private String dpdcRelCode;
	private String dpdcRelName;
	private String relationValidStartDate;
	private String relationValidEndDate;
	private String targetUuid;
	private String targetCode;
	private String targetName;
	private String itemCode;
	private String itemCodeName;
	private String lctgrItemName;
	private String validStartDtm;
	private String validEndDtm;
	private String itemValidStartDtm;
	private String itemValidEndDtm;
	private List<String> referenceUuids;
	private List<ItemOffrResDto> childOffr;
	private String offerGroupUuid;
	private String referenceUuid;
	private String leaderCode;
	private String leaderName;
}
