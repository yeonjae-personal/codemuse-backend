package com.lgcns.svcp.prod.ui.prod.dto.admin.table;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableStrcDto extends BaseDto {
    private String tableName;
    private String tableTypeCode;
    private String tableDscr;
    private String useYn;
}
