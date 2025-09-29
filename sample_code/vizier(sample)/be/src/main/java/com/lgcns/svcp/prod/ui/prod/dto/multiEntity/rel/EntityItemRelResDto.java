package com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.export.MultiEntityExportDto;
import com.lgcns.svcp.prod.ui.prod.enums.entity.EntityScope;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityItemRelResDto {
	private String itemCode;
	private String entityTypeCode;
	private String entityTypeName;
	private EntityScope entityScope;
	private int sortNo;
	private List<EntityObjRelResDto> objRel;
	private List<MultiEntityExportDto> multiEntityExportDtos;
}
