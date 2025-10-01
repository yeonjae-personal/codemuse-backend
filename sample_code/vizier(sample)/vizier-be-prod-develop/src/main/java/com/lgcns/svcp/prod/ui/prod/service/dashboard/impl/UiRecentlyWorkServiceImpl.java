package com.lgcns.svcp.prod.ui.prod.service.dashboard.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.lgcns.svcp.prod.constant.DashboardConstant;
import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.entity.DsbdRecentlyWorkEntity;
import com.lgcns.svcp.prod.entity.MiddleItemEntity;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.enums.SortDirection;
import com.lgcns.svcp.prod.mapper.DsbdRecentWorkMapper;
import com.lgcns.svcp.prod.ui.prod.dto.Sorting;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdRecentlyWorkExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.RecentWorkDetailViewDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.RecentWorkSimpleViewDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.RecentlyWorkSearchPagingDto;
import com.lgcns.svcp.prod.ui.prod.enums.LargeItemCode;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.RecentWorkExcelHelper;
import com.lgcns.svcp.prod.ui.prod.service.dashboard.UiRecentlyWorkService;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UiRecentlyWorkServiceImpl implements UiRecentlyWorkService {
	
	private final CommonDao commonDao;
	private final DsbdRecentWorkMapper recentWorkMapper; 
	private final RecentWorkExcelHelper excelHelper;
	private final MessageSource messageSource;
	
	@Override
	public Object getRecentlyWork(RecentlyWorkSearchPagingDto searchPaging, Integer page, Integer size) {
		RecentlyWorkSearchPagingDto properties = buildProperties(searchPaging, page, size);
		PageResult<DsbdRecentlyWorkEntity> pageResultRecentlyWorkEntities = commonDao.selectPagedList("ui-dsbd-recently-work-d.findProperties", properties);
		List<DsbdRecentlyWorkEntity> recentlyWorkEntities = pageResultRecentlyWorkEntities.getElements();
		if (searchPaging.getView().equals("simple")) {
			return recentlyWorkEntities.stream().map(item -> {
				RecentWorkSimpleViewDto recentWorkSimpleViewDto = recentWorkMapper.entityToSimpleViewDto(item);
				recentWorkSimpleViewDto.setWorkDate(DateUtil.formatDate(DateConstant.YYYY_MM_DD, item.getObjWorkDtm()));
				recentWorkSimpleViewDto.setWorkTypeName(getWorkType(item.getWorkType().trim()));
				Map<String, Object> maps = new HashMap<>();
				maps.put("itemCode", item.getItemCode());
				MiddleItemEntity middleItemEntity = commonDao.select("Ui-item.getMiddleItem", maps);
				recentWorkSimpleViewDto.setType(middleItemEntity.getItemCodeName());
				return recentWorkSimpleViewDto;
			}).toList();
		} else if(searchPaging.getView().equals("detail")) {
			Map<String, Object> results = new HashMap<>();
			String batchDate = null;
			List<RecentWorkDetailViewDto> detailViewDtos = new ArrayList<>();
			if (recentlyWorkEntities != null && !recentlyWorkEntities.isEmpty()) {
				batchDate = DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM, recentlyWorkEntities.get(0).getBatchRunDtm());
				detailViewDtos = convertListRecentWorkEntityToDetailDto(recentlyWorkEntities);
			}
			results.put("dateBatch", batchDate);
			results.put("data", new PageResult<RecentWorkDetailViewDto>(detailViewDtos, page, size, pageResultRecentlyWorkEntities.getTotalElements()));
			return results;
		}
		return null;
	}

	private List<RecentWorkDetailViewDto> convertListRecentWorkEntityToDetailDto(List<DsbdRecentlyWorkEntity> entities) {
		return entities.stream().map(item -> {
			RecentWorkDetailViewDto dto = recentWorkMapper.entityToDetailViewDto(item);
			dto.setWorkDate(DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM, item.getObjWorkDtm()));
			dto.setWorkTypeName(getWorkType(item.getWorkType().trim()));
			Map<String, Object> maps = new HashMap<>();
			maps.put("itemCode", item.getItemCode());
			MiddleItemEntity middleItemEntity = commonDao.select("Ui-item.getMiddleItem", maps);
			dto.setType(middleItemEntity.getItemCodeName());
			return dto;
		}).toList();
	}

	private String getWorkType(String workType) {
		return switch (workType) {
			case "01" -> messageSource.getMessage("dashboard.recentlywork.excel.data.created", null, LocaleContextHolder.getLocale());
			case "02" -> messageSource.getMessage("dashboard.recentlywork.excel.data.changed.attribute", null, LocaleContextHolder.getLocale());
			case "03" -> messageSource.getMessage("dashboard.recentlywork.excel.data.changed.structure", null, LocaleContextHolder.getLocale());
			case "04" -> messageSource.getMessage("dashboard.recentlywork.excel.data.ended", null, LocaleContextHolder.getLocale());
			default -> "";
		};
	}

	private RecentlyWorkSearchPagingDto buildProperties(RecentlyWorkSearchPagingDto properties, Integer page, Integer size) {
		properties.setSort("obj_work_dtm "+SortDirection.desc);
		if (properties.getView().equals("simple")) {
			properties.setSize(4);
			properties.setPage(1);
		} else if(properties.getView().equals("detail")) {
			properties.setCategory(getLargeItemName(properties.getCategory()));
			if (properties.getSearchBy() != null) {
				String value = properties.getSearchValue() != null ? properties.getSearchValue().trim().toLowerCase() : null;
				switch (properties.getSearchBy()) {
            		case "object-name" -> properties.setObjName(value);
            		case "object-code" -> properties.setObjCode(value);
				}
			}
			properties.setSize(size);
			properties.setPage(page);
		}
		return properties;
	}

	@Override
	public void exportExcel(String category, String type, String searchBy, String searchValue, Sorting sorting, HttpServletResponse response) {
		Map<String, Object> params = buildQueryParamExportExcelRecentWork(category, type, searchBy, searchValue, sorting);
		List<DsbdRecentlyWorkEntity> entities = commonDao.selectList("ui-dsbd-recently-work-d.findProperties", params);
		excelHelper.of(DsbdRecentlyWorkExportDto.class).downloadExcel(buildExcelInput(entities), response, true);
	}

	private ExcelInput buildExcelInput(List<DsbdRecentlyWorkEntity> entities) {
		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName(DashboardConstant.RECENTLY_WORK_ITEM_NAME);
		excelInput.setSheetName(DashboardConstant.RECENTLY_WORK_ITEM_NAME);
		excelInput.setFormatDate(DateConstant.YYYYMMDD);
		List<DsbdRecentlyWorkExportDto> datas = buildDataExcelExport(entities);
		excelInput.setDatas(datas);
		Map<String, Object> maps = new HashMap<>();
		maps.put("datas", entities);
		String batchDate = null;
		if (entities != null && !entities.isEmpty()) {
            batchDate = DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM, entities.get(0).getBatchRunDtm());
		}
		maps.put("batchDate", batchDate);
		excelInput.setObject(maps);
		return excelInput;
	}

	private List<DsbdRecentlyWorkExportDto> buildDataExcelExport(List<DsbdRecentlyWorkEntity> results) {
		return results.stream().map(item -> {
			DsbdRecentlyWorkExportDto dto = recentWorkMapper.entityToExportExcelDto(item);
			dto.setWorkDate(DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM, item.getObjWorkDtm()));
			dto.setWorkTypeName(getWorkType(item.getWorkType().trim()));
			Map<String, Object> maps = new HashMap<>();
			maps.put("itemCode", item.getItemCode());
			MiddleItemEntity middleItemEntity = commonDao.select("Ui-item.getMiddleItem", maps);
			dto.setType(middleItemEntity.getItemCodeName());
			return dto;
		}).toList();
	}

	private Map<String, Object> buildQueryParamExportExcelRecentWork(String category, String type, String searchBy,
			String searchValue, Sorting sorting) {
		Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("category", getLargeItemName(category));
        if (searchBy != null) {
			String value = searchValue != null ? searchValue.trim().toLowerCase() : null;
			switch (searchBy) {
        		case "object-name" -> params.put("objName", value);
        		case "object-code" -> params.put("objCode", value);
			}
		}
        params.put("sorting", sorting.getProperties());
        return params;
	}
	
	private String getLargeItemName(String code) {
		if (code == null) {
			return null;
		}
		return LargeItemCode.valueOf(code).getValue();
	}
}
