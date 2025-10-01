package com.lgcns.svcp.prod.ui.prod.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.category.CategoryDescriptionDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.CategoryPathDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.CategoryTreeDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.CatgMDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.RequestOfferWithCatgUuidDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.detail.OffersOfCatgegoryReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.tab.CategoryTabDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.update.CategoryTreeUpdateRequestDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.OfferOfLeafNodeDto;
import com.lgcns.svcp.prod.ui.prod.service.UiCategoryService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/ui/category")
@Tag(name = "UI-category-controller", description = "화면 카테고리 컨트롤러")
@RequiredArgsConstructor
public class UiCategoryController {

	private final UiCategoryService uiCategoryService;

	@GetMapping(value = "/products")
	@Operation(summary = "(화면) 리프노드 카테고리 상품 조회", description = "리프노드 카테고리에 연결된 상품정보 리스트 조회")
	public List<OfferOfLeafNodeDto> retrieveProdMList(@RequestParam String ctgrNodeUuid,
			@RequestParam String ctgrTabUuid) {

		CatgMDto request = new CatgMDto();
		request.setCtgrNodeUuid(ctgrNodeUuid);
		request.setCtgrTabUuid(ctgrTabUuid);
		return uiCategoryService.retrieveProdMList(request);
	}

	@GetMapping(value = "/tree")
	@Operation(summary = "(화면) 카테고리 트리 전체 조회 API", description = "카테고리 트리와 카테고리에 포함된 상품 개수 조회 API")
	public List<CategoryTreeDto> retrieveCategoryTreeList(@RequestParam String ctgrTabUuid) {
		return uiCategoryService.retrieveCategoryTreeWithOfferCounts(ctgrTabUuid);
	}

	@GetMapping(value = "/offers")
	@Operation(summary = "(화면) 카테고리 Path 전체 조회 API", description = "카테고리 리프노트 전체패스와 연결된 상품정보 조회 API")
	public PageResult<?> retrieveCategoryPathList(
			@RequestParam(value = "page", defaultValue = "1", required = true) int page,
			@RequestParam(value = "size", defaultValue = "10", required = true) int size,
			@RequestParam String ctgrTabUuid, @RequestParam(required = false) String ctgrNodeName,
			@RequestParam(required = false) String offerCd, @RequestParam(required = false) String offerNm) {
		CategoryPathDto request = new CategoryPathDto();
		request.setPage(page);
		request.setSize(size);
		request.setOfferCd(offerCd);
		request.setOfferNm(offerNm);
		request.setCtgrNodeName(ctgrNodeName);
		request.setCtgrTabUuid(ctgrTabUuid);

		return uiCategoryService.retrieveCategoryPathWithOffer(request);
	}

	@GetMapping("/offers/export")
	@Operation(summary = "api path export excel", description = "Product information export API linked to category leaf note full pass")
	public void exportExcelCategoryPathList(@RequestParam String ctgrTabUuid,
			@RequestParam(required = false) String ctgrNodeName, @RequestParam(required = false) String offerCd,
			@RequestParam(required = false) String offerNm, HttpServletResponse response) {

		CategoryPathDto request = new CategoryPathDto();
		request.setOfferCd(offerCd);
		request.setOfferNm(offerNm);
		request.setCtgrNodeName(ctgrNodeName);
		request.setCtgrTabUuid(ctgrTabUuid);
		uiCategoryService.exportExcel(request, response);
	}

	@GetMapping(value = "/description")
	@Operation(summary = "(화면) 오퍼별 카테고리 레벨에 대한 설명", description = "오퍼별 카테고리 레벨에 대한 설명")
	public CategoryDescriptionDto retrieveCategoryDescription(@RequestParam String ctgrTabUuid) {
		return uiCategoryService.retrieveCategoryDescription(ctgrTabUuid);
	}

	@GetMapping(value = "tree/offers/search")
	@Operation(summary = "(화면) 오퍼 조회", description = "오퍼 조회")
	public PageResult<?> retrieveOffersWtihCatgegory(
			@RequestParam(value = "page", defaultValue = "1", required = true) int page,
			@RequestParam(value = "size", defaultValue = "7", required = true) int size,
			@RequestParam String ctgrTabUuid, @RequestParam(required = false) String offerNm,
			@RequestParam(required = false) String offerCd) {
		RequestOfferWithCatgUuidDto request = new RequestOfferWithCatgUuidDto();
		request.setPage(page);
		request.setSize(size);
		request.setCtgrTabUuid(ctgrTabUuid);
		request.setOfferNm(offerNm);
		request.setOfferCd(offerCd);

		return uiCategoryService.retrieveOffersWithCategoryList(request);
	}

	@GetMapping(value = "detail/offers")
	@Operation(summary = "노드 상세의 오퍼 목록 조회", description = "노드 상세의 오퍼 목록 조회")
	public PageResult<?> retrieveOffersOfCatgegory(@RequestParam String ctgrNodeUuid,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "6") int size) {
		OffersOfCatgegoryReqDto req = new OffersOfCatgegoryReqDto();
		req.setCtgrNodeUuid(ctgrNodeUuid);
		req.setPage(page);
		req.setSize(size);

		return uiCategoryService.retrieveOffersOfCatgegory(req);
	}

	@GetMapping(value = "tabs")
	@Operation(summary = "카테고리 탭 조회", description = "카테고리 탭 조회")
	public List<CategoryTabDto> retrieveCatgegoryTabs() {
		return uiCategoryService.retrieveCatgegoryTabs();
	}

	@PostMapping(value = "/tree")
	@Operation(summary = "카테고리 수정", description = "카테고리 수정")
	public void ụpdateCategoryTree(@RequestBody CategoryTreeUpdateRequestDto treeUpdateDto) {
		uiCategoryService.ụpdateCategoryTree(treeUpdateDto);
	}

	@GetMapping(value = "/path-info")
	@Operation(summary = "카테고리 Path 정보 조회", description = "카테고리 Path 정보 조회")
	public List<String> retrievePathInfo(@RequestParam String ctgrNodeUuid) {
		return uiCategoryService.retrievePathInfo(ctgrNodeUuid);
	}
}
