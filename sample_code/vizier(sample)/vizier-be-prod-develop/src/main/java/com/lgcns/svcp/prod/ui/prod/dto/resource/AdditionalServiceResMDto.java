package com.lgcns.svcp.prod.ui.prod.dto.resource;

import lombok.Data;

@Data
public class AdditionalServiceResMDto {
	private String swtcPrmtnm;
	private String swtcPrmtDelNm;
	private String saSwtcPrmtNm;
	private String saSwtcPrmtDelNm;
	
	public AdditionalServiceResMDto(RawServiceResMDto rawServiceResMDto) {
        this.swtcPrmtnm = rawServiceResMDto.getSwtcPrmtnm();
        this.swtcPrmtDelNm = rawServiceResMDto.getSwtcPrmtDelNm();
        this.saSwtcPrmtNm = rawServiceResMDto.getSaSwtcPrmtDelNm();
        this.saSwtcPrmtDelNm = rawServiceResMDto.getSwtcPrmtDelNm();
    }
}
