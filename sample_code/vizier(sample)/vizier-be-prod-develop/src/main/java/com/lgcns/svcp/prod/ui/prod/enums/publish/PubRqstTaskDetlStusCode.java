package com.lgcns.svcp.prod.ui.prod.enums.publish;

public enum PubRqstTaskDetlStusCode {
	SAVED("Saved"),
	PACKED("Packed"),
	PUBLISHED("Published");

	private String value;

	private PubRqstTaskDetlStusCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
