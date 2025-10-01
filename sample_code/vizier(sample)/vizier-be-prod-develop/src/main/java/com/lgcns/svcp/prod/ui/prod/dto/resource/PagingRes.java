package com.lgcns.svcp.prod.ui.prod.dto.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingRes<T> {
    private int total;
    private int totalItem;
    private List<T> dataList;
}
