package com.lgcns.svcp.prod.ui.prod.util;

import com.lgcns.svcp.prod.ui.prod.dto.common.Attribute;

import java.util.regex.Pattern;

public class AttributeUtil {
    public static Attribute createAttribute(String value) {
        return new Attribute(value, null);
    }

    public static String camelToSnake(String str) {
        return Pattern.compile("([a-z])([A-Z]+)")
                .matcher(str)
                .replaceAll("$1_$2")
                .toLowerCase();
    }

    public static String snakeToCamel(String str) {
        return Pattern.compile("_([a-z])")
                .matcher(str)
                .replaceAll(m -> m.group(1).toUpperCase());
    }
}
