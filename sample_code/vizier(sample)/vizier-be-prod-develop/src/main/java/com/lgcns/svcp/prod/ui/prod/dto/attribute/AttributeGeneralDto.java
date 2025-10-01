package com.lgcns.svcp.prod.ui.prod.dto.attribute;

import java.util.ArrayList;
import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.Item;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeGeneralDto {
	
	@NotEmpty
	private String itemCode;
	@NotEmpty
	private String itemName;
	private String useYn;
	@NotEmpty
	private String largeItemCode;
	@NotEmpty
	private String largeItemName;
	@NotEmpty
	private String middleItemCode;
	@NotEmpty
	private String middleItemName;
	private Integer sortNo;
	private List<Item> upperItems = new ArrayList<>();
	private List<Item> lowerItems = new ArrayList<>();
}
