package com.lgcns.svcp.prod.ui.prod.dto.attribute;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeDetailDto {
	
	@Valid
	private AttributeGeneralDto general;
	
	@Valid
	private List<AttributeAdditionalDto> additionals = new ArrayList<>();
	
	private TreeViewDto treeView;
}
