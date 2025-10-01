package com.lgcns.svcp.prod.ui.prod.dto.extend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemOffrResDto {
	private String offrUuid;
	private String offrCd;
	private String offrNm;
	private String offrType;
	private String validStartDtm;
	private String validEndDtm;
	private String workTypeCode;
	private String itemValidStartDtm;
	private String itemValidEndDtm;
	private String offerGroupUuid;
}
