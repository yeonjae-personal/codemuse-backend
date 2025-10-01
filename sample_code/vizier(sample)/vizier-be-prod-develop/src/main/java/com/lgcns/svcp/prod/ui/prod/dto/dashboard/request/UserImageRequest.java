package com.lgcns.svcp.prod.ui.prod.dto.dashboard.request;

import org.apache.commons.lang3.StringUtils;

import com.lgcns.svcp.prod.validator.annotation.UserImageValidate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserImageRequest {
	
	@NotNull(message = "Field is not null")
	@UserImageValidate
	private String imageBase64;
	
	@NotNull(message = "Field is not null")
	private String imageName;
	
	@NotNull(message = "Field is not null")
	private Integer imageSeq;
	
	public String getImageBase64() {
		if (StringUtils.isNotBlank(imageBase64)) {
            return imageBase64.split(",")[1];
        }
        return imageBase64;
	}
}
