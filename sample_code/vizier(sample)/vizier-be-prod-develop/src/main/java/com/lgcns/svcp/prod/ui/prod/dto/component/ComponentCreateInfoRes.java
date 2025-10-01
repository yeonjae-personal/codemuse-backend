package com.lgcns.svcp.prod.ui.prod.dto.component;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.EntityItemRelResDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentCreateInfoRes extends ItemMappingDetailDto {
    private List<EntityItemRelResDto> entityItemInfos;
}
