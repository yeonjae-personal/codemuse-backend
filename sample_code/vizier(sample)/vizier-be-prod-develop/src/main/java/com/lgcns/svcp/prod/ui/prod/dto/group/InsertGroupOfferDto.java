package com.lgcns.svcp.prod.ui.prod.dto.group;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsertGroupOfferDto extends BaseDto {
	private String groupUuid;
	private String offerUuid;
	private String validStartDtm;
	private String validEndDtm;
}
