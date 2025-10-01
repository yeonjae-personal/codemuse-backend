package com.lgcns.svcp.prod.ui.prod.dto.admin.factor;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FactorValueDto extends BaseDto {
    private String factorValueCode;
    private String factorValueName;
    private long value;
    private String factorCode;
    private String useYn;
}
