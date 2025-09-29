package com.lgcns.svcp.prod.online.prod.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.online.prod.dto.ProdCstcRelDDto;
import com.lgcns.svcp.prod.online.prod.dto.ProdDpndRelDDto;
import com.lgcns.svcp.prod.online.prod.service.RelationService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Hidden
@RestController
@RequestMapping("/relation")
@Tag(name = "relation-controller", description = "관계정보 컨트롤러")
public class RelationController {
	@Autowired
	private RelationService relationService;
	
	@GetMapping(value = "/offer-relations")
	@Operation(summary = "종속관계 전체 조회 API", description = "상품종속관계 상세정보 전체 리스트 조회")
	public ResponseEntity<List<ProdDpndRelDDto>> retrieveAllProdDpndRelDList(@ModelAttribute ProdDpndRelDDto prodDpndRelDDto) {
		ProdDpndRelDDto request = new ProdDpndRelDDto();
		request.setBaseProdItemCd(prodDpndRelDDto.getBaseProdItemCd());
		request.setTrgtProdItemCd(prodDpndRelDDto.getTrgtProdItemCd());
		request.setDpndRelDivsCd(prodDpndRelDDto.getDpndRelDivsCd());
		List<ProdDpndRelDDto> response = relationService.retrieveAllProdDpndRelDList(request);

		return ResponseEntity.ok(response);	
	}
	
//	@GetMapping(value = "/offercostruct-relations")
//	@Operation(summary = "구성관계 전체 조회 API", description = "상품구성관계 상세정보 전체 리스트 조회")
//	public ResponseEntity<List<ProdCstcRelDDto>> retrieveAllProdCstcRelDList(@ModelAttribute ProdCstcRelDDto prodCstcRelDDto) {
//		ProdCstcRelDDto request = new ProdCstcRelDDto();
//		request.setBaseProdItemCd(prodCstcRelDDto.getBaseProdItemCd());
//		request.setTrgtProdItemCd(prodCstcRelDDto.getTrgtProdItemCd());
//		List<ProdCstcRelDDto> response = relationService.retrieveAllProdCstcRelDList(request);
//
//		return ResponseEntity.ok(response);	
//	}
//	
//	@GetMapping(value = "/rateresource-relations")
//	@Operation(summary = "요율-서비스요소 전체 조회 API", description = "요율-서비스요소 전체 리스트 조회")
//	public ResponseEntity<List<RatSvcFctrRelDDto>> retrieveAllRatSvcFctrRelDList(@Parameter(required = false) @ModelAttribute RatSvcFctrRelDDto ratSvcFctrRelDDto) {
//		RatSvcFctrRelDDto request = new RatSvcFctrRelDDto();
//		request.setRatCd(ratSvcFctrRelDDto.getRatCd());
//		request.setSvcFctrCd(ratSvcFctrRelDDto.getSvcFctrCd());
//		List<RatSvcFctrRelDDto> response = relationService.retrieveAllRatSvcFctrRelDList(request);
//
//		return ResponseEntity.ok(response);	
//	}
}
