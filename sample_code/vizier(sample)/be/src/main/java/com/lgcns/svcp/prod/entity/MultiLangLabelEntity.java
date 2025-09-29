package com.lgcns.svcp.prod.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiLangLabelEntity extends BaseEntity {
	private String labelId;
	private String langCode;
	private String regionCode;
	private String labelName;
	private String labelType;
	private String labelDscr;
}
