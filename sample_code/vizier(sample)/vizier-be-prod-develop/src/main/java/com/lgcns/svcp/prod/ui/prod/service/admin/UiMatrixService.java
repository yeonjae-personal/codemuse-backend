package com.lgcns.svcp.prod.ui.prod.service.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.context.RequestContextHolder;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixDDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixExportReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixMDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixMeasureDDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixMeasureMDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.SearchMatrixReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.builder.BuilderFactorDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.builder.BuilderFactorValueDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.builder.BuilderReqDto;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.MatrixExcelHelper;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;

@Service("uiMatrixService")
@RequiredArgsConstructor
public class UiMatrixService {
	private final CommonDao commonDao;
	private final MatrixExcelHelper excelHelper;
	private final MessageSource messageSource;

	public PageResult<?> searchMatrix(SearchMatrixReqDto reqDto) {
		return commonDao.selectPagedList("Ui-matrix.searchMatrix", reqDto);
	}

	public List<BuilderFactorDto> retrieveMatrixBuilder(String matrixCode) {
		return commonDao.selectList("Ui-matrix.retrieveMatrixBuilder", matrixCode);
	}

	public List<MatrixMeasureMDto> retrieveMatrix(String matrixCode, BuilderReqDto builderReqDto) {
		if (builderReqDto == null || builderReqDto.getBuilderDtos() == null) {
			return commonDao.selectList("Ui-matrix.retrieveMatrix", matrixCode);
		}
		return generateCartesianMatrix(matrixCode, builderReqDto.getBuilderDtos());
	}

	private List<MatrixMeasureMDto> generateCartesianMatrix(String matrixCode, List<BuilderFactorDto> factors) {
		// Return empty list if factors input is null or empty
		if (factors == null || factors.isEmpty()) {
			return Collections.emptyList();
		}

		// 1. Prepare value lists: Filter valid values (isInUse) for each factor
		List<List<BuilderFactorValueDto>> valueLists = new ArrayList<>(factors.size());
		for (BuilderFactorDto factor : factors) {
			List<BuilderFactorValueDto> vals = factor.getFactorValues();
			if (vals == null) {
				vals = Collections.emptyList();
			}

			// Filter values that are in use
			List<BuilderFactorValueDto> inUseVals = new ArrayList<>();
			for (BuilderFactorValueDto v : vals) {
				if (v.isInUse()) {
					inUseVals.add(v);
				}
			}
			valueLists.add(inUseVals);
		}

		// 2. If any factor has no valid value, the result should be empty (no
		// combination possible)
		for (List<BuilderFactorValueDto> lst : valueLists) {
			if (lst.isEmpty()) {
				return Collections.emptyList();
			}
		}

		// 3. Calculate the total number of combinations (Cartesian product of list
		// sizes)
		int n = valueLists.size();
		long total = 1L;
		int[] sizes = new int[n];
		for (int i = 0; i < n; i++) {
			sizes[i] = valueLists.get(i).size();
			total *= sizes[i];
			if (total > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Too many combinations! Please check your input.");
			}
		}

		// 4. Efficiently generate all combinations by incrementing indices array (no
		// intermediate lists)
		List<MatrixMeasureMDto> result = new ArrayList<>((int) total);
		int[] indices = new int[n];

		for (long rowId = 1; rowId <= total; rowId++) {
			// Generate one combination based on current indices
			List<MatrixMeasureDDto> comb = new ArrayList<>(n);
			for (int i = 0; i < n; i++) {
				BuilderFactorValueDto fv = valueLists.get(i).get(indices[i]);
				MatrixMeasureDDto colViewDto = new MatrixMeasureDDto();
				colViewDto.setRowId(rowId);
				colViewDto.setMatrixCode(matrixCode);
				colViewDto.setFactorCode(fv.getFactorCode());
				colViewDto.setFactorValueCode(fv.getFactorValueCode());
				colViewDto.setFactorValueName(fv.getFactorValueName());
				comb.add(colViewDto);
			}
			// Build and add the result object
			MatrixMeasureMDto matrixMeasureMDto = new MatrixMeasureMDto();
			matrixMeasureMDto.setRowId(rowId);
			matrixMeasureMDto.setMatrixCode(matrixCode);
			matrixMeasureMDto.setMeasureDDtos(comb);
			matrixMeasureMDto.setMatrixNumValue(BigDecimal.ZERO);
			result.add(matrixMeasureMDto);

			// Update indices to point to the next combination (like incrementing a number
			// in base-N)
			for (int pos = n - 1; pos >= 0; pos--) {
				indices[pos]++;
				if (indices[pos] < sizes[pos]) {
					break;
				}
				indices[pos] = 0; // Reset and carry to the next left position
			}
		}
		return result;
	}

	@Transactional
	public synchronized String createMatrix(MatrixMDto matrixMDto) {
		return createMatrixTransactional(matrixMDto);
	}

