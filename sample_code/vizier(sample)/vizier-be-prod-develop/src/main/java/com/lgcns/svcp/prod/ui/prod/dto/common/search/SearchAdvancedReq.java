package com.lgcns.svcp.prod.ui.prod.dto.common.search;

import java.util.List;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchAdvancedReq extends BasePaginationDto {
    private String objCode;
    private String objName;
    private String itemCode;
    private String mctgrItemCode;
    private boolean onlyValidDtm;
    //    private List<DynamicFieldReq> general;
    private List<DynamicFieldReq> additional;
    private String language;
}
