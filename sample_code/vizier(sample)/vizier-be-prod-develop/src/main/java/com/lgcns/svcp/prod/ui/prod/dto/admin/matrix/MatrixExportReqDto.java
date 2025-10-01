package com.lgcns.svcp.prod.ui.prod.dto.admin.matrix;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.builder.BuilderFactorDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatrixExportReqDto {
    private String matrixCode;
    private String matrixCodeName;

    private List<MatrixMeasureMDto> measureMDtos;
    private List<BuilderFactorDto> builderDtos;
}
