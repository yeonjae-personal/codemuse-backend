package com.lgcns.svcp.prod.ui.prod.dto.common.metadata;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.ref.TableColumnKeyValue;
import com.lgcns.svcp.prod.util.StringUtilCustom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdditionalDetailDto extends BaseDto {
	private String attrUuid;
	private String itemCode;
	private String fieldTypeCode;
	private String commGroupCode;
	private long sortNo;
	private String useYn;
	private String attrMaxLength;
	private String requiredYn;
	private String labelId;
	private String dispTab;
	private String dispCardYn;
	private String advSearchYn;
	private String attrVal;
	private String attrRefTableName;

	@JsonInclude(Include.NON_NULL)
	private List<TableColumnKeyValue> tableColumns;

	@JsonIgnore
	private String objUuid;
	private String obName;
	private String labelName;
	private String labelDscr;

	public String getAttrVal() {
		if (StringUtilCustom.isEmpty(attrVal) || (!"NF".equals(fieldTypeCode) && !"RF".equals(fieldTypeCode))) {
			return attrVal;
		}
		if (attrVal.matches("0+(\\.0+)?")) {
			return "0";
		}
		return attrVal.replaceFirst("^0+(?!\\.)", "").replaceFirst("(\\.\\d*?)0+$", "$1").replaceFirst("\\.$",
				"");
	}
}
