package com.lgcns.svcp.prod.ui.prod.dto.group;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.extend.ItemOffrResDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveGroupReqDto extends ItemMappingDetailDto {
    private List<ItemOffrResDto> offerRel;
}