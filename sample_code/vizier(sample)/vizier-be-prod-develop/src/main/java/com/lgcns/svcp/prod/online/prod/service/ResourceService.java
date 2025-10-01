package com.lgcns.svcp.prod.online.prod.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto;
import com.lgcns.svcp.prod.online.prod.dto.component.characteristic.SalesDto;
import com.lgcns.svcp.prod.online.prod.dto.resource.BillingElementDto;
import com.lgcns.svcp.prod.online.prod.dto.resource.RatingElementDto;
import com.lgcns.svcp.prod.online.prod.dto.resource.ServiceElementDto;
import com.lgcns.svcp.prod.online.prod.util.MapUtil;

import com.lgcns.svcp.prod.dataaccess.CommonDao;

@Component
public class ResourceService {
	@Autowired
	private CommonDao commonDao;

	public List<?> retrieveSalesList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("SI");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		SalesDto salesDto = new SalesDto();
		salesDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Characteristic.selectSalesList", salesDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public List<?> retrieveSales(String inputCode) {

		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("SI");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		SalesDto salesDto = new SalesDto();
		salesDto.setSalesCode(inputCode);

		salesDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Characteristic.selectSales", salesDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}
	
	public List<?> retrieveBillingElementList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("BE");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		BillingElementDto billingElementDto = new BillingElementDto();
		billingElementDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Resource.retrieveBillingElementList", billingElementDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}
	
	public List<?> retrieveBillingElement(String inptuCode) {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("BE");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		BillingElementDto billingElementDto = new BillingElementDto();
		billingElementDto.setObjCode(inptuCode);

		billingElementDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Resource.retrieveBillingElement", billingElementDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}
	
	public List<?> retrieveRatingElementList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("RE");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		RatingElementDto ratingElementDto = new RatingElementDto();
		ratingElementDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Resource.retrieveRatingElementList", ratingElementDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}
	
	public List<?> retrieveRatingElement(String inptuCode) {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("RE");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		RatingElementDto ratingElementDto = new RatingElementDto();
		ratingElementDto.setObjCode(inptuCode);

		ratingElementDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Resource.retrieveRatingElement", ratingElementDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}
	
	public List<?> retrieveServiceElementList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("SE");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		ServiceElementDto serviceElementDto = new ServiceElementDto();
		serviceElementDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Resource.retrieveServiceElementList", serviceElementDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}
	
	public List<?> retrieveServiceElement(String inptuCode) {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("SE");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		ServiceElementDto serviceElementDto = new ServiceElementDto();
		serviceElementDto.setObjCode(inptuCode);

		serviceElementDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Resource.retrieveServiceElement", serviceElementDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}	
}
