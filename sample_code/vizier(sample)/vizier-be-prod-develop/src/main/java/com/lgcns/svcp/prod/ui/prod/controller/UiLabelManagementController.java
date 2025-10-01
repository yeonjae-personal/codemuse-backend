package com.lgcns.svcp.prod.ui.prod.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.label.LabelItemDto;
import com.lgcns.svcp.prod.ui.prod.dto.label.LabelSearchPagingDto;
import com.lgcns.svcp.prod.ui.prod.dto.label.MultiLangLabelDto;
import com.lgcns.svcp.prod.ui.prod.service.UiLabelService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/ui/admin/label")
@Validated
@Tag(name = "UI-admin-label-management-controller", description = "ui admin label management controller")
@RequiredArgsConstructor
public class UiLabelManagementController {

	private final UiLabelService labelService;

	@GetMapping
	@Operation(summary = "show label", description = "show label")
	public PageResult<MultiLangLabelDto> getData(@RequestParam Integer page, @RequestParam Integer size, 
							@RequestParam(required = false) String type, @RequestParam(required = false) String value, HttpServletRequest request) {
		String language = request.getHeader("X-Language");
		LabelSearchPagingDto searchPaging = new LabelSearchPagingDto();
		if (page != null && size != null) {
			searchPaging.setPage(page);
			searchPaging.setSize(size);
		}
		searchPaging.setType(type);
		searchPaging.setValue(value);
		searchPaging.setLangCode(language);
		return labelService.findAll(searchPaging);
	}

	@GetMapping("/i18n")
	@Operation(summary = "get language i18n", description = "get language i18n")
	public List<MultiLangLabelDto> getLanguageI18n() {
		return labelService.findLanguageI18n();
	}
	
	@GetMapping("/language")
	@Operation(summary = "get all language", description = "get language i18n")
	public List<LabelItemDto> getAllLanguage() {
		return labelService.getAllLanguage();
	}

	@PostMapping
	@Operation(summary = "save label", description = "save label")
	public void saveLabel(@RequestBody MultiLangLabelDto request) {
		labelService.save(request);
	}

	@PutMapping
	@Operation(summary = "update label", description = "update label")
	public void updateLabel(@RequestBody MultiLangLabelDto request) {
		labelService.save(request);
	}

	@DeleteMapping
	@Operation(summary = "delete label", description = "delete label")
	public void deleteLabel(@RequestParam String labelId) {
		labelService.delete(labelId);
	}

	@GetMapping("/export")
	@Operation(summary = "export label", description = "export label")
	public void exportLabel(@RequestParam(required = false) String type, @RequestParam(required = false) String value,
			@RequestParam String language, HttpServletResponse response) {
		labelService.exportExcel(type, value, language, response);
	}

	@PostMapping("/import")
	@Operation(summary = "import label", description = "import label")
	public void importLabel(HttpServletRequest request) throws IOException, ServletException {
		labelService.importExcel(request);
	}
}
