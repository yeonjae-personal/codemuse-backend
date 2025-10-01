package com.lgcns.svcp.prod.ui.prod.dto.userpocket;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPocketRequest {
	
	@NotBlank
	private String uuid;
}
