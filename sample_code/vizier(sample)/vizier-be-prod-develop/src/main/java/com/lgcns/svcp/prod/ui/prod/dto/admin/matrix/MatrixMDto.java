package com.lgcns.svcp.prod.ui.prod.dto.admin.matrix;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatrixMDto extends BaseDto {
    private String matrixCode;
    private String matrixCodeName;
    private String useYn;

    private List<MatrixDDto> matrixDDtos;
    private List<MatrixMeasureMDto> measureMDtos;
}
