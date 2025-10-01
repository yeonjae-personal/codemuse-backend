package com.lgcns.svcp.prod.ruleengine.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ruleengine.dto.field.FieldDto;
import com.lgcns.svcp.prod.ruleengine.dto.field.FieldSearchPagingDto;
import com.lgcns.svcp.prod.ruleengine.service.FieldService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("rule-engine/field")
@Tag(name = "rule-engine-field-controller", description = "룰엔진 필드 컨트롤러")
@AllArgsConstructor
public class FieldController {
	
	private final FieldService fieldService;
	
	@GetMapping
	@Operation(summary = "search field", description = "search field")
	public PageResult<FieldDto> searchField(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size, 
				@RequestParam(name = "type", required = false) String type, @RequestParam(name = "value", required = false) String value, HttpServletRequest request) {
		
		FieldSearchPagingDto searchPaging = new FieldSearchPagingDto();
		searchPaging.setPage(page);
		searchPaging.setSize(size);
		searchPaging.setType(type);
		searchPaging.setValue(value);
	    return fieldService.retrieveAllFields(searchPaging);
	}
	
	@PostMapping
	@Operation(summary = "save field", description = "save field")
	public void saveField(@RequestBody FieldDto fieldDto) {
		fieldService.save(fieldDto);
	}
}
