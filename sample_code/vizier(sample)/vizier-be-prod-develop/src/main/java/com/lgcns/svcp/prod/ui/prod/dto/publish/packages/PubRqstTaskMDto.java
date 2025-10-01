package com.lgcns.svcp.prod.ui.prod.dto.publish.packages;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.ui.prod.enums.publish.PubPrcsTypeCode;
import com.lgcns.svcp.prod.ui.prod.enums.publish.PubRqstStusCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PubRqstTaskMDto extends BaseDto {
	private String pubRqstTaskCode;
	private String pubRqstTaskCodeName;
	private String pubRqstTaskPubr;
	private String pubRqstTaskPubrDeptCd;
	private PubRqstStusCode pubRqstStusCode;
	private String pubRqstBfrStusCode;
	private PubPrcsTypeCode pubPrcsTypeCode;
	private String crteDtm;
	private String vldateDtm;
	private String duedDtm;
	private String exprDtm;
	private String ovwCntn;
}
