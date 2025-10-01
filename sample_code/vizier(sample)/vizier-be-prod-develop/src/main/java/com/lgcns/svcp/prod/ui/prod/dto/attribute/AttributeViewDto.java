package com.lgcns.svcp.prod.ui.prod.dto.attribute;

import java.util.ArrayList;
import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.Item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeViewDto {
	
	private String code;
	private String name;
	private int sortNo;
	private List<AttributeMiddleItemDto> middleItems = new ArrayList<>();
	private List<Item> items = new ArrayList<>();
}
