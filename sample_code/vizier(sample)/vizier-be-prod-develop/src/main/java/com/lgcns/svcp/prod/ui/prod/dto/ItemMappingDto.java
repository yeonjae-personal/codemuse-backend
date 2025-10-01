package com.lgcns.svcp.prod.ui.prod.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemMappingDto extends BaseDto {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String objUuid;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String objCode;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String objName;

    private String itemCode;
	private String validStartDtm;
	private String validEndDtm;
	
}
