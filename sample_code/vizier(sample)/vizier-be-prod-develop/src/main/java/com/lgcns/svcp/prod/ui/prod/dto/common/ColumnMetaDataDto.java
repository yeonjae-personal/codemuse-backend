package com.lgcns.svcp.prod.ui.prod.dto.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnMetaDataDto {
	private String columnName;
	private String fieldType;
	private String editYn;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Attribute> selectOptions;
}
