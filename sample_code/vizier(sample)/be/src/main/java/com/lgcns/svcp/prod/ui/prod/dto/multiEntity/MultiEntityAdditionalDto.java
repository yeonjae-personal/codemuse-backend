package com.lgcns.svcp.prod.ui.prod.dto.multiEntity;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiEntityAdditionalDto extends BaseDto {
	private String attrUuid;
	private String entityCode;
	private String entityTypeCode;
	private String fieldTypeCode;
	private String commGroupCode;
	private String labelId;
	private String requiredYn;
	private String attrMaxLength;
	private String sortNo;
	private String attrVal;
	private String labelDscr;
}
