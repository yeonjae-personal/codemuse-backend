package com.lgcns.svcp.prod.util.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelCellStyleSupportCustom extends ExcelCellStyleSupport {

	public ExcelCellStyleSupportCustom(Workbook workbook) {
		super(workbook);
	}
	
	@Override
	public Font createFont() {
		return super.createFont();
	}
	
	@Override
	public CellStyle createCellStyle() {
		return super.createCellStyle();
	}
	
	@Override
	public CellStyle createTitleCellStyle() {
		return super.createTitleCellStyle();
	}
}
