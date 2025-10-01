package com.lgcns.svcp.prod.online.prod.dto.component.service;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class VoiceAndResourceDto {
	private List<Map<String, Object>> voice;
	private Map<String, List<String>> resourceList;
}
