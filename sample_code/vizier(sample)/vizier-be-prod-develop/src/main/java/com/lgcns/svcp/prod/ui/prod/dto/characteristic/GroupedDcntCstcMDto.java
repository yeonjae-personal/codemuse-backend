package com.lgcns.svcp.prod.ui.prod.dto.characteristic;

import lombok.Data;

@Data
public class GroupedDcntCstcMDto {
	public GroupedDcntCstcMDto(DcntCstcMDto dcntCstcMDto) {
		this.generalDetails = new GeneralDetailFields(dcntCstcMDto);
		this.additionalParams = new AdditionalParamFields(dcntCstcMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String dcntCstcCd;
		private String dcntCstcNm;
		private String pnltOccrYn;
		private String dcntGrpKdCd;
		private String rgstUsr;
		private String rgstDtm;
		private String updUsr;
		private String updDtm;

		public GeneralDetailFields (DcntCstcMDto dcntCstcMDto) {
			this.type = dcntCstcMDto.getType();
			this.dcntCstcCd = dcntCstcMDto.getDcntCstcCd();
			this.dcntCstcNm = dcntCstcMDto.getDcntCstcNm();
			this.pnltOccrYn = dcntCstcMDto.getPnltOccrYn();
			this.dcntGrpKdCd = dcntCstcMDto.getDcntGrpKdCd();
			this.rgstUsr = dcntCstcMDto.getRgstUsr();
			this.rgstDtm = dcntCstcMDto.getRgstDtm();
			this.updUsr = dcntCstcMDto.getUpdUsr();
			this.updDtm = dcntCstcMDto.getUpdDtm();
		}
	}
	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {
		private String dcntKdCd;

		public AdditionalParamFields (DcntCstcMDto dcntCstcMDto) {
			this.dcntKdCd = dcntCstcMDto.getDcntKdCd();
		}
	}
}
