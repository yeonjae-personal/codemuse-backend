package com.lgcns.svcp.prod.ui.prod.dto.component.list;

import lombok.Data;

@Data
public class ComponentSearchRes {
    private String uuid;
    private String code;
    private String name;
    private String itemCode;
    private String itemType;
    private String type;
    private String subType;
    private String startDate;
    private String endDate;
}
