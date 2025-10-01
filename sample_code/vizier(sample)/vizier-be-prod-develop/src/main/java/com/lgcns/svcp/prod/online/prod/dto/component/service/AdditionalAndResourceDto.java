package com.lgcns.svcp.prod.online.prod.dto.component.service;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AdditionalAndResourceDto {
	private List<Map<String, Object>> additional;
	private Map<String, List<String>> resourceList;
}
