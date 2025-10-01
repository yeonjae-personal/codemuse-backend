package com.lgcns.svcp.prod.ui.prod.dto.price;

import lombok.Data;

@Data
public class GroupedAlowMDto {
	public GroupedAlowMDto(AlowMDto alowMDto) {
		this.generalDetails = new GeneralDetailFields(alowMDto);
		this.additionalParams = new AdditionalParamFields(alowMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String alowCd;
		private String alowNm;
		private String urstYn;
		private String shrnPsblYn;
		private String deqtAplyCyvl;
		private String deqtAplyCyclCd;
		private String alowAplyPotCd;
		private String alowPrty;
		private String deqtvl;
		private String deqtAplyUval;
		private String ratDivsCd;
		private String valdStrtDtm;
		private String valdEndDtm;

		public GeneralDetailFields (AlowMDto alowMDto) {
			this.type = alowMDto.getType();
			this.alowCd = alowMDto.getAlowCd();
			this.alowNm = alowMDto.getAlowNm();
			this.urstYn = alowMDto.getUrstYn();
			this.shrnPsblYn = alowMDto.getShrnPsblYn();
			this.deqtAplyCyvl = alowMDto.getDeqtAplyCyvl();
			this.deqtAplyCyclCd = alowMDto.getDeqtAplyCyclCd();
			this.alowAplyPotCd = alowMDto.getAlowAplyPotCd();
			this.alowPrty = alowMDto.getAlowPrty();
			this.deqtvl = alowMDto.getDeqtvl();
			this.deqtAplyUval = alowMDto.getDeqtAplyUval();
			this.ratDivsCd = alowMDto.getRatDivsCd();
			this.valdStrtDtm = alowMDto.getValdStrtDtm();
			this.valdEndDtm = alowMDto.getValdEndDtm();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {
		private String asgnCondDivsCd;
		private String asgnPsblNoCnt;

		public AdditionalParamFields (AlowMDto alowMDto) {
			this.asgnCondDivsCd = alowMDto.getAsgnCondDivsCd();
			this.asgnPsblNoCnt = alowMDto.getAsgnPsblNoCnt();
		}
	}
}
