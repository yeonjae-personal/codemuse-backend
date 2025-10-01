package com.lgcns.svcp.prod.ui.prod.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.item.LargeItemDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.MiddleItemDto;
import com.lgcns.svcp.prod.ui.prod.service.MetadataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/ui/metadata")
@Tag(name = "UI-metadata-controller", description = "metadata controller")
@RequiredArgsConstructor
public class UiMetadataController {
	
	private final MetadataService metadataService;
	
	@GetMapping("/middle-item")
	@Operation(summary = "load middle category item", description = "load middle category item")
	public List<MiddleItemDto> getMiddleItem(@RequestParam(required = false) String largeItemCode) {
		return metadataService.getMiddleItem(largeItemCode);
	}
	
	@GetMapping("/large-item")
	@Operation(summary = "load large item", description = "load large item")
	public List<LargeItemDto> getLargeItem() {
		return metadataService.getLargeItem();
	}
}
