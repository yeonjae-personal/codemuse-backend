package com.lgcns.svcp.prod.ui.prod.service.customexcel.impl;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixExportReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixMeasureDDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixMeasureMDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.builder.BuilderFactorDto;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.MatrixExcelHelper;
import com.lgcns.svcp.prod.util.excel.ExcelCellStyleSupportCustom;
import com.lgcns.svcp.prod.util.excel.ExcelHelperImpl;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.excel.ExcelReader;
import com.lgcns.svcp.prod.util.excel.ExcelWriter;

import jakarta.servlet.http.Part;

@Component
public class MatrixExcelHelperImpl extends ExcelHelperImpl implements MatrixExcelHelper {

	@SuppressWarnings("unchecked")
	@Override
	public String[] createTitleHeader(Object... object) {
		List<BuilderFactorDto> builderDtos = (List<BuilderFactorDto>) object[0];
		List<String> headers = builderDtos.stream().map(BuilderFactorDto::getFactorName).collect(Collectors.toList());
		headers.add("Value");
		return headers.toArray(new String[0]);
	}

	@Override
	public void createSheet(ExcelWriter excelWriter, ExcelInput excelInput) {
		MatrixExportReqDto exportDto = (MatrixExportReqDto) excelInput.getObject();
		List<MatrixMeasureMDto> measures = exportDto.getMeasureMDtos();
		Workbook workbook = excelWriter.getWorkbook();

		// Set up title style
		excelWriter.getStyle().setTitleCellStyle(createTitleCellStyleCustom(new ExcelCellStyleSupportCustom(workbook)));

		Sheet sheet = workbook.createSheet(excelInput.getSheetName());

		int rowNum = 0;
		rowNum = writeMatrixInfo(sheet, exportDto, rowNum);
		rowNum = writeMatrixEditorNote(sheet, rowNum);

		String[] headers = createTitleHeader(exportDto.getBuilderDtos());
		int headerRowNum = rowNum;
		rowNum = writeHeaderRow(sheet, headers, rowNum);

		writeTableBody(sheet, headers, measures, rowNum);

		formatSheet(sheet, headers, measures);

		formatMatrixRanges((XSSFWorkbook) workbook, sheet, headers, measures, headerRowNum);

		unlockValueColumn((XSSFWorkbook) workbook, sheet, headers.length, measures);
		sheet.protectSheet("");
	}

	// 1. Helper: Write matrix code & name
	private int writeMatrixInfo(Sheet sheet, MatrixExportReqDto dto, int rowNum) {
		sheet.createRow(rowNum); // blank row
		rowNum++;

		Row row = sheet.createRow(rowNum);
		rowNum++;
		row.createCell(1).setCellValue("Matrix Code");
		row.createCell(3).setCellValue(dto.getMatrixCode());

		row = sheet.createRow(rowNum);
		rowNum++;
		row.createCell(1).setCellValue("Matrix Name");
		row.createCell(3).setCellValue(dto.getMatrixCodeName());

		sheet.createRow(rowNum); // blank row
		rowNum++;
		return rowNum;
	}

	// 2. Helper: Write editor note
	private int writeMatrixEditorNote(Sheet sheet, int rowNum) {
		Row row = sheet.createRow(rowNum);
		rowNum++;
		row.createCell(1).setCellValue("■  Matrix Editor");
		row.createCell(3)
				.setCellValue("※ Only the VALUE column can be edited, and it must contain numeric values only.");
		return rowNum;
	}

	// 3. Helper: Write table header
	private int writeHeaderRow(Sheet sheet, String[] headers, int rowNum) {
		Row headerRow = sheet.createRow(rowNum);
		rowNum++;
		for (int i = 0; i < headers.length; i++) {
			headerRow.createCell(i + 1).setCellValue(headers[i].toUpperCase());
		}
		return rowNum;
	}

	// 4. Helper: Write table body
	private void writeTableBody(Sheet sheet, String[] headers, List<MatrixMeasureMDto> measures, int startRow) {
		if (measures == null) {
			return;
		}
		for (int i = 0; i < measures.size(); i++) {
			MatrixMeasureMDto measure = measures.get(i);
			Row row = sheet.createRow(startRow + i);
			int col = 1;
			if (measure.getMeasureDDtos() != null) {
				for (MatrixMeasureDDto d : measure.getMeasureDDtos()) {
					row.createCell(col).setCellValue(d.getFactorValueName());
					col++;
				}
			}
			BigDecimal value = measure.getMatrixNumValue();
			if (value != null) {
				row.createCell(headers.length).setCellValue(value.toPlainString());
			}
		}
	}

