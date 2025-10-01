package com.lgcns.svcp.prod.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import com.lgcns.svcp.prod.exception.BusinessException;

public class DateUtil {

	public static final DateTimeFormatter SOURCE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter TARGET_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private static final DateTimeFormatter WORKNO_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public static String formatDate(String dateStr) {
		if (dateStr == null || dateStr.isEmpty()) {
			return null;
		}
		LocalDateTime dateTime = LocalDateTime.parse(dateStr, SOURCE_FORMATTER);
		return dateTime.format(TARGET_FORMATTER);
	}

	public static String formatDate(String format, Date date) {
		if (date != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.format(date);
		}
		return null;
	}

	public static Boolean isValidStartDateEndDate(String startDateStr, String endDateStr) {
		LocalDate startDate = LocalDate.parse(startDateStr, SOURCE_FORMATTER);
		LocalDate endDate = LocalDate.parse(endDateStr, SOURCE_FORMATTER);
		return !startDate.isAfter(endDate);
	}

	public static Date convertToDateByResponseFormat(String dateStr) {
		if (dateStr == null || dateStr.isEmpty()) {
			return null;
		}
		try {
			return DateFormatUtil.getResponseDateFormat().parse(dateStr);
		} catch (ParseException e) {
			throw new BusinessException("Wrong format date!");
		}
	}

	public static Date convertToDateByDatabaseFormat(String dateStr) {
		if (dateStr == null || dateStr.isEmpty()) {
			return null;
		}
		try {
			return DateFormatUtil.getDatabaseDateFormat().parse(dateStr);
		} catch (ParseException e) {
			throw new BusinessException("Wrong format date!");
		}
	}

	public static long generateWorkNo() {
		LocalDateTime now = LocalDateTime.now();
		String formattedDate = now.format(WORKNO_FORMAT);
		return Long.parseLong(formattedDate);
	}

	public static Long convertWorkNo(String datetime) {
		LocalDateTime now = LocalDateTime.now();
		String formattedDate = now.format(WORKNO_FORMAT);
		return Long.parseLong(formattedDate);
	}

	public static long convertStringToWorkNo(String input) {
		try {
			String numericString = input.replaceAll("[^0-9]", "");
			return Long.parseLong(numericString);
		} catch (Exception e) {
			throw new BusinessException("Invalid input format: " + input);
		}
	}

	/*
	 * example date: 2024-07-01
	 */
	public static int getMonth(String date) {
		LocalDate localDate = LocalDate.parse(date);
		return localDate.getMonthValue();
	}

	/*
	 * example date: 2024-07-01
	 */
	public static int getYear(String date) {
		LocalDate localDate = LocalDate.parse(date);
		return localDate.getYear();
	}

	/*
	 * example date: 2024-07-01
	 */
	public static int getDayOfMonth(String date) {
		LocalDate localDate = LocalDate.parse(date);
		return localDate.getDayOfMonth();
	}

	public static Long getDuration(Date startDate, Date endDate) {
		if (endDate != null) {
			long diffInMillies = Math.abs(startDate.getTime() - endDate.getTime());
			return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		}
		return null;
	}

	public static Date minusDays(LocalDate localDate, long daysToSubtract) {
		LocalDate minusDate = localDate.minusDays(daysToSubtract);
		Calendar calendar = new GregorianCalendar();
		calendar.set(minusDate.getYear(), minusDate.getMonthValue() - 1, minusDate.getDayOfMonth());
		return calendar.getTime();
	}
}
