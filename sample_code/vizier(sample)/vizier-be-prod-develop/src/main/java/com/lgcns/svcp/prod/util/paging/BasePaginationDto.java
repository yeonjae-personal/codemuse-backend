package com.lgcns.svcp.prod.util.paging;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Base DTO for pagination, with page starting from 1, auto-calculated
 * offset/limit, and dynamic sorting.
 */
public class BasePaginationDto {
	
	private int page = 1; // Page starts at 1
	private int size = 10; // Default 10 records per page
	private String sort; // Sort field and direction, e.g., "col1 ASC,col2 DESC,col3"

	public int getPage() {
		return page;
	}

	/**
	 * Ensures page is at least 1.
	 */
	public void setPage(int page) {
		this.page = Math.max(page, 1);
	}

	public int getSize() {
		return size;
	}

	/**
	 * Ensures size is greater than 0, defaults to 10 if invalid.
	 */
	public void setSize(int size) {
		this.size = (size <= 0) ? 10 : size;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * Parses sort string (e.g., "col1 ASC,col2 DESC,col3") into a Map. Default
	 * direction is "ASC" if not specified. Preserves order of fields.
	 * 
	 * @return Map with field as key and direction as value.
	 */
	@JsonIgnore
	public Map<String, String> getSortMap() {
		Map<String, String> sortMap = new LinkedHashMap<>();
		if (sort == null || sort.trim().isEmpty()) {
			return sortMap;
		}

		String[] pairs = sort.split(",");
		for (String pair : pairs) {
			String[] parts = pair.trim().split("\\s+");
			String field = parts[0].trim();
			String direction = (parts.length > 1 && parts[1].trim().equalsIgnoreCase("DESC")) ? "DESC" : "ASC";
			if (!field.isEmpty()) {
				sortMap.put(field, direction);
			}
		}
		return sortMap;
	}
}