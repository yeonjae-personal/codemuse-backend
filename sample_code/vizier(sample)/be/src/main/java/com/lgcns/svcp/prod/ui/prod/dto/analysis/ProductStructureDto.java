package com.lgcns.svcp.prod.ui.prod.dto.analysis;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductStructureDto extends BasePaginationDto {
	private String offrCd;
	private String offrUuid;
	private String offrNm;
	private String cmpCd;
	private String cmpUuid;
	private String cmpNm;
	private String cmpValdStrtDtm;
	private String cmpValdEndDtm;
	private String svcCd;
	private String svcUuid;
	private String svcNm;
	private String svcValdStrtDtm;
	private String svcValdEndDtm;

	private String objCode;
	private String objName;
	private String lctgrItemCode;
}
