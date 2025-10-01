package com.lgcns.svcp.prod.ui.prod.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.admin.table.SaveTableDataReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.SearcTableStrcReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.SearchTableDataReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.SearchTableTypeReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.TableColumnDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.TableStrcTypeDto;
import com.lgcns.svcp.prod.ui.prod.service.admin.UiTableService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ui/table-management")
@Tag(name = "UI-table-controller", description = "화면 Table Structure 컨트롤러")
public class UiTableController {
	private final UiTableService uiTableService;

	@GetMapping()
	@Operation(summary = "테이블 유형 조회", description = "테이블 유형 조회")
	public PageResult<?> searchTableType(@RequestParam(required = false) String tableTypeName,
			@RequestParam(required = false) String tableTypeCode,
			@RequestParam(required = false) String useYn, @RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size) {

		SearchTableTypeReqDto reqDto = new SearchTableTypeReqDto();
		reqDto.setSize(size);
		reqDto.setPage(page);
		reqDto.setTableTypeName(tableTypeName);
		reqDto.setTableTypeCode(tableTypeCode);
		reqDto.setUseYn(useYn);

		return uiTableService.searchTableType(reqDto);
	}

	@GetMapping("/{tableTypeCode}")
	@Operation(summary = "테이블 유형 상세", description = "테이블 유형 상세")
	public TableStrcTypeDto retrieveTableType(@PathVariable String tableTypeCode,
			@RequestParam(required = false) String tableName, @RequestParam(required = false) String useYn,
			@RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size) {

		SearcTableStrcReqDto reqDto = new SearcTableStrcReqDto();
		reqDto.setSize(size);
		reqDto.setPage(page);
		reqDto.setTableTypeCode(tableTypeCode);
		reqDto.setTableName(tableName);
		reqDto.setUseYn(useYn);

		return uiTableService.retrieveTableType(reqDto);
	}

	@PutMapping("/{tableTypeCode}")
	@Operation(summary = "테이블 유형 수정", description = "테이블 유형 수정")
	public void updateTableStrcType(@PathVariable String tableTypeCode,
			@RequestBody TableStrcTypeDto tableStrcTypeDto) {
		uiTableService.updateTableStrcType(tableStrcTypeDto);
	}

	@PostMapping("/struc/header/{tableName}")
	@Operation(summary = "테이블 스트럭쳐 헤더", description = "테이블 스트럭쳐 헤더")
	public List<TableColumnDto> retrieveTableHeader(@PathVariable String tableName,
			@RequestBody SearchTableDataReqDto reqDto) {
		return uiTableService.retrieveTableHeader(tableName, reqDto);
	}

	@PostMapping("/struc/{tableName}")
	@Operation(summary = "테이블 스트럭쳐 상세", description = "테이블 스트럭쳐 상세")
	public PageResult<?> retrieveTableData(@PathVariable String tableName, @RequestBody SearchTableDataReqDto reqDto) {
		return uiTableService.retrieveTableData(tableName, reqDto);
	}
	
	@GetMapping("/export")
	@Operation(summary = "export table", description = "export table")
	public void exportLabel(@RequestParam(required = false) String tableName, @RequestParam String language, HttpServletResponse response) {
		SearchTableDataReqDto reqDto = new SearchTableDataReqDto();
		uiTableService.export(tableName, language, reqDto, response);
	}
	
	@PutMapping("/struc/{tableName}")
	@Operation(summary = "테이블 스트럭쳐 수정", description = "테이블 스트럭쳐 수정")
	public void saveTableData(@PathVariable String tableName, @RequestBody SaveTableDataReqDto reqDto) {
		uiTableService.saveTableData(tableName, reqDto);
	}
}
