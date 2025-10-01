package com.lgcns.svcp.prod.advice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lgcns.svcp.prod.exception.BusinessException;

@ControllerAdvice
public class ControllerSupport {
	
	private static final String PACKAGE_PREFIX = "com.lgcns";
    private static final int MAX_LINES = 10;
    
    @ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception ex){
    	HttpHeaders headers = new HttpHeaders();
		headers.add("BIZError", "N");
		ErrorResponseBody body = new ErrorResponseBody();
		body.setErrorCode("500");
		body.setErrorMsg(ex.getMessage());
		body.setErrorDetail(getExceptionMessage(ex));
		body.setErrorStack(getErrorStack(ex));
		return new ResponseEntity<>(body, headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthenticationException(Exception ex){
		HttpHeaders headers = new HttpHeaders();
		headers.add("AuthError", "N");
		ErrorResponseBody body = new ErrorResponseBody();
		body.setErrorCode("403");
		body.setErrorMsg(ex.getMessage());
		body.setErrorDetail(getExceptionMessage(ex));
		body.setErrorStack(getErrorStack(ex));
		return new ResponseEntity<>(body, headers, HttpStatus.FORBIDDEN);
	}

    @ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleBusinessException(BusinessException ex){
		HttpHeaders headers = new HttpHeaders();
		headers.add("BIZError", "Y");
		ErrorResponseBody body = new ErrorResponseBody();
		body.setErrorCode("400");
		body.setErrorMsg(ex.getMessage());
		body.setErrorDetail(getBusinessExceptionMessage(ex));
		body.setErrorStack(getErrorStack(ex));
		return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
	}
	
    protected String getExceptionMessage(Throwable th) {
    	List<String> messages = new ArrayList<>();
    	List<Throwable> ths = new ArrayList<>();
    	Throwable cur = th;
    	while(cur != null && !ths.contains(cur)) {
    		messages.add(cur.toString());
    		ths.add(cur);
    		cur = cur.getCause();
    	}
    	Collections.reverse(messages);
    	return messages.toString();
    }
    
    private String getBusinessExceptionMessage(Throwable th) {
    	String message = th.getMessage();
    	return th.getClass().getName() + (message == null ? "" : " [" + message + "]");
    }
    
    protected String getErrorStack(Throwable th) {
		StringBuilder sb = new StringBuilder();
        int lineCount = 0;

        for (StackTraceElement elem : th.getStackTrace()) {
        	String line = elem.toString();
            if (lineCount < MAX_LINES || line.contains(PACKAGE_PREFIX)) {
                sb.append(line).append("\n");
                lineCount++;
            } else {
                sb.append(".");
            }
        }

        return sb.toString();
	}
}
