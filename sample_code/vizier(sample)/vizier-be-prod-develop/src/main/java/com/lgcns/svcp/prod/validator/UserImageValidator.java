package com.lgcns.svcp.prod.validator;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import com.lgcns.svcp.prod.constant.DashboardConstant;
import com.lgcns.svcp.prod.util.FileUtil;
import com.lgcns.svcp.prod.validator.annotation.UserImageValidate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserImageValidator implements ConstraintValidator<UserImageValidate, String> {
	
	private static String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpeg|jpg|png|gif|bmp))$)";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		if (StringUtils.isNotEmpty(value)) {
			InputStream inputStream = FileUtil.getInputStreamFromBase64(value.split(",")[1]);
			if (FileUtil.checkFileFormat(IMAGE_PATTERN, ".."+FileUtil.getImageType(inputStream)) &&
					FileUtil.checkFileSize(DashboardConstant.USER_IMAGE_MAX_FILE_SIZE, FileUtil.getFileSize(inputStream))) {
				return true;
			}
			return false;
		}
		return true;
	}
}
