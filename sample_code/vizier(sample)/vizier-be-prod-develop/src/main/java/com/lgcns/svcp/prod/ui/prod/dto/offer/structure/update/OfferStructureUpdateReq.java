package com.lgcns.svcp.prod.ui.prod.dto.offer.structure.update;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferStructureUpdateReq {
    private String offerUuid;
    private String chgDeptName;
    private String chgUser;
    private List<OfferComponentStructureReq> componentList;
}