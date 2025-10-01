package com.lgcns.svcp.prod.ui.prod.util;

import com.lgcns.svcp.prod.ui.prod.dto.characteristic.DcntCstcMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.BlngInfoMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.DcntTrgtInfoMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.LobMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.QosMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.SlinInfoMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.SpamMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.AlowMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.DcntRatMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.MfMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.PdspTossLkgeDDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.RtngDcntMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.UsfeMDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.GroupedBlngResMDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.GroupedServiceResMDto;

public enum ItemDetailType {
	BASE_FEE("RC", "basfCd", MfMDto.class, "uiComponentService", "getGroupedMfM"),
    USE_FEE("UC", "usfeCd", UsfeMDto.class, "uiComponentService", "getGroupedUsfeM"),
    VOICE("VO", "pdspCd", PdspTossLkgeDDto.class, "uiComponentService", "getGroupedPdspTossLkgeD"),
    MESSAGE("MS", "pdspCd", PdspTossLkgeDDto.class, "uiComponentService", "getGroupedPdspTossLkgeD"),
    ADDITIONAL("AD", "pdspCd", PdspTossLkgeDDto.class, "uiComponentService", "getGroupedPdspTossLkgeD"),
    ALLOWANCE("AW", "alowCd", AlowMDto.class, "uiComponentService", "getGroupedAlowM"),
    RTNG_DISCOUNT("RD", "rtngDcntCd", RtngDcntMDto.class, "uiComponentService", "getGroupedRtngDcntM"),
    BILLING("BI", "blngInfoCd", BlngInfoMDto.class, "uiComponentService", "getGroupedBlngInfoM"),
    SALES("SI", "slinInfoCd", SlinInfoMDto.class, "uiComponentService", "getGroupedSlinInfoM"),
    LOB("LB", "lobCd", LobMDto.class, "uiComponentService", "getGroupedLobInfo"),
    SPAM("SP", "spamCd", SpamMDto.class, "uiComponentService", "getGroupedSpamInfo"),
    QOS("QS", "qosCd", QosMDto.class, "uiComponentService", "getGroupedQosInfo"),
    DISCOUNT_INFORMATION("DC", "dcntCstcCd", DcntCstcMDto.class, "uiComponentService", "getGroupedDcntCstcM"),
    DISCOUNT_TARGET("DT", "dcntTrgtInfoCd", DcntTrgtInfoMDto.class, "uiComponentService", "getGroupedDcntTrgtInfoM"),
    DISCOUNT_RATE("DR", "dcntRatCd", DcntRatMDto.class, "uiComponentService", "getGroupedDcntRatM"),
    RATING_ELEMENT("RE", "svcFctrCd", GroupedBlngResMDto.class, "uiResourceService", "retrieveGroupedBlngResM"),
    BILLING_ELEMENT("BE", "svcFctrCd", GroupedBlngResMDto.class, "uiResourceService", "retrieveGroupedBlngResM"),
    SERVICE_ELEMENT("PE", "svcFctrCd", GroupedServiceResMDto.class, "uiResourceService", "retrieveGroupedServiceResM")
    ;

    private final String type;
    private final String codeColumnName;
    private final Class<?> dtoClass;
    private final String serviceBeanName;
    private final String serviceMethodName;

    ItemDetailType(String type, String codeColumnName, Class<?> dtoClass, String serviceBeanName, String serviceMethodName) {
        this.type = type;
        this.codeColumnName = codeColumnName;
        this.dtoClass = dtoClass;
        this.serviceBeanName = serviceBeanName;
        this.serviceMethodName = serviceMethodName;
    }

    public String getType() {
        return type;
    }
    
    public String getCodeColumnName() {
        return codeColumnName;
    }

    public Class<?> getDtoClass() {
        return dtoClass;
    }

    public String getServiceBeanName() {
        return serviceBeanName;
    }

    public String getServiceMethodName() {
        return serviceMethodName;
    }

    public static ItemDetailType fromCode(String code) {
        for (ItemDetailType type : values()) {
            if (type.getType().equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid input code: " + code);
    }
}
