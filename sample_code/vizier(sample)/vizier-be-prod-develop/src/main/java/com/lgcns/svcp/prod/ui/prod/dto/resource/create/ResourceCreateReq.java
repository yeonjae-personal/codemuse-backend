package com.lgcns.svcp.prod.ui.prod.dto.resource.create;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.SaveEntityObjRelReqDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceCreateReq extends ItemMappingDetailDto {
    private List<SaveEntityObjRelReqDto> insertEntityObjRels;
}
