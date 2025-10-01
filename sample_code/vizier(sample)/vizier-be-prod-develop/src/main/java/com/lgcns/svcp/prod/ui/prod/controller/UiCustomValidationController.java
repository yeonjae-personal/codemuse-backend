package com.lgcns.svcp.prod.ui.prod.controller;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.BoxAndAttrRequest;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationItemResponse;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationMainRespone;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.SaveCusSearchDto;
import com.lgcns.svcp.prod.ui.prod.dto.customvalidation.history.HistoryResponse;
import com.lgcns.svcp.prod.ui.prod.service.UiCustomValidationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/ui/custom-validation")
@Validated
@Tag(name = "UI-custom-validation-controller", description = "ui custom validation controller")
@RequiredArgsConstructor
public class UiCustomValidationController {
	
	private final UiCustomValidationService customValidationService;
	
	@GetMapping
	@Operation(summary = "get list custom validation in main", description ="get list custom validation in main")
	public List<CustomValidationMainRespone> showListMain(@RequestParam String item, @RequestParam String type,
																		@RequestParam(required = false) String subType,
																		@RequestParam(required = false) String attrUuid) {
		return customValidationService.getListMain(item, type, subType, attrUuid);
	}
	
	@GetMapping("/admin")
	@Operation(summary = "load data custom validation in admin", description ="load data custom validation in admin")
	public Object showListAdmin(@RequestParam String view, @RequestParam(required = false) String searchBy, @RequestParam(required = false) String item, @RequestParam(required = false) String type,
														   @RequestParam(required = false) String action, @RequestParam(required = false) String subType, 
														   @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, HttpServletResponse response, HttpServletRequest request) throws IOException {
		if (view.equals("gridview")) {
			if (StringUtils.isBlank(action)) {
				action = "condition";
			}
			return customValidationService.getListAdmin(item, type, subType, action);
		} else if (view.equals("listview")) {
			String language = request.getHeader("X-Language");
			if (StringUtils.isBlank(searchBy)) {
				searchBy = "C";
			}
			return customValidationService.getData(page, size, searchBy, item, type, subType, language);
		}
		throw new BusinessException("View params is not matching!");
	}
	
	@GetMapping("/admin/export")
	@Operation(summary = "export custom validation excel", description ="export custom validation excel")
	public void exportExcel(@RequestParam(required = false) String searchBy, @RequestParam(required = false) String item, @RequestParam(required = false) String type,
			   				@RequestParam(required = false) String action, @RequestParam(required = false) String subType, @RequestParam(required = false) String language, HttpServletResponse response) {
		customValidationService.exportExcel(searchBy, item, type, subType, language, response);
	}
	
	@PostMapping("/admin")
	@Operation(summary = "save validation", description ="save validation")
	public List<SaveCusSearchDto> saveValidation(@Valid @RequestBody BoxAndAttrRequest request) {
		return customValidationService.save(request);
	}
	
	@GetMapping("/history")
	@Operation(summary = "get validation history", description ="get validation history")
	public HistoryResponse getValidationHistory(@RequestParam String validCode) {
		return customValidationService.getHistory(validCode);
	}
	
	@GetMapping("/item")
	@Operation(summary = "get item/type/subtype custom validation", description ="get item/type/subtype custom validation")
	public List<CustomValidationItemResponse> getItem(@RequestParam(required = false) String item, @RequestParam(required = false) String type, @RequestParam(required = false) String subType) {
		return customValidationService.getItem(item, type, subType);
	}
}
