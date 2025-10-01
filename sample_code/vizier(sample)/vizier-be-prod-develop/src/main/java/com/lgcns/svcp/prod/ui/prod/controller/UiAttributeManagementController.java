package com.lgcns.svcp.prod.ui.prod.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeUpperLowerItemsDto;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeViewDto;
import com.lgcns.svcp.prod.ui.prod.service.UiAttributeManagementService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Hidden
@CrossOrigin
@RestController
@RequestMapping("/ui/admin/attribute")
@Validated
@Tag(name = "UI-admin-attribute-management-controller", description = "ui admin attribute management controller")
@RequiredArgsConstructor
public class UiAttributeManagementController {
	
	private final UiAttributeManagementService attributeService;

	@GetMapping
	@Operation(summary = "show list attribute", description = "show list attribute")
	public List<AttributeViewDto> getData() {
		return attributeService.getData();
	}
	
	@GetMapping("/detail")
	@Operation(summary = "show attribute detail", description = "show attribute detail")
	public AttributeDetailDto getAttributeDetail(@RequestParam String code) {
		return attributeService.getDetail(code);
	}
	
	@GetMapping("/item")
	@Operation(summary = "get upper/lower items", description = "get upper/lower items")
	public AttributeUpperLowerItemsDto getItems(@RequestParam String largeItem) {
		return attributeService.getUpperOrLowerItems(largeItem);
	}
	
	@PostMapping
	@Operation(summary = "save attribute", description = "save attribute")
	public void saveAttribute(@Valid @RequestBody AttributeDetailDto request) {
		attributeService.save(request);
	} 
}
