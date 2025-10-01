package com.lgcns.svcp.prod.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ConstraintViolationAdvice extends ControllerSupport {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleNotValidException(MethodArgumentNotValidException ex) {
		ErrorResponseBody body = new ErrorResponseBody();
		body.setErrorMsg("Error constraint violation exception");
		body.setErrorDetail(getExceptionMessage(ex));
		body.setErrorStack(getErrorStack(ex));
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
		ErrorResponseBody body = new ErrorResponseBody();
		body.setErrorMsg("Error constraint violation exception");
		body.setErrorDetail(getExceptionMessage(ex));
		body.setErrorStack(getErrorStack(ex));
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
    
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(BindException ex) {
		ErrorResponseBody body = new ErrorResponseBody();
		body.setErrorMsg("Error constraint violation exception");
		body.setErrorDetail(getExceptionMessage(ex));
		body.setErrorStack(getErrorStack(ex));
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}
