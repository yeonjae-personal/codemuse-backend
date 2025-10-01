package com.lgcns.svcp.prod.ui.prod.dto.common.metadata;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.util.StringUtilCustom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralDetailDto extends BaseDto {
	private String objUuid;
	private String colName;
	private String fieldTypeCode;
	private String editYn;
	private int sortNo;
	private String useYn;
	private String attrMaxLength;
	private String requiredYn;
	private String labelId;
	private String attrVal;

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
