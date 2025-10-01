package com.lgcns.svcp.prod.ui.prod.service.customexcel.impl;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.export.MultiEntityExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.export.ResourceExportDto;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.ResourceExcelHelper;
import com.lgcns.svcp.prod.util.DateFormatUtil;
import com.lgcns.svcp.prod.util.excel.ExcelCellStyleSupportCustom;
import com.lgcns.svcp.prod.util.excel.ExcelHelperImpl;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.excel.ExcelWriter;
import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;
import com.lgcns.svcp.prod.util.excel.annotation.Value;

@Component
public class ResourceExcelHelperImpl extends ExcelHelperImpl implements ResourceExcelHelper {
	
	private final int MAX_COLUMN_WIDTH = 40 * 261;
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public String[] createTitleHeader(Object... object) {
		ResourceExportDto dataHeader = (ResourceExportDto) object[0];

		List<String> header = new ArrayList<>();
		header.add(getHeaderLabel("index"));

		List<GeneralDetailDto> general = dataHeader.getGeneral();
		List<AdditionalDetailDto> additional = dataHeader.getAdditional();
		general.sort(Comparator.comparing(GeneralDetailDto::getSortNo));
		additional.sort(Comparator.comparing(AdditionalDetailDto::getSortNo));

		for (GeneralDetailDto g : general) {
			if ("HD".equals(g.getFieldTypeCode()) || "item_code".equals(g.getColName())
					|| "ctgr_node_uuid".equals(g.getColName())) {
				continue;
			}
			header.add(g.getLabelName());
		}

		for (AdditionalDetailDto a : additional) {
			header.add(a.getLabelName());
		}
		return header.toArray(new String[0]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createSheet(ExcelWriter excelWriter, ExcelInput excelInput) {
		Workbook workbook = excelWriter.getWorkbook();

		// Create sheet
		excelWriter.getStyle().setTitleCellStyle(createTitleCellStyleCustom(new ExcelCellStyleSupportCustom(workbook)));

		Sheet sheet = workbook.createSheet(excelInput.getSheetName());

		// Create header
		Object header = null;
		List<MultiEntityExportDto> multiEntities = null;
		String itemCode = null;
		String itemName = null;
		if (excelInput.getObject() instanceof Object[]) {
			Object[] objects = (Object[]) excelInput.getObject();
			header = objects[0];
			if (objects[1] != null) {
				multiEntities = (List<MultiEntityExportDto>) objects[1];
			}
			itemCode = (String) objects[2];
			itemName = (String) objects[3];
		}
		String[] headers = createTitleHeader(header);
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

		// Create body
		List<ResourceExportDto> datas = (List<ResourceExportDto>) excelInput.getDatas();

		for (int iny = 0, rowSize = datas.size(); iny < rowSize; iny++) {
			Row valueRow = sheet.createRow(iny + 3);
			ResourceExportDto data = datas.get(iny);

			int index = data.getIndex();
			List<GeneralDetailDto> general = data.getGeneral();
			List<AdditionalDetailDto> additional = data.getAdditional();

			general.sort(Comparator.comparing(GeneralDetailDto::getSortNo));
			additional.sort(Comparator.comparing(AdditionalDetailDto::getSortNo));
			
			//build cell style
			CellStyle cellStyle = excelWriter.getStyle().getCellStyle(); 
			DataFormat format = workbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("@"));

			Cell cellFirst = valueRow.createCell(1);
			cellFirst.setCellStyle(cellStyle);
			cellFirst.setCellValue(index);
			maxLength[0] = Math.max(maxLength[0], String.valueOf(index).length());
			int inx = 2;
			for (GeneralDetailDto g : general) {
				String attrVal = g.getAttrVal();
				String fieldTypeCode = g.getFieldTypeCode();
				if ("HD".equals(g.getFieldTypeCode()) || "item_code".equals(g.getColName())) {
					continue;
				}
				if ("DP".equals(fieldTypeCode)) {
					attrVal = formatExportDate(attrVal);
				}
				Cell cell = valueRow.createCell(inx);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(attrVal);
				int valueLength = 0;
				try {
					if (attrVal != null) {
						valueLength = attrVal.getBytes("EUC-KR").length;
					}
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException("Error: " + e.getMessage());
				}
				maxLength[inx - 1] = Math.max(maxLength[inx - 1], valueLength);
				inx++;
			}

			for (AdditionalDetailDto a : additional) {
				String attrVal = a.getAttrVal();
				String fieldTypeCode = a.getFieldTypeCode();
				if ("HD".equals(fieldTypeCode)) {
					continue;
				}
				if ("DP".equals(fieldTypeCode)) {
					attrVal = convertDate(attrVal);
				}

				Cell cell = valueRow.createCell(inx);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(attrVal);
				int valueLength = 0;
				try {
					if (attrVal != null) {
						valueLength = attrVal.getBytes("EUC-KR").length;
					}
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException("Error: " + e.getMessage());
				}
				maxLength[inx - 1] = Math.max(maxLength[inx - 1], valueLength);
				inx++;
			}
		}

		sheet.createFreezePane(0, 3);
        sheet.setColumnWidth(0, 430);
        Row valueRow = sheet.createRow(1);
        Cell cell = valueRow.createCell(1);
        cell.setCellValue("■ "+excelInput.getSheetName()+" ("+itemName+")");
        sheet.setDisplayGridlines(false);
        
		for (int i = 0; i < headers.length; i++) {
			int width = Math.min(maxLength[i] * 256 + 512, MAX_COLUMN_WIDTH);
		    sheet.setColumnWidth(i + 1, width);
		}
		
		if (itemCode.equalsIgnoreCase("SE")) {
			String[] headerMulti = createTitleHeaderMulti();
			excelWriter.setAutoCellSize(true);
			excelWriter.setTitles(headerMulti);
			excelWriter.createSheet(multiEntities, "Multi-Entity");
		}
	}
	
	private String[] createTitleHeaderMulti() {
		if (MultiEntityExportDto.class.isAnnotationPresent(CustomTitleHeader.class)) {
			List<String> results = new ArrayList<>();
			Field[] fields = MultiEntityExportDto.class.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.isAnnotationPresent(Value.class)) {
					Value value = field.getAnnotation(Value.class);
					results.add(messageSource.getMessage(value.name(), null, LocaleContextHolder.getLocale()));
				}
			}
			return results.toArray(new String[0]);
		}
		return null;
	}

	private String getHeaderLabel(String name) {
		return messageSource.getMessage("offer.excel." + name, null, LocaleContextHolder.getLocale());
	}

	private String formatExportDate(String stringDate) {
		if (stringDate == null) {
			return null;
		}
		if (!DATABASE_DATE_PATTERN.matcher(stringDate).matches()) {
			return stringDate;
		}
		try {
			Date endDate = DateFormatUtil.getDatabaseDateFormat() .parse(stringDate);
			return DateFormatUtil.getResponseDateFormat().format(endDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String convertDate(String dateStr) {
		if (dateStr == null) {
			return null;
		}
		String outputFormat = "yyyy-MM-dd HH:mm:ss";
		List<DateTimeFormatter> inputFormatters = Arrays.asList(
	            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss XXX"), // e.g., "2025/07/18 16:06:00 +09:00"
	            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"), // e.g., "2025-07-18 16:06:00 +09:00"
	            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX"), // e.g., "18/07/2025 16:06:00 +09:00"
	            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss XXX"), // e.g., "07/18/2025 16:06:00 +09:00"
	            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),     // e.g., "2025/07/18 16:06:00"
	            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),     // e.g., "2025-07-18 16:06:00"
	            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),     // e.g., "18/07/2025 16:06:00"
	            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"),      // e.g., "07/18/2025 16:06:00"
	            
	            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm XXX"), // e.g., "2025/07/18 16:06:00 +09:00"
	            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm XXX"), // e.g., "2025-07-18 16:06:00 +09:00"
	            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm XXX"), // e.g., "18/07/2025 16:06:00 +09:00"
	            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm XXX"), // e.g., "07/18/2025 16:06:00 +09:00"
	            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),     // e.g., "2025/07/18 16:06:00"
	            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),     // e.g., "2025-07-18 16:06:00"
	            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),     // e.g., "18/07/2025 16:06:00"
	            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")      // e.g., "07/18/2025 16:06:00"
	        );
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);

	        for (DateTimeFormatter formatter : inputFormatters) {
	            try {
	                ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr, formatter);
	                return zonedDateTime.format(outputFormatter);
	            } catch (DateTimeParseException e) {
	                // Continue to the next formatter
	            }
	        }

	    return null; // Return "" if no formatter matches
	}
}
