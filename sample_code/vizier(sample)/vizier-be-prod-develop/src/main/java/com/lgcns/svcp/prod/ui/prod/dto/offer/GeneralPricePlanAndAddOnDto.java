package com.lgcns.svcp.prod.ui.prod.dto.offer;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.common.ColumnMetaDataDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.Attribute;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralPricePlanAndAddOnDto {
	private Attribute type;
	private Attribute prodUuid;
	private Attribute prodCd;
	private Attribute prodNm;
	private Attribute prodKdCd;
	private Attribute custKdCd;
	private Attribute prodAgeDivsCd;
	private Attribute saleValdStrtDtm;
	private Attribute saleValdEndDtm;
	private Attribute prodOvwDesc;

	private List<ColumnMetaDataDto> columnMetaData;
}
