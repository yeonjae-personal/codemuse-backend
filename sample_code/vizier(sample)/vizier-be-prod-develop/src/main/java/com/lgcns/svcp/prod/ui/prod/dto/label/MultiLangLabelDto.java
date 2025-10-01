package com.lgcns.svcp.prod.ui.prod.dto.label;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiLangLabelDto {
	
	private String labelId;
	private String labelType;
	@Valid
	private List<LabelItemDto> items = new ArrayList<>();
}
