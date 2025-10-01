package com.lgcns.svcp.prod.ui.prod.enums;

public enum OfferType {
	
    PRICEPLAN("PricePlan"),
    DISCOUNT("Discount"),
    ADDON("Add-On"),
    DEVICE("Device");
    
    private String value;
	
    private OfferType(String value) {
    	this.value = value;
    }

	public String getValue() {
		return value;
	}
}
