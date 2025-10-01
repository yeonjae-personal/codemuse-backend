package com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel;

import lombok.Data;

@Data
public class EntityObjRelResDto {
	private String objUuid;
	private String entityCode;
	private String validStartDtm;
	private String validEndDtm;
	private String entityName;
	private String entityTypeCode;
    private String itemValidStartDtm;
    private String itemValidEndDtm;
}
