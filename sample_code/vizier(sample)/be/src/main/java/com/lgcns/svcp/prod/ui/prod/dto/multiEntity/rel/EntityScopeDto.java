package com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel;

import com.lgcns.svcp.prod.ui.prod.enums.entity.EntityScope;

import lombok.Data;

@Data
public class EntityScopeDto {
	private String entityCode;
	private EntityScope entityScope;
}
