package com.lgcns.svcp.prod.ui.prod.dto.component.addResource;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentAddResourceDto extends BasePaginationDto {
    private String componentUUID;
    private String uuid;
    private String name;
    private String code;
    private String itemCode;
    private String rscTypeCode;
    private String startDate;
    private String endDate;
    private String relationStartDate;
    private String relationEndDate;
    private String overview;
    private String componentCreateType;
}
