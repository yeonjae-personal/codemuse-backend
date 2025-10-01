package com.lgcns.svcp.prod.online.prod.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.online.prod.dto.group.OfferGroupAndOfferDto;
import com.lgcns.svcp.prod.online.prod.dto.group.OfferGroupMDto;
import com.lgcns.svcp.prod.online.prod.dto.group.OfferGroupWithOfferDto;
import com.lgcns.svcp.prod.online.prod.service.GroupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("/group")
@Tag(name = "group-controller ", description = "그룹 컨트롤러")
public class GroupController {
	@Autowired
	private GroupService groupService;
	
	@GetMapping(value = "/offer-groups")
	@Operation(summary = "빌링요소 전체 조회 API", description = "빌링요소 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveOfferGroupList() {
		List<?> response = groupService.retrieveOfferGroupList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/offer-groups/{offergroupcode}")
	@Operation(summary = "오퍼그룹 단건 조회 API", description = "오퍼그룹코드로 오퍼그룹정보와 연결된 오퍼 조회")
	public ResponseEntity<OfferGroupAndOfferDto> retrieveOfferGroupAndOffer(@Parameter(description ="오퍼그룹코드", required = true, example = "GROG000010")
	@PathVariable("offergroupcode") String offerGroupCode) {
		OfferGroupAndOfferDto response = groupService.retrieveOfferGroupAndOffer(offerGroupCode);
		return ResponseEntity.ok(response);
	}
	

//	@GetMapping(value = "offer-groups")
	@Operation(summary = "오퍼그룹 조회 리스트 API", description = "오퍼그룹 기본정보 전체 리스트 조회")
	public ResponseEntity<List<OfferGroupMDto>> retrieveOfferGroupMList() {
		List<OfferGroupMDto> response = groupService.retrieveOfferGroupMList();
		return ResponseEntity.ok(response);
	}

//	@GetMapping(value = "offer-groups/offer")
	@Operation(summary = "오퍼그룹 연결정보 상세 리스트 API", description = "오퍼그룹-오퍼 연결정보 조회, 오퍼그룹UUID 또는 오퍼UUID로 검색 가능")
	public ResponseEntity<List<OfferGroupWithOfferDto>> retrieveOfferGroupWithOfferList(
			@RequestParam(name = "오퍼 UUID", required = false, defaultValue = "0bdb1abd-6c66-4f79-ad72-dacbb2cb7a92") String offerUuid,
			@RequestParam(name = "오퍼그룹 UUID", required = false, defaultValue = "18f86142-c6a1-489d-9372-1c627400e330") String offerGroupUuid) {
		OfferGroupWithOfferDto request = new OfferGroupWithOfferDto();
		request.setOfferGroupUuid(offerGroupUuid);
		request.setOfferUuid(offerUuid);
		List<OfferGroupWithOfferDto> response = groupService.retrieveOfferGroupWithOfferList(request);
		return ResponseEntity.ok(response);
	}
}
	
