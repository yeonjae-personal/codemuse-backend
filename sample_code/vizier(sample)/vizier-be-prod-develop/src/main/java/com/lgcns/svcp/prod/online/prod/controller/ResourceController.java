package com.lgcns.svcp.prod.online.prod.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.online.prod.service.ResourceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("/resource")
@Tag(name = "resource-controller ", description = "리소스 컨트롤러")
public class ResourceController {
	@Autowired
	private ResourceService resourceService;

	@GetMapping(value = "/billing-elements")
	@Operation(summary = "빌링요소 전체 조회 API", description = "빌링요소 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveBillingElementList() {
		List<?> response = resourceService.retrieveBillingElementList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/billing-elements/{blngrsccode}")
	@Operation(summary = "빌링요소 단건 조회 API", description = "빌링요소코드로 빌링요소 상세정보 조회")
	public ResponseEntity<List<?>> retrieveBillingElement(@Parameter(description ="빌링요소코드", required = true, example = "RSBE000030")
	@PathVariable("blngrsccode") String blngRscCode) {
		List<?> response = resourceService.retrieveBillingElement(blngRscCode);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/rating-elements")
	@Operation(summary = "과금요소 전체 조회 API", description = "과금요소 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveRatingElementList() {
		List<?> response = resourceService.retrieveRatingElementList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/rating-elements/{rtngrsccode}")
	@Operation(summary = "과금요소 단건 조회 API", description = "과금요소코드로 과금요소 상세정보 조회")
	public ResponseEntity<List<?>> retrieveRatingElement(@Parameter(description ="과금요소코드", required = true, example = "RSRE000363")
	@PathVariable("rtngrsccode") String rtngRscCode) {
		List<?> response = resourceService.retrieveRatingElement(rtngRscCode);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/service-elements")
	@Operation(summary = "서비스요소 전체 조회 API", description = "서비스요소 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveServiceElementList() {
		List<?> response = resourceService.retrieveServiceElementList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/service-elements/{svcrsccode}")
	@Operation(summary = "서비스요소 단건 조회 API", description = "서비스요소코드로 서비스요소 상세정보 조회")
	public ResponseEntity<List<?>> retrieveServiceElement(@Parameter(description ="서비스요소코드", required = true, example = "RSSE000037")
	@PathVariable("svcrsccode") String svcRscCode) {
		List<?> response = resourceService.retrieveServiceElement(svcRscCode);
		return ResponseEntity.ok(response);
	}

}