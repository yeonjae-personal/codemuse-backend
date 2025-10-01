package com.lgcns.svcp.prod.ui.prod.dto.multiEntity.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchItemRelationReq {
    private String objUuid;
    private boolean onlyValidDtm;
}
