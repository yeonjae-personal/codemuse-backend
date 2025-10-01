package com.lgcns.svcp.prod.ui.prod.controller;

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

import com.lgcns.svcp.prod.ui.prod.dto.extend.GroupDetailResDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.ItemOffrResDto;
import com.lgcns.svcp.prod.ui.prod.dto.extend.SearchGroupReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.group.InsertGroupOfferDto;
import com.lgcns.svcp.prod.ui.prod.dto.group.SaveGroupReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.service.UIGroupService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ui/group")
@Tag(name = "UI-group-controller", description = "화면 오퍼그룹 컨트롤러")
public class UIGroupController {

	private final UIGroupService uiGroupService;

	@GetMapping()
	@Operation(summary = "오퍼그룹 조회", description = "오퍼그룹 조회")
	public Object searchOfferGroup(@RequestParam(required = false) String objCode,
			@RequestParam(required = false) String objName, @RequestParam(required = false) String itemCode,
			@RequestParam(required = false) String childOffrUuid, @RequestParam(required = false) boolean onlyValidDtm,
			@RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size,
			@RequestParam(defaultValue = "true", required = false) boolean isPaged) {

		SearchGroupReqDto reqDto = new SearchGroupReqDto();
		reqDto.setSize(size);
		reqDto.setPage(page);
		reqDto.setOffrGrpCd(objCode);
		reqDto.setOffrGrpNm(objName);
		reqDto.setItemCode(itemCode);
		reqDto.setChildOffrUuid(childOffrUuid);
		reqDto.setOnlyValidDtm(onlyValidDtm);
		reqDto.setPaged(isPaged);

		return uiGroupService.searchOfferGroup(reqDto);
	}
	
	@GetMapping(value = "/advanced-detail")
	public PageResult<?> retrieveGroupsAdvancedDetail(@ModelAttribute SearchGroupReqDto reqDto) {
		return uiGroupService.retrieveGroupsAdvancedDetail(reqDto);
	}

	@GetMapping(value = "/detail")
	@Operation(summary = "오퍼그룹 상세 조회", description = "오퍼그룹 상세 조회")
	public GroupDetailResDto retrieveGroupDetail(@RequestParam String objUuid, HttpServletRequest request) {
		String language = request.getHeader("X-Language");
		return uiGroupService.retrieveGroupDetail(objUuid, language);
	}

	@PutMapping()
	@Operation(summary = "오퍼그룹 수정", description = "오퍼그룹 수정")
	public void updateGroup(@RequestBody SaveGroupReqDto saveGroupReqDto) {
		uiGroupService.updateGroup(saveGroupReqDto);
	}

	@PostMapping()
	@Operation(summary = "오퍼그룹 생성", description = "오퍼그룹 생성")
	public Map<String, String> createGroup(@RequestBody SaveGroupReqDto saveGroupReqDto) {
		return uiGroupService.createGroup(saveGroupReqDto);
	}

	@GetMapping(value = "/create-info")
	@Operation(summary = "오퍼그룹 생성을 위한 정보 조회", description = "오퍼그룹 생성을 위한 정보 조회")
	public ItemMappingDetailDto retrieveCreateInfo(@RequestParam String itemCode) {
		return uiGroupService.retrieveCreateInfo(itemCode);
	}

	@GetMapping(value = "/offer-group-rel")
	@Operation(summary = "그룹의 오퍼 목록 조회", description = "그룹의 오퍼 목록 조회")
	public List<ItemOffrResDto> retrieveOfferGroupRel(@RequestParam String objUuid) {
		return uiGroupService.retrieveOfferGroupRel(objUuid);
	}

	@PutMapping(value = "/offer-group-rel")
	@Operation(summary = "그룹의 오퍼 목록 수정", description = "그룹의 오퍼 목록 수정")
	public void updateOfferGroupRel(@RequestBody List<InsertGroupOfferDto> groupOfferList) {
		uiGroupService.updateOfferGroupRel(groupOfferList);
	}
	
	@GetMapping("/export")
	@Operation(summary = "export group", description = "export group")
	public void exportGroup(@ModelAttribute SearchGroupReqDto reqDto, HttpServletResponse response) {
		uiGroupService.export(reqDto, response);
	}
}
