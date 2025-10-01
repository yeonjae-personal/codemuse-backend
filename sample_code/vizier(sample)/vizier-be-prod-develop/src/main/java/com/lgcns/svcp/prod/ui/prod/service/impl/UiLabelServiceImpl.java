package com.lgcns.svcp.prod.ui.prod.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lgcns.svcp.prod.context.RequestContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.entity.MultiLangLabelEntity;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.mapper.LabelMapper;
import com.lgcns.svcp.prod.ui.prod.dto.label.LabelExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.label.LabelItemDto;
import com.lgcns.svcp.prod.ui.prod.dto.label.LabelItemUpdateDto;
import com.lgcns.svcp.prod.ui.prod.dto.label.LabelSearchPagingDto;
import com.lgcns.svcp.prod.ui.prod.dto.label.MultiLangLabelDto;
import com.lgcns.svcp.prod.ui.prod.service.UiLabelService;
import com.lgcns.svcp.prod.util.FileUtil;
import com.lgcns.svcp.prod.util.excel.ExcelHelper;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.excel.ExcelReader;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UiLabelServiceImpl implements UiLabelService {

	private final CommonDao commonDao;
	private final LabelMapper labelMapper;
	private final ExcelHelper excelHelper;

	@Override
	public PageResult<MultiLangLabelDto> findAll(LabelSearchPagingDto params) {
		LabelSearchPagingDto properties = buildProperties(params);
		PageResult<MultiLangLabelEntity> pageResult = commonDao.selectPagedList("ui-multi-lang-label-m.findByProperties", properties);
		List<MultiLangLabelDto> dtos = convertListEntityToDto(pageResult.getElements());
		return new PageResult<MultiLangLabelDto>(dtos, pageResult.getPage(), pageResult.getSize(), pageResult.getTotalElements());
	}

	private List<MultiLangLabelDto> convertListEntityToDto(List<MultiLangLabelEntity> entities) {
		return entities.stream().map(item -> {
			MultiLangLabelDto dto = labelMapper.entityToDto(item);
			List<LabelItemDto> items = commonDao.selectList("ui-multi-lang-label-m.findByLabelGetAll", item.getLabelId());
			dto.setItems(items);
			return dto;
		}).toList();
	}

	private LabelSearchPagingDto buildProperties(LabelSearchPagingDto params) {
		if (StringUtils.isNotBlank(params.getType()) && StringUtils.isNotBlank(params.getValue())) {
			switch (params.getType()) {
        		case "code" -> params.setLabelId(params.getValue().trim().toLowerCase());
        		case "name" -> params.setLabelName(params.getValue().trim().toLowerCase());
			}
		}
		return params;
	}

	@Override
	@Transactional
	public void save(MultiLangLabelDto input) {
		List<LabelItemDto> newLabelItems = input.getItems();
		List<String> newLabelStrs = newLabelItems.stream().map(LabelItemDto::getLangCode).toList();
		if (!newLabelStrs.contains("en")) {
			throw new BusinessException("Required language EN");
		}
		if (StringUtils.isNotBlank(input.getLabelId())) {
			List<LabelItemDto> existsLabelItems = commonDao.selectList("ui-multi-lang-label-m.findByLabelId",
					input.getLabelId());
			List<String> existLabelStrs = existsLabelItems.stream().map(LabelItemDto::getLangCode).toList();
			List<LabelItemDto> labelAfters = new ArrayList<>();
			for (LabelItemDto labelItemDto : newLabelItems) {
				if (!existLabelStrs.contains(labelItemDto.getLangCode())) {
					LabelItemUpdateDto updateLabel = convertToUpdateLabelItem(labelItemDto, input.getLabelId());
					commonDao.insert("ui-multi-lang-label-m.insert", updateLabel);
				} else {
					labelAfters.add(labelItemDto);
				}
			}
			for (LabelItemDto labelItemDto : existsLabelItems) {
				if (!newLabelStrs.contains(labelItemDto.getLangCode())) {
					Map<String, Object> mapDeletes = new HashMap<>();
					mapDeletes.put("labelId", input.getLabelId());
					mapDeletes.put("langCode", labelItemDto.getLangCode());
					mapDeletes.put("regionCode", labelItemDto.getRegionCode());
					commonDao.delete("ui-multi-lang-label-m.deleteInUpdate", mapDeletes);
				}
			}
			List<LabelItemUpdateDto> updateLabels = labelAfters.stream()
					.map(item -> convertToUpdateLabelItem(item, input.getLabelId())).toList();
			commonDao.batchUpdate("ui-multi-lang-label-m.update", updateLabels);
		} else {
			String newLabelId = createLabelId();
			List<LabelItemUpdateDto> newsLabels = newLabelItems.stream()
					.map(item -> convertToUpdateLabelItem(item, newLabelId)).toList();
			commonDao.batchInsert("ui-multi-lang-label-m.insert", newsLabels);
		}
	}

	private String createLabelId() {
		String resultStr = "00000001";
		List<String> labelIds = commonDao.selectList("ui-multi-lang-label-m.findLabelIds");
		if (labelIds != null && !labelIds.isEmpty()) {
			Integer result = Integer.parseInt(labelIds.get(0).substring(2)) + 1;
			if (result >= 1 && result < 10) {
				resultStr = "0000000" + result.toString();
			} else if (result >= 10 && result < 100) {
				resultStr = "000000" + result.toString();
			} else if (result >= 100 && result < 1000) {
				resultStr = "00000" + result.toString();
			} else if (result >= 1000 && result < 10000) {
				resultStr = "0000" + result.toString();
			} else if (result >= 10000 && result < 100000) {
				resultStr = "000" + result.toString();
			} else if (result >= 100000 && result < 1000000) {
				resultStr = "00" + result.toString();
			} else if (result >= 1000000 && result < 10000000) {
				resultStr = "0" + result.toString();
			} else if (result >= 10000000 && result < 100000000) {
				resultStr = result.toString();
			} else if (result >= 100000000) {
				throw new BusinessException("LabelId is limit");
			}
		}
		resultStr = "LB" + resultStr;
		RequestContextHolder.setCode(resultStr);
		return resultStr;
	}

	private LabelItemUpdateDto convertToUpdateLabelItem(LabelItemDto item, String labelId) {
		LabelItemUpdateDto result = new LabelItemUpdateDto();
		result.setLabelId(labelId);
		result.setLabelName(item.getLabelName());
		result.setRegionCode(item.getRegionCode());
		result.setLangCode(item.getLangCode());
		result.setLabelDscr(item.getLabelDscr());
		result.setLabelType("Additional");
		return result;
	}

	@Override
	@Transactional
	public void delete(String labelId) {
		int isUseByAttribute = (int) commonDao.select("ui-add-attr-header-m.countByLabelId", labelId);
		if (isUseByAttribute > 0) {
			throw new BusinessException("Label is used by attribute!");
		} else {
			commonDao.delete("ui-multi-lang-label-m.delete", labelId);
		}
	}
	
	@Override
	public List<MultiLangLabelDto> findLanguageI18n() {
		List<MultiLangLabelDto> results = new ArrayList<>();
		List<MultiLangLabelEntity> multiLangLabelEntities = commonDao
				.selectList("ui-multi-lang-label-m.findLanguageI18n");
		Map<String, List<MultiLangLabelEntity>> mapEntities = multiLangLabelEntities.stream()
				.collect(Collectors.groupingBy(MultiLangLabelEntity::getLabelId));
		for (Map.Entry<String, List<MultiLangLabelEntity>> entry : mapEntities.entrySet()) {
			MultiLangLabelDto dto = new MultiLangLabelDto();
			dto.setLabelId(entry.getKey());
			List<LabelItemDto> labelItemDtos = new ArrayList<>();
			for (MultiLangLabelEntity entity : entry.getValue()) {
				LabelItemDto itemDto = new LabelItemDto();
				itemDto.setLabelName(entity.getLabelName());
				itemDto.setLangCode(entity.getLangCode());
				itemDto.setLabelDscr(entity.getLabelDscr());
				labelItemDtos.add(itemDto);
			}
			dto.setItems(labelItemDtos);
			results.add(dto);
		}
		return results;
	}

	@Override
	@Transactional
	public void exportExcel(String type, String value, String language, HttpServletResponse response) {
		Map<String, Object> params = buildQueryParamExportLabel(type, value, language);
		List<MultiLangLabelEntity> entities = commonDao.selectList("ui-multi-lang-label-m.findByProperties", params);
		excelHelper.of(LabelExportDto.class).downloadExcel(buildExcelInput(entities), response, false);
	}

	private ExcelInput buildExcelInput(List<MultiLangLabelEntity> entities) {
		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName("label");
		excelInput.setSheetName("Label");
		excelInput.setFormatDate(DateConstant.YYYYMMDD);
		List<LabelExportDto> datas = buildDataExcelExport(entities);
		excelInput.setDatas(datas);
		return excelInput;
	}

	private List<LabelExportDto> buildDataExcelExport(List<MultiLangLabelEntity> entities) {
		return entities.stream().map(item -> {
			LabelExportDto dto = new LabelExportDto();
			dto.setLabelName(item.getLabelName());
			dto.setLangCode(item.getLangCode());
			dto.setRegionCode(item.getRegionCode());
			dto.setLabelCode(item.getLabelId());
			dto.setLabelDscr(item.getLabelDscr());
			return dto;
		}).toList();
	}

	private Map<String, Object> buildQueryParamExportLabel(String type, String value, String language) {
		Map<String, Object> result = new HashMap<>();
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
			switch (type) {
			case "code" -> result.put("labelId", value.trim().toLowerCase());
			case "name" -> result.put("labelName", value.trim().toLowerCase());
			}
		}
		return result;
	}

	@Override
	@Transactional
	public void importExcel(HttpServletRequest request) throws IOException, ServletException {
		Part filePart = request.getPart("file");
		InputStream inputStream = filePart.getInputStream();
		String extendFile = FileUtil.getExtension(filePart.getSubmittedFileName());
		ExcelReader excelReader = new ExcelReader(inputStream, extendFile);
		importExcelBySheet(excelReader);
	}

	private void importExcelBySheet(ExcelReader excelReader) {
		Sheet sheet1 = excelReader.getWorkBook().getSheet("Label_Template");
		Sheet sheet2 = excelReader.getWorkBook().getSheet("Sheet");
		if (sheet1 != null) {
			String[] fieldNames = new String[] { "langCode", "regionCode", "labelName", "labelDscr" };
			List<LabelItemUpdateDto> labels = excelReader.getList(LabelItemUpdateDto.class, fieldNames,
					"Label_Template");
			List<String> langCodes = labels.stream().map(LabelItemUpdateDto::getLangCode).toList();
			if (!langCodes.contains("en")) {
				throw new BusinessException("Lang EN is missing!");
			}
			String labelId = null;
			for (LabelItemUpdateDto item : labels) {
				if (!StringUtils.isNotBlank(item.getLangCode())) {
					throw new BusinessException("Label code is empty!");
				} else if (!StringUtils.isNotBlank(item.getLabelName())) {
					throw new BusinessException("Label Name is empty!");
				} else if (!StringUtils.isNotBlank(item.getRegionCode())) {
					throw new BusinessException("Region code is empty!");
				} else {
					if (item.getLangCode().equals("en")) {
						if (labelId == null) {
							labelId = createLabelId();
						} else {
							Integer result = Integer.parseInt(labelId.substring(2)) + 1;
							if (result >= 1 && result < 10) {
								labelId = "LB0000000" + result.toString();
							} else if (result >= 10 && result < 100) {
								labelId = "LB000000" + result.toString();
							} else if (result >= 100 && result < 1000) {
								labelId = "LB00000" + result.toString();
							} else if (result >= 1000 && result < 10000) {
								labelId = "LB0000" + result.toString();
							} else if (result >= 10000 && result < 100000) {
								labelId = "LB000" + result.toString();
							} else if (result >= 100000 && result < 1000000) {
								labelId = "LB00" + result.toString();
							} else if (result >= 1000000 && result < 10000000) {
								labelId = "LB0" + result.toString();
							} else if (result >= 10000000 && result < 100000000) {
								labelId = "LB" + result.toString();
							} else if (result >= 100000000) {
								throw new BusinessException("LabelId is limit");
							}
						}
					}
					item.setLabelId(labelId);
					item.setLabelType("Additional");
				}
			}
			if (labels == null || labels.isEmpty()) {
				throw new BusinessException("File is empty!");
			} else {
				commonDao.batchInsert("ui-multi-lang-label-m.insert", labels);
			}
		} else if (sheet2 != null) {
			String[] fieldNames = new String[] { "labelId", "langCode", "regionCode", "labelName" };
			List<LabelItemUpdateDto> labels = excelReader.getList(LabelItemUpdateDto.class, fieldNames, "Sheet");
			List<LabelItemUpdateDto> labelInserts = new ArrayList<>();
			List<LabelItemUpdateDto> labelUpdates = new ArrayList<>();
			for (LabelItemUpdateDto dto : labels) {
				int existLabel = commonDao.select("ui-multi-lang-label-m.checkLabelExistImportExcel", dto);
				if (existLabel > 0) {
					labelUpdates.add(dto);
				} else {
					if (StringUtils.isNotBlank(dto.getLabelId()) && StringUtils.isNotBlank(dto.getLangCode())
							&& StringUtils.isNotBlank(dto.getRegionCode())
							&& StringUtils.isNotBlank(dto.getLabelName())) {
						labelInserts.add(dto);
					}
				}
			}
			if (labels == null || labels.isEmpty()) {
				throw new BusinessException("File is empty!");
			} else {
				if (labelInserts != null && !labelInserts.isEmpty()) {
					commonDao.batchInsert("ui-multi-lang-label-m.insertLabelSystem", labelInserts);
				} else if (labelUpdates != null && !labelUpdates.isEmpty()) {
					commonDao.batchUpdate("ui-multi-lang-label-m.updateFromExcel", labelUpdates);
				}
			}
		} else {
			throw new BusinessException("sheet is not exists!");
		}
	}

	@Override
	public List<LabelItemDto> getAllLanguage() {
		return commonDao.selectList("ui-lang-m.findAll");
	}

}
