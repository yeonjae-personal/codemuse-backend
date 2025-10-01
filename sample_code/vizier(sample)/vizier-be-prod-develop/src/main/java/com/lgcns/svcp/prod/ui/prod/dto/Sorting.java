package com.lgcns.svcp.prod.ui.prod.dto;

import java.util.Arrays;
import java.util.List;

public class Sorting {
	
	private List<String> properties;
	
	public Sorting(String... sortProperties) {
		if (sortProperties.length != 0) {
			this.properties = Arrays.asList(sortProperties);
		} 
	}

	public List<String> getProperties() {
		return properties;
	}
}
