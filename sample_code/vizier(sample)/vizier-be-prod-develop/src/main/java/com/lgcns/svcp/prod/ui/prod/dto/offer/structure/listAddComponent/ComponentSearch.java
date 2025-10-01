package com.lgcns.svcp.prod.ui.prod.dto.offer.structure.listAddComponent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComponentSearch {
    private String offerUUID;
    private String offerItemCode;
    private String componentType;
    private String itemCode;
    private String code;
    private String name;
    private Integer page;
    private Integer size;
}
