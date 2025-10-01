package com.lgcns.svcp.prod.online.prod.dto.component.price;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class BaseFeeAndResourceDto {
	private List<Map<String, Object>> baseFee;
	private Map<String, List<String>> resourceList;
}
