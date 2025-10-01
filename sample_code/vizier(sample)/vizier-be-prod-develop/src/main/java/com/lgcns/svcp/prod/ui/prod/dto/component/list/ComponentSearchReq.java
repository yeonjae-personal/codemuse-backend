package com.lgcns.svcp.prod.ui.prod.dto.component.list;

import com.lgcns.svcp.prod.ui.prod.enums.ComponentItemCode;
import com.lgcns.svcp.prod.ui.prod.enums.ComponentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentSearchReq {
    private String offerUUID;
    private ComponentType componentType;
    private ComponentItemCode componentSubType;
    private String code;
    private String name;
    private boolean onlyValidDtm;
    private Integer page;
    private Integer size;
}
