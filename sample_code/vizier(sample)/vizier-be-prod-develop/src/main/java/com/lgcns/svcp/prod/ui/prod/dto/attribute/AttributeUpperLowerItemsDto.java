package com.lgcns.svcp.prod.ui.prod.dto.attribute;

import java.util.ArrayList;
import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.Item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeUpperLowerItemsDto {
	
	private List<Item> upperItems = new ArrayList<>();
	private List<Item> lowerItems = new ArrayList<>();
}
