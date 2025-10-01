package com.lgcns.svcp.prod.ui.prod.dto.category.update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lgcns.svcp.prod.ui.prod.dto.category.CategoryDescriptionDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.CtgrLevelDto;
import com.lgcns.svcp.prod.util.UuidUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryTreeUpdateRequestDto {
    private String ctgrTabUuid;
    private CategoryDescriptionDto description;
    private List<CategorySaveDto> categoryTree;

    @JsonIgnore
    public List<CtgrLevelDto> getCtgrLevels() {
        List<CtgrLevelDto> ctgrLevels = new ArrayList<>();
        ctgrLevels.add(convertLevel("1", description.getLevel1()));
        ctgrLevels.add(convertLevel("2", description.getLevel2()));
        ctgrLevels.add(convertLevel("3", description.getLevel3()));
        ctgrLevels.add(convertLevel("4", description.getLevel4()));
        ctgrLevels.add(convertLevel("5", description.getLevel5()));
        return ctgrLevels;
    }

    @JsonIgnore
    public List<CategorySaveDto> getCategoryTreeFlat() {
        return flattenCategoryTree(categoryTree);
    }

    @JsonIgnore
    public List<CategoryOfferRelDto> getCategoryOfferFlat() {
        List<CategorySaveDto> inputList = getCategoryTreeFlat();
        return inputList.stream().flatMap(ctgr -> {
            String ctgrNodeUuid = ctgr.getCtgrNodeUuid();
            List<CategoryOfferRelDto> offerRel = ctgr.getOfferRel();
            if (offerRel == null) {
                offerRel = new ArrayList<>();
            }
            offerRel.forEach(b -> b.setCtgrNodeUuid(ctgrNodeUuid));
            return offerRel.stream();
        }).collect(Collectors.toList());
    }

    private CtgrLevelDto convertLevel(String levelNo, String levelDscrCntn) {
        CtgrLevelDto ctgrLevel = new CtgrLevelDto();
        ctgrLevel.setCtgrTabUuid(ctgrTabUuid);
        ctgrLevel.setLevelNo(levelNo);
        ctgrLevel.setLevelDscrCntn(levelDscrCntn);
        return ctgrLevel;
    }

    private List<CategorySaveDto> flattenCategoryTree(List<CategorySaveDto> inputList) {
        if (inputList == null) {
            return new ArrayList<>();
        }
        return inputList.stream().flatMap(ctgr -> {
            List<CategorySaveDto> children = ctgr.getChildren();
            if (ctgr.getCtgrNodeUuid() == null) {
                String ctgrNodeUuid = UuidUtil.generateRandomUUID();
                ctgr.setCtgrNodeUuid(ctgrNodeUuid);
                if (children != null) {
                    children.forEach(child -> {
                        if (child.getHpstCtgrNodeUuid() == null) {
                            child.setHpstCtgrNodeUuid(ctgrNodeUuid);
                        }
                    });
                }
            }
            ctgr.setCtgrTabUuid(ctgrTabUuid);
            List<CategorySaveDto> combined = new ArrayList<>();
            combined.add(ctgr);
            combined.addAll(flattenCategoryTree(children));
            return combined.stream();
        }).collect(Collectors.toList());
    }
}
