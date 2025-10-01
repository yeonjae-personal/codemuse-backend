package com.lgcns.svcp.prod.ui.prod.dto.admin.table;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveTableDataReqDto {
    private List<List<String>> deleteData;
    private List<List<String>> updateData;
    private List<List<String>> addData;
}
