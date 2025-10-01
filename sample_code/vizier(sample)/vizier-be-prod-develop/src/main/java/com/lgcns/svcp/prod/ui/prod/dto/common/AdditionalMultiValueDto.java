package com.lgcns.svcp.prod.ui.prod.dto.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.util.StringUtilCustom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalMultiValueDto extends BaseDto {
    private String objUuid;
    private String attrUuid;
    private int attrSeq;
    private String attrVal;

    public static List<AdditionalMultiValueDto> parseVals(String objUuid, String attrUuid, String attrVals) {
        if (StringUtilCustom.isEmpty(attrVals) || "[]".equals(attrVals)) {
            return null;
        }
        List<String> multiValue = Arrays.stream(attrVals.replaceAll("[\"\\[\\]]", "").split(",")).toList();
        List<AdditionalMultiValueDto> result = new ArrayList<>();
        if (!multiValue.isEmpty()) {
            for (int i = 0; i < multiValue.size(); i++) {
                AdditionalMultiValueDto dto = new AdditionalMultiValueDto();
                dto.setObjUuid(objUuid);
                dto.setAttrUuid(attrUuid);
                dto.setAttrVal(multiValue.get(i));
                dto.setAttrSeq(i);
                result.add(dto);
            }
        }

        return result;
    }
}
