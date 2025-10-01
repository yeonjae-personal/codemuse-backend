package com.lgcns.svcp.prod.ui.prod.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.common.structure.ItemStructureDto;
import com.lgcns.svcp.prod.ui.prod.service.UiCommonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/ui")
@Tag(name = "UI-controller", description = "화면 컨트롤러")
public class UIController {

	private final UiCommonService uiCommonService;

	@GetMapping(value = "/common/item-structure")
	@Operation(summary = "아이템 구조 조회", description = "아이템 구조 조회")
	public List<ItemStructureDto> retreiveItemStructure(@RequestParam(required = false) String itemCode,
			@RequestParam(required = false) String mctgrItemCode) {
		return uiCommonService.retreiveItemStructure(itemCode, mctgrItemCode);
	}
}
