package com.lgcns.svcp.prod.util.excel;

import org.apache.poi.ss.usermodel.Workbook;

import jakarta.servlet.http.HttpServletResponse;

public interface ExcelHelper {
	void downloadExcel(ExcelInput input, HttpServletResponse response, boolean isCustomTemplate);

	String[] createTitleHeader(Object... object);

	void createSheet(ExcelWriter excelWriter, ExcelInput excelInput);

	default void handleCustomTemplate(Workbook workbook, Object object) {
	}

	ExcelHelper of(Class<?> tClass);
}
