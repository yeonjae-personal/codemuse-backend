package com.lgcns.svcp.prod.ui.prod.dto.component.addResource;

import com.lgcns.svcp.prod.ui.prod.dto.item.Item;
import lombok.Data;

import java.util.List;

@Data
public class ComponentAddResourceRes {
    private String uuid;
    private String name;
    private String code;
    private String itemCode;
    private String startDate;
    private String endDate;
    private String overview;
}
