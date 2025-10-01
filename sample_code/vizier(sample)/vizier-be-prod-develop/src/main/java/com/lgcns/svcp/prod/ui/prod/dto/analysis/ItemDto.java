package com.lgcns.svcp.prod.ui.prod.dto.analysis;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto extends BasePaginationDto {
	private String prodUuid;
	private String type;
	private String detlType;
	private String subType;
	private String prodItemCd;
	private String prodItemNm;
	private String objCode;
	private String objName;
	private String objUuid;
	private String itemCode;
	private String validStartDtm;
	private String validEndDtm;
	private int trgtProdItemCount;
	private int baseProdItemCount;
	private boolean onlyValidDtm;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<AdditionalDetailDto> additional;
}
