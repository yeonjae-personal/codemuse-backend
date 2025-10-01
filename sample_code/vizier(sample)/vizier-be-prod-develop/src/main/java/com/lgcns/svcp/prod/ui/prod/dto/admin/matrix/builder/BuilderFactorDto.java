package com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.builder;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuilderFactorDto {
    private long seqNo;
    private String factorCode;
    private String factorName;
    private List<BuilderFactorValueDto> factorValues;
}
