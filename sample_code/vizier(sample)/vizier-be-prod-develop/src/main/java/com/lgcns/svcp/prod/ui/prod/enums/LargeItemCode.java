package com.lgcns.svcp.prod.ui.prod.enums;

public enum LargeItemCode {
	
	O("Offer"),
    G("Group"),
    C("Component"),
    R("Resource");
    
    private String value;
	
    private LargeItemCode(String value) {
    	this.value = value;
    }

	public String getValue() {
		return value;
	}
}
