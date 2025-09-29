package com.lgcns.svcp.prod.ui.prod.dto.extend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetReqDto {
	private String offerUuid;
	private boolean onlyValidDtm;
	private boolean includeGroup;
}
