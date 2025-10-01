package com.lgcns.svcp.prod.ui.prod.dto.price;

import lombok.Data;

@Data
public class GroupedMfMDto {

	public GroupedMfMDto(MfMDto mfMDto) {
		this.generalDetails = new GeneralDetailFields(mfMDto);
		this.additionalParams = new AdditionalParamFields(mfMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String basfCd;
		private String basfNm;
		private String ratDivsCd;
		private String basf;
		private String basfAplyCyvl;
		private String basfAplyCyclCd;
		private String daCalcDivsCd;
		private String ratAplyKdCd;
		private String ppayPopyDivsCd;
		private String valdStrtDtm;
		private String valdEndDtm;

		public GeneralDetailFields (MfMDto mfMDto) {
			this.type = mfMDto.getType();
			this.basfCd = mfMDto.getBasfCd();
			this.basfNm = mfMDto.getBasfNm();
			this.ratDivsCd = mfMDto.getRatDivsCd();
			this.basf = mfMDto.getBasf();
			this.basfAplyCyvl = mfMDto.getBasfAplyCyvl();
			this.basfAplyCyclCd = mfMDto.getBasfAplyCyclCd();
			this.daCalcDivsCd = mfMDto.getDaCalcDivsCd();
			this.ratAplyKdCd = mfMDto.getRatAplyKdCd();
			this.ppayPopyDivsCd = mfMDto.getPpayPopyDivsCd();
			this.valdStrtDtm = mfMDto.getValdStrtDtm();
			this.valdEndDtm = mfMDto.getValdEndDtm();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {
		private String susBasf;
		private String billItemLclsCd;
		private String billItemMclsCd;
		private String billItemCd;
		private String susChrgImpsYn;
		private String susRatAplyKdCd;

		public AdditionalParamFields (MfMDto mfMDto) {
			this.susBasf = mfMDto.getSusBasf();
			this.billItemLclsCd = mfMDto.getBillItemLclsCd();
			this.billItemMclsCd = mfMDto.getBillItemMclsCd();
			this.billItemCd = mfMDto.getBillItemCd();
			this.susChrgImpsYn = mfMDto.getSusChrgImpsYn();
			this.susRatAplyKdCd = mfMDto.getSusRatAplyKdCd();
		}
	}
}
