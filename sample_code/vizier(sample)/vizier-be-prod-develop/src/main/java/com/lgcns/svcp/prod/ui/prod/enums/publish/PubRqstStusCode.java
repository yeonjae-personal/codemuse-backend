package com.lgcns.svcp.prod.ui.prod.enums.publish;

public enum PubRqstStusCode {
	C("Created"),
	M("Composed"),
	V("Validation"),
	I("In Progress"),
	D("Delay"),
	P("Publish Complete"),
	O("Prod Transfer"),
	E("Expire");

	private String value;

	private PubRqstStusCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
