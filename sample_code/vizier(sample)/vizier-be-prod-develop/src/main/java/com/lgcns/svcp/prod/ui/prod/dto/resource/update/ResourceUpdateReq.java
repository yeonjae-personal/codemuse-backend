package com.lgcns.svcp.prod.ui.prod.dto.resource.update;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.SaveEntityObjRelReqDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceUpdateReq extends ItemMappingDetailDto {
    private List<SaveEntityObjRelReqDto> insertEntityObjRels;
    private List<SaveEntityObjRelReqDto> updateEntityObjRels;
}
