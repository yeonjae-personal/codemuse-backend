package com.lgcns.svcp.prod.ui.prod.dto.offer.structure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferStructureComponent {
    private String mctgrItemCode;
    private String mctgrItemName;
    private String itemCode;
    private String itemCodeName;
    private String objUuid;
    private String objCode;
    private String objName;
    private String validStartDtm;
    private String validEndDtm;
    private String relationValidStartDtm;
    private String relationValidEndDtm;
    private String strcTypeCode;
}
