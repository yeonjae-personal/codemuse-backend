package com.lgcns.svcp.prod.ui.prod.dto.admin.matrix;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchMatrixResDto {
    private String matrixCode;
    private String matrixCodeName;
    private String useYn;
    private Object factorCodes;

    public String[] getFactorCodes() {
        if (factorCodes instanceof String factorCodesString) {
            return factorCodesString.trim().isEmpty() ? new String[0] : factorCodesString.split(",");
        }
        return new String[0];
    }
}
