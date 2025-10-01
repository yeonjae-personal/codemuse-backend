package com.lgcns.svcp.prod.online.prod.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.online.prod.dto.component.benefit.AllowanceAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.benefit.RatingDiscountAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.characteristic.SalesDto;
import com.lgcns.svcp.prod.online.prod.dto.component.price.BaseFeeAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.price.UsageFeeAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.service.AdditionalAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.service.MessageAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.service.VoiceAndResourceDto;
import com.lgcns.svcp.prod.online.prod.service.ComponentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/component")
@Tag(name = "component-controller", description = "컴포넌트 컨트롤러")
public class ComponentController {
	@Autowired
	private ComponentService componentService;
	
	@GetMapping(value = "/price/basefees")
	@Operation(summary = "기본료 전체 조회 API", description = "기본료 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveBaseFeeList() {
		List<?> response = componentService.retrieveBaseFeeList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/price/basefees/{bsfcode}")
	@Operation(summary = "기본료 단건조회 API", description = "기본료코드로 기본료 상세정보, 연결된 리소스 조회")
	public ResponseEntity<BaseFeeAndResourceDto> retrieveBaseFeeAndResource(@Parameter(description ="기본료코드", required = true, example = "PRRC000027")
																			@PathVariable("bsfcode") String bsfCode) {
		BaseFeeAndResourceDto response = componentService.retrieveBaseFeeAndResource(bsfCode);
		return ResponseEntity.ok(response);
	}
	
	//사용료
	@GetMapping(value = "/price/usagefees")
	@Operation(summary = "사용료 전체 조회 API", description = "사용료 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveUsageFeeList() {
		List<?> response = componentService.retrieveUsageFeeList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/price/usagefees/{usfcode}")
	@Operation(summary = "사용료 단건조회 API", description = "사용료코드로 사용료 상세정보, 연결된 리소스 조회")
	public ResponseEntity<UsageFeeAndResourceDto> retrieveUsageFeeAndResource(@Parameter(description ="사용료코드", required = true, example = "PRUC000007")
																			@PathVariable("usfcode") String usfCode) {
		UsageFeeAndResourceDto response = componentService.retrieveUsageFeeAndResource(usfCode);
		return ResponseEntity.ok(response);
	}
	//할인요율
	@GetMapping(value = "/price/discount-rates")
	@Operation(summary = "할인요율 전체 조회 API", description = "할인요율 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveDiscountRateList() {
		List<?> response = componentService.retrieveDiscountRateList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/price/discount-rates/{dcratecode}")
	@Operation(summary = "할인요율 단건조회 API", description = "할인요율코드로 할인요율 상세정보 조회")
	public ResponseEntity<List<?>> retrieveDiscountRate(@Parameter(description ="할인요율코드", required = true, example = "PRDR000001")
																			@PathVariable("dcratecode") String dcRateCode) {
		List<?> response = componentService.retrieveDiscountRate(dcRateCode);
		return ResponseEntity.ok(response);
	}
	//공제
	@GetMapping(value = "/benefit/allowances")
	@Operation(summary = "공제 전체 조회 API", description = "공제 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveAllowanceList() {
		List<?> response = componentService.retrieveAllowanceList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/benefit/allowances/{alwnccode}")
	@Operation(summary = "공제 단건조회 API", description = "공제코드로 공제 상세정보, 연결된 리소스 조회")
	public ResponseEntity<AllowanceAndResourceDto> retrieveAllowance(@Parameter(description ="공제코드", required = true, example = "BNAW000130")
																			@PathVariable("alwnccode") String alwncCode) {
		AllowanceAndResourceDto response = componentService.retrieveAllowanceAndResource(alwncCode);
		return ResponseEntity.ok(response);
	}
	//과금할인
	@GetMapping(value = "/benefit/rating-discounts")
	@Operation(summary = "과금할인 전체 조회 API", description = "과금할인 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveRatingDiscountList() {
		List<?> response = componentService.retrieveRatingDiscountList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/benefit/rating-discounts/{rtngdccode}")
	@Operation(summary = "과금할인 단건조회 API", description = "과금할인코드로 과금할인 상세정보, 연결된 리소스 조회")
	public ResponseEntity<RatingDiscountAndResourceDto> retrieveRatingDiscount(@Parameter(description ="과금할인코드", required = true, example = "BNRD000014")
																			@PathVariable("rtngdccode") String rtngDcCode) {
		RatingDiscountAndResourceDto response = componentService.retrieveRatingDiscountAndResource(rtngDcCode);
		return ResponseEntity.ok(response);
	}
	
	//빌링
	@GetMapping(value = "/characteristic/billing")
	@Operation(summary = "빌링 전체 조회 API", description = "빌링 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveBillingList() {
		List<?> response = componentService.retrieveBillingList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/characteristic/billing/{blngcode}")
	@Operation(summary = "빌링 단건조회 API", description = "빌링코드로 빌링 상세정보 조회")
	public ResponseEntity<List<?>> retrieveBilling(@Parameter(description ="빌링코드", required = true, example = "CHBI000066")
																			@PathVariable("blngcode") String blngCode) {
		List<?> response = componentService.retrieveBilling(blngCode);
		return ResponseEntity.ok(response);
	}
	//매출
	@GetMapping(value = "/characteristic/sales")
	@Operation(summary = "매출 전체 조회 API", description = "매출 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveSalesList() {
		List<?> response = componentService.retrieveSalesList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/characteristic/sales/{salescode}")
	@Operation(summary = "매출 단건조회 API", description = "매출코드로 매출 상세정보 조회")
	public ResponseEntity<List<?>> retrieveSales(@Parameter(description ="매출코드", required = true, example = "CHSI000038")
																			@PathVariable("salescode") String salesCode) {
		List<?> response = componentService.retrieveSales(salesCode);
		return ResponseEntity.ok(response);
	}
	//할인구성
	@GetMapping(value = "/characteristic/discount-config")
	@Operation(summary = "할인구성 전체 조회 API", description = "할인구성 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveDiscountInfoList() {
		List<?> response = componentService.retrieveDiscountConfigurationList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/characteristic/discount-config/{dccfgrtcode}")
	@Operation(summary = "할인구성 단건조회 API", description = "할인구성코드로 할인구성 상세정보 조회")
	public ResponseEntity<List<?>> retrieveDisocuntInfo(@Parameter(description ="할인구성코드", required = true, example = "CHDI000005")
																			@PathVariable("dccfgrtcode") String dcCfgrtCode) {
		List<?> response = componentService.retrieveDiscountConfiguration(dcCfgrtCode);
		return ResponseEntity.ok(response);
	}
	
	//SPAM, additional이 없는데 일단 만든다?
	
	@GetMapping(value = "/service/additionals")
	@Operation(summary = "기타 서비스 전체 조회 API", description = "기타 서비스 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveAdditionalList() {
		List<?> response = componentService.retrieveAdditionalList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/service/additionals/{addsvccode}")
	@Operation(summary = "기타 서비스 단건조회 API", description = "서비스코드로 기타 서비스 상세정보, 연결된 리소스 조회")
	public ResponseEntity<AdditionalAndResourceDto> retrieveAdditional(@Parameter(description ="서비스코드", required = true, example = "SRAD000007")
																			@PathVariable("addsvccode") String addSvcCode) {
		AdditionalAndResourceDto response = componentService.retrieveAdditionalAndResource(addSvcCode);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/service/messages")
	@Operation(summary = "문자 서비스 전체 조회 API", description = "문자 서비스 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveMessageList() {
		List<?> response = componentService.retrieveMessageList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/service/messages/{msgsvccode}")
	@Operation(summary = "문자 서비스 단건조회 API", description = "서비스코드로 문자 서비스 상세정보, 연결된 리소스 조회")
	public ResponseEntity<MessageAndResourceDto> retrieveMessage(@Parameter(description ="서비스코드", required = true, example = "SRMS000067")
																			@PathVariable("msgsvccode") String msgSvcCode) {
		MessageAndResourceDto response = componentService.retrieveMessageAndResource(msgSvcCode);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/service/voices")
	@Operation(summary = "음성 서비스 전체 조회 API", description = "음성 서비스 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveVoiceList() {
		List<?> response = componentService.retrieveVoiceList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/service/voices/{vicsvccode}")
	@Operation(summary = "음성 서비스 단건조회 API", description = "서비스코드로 음성 서비스 상세정보, 연결된 리소스 조회")
	public ResponseEntity<VoiceAndResourceDto> retrieveVoice(@Parameter(description ="서비스코드", required = true, example = "SRVO000098")
																			@PathVariable("vicsvccode") String vicSvcCode) {
		VoiceAndResourceDto response = componentService.retrieveVoiceAndResource(vicSvcCode);
		return ResponseEntity.ok(response);
	}

}
