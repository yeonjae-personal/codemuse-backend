package com.lgcns.svcp.prod.ui.prod.dto.offer.structure.update;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferComponentStructureReq extends BaseDto {
    private String offerUuid;
    private String objUuid;
    private String relationValidStartDtm;
    private String relationValidEndDtm;
    private String workTypeCode;
}
