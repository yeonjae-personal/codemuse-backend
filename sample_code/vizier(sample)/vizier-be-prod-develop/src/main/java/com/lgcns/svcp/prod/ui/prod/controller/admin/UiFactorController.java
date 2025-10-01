package com.lgcns.svcp.prod.ui.prod.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.admin.factor.FactorTypeDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.factor.SearchFactorReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.factor.SearchFactorTypeReqDto;
import com.lgcns.svcp.prod.ui.prod.service.admin.UiFactorService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ui/factor-management")
@Tag(name = "UI-factor-controller", description = "화면 Factor 컨트롤러")
public class UiFactorController {

	private final UiFactorService uiFactorService;

	@GetMapping("/search-factor-type")
	@Operation(summary = "Factor-Type 조회", description = "Factor-Type 조회")
	public PageResult<?> searchFactorType(@RequestParam(required = false) String factorTypeCode,
			@RequestParam(required = false) String factorTypeName,
			@RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size) {

		SearchFactorTypeReqDto reqDto = new SearchFactorTypeReqDto();
		reqDto.setSize(size);
		reqDto.setPage(page);
		reqDto.setFactorTypeCode(factorTypeCode);
		reqDto.setFactorTypeName(factorTypeName);

		return uiFactorService.searchFactorType(reqDto);
	}

	@GetMapping("/search-factor-info")
	@Operation(summary = "Factor Type로 Factor 검색", description = "Factor Type로 Factor 검색")
	public List<FactorTypeDto> searchFactorInfo(@RequestParam(defaultValue = "Y", required = false) String useYn) {
		SearchFactorTypeReqDto reqDto = new SearchFactorTypeReqDto();
		reqDto.setUseYn(useYn);
		return uiFactorService.searchFactorInfo(reqDto);
	}

	@GetMapping("/search-factor")
	@Operation(summary = "Factor 조회", description = "Factor 조회")
	public PageResult<?> searchFactor(@RequestParam(required = false) String factorTypeCode,
			@RequestParam(required = false) String factorTypeName, @RequestParam(required = false) String factorCode,
			@RequestParam(required = false) String factorName, @RequestParam(required = false) String useYn,
			@RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size) {

		SearchFactorReqDto reqDto = new SearchFactorReqDto();
		reqDto.setSize(size);
		reqDto.setPage(page);

		reqDto.setFactorTypeCode(factorTypeCode);
		reqDto.setFactorTypeName(factorTypeName);
		reqDto.setFactorCode(factorCode);
		reqDto.setFactorName(factorName);
		reqDto.setUseYn(useYn);
		return uiFactorService.searchFactor(reqDto);
	}

	@GetMapping("/factor-type")
	@Operation(summary = "Factor-Type 상세 조회", description = "Factor-Type 상세 조회")
	public FactorTypeDto retrieveFactorType(@RequestParam String factorTypeCode,
			@RequestParam(required = false) String factorCode, @RequestParam(required = false) String factorName,
			@RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size) {

		SearchFactorReqDto reqDto = new SearchFactorReqDto();
		reqDto.setSize(size);
		reqDto.setPage(page);
		reqDto.setFactorTypeCode(factorTypeCode);
		reqDto.setFactorCode(factorCode);
		reqDto.setFactorName(factorName);

		return uiFactorService.retrieveFactorType(reqDto);
	}

	@PutMapping("/factor-type")
	@Operation(summary = "Factor-Type 수정", description = "Factor-Type 수정")
	public void updateFactorType(@RequestBody FactorTypeDto factorTypeDto) {
		uiFactorService.updateFactorType(factorTypeDto);
	}
}
