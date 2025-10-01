package com.lgcns.svcp.prod.ui.prod.service.customexcel;

import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixExportReqDto;
import com.lgcns.svcp.prod.util.excel.ExcelHelper;

import jakarta.servlet.http.Part;

public interface MatrixExcelHelper extends ExcelHelper {
    String DEFAULT_FONT = "LG스마트체 Regular";
    short DEFAULT_FONT_SIZE = 10;

    MatrixExportReqDto parseMatrixFromExcel(Part filePart, MatrixExportReqDto matrixExportReqDto);
}
