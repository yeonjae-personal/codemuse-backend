package com.lgcns.svcp.prod.ui.prod.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.ProdStruDWithItemsMDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.search.SearchAdvancedReq;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.OfferCreateInfo;
import com.lgcns.svcp.prod.ui.prod.dto.offer.list.OfferSearchReq;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.OfferStructureDetailReq;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.OfferStructureDetailRes;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.listAddComponent.ComponentSearch;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.update.OfferStructureUpdateReq;
import com.lgcns.svcp.prod.ui.prod.service.UiOfferService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ui")
@Tag(name = "UI-offer-controller", description = "화면 오퍼 컨트롤러")
public class UIOfferController {

	private final UiOfferService uiOfferService;

	@GetMapping(value = "/structure")
	@Operation(summary = "(화면) 상품구조 base 또는 trgt으로 조회 API", description = "상품구조 리스트 조회")
	public List<ProdStruDWithItemsMDto> retrieveProdStruDWithItemsMByBaseAndTrgt(
			@RequestParam(required = false) String baseUuid, @RequestParam(required = false) String trgtUuid) {

		ProdStruDWithItemsMDto request = new ProdStruDWithItemsMDto();
		request.setBaseUuid(baseUuid);
		request.setTrgtUuid(trgtUuid);
		return uiOfferService.retrieveProdStruDWithItemsMListWithPartiotion(request);
	}

	@GetMapping(value = "/offers")
	@Operation(summary = "(화면) 요금제, 부가상품 전체 조회 API", description = "요금제와 부가상품 상세정보 전체 리스트 조회")
	public PageResult<?> getOffers(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(required = false) String itemCode, @RequestParam(required = false) boolean onlyValidDtm,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code) {
		OfferSearchReq req = new OfferSearchReq(code, name, itemCode, onlyValidDtm, page, size);
		return uiOfferService.getOffers(req);
	}

	@GetMapping("/offer/export")
	public void downloadExcel(@ModelAttribute SearchAdvancedReq searchAdvancedReq, HttpServletResponse response) {
		uiOfferService.exportToExcel(searchAdvancedReq, response);
	}

	@GetMapping("offer/create-info")
	@Operation(summary = "Get full offer creation info")
	public OfferCreateInfo getOfferCreateInfo(@RequestParam String itemCode,
			@RequestParam(required = false, defaultValue = "en") String language) {
		return uiOfferService.getOfferCreateInfo(itemCode, language);
	}

	@PostMapping("offer")
	@Operation(summary = "Create Offer")
	public Map<String, String> createOffer(@RequestBody ItemMappingDetailDto req) {
		return uiOfferService.createOffer(req);
	}

	@PutMapping("offer")
	@Operation(summary = "Edit Offer")
	public void editOffer(@RequestBody ItemMappingDetailDto req) {
		uiOfferService.editOffer(req);
	}

	@GetMapping("offer/detail")
	@Operation(summary = "Get offer detail")
	public ItemMappingDetailDto getOfferDetail(@RequestParam String objUuid, HttpServletRequest request) {
		String language = request.getHeader("X-Language");
		return uiOfferService.getOfferDetail(objUuid, language);
	}

	@GetMapping("offer/structure")
	@Operation(summary = "Get offer structure")
	public List<OfferStructureDetailRes> getOfferStructure(@RequestParam String objUuid,
			@RequestParam(required = false) boolean onlyValidDtm) {
		OfferStructureDetailReq req = new OfferStructureDetailReq();
		req.setObjUuid(objUuid);
		req.setOnlyValidDtm(onlyValidDtm);

		return uiOfferService.getOfferStructure(req);
	}

	@PutMapping("offer/structure")
	@Operation(summary = "Edit offer structure")
	public void editOfferStructure(@RequestBody OfferStructureUpdateReq req) throws ParseException {
		uiOfferService.updateOfferStructure(req);
	}

	@GetMapping("offer/structure/components")
	@Operation(summary = "Get list-add-component for offer structure")
	public PageResult<?> getStructureComponentListAdd(@RequestParam(required = false) String offerUUID,
			@RequestParam(required = false) String offerItemCode, @RequestParam(required = false) String componentType,
			@RequestParam(required = false) String itemCode, @RequestParam(required = false) String componentCode,
			@RequestParam(required = false) String componentName, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {

		ComponentSearch search = new ComponentSearch(offerUUID, offerItemCode, componentType, itemCode, componentCode,
				componentName, page, size);
		return uiOfferService.getStructureComponentListAdd(search);
	}

	@GetMapping(value = "/offers/advanced")
	@Operation(summary = "오퍼 상세 검색", description = "오퍼 상세 검색")
	public PageResult<?> retrieveOffersAdvanced(@ModelAttribute SearchAdvancedReq searchAdvancedReq) {
		return uiOfferService.retrieveOffersAdvanced(searchAdvancedReq);
	}

	@GetMapping(value = "/offers/advanced-detail")
	@Operation(summary = "오퍼 리스트뷰 조회", description = "오퍼 리스트뷰 조회")
	public PageResult<?> retrieveOffersAdvancedWithDetail(@ModelAttribute SearchAdvancedReq searchAdvancedReq) {
		return uiOfferService.retrieveOffersAdvancedWithDetail(searchAdvancedReq);
	}
}
