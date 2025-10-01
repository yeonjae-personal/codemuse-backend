package com.lgcns.svcp.prod.i18n;

import java.util.Locale;

import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyLocaleResolver extends AbstractLocaleResolver {
	
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Locale defaultLocale = getDefaultLocale();
		if (defaultLocale != null && request.getHeader("X-Language") == null && request.getParameter("language") == null) {
			return defaultLocale;
		}
		String language = request.getHeader("X-Language");
		String param = request.getParameter("language");
		Locale locale = getLocale(language != null ? language : param);
		if (locale == null) {
			return defaultLocale;
		}
		return locale;
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		throw new UnsupportedOperationException(
				"Cannot change HTTP X-Language header - use a different locale resolution strategy");
	}
	
	private Locale getLocale(String param) {
		if (param.equals("en")) {
			return Locale.ENGLISH;
		} else if (param.equals("ko")) {
			return Locale.KOREAN;
		}
		return null;
	}
}
