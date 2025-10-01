package com.lgcns.svcp.prod.ui.prod.service.customexcel.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.ui.prod.dto.admin.table.TableColumnDto;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.TableExcelHelper;
import com.lgcns.svcp.prod.util.excel.ExcelCellStyleSupportCustom;
import com.lgcns.svcp.prod.util.excel.ExcelHelperImpl;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.excel.ExcelWriter;

@Component
public class TableExcelHelperImpl extends ExcelHelperImpl implements TableExcelHelper {
	
	private final int MAX_COLUMN_WIDTH = 40 * 261;
	
	@Override
	public String[] createTitleHeader(Object... object) {
		List<TableColumnDto> dataHeader = (List<TableColumnDto>) object[0];

		List<String> header = new ArrayList<>();
		header.add(getHeaderLabel("index"));

		for (TableColumnDto columnDto : dataHeader) {
			header.add(columnDto.getColumnComment());
		}
		
		return header.toArray(new String[0]);
	}

	@Override
	public void createSheet(ExcelWriter excelWriter, ExcelInput excelInput) {
		Workbook workbook = excelWriter.getWorkbook();

		// Create sheet
		excelWriter.getStyle().setTitleCellStyle(createTitleCellStyleCustom(new ExcelCellStyleSupportCustom(workbook)));

		Sheet sheet = workbook.createSheet(excelInput.getSheetName());

		// Create header
		String[] headers = createTitleHeader(excelInput.getObject());
		excelWriter.createTitle(headers, sheet);
		
		int[] maxLength = new int[headers.length];
		for (int i = 0; i < headers.length; i++) {
			int charCount = 0;
		    if (headers[i] == null) {
		    	charCount = 4;
		    } else {
		    	charCount = headers[i].getBytes().length;
		    }
			maxLength[i] = charCount;
		}
		
		List<?> datas = excelInput.getDatas();
		List<TableColumnDto> dataHeader = (List<TableColumnDto>) excelInput.getObject();
		for (int iny = 0, rowSize = datas.size(); iny < rowSize; iny++) {
			Row valueRow = sheet.createRow(iny + 3);
			Cell cellFirst = valueRow.createCell(1);
			cellFirst.setCellStyle(excelWriter.getStyle().getCellStyle());
			cellFirst.setCellValue(iny + 1);
			maxLength[0] = Math.max(maxLength[0], String.valueOf(iny + 1).length());
			LinkedHashMap<String, Object> maps = (LinkedHashMap<String, Object>) datas.get(iny);
			int inx = 2;
			for (TableColumnDto column: dataHeader) {
				Cell cell = valueRow.createCell(inx);
				CellStyle cellStyle = excelWriter.getStyle().getCellStyle(); 
				DataFormat format = workbook.createDataFormat();
				cellStyle.setDataFormat(format.getFormat("@"));
				cell.setCellStyle(cellStyle);
				for (Entry<String, Object> e : maps.entrySet()) {
					if (column.getColumnName().equalsIgnoreCase(e.getKey())) {
						String result = null;
						if (column.getColumnType().equals("DM")) {							
					        String cleanedInput = e.getValue().toString().replace("[", "").replace("]", "").replace("\"", "");					        					   
					        String[] array = cleanedInput.split(",");				        					        
					        List<String> list = Arrays.asList(array);			        
					        result = String.join(";", list);
						} else {
							result = e.getValue().toString();
						}
						cell.setCellValue(result);
						int valueLength = 0;
						try {
							if (result != null) {
								valueLength = result.getBytes("EUC-KR").length;
							}
						} catch (UnsupportedEncodingException e1) {
							throw new RuntimeException("Error: " + e1.getMessage());
						}
						maxLength[inx - 1] = Math.max(maxLength[inx - 1], valueLength);						
					}
				}
				inx++;
			}
		}
		sheet.createFreezePane(0, 3);
        sheet.setColumnWidth(0, 430);
        Row valueRow = sheet.createRow(1);
        Cell cell = valueRow.createCell(1);
        cell.setCellValue("■ "+excelInput.getSheetName()+"");
        sheet.setDisplayGridlines(false);
        
		// Auto-size columns based on header text
		for (int i = 0; i < headers.length; i++) {
			int width = Math.min(maxLength[i] * 256 + 512, MAX_COLUMN_WIDTH);
		    sheet.setColumnWidth(i + 1, width);
		}
	}

	private String getHeaderLabel(String name) {
		return messageSource.getMessage("offer.excel." + name, null, LocaleContextHolder.getLocale());
	}
}
