package com.lgcns.svcp.prod.ui.prod.dto.component.search;

import com.lgcns.svcp.prod.ui.prod.dto.common.search.SearchAdvancedReq;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentSearchAdvancedReq extends SearchAdvancedReq {
    private String offerUuid;
}
