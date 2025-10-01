package com.lgcns.svcp.prod.ui.prod.dto.price;

import lombok.Data;

@Data
public class GroupedRtngDcntMDto {
	public GroupedRtngDcntMDto(RtngDcntMDto rtngDcntMDto) {
		this.generalDetails = new GeneralDetailFields(rtngDcntMDto);
		this.additionalParams = new AdditionalParamFields(rtngDcntMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String rtngDcntCd;
		private String rtngDcntNm;
		private String ratDivsCd;
		private String rtngDcntRatAplyUval;
		private String rtngDcntRat;
		private String prty;
		private String valdStrtDtm;
		private String valdEndDtm;

		public GeneralDetailFields (RtngDcntMDto rtngDcntMDto) {
			this.type = rtngDcntMDto.getType();
			this.rtngDcntCd = rtngDcntMDto.getRtngDcntCd();
			this.rtngDcntNm = rtngDcntMDto.getRtngDcntNm();
			this.ratDivsCd = rtngDcntMDto.getRatDivsCd();
			this.rtngDcntRatAplyUval = rtngDcntMDto.getRtngDcntRatAplyUval();
			this.rtngDcntRat = rtngDcntMDto.getRtngDcntRat();
			this.valdStrtDtm = rtngDcntMDto.getValdStrtDtm();
			this.valdEndDtm = rtngDcntMDto.getValdEndDtm();
			this.prty = rtngDcntMDto.getPrty();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {
		private String asgnCondDivsCd;
		private String maxPermCnt;

		public AdditionalParamFields (RtngDcntMDto rtngDcntMDto) {
			this.asgnCondDivsCd = rtngDcntMDto.getAsgnCondDivsCd();
			this.maxPermCnt = rtngDcntMDto.getMaxPermCnt();
		}
	}
}
