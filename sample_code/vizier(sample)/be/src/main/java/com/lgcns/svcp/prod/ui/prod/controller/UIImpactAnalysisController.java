package com.lgcns.svcp.prod.ui.prod.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.analysis.ImpactAnalysisResponseDto;
import com.lgcns.svcp.prod.ui.prod.dto.analysis.ItemDto;
import com.lgcns.svcp.prod.ui.prod.dto.analysis.ProductStructureDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.SelectOptionDto;
import com.lgcns.svcp.prod.ui.prod.service.UiImpactAnalysisService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/ui/impact-analysis")
@Tag(name = "UI-impact-analysis-controller", description = "화면 영향도 분석 컨트롤러")
public class UIImpactAnalysisController {
	@Autowired
	private UiImpactAnalysisService uiImpactAnalysisService;

	@GetMapping(value = "/relation")
	@Operation(summary = "(화면) 부모,형제상품 조회 API", description = "부모,형제상품 uuid, 코드, 명칭 조회")
	public ImpactAnalysisResponseDto getImpactAnalysisResponseDto(@RequestParam String prodUuid) {
		ItemDto request = new ItemDto();
		request.setProdUuid(prodUuid);
		return uiImpactAnalysisService.getImpactAnalysisResponseDto(request);
	}

	@GetMapping(value = "/children")
	@Operation(summary = "(화면) 자식상품 조회 API", description = "자식상품 uuid, 코드, 명칭 조회")
	public List<ItemDto> getSiblings(@RequestParam String prodUuid) {
		ItemDto request = new ItemDto();
		request.setProdUuid(prodUuid);
		return uiImpactAnalysisService.getChildrenProdM(request);
	}

	@GetMapping(value = "/items")
	@Operation(summary = "(화면) 오퍼,컴포넌트,리소스 조회 API", description = "오퍼, 컴포넌트, 리소스 조회")
	public PageResult<?> getProdItems(@RequestParam(value = "page", defaultValue = "1", required = true) int page,
			@RequestParam(value = "size", defaultValue = "10", required = true) int size,
			@RequestParam(required = false) String prodItemNm, @RequestParam(required = false) String prodItemCd,
			@RequestParam(required = false) String type, @RequestParam(required = false) String detlType,
			@RequestParam(required = false) String subType, @RequestParam(required = false) boolean onlyValidDtm) {
		ItemDto request = new ItemDto();

		request.setType(type);
		request.setDetlType(detlType);
		request.setSubType(subType);
		request.setSize(size);
		request.setPage(page);
		request.setProdItemNm(prodItemNm);
		request.setProdItemCd(prodItemCd);
		request.setOnlyValidDtm(onlyValidDtm);

		return uiImpactAnalysisService.retrieveItemsPagedList(request);
	}

	@GetMapping(value = "/relation/listview")
	@Operation(summary = "(화면) 상품구조 리스트뷰 조회 API", description = "상품구조 리스트뷰 조회")
	public PageResult<?> retrieveProductStructureListView(
			@RequestParam(value = "page", defaultValue = "1", required = true) int page,
			@RequestParam(value = "size", defaultValue = "10", required = true) int size,
			@RequestParam(required = false) String offerUuid,
			@RequestParam(required = false) String componentUuid,
			@RequestParam(required = false) String resourceUuid,
			@RequestParam(required = false) String objCode,
			@RequestParam(required = false) String objName,
			@RequestParam(required = false) String lctgrItemCode) {
		ProductStructureDto request = new ProductStructureDto();
		request.setOffrUuid(offerUuid);
		request.setCmpUuid(componentUuid);
		request.setSvcUuid(resourceUuid);
		request.setSize(size);
		request.setPage(page);
		request.setObjCode(objCode);
		request.setObjName(objName);
		request.setLctgrItemCode(lctgrItemCode);

		return uiImpactAnalysisService.retrieveProductStructureListView(request);
	}

	@GetMapping(value = "/relation/listview/export")
	@Operation(summary = "Impact Analysis 리스트뷰 조회결과 다운로드", description = "Impact Analysis 리스트뷰 조회결과 다운로드")
	public void downloadProductStructureListView(@RequestParam(required = false) String offerUuid,
			@RequestParam(required = false) String componentUuid, @RequestParam(required = false) String resourceUuid,
			HttpServletResponse response) {

		ProductStructureDto request = new ProductStructureDto();
		request.setOffrUuid(offerUuid);
		request.setCmpUuid(componentUuid);
		request.setSvcUuid(resourceUuid);

		uiImpactAnalysisService.downloadProductStructureListView(request, response);
	}

	@GetMapping(value = "/items/search-info")
	@Operation(summary = "Target Search 동적 데이터 조회", description = "Target Search 동적 데이터 조회")
	public List<SelectOptionDto> searchInfoItem() {
		return uiImpactAnalysisService.searchInfoItem();
	}
}
