package com.lgcns.svcp.prod.ui.prod.dto.common.search;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationSearchRes {
	
	private String objUuid;
	private String objCode;
	private String objName;
    private String itemCode;
	private String validStartDtm;
	private String validEndDtm;
	
	private List<GeneralDetailDto> general;
	private List<AdditionalDetailDto> additional;
}
