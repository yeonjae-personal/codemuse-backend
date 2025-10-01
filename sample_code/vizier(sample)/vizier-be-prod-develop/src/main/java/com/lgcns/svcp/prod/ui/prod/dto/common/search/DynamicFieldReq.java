package com.lgcns.svcp.prod.ui.prod.dto.common.search;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DynamicFieldReq {
    private String fieldName;
    private String fieldType;
    private String fieldValue;
    private String fieldValueMin;
    private String fieldValueMax;
    private List<String> fieldValues;

    public List<String> getFieldValues() {
        if ((fieldValues == null || fieldValues.isEmpty())
                && ("DL".equalsIgnoreCase(this.fieldType) || "DM".equalsIgnoreCase(this.fieldType))) {
            if (fieldValue == null || fieldValue.trim().isEmpty() || "[]".equals(fieldValue.trim()))
                return null;
            String trimmed = fieldValue.trim();
            if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                trimmed = trimmed.substring(1, trimmed.length() - 1);
            }
//            if (trimmed.trim().isEmpty())
//                return null;
            String[] parts = trimmed.split("\\s*,\\s*");
            List<String> result = new ArrayList<>();
            for (String part : parts) {
                part = part.trim();
                if (part.startsWith("\"") && part.endsWith("\"") && part.length() >= 2) {
                    part = part.substring(1, part.length() - 1);
                }
                result.add(part);
            }
            return result;
        }
        return fieldValues;
    }
}
