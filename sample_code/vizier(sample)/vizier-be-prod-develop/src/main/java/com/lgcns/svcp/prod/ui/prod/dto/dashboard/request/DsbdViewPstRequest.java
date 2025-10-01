package com.lgcns.svcp.prod.ui.prod.dto.dashboard.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsbdViewPstRequest {
	
	@NotBlank
	private String dsbdViewUuid;
	
	@NotNull
	private Integer posX;
	
	@NotNull
	private Integer posY;
}
