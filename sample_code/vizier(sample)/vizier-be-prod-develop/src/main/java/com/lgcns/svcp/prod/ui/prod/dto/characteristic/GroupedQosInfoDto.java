package com.lgcns.svcp.prod.ui.prod.dto.characteristic;

import java.util.List;

import lombok.Data;

@Data
public class GroupedQosInfoDto {
	public GroupedQosInfoDto(QosInfoDto qosInfoDto) {
		this.generalDetails = new GeneralDetailFields(qosInfoDto);
		this.additionalParams = new AdditionalParamFields(qosInfoDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String qosCd;
		private String qosNm;
		private String qosPlcyCd;
		private String qosPlcyGrpCd;
		private String thrsIdfyCd;
//		private List<QosPlcyRelDDto> qosPlcyList;

		private String valdStrtDtm;
		private String valdEndDtm;


		public GeneralDetailFields (QosInfoDto qosInfoDto) {
			this.type = qosInfoDto.getType();
			this.qosCd = qosInfoDto.getQosCd();
			this.qosNm = qosInfoDto.getQosNm();
//			this.qosPlcyList = qosInfoDto.getQosPlcyRelDDto();
			this.qosPlcyGrpCd = qosInfoDto.getQosPlcyGrpCd();
			this.thrsIdfyCd = qosInfoDto.getThrsIdfyCd();
			this.valdStrtDtm = qosInfoDto.getValdStrtDtm();
			this.valdEndDtm = qosInfoDto.getValdEndDtm();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {

		public AdditionalParamFields (QosInfoDto qosInfoDto) {
		}
	}

}