	// 5. Helper: Basic sheet settings
	private void formatSheet(Sheet sheet, String[] headers, List<MatrixMeasureMDto> measures) {
		sheet.getRow(0).setHeight((short) 120);
		sheet.getRow(5).setHeight((short) 360);
		sheet.setColumnWidth(0, 360);
		int columnCount = measures != null && measures.size() > 8 ? measures.size() : 8;
		for (int i = 0; i < columnCount; i++) {
			sheet.setColumnWidth(i + 1, 256 * 10);
		}
		sheet.setDisplayGridlines(false);
	}

	// 6. Helper: Format ranges (header, notes, etc)
	private void formatMatrixRanges(XSSFWorkbook workbook, Sheet sheet, String[] headers,
			List<MatrixMeasureMDto> measures, int headerRowIndex) {

		Map<String, XSSFCellStyle> styleCache = new HashMap<>();

		// B2:C2
		formatRange(workbook, sheet, styleCache, new CellRangeAddress(1, 1, 1, 2), DEFAULT_FONT, DEFAULT_FONT_SIZE,
				null, null, true, false, false, HorizontalAlignment.LEFT, null);

		// D2:H2
		formatRange(workbook, sheet, styleCache, new CellRangeAddress(1, 1, 3, 7), DEFAULT_FONT, DEFAULT_FONT_SIZE,
				null, null, true, false, false, HorizontalAlignment.LEFT, null);

		// B3:C3
		formatRange(workbook, sheet, styleCache, new CellRangeAddress(2, 2, 1, 2), DEFAULT_FONT, DEFAULT_FONT_SIZE,
				null, null, true, false, false, HorizontalAlignment.LEFT, null);

		// D3:H3
		formatRange(workbook, sheet, styleCache, new CellRangeAddress(2, 2, 3, 7), DEFAULT_FONT, DEFAULT_FONT_SIZE,
				null, null, true, false, false, HorizontalAlignment.LEFT, null);

		// D5 warning (row 4, column 3)
		formatRange(workbook, sheet, styleCache, new CellRangeAddress(4, 4, 3, 3), DEFAULT_FONT, DEFAULT_FONT_SIZE,
				new java.awt.Color(192, 0, 0), null, false, true, false, HorizontalAlignment.LEFT, null);

		// Format header (row 6 = index 5)
		for (int col = 1; col <= headers.length; col++) {
			formatRange(workbook, sheet, styleCache, new CellRangeAddress(headerRowIndex, headerRowIndex, col, col),
					DEFAULT_FONT, DEFAULT_FONT_SIZE, null, new java.awt.Color(214, 220, 228), true, false, true,
					HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
		}

		// Format body (row 7 = index 6+)
		if (measures != null) {
			for (int i = 0; i < measures.size(); i++) {
				int bodyRowIndex = headerRowIndex + 1 + i;
				for (int col = 1; col <= headers.length; col++) {
					formatRange(workbook, sheet, styleCache, new CellRangeAddress(bodyRowIndex, bodyRowIndex, col, col),
							DEFAULT_FONT, DEFAULT_FONT_SIZE, null, null, true, false, false, HorizontalAlignment.LEFT,
							null);
				}
			}
		}
	}

	// 7. Helper: Unlock Value column for edit
	private void unlockValueColumn(XSSFWorkbook workbook, Sheet sheet, int valueCol, List<MatrixMeasureMDto> measures) {
		int firstValueRow = 6;
		if (measures == null) {
			return;
		}
		for (int i = 0; i < measures.size(); i++) {
			int rowIdx = firstValueRow + i;
			Row row = sheet.getRow(rowIdx);
			if (row != null) {
				Cell cell = row.getCell(valueCol);
				if (cell == null) {
					cell = row.createCell(valueCol);
				}
				CellStyle oldStyle = cell.getCellStyle();
				CellStyle unlockedStyle = workbook.createCellStyle();
				if (oldStyle != null) {
					unlockedStyle.cloneStyleFrom(oldStyle);
				}
				unlockedStyle.setLocked(false);
				cell.setCellStyle(unlockedStyle);
			}
		}
	}

	private void formatRange(XSSFWorkbook workbook, Sheet sheet, Map<String, XSSFCellStyle> styleCache,
			CellRangeAddress range, String fontName, short fontSize, Color fontColor, Color backgroundColor,
			boolean withBorder, boolean italic, boolean bold, HorizontalAlignment align, VerticalAlignment vAlign) {
		for (int rowIdx = range.getFirstRow(); rowIdx <= range.getLastRow(); rowIdx++) {
			Row row = sheet.getRow(rowIdx);
			if (row == null) {
				row = sheet.createRow(rowIdx);
			}
			for (int colIdx = range.getFirstColumn(); colIdx <= range.getLastColumn(); colIdx++) {
				Cell cell = row.getCell(colIdx);
				if (cell == null) {
					cell = row.createCell(colIdx);
				}

				// Compose a key for the cache (all style properties)
				String styleKey = fontName + "_" + fontSize + "_" + (fontColor != null ? fontColor.getRGB() : "") + "_"
						+ (backgroundColor != null ? backgroundColor.getRGB() : "") + "_" + withBorder + "_" + italic
						+ "_" + bold + "_" + (align != null ? align.name() : "") + "_" + (rowIdx == range.getFirstRow())
						+ (rowIdx == range.getLastRow()) + (colIdx == range.getFirstColumn())
						+ (colIdx == range.getLastColumn());

				XSSFCellStyle style = styleCache.get(styleKey);
				if (style == null) {
					XSSFFont font = workbook.createFont();
					font.setFontName(fontName);
					font.setFontHeightInPoints(fontSize);
					if (fontColor != null) {
						font.setColor(new XSSFColor(fontColor, null));
					}
					font.setItalic(italic);
					font.setBold(bold);

					style = workbook.createCellStyle();
					style.setFont(font);

					if (backgroundColor != null) {
						style.setFillForegroundColor(new XSSFColor(backgroundColor, null));
						style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					}
					if (align != null) {
						style.setAlignment(align);
					}
					if (vAlign != null) {
						style.setVerticalAlignment(vAlign);
					}

					// Set border only for outer edges
					if (withBorder) {
						if (rowIdx == range.getFirstRow()) {
							style.setBorderTop(BorderStyle.THIN);
						}
						if (rowIdx == range.getLastRow()) {
							style.setBorderBottom(BorderStyle.THIN);
						}
						if (colIdx == range.getFirstColumn()) {
							style.setBorderLeft(BorderStyle.THIN);
						}
						if (colIdx == range.getLastColumn()) {
							style.setBorderRight(BorderStyle.THIN);
						}
					}
					styleCache.put(styleKey, style);
				}
				cell.setCellStyle(style);
			}
		}
	}

	@Override
	public MatrixExportReqDto parseMatrixFromExcel(Part filePart, MatrixExportReqDto matrixExportReqDto) {
		String matrixCode = matrixExportReqDto.getMatrixCode();
		List<MatrixMeasureMDto> measureMDtos = matrixExportReqDto.getMeasureMDtos();
		List<BuilderFactorDto> builderDtos = matrixExportReqDto.getBuilderDtos();

		// Create Excel reader and get workbook & target sheet
		ExcelReader excelReader = new ExcelReader(filePart);
		Workbook workbook = excelReader.getWorkBook();
		Sheet sheet = workbook.getSheet(matrixCode);

		// Check if sheet with matrixCode exists
		if (sheet == null) {
			throw new BusinessException(
					messageSource.getMessage("matrix.excel.matrixcode.invalid", null, LocaleContextHolder.getLocale()));
		}

		// Validate that the matrix code in Excel matches the provided code
		validateMatrixCode(sheet, matrixCode);

		// Validate that the header row in Excel matches the factor names
		validateHeader(sheet, builderDtos);

		// Validate each data row: factor value names and value column
		for (int i = 0; i < measureMDtos.size(); i++) {
			MatrixMeasureMDto measure = measureMDtos.get(i);
			validateRowData(sheet, measure, 6 + i);
		}

		// Return the DTO with values set from Excel
		return matrixExportReqDto;
	}

	/**
	 * Checks if the matrix code in cell D2 matches the provided matrixCode.
	 */
	private void validateMatrixCode(Sheet sheet, String matrixCode) {
		// D2 = row 1, column 3 (0-based index)
		Row row = sheet.getRow(1);
		Cell cell = row != null ? row.getCell(3) : null;
		String value = cell != null ? cell.toString() : "";
		if (!matrixCode.equals(value)) {
			throw new BusinessException(
					messageSource.getMessage("matrix.excel.matrixcode.invalid", null, LocaleContextHolder.getLocale()));
		}
	}

	/**
	 * Validates the header row (row 6) matches the builder factor names.
	 */
	private void validateHeader(Sheet sheet, List<BuilderFactorDto> builderDtos) {
		// Row 5 = Excel row 6 (header)
		Row headerRow = sheet.getRow(5);
		if (headerRow == null) {
			throw new BusinessException(
					messageSource.getMessage("matrix.excel.data.notmatch", null, LocaleContextHolder.getLocale()));
		}
		List<String> actualHeaders = new ArrayList<>();
		int firstCell = 1; // Start from column B, skip column A
		int lastCell = headerRow.getLastCellNum() - 1; // Exclude last column ("Value")

		// Read header cells from B to the column before last
		for (int i = firstCell; i < lastCell; i++) {
			Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
			actualHeaders.add(cell != null ? cell.toString().toUpperCase() : "");
		}

		// Collect expected header names from builderDtos
		List<String> expectedHeaders = builderDtos.stream().map(BuilderFactorDto::getFactorName)
				.map(String::toUpperCase).toList();

		// Compare actual headers with expected headers
		if (!actualHeaders.equals(expectedHeaders)) {
			throw new BusinessException(
					messageSource.getMessage("matrix.excel.data.notmatch", null, LocaleContextHolder.getLocale()));
		}
	}

	/**
	 * Validates a data row: factor value names (columns) and numeric value.
	 */
	private void validateRowData(Sheet sheet, MatrixMeasureMDto measure, int rowIndex) {
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			throw new BusinessException(
					messageSource.getMessage("matrix.excel.data.notmatch", null, LocaleContextHolder.getLocale()));
		}

		List<String> actualData = new ArrayList<>();
		int firstCell = 1; // Skip column A
		int lastCell = row.getLastCellNum() - 1; // Exclude "Value" column

		// Read factor value cells for this row
		for (int j = firstCell; j < lastCell; j++) {
			Cell cell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
			actualData.add(cell != null ? cell.toString() : "");
		}

		// Collect expected factor value names from DTO
		List<String> expectedData = measure.getMeasureDDtos().stream().map(MatrixMeasureDDto::getFactorValueName)
				.toList();

		// Compare actual data with expected data
		if (!actualData.equals(expectedData)) {
			throw new BusinessException(
					messageSource.getMessage("matrix.excel.data.notmatch", null, LocaleContextHolder.getLocale()));
		}

		// Parse the "Value" column (last column) and set into DTO
		Cell valueCell = row.getCell(row.getLastCellNum() - 1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
		try {
			// Compare and update BigDecimal value
			BigDecimal oldValue = measure.getMatrixNumValue();
			BigDecimal newValue = parseDecimalValue(valueCell);
			measure.setMatrixNumValue(newValue);
			// Use compareTo for BigDecimal comparison with null checks
			measure.setChanged(
					oldValue == null || newValue == null ? oldValue != newValue : oldValue.compareTo(newValue) != 0);
		} catch (NumberFormatException e) {
			throw new BusinessException(
					messageSource.getMessage("matrix.excel.data.notmatch", null, LocaleContextHolder.getLocale()));
		}
	}

	/**
	 * Parses a cell as BigDecimal, throws if not a valid BigDecimal.
	 */
	private BigDecimal parseDecimalValue(Cell cell) {
		if (cell == null || cell.getCellType() == CellType.BLANK) {
			return null;
		}
		return switch (cell.getCellType()) {
		case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue());
		case STRING -> new BigDecimal(cell.getStringCellValue().trim());
		default -> throw new NumberFormatException("Unsupported cell type: " + cell.getCellType());
		};
	}

}
