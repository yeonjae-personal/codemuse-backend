package com.lgcns.svcp.prod.ui.prod.dto.multiEntity.detail;

import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.MultiEntityDto;

import lombok.Data;

@Data
public class BsnLineDto extends MultiEntityDto {
	private String bsnLineTypeCode;
	private String ovwCntn;
}
