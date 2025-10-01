package com.lgcns.svcp.prod.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsbdViewPstEntity extends BaseEntity {
	
	private String dsbdViewUuid;
	private String userId;
	private Integer posX;
	private Integer posY;
}
