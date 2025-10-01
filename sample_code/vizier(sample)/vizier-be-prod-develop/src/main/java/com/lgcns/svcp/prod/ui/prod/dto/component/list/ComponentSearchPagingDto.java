package com.lgcns.svcp.prod.ui.prod.dto.component.list;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentSearchPagingDto extends BasePaginationDto {
    private String offerUUID;
    private String uuid;
    private String code;
    private String name;
    private String itemCode;
    private String itemType;
    private String type;
    private String subType;
    private boolean onlyValidDtm;
}
