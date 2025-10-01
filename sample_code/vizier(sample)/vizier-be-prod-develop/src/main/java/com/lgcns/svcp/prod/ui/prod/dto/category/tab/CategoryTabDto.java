package com.lgcns.svcp.prod.ui.prod.dto.category.tab;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Data;

@Data
public class CategoryTabDto extends BaseDto {
    private String ctgrTabUuid;
    private String ctgrTabName;
    private int sortNo;
    private String useYn;
}
