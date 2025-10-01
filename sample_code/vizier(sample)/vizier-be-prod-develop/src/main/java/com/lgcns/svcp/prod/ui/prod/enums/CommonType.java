package com.lgcns.svcp.prod.ui.prod.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonType {
    PP("PricePlan","PP","Offer","PricePlan", null, "OFPP" ),
    AO("AddOn","AO","Offer","PricePlan", null, "OFAO" ),
    DC("Discount","DC","Offer","PricePlan", null, "OFDC" ),
    DV("Device","DV","Offer","PricePlan", null, "OFDV" ),
    BI("BillingInformation","BI","Component","Characteristics", "Billing Information", "CHBI" ),
    DI("DiscountInformation","DI","Component","Characteristics", "Discount Information", "CHDI" ),
    DT("DiscountTarget","DT","Component","Characteristics", "Discount Target", "CHDT" ),
    LB("LineOfBusinessInformation","LB","Component","Characteristics", "Line Of Business Information", "CHLB" ),
    QS("QoSInformation","QS","Component","Characteristics", "QoS Information", "CHQS" ),
    SI("SalesInformation","SI","Component","Characteristics", "Sales Information", "CHSI" ),
    SP("SpamInformation","SP","Component","Characteristics", "Spam Information", "CHSP" ),
    AD("Additional","AD","Component","Service", "Additional", "SRAD" ),
    MS("Message","MS","Component","Service", "Message", "SRMS" ),
    VO("Voice","VO","Component","Service", "Voice", "SRVO" ),
    AW("Allowance","AW","Component","Benefit", "Allowance", "BNAW" ),
    RD("RatingDiscount","RD","Component","Benefit", "Ration Discount", "BNRD" ),
    DR("DiscountRate","DR","Component","Price", "Discount Rate", "PRDR" ),
    OC("OneTimeCharge","OC","Component","Price", "OneTime Charge", "OPOC" ),
    RC("RecurringCharge","RC","Component","Price", "Recurring Charge", "PRRC" ),
    UC("UsageCharge","UC","Component","Price", "Usage Charge", "PRUC" ),
    BE("BillingElement","BE","Resource","Billing Element", null, "RSBE" ),
    RE("RatingElement","RE","Resource","Rating Element", null, "RSRE" ),
    SE("ServiceElement","SE","Resource","Service Element", null, "RSSE" );

    private final String name;
    private final String code;
    private final String itemType;
    private final String type;
    private final String subType;
    private final String prefix;
}
