package com.lgcns.svcp.prod.online.prod.dto.offer;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class DiscountAndComponentDto {
	private List<Map<String, Object>> discount;
	private Map<String, List<String>> componentList;
}
