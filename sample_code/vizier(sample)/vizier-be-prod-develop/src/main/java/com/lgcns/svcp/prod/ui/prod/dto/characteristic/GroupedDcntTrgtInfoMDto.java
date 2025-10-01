package com.lgcns.svcp.prod.ui.prod.dto.characteristic;

import lombok.Data;

@Data
public class GroupedDcntTrgtInfoMDto {
	public GroupedDcntTrgtInfoMDto(DcntTrgtInfoMDto dcntTrgtInfoMDto) {
		this.generalDetails = new GeneralDetailFields(dcntTrgtInfoMDto);
		this.additionalParams = new AdditionalParamFields(dcntTrgtInfoMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String dcntTrgtInfoCd;
		private String dcntTrgtInfoNm;
		private String dcntTrgtItemSno;
		private String offrGrpCd;
		private String prodCd;
		private String dcntTrgtChrgKdCd;
		private String valdStrtDtm;
		private String valdEndDtm;
		private String rgstUsr;
		private String rgstDtm;
		private String updUsr;
		private String updDtm;

		public GeneralDetailFields (DcntTrgtInfoMDto dcntTrgtInfoMDto) {
			this.type = dcntTrgtInfoMDto.getType();
			this.dcntTrgtInfoCd = dcntTrgtInfoMDto.getDcntTrgtInfoCd();
			this.dcntTrgtInfoNm = dcntTrgtInfoMDto.getDcntTrgtInfoNm();
			this.dcntTrgtItemSno = dcntTrgtInfoMDto.getDcntTrgtItemSno();
			this.offrGrpCd = dcntTrgtInfoMDto.getOffrGrpCd();
			this.prodCd = dcntTrgtInfoMDto.getProdCd();
			this.dcntTrgtChrgKdCd = dcntTrgtInfoMDto.getDcntTrgtChrgKdCd();
			this.valdStrtDtm = dcntTrgtInfoMDto.getValdStrtDtm();
			this.valdEndDtm = dcntTrgtInfoMDto.getValdEndDtm();
			this.rgstUsr = dcntTrgtInfoMDto.getRgstUsr();
			this.rgstDtm = dcntTrgtInfoMDto.getRgstDtm();
			this.updUsr = dcntTrgtInfoMDto.getUpdUsr();
			this.updDtm = dcntTrgtInfoMDto.getUpdDtm();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {
		private String ratCd;
		private String svcFctrCd;
		private String billItemLclsCd;
		private String billItemMclsCd;
		private String billItemCd;

		public AdditionalParamFields (DcntTrgtInfoMDto dcntTrgtInfoMDto) {
			this.ratCd = dcntTrgtInfoMDto.getRatCd();
			this.svcFctrCd = dcntTrgtInfoMDto.getSvcFctrCd();
			this.billItemLclsCd = dcntTrgtInfoMDto.getBillItemLclsCd();
			this.billItemMclsCd = dcntTrgtInfoMDto.getBillItemMclsCd();
			this.billItemCd = dcntTrgtInfoMDto.getBillItemCd();
		}
	}
}
