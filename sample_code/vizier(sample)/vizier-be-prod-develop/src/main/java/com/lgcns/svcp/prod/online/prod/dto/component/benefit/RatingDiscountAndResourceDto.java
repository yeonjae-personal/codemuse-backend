package com.lgcns.svcp.prod.online.prod.dto.component.benefit;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class RatingDiscountAndResourceDto {
	private List<Map<String, Object>> ratingDiscount;
	private Map<String, List<String>> resourceList;
}
