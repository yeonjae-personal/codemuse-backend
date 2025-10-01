package com.lgcns.svcp.prod.ui.prod.dto.admin.factor;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.util.paging.PageResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FactorTypeDto extends BaseDto {
    private String factorTypeCode;
    private String factorTypeName;
    private String useYn;
    private int sortNo;
    private List<FactorDto> factorLst;
    private PageResult<?> factorSearchLst;
}
