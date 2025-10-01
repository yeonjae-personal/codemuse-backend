package com.lgcns.svcp.prod.ui.prod.service;

import java.io.IOException;
import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.label.LabelItemDto;
import com.lgcns.svcp.prod.ui.prod.dto.label.LabelSearchPagingDto;
import com.lgcns.svcp.prod.ui.prod.dto.label.MultiLangLabelDto;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UiLabelService {
	PageResult<MultiLangLabelDto> findAll(LabelSearchPagingDto params);

	void save(MultiLangLabelDto input);

	void delete(String labelId);

	void exportExcel(String type, String value, String language, HttpServletResponse response);

	void importExcel(HttpServletRequest request) throws IOException, ServletException;

	List<MultiLangLabelDto> findLanguageI18n();

	List<LabelItemDto> getAllLanguage();
}
