package com.lgcns.svcp.prod.ui.prod.dto.characteristic;

import lombok.Data;

@Data
public class GroupedSlinInfoMDto {
	public GroupedSlinInfoMDto(SlinInfoMDto slinInfoMDto) {
		this.generalDetails = new GeneralDetailFields(slinInfoMDto);
		this.additionalParams = new AdditionalParamFields(slinInfoMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String slinInfoCd;
		private String slinInfoNm;
		private String slinMgmtUnitCd;
		private String valdEndDtm;

		public GeneralDetailFields (SlinInfoMDto slinInfoMDto) {
			this.type = slinInfoMDto.getType();
			this.slinInfoCd = slinInfoMDto.getSlinInfoCd();
			this.slinInfoNm = slinInfoMDto.getSlinInfoNm();
			this.slinMgmtUnitCd = slinInfoMDto.getSlinMgmtUnitCd();
			this.valdEndDtm = slinInfoMDto.getValdEndDtm();
		}
	}


	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {

		public AdditionalParamFields (SlinInfoMDto slinInfoMDto) {
		}
	}
}
