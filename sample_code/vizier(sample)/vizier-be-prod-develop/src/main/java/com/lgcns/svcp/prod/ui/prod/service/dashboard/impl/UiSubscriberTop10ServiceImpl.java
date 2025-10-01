package com.lgcns.svcp.prod.ui.prod.service.dashboard.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.lgcns.svcp.prod.constant.DashboardConstant;
import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.entity.DsbdOfferSubCntEntity;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.enums.SortDirection;
import com.lgcns.svcp.prod.mapper.DsbdOfferSubCntMapper;
import com.lgcns.svcp.prod.ui.prod.dto.Sorting;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdOfferSubCntDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdOfferSubCntExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.SubscribeTop10SearchPagingDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.SubscribeTopSimpleViewResponse;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.SubscriberTop10ExcelHelper;
import com.lgcns.svcp.prod.ui.prod.service.dashboard.UiSubscriberTop10Service;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UiSubscriberTop10ServiceImpl implements UiSubscriberTop10Service {
	
	private final CommonDao commonDao;
	
	private final MessageSource messageSource;
	
	private final DsbdOfferSubCntMapper dsbdOfferSubCntMapper;
	
	private final SubscriberTop10ExcelHelper excelHelper;
	
	@Override
	public Object getSubscribeTop10(SubscribeTop10SearchPagingDto searchPaging, Integer page, Integer size) {
		SubscribeTop10SearchPagingDto properties = buildProperties(searchPaging, page, size);
		PageResult<DsbdOfferSubCntEntity> pageResultCntEntities = commonDao.selectPagedList("ui-dsbd-offer-sub-cnt.findProperties", properties);
		List<DsbdOfferSubCntEntity> cntEntities = pageResultCntEntities.getElements();
		if (searchPaging.getView().equals("simple")) {
			return cntEntities.stream().map(item -> {
				SubscribeTopSimpleViewResponse simpleViewResponse = dsbdOfferSubCntMapper.entityToSimpleViewResponse(item);
				simpleViewResponse.setStatus(item.getSaleValidEndDtm() != null ? true : false);
				return simpleViewResponse;
			}).toList();
		} else if(searchPaging.getView().equals("detail")) {
			Map<String, Object> results = new HashMap<>();
			String batchDate = null;
			List<DsbdOfferSubCntDto> cntDtos = new ArrayList<>();
			if (cntEntities != null && !cntEntities.isEmpty()) {
				batchDate = DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM, cntEntities.get(0).getBatchRunDtm());
				cntDtos = convertListOfferSubCntEntityToDto(cntEntities);
			}
			results.put("dateBatch", batchDate);
			if (searchPaging.getMax() == null) {
				results.put("data", new PageResult<DsbdOfferSubCntDto>(cntDtos, page, size, pageResultCntEntities.getTotalElements()));
			} else {
				results.put("data", cntDtos);
			}
			return results;
		}
		return null;
	}
	
	@Override
	public void exportExcel(String type, String searchBy, String searchValue, Sorting sorting, HttpServletResponse response) {
		Map<String, Object> params = buildQueryParamExportExcelSubTop10(type, searchBy, searchValue, sorting);
		List<DsbdOfferSubCntEntity> entities = commonDao.selectList("ui-dsbd-offer-sub-cnt.findProperties", params);
		excelHelper.of(DsbdOfferSubCntExportDto.class).downloadExcel(buildExcelInput(entities), response, true);
	}
	
	private SubscribeTop10SearchPagingDto buildProperties(SubscribeTop10SearchPagingDto properties, Integer page, Integer size) {
		properties.setSort("sub_cnt "+SortDirection.desc);
		if (properties.getView().equals("simple")) {
			properties.setPage(1);
			properties.setSize(properties.getMax());
		} else if(properties.getView().equals("detail")) {
			if (properties.getSearchBy() != null) {
				String value = properties.getSearchValue() != null ? properties.getSearchValue().trim().toLowerCase() : null;
				switch (properties.getSearchBy()) {
            		case "offer-name" -> properties.setOfferName(value);
            		case "offer-code" -> properties.setOfferCode(value);
				}
			}
			if (properties.getMax() != null) {
				properties.setPage(1);
				properties.setSize(properties.getMax());
			} else {		
				properties.setPage(page);
				properties.setSize(size);
			}
		}
		return properties;
	}
	
	private Map<String, Object> buildQueryParamExportExcelSubTop10(String type, String searchBy, String searchValue, Sorting sorting) {
		Map<String, Object> params = new HashMap<>();
        params.put("offerTypeName", type);
        if (searchBy != null) {
			String value = searchValue != null ? searchValue.trim().toLowerCase() : null;
			switch (searchBy) {
        		case "offer-name" -> params.put("offerName", value);
        		case "offer-code" -> params.put("offerCode", value);
			}
		}
        params.put("sorting", sorting.getProperties());
        return params;
	}
	
	private List<DsbdOfferSubCntExportDto> buildDataExcelExport(List<DsbdOfferSubCntEntity> results) {
		return results.stream().map(item -> {
			DsbdOfferSubCntExportDto dto = dsbdOfferSubCntMapper.entityToExportDto(item);
			dto.setStartDate(DateUtil.formatDate(DateConstant.YYYY_MM_DD, item.getSaleValidStartDtm()));
			Date saleValidEndDate = null;
			String status = null;
			if (item.getSaleValidEndDtm() != null) {
				saleValidEndDate = item.getSaleValidEndDtm();
				dto.setEndDate(DateUtil.formatDate(DateConstant.YYYY_MM_DD, item.getSaleValidEndDtm()));
				status = messageSource.getMessage("dashboard.subscribertop10.excel.header.status.endofsale", null, LocaleContextHolder.getLocale());
			} else {
				saleValidEndDate = new Date();
				status = messageSource.getMessage("dashboard.subscribertop10.excel.header.status.onsale", null, LocaleContextHolder.getLocale());
			}
			dto.setDuration(DateUtil.getDuration(item.getSaleValidStartDtm(), saleValidEndDate));
			dto.setStatus(status);
			return dto;
		}).toList();
	}
	
	private ExcelInput buildExcelInput(List<DsbdOfferSubCntEntity> entities) {
		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName(DashboardConstant.SUBSCRIBE_TOP_10_ITEM_NAME);
		excelInput.setSheetName(DashboardConstant.SUBSCRIBE_TOP_10_ITEM_NAME);
		excelInput.setFormatDate(DateConstant.YYYYMMDD);
		List<DsbdOfferSubCntExportDto> datas = buildDataExcelExport(entities);
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
	
	private List<DsbdOfferSubCntDto> convertListOfferSubCntEntityToDto(List<DsbdOfferSubCntEntity> entities) {
		return entities.stream().map(item -> {
			DsbdOfferSubCntDto dto = dsbdOfferSubCntMapper.entityToDto(item);
			dto.setStartDate(DateUtil.formatDate(DateConstant.YYYY_MM_DD, item.getSaleValidStartDtm()));
			Date saleValidEndDate = null;
			boolean status = false;
			if (item.getSaleValidEndDtm() != null) {
				status = true;
				dto.setEndDate(DateUtil.formatDate(DateConstant.YYYY_MM_DD, item.getSaleValidEndDtm()));
				saleValidEndDate = item.getSaleValidEndDtm();
			} else {
				saleValidEndDate = new Date();
			}
			dto.setDuration(DateUtil.getDuration(item.getSaleValidStartDtm(), saleValidEndDate));
			dto.setStatus(status);
			return dto;
		}).toList();
	}
}
