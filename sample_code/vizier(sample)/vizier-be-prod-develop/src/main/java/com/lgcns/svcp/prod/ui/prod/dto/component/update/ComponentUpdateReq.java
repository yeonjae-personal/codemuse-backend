package com.lgcns.svcp.prod.ui.prod.dto.component.update;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.SaveEntityObjRelReqDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentUpdateReq extends ItemMappingDetailDto {
    private List<ComponentUpdateResourceReq> resources;
    private List<SaveEntityObjRelReqDto> insertEntityObjRels;
    private List<SaveEntityObjRelReqDto> updateEntityObjRels;
}
