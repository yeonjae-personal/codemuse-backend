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

import com.lgcns.svcp.prod.ui.prod.dto.common.search.SearchAdvancedReq;
import com.lgcns.svcp.prod.ui.prod.dto.item.Item;
import com.lgcns.svcp.prod.ui.prod.dto.item.ItemReq;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.resource.ResourceSearchReq;
import com.lgcns.svcp.prod.ui.prod.dto.resource.create.ResourceCreateInfoRes;
import com.lgcns.svcp.prod.ui.prod.dto.resource.create.ResourceCreateReq;
import com.lgcns.svcp.prod.ui.prod.dto.resource.update.ResourceUpdateReq;
import com.lgcns.svcp.prod.ui.prod.service.UiResourceService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ui")
@Tag(name = "UI-resource-controller", description = "화면 리소스 컨트롤러")
public class UiResourceController {

	private final UiResourceService uiResourceService;

	@GetMapping(value = "/resource/items")
	@Operation()
	public List<Item> getAllItem(@RequestParam(required = false) String mItemCode,
			@RequestParam(required = false) String lItemCode, @RequestParam(required = false) String itemCode) {
		ItemReq req = new ItemReq();
		req.setItemCode(itemCode);
		req.setMItemCode(mItemCode);
		req.setLItemCode(lItemCode);
		return uiResourceService.getAllItem(req);
	}

	@GetMapping(value = "/resource/create-info")
	@Operation
	public ResourceCreateInfoRes getCreateResourceForm(@RequestParam String itemCode) {
		return uiResourceService.getResourceForm(itemCode);
	}

	@PostMapping(value = "/resource")
	@Operation()
	public Map<String, String> createResource(@RequestBody ResourceCreateReq req) {
		return uiResourceService.createResource(req);
	}

	@GetMapping(value = "/resource/get-by-type")
	@Operation()
	public PageResult<?> getResources(@RequestParam(required = false) String objUuid,
			@RequestParam(required = false) String itemCode, @RequestParam(required = false) String objName,
			@RequestParam(required = false) String objCode, @RequestParam(required = false) boolean onlyValidDtm,
			@RequestParam(required = false) int page, @RequestParam(required = false) int size) {

		ResourceSearchReq resourceSearchReq = new ResourceSearchReq();
		resourceSearchReq.setObjUuid(objUuid);
		resourceSearchReq.setObjCode(objCode);
		resourceSearchReq.setObjName(objName);
		resourceSearchReq.setItemCode(itemCode);
		resourceSearchReq.setOnlyValidDtm(onlyValidDtm);
		resourceSearchReq.setPage(page);
		resourceSearchReq.setSize(size);

		return uiResourceService.getResources(resourceSearchReq);
	}

	@GetMapping(value = "/resource/detail")
	@Operation(summary = "Get resource detail by resource uuid")
	public ItemMappingDetailDto getResourceDetail(@RequestParam String objUuid, HttpServletRequest request) {
		String language = request.getHeader("X-Language");
		return uiResourceService.getResourceDetail(objUuid, language);
	}

	@PutMapping(value = "/resource")
	@Operation(summary = "Update resource")
	public void updateResource(@RequestBody ResourceUpdateReq req) {
		uiResourceService.updateResource(req);
	}

	@GetMapping(value = "/resource/advanced")
	@Operation(summary = "리소스 상세 검색", description = "리소스 상세 검색")
	public PageResult<?> retrieveResourcesAdvanced(@ModelAttribute SearchAdvancedReq searchAdvancedReq) {
		return uiResourceService.retrieveResourcesAdvanced(searchAdvancedReq);
	}
	
	@GetMapping(value = "/resource/advanced-detail")
	public PageResult<?> retrieveResourcesAdvancedDetail(@ModelAttribute SearchAdvancedReq searchAdvancedReq) {
		return uiResourceService.retrieveResourcesAdvancedDetail(searchAdvancedReq);
	}
	
	@GetMapping("/resource/export")
	@Operation(summary = "export resource", description = "export resource")
	public void exportResource(@ModelAttribute SearchAdvancedReq searchAdvancedReq, HttpServletResponse response) {
		uiResourceService.export(searchAdvancedReq, response);
	}
}
