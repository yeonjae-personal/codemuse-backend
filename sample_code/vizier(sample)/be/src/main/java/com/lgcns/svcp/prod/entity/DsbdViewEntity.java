package com.lgcns.svcp.prod.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsbdViewEntity extends BaseEntity {
	
	private String dsbdViewUuid;
	private String dsbdViewName;
	private String dsbdViewCode;
	private String ctgrTypeCode;
	private String dsbdViewDscr;
	private Integer dsbdViewSortNo;
}
