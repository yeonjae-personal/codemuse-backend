package com.lgcns.svcp.prod.ruleengine.entity;

import com.lgcns.svcp.prod.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldEntity extends BaseEntity {
	
	private String fieldUuid;
	private String fieldDispName;
	private String fieldKeyName;
	private String fieldDataType;
	private String useYn;
}
