package com.lgcns.svcp.prod.ui.prod.dto.category;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Data;

@Data
public class CategoryTreeDto extends BaseDto {
    private String ctgrNodeUuid;
    private String ctgrTabUuid;
    private String ctgrNodeName;
    private String hpstCtgrNodeUuid;
    private String tclsCtgrYn;
    private String chgDeptName;
    private String chgUser;
    private String ctgrOvwCntn;
    private String useYn;

    private String isLeafCategoryNode;
    private int totalOfferCount;
    private String level;
    private Boolean showChilderen = false;
    private List<CategoryTreeDto> children;
}
