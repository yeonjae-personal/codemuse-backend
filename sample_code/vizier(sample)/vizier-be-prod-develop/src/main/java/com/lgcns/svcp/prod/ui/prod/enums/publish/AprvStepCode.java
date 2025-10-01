package com.lgcns.svcp.prod.ui.prod.enums.publish;

public enum AprvStepCode {
	D("Design"),
	P("Pricing Review"),
	I("IT Review"),
	E("Excute Approval"),
	C("Complete Approval");
	
	private String value;

	private AprvStepCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
