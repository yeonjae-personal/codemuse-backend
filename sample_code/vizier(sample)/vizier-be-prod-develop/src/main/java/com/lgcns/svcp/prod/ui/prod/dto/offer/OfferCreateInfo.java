package com.lgcns.svcp.prod.ui.prod.dto.offer;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.OfferStructureDetailRes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferCreateInfo extends ItemMappingDetailDto {
    private List<OfferStructureDetailRes> structures;
}
