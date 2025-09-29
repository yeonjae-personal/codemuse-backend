package com.lgcns.svcp.prod.util.excel;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelCellStyleSupport {
	public static final String DEFAULT_DATE_FORMAT = "yyyy-mm-dd";

	public static final String DEFAULT_TIME_FORMAT = "yyyy-mm-dd h:mm:ss";

	public static final short DEFAULT_TITLE_FONT_HEIGHT = 10;

	public static final short DEFAULT_FONT_HEIGHT = 10;

	private String calendarFormat = DEFAULT_DATE_FORMAT;

	private String dateFormat = DEFAULT_DATE_FORMAT;

	private String timeFormat = DEFAULT_TIME_FORMAT;

	private String fontName = "LG스마트체 Regular";

	public short titleFontHeight = DEFAULT_TITLE_FONT_HEIGHT;

	public short fontHeight = DEFAULT_FONT_HEIGHT;

	private Font font = null;

	private Workbook workbook;

	/**
	 * 타이틀 CellStyle
	 */
	private CellStyle titleCellStyle;

	/**
	 * 일반 데이터 CellStyle
	 */
	private CellStyle cellStyle;

	/**
	 * Date CellStyle
	 */
	private CellStyle dateCellStyle;

	/**
	 * Time CellStyle
	 */
	private CellStyle timeCellStyle;

	/**
	 * Calendar CellStyle
	 */
	private CellStyle calendarCellStyle;

	public ExcelCellStyleSupport(Workbook workbook) {
		this.workbook = workbook;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	protected Font createFont() {
		Font font = getWorkbook().createFont();
		font.setFontHeightInPoints(getFontHeight());
		font.setFontName(getFontName());
		return font;
	}

	public Font getFont() {
		if (font == null) {
			font = createFont();
		}
		return font;
	}

	public CellStyle getCellStyle(Object data) {
		if (data == null || data instanceof String || data instanceof Number || data instanceof Boolean) {
			return getCellStyle();
		} else if (data instanceof Timestamp || data instanceof LocalDateTime) {
			return getTimeCellStyle();
		} else if (data instanceof Date || data instanceof LocalDate || data instanceof Calendar) {
			return getDateCellStyle();
		} else if (data instanceof Calendar) {
			return getCalendarCellStyle();
		} else if (data instanceof RichTextString) {
			return getCellStyle();
		} else {
			return getCellStyle();
		}
	}

	protected CellStyle createCellStyle() {
		CellStyle cellStyle = getWorkbook().createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setFont(getFont());
		return cellStyle;
	}

	public CellStyle getCellStyle() {
		if (cellStyle == null) {
			cellStyle = createCellStyle();
		}
		return cellStyle;
	}

	public void setCellStyle(CellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}

	protected CellStyle createTitleCellStyle() {
		CellStyle cellStyle = createCellStyle();
		Font font = createFont();
		font.setBold(true);
		font.setFontHeightInPoints(getTitleFontHeight());
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getTitleCellStyle() {
		if (titleCellStyle == null) {
			titleCellStyle = createTitleCellStyle();
		}
		return titleCellStyle;
	}

	public void setTitleCellStyle(CellStyle titleCellStyle) {
		this.titleCellStyle = titleCellStyle;
	}

	protected CellStyle createDateCellStyle() {
		CellStyle style = createCellStyle();
		DataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat(getDateFormat()));
		return style;
	}

	public CellStyle getDateCellStyle() {
		if (dateCellStyle == null) {
			dateCellStyle = createDateCellStyle();
		}
		return this.dateCellStyle;
	}

	public void setDateCellStyle(CellStyle dateCellStyle) {
		this.dateCellStyle = dateCellStyle;
	}

	protected CellStyle createTimeCellStyle() {
		CellStyle style = createCellStyle();
		DataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat(getTimeFormat()));
		return style;
	}

	public CellStyle getTimeCellStyle() {
		if (timeCellStyle == null) {
			timeCellStyle = createTimeCellStyle();
		}
		return timeCellStyle;
	}

	public void setTimeCellStyle(CellStyle timeCellStyle) {
		this.timeCellStyle = timeCellStyle;
	}

	protected CellStyle createCalendarCellStyle() {
		CellStyle style = createCellStyle();
		DataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat(getCalendarFormat()));
		return style;
	}

	public CellStyle getCalendarCellStyle() {
		if (calendarCellStyle == null) {
			calendarCellStyle = createCalendarCellStyle();
		}
		return calendarCellStyle;
	}

	public void setCalendarCellStyle(CellStyle calendarCellStyle) {
		this.calendarCellStyle = calendarCellStyle;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getCalendarFormat() {
		return calendarFormat;
	}

	public void setCalendarFormat(String calendarFormat) {
		this.calendarFormat = calendarFormat;
	}

	public short getTitleFontHeight() {
		return titleFontHeight;
	}

	public void setTitleFontHeight(short titleFontHeight) {
		this.titleFontHeight = titleFontHeight;
	}

	public short getFontHeight() {
		return fontHeight;
	}

	public void setFontHeight(short fontHeight) {
		this.fontHeight = fontHeight;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

}
