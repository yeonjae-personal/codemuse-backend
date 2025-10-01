package com.lgcns.svcp.prod.ui.prod.dto.common;

import lombok.Data;

@Data
public class RequestCommonCodeDto {
	private String columnName;
	private String columnValue;
	private String attrUuid;
	private String prodUuid;
}
