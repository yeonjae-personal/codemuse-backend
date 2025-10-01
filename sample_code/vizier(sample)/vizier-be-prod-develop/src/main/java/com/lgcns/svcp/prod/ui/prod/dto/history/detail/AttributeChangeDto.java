package com.lgcns.svcp.prod.ui.prod.dto.history.detail;

import lombok.Data;

@Data
public class AttributeChangeDto extends ChangedDto {
	private String labelId;
    private String commGroupCode;
    private String fieldTypeCode;
	private String beforeValue;
	private String afterValue;
}
