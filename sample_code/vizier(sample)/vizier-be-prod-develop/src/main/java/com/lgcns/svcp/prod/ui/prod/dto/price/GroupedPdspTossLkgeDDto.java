package com.lgcns.svcp.prod.ui.prod.dto.price;

import lombok.Data;

@Data
public class GroupedPdspTossLkgeDDto {
	public GroupedPdspTossLkgeDDto(PdspTossLkgeDDto pdspTossLkgeDDto) {
		this.generalDetails = new GeneralDetailFields(pdspTossLkgeDDto);
		this.additionalParams = new AdditionalParamFields(pdspTossLkgeDDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String pdspNm;
		private String pdspCd;
		private String valdEndDtm;

		public GeneralDetailFields (PdspTossLkgeDDto pdspTossLkgeDDto) {
			this.type = pdspTossLkgeDDto.getType();
			this.pdspNm = pdspTossLkgeDDto.getPdspNm();
			this.pdspCd = pdspTossLkgeDDto.getPdspCd();
			this.valdEndDtm = pdspTossLkgeDDto.getValdEndDtm();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {
		private String prvsYn;
		private String dvicPrvsYn;
		private String ipRqstNeedYn;
		private String workOrdrPrssYn;

		public AdditionalParamFields (PdspTossLkgeDDto pdspTossLkgeDDto) {
			this.prvsYn = pdspTossLkgeDDto.getPrvsYn();
			this.dvicPrvsYn = pdspTossLkgeDDto.getDvicPrvsYn();
			this.ipRqstNeedYn = pdspTossLkgeDDto.getIpRqstNeedYn();
			this.workOrdrPrssYn = pdspTossLkgeDDto.getWorkOrdrPrssYn();
		}
	}
}
