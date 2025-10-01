package com.lgcns.svcp.prod.online.prod.dto.group;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class OfferGroupAndOfferDto {
	private List<Map<String, Object>> offerGroup;
	private Map<String, List<String>> offerList;
}
