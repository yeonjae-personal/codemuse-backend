package com.lgcns.svcp.prod.exception;

/**
 * BusinessException with messageCode and optional arguments. Uses i18n message
 * if available, otherwise fallback to code itself.
 */
public class BusinessException extends RuntimeException {

	public BusinessException(String messageCode) {
		super(messageCode);
	}
}
