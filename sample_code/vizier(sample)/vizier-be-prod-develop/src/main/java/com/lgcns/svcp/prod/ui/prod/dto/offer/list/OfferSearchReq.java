package com.lgcns.svcp.prod.ui.prod.dto.offer.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferSearchReq {
    private String objCode;
    private String objName;
    private String itemCode;
    private boolean onlyValidDtm;
    private Integer page;
    private Integer size;

    public OfferSearchReq(String objCode, String objName, String itemCode) {
        this.objCode = objCode;
        this.objName = objName;
        this.itemCode = itemCode;
    }
}
