package com.lgcns.svcp.prod.ui.prod.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.publish.aprv.AprvFlowTmptMSearchReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.item.ChngDataListLSearchReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.packages.PubAprvSubStepLDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.packages.PubPackageDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.packages.PubRqstTaskMSearchReqDto;
import com.lgcns.svcp.prod.ui.prod.enums.YesNo;
import com.lgcns.svcp.prod.ui.prod.service.UIPublishService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ui/publish-management")
@Tag(name = "UI-publish-controller", description = "화면 Publish 관리 컨트롤러")
public class UIPublishController {
	private final UIPublishService uiPublishService;

	@GetMapping(value = "/packages")
	@Operation(summary = "Package Search", description = "Package Search")
	public PageResult<?> searchPackages(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(required = false) String pubRqstTaskCode,
			@RequestParam(required = false) String pubRqstTaskCodeName,
			@RequestParam(required = false) String pubRqstTaskPubr,
			@RequestParam(required = false) String pubRqstStusCode) {

		PubRqstTaskMSearchReqDto reqDto = new PubRqstTaskMSearchReqDto();
		reqDto.setPage(page);
		reqDto.setSize(size);
		reqDto.setPubRqstTaskCode(pubRqstTaskCode);
		reqDto.setPubRqstTaskCodeName(pubRqstTaskCodeName);
		reqDto.setPubRqstTaskPubr(pubRqstTaskPubr);
		reqDto.setPubRqstStusCode(pubRqstStusCode);

		return uiPublishService.searchPubRqstTaskMaster(reqDto);
	}

	@GetMapping(value = "/items")
	@Operation(summary = "Item Search", description = "Item Search")
	public PageResult<?> itemSearch(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(required = false) String chngDataCode,
			@RequestParam(required = false) String chngDataCodeName, @RequestParam(required = false) String chgDeptName,
			@RequestParam(required = false) String chgUser) {

		ChngDataListLSearchReqDto reqDto = new ChngDataListLSearchReqDto();
		reqDto.setPage(page);
		reqDto.setSize(size);
		reqDto.setChngDataCode(chngDataCode);
		reqDto.setChngDataCodeName(chngDataCodeName);
		reqDto.setChgDeptName(chgDeptName);
		reqDto.setChgUser(chgUser);

		return uiPublishService.searchChngDataDetail(reqDto);
	}

	@GetMapping(value = "/package/{pubRqstTaskCode}")
	@Operation(summary = "Package Search", description = "Package Search")
	public PubPackageDto retrievePackage(@PathVariable String pubRqstTaskCode) {
		return uiPublishService.retrievePackage(pubRqstTaskCode);
	}

	@PostMapping("/package/general")
	@Operation(summary = "Create General Publish Package", description = "Create General Publish Package")
	public String createGeneralPublishPackage(@RequestBody PubPackageDto pubPackageDto) {
		return uiPublishService.createGeneralPublishPackage(pubPackageDto);
	}

	@PutMapping("/package/general")
	@Operation(summary = "Update General Publish Package", description = "Update General Publish Package")
	public void updateGeneralPublishPackage(@RequestBody PubPackageDto pubPackageDto) {
		uiPublishService.updateGeneralPublishPackage(pubPackageDto);
	}

	@PostMapping("/package/compose")
	@Operation(summary = "Save Compose Publish Package", description = "Save Compose Publish Package")
	public void saveComposePublishPackage(@RequestBody PubPackageDto pubPackageDto) {
		uiPublishService.saveComposePublishPackage(pubPackageDto);
	}

	@PostMapping("/package/validate")
	@Operation(summary = "Validate Publish Package", description = "Validate Publish Package")
	public YesNo validatePublishPackage(@RequestBody PubPackageDto pubPackageDto) {
		return uiPublishService.validatePublishPackage(pubPackageDto);
	}

	@PostMapping("/packages/approval-requests")
	@Operation(summary = "Request Approval", description = "Request Approval")
	public void requestApproval(@RequestBody PubPackageDto pubPackageDto) {
		uiPublishService.requestApproval(pubPackageDto);
	}

	@PostMapping("/package/approval")
	@Operation(summary = "Save Approve Publish Package", description = "Save Approve Publish Package")
	public void saveApprovePublishPackage(@RequestBody PubPackageDto pubPackageDto) {
		uiPublishService.saveApprovePublishPackage(pubPackageDto);
	}

	@PutMapping("/approval")
	@Operation(summary = "Approve/Reject Publish Package", description = "Approve/Reject Publish Package")
	public void decidePublishPackage(@RequestBody PubAprvSubStepLDto pubAprvSubStepLDto) {
		uiPublishService.decidePublishPackage(pubAprvSubStepLDto);
	}

	@PostMapping("/package/publish")
	@Operation(summary = "Publish Request", description = "Publish Request")
	public void publishedPackage(@RequestBody PubPackageDto pubPackageDto) {
		uiPublishService.publishedPackage(pubPackageDto, false);
	}

	@GetMapping(value = "/approval-templates")
	@Operation(summary = "Approval Template Search", description = "Search for approval templates")
	public PageResult<?> searchPackages(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(required = false) String aprvFlowTmptCode,
			@RequestParam(required = false) String aprvFlowTmptName, @RequestParam(required = false) String aprvUser) {

		AprvFlowTmptMSearchReqDto reqDto = new AprvFlowTmptMSearchReqDto();
		reqDto.setPage(page);
		reqDto.setSize(size);
		reqDto.setAprvFlowTmptCode(aprvFlowTmptCode);
		reqDto.setAprvFlowTmptName(aprvFlowTmptName);
		reqDto.setAprvUser(aprvUser);

		return uiPublishService.searchAprvFlowTmptM(reqDto);
	}
}
