package com.lgcns.svcp.prod.ui.prod.dto.characteristic;

import java.util.List;

import lombok.Data;

@Data
public class GroupedSpamInfoDto {
	public GroupedSpamInfoDto(SpamInfoDto spamInfoDto) {
		this.generalDetails = new GeneralDetailFields(spamInfoDto);
		this.additionalParams = new AdditionalParamFields(spamInfoDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String spamCd;
		private String spamNm;
		private String thrsIdfyCd;
		private String lvwuPlcyCd;
//		private List<SpamLvwuPlcyDDto> lvwuPlcyInfoList;
		private String valdStrtDtm;
		private String valdEndDtm;

		public GeneralDetailFields (SpamInfoDto spamInfoDto) {
			this.type = spamInfoDto.getType();
			this.spamCd = spamInfoDto.getSpamCd();
			this.spamNm = spamInfoDto.getSpamNm();
			this.thrsIdfyCd = spamInfoDto.getThrsIdfyCd();
//			this.lvwuPlcyInfoList = spamInfoDto.getSpamLvwuPlcyDDto();
			this.valdStrtDtm = spamInfoDto.getValdStrtDtm();
			this.valdEndDtm = spamInfoDto.getValdEndDtm();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {

		public AdditionalParamFields (SpamInfoDto spamInfoDto) {
		}
	}
}
