package com.lgcns.svcp.prod.ui.prod.dto.characteristic;

import java.util.List;

import lombok.Data;

@Data
public class GroupedLobInfoDto {
	public GroupedLobInfoDto(LobInfoDto lobInfoDto) {
		this.generalDetails = new GeneralDetailFields(lobInfoDto);
		this.additionalParams = new AdditionalParamFields(lobInfoDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String lobCd;
		private String lobNm;
		private String mrktCd;
		private String svcCd;
//		private List<LobMrktRelDDto> mrktInfoList;

		private String valdEndDtm;

		public GeneralDetailFields (LobInfoDto lobInfoDto) {
			this.type = lobInfoDto.getType();
			this.lobCd = lobInfoDto.getLobCd();
//			this.mrktInfoList = lobInfoDto.getLobMrktRelD();
			this.lobNm = lobInfoDto.getLobNm();
			this.svcCd = lobInfoDto.getSvcCd();
			this.valdEndDtm = lobInfoDto.getValdEndDtm();
		}
	}
	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {
		private String majrUseYn;

		public AdditionalParamFields (LobInfoDto lobInfoDto) {
			this.majrUseYn = lobInfoDto.getMajrUseYn();
		}
	}
}
