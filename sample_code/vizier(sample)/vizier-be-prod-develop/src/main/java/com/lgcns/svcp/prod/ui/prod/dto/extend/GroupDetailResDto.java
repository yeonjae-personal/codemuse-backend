package com.lgcns.svcp.prod.ui.prod.dto.extend;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupDetailResDto extends ItemMappingDetailDto {
    private List<ItemOffrResDto> childOffr;
}
