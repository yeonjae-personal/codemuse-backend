package com.lgcns.svcp.prod.ui.prod.dto.offer;

import lombok.Data;
@Data
public class GroupedDcntMDto {
	public GroupedDcntMDto(DcntMDto dcntMDto) {
		this.generalDetails = new GeneralDetailFields(dcntMDto);
		this.additionalParams = new AdditionalParamFields(dcntMDto);
		this.overView = dcntMDto.getOverView();
		this.comment = dcntMDto.getComment();
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String dcntCd;
		private String dcntNm;
		private String prty;
		private String prodKdCd;
		private String dcntValdStrtDtm;
		private String dcntValdEndDtm;
		private String rgstUsr;
		private String rgstDtm;
		private String updUsr;
		private String updDtm;

		public GeneralDetailFields (DcntMDto dcntMDto) {
			this.type = dcntMDto.getType();
			this.dcntCd = dcntMDto.getDcntCd();
			this.dcntNm = dcntMDto.getDcntNm();
			this.prty = dcntMDto.getPrty();
			this.prodKdCd = dcntMDto.getProdKdCd();
			this.dcntValdStrtDtm = dcntMDto.getDcntValdStrtDtm();
			this.dcntValdEndDtm = dcntMDto.getDcntValdEndDtm();
			this.rgstUsr = dcntMDto.getRgstUsr();
			this.rgstDtm = dcntMDto.getRgstDtm();
			this.updUsr = dcntMDto.getUpdUsr();
			this.updDtm = dcntMDto.getUpdDtm();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {
		private String dcntGrpRepKdCd;

		public AdditionalParamFields (DcntMDto dcntMDto) {
			this.dcntGrpRepKdCd = dcntMDto.getDcntGrpRepKdCd();
		}

	}
	private String overView;
	private String comment;
}
