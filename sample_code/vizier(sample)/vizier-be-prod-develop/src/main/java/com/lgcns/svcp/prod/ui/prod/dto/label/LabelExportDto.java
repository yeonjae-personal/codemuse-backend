package com.lgcns.svcp.prod.ui.prod.dto.label;

import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CustomTitleHeader
public class LabelExportDto {
	
	private String labelCode;

	private String langCode;

	private String regionCode;

	private String labelName;
	
	private String labelDscr;
}
