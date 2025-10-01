package com.lgcns.svcp.prod.ui.prod.dto.resource;

import lombok.Data;

@Data
public class AdditionalBlngResMDto {
	private String usePlcyYn;
	private String rawSvcFctrCd;
	private String svcFctrKdDetlCd;
	private String svcFctrKdCd;
	private String rtmSysAplyYn;

	public AdditionalBlngResMDto(RawBlngResMDto rawBlngResMDto) {
        this.usePlcyYn = rawBlngResMDto.getUsePlcyYn();
        this.rawSvcFctrCd = rawBlngResMDto.getRawSvcFctrCd();
        this.svcFctrKdDetlCd = rawBlngResMDto.getSvcFctrCallKdDetlCd();
        this.svcFctrKdCd = rawBlngResMDto.getRawSvcFctrCd();
        this.rtmSysAplyYn = rawBlngResMDto.getRtmSysAplyYn();
    }
}
