package com.lgcns.svcp.prod.ui.prod.dto.attribute;

import java.util.ArrayList;
import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.Item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeMiddleItemDto {
	
	private String itemCode;
	private String itemName;
	private String largeItemCode;
    private String largeItemName;
	private int sortNo;
	private List<Item> items = new ArrayList<>();
}
