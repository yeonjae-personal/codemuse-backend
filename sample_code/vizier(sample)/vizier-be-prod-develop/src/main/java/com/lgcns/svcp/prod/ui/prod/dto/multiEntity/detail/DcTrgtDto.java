package com.lgcns.svcp.prod.ui.prod.dto.multiEntity.detail;

import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.MultiEntityDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DcTrgtDto extends MultiEntityDto {
	private String groupUuid;
	private String offerUuid;
	private String cpntUuid;
	private String rscUuid;
	private String chrgTypeCode;
	private String ovwCntn;
	private String groupName;
	private String offerName;
	private String cpntName;
	private String rscName;
}
