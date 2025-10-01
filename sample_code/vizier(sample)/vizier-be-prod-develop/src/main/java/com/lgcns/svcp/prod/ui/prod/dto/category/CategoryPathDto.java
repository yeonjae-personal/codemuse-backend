package com.lgcns.svcp.prod.ui.prod.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryPathDto extends BasePaginationDto{
	@JsonIgnore
	private String catgUuid;
	private String level1;
	private String level2;
	private String level3;
	private String level4;
	private String level5;
	private String offerCd;
	private String offerNm;
	private String level;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	@JsonIgnore
    private String ctgrNodeName;
    @JsonIgnore
    private String ctgrTabUuid;
}
