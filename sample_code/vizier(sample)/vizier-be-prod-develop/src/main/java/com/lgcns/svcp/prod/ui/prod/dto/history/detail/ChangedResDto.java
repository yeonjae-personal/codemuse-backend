package com.lgcns.svcp.prod.ui.prod.dto.history.detail;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangedResDto {
	private String changeTypeName;
	private String chgDeptName;
	private String chgUser;
	private List<AttributeChangedResDto> fields;
	private List<StructureChangeResDto> structures;
}
