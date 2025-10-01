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

import com.lgcns.svcp.prod.ui.prod.dto.component.ComponentCreateInfoRes;
import com.lgcns.svcp.prod.ui.prod.dto.component.ComponentGeneralDto;
import com.lgcns.svcp.prod.ui.prod.dto.component.CreateComponentReq;
import com.lgcns.svcp.prod.ui.prod.dto.component.addResource.ComponentAddResourceReq;
import com.lgcns.svcp.prod.ui.prod.dto.component.list.ComponentSearchReq;
import com.lgcns.svcp.prod.ui.prod.dto.component.search.ComponentSearchAdvancedReq;
import com.lgcns.svcp.prod.ui.prod.dto.component.update.ComponentUpdateReq;
import com.lgcns.svcp.prod.ui.prod.dto.item.Item;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.enums.ComponentItemCode;
import com.lgcns.svcp.prod.ui.prod.enums.ComponentType;
import com.lgcns.svcp.prod.ui.prod.service.UiComponentService;
import com.lgcns.svcp.prod.util.StringUtilCustom;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ui")
@Tag(name = "UI-component-controller", description = "Component")
@RequiredArgsConstructor
public class UiComponentController {

    private final UiComponentService uiComponentService;

    @GetMapping(value = "component/create-info")
    @Operation(summary = "Get component create information")
	public ComponentCreateInfoRes getComponentCreateForm(@RequestParam String itemCode) {
		return uiComponentService.getCreateComponentForm(itemCode);
    }

    @PostMapping(value = "component")
    @Operation(summary = "Create Component")
	public Map<String, String> createComponent(@RequestBody CreateComponentReq req) {
        return uiComponentService.createComponent(req);
    }

    @GetMapping(value = "/component/by-resource")
    @Operation(summary = "Get component impact analysis by resource id")
	public List<ComponentGeneralDto> getComponentImpactAnalysis(@RequestParam String resourceUUID) {
		return uiComponentService.getComponentImpactAnalysis(resourceUUID);
    }

    @GetMapping(value = "/component/detail")
    @Operation(summary = "Get component detail by component uuid")
	public ItemMappingDetailDto getComponentDetail(@RequestParam String objUuid, HttpServletRequest request) {
    	String language = request.getHeader("X-Language");
		return uiComponentService.getComponentDetail(objUuid, language);
    }

    @GetMapping(value = "/component/resources")
    @Operation(summary = "Get all resource can be added by component")
	public PageResult<?> getResources(@RequestParam(required = false) String componentUUID,
                                 @RequestParam(required = false) String componentType,
                                 @RequestParam(required = false) String componentSubType,
                                 @RequestParam(required = false) String componentCreateType,
                                 @RequestParam(required = false) String rscName,
                                 @RequestParam(required = false) String rscCode,
                                 @RequestParam(required = false) String rscItemCode,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer size) {

        String itemCode = StringUtilCustom.isBlank(rscItemCode) ? null : rscItemCode.trim();
        ComponentAddResourceReq addResourceReq = new ComponentAddResourceReq();
        addResourceReq.setComponentUUID(componentUUID);
        addResourceReq.setComponentType(componentType);
        addResourceReq.setComponentSubType(componentSubType);
        addResourceReq.setComponentCreateType(componentCreateType);
        addResourceReq.setName(rscName);
        addResourceReq.setCode(rscCode);
        addResourceReq.setItemCode(itemCode);
        addResourceReq.setPage(page);
        addResourceReq.setSize(size);
        
		return uiComponentService.getResourceAddForComponent(addResourceReq);
    }

    @GetMapping(value = "/components")
    @Operation(summary = "Get component")
	public PageResult<?> getListComponent(@RequestParam(required = false) String offerUUID,
                                     @RequestParam(required = false) ComponentType componentType,
                                     @RequestParam(required = false) ComponentItemCode componentSubType,
                                     @RequestParam(required = false) String componentCode,
                                     @RequestParam(required = false) String componentName,
                                     @RequestParam(required = false) boolean onlyValidDtm,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size) {
        ComponentSearchReq req = new ComponentSearchReq(offerUUID, componentType, componentSubType, componentCode, componentName, onlyValidDtm, page, size);
		return uiComponentService.getComponentList(req);
    }

    @PutMapping(value = "/component")
    @Operation(summary = "Update component")
	public void updateComponent(@RequestBody ComponentUpdateReq updateReq) {
        uiComponentService.updateComponent(updateReq);
    }

    @GetMapping(value = "/component/types")
    @Operation(summary = "컴포넌트 검색조건의 Type 조회")
	public List<Item> retrieveAllTypeComponent() {
		return uiComponentService.retrieveAllTypeComponent();
    }

    @GetMapping(value = "/component/advanced")
    @Operation(summary = "콤포넌트 상세 검색", description = "콤포넌트 상세 검색")
	public PageResult<?> retrieveComponentsAdvanced(@ModelAttribute ComponentSearchAdvancedReq searchAdvancedReq) {
		return uiComponentService.retrieveComponentsAdvanced(searchAdvancedReq);
    }
    
    @GetMapping(value = "/component/advanced-detail")
	public PageResult<?> retrieveComponentsAdvancedWithDetail(
			@ModelAttribute ComponentSearchAdvancedReq searchAdvancedReq) {
		return uiComponentService.retrieveComponentsAdvancedDetail(searchAdvancedReq);
    }
    
    @GetMapping("/component/export")
	@Operation(summary = "export component", description = "export component")
	public void exportComponent(@ModelAttribute ComponentSearchAdvancedReq searchAdvancedReq, HttpServletResponse response) {
    	uiComponentService.export(searchAdvancedReq, response);
	}
}
