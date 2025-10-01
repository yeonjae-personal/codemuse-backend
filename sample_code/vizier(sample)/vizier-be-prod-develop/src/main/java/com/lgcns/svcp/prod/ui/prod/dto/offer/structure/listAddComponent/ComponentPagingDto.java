package com.lgcns.svcp.prod.ui.prod.dto.offer.structure.listAddComponent;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentPagingDto extends BasePaginationDto {
    private String offerUUID;
    private String objUUID;
    private String objName;
    private String objCode;

    private String itemCode;
    private String validStartDtm;
    private String validEndDtm;
    private String componentType;
}
