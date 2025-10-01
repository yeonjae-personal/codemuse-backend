package com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.builder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuilderFactorValueDto {
    private String factorCode;
    private String factorValueCode;
    private String factorValueName;
    private boolean isInUse;
}
