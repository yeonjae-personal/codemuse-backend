package com.lgcns.svcp.prod.ui.prod.service.customexcel.impl;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.ui.prod.service.customexcel.RecentWorkExcelHelper;
import com.lgcns.svcp.prod.util.excel.ExcelHelperImpl;

@Component
public class RecentWorkExcelHelperImpl extends ExcelHelperImpl implements RecentWorkExcelHelper {
	
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("unchecked")
	@Override
	public void handleCustomTemplate(Workbook workbook, Object object) {
		Map<String, Object> maps = (Map<String, Object>) object;
		XSSFSheet sheet = (XSSFSheet) workbook.getSheet("recentlywork");
		createBatchDateStyle(sheet, maps);
	}
	
	private void createBatchDateStyle(Sheet sheet, Map<String, Object> maps) {
		int rowNumer = 3;
		sheet.shiftRows(0, sheet.getLastRowNum(), rowNumer);
		for (int i = 0; i < rowNumer; i++) {
			sheet.createRow(i);
		}
		int firstRow = 1;
		int lastRow = 1;
		int firstCol = 5;
		int lastCol = 7;
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
		Row row = sheet.getRow(1);
        Cell cell = row.createCell(5);
		cell.setCellValue(messageSource.getMessage("dashboard.subscribertop10.excel.baseon", 
				null, LocaleContextHolder.getLocale())+": "+maps.get("batchDate").toString());
	}
}
