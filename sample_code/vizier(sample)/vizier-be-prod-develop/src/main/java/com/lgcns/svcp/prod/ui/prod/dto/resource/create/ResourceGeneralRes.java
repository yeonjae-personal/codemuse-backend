package com.lgcns.svcp.prod.ui.prod.dto.resource.create;

import com.lgcns.svcp.prod.ui.prod.dto.item.Item;
import lombok.Data;

import java.util.List;

@Data
public class ResourceGeneralRes {
    private String uuid;
    private String name;
    private String code;
    private String itemCode;
    private List<Item> itemCodeList;
    private String typeCode;
    private String startDate;
    private String endDate;
    private String relationStartDate;
    private String relationEndDate;
    private String overview;
}
