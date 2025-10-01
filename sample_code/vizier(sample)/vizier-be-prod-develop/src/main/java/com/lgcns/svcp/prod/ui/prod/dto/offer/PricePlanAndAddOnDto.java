package com.lgcns.svcp.prod.ui.prod.dto.offer;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.ColumnMetaDataDto;

import lombok.Data;

@Data
public class PricePlanAndAddOnDto extends BaseDto {
	private String type;
	private String prodUuid;
	private String prodCd;
	private String prodNm;
	private String prodKdCd;
	private String custKdCd;
	private String prodAgeDivsCd;
	private String prodValdStrtDtm;
	private String prodValdEndDtm;
	private String saleValdStrtDtm;
	private String saleValdEndDtm;
	private String prodOvwDesc;
	private String duplTrgtUuid;

	private List<ColumnMetaDataDto> columnMetaData;
}
