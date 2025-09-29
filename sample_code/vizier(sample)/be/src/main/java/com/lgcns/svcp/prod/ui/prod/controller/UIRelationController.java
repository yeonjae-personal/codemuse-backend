package com.lgcns.svcp.prod.ui.prod.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.common.search.SearchAdvancedReq;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.service.UIRelationService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ui/relation")
@Tag(name = "UI-relation-controller", description = "Relation Management Controller")
public class UIRelationController {
	private final UIRelationService uiRelationService;

	@GetMapping(value = "/search/advanced")
	@Operation(summary = "Retrieve advanced list of relations", description = "Fetches a filtered list of relations based on advanced search criteria.")
	public PageResult<?> retrieveRelationsAdvanced(@ModelAttribute SearchAdvancedReq searchAdvancedReq) {
		return uiRelationService.retrieveRelationsAdvanced(searchAdvancedReq);
	}

	@GetMapping("/create-info")
	@Operation(summary = "Get full realtion creation info")
	public ItemMappingDetailDto retrieveRelationCreateInfo(@RequestParam String itemCode,
			@RequestParam(required = false, defaultValue = "en") String language) {
		return uiRelationService.retrieveRelationCreateInfo(itemCode, language);
	}

	@PostMapping()
	@Operation(summary = "Create realtion")
	public String createRelation(@RequestBody ItemMappingDetailDto req) {
		return uiRelationService.createRelation(req);
	}

	@PutMapping()
	@Operation(summary = "Update relation")
	public void updateRelation(@RequestBody ItemMappingDetailDto req) {
		uiRelationService.updateRelation(req);
	}
}
