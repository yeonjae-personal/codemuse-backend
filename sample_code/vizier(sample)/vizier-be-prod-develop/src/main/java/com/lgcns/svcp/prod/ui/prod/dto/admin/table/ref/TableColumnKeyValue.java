package com.lgcns.svcp.prod.ui.prod.dto.admin.table.ref;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableColumnKeyValue {
	@JsonIgnore
	private String tableName;
	private String cmcdDetlId; // sort_no = 1
	private String cmcdDetlNm; // sort_no = 2
}
