package com.lgcns.svcp.prod.ui.prod.dto.history.save;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditonalAttributeHistDto extends BaseDto {
	private long workNo;
	private String objUuid;
	private String attrUuid;
	private String attrVal;
	private String updUserDeptName;
	private String attrValUpdUser;
}
