package com.lgcns.svcp.prod.ui.prod.dto.offer;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.common.ColumnMetaDataDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralPricePlanAndAddOnCreateDto {
	private List<ColumnMetaDataDto> columnMetaData;
}
