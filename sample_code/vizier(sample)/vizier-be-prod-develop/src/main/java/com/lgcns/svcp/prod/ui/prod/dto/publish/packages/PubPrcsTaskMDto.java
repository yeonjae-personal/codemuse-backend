package com.lgcns.svcp.prod.ui.prod.dto.publish.packages;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.ui.prod.enums.publish.PubPrcsStusCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PubPrcsTaskMDto extends BaseDto {
	private String pubRqstTaskCode;
	private String pubRqstTaskCodeName;
	private PubPrcsStusCode pubPrcsStusCode;
	private String pubPrcsRsvDtm;
	private String pubPrcsStartDtm;
	private String pubPrcsEndDtm;
	private String pubPrcsRslt;
	private String pubPrcsMsg;
}
