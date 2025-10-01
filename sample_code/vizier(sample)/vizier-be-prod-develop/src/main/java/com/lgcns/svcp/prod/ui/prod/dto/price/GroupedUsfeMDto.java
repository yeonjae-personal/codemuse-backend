package com.lgcns.svcp.prod.ui.prod.dto.price;

import lombok.Data;

@Data
public class GroupedUsfeMDto {

	public GroupedUsfeMDto(UsfeMDto usfeMDto) {
		this.generalDetails = new GeneralDetailFields(usfeMDto);
		this.additionalParams = new AdditionalParamFields(usfeMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String usfeCd;
		private String usfeNm;
		private String ratDivsCd;
		private String ioclDivsCd;
		private String usfeAplyUval;
		private String ratAplyUnitCd;
		private String useRat;
		private String ratAplyKdCd;
		private String valdStrtDtm;
		private String valdEndDtm;

		public GeneralDetailFields (UsfeMDto usfeMDto) {
			this.type = usfeMDto.getType();
			this.usfeCd = usfeMDto.getUsfeCd();
			this.usfeNm = usfeMDto.getUsfeNm();
			this.ratDivsCd = usfeMDto.getRatDivsCd();
			this.ioclDivsCd = usfeMDto.getIoclDivsCd();
			this.usfeAplyUval = usfeMDto.getUsfeAplyUval();
			this.ratAplyUnitCd = usfeMDto.getRatAplyUnitCd();
			this.useRat = usfeMDto.getUseRat();
			this.ratAplyKdCd = usfeMDto.getRatAplyKdCd();
			this.valdStrtDtm = usfeMDto.getValdStrtDtm();
			this.valdEndDtm = usfeMDto.getValdEndDtm();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {
		private String asgnCondDivsCd;
		private String initUsfeAplyUval;
		private String initRatAplyUnitCd;
		private String initUsfeAplyUnitInclYn;
		private String initUsfe;

		public AdditionalParamFields (UsfeMDto usfeMDto) {
			this.asgnCondDivsCd = usfeMDto.getAsgnCondDivsCd();
			this.initUsfeAplyUval = usfeMDto.getInitUsfeAplyUval();
			this.initRatAplyUnitCd = usfeMDto.getInitRatAplyUnitCd();
			this.initUsfeAplyUnitInclYn = usfeMDto.getInitUsfeAplyUnitInclYn();
			this.initUsfe = usfeMDto.getInitUsfe();
		}
	}
}

