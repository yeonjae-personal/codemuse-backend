package com.lgcns.svcp.prod.ui.prod.dto.offer;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;

import lombok.Data;

@Data
public class SearchGroupWithDetailResDto {
	private String offerUuid;
	private String offerCode;
	private String offerName;
	private String offerType;
    private List<GeneralDetailDto> general;
    private List<AdditionalDetailDto> additional;

}
