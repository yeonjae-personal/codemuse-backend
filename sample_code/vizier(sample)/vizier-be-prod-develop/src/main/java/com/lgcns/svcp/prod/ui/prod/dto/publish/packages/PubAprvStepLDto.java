package com.lgcns.svcp.prod.ui.prod.dto.publish.packages;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.ui.prod.enums.YesNo;
import com.lgcns.svcp.prod.ui.prod.enums.publish.AprvStepCode;
import com.lgcns.svcp.prod.ui.prod.enums.publish.AprvStusCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PubAprvStepLDto extends BaseDto {
	private String pubAprvUuid;
	private long sortNo;
	private AprvStepCode pubAprvStepCode;
	private AprvStusCode aprvStusCode;
	private String aprvStusDscr;
	private String aprvDtm;
	private long lmtTm;
	private YesNo useYn;

	private List<PubAprvSubStepLDto> pubAprvSubStepLDtos;
}
