package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.Item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomValidationItemResponse {
	
	private String itemCode;
    private String itemName;
    private String middleItemCode;
    private String middleItemName;
    private String largeItemCode;
    private String largeItemName;   
    private List<Item> types;
    private List<CustomValidationComponentItem> componentItem;
}
