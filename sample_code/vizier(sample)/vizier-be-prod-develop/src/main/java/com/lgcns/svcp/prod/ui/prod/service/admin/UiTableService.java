package com.lgcns.svcp.prod.ui.prod.service.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.SaveTableDataDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.SaveTableDataReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.SearcTableStrcReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.SearchTableDataDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.SearchTableDataReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.SearchTableTypeReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.TableColumnDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.TableStrcTypeDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.table.ref.TableColumnKeyValue;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.search.DynamicFieldReq;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.TableExcelHelper;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service("uiTableService")
@RequiredArgsConstructor
public class UiTableService {

	private final CommonDao commonDao;
	private final MessageSource messageSource;
	private final TableExcelHelper excelHelper;

	public PageResult<?> searchTableType(SearchTableTypeReqDto reqDto) {
		return commonDao.selectPagedList("Ui-table.searchTableType", reqDto);
	}

	public TableStrcTypeDto retrieveTableType(SearcTableStrcReqDto reqDto) {
		SearchTableTypeReqDto typeReqDto = new SearchTableTypeReqDto();
		typeReqDto.setTableTypeCode(reqDto.getTableTypeCode());

		TableStrcTypeDto tableStrcTypeDto = commonDao.select("Ui-table.searchTableType", typeReqDto);

		if (tableStrcTypeDto != null) {
			PageResult<?> tableStrcDtos = commonDao.selectPagedList("Ui-table.searchTableStrc", reqDto);
			tableStrcTypeDto.setTableStrcDtos(tableStrcDtos);
		}
		return tableStrcTypeDto;
	}

	public void updateTableStrcType(TableStrcTypeDto tableStrcTypeDto) {
		commonDao.update("Ui-table.updateTableStrcType", tableStrcTypeDto);
	}

	public List<TableColumnDto> retrieveTableHeader(String tableName, SearchTableDataReqDto reqDto) {
		List<TableColumnDto> headers = commonDao.selectList("Ui-table.retrieveTableHeader",
				Collections.singletonMap("tableName", tableName));
		if (headers == null || headers.isEmpty()) {
			throw new BusinessException("Cannot find columns for the table ('" + tableName
					+ "'). Please check the table name or input data again.");
		}

		if (reqDto != null) {
			SearchTableDataDto searchTableDataDto = buildSearchTableDto(tableName, reqDto, headers);
			List<Map<String, String>> result = commonDao.selectList("Ui-table.retrieveTableData", searchTableDataDto);

			// Map to hold distinct values for each column
			Map<String, Set<String>> distinctValuesMap = new HashMap<>();

			// JSON parser for DM-type columns
			ObjectMapper objectMapper = new ObjectMapper();

			for (TableColumnDto column : headers) {
				String columnName = column.getColumnName();
				String columnType = column.getColumnType();

				// Use LinkedHashSet to maintain insertion order and uniqueness
				Set<String> distinctValues = new LinkedHashSet<>();

				if ("DL".equals(columnType)) {
					// For DL type: extract raw values directly from rows
					for (Map<String, String> row : result) {
						String value = row.get(columnName);
						if (value != null && !value.isEmpty()) {
							distinctValues.add(value);
						}
					}

					distinctValuesMap.put(columnName, distinctValues);

				} else if ("DM".equals(columnType)) {
					// For DM type: value is a JSON array string like ["A", "B", "C"]
					for (Map<String, String> row : result) {
						String value = row.get(columnName);

						// Skip if null, empty, or "[]"
						if (value == null || value.isEmpty() || "[]".equals(value.trim())) {
							continue;
						}

						try {
							// Parse JSON array string to List<String>
							List<String> items = objectMapper.readValue(value, new TypeReference<List<String>>() {
							});
							for (String item : items) {
								if (item != null && !item.isEmpty()) {
									distinctValues.add(item);
								}
							}
						} catch (Exception e) {
							// Log parsing errors (can replace with logger if needed)
							throw new BusinessException("Failed to parse DM column value: " + value);
						}
					}

					distinctValuesMap.put(columnName, distinctValues);
				}

				// Set filtered values to the DTO for use on the frontend
				column.setAllowedFilters(new ArrayList<>(distinctValues));
			}
		}
		return headers;
	}

	public PageResult<?> retrieveTableData(String tableName, SearchTableDataReqDto reqDto) {
		List<TableColumnDto> headers = retrieveTableHeader(tableName, reqDto);
		SearchTableDataDto searchTableDataDto = buildSearchTableDto(tableName, reqDto, headers);
		return commonDao.selectPagedList("Ui-table.retrieveTableData", searchTableDataDto);
	}

