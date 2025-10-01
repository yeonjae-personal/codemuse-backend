package com.lgcns.svcp.prod.ui.prod.dto.admin.matrix;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatrixMeasureDDto extends BaseDto {
    private String matrixCode;
    private long rowId;
    private String factorCode;
    private String factorValueCode;

    private String factorValueName;
}
