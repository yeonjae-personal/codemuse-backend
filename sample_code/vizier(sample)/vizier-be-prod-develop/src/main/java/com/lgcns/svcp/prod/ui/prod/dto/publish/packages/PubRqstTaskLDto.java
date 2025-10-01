package com.lgcns.svcp.prod.ui.prod.dto.publish.packages;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.ui.prod.enums.YesNo;
import com.lgcns.svcp.prod.ui.prod.enums.publish.PubRqstTaskDetlStusCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PubRqstTaskLDto extends BaseDto {
	private String pubRqstTaskCode;
	private long chngDataSeq;
	private PubRqstTaskDetlStusCode pubRqstDetlStusCode;
	private YesNo vldateFnshYn;
}
