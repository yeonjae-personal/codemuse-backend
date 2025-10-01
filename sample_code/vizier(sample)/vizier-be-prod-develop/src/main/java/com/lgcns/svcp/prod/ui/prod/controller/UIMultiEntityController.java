package com.lgcns.svcp.prod.ui.prod.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.common.SelectOptionDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.MultiEntityAdditionalDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.MultiEntityDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.create.CreateEntityReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.EntityItemRelResDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.search.SearchItemRelationReq;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.search.SearchMultiEntityReqDto;
import com.lgcns.svcp.prod.ui.prod.service.UIMultiEntityService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ui/multi-entity")
@Tag(name = "UI-multi-entity-controller", description = "화면 멀티 엔티티 컨트롤러")
@RequiredArgsConstructor
public class UIMultiEntityController {

	private final UIMultiEntityService uiMultiEntityService;

	@GetMapping(value = "/search-info")
	@Operation(summary = "Multi Entity 검색을 위한 정보 조회", description = "Multi Entity 검색을 위한 정보 조회")
	public List<SelectOptionDto> retrieveSearchInfo() {
		return uiMultiEntityService.retrieveSearchInfo();
	}

	@GetMapping()
	@Operation(summary = "Multi Entity 목록 조회", description = "Multi Entity 목록 조회")
	public PageResult<?> retrieveMultiEntityList(@RequestParam String itemCode,
			@RequestParam(required = false) String entityTypeCode,
			@RequestParam(required = false) String multiEntityCode,
			@RequestParam(required = false) String multiEntityName,
			@RequestParam(required = false) boolean onlyValidDtm,
			@RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size) {

		SearchMultiEntityReqDto reqDto = new SearchMultiEntityReqDto();
		reqDto.setSize(size);
		reqDto.setPage(page);
		reqDto.setItemCode(itemCode);
		reqDto.setEntityTypeCode(entityTypeCode);
		reqDto.setMultiEntityCode(multiEntityCode);
		reqDto.setMultiEntityName(multiEntityName);
		reqDto.setOnlyValidDtm(onlyValidDtm);

		return uiMultiEntityService.retrieveMultiEntityList(reqDto);
	}

	@GetMapping(value = "/detail")
	@Operation(summary = "Multi Entity 상세 조회", description = "Multi Entity 상세 조회")
	public MultiEntityDto retrieveMultiEntityDetail(@RequestParam String entityCode,
			@RequestParam String entityTypeCode, HttpServletRequest request) {
		String langCode = request.getHeader("X-Language");
		return uiMultiEntityService.retrieveMultiEntityDetail(entityCode, entityTypeCode, langCode);
	}

	@GetMapping(value = "/create-info")
	@Operation(summary = "Multi Entity 생성을 위한 정보 조회", description = "Multi Entity 생성을 위한 정보 조회")
	public List<MultiEntityAdditionalDto> retrieveCreateInfo(@RequestParam String entityTypeCode,
			HttpServletRequest request) {
		String langCode = request.getHeader("X-Language");
		return uiMultiEntityService.retrieveCreateInfo(entityTypeCode, langCode);
	}

	@PostMapping()
	@Operation(summary = "Multi Entity 생성", description = "Multi Entity 생성")
	public Map<String, String> createMultiEntity(@RequestBody CreateEntityReqDto createEntityReqDto) {
		return uiMultiEntityService.createMultiEntity(createEntityReqDto);
	}

	@PutMapping()
	@Operation(summary = "Multi Entity 수정", description = "Multi Entity 수정")
	public void updateMultiEntity(@RequestBody CreateEntityReqDto saveEntityReqDto) {
		uiMultiEntityService.updateMultiEntity(saveEntityReqDto);
	}

	@GetMapping("/item-relation")
	@Operation(summary = "Item에 연결된 Multi Entity 목록 조회", description = "Item에 연결된 Multi Entity 목록 조회")
	public List<EntityItemRelResDto> retrieveItemRelation(@RequestParam String objUuid,
			@RequestParam(required = false, defaultValue = "false") boolean onlyValidDtm) {
		SearchItemRelationReq req = new SearchItemRelationReq();
		req.setObjUuid(objUuid);
		req.setOnlyValidDtm(onlyValidDtm);
		return uiMultiEntityService.retrieveItemRelation(req);
	}

}
