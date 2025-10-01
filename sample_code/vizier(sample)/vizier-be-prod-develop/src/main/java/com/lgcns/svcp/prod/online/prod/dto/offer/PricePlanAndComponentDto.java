package com.lgcns.svcp.prod.online.prod.dto.offer;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class PricePlanAndComponentDto {
	private List<Map<String, Object>> pricePlan;
	private Map<String, List<String>> componentList;
//	private List<String> benefit;
//	private List<String> characteristics;
//	private List<String> price;
//	private List<String> service;
}
