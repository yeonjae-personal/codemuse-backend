package com.lgcns.svcp.prod.ui.prod.enums;

public enum DsbdViewTypeEnums {
	
	S("satistics"),
	P("personalized");
	
	private String value;
	
	DsbdViewTypeEnums(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
