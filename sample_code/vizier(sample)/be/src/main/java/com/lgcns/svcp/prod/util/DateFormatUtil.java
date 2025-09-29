package com.lgcns.svcp.prod.util;

import java.text.SimpleDateFormat;

public class DateFormatUtil {
	
	public static SimpleDateFormat getDatabaseDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public static SimpleDateFormat getResponseDateFormat() {
		return new SimpleDateFormat("yyyy/MM/dd");
	}
}
