package com.lgcns.svcp.prod.advice;

import java.io.Serializable;

import lombok.Data;

@Data
public class ErrorResponseBody implements Serializable{
	
	private static final long serialVersionUID = -3998297831937665656L;
	
	private String errorCode;
	private String errorMsg;
	private String errorStack;
	private String errorDetail;
}
