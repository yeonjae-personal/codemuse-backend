package com.lgcns.svcp.prod.ui.prod.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestCommonCodeNameDto {
	private String tableName;
	private String columnName;
	private String columnValue;
}
