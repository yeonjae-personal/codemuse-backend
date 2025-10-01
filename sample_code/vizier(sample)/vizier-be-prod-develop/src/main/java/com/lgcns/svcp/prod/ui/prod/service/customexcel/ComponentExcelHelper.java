package com.lgcns.svcp.prod.ui.prod.service.customexcel;

import java.util.regex.Pattern;

import com.lgcns.svcp.prod.util.excel.ExcelHelper;

public interface ComponentExcelHelper extends ExcelHelper {
	Pattern DATABASE_DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
}
