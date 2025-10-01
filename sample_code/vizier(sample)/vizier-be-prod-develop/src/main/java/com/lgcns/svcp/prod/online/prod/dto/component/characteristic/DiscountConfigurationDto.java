package com.lgcns.svcp.prod.online.prod.dto.component.characteristic;

import java.util.List;

import com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto;

import lombok.Data;

@Data
public class DiscountConfigurationDto {
	private String dcCfgrtUuid;
	private String dcCfgrtCode;
	private String dcCfgrtName;
	private String dcRgstDivCode;
	private String dcApplyPriodCnt;
	private String dcApplyPriodUnitCode;
	private String usePriodApplyCode;
	private String dplcnRgstPosibYn;
	private String dcCfgrtOvwCntn;
	private String rgstDeptName;
	private String dplcTrgtUuid;
	private String validStartDtm;
	private String validEndDtm;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	private List<AdditionalColumnsDto> additionalColumns;
}
