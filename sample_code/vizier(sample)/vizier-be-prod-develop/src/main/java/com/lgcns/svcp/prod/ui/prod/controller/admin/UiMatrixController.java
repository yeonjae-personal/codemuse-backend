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

import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixExportReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixMDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixMeasureMDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.SearchMatrixReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.builder.BuilderFactorDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.builder.BuilderReqDto;
import com.lgcns.svcp.prod.ui.prod.service.admin.UiMatrixService;
import com.lgcns.svcp.prod.util.paging.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ui/matrix-management")
@Tag(name = "UI-matrix-controller", description = "화면 Matrix 컨트롤러")
public class UiMatrixController {
	private final UiMatrixService uiMatrixService;

	@GetMapping()
	@Operation(summary = "Matrix 조회", description = "Matrix 조회")
	public PageResult<?> searchMatrix(@RequestParam(required = false) String matrixCodeName,
			@RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size) {

		SearchMatrixReqDto reqDto = new SearchMatrixReqDto();
		reqDto.setSize(size);
		reqDto.setPage(page);
		reqDto.setMatrixCodeName(matrixCodeName);

		return uiMatrixService.searchMatrix(reqDto);
	}

	@GetMapping("/builder")
	@Operation(summary = "Matrix Builder 상세조회", description = "Matrix Builder 상세조회")
	public List<BuilderFactorDto> retrieveMatrixBuilder(String matrixCode) {
		return uiMatrixService.retrieveMatrixBuilder(matrixCode);
	}

	@PostMapping("/{matrixCode}")
	@Operation(summary = "Matrix 상세조회", description = "Matrix 상세조회")
	public List<MatrixMeasureMDto> retrieveMatrix(@PathVariable String matrixCode,
			@RequestBody BuilderReqDto builderReqDto) {
		return uiMatrixService.retrieveMatrix(matrixCode, builderReqDto);
	}

	@PostMapping()
	@Operation(summary = "Matrix 생성", description = "Matrix 생성")
	public String createMatrix(@RequestBody MatrixMDto matrixMDto) {
		return uiMatrixService.createMatrix(matrixMDto);
	}

	@PutMapping("/{matrixCode}")
	@Operation(summary = "Matrix 수정", description = "Matrix 수정")
	public void updateMatrix(@PathVariable String matrixCode, @RequestBody MatrixMDto matrixMDto) {
		uiMatrixService.updateMatrix(matrixCode, matrixMDto);
	}

	@PostMapping("/export")
	@Operation(summary = "Matrix 다운로드", description = "Matrix 다운로드")
	public void exportMatrix(@RequestBody MatrixExportReqDto matrixExportReqDto, HttpServletResponse response) {
		uiMatrixService.exportMatrix(matrixExportReqDto, response);
	}

}
