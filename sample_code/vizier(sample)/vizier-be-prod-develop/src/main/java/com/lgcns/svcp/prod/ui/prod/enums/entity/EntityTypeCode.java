package com.lgcns.svcp.prod.ui.prod.enums.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityTypeCode {
	EBL("EBL"),
	EDT("EDT"),
	ESC("ESC"),
	ETC("EUC"),
	UNKNOWN(null);
	
	private final String prefix;

	private static boolean isFixedCode(String code) {
		for (EntityTypeCode type : values()) {
			if (type.name().equals(code) && EntityTypeCode.valueOf(code) != EntityTypeCode.ETC) {
				return true;
			}
		}
		return false;
	}

	private static boolean isValidDynamicCode(String code) {
		return code != null && code.matches("^ETC\\d{5}$");
	}

//	public static boolean isValidCode(String code) {
//		return isFixedCode(code) || isValidDynamicCode(code);
//	}

	public static EntityTypeCode getEnumFromCode(String code) {
		if (isFixedCode(code)) {
			return EntityTypeCode.valueOf(code);
		} else if (isValidDynamicCode(code)) {
			return EntityTypeCode.ETC;
		}
		return EntityTypeCode.UNKNOWN;
	}
}
