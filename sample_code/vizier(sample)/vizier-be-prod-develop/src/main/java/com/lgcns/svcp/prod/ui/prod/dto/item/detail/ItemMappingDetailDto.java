package com.lgcns.svcp.prod.ui.prod.dto.item.detail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemMappingDetailDto {
    private List<GeneralDetailDto> general;
    private List<AdditionalDetailDto> additional;

    @JsonIgnore
    public Map<String, String> getGeneralParam() {
        Map<String, String> params = new HashMap<>();
        if (general != null) {
            for (GeneralDetailDto gnrl : general) {
                String camelKey = snakeToCamel(gnrl.getColName());
                params.put(camelKey, gnrl.getAttrVal());
            }
        }
        return params;
    }

    @JsonIgnore
    public Map<String, String> getGeneralEditYParam() {
        Map<String, String> params = new HashMap<>();
        if (general != null) {
            for (GeneralDetailDto gnrl : general) {
                if ("Y".equals(gnrl.getEditYn())) {
                    String camelKey = snakeToCamel(gnrl.getColName());
                    params.put(camelKey, gnrl.getAttrVal());
                }
            }
        }
        return params;
    }

    @JsonIgnore
    public Map<String, String> getAdditionalParam() {
        Map<String, String> params = new HashMap<>();
        if (additional != null) {
            for (AdditionalDetailDto add : additional) {
                params.put(add.getAttrUuid(), add.getAttrVal());
            }
        }
        return params;
    }

    private String snakeToCamel(String snake) {
        if (snake == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (int i = 0; i < snake.length(); i++) {
            char c = snake.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else if (nextUpper) {
                result.append(Character.toUpperCase(c));
                nextUpper = false;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
