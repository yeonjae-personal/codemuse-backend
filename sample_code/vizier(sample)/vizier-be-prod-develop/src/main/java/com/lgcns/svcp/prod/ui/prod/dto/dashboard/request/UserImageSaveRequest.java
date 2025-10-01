package com.lgcns.svcp.prod.ui.prod.dto.dashboard.request;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserImageSaveRequest {
	
	@NotBlank(message = "Field is required")
	private String dsbdViewUuid;
	
	@Valid
	private List<UserImageRequest> requests = new ArrayList<>();
}
