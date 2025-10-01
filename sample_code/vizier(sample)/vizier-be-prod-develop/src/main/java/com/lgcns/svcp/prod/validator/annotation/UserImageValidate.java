package com.lgcns.svcp.prod.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lgcns.svcp.prod.validator.UserImageValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = UserImageValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserImageValidate {
	
	String message() default "Format or size is incorrect";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
