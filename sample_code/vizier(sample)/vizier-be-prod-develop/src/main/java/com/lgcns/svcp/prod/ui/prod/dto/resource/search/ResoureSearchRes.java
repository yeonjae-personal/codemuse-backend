package com.lgcns.svcp.prod.ui.prod.dto.resource.search;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResoureSearchRes {
	// Common
	private String objUuid;
	private String objCode;
	private String objName;
	private String itemCode;
	private String validStartDtm;
	private String validEndDtm;
	private String dplcTrgtUuid;
	private String chgDeptName;
	private String chgUser;
	private String ovwCntn;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	
	private List<GeneralDetailDto> general;
	private List<AdditionalDetailDto> additional;

	private String mctgrItemCode;
	private String lctgrItemCode;
}
