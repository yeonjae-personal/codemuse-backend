package com.lgcns.svcp.prod.ui.prod.dto.component;

import lombok.Data;

@Data
public class ComponentGeneralDto {
    private String uuid;
    private String code;
    private String name;
    private String amount;
    private String itemType;
    private String itemCode;
    private String startDate;
    private String endDate;
    private String relationStartDate;
    private String relationEndDate;
    private Long numbOfResources;
}
