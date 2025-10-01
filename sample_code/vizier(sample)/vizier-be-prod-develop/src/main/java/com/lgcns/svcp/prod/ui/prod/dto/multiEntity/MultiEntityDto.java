package com.lgcns.svcp.prod.ui.prod.dto.multiEntity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lgcns.svcp.prod.ui.prod.dto.ItemMappingDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiEntityDto extends ItemMappingDto {
	private String entityCode;
	private String entityName;
	private String entityTypeCode;
    private String entityScope;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<MultiEntityAdditionalDto> additional;
}
