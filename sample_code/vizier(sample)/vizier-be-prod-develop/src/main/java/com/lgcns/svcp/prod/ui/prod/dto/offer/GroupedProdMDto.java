package com.lgcns.svcp.prod.ui.prod.dto.offer;

import java.util.List;
import java.util.Map;

import com.lgcns.svcp.prod.ui.prod.dto.common.ColumnMetaDataDto;

import lombok.Data;

@Data
public class GroupedProdMDto {
	public GroupedProdMDto(ProdMDto prodMDto) {
		this.generalDetails = new GeneralDetailFields(prodMDto);
//		this.salesPeriod = new SalesPeriodFields(prodMDto);
		this.additionalParams = new AdditionalParamFields(prodMDto);
		this.overView = prodMDto.getOverView();
		this.comment = prodMDto.getComment();
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String prodCd;
		private String prodNm;
		private String prodKdCd;
//		private Map<String, String> prodKdCd;
		private String custKdCd;
		private String prodAgeDivsCd;
		private String saleValdStrtDtm;
		private String saleValdEndDtm;

		public GeneralDetailFields (ProdMDto prodMDto) {
			this.type = prodMDto.getType();
			this.prodCd = prodMDto.getProdCd();
			this.prodNm = prodMDto.getProdNm();
			this.prodKdCd = prodMDto.getProdKdCd();
			this.custKdCd = prodMDto.getCustKdCd();
			this.prodAgeDivsCd = prodMDto.getProdAgeDivsCd();
			this.saleValdStrtDtm = prodMDto.getSaleValdStrtDtm();
			this.saleValdEndDtm = prodMDto.getSaleValdEndDtm();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class AdditionalParamFields {
		private String smsNotiYn;
		private String prodGrdCd;
		private String expyImpsbTermDays;
		private String rjnImpsbTermDays;
		private String mbspDcntRqstPsblYn;

		public AdditionalParamFields (ProdMDto prodMDto) {
			this.smsNotiYn = prodMDto.getSmsNotiYn();
			this.prodGrdCd = prodMDto.getProdGrdCd();
			this.expyImpsbTermDays = prodMDto.getExpyImpsbTermDays();
			this.rjnImpsbTermDays = prodMDto.getRjnImpsbTermDays();
			this.mbspDcntRqstPsblYn = prodMDto.getMbspDcntRqstPsblYn();
		}
	}
	
	private String overView;
	private String comment;
	private List<ColumnMetaDataDto> columnMetaData;
}
