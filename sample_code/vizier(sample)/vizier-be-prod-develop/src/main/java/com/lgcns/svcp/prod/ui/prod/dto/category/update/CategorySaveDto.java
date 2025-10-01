package com.lgcns.svcp.prod.ui.prod.dto.category.update;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.category.CatgMDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorySaveDto extends CatgMDto {
    private List<CategorySaveDto> children;
    private List<CategoryOfferRelDto> offerRel;
}
