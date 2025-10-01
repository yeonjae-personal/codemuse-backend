package com.lgcns.svcp.prod.ui.prod.dto.dashboard.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserImageDeleteRequest {
	
	@NotNull(message = "Field is not null")
	private Integer imageSeq;
	
	@NotBlank(message = "Field is required")
	private String dsbdViewUuid;
}
