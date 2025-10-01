package com.lgcns.svcp.prod.ui.prod.dto.item;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MiddleItemDto {
	
	private String code;
	private String name;
	private int sortNo;
	private List<Item> items = new ArrayList<>();
}
