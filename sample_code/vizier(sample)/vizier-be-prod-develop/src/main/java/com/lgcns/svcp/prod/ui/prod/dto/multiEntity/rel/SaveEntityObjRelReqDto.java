package com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Data;

@Data
public class SaveEntityObjRelReqDto extends BaseDto {
	@JsonIgnore
	private String objUuid;
	@JsonIgnore
	private String oldEntityCode;
	private String entityCode;
	private String validStartDtm;
	private String validEndDtm;
}
