package com.lgcns.svcp.prod.online.prod.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class CtgrNodeMDto {
	@JsonIgnore
	private String objUuid;
	private String ctgrNodeUuid;
	private String ctgrTabUuid;
	private String ctgrNodeName;
	private String hpstCtgrNodeUuid;
	private String tclsCtgrYn;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
}
