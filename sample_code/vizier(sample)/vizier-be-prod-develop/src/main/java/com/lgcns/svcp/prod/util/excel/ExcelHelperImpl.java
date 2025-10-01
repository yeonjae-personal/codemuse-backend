package com.lgcns.svcp.prod.util.excel;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;
import com.lgcns.svcp.prod.util.excel.annotation.Value;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Primary
@Component
public class ExcelHelperImpl implements ExcelHelper {

	private Class<?> tClass;

	@Autowired
	public MessageSource messageSource;

	@Override
	public void downloadExcel(ExcelInput input, HttpServletResponse response, boolean isCustomTemplate) {
		if (System.getProperty("org.apache.poi.ss.ignoreMissingFontSystem") == null) {
			System.setProperty("org.apache.poi.ss.ignoreMissingFontSystem", "true");
		}
		ExcelWriter excelWriter = new ExcelWriter(input.getExtention());
		try (ServletOutputStream outputStream = response.getOutputStream();
				Workbook workbook = excelWriter.getWorkbook();) {
			response.setContentType("application/octet-stream");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename="
					+ createExcelFileName(input.getFileName(), input.getExtention(), input.getFormatDate()) + "";
			response.setHeader(headerKey, headerValue);
			if ((input.getDatas() != null && !input.getDatas().isEmpty()) || input.getObject() != null) {
				createSheet(excelWriter, input);
				if (isCustomTemplate) {
					handleCustomTemplate(workbook, input.getObject());
				}
			} else {
				excelWriter.getStyle()
						.setTitleCellStyle(createTitleCellStyleCustom(new ExcelCellStyleSupportCustom(workbook)));
				excelWriter.createTitle(createTitleHeader(input.getObject()),
						workbook.createSheet(input.getSheetName()));
			}
			workbook.write(outputStream);
		} catch (IOException e) {
			throw new RuntimeException(
					messageSource.getMessage("system.error.excel", null, LocaleContextHolder.getLocale()));
		} catch (Exception e) {
			throw new RuntimeException(
					messageSource.getMessage("system.error.excel", null, LocaleContextHolder.getLocale()));
		}
	}

	@Override
	public String[] createTitleHeader(Object... object) {
		if (tClass.isAnnotationPresent(CustomTitleHeader.class)) {
			List<String> results = new ArrayList<>();
			Field[] fields = tClass.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.isAnnotationPresent(Value.class)) {
					Value value = field.getAnnotation(Value.class);
					results.add(messageSource.getMessage(value.name(), null, LocaleContextHolder.getLocale()));
				} else {
					results.add(field.getName());
				}
			}
			return results.toArray(new String[0]);
		}
		return new String[0];
	}

	@Override
	public void createSheet(ExcelWriter excelWriter, ExcelInput excelInput) {
		ExcelCellStyleSupportCustom excelCellStyle = new ExcelCellStyleSupportCustom(excelWriter.getWorkbook());
		excelWriter.setTitles(createTitleHeader());
		excelWriter.getStyle().setTitleCellStyle(createTitleCellStyleCustom(excelCellStyle));
		excelWriter.setAutoCellSize(true);
		CellStyle cellStyle = excelWriter.getStyle().getCellStyle(); 
		DataFormat format = excelWriter.getWorkbook().createDataFormat();
		cellStyle.setDataFormat(format.getFormat("@"));
		excelWriter.createSheet(excelInput.getDatas(), excelInput.getSheetName());
	}

	public CellStyle createTitleCellStyleCustom(ExcelCellStyleSupportCustom excelCellStyle) {
		CellStyle cellStyle = excelCellStyle.createTitleCellStyle();
		Color customColor = new Color(214, 220, 228); // RGB for grey
        XSSFColor xssfColor = new XSSFColor(customColor, null);
		cellStyle.setFillForegroundColor(xssfColor);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		return cellStyle;
	}

	private String createExcelFileName(String name, String extention, String formatDate) {
		DateFormat dateFormat = new SimpleDateFormat(formatDate);
		String dateStr = dateFormat.format(new Date());
		return name + "_" + dateStr + "." + extention;
	}

	@Override
	public ExcelHelper of(Class<?> tClass) {
		this.tClass = tClass;
		return this;
	}
}
