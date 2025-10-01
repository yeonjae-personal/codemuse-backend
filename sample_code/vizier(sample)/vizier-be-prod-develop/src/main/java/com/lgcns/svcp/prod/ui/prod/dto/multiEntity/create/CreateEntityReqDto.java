package com.lgcns.svcp.prod.ui.prod.dto.multiEntity.create;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.MultiEntityAdditionalDto;

import lombok.Data;

@Data
public class CreateEntityReqDto extends BaseDto {
	private String entityCode;
	private String entityName;
	private String entityTypeCode;
	private String validStartDtm;
	private String validEndDtm;
	private String ovwCntn;
	private List<MultiEntityAdditionalDto> additional;

	/* BsnLineDto */
	private String bsnLineTypeCode;

	/* DcTrgtDto */
	private String groupUuid;
	private String offerUuid;
	private String cpntUuid;
	private String rscUuid;
	private String chrgTypeCode;

	/* SaleCpnyDto */
	private String mvnoBsnoYn;

}
