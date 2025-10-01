package com.lgcns.svcp.prod.online.prod.dto.component.price;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class UsageFeeAndResourceDto {
	private List<Map<String, Object>> usageFee;
	private Map<String, List<String>> resourceList;
}
