package com.lgcns.svcp.prod.util.excel;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.MethodInvoker;

import com.lgcns.svcp.prod.util.StringUtilCustom;

public class ExcelWriter {
	
	protected Workbook workbook;
	protected String excelType;
	private ExcelCellStyleSupport style;
	protected String[] titles = null;
	protected boolean isAutoCellSize = false;

	public static final String XLS = "xls";

	public static final String XLSX = "xlsx";

	private final int MAX_COLUMN_WIDTH = 40 * 261;

	public ExcelWriter(String excelType) {
		if (XLS.equalsIgnoreCase(excelType)) {
			workbook = new HSSFWorkbook();
		} else if (XLSX.equalsIgnoreCase(excelType)) {
			workbook = new XSSFWorkbook();
		}
		this.excelType = excelType;
		initStyle();
	}

	protected void initStyle() {
		style = createCellStyleSupport(workbook);
	}

	public ExcelCellStyleSupport createCellStyleSupport(Workbook workbook) {
		return new ExcelCellStyleSupport(workbook);
	}

	public Workbook getWorkbook() {
		return this.workbook;
	}

	public ExcelCellStyleSupport getStyle() {
		return style;
	}

	public void createTitle(String[] titleStrs, Sheet sheet) {
		Row keyrow = sheet.createRow(2);
		for (int inx = 0; inx < titleStrs.length; inx++) {
			Cell cell = keyrow.createCell((short) inx + 1);
			cell.setCellStyle(style.getTitleCellStyle());
			cell.setCellValue(titleStrs[inx]);
		}
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public void setAutoCellSize(boolean isAutoCellSize) {
		this.isAutoCellSize = isAutoCellSize;
	}

	public <T> void createSheet(List<?> inputList, String sheetName) {
		Sheet sheet = (sheetName != null) ? workbook.createSheet(sheetName) : workbook.createSheet();

		if (inputList == null) {
			return;
		}

		String[] fieldNames = getFieldName(inputList.get(0).getClass());
		int fieldSize = fieldNames.length;
		int[] maxLength = new int[fieldSize];

		List<String> titleList = new ArrayList<String>();

		if (this.titles == null) {
			this.titles = fieldNames;
		} else if (this.titles.length != fieldNames.length) {
			throw new RuntimeException("The number of excel titles and vo parameters should be same.");
		}

		for (int i = 0; i < titles.length; i++) {
			maxLength[i] = titles[i].getBytes().length;
		}
		createTitle(this.titles, sheet);
		MethodInvoker[] invoker = null;

		// Camel case로 변환하는 Util이 나오면 엑셀칼럼값이 CamelCase로 변환되게 코드수정해야함.
		for (int iny = 0, rowSize = inputList.size(); iny < rowSize; iny++) {
			Row valueRow = sheet.createRow(iny + 3);
			for (int inx = 0; inx < fieldSize; inx++) {
				Cell cell = valueRow.createCell(inx + 1);
				Object value = null;
				try {
					if (invoker == null) {
						invoker = new MethodInvoker[fieldSize];
					}
					if (invoker[inx] == null) {
						invoker[inx] = new MethodInvoker();
						invoker[inx].setTargetMethod("get" + StringUtilCustom.toUpperCaseFirstLetter(fieldNames[inx]));
						invoker[inx].setTargetObject(inputList.get(iny));
						invoker[inx].prepare();
						value = invoker[inx].invoke();
					} else {
						invoker[inx].setTargetObject(inputList.get(iny));
						value = invoker[inx].invoke();
					}
				} catch (Exception e) {
					throw new RuntimeException("Exception while converting " + inputList.get(0).getClass() + "to csv", e);
				}

				int valueLength = 0;
				try {
					if (value != null) {
						valueLength = value.toString().getBytes("EUC-KR").length;
					}
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException("Error: " + e.getMessage());
				}
				maxLength[inx] = Math.max(maxLength[inx], valueLength);
				cell.setCellStyle(style.getCellStyle(value));
				setCellValue(cell, value);
			}
		}
		
        sheet.createFreezePane(0, 3);
        sheet.setColumnWidth(0, 430);
        Row valueRow = sheet.createRow(1);
        Cell cell = valueRow.createCell(1);
        cell.setCellValue("■ "+sheetName);
        sheet.setDisplayGridlines(false);
        if (isAutoCellSize) {
			for (int i = 0; i < fieldSize; i++) {
				int width = Math.min(maxLength[i] * 256 + 512, MAX_COLUMN_WIDTH);				
				sheet.setColumnWidth(i + 1, width);
			}
		}
	}

	public String[] getFieldName(Class<?> dataClass) {
		Field[] fields = dataClass.getDeclaredFields();

		List<String> fieldNameList = new ArrayList<String>();
		for (int inx = 0, length = fields.length; inx < length; inx++) {

			String fieldName = fields[inx].getName();
			if (!fields[inx].isSynthetic() && !fieldName.startsWith("$")) {
				fieldNameList.add(fieldName);
			}

		}
		return fieldNameList.toArray(new String[fieldNameList.size()]);
	}

	public void setCellValue(Cell cell, Object data) {

		if (data == null) { // CellType.BLANK
			cell.setBlank();
		} else if (data instanceof String) {
			String value = (String) data;
			if (value.startsWith("=")) { // CellType.FORMULA
				cell.setCellFormula(value.substring(1));
			} else { // CellType.STRING
				cell.setCellValue(value);
			}
		} else if (data instanceof Number) { // CellType.NUMERIC
			cell.setCellValue(((Number) data).doubleValue());
		} else if (data instanceof Boolean) {
			cell.setCellValue((Boolean) data);
		} else if (data instanceof Date) { // CellType.NUMERIC
			cell.setCellValue((Date) data);
		} else if (data instanceof LocalDate) { // CellType.NUMERIC
			cell.setCellValue((LocalDate) data);
		} else if (data instanceof LocalDateTime) { // CellType.NUMERIC
			cell.setCellValue((LocalDateTime) data);
		} else if (data instanceof Calendar) { // CellType.NUMERIC
			cell.setCellValue(((Calendar) data).getTime());
		} else if (data instanceof RichTextString) {
			cell.setCellValue((RichTextString) data);
		} else {
			cell.setCellValue(String.valueOf(data));
		}
	}

}
