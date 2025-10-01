package com.lgcns.svcp.prod.ui.prod.dto.admin.factor;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FactorDto extends BaseDto {
    private String factorCode;
    private String factorName;
    private String factorTypeCode;
    private String useYn;
    private List<FactorValueDto> factorValueLst;
}