	private SearchTableDataDto buildSearchTableDto(String tableName, SearchTableDataReqDto reqDto,
			List<TableColumnDto> headers) {

		SearchTableDataDto searchTableDataDto = new SearchTableDataDto();
		searchTableDataDto.setTableName(tableName);

		searchTableDataDto.setSize(reqDto.getSize());
		searchTableDataDto.setPage(reqDto.getPage());

		List<String> selectColumns = headers.stream().map(TableColumnDto::getColumnName).collect(Collectors.toList());
		searchTableDataDto.setSelectColumns(selectColumns);

		// Validate sort param
		Map<String, String> fieldSorts = reqDto.getFieldSorts();
		String orderByClause = null;

		if (fieldSorts != null && !fieldSorts.isEmpty()) {
			Map<String, String> headerTypeMap = headers.stream().collect(
					Collectors.toMap(TableColumnDto::getColumnName, TableColumnDto::getColumnType, (a, b) -> a));

			List<String> orderByList = fieldSorts.entrySet().stream().map(entry -> {
				String columnName = entry.getKey();
				String direction = "DESC".equalsIgnoreCase(entry.getValue()) ? "DESC" : "ASC";
				String type = headerTypeMap.get(columnName);

				if ("NF".equalsIgnoreCase(type) || "RF".equalsIgnoreCase(type)) {
					return "CAST(" + columnName + " AS UNSIGNED) " + direction;
				}
				return "UPPER(CAST(" + columnName + " AS CHAR)) " + direction;
			}).collect(Collectors.toList());

			if (!orderByList.isEmpty()) {
				orderByClause = String.join(", ", orderByList);
			}
		}

		searchTableDataDto.setOrderByClause(orderByClause);

		// Validate condtion param
		List<DynamicFieldReq> fieldSearchs = reqDto.getFieldSearchs();
		if (fieldSearchs != null && !fieldSearchs.isEmpty()) {
			for (DynamicFieldReq field : fieldSearchs) {
				if (!selectColumns.contains(field.getFieldName())) {
					throw new BusinessException(
							"The condition field key ('" + field.getFieldName() + "') is invalid. Please check again.");
				}
			}
		}
		searchTableDataDto.setFieldSearchs(fieldSearchs);
		return searchTableDataDto;
	}

	public void saveTableData(String tableName, SaveTableDataReqDto reqDto) {
		// Retrieve table column headers
		List<TableColumnDto> headers = retrieveTableHeader(tableName, null);

		// Process DELETE operation if data exists
		List<List<String>> deleteData = reqDto.getDeleteData();
		if (deleteData != null && !deleteData.isEmpty()) {
			List<SaveTableDataDto> deleteList = buildTableDataDtoList(tableName, headers, deleteData);
			int deleteResult = commonDao.batchDelete("Ui-table.deleteTableData", deleteList);
			if (deleteResult != deleteList.size()) {
				throw new BusinessException("Delete failed. Please check your data.");
			}
		}

		// Process UPDATE operation if data exists
		List<List<String>> updateData = reqDto.getUpdateData();
		if (updateData != null && !updateData.isEmpty()) {
			List<SaveTableDataDto> updateList = buildTableDataDtoList(tableName, headers, updateData);
			int updateResult = commonDao.batchUpdate("Ui-table.updateTableData", updateList);
			if (updateResult != updateList.size()) {
				throw new BusinessException("Update failed. Please check your data.");
			}
		}

		// Process INSERT operation if data exists
		List<List<String>> addData = reqDto.getAddData();
		List<SaveTableDataDto> addList = null;
		try {
			if (addData != null && !addData.isEmpty()) {
				addList = buildTableDataDtoList(tableName, headers, addData);
				int insertResult = commonDao.batchInsert("Ui-table.insertTableData", addList);
				if (insertResult != addList.size()) {
					throw new BusinessException("Insert failed. Please check your data.");
				}
			}
		} catch (DuplicateKeyException e) {
			List<String> primaryKeyNames = headers.stream().filter(col -> "Y".equalsIgnoreCase(col.getColumnKeyYn()))
					.map(TableColumnDto::getColumnComment).collect(Collectors.toList());

			String keysString = String.join(", ", primaryKeyNames);

			throw new BusinessException(messageSource.getMessage("table.primarykey.duplicate",
					new Object[] { keysString }, LocaleContextHolder.getLocale()));
		}

	}

