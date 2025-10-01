package com.lgcns.svcp.prod.ui.prod.dto.admin.matrix;

import java.math.BigDecimal;
import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatrixMeasureMDto extends BaseDto {
    private String matrixCode;
    private long rowId;
    private BigDecimal matrixNumValue;
    private List<MatrixMeasureDDto> measureDDtos;
    private boolean isChanged;
}
