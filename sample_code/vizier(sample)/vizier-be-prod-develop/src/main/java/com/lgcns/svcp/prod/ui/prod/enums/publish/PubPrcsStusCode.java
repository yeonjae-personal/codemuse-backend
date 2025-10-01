package com.lgcns.svcp.prod.ui.prod.enums.publish;

public enum PubPrcsStusCode {
	I("Publishing"),
	C("Published");

	private String value;

	private PubPrcsStusCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