	@Transactional
	public String createMatrixTransactional(MatrixMDto matrixMDto) {
		// Generate and set matrix code
		String matrixCode = commonDao.select("Ui-matrix.generateNextMatrixCode");
		RequestContextHolder.setCode(matrixCode);
		matrixMDto.setMatrixCode(matrixCode);
		matrixMDto.setUseYn("Y");

		// Initialize collections
		List<MatrixDDto> matrixDetails = matrixMDto.getMatrixDDtos();
		List<MatrixMeasureMDto> measureMasters = matrixMDto.getMeasureMDtos();
		List<MatrixMeasureDDto> measureDetails = new ArrayList<>();

		// Process matrix details and measures
		if (matrixDetails != null && !matrixDetails.isEmpty()) {
			matrixDetails.forEach(detail -> detail.setMatrixCode(matrixCode));
		}
		if (measureMasters != null && !measureMasters.isEmpty()) {
			measureMasters.forEach(detail -> {
				detail.setMatrixCode(matrixCode);
				List<MatrixMeasureDDto> subMeasureDetails = detail.getMeasureDDtos();
				if (subMeasureDetails != null && !subMeasureDetails.isEmpty()) {
					subMeasureDetails.forEach(subDetail -> {
						subDetail.setMatrixCode(matrixCode);
						subDetail.setRowId(detail.getRowId());
					});
					measureDetails.addAll(subMeasureDetails);
				}
			});
		}

		// Perform database operations
		commonDao.insert("Ui-matrix.insertMatrixM", matrixMDto);
		int a = 0;

		if (matrixDetails != null && !matrixDetails.isEmpty()) {
			a = commonDao.batchInsert("Ui-matrix.insertMatrixD", matrixDetails);
		}

		if (measureMasters != null && !measureMasters.isEmpty()) {
			a = commonDao.batchInsert("Ui-matrix.insertMatrixMeasureM", measureMasters);
		}

		if (!measureDetails.isEmpty()) {
			a =commonDao.batchInsert("Ui-matrix.insertMatrixMeasureD", measureDetails);
		}

		return matrixCode;
	}

	@Transactional
	public void updateMatrix(String matrixCode, MatrixMDto matrixMDto) {
		// Generate and set matrix code
		matrixMDto.setMatrixCode(matrixCode);
        RequestContextHolder.setCode(matrixCode);

		// Initialize collections
		List<MatrixDDto> matrixDetails = matrixMDto.getMatrixDDtos();
		List<MatrixMeasureMDto> measureMasters = matrixMDto.getMeasureMDtos();
		List<MatrixMeasureDDto> measureDetails = new ArrayList<>();

		// Process matrix details and measures
		if (matrixDetails != null && !matrixDetails.isEmpty()) {
			matrixDetails.forEach(detail -> detail.setMatrixCode(matrixCode));
		}
		if (measureMasters != null && !measureMasters.isEmpty()) {
			measureMasters.forEach(detail -> {
				detail.setMatrixCode(matrixCode);
				List<MatrixMeasureDDto> subMeasureDetails = detail.getMeasureDDtos();
				if (subMeasureDetails != null && !subMeasureDetails.isEmpty()) {
					subMeasureDetails.forEach(subDetail -> {
						subDetail.setMatrixCode(matrixCode);
						subDetail.setRowId(detail.getRowId());
					});
					measureDetails.addAll(subMeasureDetails);
				}
			});
		}

		// Perform database operations
		commonDao.update("Ui-matrix.updateMatrixM", matrixMDto);

		commonDao.delete("Ui-matrix.deleteMatrixD", matrixCode);
		if (matrixDetails != null && !matrixDetails.isEmpty()) {
			commonDao.batchInsert("Ui-matrix.insertMatrixD", matrixDetails);
		}

		commonDao.delete("Ui-matrix.deleteMatrixMeasureM", matrixCode);
		if (measureMasters != null && !measureMasters.isEmpty()) {
			commonDao.batchInsert("Ui-matrix.insertMatrixMeasureM", measureMasters);
		}

		commonDao.delete("Ui-matrix.deleteMatrixMeasureD", matrixCode);
		if (!measureDetails.isEmpty()) {
			commonDao.batchInsert("Ui-matrix.insertMatrixMeasureD", measureDetails);
		}
	}

	public void exportMatrix(MatrixExportReqDto matrixExportReqDto, HttpServletResponse response) {
		excelHelper.downloadExcel(buildExcelInput(matrixExportReqDto), response, false);
	}

	private ExcelInput buildExcelInput(MatrixExportReqDto matrixExportReqDto) {
		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName("Matrix_" + matrixExportReqDto.getMatrixCode());
		excelInput.setSheetName(matrixExportReqDto.getMatrixCode());
		excelInput.setFormatDate(DateConstant.YYYYMMDDHHMMSS);

		// excelInput.setDatas(matrixExportReqDto.getMeasureMDtos());
		excelInput.setObject(matrixExportReqDto);

		return excelInput;
	}

	@Transactional
	public List<MatrixMeasureMDto> importMatrix(HttpServletRequest request) {
		try {
			Part filePart = request.getPart("file");

			Part dataPart = request.getPart("data");
			String json;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(dataPart.getInputStream()))) {
				json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
			}

			// Validate file name
			String fileName = filePart.getSubmittedFileName();
			if (!fileName.startsWith("Matrix_")) {
				throw new BusinessException(messageSource.getMessage("matrix.excel.filename.invalid", null,
						LocaleContextHolder.getLocale()));
			}

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			MatrixExportReqDto matrixExportReqDto = mapper.readValue(json, MatrixExportReqDto.class);
			MatrixExportReqDto matrixExcelqDto = excelHelper.parseMatrixFromExcel(filePart, matrixExportReqDto);

			return matrixExcelqDto.getMeasureMDtos();
		} catch (IOException | ServletException e) {
			e.printStackTrace();
			throw new BusinessException("An error occurred while importing the matrix.");
		}
	}

}
