package com.lgcns.svcp.prod.ui.prod.service;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.BoxAndAttrRequest;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.BoxAndAttrRespone;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationItemResponse;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationMainRespone;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.SaveCusSearchDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationPagingResponse;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.history.HistoryResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

public interface UiCustomValidationService {
	List<CustomValidationMainRespone> getListMain(String item, String type, String subType, String attrUuid);
	BoxAndAttrRespone getListAdmin(String item, String type, String subType, String action);
	List<SaveCusSearchDto> save(@Valid BoxAndAttrRequest request);
	HistoryResponse getHistory(String validCode);
	CustomValidationPagingResponse getData(int page, int size, String searchBy, String item, String type, String subType, String language);
	void exportExcel(String searchBy, String item, String type, String subType, String language, HttpServletResponse response);
	List<CustomValidationItemResponse> getItem(String item, String type, String subType);
}
