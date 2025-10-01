package com.lgcns.svcp.prod.ui.prod.dto.admin.table;

import java.util.Map;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveTableDataDto extends BaseDto {
    private Map<String, Object> columnData;
    private String tableName;
    private Map<String, Object> columnPrimaryKeys;
}
