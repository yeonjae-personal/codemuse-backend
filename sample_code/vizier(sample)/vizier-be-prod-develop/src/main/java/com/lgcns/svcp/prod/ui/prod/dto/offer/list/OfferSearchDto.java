package com.lgcns.svcp.prod.ui.prod.dto.offer.list;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferSearchDto extends BasePaginationDto {
    private String objCode;
    private String objName;
    private boolean onlyValidDtm;
    private String itemCode;
}
