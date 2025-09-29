package com.lgcns.svcp.prod.util.paging;

import java.util.List;

/**
 * Result DTO for paginated responses, including content and pagination
 * metadata.
 */
public class PageResult<T> {
	private final List<T> elements;
	private final int page;
	private final int size;
	private final long totalElements;
	private final int totalPages;
	private static final int[] CUSTOM_ROW_SIZE = new int[] { 10, 20, 50 };

	public PageResult(List<T> elements, int page, int size, long totalElements) {
		this.elements = elements;
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;
		this.totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
	}

	public List<T> getElements() {
		return elements;
	}

	public int getPage() {
		return page;
	}

	public int getSize() {
		return size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int[] getCustomRowSize() {
		return CUSTOM_ROW_SIZE;
	}
}