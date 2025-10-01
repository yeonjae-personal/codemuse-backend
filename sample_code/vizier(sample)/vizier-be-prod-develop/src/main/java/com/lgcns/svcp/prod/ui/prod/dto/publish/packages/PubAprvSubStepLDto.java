package com.lgcns.svcp.prod.ui.prod.dto.publish.packages;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.ui.prod.enums.YesNo;
import com.lgcns.svcp.prod.ui.prod.enums.publish.AprvStusCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PubAprvSubStepLDto extends BaseDto {
	private String pubAprvUuid;
	private long sortNo;
	private long subSortNo;
	private String aprvUser;
	private String aprvUserDeptCd;
	private AprvStusCode aprvStusCode;
	private String aprvStusDscr;
	private String aprvDtm;
	private YesNo useYn;
}
