package com.lgcns.svcp.prod.ui.prod.service.customexcel.impl;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationExcelDto;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.CustomValidationExcelHelper;
import com.lgcns.svcp.prod.util.excel.ExcelCellStyleSupportCustom;
import com.lgcns.svcp.prod.util.excel.ExcelHelperImpl;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.excel.ExcelWriter;

@Component
public class CustomValidationExcelHelperImpl extends ExcelHelperImpl implements CustomValidationExcelHelper {
	
	private final int MAX_COLUMN_WIDTH = 40 * 261;
		
	@Override
	@SuppressWarnings("unchecked")
	public void createSheet(ExcelWriter excelWriter, ExcelInput excelInput) {
		Workbook workbook = excelWriter.getWorkbook();
		Sheet sheet = workbook.createSheet(excelInput.getSheetName()); 
		ExcelCellStyleSupportCustom excelCellStyle = new ExcelCellStyleSupportCustom(excelWriter.getWorkbook());
		CellStyle createCellStyle = excelCellStyle.createCellStyle();
		createTitleValidation(sheet, excelWriter.getWorkbook());
		Map<Integer, List<CustomValidationExcelDto>> maps = (Map<Integer, List<CustomValidationExcelDto>>) excelInput.getObject();
		Integer index = 4;

		// Array to store the maximum length of each column
		int[] maxLengths = new int[12]; // Assuming you have 12 columns (0 to 11)

		// Initialize maxLengths with header lengths
		String[] headers = super.createTitleHeader();
		for (int i = 0; i < headers.length; i++) {
		    maxLengths[i + 1] = headers[i].length(); // Assuming headers start from column 1
		}

		for(Map.Entry<Integer, List<CustomValidationExcelDto>> entry : maps.entrySet()) {
		    Integer number = entry.getKey();
		    List<CustomValidationExcelDto> conditions = new ArrayList<>();
		    List<CustomValidationExcelDto> actions = new ArrayList<>();
		    for (CustomValidationExcelDto item: entry.getValue()) {
		        if (item.getCondType().equals("C")) {
		            conditions.add(item);
		        }
		        if (item.getCondType().equals("A")) {
		            actions.add(item);
		        }
		    }
		    Integer firstRow = index;
		    Integer lastRow = null;
		    if (conditions.size() >= actions.size()) {
		        Integer firstRowCondition = null;
		        Integer lastRowCondition = null;
		        Integer firstRowAction = null;
		        Integer lastRowAction = null;
		        for (int i = 1; i <= conditions.size(); i++) {
		            if (i == 1) {
		                firstRowCondition = index;
		                firstRowAction = index;
		            } else {
		                CustomValidationExcelDto conditionItem1 = conditions.get(i - 1 - 1);
		                CustomValidationExcelDto conditionItem2 = conditions.get(i - 1);
		                if (!conditionItem1.getItemCode().equals(conditionItem2.getItemCode())) {
		                    firstRowCondition = index;
		                    lastRowCondition = null;
		                } else {
		                    lastRowCondition = index;
		                }
		            }
		            Row row = sheet.createRow(index);
		            Cell cell0 = row.createCell(1);
		            cell0.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell0.setCellValue(number);
		            updateMaxLength(maxLengths, 1, number.toString().length());

		            CustomValidationExcelDto conditionItem = conditions.get(i - 1);
		            Cell cell1 = row.createCell(2);
		            cell1.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell1.setCellValue(conditionItem.getItemCodeName() != null ? conditionItem.getItemCodeName() : "");
		            updateMaxLength(maxLengths, 2, conditionItem.getItemCodeName() != null ? conditionItem.getItemCodeName().length() : 0);

		            Cell cell2 = row.createCell(3);
		            cell2.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell2.setCellValue(conditionItem.getLabelId() != null ? conditionItem.getLabelName() : "");
		            updateMaxLength(maxLengths, 3, conditionItem.getLabelName() != null ? conditionItem.getLabelName().length() : 0);

		            Cell cell3 = row.createCell(4);
		            cell3.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell3.setCellValue(conditionItem.getAttrValue() != null ? conditionItem.getAttrValue() : "");
		            updateMaxLength(maxLengths, 4, conditionItem.getAttrValue() != null ? conditionItem.getAttrValue().length() : 0);

		            Cell cell4 = row.createCell(5);
		            cell4.setCellStyle(createCellStyleValidation(createCellStyle));
		            Cell cell5 = row.createCell(6);
		            cell5.setCellStyle(createCellStyleValidation(createCellStyle));
		            Cell cell6 = row.createCell(7);
		            cell6.setCellStyle(createCellStyleValidation(createCellStyle));
		            Cell cell7 = row.createCell(8);
		            cell7.setCellStyle(createCellStyleValidation(createCellStyle));
		            Cell cell8 = row.createCell(9);
		            cell8.setCellStyle(createCellStyleValidation(createCellStyle));
		            Cell cell9 = row.createCell(10);
		            cell9.setCellStyle(createCellStyleValidation(createCellStyle));
		            Cell cell10 = row.createCell(11);
		            cell10.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell7.setCellValue(conditionItem.getRgstUser());
		            updateMaxLength(maxLengths, 8, conditionItem.getRgstUser() != null ? conditionItem.getRgstUser().length() : 0);

		            cell8.setCellValue(convertDate(conditionItem.getRgstDtm()));
		            updateMaxLength(maxLengths, 9, convertDate(conditionItem.getRgstDtm()).length());

		            cell9.setCellValue(conditionItem.getUpdUser());
		            updateMaxLength(maxLengths, 10, conditionItem.getUpdUser() != null ? conditionItem.getUpdUser().length() : 0);

		            cell10.setCellValue(convertDate(conditionItem.getUpdDtm()));
		            updateMaxLength(maxLengths, 11, convertDate(conditionItem.getUpdDtm()).length());

		            try {
		                CustomValidationExcelDto actionItem = actions.get(i - 1);
		                if (i != 1) {
		                    CustomValidationExcelDto actionItem1 = actions.get(i - 1 - 1);
		                    CustomValidationExcelDto actionItem2 = actions.get(i - 1);
		                    if (!actionItem1.getItemCode().equals(actionItem2.getItemCode())) {
		                        firstRowAction = index;
		                        lastRowAction = null;
		                    } else {
		                        lastRowAction = index;
		                    }
		                }
		                cell4.setCellValue(actionItem.getItemCodeName() != null ? actionItem.getItemCodeName() : "");
		                updateMaxLength(maxLengths, 5, actionItem.getItemCodeName() != null ? actionItem.getItemCodeName().length() : 0);

		                cell5.setCellValue(actionItem.getLabelId() != null ? actionItem.getLabelName() : "");
		                updateMaxLength(maxLengths, 6, actionItem.getLabelName() != null ? actionItem.getLabelName().length() : 0);
		                cell6.setCellValue(actionItem.getAttrValue() != null ? actionItem.getAttrValue() : "");
		                updateMaxLength(maxLengths, 7, actionItem.getAttrValue() != null ? actionItem.getAttrValue().length() : 0);
		            } catch (IndexOutOfBoundsException e) {	
		                lastRowAction = index;
		            }
		            if (i == conditions.size()) {
		                lastRow = index;
		            }
		            index++;
		        }
		        //merge row item condition
		        if (firstRowCondition != null && lastRowCondition != null && (lastRowCondition > firstRowCondition)) {
		            sheet.addMergedRegion(new CellRangeAddress(firstRowCondition, lastRowCondition, 2, 2));
		        }
		        //merge row item action
		        if (firstRowAction != null && lastRowAction != null && (lastRowAction > firstRowAction)) {
		            sheet.addMergedRegion(new CellRangeAddress(firstRowAction, lastRowAction, 5, 5));
		            sheet.addMergedRegion(new CellRangeAddress(firstRowAction, lastRowAction, 6, 6));
		            sheet.addMergedRegion(new CellRangeAddress(firstRowAction, lastRowAction, 7, 7));
		        }
		    } else {
		        Integer firstRowCondition = null;
		        Integer lastRowCondition = null;
		        Integer firstRowAction = null;
		        Integer lastRowAction = null;
		        for (int i = 1; i <= actions.size(); i++) {
		            if (i == 1) {
		                firstRowCondition = index;
		                firstRowAction = index;
		            }
		            Row row = sheet.createRow(index);
		            Cell cell0 = row.createCell(1);
		            cell0.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell0.setCellValue(number);
		            updateMaxLength(maxLengths, 1, number.toString().length());
		            Cell cell1 = row.createCell(2);
		            cell1.setCellStyle(createCellStyleValidation(createCellStyle));
		            Cell cell2 = row.createCell(3);
		            cell2.setCellStyle(createCellStyleValidation(createCellStyle));
		            Cell cell3 = row.createCell(4);
		            cell3.setCellStyle(createCellStyleValidation(createCellStyle));
		            try {
		                CustomValidationExcelDto conditionItem = conditions.get(i - 1);
		                if (i != 1) {
		                    CustomValidationExcelDto conditionItem1 = conditions.get(i - 1 - 1);
		                    CustomValidationExcelDto conditionItem2 = conditions.get(i - 1);
		                    if (!conditionItem1.getItemCode().equals(conditionItem2.getItemCode())) {
		                        firstRowCondition = index;
		                        lastRowCondition = null;
		                    } else {
		                        lastRowCondition = index;
		                    }
		                }
		                cell1.setCellValue(conditionItem.getItemCodeName() != null ? conditionItem.getItemCodeName() : "");
		                updateMaxLength(maxLengths, 2, conditionItem.getItemCodeName() != null ? conditionItem.getItemCodeName().length() : 0);

		                cell2.setCellValue(conditionItem.getLabelId() != null ? conditionItem.getLabelName() : "");
		                updateMaxLength(maxLengths, 3, conditionItem.getLabelName() != null ? conditionItem.getLabelName().length() : 0);
		                cell3.setCellValue(conditionItem.getAttrValue() != null ? conditionItem.getAttrValue() : "");
		                updateMaxLength(maxLengths, 4, conditionItem.getAttrValue() != null ? conditionItem.getAttrValue().length() : 0);
		            } catch (Exception e) {
		                lastRowCondition = index;
		            }
		            CustomValidationExcelDto actionItem = actions.get(i - 1);
		            if (i != 1) {
		                CustomValidationExcelDto actionItem1 = actions.get(i - 1 - 1);
		                CustomValidationExcelDto actionItem2 = actions.get(i - 1);
		                if (!actionItem1.getItemCode().equals(actionItem2.getItemCode())) {
		                    firstRowAction = index;
		                    lastRowAction = null;
		                } else {
		                    lastRowAction = index;
		                }
		            }
		            Cell cell4 = row.createCell(5);
		            cell4.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell4.setCellValue(actionItem.getItemCodeName() != null ? actionItem.getItemCodeName() : "");
		            updateMaxLength(maxLengths, 5, actionItem.getItemCodeName() != null ? actionItem.getItemCodeName().length() : 0);

		            Cell cell5 = row.createCell(6);
		            cell5.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell5.setCellValue(actionItem.getLabelId() != null ? actionItem.getLabelName() : "");
		            updateMaxLength(maxLengths, 6, actionItem.getLabelName() != null ? actionItem.getLabelName().length() : 0);

		            Cell cell6 = row.createCell(7);
		            cell6.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell6.setCellValue(actionItem.getAttrValue() != null ? actionItem.getAttrValue() : "");
		            updateMaxLength(maxLengths, 7, actionItem.getAttrValue() != null ? actionItem.getAttrValue().length() : 0);
		            Cell cell7 = row.createCell(8);
		            cell7.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell7.setCellValue(actionItem.getRgstUser());
		            updateMaxLength(maxLengths, 8, actionItem.getRgstUser() != null ? actionItem.getRgstUser().length() : 0);
		            Cell cell8 = row.createCell(9);
		            cell8.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell8.setCellValue(convertDate(actionItem.getRgstDtm()));
		            updateMaxLength(maxLengths, 9, convertDate(actionItem.getRgstDtm()).length());

		            Cell cell9 = row.createCell(10);
		            cell9.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell9.setCellValue(actionItem.getUpdUser());
		            updateMaxLength(maxLengths, 10, actionItem.getUpdUser() != null ? actionItem.getUpdUser().length() : 0);

		            Cell cell10 = row.createCell(11);
		            cell10.setCellStyle(createCellStyleValidation(createCellStyle));
		            cell10.setCellValue(convertDate(actionItem.getUpdDtm()));
		            updateMaxLength(maxLengths, 11, convertDate(actionItem.getUpdDtm()).length());

		            if (i == actions.size()) {
		                lastRow = index;
		            }
		            index++;
		        }
		        //merge row item condition
		        if (firstRowCondition != null && lastRowCondition != null && (lastRowCondition > firstRowCondition)) {
		            sheet.addMergedRegion(new CellRangeAddress(firstRowCondition, lastRowCondition, 2, 2));
		            sheet.addMergedRegion(new CellRangeAddress(firstRowCondition, lastRowCondition, 3, 3));
		            sheet.addMergedRegion(new CellRangeAddress(firstRowCondition, lastRowCondition, 4, 4));
		        }
		        //merge row item action
		        if (firstRowAction != null && lastRowAction != null && (lastRowAction > firstRowAction)) {
		            sheet.addMergedRegion(new CellRangeAddress(firstRowAction, lastRowAction, 5, 5));
		        }
		    }
		    //merge row number
		    if (lastRow > firstRow) {
		        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 1, 1));
		        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 8, 8));
		        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 9, 9));
		        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 10, 10));
		        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 11, 11));
		    }
		}

		sheet.createFreezePane(0, 4);
		sheet.setColumnWidth(0, 430);

		Row valueRow = sheet.createRow(1);
		Cell cell = valueRow.createCell(1);
		cell.setCellValue("■ "+excelInput.getSheetName()+"");
		sheet.setDisplayGridlines(false);

		// Set column width based on the maximum length of the values in each column
		for (int i = 1; i < maxLengths.length; i++) {
		    // Estimate width: 1 char ≈ 256 units, add 1024 for padding
		    int width = Math.min(maxLengths[i] * 256 + 1024, MAX_COLUMN_WIDTH); // Max Excel column width
		    sheet.setColumnWidth(i, width);
		}

	}
	
	// Helper method to update the maximum length of a column
	private void updateMaxLength(int[] maxLengths, int columnIndex, int length) {
	    if (length > maxLengths[columnIndex]) {
	        maxLengths[columnIndex] = length;
	    }
	}		
	
	public void createTitleValidation(Sheet sheet, Workbook workbook) {
		ExcelCellStyleSupportCustom excelCellStyle = new ExcelCellStyleSupportCustom(workbook);
		Row keyrow0 = sheet.createRow(2);
		Row keyrow1 = sheet.createRow(3);
		for (int inx = 0; inx < 11; inx++) {
			Cell cell0 = keyrow0.createCell(inx + 1);
			cell0.setCellStyle(createTitleCellStyleValidation(excelCellStyle.createTitleCellStyle()));
			Cell cell1 = keyrow1.createCell(inx + 1);
			cell1.setCellStyle(createTitleCellStyleValidation(excelCellStyle.createTitleCellStyle()));
		}
		keyrow0.getCell(1).setCellValue(messageSource.getMessage("customvalidation.excel.number", null, LocaleContextHolder.getLocale()));
		keyrow0.getCell(2).setCellValue(messageSource.getMessage("customvalidation.excel.condition", null, LocaleContextHolder.getLocale()));
		keyrow0.getCell(5).setCellValue(messageSource.getMessage("customvalidation.excel.action", null, LocaleContextHolder.getLocale()));
		keyrow0.getCell(8).setCellValue(messageSource.getMessage("customvalidation.excel.registeruser", null, LocaleContextHolder.getLocale()));
		keyrow0.getCell(9).setCellValue(messageSource.getMessage("customvalidation.excel.registerdate", null, LocaleContextHolder.getLocale()));
		keyrow0.getCell(10).setCellValue(messageSource.getMessage("customvalidation.excel.modifieduser", null, LocaleContextHolder.getLocale()));
		keyrow0.getCell(11).setCellValue(messageSource.getMessage("customvalidation.excel.modifieddate", null, LocaleContextHolder.getLocale()));
		
		keyrow1.getCell(2).setCellValue(messageSource.getMessage("customvalidation.excel.item", null, LocaleContextHolder.getLocale()));
		keyrow1.getCell(3).setCellValue(messageSource.getMessage("customvalidation.excel.attribute", null, LocaleContextHolder.getLocale()));
		keyrow1.getCell(4).setCellValue(messageSource.getMessage("customvalidation.excel.validation", null, LocaleContextHolder.getLocale()));
		keyrow1.getCell(5).setCellValue(messageSource.getMessage("customvalidation.excel.item", null, LocaleContextHolder.getLocale()));
		keyrow1.getCell(6).setCellValue(messageSource.getMessage("customvalidation.excel.attribute", null, LocaleContextHolder.getLocale()));
		keyrow1.getCell(7).setCellValue(messageSource.getMessage("customvalidation.excel.validation", null, LocaleContextHolder.getLocale()));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 4));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 7));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 8, 8));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 9, 9));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 10, 10));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 11, 11));
	}
	
	private CellStyle createCellStyleValidation(CellStyle createStyle) {
		createStyle.setAlignment(HorizontalAlignment.CENTER);
		createStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		return createStyle;
	}
	
	private CellStyle createTitleCellStyleValidation(CellStyle createStyle) {
		createStyle.setAlignment(HorizontalAlignment.CENTER);
		createStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		Color customColor = new Color(214, 220, 228); // RGB for grey
        XSSFColor xssfColor = new XSSFColor(customColor, null);
        createStyle.setFillForegroundColor(xssfColor);
        createStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return createStyle;
	}
	
	private String convertDate(String dateStr) {
		if (dateStr == null) {
			return null;
		}
		String outputFormat = "yyyy-MM-dd HH:mm:ss";
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);
        try {
        	LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnn"));
            return localDateTime.format(outputFormatter);
        } catch (DateTimeParseException e) {
            // Continue to the next formatter
        }
	    return null; // Return "" if no formatter matches
	}
}
