package com.lgcns.svcp.prod.online.prod.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto;

public class MapUtil {
	public static Map<String, Object> mapToFlatMapWithNullHandling(Map<String, Object> result, List<AdditionalColumnsDto> additionalColumns) {
		// 모든 키-값 쌍을 그대로 복사
		Map<String, Object> flatMap = result.entrySet().stream()
				.collect(Collectors.toMap(
						entry -> toCamelCase(entry.getKey()), 
						Map.Entry::getValue, 
						(oldValue, newValue) -> oldValue, 
						LinkedHashMap::new));
		// additionalColumns에서 컬럼 이름 가져와 처리
		additionalColumns.forEach(column -> {
			String columnName = toCamelCase(column.getAttrName());
			flatMap.putIfAbsent(columnName, null); // 값이 없으면 null로 초기화
		});

		return flatMap;
	}

	public static String toCamelCase(String input) {
		//공백과 _로 구분된 단어들을 분리
		String[] words = input.split("[ _]+");

		StringBuilder camelCaseString = new StringBuilder();

		// 첫 번째 단어는 모두 소문자로 처리
		if (words.length > 0) {
			camelCaseString.append(words[0].toLowerCase());
		}

		// 나머지 단어들은 첫 글자를 대문자로 변환
		for (int i = 1; i < words.length; i++) {
			String word = words[i];
			if (!word.isEmpty()) {
				camelCaseString.append(word.substring(0, 1).toUpperCase()) // 첫 글자 대문자
				.append(word.substring(1).toLowerCase());   // 나머지는 소문자
			}
		}

		return camelCaseString.toString();
	}
}
