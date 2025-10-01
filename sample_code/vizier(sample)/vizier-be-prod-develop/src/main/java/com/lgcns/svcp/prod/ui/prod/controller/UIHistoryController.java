package com.lgcns.svcp.prod.ui.prod.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.history.detail.HistoryDetailResDto;
import com.lgcns.svcp.prod.ui.prod.service.UIHistoryService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Hidden
@RestController
@RequestMapping("/ui/history")
@Tag(name = "UI-history-controller", description = "Item History 컨트롤러")
@RequiredArgsConstructor
public class UIHistoryController {
	private final UIHistoryService uiHistoryService;

	@GetMapping("/detail")
	@Operation(summary = "Item History 상세 정보 조회", description = "Item History 상세 정보 조회")
	public HistoryDetailResDto retrieveHistoryDetail(@RequestParam String objUuid) {
		return uiHistoryService.retrieveHistoryDetail(objUuid);
	}
}
