package com.lgcns.svcp.prod.online.prod.dto.offer;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AddOnAndComponentDto {
	private List<Map<String, Object>> addOn;
	private Map<String, List<String>> componentList;
}
