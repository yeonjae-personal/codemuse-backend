package com.lgcns.svcp.prod.ui.prod.dto.label;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelItemDto {
	
	@NotEmpty
	private String langCode;
	
	@NotEmpty
	private String regionCode;
	
	@NotEmpty
	private String labelName;
	
	private String labelDscr;
	
	private String labelCode;
}
