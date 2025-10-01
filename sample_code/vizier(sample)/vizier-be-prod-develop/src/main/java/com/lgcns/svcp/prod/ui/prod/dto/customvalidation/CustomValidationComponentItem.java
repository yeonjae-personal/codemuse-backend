package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.Item;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomValidationComponentItem {
	
	private String itemCode;
    private String itemName;
    private Integer sortNo;
	private List<Item> types;
}
