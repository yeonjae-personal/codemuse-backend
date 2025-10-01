package com.lgcns.svcp.prod.ui.prod.dto.history.detail;

import lombok.Data;

@Data
public class StructureChangeDto extends ChangedDto {
	private String workTypeCode;
	private String mctgrItemName;
	private String itemCodeName;
	private String objName;
}
