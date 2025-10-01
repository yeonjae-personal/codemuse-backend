package com.lgcns.svcp.prod.ui.prod.enums.publish;

public enum AprvStusCode {
	REQ("Request"),
	APR("Approval"),
	REJ("Reject");


	private String value;

	private AprvStusCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
