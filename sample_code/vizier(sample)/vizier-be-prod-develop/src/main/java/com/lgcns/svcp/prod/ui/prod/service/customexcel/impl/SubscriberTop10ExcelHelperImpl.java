package com.lgcns.svcp.prod.ui.prod.service.customexcel.impl;


import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.constant.DashboardConstant;
import com.lgcns.svcp.prod.entity.DsbdOfferSubCntEntity;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.SubscriberTop10ExcelHelper;
import com.lgcns.svcp.prod.util.excel.ExcelCellStyleSupportCustom;
import com.lgcns.svcp.prod.util.excel.ExcelHelperImpl;

@Component
public class SubscriberTop10ExcelHelperImpl extends ExcelHelperImpl implements SubscriberTop10ExcelHelper {
	
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("unchecked")
	@Override
	public void handleCustomTemplate(Workbook workbook, Object object) {
		ExcelCellStyleSupportCustom excelCell = new ExcelCellStyleSupportCustom(workbook);
		Map<String, Object> maps = (Map<String, Object>) object;
		XSSFSheet sheet = (XSSFSheet) workbook.getSheet(DashboardConstant.SUBSCRIBE_TOP_10_ITEM_NAME);
		createBatchDateStyle(sheet, maps);
		createStyleStatusColumn(sheet, maps, excelCell);
	}

	@SuppressWarnings("unchecked")
	private void createStyleStatusColumn(Sheet sheet, Map<String, Object> maps, ExcelCellStyleSupportCustom excelCellStyle) {
		List<DsbdOfferSubCntEntity> datas = (List<DsbdOfferSubCntEntity>) maps.get("datas");
		int index = 4;
		for (DsbdOfferSubCntEntity item: datas) {
			Row row = sheet.getRow(index);
			Font font = excelCellStyle.createFont();
			Cell cellStatus = row.getCell(4);
			CellStyle cellStyle = excelCellStyle.createCellStyle();
			if (item.getSaleValidEndDtm() == null) {
				font.setColor(IndexedColors.LIGHT_BLUE.getIndex());
			} else {
				font.setColor(IndexedColors.RED1.getIndex()); 
			}
			cellStyle.setFont(font);
			cellStatus.setCellStyle(cellStyle);
			index++;
		}
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
