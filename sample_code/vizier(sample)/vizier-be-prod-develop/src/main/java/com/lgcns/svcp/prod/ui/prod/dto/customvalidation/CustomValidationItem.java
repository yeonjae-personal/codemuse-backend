package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.Item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomValidationItem {
	
	private Item item;
	private List<CustomValidationComponentItem> componentItem;
}
