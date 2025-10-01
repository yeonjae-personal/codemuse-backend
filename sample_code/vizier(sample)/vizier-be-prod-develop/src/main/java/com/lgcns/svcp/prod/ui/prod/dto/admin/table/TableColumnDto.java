package com.lgcns.svcp.prod.ui.prod.dto.admin.table;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableColumnDto extends BaseDto {
	private String tableName;
	private String columnName;
	private String columnType;
	private String commGroupCode;
	private String attrMaxLength;
	private String columnKeyYn;
	private String requiredYn;
	private String columnComment;
	private long sortNo;
	private String useYn;
	private List<String> allowedFilters;
}