	private List<SaveTableDataDto> buildTableDataDtoList(String tableName, List<TableColumnDto> headers,
			List<List<String>> data) {
		List<SaveTableDataDto> tableDataList = new ArrayList<>();
		int headerSize = headers.size();
		for (List<String> row : data) {
			// Check if row size matches header size
			if (headerSize != row.size()) {
				throw new BusinessException("Mismatch between header columns and data columns at row: expected "
						+ headerSize + " but got " + row.size());
			}

			SaveTableDataDto tableRow = new SaveTableDataDto();
			tableRow.setTableName(tableName);

			Map<String, Object> columnData = new LinkedHashMap<>();
			Map<String, Object> columnPrimaryKeys = new LinkedHashMap<>();

			// Map each cell in the row to its column name
			for (int i = 0; i < headerSize; i++) {
				TableColumnDto header = headers.get(i);
				String columnName = header.getColumnName();
				String columnType = header.getColumnType();
				Object value = row.get(i);
				if (value != null) {
					if ("NF".equals(columnType) || "RF".equals(columnType)) {
						try {
							value = new BigDecimal((String) value);
						} catch (NumberFormatException e) {
							throw new IllegalArgumentException("Cannot convert to DECIMAL: " + value, e);
						}
					} else if ("DP".equals(columnType)) {
						try {
							value = DateUtil.convertToDateByDatabaseFormat((String) value);
						} catch (Exception e) {
							throw new IllegalArgumentException("Cannot convert to TIMESTAMP: " + value, e);
						}
					}
				}
				if ("Y".equalsIgnoreCase(header.getRequiredYn()) && value == null) {
					throw new BusinessException(messageSource.getMessage("table.field.empty",
							new Object[] { header.getColumnComment() }, LocaleContextHolder.getLocale()));
				}

				// Assign to primary key or data depending on columnKeyYn flag
				if ("Y".equalsIgnoreCase(header.getColumnKeyYn())) {
					if (value == null) {
						throw new BusinessException(messageSource.getMessage("table.primarykey.null", null,
								LocaleContextHolder.getLocale()));
					}
					columnPrimaryKeys.put(columnName, value);
				} else {
					columnData.put(columnName, value);
				}
			}
			tableRow.setColumnData(columnData);
			tableRow.setColumnPrimaryKeys(columnPrimaryKeys);
			tableDataList.add(tableRow);
		}
		return tableDataList;
	}

	public void populateTableColumnKeyValues(List<AdditionalDetailDto> additionalDetails) {
		// Extract distinct non-empty table names
		List<String> tableNames = additionalDetails.stream().map(AdditionalDetailDto::getAttrRefTableName)
				.filter(Objects::nonNull).filter(name -> !name.isEmpty()).distinct().toList();

		if (tableNames.isEmpty()) {
			return;
		}

		// Fetch column names from tb_table_column_m
		List<TableColumnDto> allColumns = commonDao.selectList("Ui-table.getColumnsByTables",
				Map.of("targetTableNames", tableNames));

		// Group columns by tableName and sortNo
		Map<String, Map<Long, String>> columnsByTable = allColumns.stream()
				.collect(Collectors.groupingBy(TableColumnDto::getTableName,
						Collectors.toMap(TableColumnDto::getSortNo, TableColumnDto::getColumnName, (c1, c2) -> c1)));

		// Fetch data from dynamic tables
		Map<String, List<TableColumnKeyValue>> dataByTable = new HashMap<>();
		for (String tblName : tableNames) {
			Map<Long, String> columnMap = columnsByTable.getOrDefault(tblName, Map.of());
			String keyColumn = columnMap.get(1L);
			String nameColumn = columnMap.get(2L);

			if (keyColumn == null || nameColumn == null) {
				dataByTable.put(tblName, List.of());
				continue;
			}

			// Query data from dynamic table
			List<TableColumnKeyValue> tableData = commonDao.selectList("Ui-table.getTableColumnKeyValues",
					Map.of("tableName", tblName, "cmcdDetlId", keyColumn, "cmcdDetlNm", nameColumn));

			dataByTable.put(tblName, tableData);
		}

		// Map data to AdditionalDetailDto
		additionalDetails.forEach(detail -> {
			String tblName = detail.getAttrRefTableName();
			if (tblName == null || tblName.isEmpty()) {
				return;
			}
			detail.setTableColumns(dataByTable.getOrDefault(tblName, List.of()));
		});
	}

	public void export(String tableName, String language, SearchTableDataReqDto reqDto, HttpServletResponse response) {
		List<TableColumnDto> headers = retrieveTableHeader(tableName, reqDto);
		SearchTableDataDto searchTableDataDto = buildSearchTableDto(tableName, reqDto, headers);
		List<?> datas = commonDao.selectList("Ui-table.retrieveTableData", searchTableDataDto);
		excelHelper.downloadExcel(buildExcelInput(datas, headers, tableName), response, false);
	}
	
	private ExcelInput buildExcelInput(List<?> datas, Object header, String tableName) {
		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName("table_"+tableName);
		excelInput.setSheetName("table_"+tableName);
		excelInput.setFormatDate(DateConstant.YYYYMMDD);
		excelInput.setDatas(datas);
		excelInput.setObject(header);
		return excelInput;
	}
}
