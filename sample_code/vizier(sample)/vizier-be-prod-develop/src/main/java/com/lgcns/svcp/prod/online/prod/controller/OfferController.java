package com.lgcns.svcp.prod.online.prod.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.online.prod.dto.offer.PricePlanAndComponentDto;
import com.lgcns.svcp.prod.online.prod.dto.offer.AddOnAndComponentDto;
import com.lgcns.svcp.prod.online.prod.dto.offer.DiscountAndComponentDto;
import com.lgcns.svcp.prod.online.prod.dto.offer.PricePlanDto;
import com.lgcns.svcp.prod.online.prod.service.OfferService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("/offer")
@Tag(name = "offer-controller", description = "상품 컨트롤러")
public class OfferController {
	@Autowired
	private OfferService offerService;

//	@GetMapping(value = "/products")
//	@Operation(summary = "요금제, 부가상품 전체 조회 API", description = "요금제와 부가상품 상세정보 전체 리스트 조회")
//	public ResponseEntity<List<ProdMDto>> retrieveProdM(@RequestParam(required = false) String prodNm, @RequestParam(required = false) String prodKdCd) {
//		ProdMDto request = new ProdMDto();
//		request.setProdNm(prodNm);
//		request.setProdKdCd(prodKdCd);
//		List<ProdMDto> response = offerService.retrieveProdMList(request);
//		return ResponseEntity.ok(response);
//	}
	
	@GetMapping(value = "/priceplans")
	@Operation(summary = "요금제전체 조회 API", description = "요금제 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrievePricePlans() {
		List<?> response = offerService.retrievePricePlanList();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/addons")
	@Operation(summary = "부가상품 전체 조회 API", description = "부가상품 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveAddOns() {
		List<?> response = offerService.retrieveAddOnList();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/discounts")
	@Operation(summary = "할인상품 전체 조회 API", description = "할인 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveDiscounts() {
		List<?> response = offerService.retrieveDiscountList();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/priceplans/{ppcode}")
	@Operation(summary = "요금제 단건, 요금제 하위 조회 API", description = "요금제, 요금제 하위 컴포넌트 리스트 조회")
	public ResponseEntity<PricePlanAndComponentDto> retrievePricePlanAndComponent(@Parameter(description ="요금제 코드", example = "OFPP000067")
																				  @PathVariable("ppcode") String ppCode) {
		PricePlanAndComponentDto response = offerService.retrievePricePlanAndComponent(ppCode);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/addons/{addoncode}")
	@Operation(summary = "부가상품 단건, 부가상품 하위 조회 API", description = "부가상품, 부가상품 하위 컴포넌트 리스트 조회")
	public ResponseEntity<AddOnAndComponentDto> retrieveAddOnAndComponent(@Parameter(description ="부가상품 코드", example = "OFAO000007")
																			  @PathVariable("addoncode") String addonCode) {
		AddOnAndComponentDto response = offerService.retrieveAddOnAndComponent(addonCode);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/discounts/{dccode}")
	@Operation(summary = "할인 단건, 할인 하위 조회 API", description = "할인, 할인 하위 컴포넌트 리스트 조회")
	public ResponseEntity<DiscountAndComponentDto> retrieveDiscountAndComponent(@Parameter(description ="할인 코드", example = "OFDC000007")
																			  @PathVariable("dccode") String dcCode) {
		DiscountAndComponentDto response = offerService.retrieveDiscountAndComponent(dcCode);
		return ResponseEntity.ok(response);
	}
	
	//임시처리 06.02 , 삭제대상 25.06.26
	//@GetMapping(value = "/products/ordr")
//	@Operation(summary = "요금제, 부가상품 조회 API", description = "요금제 코드와 조건 추가")
//	public ResponseEntity<List<ProdMDto>> retrieveProdMListOrdr(@RequestParam(required = false) List<String> prodCdList,
//			@RequestParam(required = true) String prodKdCd,
//			@RequestParam(required = false) String susProdEntrPsblYn, @RequestParam(required = false) String rsvPsblYn) {
//		ProdMDto request = new ProdMDto();
//		String prodCd = String.join(",", prodCdList);
//		request.setProdCd(prodCd);
//		request.setProdKdCd(prodKdCd);
//		request.setSusProdEntrPsblYn(susProdEntrPsblYn);
//		request.setRsvPsblYn(rsvPsblYn);
//
//		List<ProdMDto> response = offerService.retrieveProdMListOrdr(request);
//		return ResponseEntity.ok(response);
//	}
}

