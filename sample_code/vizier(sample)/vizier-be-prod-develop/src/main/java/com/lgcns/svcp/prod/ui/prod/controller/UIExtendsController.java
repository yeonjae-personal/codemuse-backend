package com.lgcns.svcp.prod.ui.prod.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.extend.CountTargetResDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.OffrDpdcReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.RelationViewReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.RelationViewResDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.SaveTargetReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.TargetReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.TargetResDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.service.UIExtendsService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ui/extends/dependency")
@Tag(name = "UI-extends-controller", description = "화면 Extends 컨트롤러")
public class UIExtendsController {

	private final UIExtendsService uiExtendsService;

	@GetMapping(value = "/target")
	@Operation(summary = "리더/폴로어의 그룹 조회", description = "리더/폴로어의 그룹 조회")
	public TargetResDto getTarget(@RequestParam String offerUuid,
			@RequestParam(required = false) boolean onlyValidDtm,
			@RequestParam(required = false) boolean includeGroup) {
		TargetReqDto req = new TargetReqDto();
		req.setOfferUuid(offerUuid);
		req.setOnlyValidDtm(onlyValidDtm);
		req.setIncludeGroup(includeGroup);

		return uiExtendsService.getTarget(req);
	}

	@GetMapping(value = "/leader")
	@Operation(summary = "리더 조회", description = "리더 조회")
	public List<RelationViewResDto> getLeader(@RequestParam String targetUuid,
			@RequestParam(required = false) boolean onlyValidDtm,
			@RequestParam(required = false) boolean includeGroup) {
		RelationViewReqDto req = new RelationViewReqDto();
		req.setTargetUuid(targetUuid);
		req.setOnlyValidDtm(onlyValidDtm);
		req.setIncludeGroup(includeGroup);

		return uiExtendsService.getLeader(req);
	}

	@GetMapping(value = "/follower")
	@Operation(summary = "폴로어 조회", description = "폴로어 조회")
	public List<RelationViewResDto> getFollower(@RequestParam String targetUuid,
			@RequestParam(required = false) boolean onlyValidDtm,
			@RequestParam(required = false) boolean includeGroup) {
		RelationViewReqDto req = new RelationViewReqDto();
		req.setTargetUuid(targetUuid);
		req.setOnlyValidDtm(onlyValidDtm);
		req.setIncludeGroup(includeGroup);

		return uiExtendsService.getFollower(req);
	}
	
	@GetMapping("/relation/export")
	@Operation(summary = "export relation", description = "export relation")
	public void exportComponent(@RequestParam String uuid, @RequestParam(required = false) String category, @RequestParam(required = false) String type, 
								@RequestParam(required = false) String value, HttpServletResponse response) {
		uiExtendsService.downloadRelationManager(uuid, category, type, value, response);
	}
	
	@GetMapping("/relation/gridview")
	@Operation(summary = "show grid view relation", description = "show grid view relation")
	public PageResult<?> showGridViewRelation(@RequestParam String uuid,
			@RequestParam(required = false) String category, @RequestParam(required = false) String type,
													@RequestParam(required = false) String value, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
		return uiExtendsService.getRelationGridView(uuid, category, type, value, page, size);
	}

	@PostMapping(value = "/target")
	@Operation(summary = "Relation 수정", description = "Relation 수정")
	public void saveTarget(@RequestBody SaveTargetReqDto reqDto) {
		uiExtendsService.saveTarget(reqDto);
	}

	@PutMapping(value = "/validity")
	@Operation(summary = "Relation의 오퍼 또는 그룹 활성여부 설정", description = "Relation의 오퍼 또는 그룹 활성여부 설정")
	public void updateOffrDpdc(@RequestBody OffrDpdcReqDto reqDto) {
		uiExtendsService.updateOffrDpdc(reqDto);
	}

	@GetMapping(value = "/relation/definition")
	@Operation(summary = "Relation 상세 조회", description = "Relation 상세 조회")
	public ItemMappingDetailDto getRelationDefinition(@RequestParam String dpdcRelUuid) {
		return uiExtendsService.retrieveDpdcRelDef(dpdcRelUuid);
	}

	@GetMapping(value = "/count-target")
	@Operation(summary = "리더/폴로어 건수 COUNT", description = "리더/폴로어 건수 COUNT")
	public CountTargetResDto countTarget(@RequestParam String targetUuid,
			@RequestParam(required = false) boolean onlyValidDtm,
			@RequestParam(required = false) boolean includeGroup) {
		RelationViewReqDto req = new RelationViewReqDto();
		req.setTargetUuid(targetUuid);
		req.setOnlyValidDtm(onlyValidDtm);
		req.setIncludeGroup(includeGroup);
		return uiExtendsService.countTarget(req);
	}
}
