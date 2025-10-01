package com.lgcns.svcp.prod.ui.prod.dto.label;

import com.lgcns.svcp.prod.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelItemUpdateDto extends BaseEntity {
	
	private String langCode;
	private String regionCode;
	private String labelName;
	private String labelId;
	private String labelType;
	private String labelDscr;
}
