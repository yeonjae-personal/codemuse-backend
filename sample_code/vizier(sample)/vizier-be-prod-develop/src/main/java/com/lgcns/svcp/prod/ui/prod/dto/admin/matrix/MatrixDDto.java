package com.lgcns.svcp.prod.ui.prod.dto.admin.matrix;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatrixDDto extends BaseDto {
    private String matrixCode;
    private String factorCode;
    private long seqNo;
}
