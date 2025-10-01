package com.lgcns.svcp.prod.ui.prod.dto.price;

import lombok.Data;

@Data
public class GroupedDcntRatMDto {
	public GroupedDcntRatMDto(DcntRatMDto dcntRatMDto) {
		this.generalDetails = new GeneralDetailFields(dcntRatMDto);
		this.additionalParams = new AdditionalParamFields(dcntRatMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type; 
		private String dcntRatCd;
		private String dcntRatNm;
		private String ratDivsCd;
		private String dcntRat;
		private String dcntRatAplyCyvl;
		private String dcntRatAplyCyclCd;
		private String daCalcDivsCd;
		private String ratAplyKdCd;
		private String valdStrtDtm;
		private String valdEndDtm;
		private String rgstUsr;
		private String rgstDtm;
		private String updUsr;
		private String updDtm;

		public GeneralDetailFields (DcntRatMDto dcntRatMDto) {
			this.type = dcntRatMDto.getType();
			this.dcntRatCd = dcntRatMDto.getDcntRatCd();
			this.dcntRatNm = dcntRatMDto.getDcntRatNm();
			this.ratDivsCd = dcntRatMDto.getRatDivsCd();
			this.dcntRat = dcntRatMDto.getDcntRat();
			this.dcntRatAplyCyvl = dcntRatMDto.getDcntRatAplyCyvl();
			this.dcntRatAplyCyclCd = dcntRatMDto.getDcntRatAplyCyclCd();
			this.daCalcDivsCd = dcntRatMDto.getDaCalcDivsCd();
			this.ratAplyKdCd = dcntRatMDto.getRatAplyKdCd();
			this.valdStrtDtm = dcntRatMDto.getValdStrtDtm();
			this.valdEndDtm = dcntRatMDto.getValdEndDtm();
			this.rgstUsr = dcntRatMDto.getRgstUsr();
			this.rgstDtm = dcntRatMDto.getRgstDtm();
			this.updUsr = dcntRatMDto.getUpdUsr();
			this.updDtm = dcntRatMDto.getUpdDtm();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {
		private String billItemLclsCd;
		private String billItemMclsCd;
		private String billItemCd;
		private String ratCalcMthdCd;
		private String sectAplyKdCd;

		public AdditionalParamFields (DcntRatMDto dcntRatMDto) {
			this.billItemLclsCd = dcntRatMDto.getBillItemLclsCd();
			this.billItemMclsCd = dcntRatMDto.getBillItemMclsCd();
			this.billItemCd = dcntRatMDto.getBillItemCd();
			this.ratCalcMthdCd = dcntRatMDto.getRatCalcMthdCd();
			this.sectAplyKdCd = dcntRatMDto.getSectAplyKdCd();
		}
	}
}
