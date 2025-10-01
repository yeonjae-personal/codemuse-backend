package com.lgcns.svcp.prod.ui.prod.dto.admin.table;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.util.paging.PageResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableStrcTypeDto extends BaseDto {
    private String tableTypeCode;
    private String tableTypeName;
    private String useYn;
    private long sortNo;
    private PageResult<?> tableStrcDtos;
}
