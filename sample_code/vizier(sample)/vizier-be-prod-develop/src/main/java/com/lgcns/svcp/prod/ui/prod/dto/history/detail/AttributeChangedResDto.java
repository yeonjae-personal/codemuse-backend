package com.lgcns.svcp.prod.ui.prod.dto.history.detail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttributeChangedResDto {
	private Long workNo;
	private String labelId;
    private String commGroupCode;
    private String fieldTypeCode;
	private String beforeValue;
	private String afterValue;
}
