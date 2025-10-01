package com.lgcns.svcp.prod.ui.prod.dto.history.detail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StructureChangeResDto {
	private Long workNo;
	private String workTypeCode;
	private String mctgrItemName;
	private String itemCodeName;
	private String objName;
}
