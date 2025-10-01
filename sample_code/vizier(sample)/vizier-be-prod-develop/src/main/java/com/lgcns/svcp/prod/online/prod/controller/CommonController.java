package com.lgcns.svcp.prod.online.prod.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.online.prod.dto.ProdItemMapgMDto;
import com.lgcns.svcp.prod.online.prod.dto.offer.PricePlanDto;
import com.lgcns.svcp.prod.online.prod.service.CommonService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Hidden
@CrossOrigin
@RestController
@RequestMapping("/common")
@Tag(name = "common-controller ", description = "공용 컨트롤러")
public class CommonController {
	@Autowired
	private CommonService commonService;
//	@GetMapping(value = "/mapping/uuidcodes")
	@Operation(summary = "UUID - 코드 매핑 리스트 조회 API", description = "데이터에 UUID정보만 있어 코드를 알아야할 때 사용")
	public ResponseEntity<List<ProdItemMapgMDto>> retrieveProdItemMapgMList(@ModelAttribute ProdItemMapgMDto prodItemMapgMDto) {
		ProdItemMapgMDto request = new ProdItemMapgMDto();
		request.setProdItemCd(prodItemMapgMDto.getProdItemCd());
		List<ProdItemMapgMDto> response = commonService.retrieveProdItemMapgMList(request);
		return ResponseEntity.ok(response);
	}
	//uuid로 단건 조회는 보류
//	@GetMapping(value = "/mappings/{prodUuid}")
//	@Operation(summary = "", description = "")
//	public ResponseEntity<ProdItemMapgMDto> retrieveProdItemMapgM(@Parameter(description ="", required = true, example = "") @PathVariable("prodUuid") String prodUuid) {
//		ProdItemMapgMDto request = new ProdItemMapgMDto();
//		request.setProdUuid(prodUuid);
//		ProdItemMapgMDto response = CommonService.retrieveProdItemMapgM(request);
//		return ResponseEntity.ok(response);
//	}
	
//	@GetMapping(value = "")
//	@GetMapping(value = "/mapping/uuidcodes")
//	public ResponseEntity<OfferInfoDto> retrieveOfferInfo(@RequestParam String offerCd) {
//		PricePlanDto request = new PricePlanDto();
//		request.setPpCode(offerCd);
//		
//		OfferInfoDto response = commonService.retrieveOfferInfo(request);
//		
//		return ResponseEntity.ok(response);
//	}
}
