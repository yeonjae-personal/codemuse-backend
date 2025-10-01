package com.lgcns.svcp.prod.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class StringUtilCustom extends StringUtils {

	public static String snakeToCamel(String snakeCase) {
		if (snakeCase == null || snakeCase.isEmpty()) {
			return snakeCase;
		}

		StringBuilder camelCase = new StringBuilder();
		boolean nextCharUpperCase = false;

		for (int i = 0; i < snakeCase.length(); i++) {
			char currentChar = snakeCase.charAt(i);

			if (currentChar == '_') {
				nextCharUpperCase = true;
			} else {
				if (nextCharUpperCase) {
					camelCase.append(Character.toUpperCase(currentChar));
					nextCharUpperCase = false;
				} else {
					camelCase.append(currentChar);
				}
			}
		}

		return camelCase.toString();
	}

	public static String covertToWonWithoutWCharacter(String amount) {
		if (amount == null || amount.isEmpty()) {
			return null;
		}
		Currency krw = Currency.getInstance("KRW");
		Locale loc = Locale.KOREA;
		NumberFormat inrFormatter = NumberFormat.getCurrencyInstance(loc);
		BigDecimal decimal = new BigDecimal(amount);
		return inrFormatter.format(decimal).replace("₩", "");
	}

	public static String convertBigDecimalToString(BigDecimal bigDecimal) {
		if (bigDecimal == null) {
			return null;
		}
		return bigDecimal.stripTrailingZeros().toPlainString();
	}

	public static String toUpperCaseFirstLetter(String source) {
		if (source == null || source.length() < 1) {
			return source;
		}
		return (source.substring(0, 1).toUpperCase() + source.substring(1));
	}

	public static boolean equals(String str1, String str2, boolean ignoreCase) {
		if (str1 == null && str2 == null) {
			return true;
		}
		if (str1 == null || str2 == null) {
			return false;
		}

		return ignoreCase ? str1.equalsIgnoreCase(str2) : str1.equals(str2);
	}

	public static boolean equals(String str1, String str2) {
		return equals(str1, str2, false);
	}

	public static boolean equalsIgnoreCase(String str1, String str2) {
		return equals(str1, str2, true);
	}

	public static boolean isBlank(String str) {
		return !(str != null && !str.isBlank());
	}

	public static boolean isIncludes(String str1, List<String> list){
		for(String str2: list){
			if(str2.equals(str1)){
				return true;
			}
		}
		return false;
	}

	public static boolean isContains(String str1, List<String> list){
		for(String str2: list){
			if(str1.contains(str2)){
				return true;
			}
		}
		return false;
	}

	public static boolean isStartWith(String str1, List<String> list){
		for(String str2: list){
			if(str1.contains(str2)){
				return true;
			}
		}
		return false;
	}
}
