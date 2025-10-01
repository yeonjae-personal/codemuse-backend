package com.lgcns.svcp.prod.ui.prod.dto.characteristic;

import lombok.Data;

@Data
public class GroupedBlngInfoMDto {
	public GroupedBlngInfoMDto(BlngInfoMDto blngInfoMDto) {
        this.generalDetails = new GeneralDetailFields(blngInfoMDto);
        this.additionalParams = new AdditionalParamFields(blngInfoMDto);
    }
	
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String blngInfoCd;
		private String blngInfoNm;
		private String rtmRtngTrgtDivsCd;
		private String prodBillMthdCd;
		private String atstNtagYn;

		public GeneralDetailFields (BlngInfoMDto blngInfoMDto) {
			this.type = blngInfoMDto.getType();
			this.blngInfoCd = blngInfoMDto.getBlngInfoCd();
			this.blngInfoNm = blngInfoMDto.getBlngInfoNm();
			this.rtmRtngTrgtDivsCd = blngInfoMDto.getRtmRtngTrgtDivsCd();
			this.prodBillMthdCd = blngInfoMDto.getProdBillMthdCd();
			this.atstNtagYn = blngInfoMDto.getAtstNtagYn();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {
		private String asgnCondDivsCd;
		private String prodAplyLvCd;
		private String agmtDcntRat;
		private String strmKdDivsCd;

		public AdditionalParamFields (BlngInfoMDto blngInfoMDto) {
			this.asgnCondDivsCd = blngInfoMDto.getAsgnCondDivsCd();
			this.prodAplyLvCd = blngInfoMDto.getProdAplyLvCd();
			this.agmtDcntRat = blngInfoMDto.getAgmtDcntRat();
			this.strmKdDivsCd = blngInfoMDto.getStrmKdDivsCd();
		}
	}
}
