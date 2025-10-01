package com.lgcns.svcp.prod.online.prod.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto;
import com.lgcns.svcp.prod.online.prod.dto.common.Item;
import com.lgcns.svcp.prod.online.prod.dto.component.characteristic.BillingDto;
import com.lgcns.svcp.prod.online.prod.dto.component.characteristic.SalesDto;
import com.lgcns.svcp.prod.online.prod.dto.component.characteristic.DiscountConfigurationDto;
import com.lgcns.svcp.prod.online.prod.dto.component.benefit.AllowanceAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.benefit.AllowanceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.benefit.RatingDiscountAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.benefit.RatingDiscountDto;
import com.lgcns.svcp.prod.online.prod.dto.component.price.BaseFeeAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.price.BaseFeeDto;
import com.lgcns.svcp.prod.online.prod.dto.component.price.UsageFeeAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.price.UsageFeeDto;
import com.lgcns.svcp.prod.online.prod.dto.component.service.AdditionalAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.service.AdditionalDto;
import com.lgcns.svcp.prod.online.prod.dto.component.service.MessageAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.service.MessageDto;
import com.lgcns.svcp.prod.online.prod.dto.component.service.VoiceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.service.VoiceAndResourceDto;
import com.lgcns.svcp.prod.online.prod.dto.component.price.DiscountRateDto;
import com.lgcns.svcp.prod.online.prod.util.MapUtil;

import com.lgcns.svcp.prod.dataaccess.CommonDao;

@Component
public class ComponentService {
	@Autowired
	private CommonDao commonDao;

	public List<?> retrieveBaseFeeList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("RC");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		BaseFeeDto baseFeeDto = new BaseFeeDto();
		baseFeeDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Price.selectBaseFeeList", baseFeeDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public BaseFeeAndResourceDto retrieveBaseFeeAndResource(String inputCode) {

		AdditionalColumnsDto request = new AdditionalColumnsDto();

		request.setItemCode("RC");

		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		BaseFeeDto baseFeeDto = new BaseFeeDto();
		baseFeeDto.setBsfCode(inputCode);

		baseFeeDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Price.selectBaseFee", baseFeeDto);

		List<Map<String, Object>> baseFee = resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());

		List<Item> resourceList = commonDao.selectList("Relation.selectResourceList", inputCode);

		Map<String, List<String>> groupResourceList = resourceList.stream()
				.collect(Collectors.groupingBy(Item::getItemTypeNm,
						Collectors.mapping(Item::getUuid, Collectors.toList())));

		BaseFeeAndResourceDto result = new BaseFeeAndResourceDto();
		result.setBaseFee(baseFee);
		result.setResourceList(groupResourceList);

		return result;
	}

	public List<?> retrieveUsageFeeList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("UC");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		UsageFeeDto usageFeeDto = new UsageFeeDto();
		usageFeeDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Price.selectUsageFeeList", usageFeeDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public UsageFeeAndResourceDto retrieveUsageFeeAndResource(String inputCode) {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("UC");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		UsageFeeDto usageFeeDto = new UsageFeeDto();
		usageFeeDto.setUsfCode(inputCode);

		usageFeeDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Price.selectUsageFee", usageFeeDto);

		List<Map<String, Object>> usageFee = resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());

		List<Item> resourceList = commonDao.selectList("Relation.selectResourceList", inputCode);

		Map<String, List<String>> groupResourceList = resourceList.stream()
				.collect(Collectors.groupingBy(Item::getItemTypeNm,
						Collectors.mapping(Item::getUuid, Collectors.toList())));

		UsageFeeAndResourceDto result = new UsageFeeAndResourceDto();
		result.setUsageFee(usageFee);
		result.setResourceList(groupResourceList);

		return result;
	}

	public List<?> retrieveDiscountRateList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("DR");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		DiscountRateDto discountRateDto = new DiscountRateDto();
		discountRateDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Price.selectDiscountRateList", discountRateDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public List<?> retrieveDiscountRate(String inputCode) {

		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("DR");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		DiscountRateDto discountRateDto = new DiscountRateDto();
		discountRateDto.setDcRateCode(inputCode);

		discountRateDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Price.selectDiscountRate", discountRateDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public List<?> retrieveAllowanceList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("AW");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);
		
		AllowanceDto allowanceDto = new AllowanceDto();
		allowanceDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Benefit.selectAllowanceList", allowanceDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public AllowanceAndResourceDto retrieveAllowanceAndResource(String inputCode) {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("AW");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		AllowanceDto allowanceDto = new AllowanceDto();
		allowanceDto.setAlwncCode(inputCode);

		allowanceDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Benefit.selectAllowance", allowanceDto);

		List<Map<String, Object>> allowance = resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());

		List<Item> resourceList = commonDao.selectList("Relation.selectResourceList", inputCode);

		Map<String, List<String>> groupResourceList = resourceList.stream()
				.collect(Collectors.groupingBy(Item::getItemTypeNm,
						Collectors.mapping(Item::getUuid, Collectors.toList())));

		AllowanceAndResourceDto result = new AllowanceAndResourceDto();
		result.setAllowance(allowance);
		result.setResourceList(groupResourceList);

		return result;
	}
	
	public List<?> retrieveRatingDiscountList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("RD");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);
		
		RatingDiscountDto ratingDiscountDto = new RatingDiscountDto();
		ratingDiscountDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Benefit.selectRatingDiscountList", ratingDiscountDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public RatingDiscountAndResourceDto retrieveRatingDiscountAndResource(String inputCode) {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("RD");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		RatingDiscountDto ratingDiscountDto = new RatingDiscountDto();
		ratingDiscountDto.setRtngDcCode(inputCode);

		ratingDiscountDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Benefit.selectRatingDiscount", ratingDiscountDto);

		List<Map<String, Object>> ratingDiscount = resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());

		List<Item> resourceList = commonDao.selectList("Relation.selectResourceList", inputCode);

		Map<String, List<String>> groupResourceList = resourceList.stream()
				.collect(Collectors.groupingBy(Item::getItemTypeNm,
						Collectors.mapping(Item::getUuid, Collectors.toList())));

		RatingDiscountAndResourceDto result = new RatingDiscountAndResourceDto();
		result.setRatingDiscount(ratingDiscount);
		result.setResourceList(groupResourceList);

		return result;
	}

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

	public List<?> retrieveBillingList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("BI");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		BillingDto billingDto = new BillingDto();
		billingDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Characteristic.selectBillingList", billingDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public List<?> retrieveBilling(String inputCode) {

		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("BI");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		BillingDto billingDto = new BillingDto();
		billingDto.setBlngCode(inputCode);

		billingDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Characteristic.selectBilling", billingDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}
	
	public List<?> retrieveDiscountConfigurationList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("DI");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		DiscountConfigurationDto discountConfigurationDto = new DiscountConfigurationDto();
		discountConfigurationDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Characteristic.selectDiscountConfigList", discountConfigurationDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public List<?> retrieveDiscountConfiguration(String inputCode) {

		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("DI");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		DiscountConfigurationDto discountConfigurationDto = new DiscountConfigurationDto();
		discountConfigurationDto.setDcCfgrtCode(inputCode);

		discountConfigurationDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Characteristic.selectDiscountConfig", discountConfigurationDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public List<?> retrieveAdditionalList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("AD");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);
		//추가속성의 additional과 service중에 additional의 의미가 혼동되지 않을까?
		AdditionalDto additionalDto = new AdditionalDto();
		additionalDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Service.selectAdditionalList", additionalDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public AdditionalAndResourceDto retrieveAdditionalAndResource(String inputCode) {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("AD");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		AdditionalDto additionalDto = new AdditionalDto();
		additionalDto.setAddSvcCode(inputCode);

		additionalDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Service.selectAdditional", additionalDto);

		List<Map<String, Object>> additional = resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
		
		
		List<Item> resourceList = commonDao.selectList("Relation.selectResourceList", inputCode);

		Map<String, List<String>> groupResourceList = resourceList.stream()
				.collect(Collectors.groupingBy(Item::getItemTypeNm,
						Collectors.mapping(Item::getUuid, Collectors.toList())));

		AdditionalAndResourceDto result = new AdditionalAndResourceDto();
		result.setAdditional(additional);
		if(!additional.isEmpty()) result.setResourceList(groupResourceList);

		return result;
	}
	
	public List<?> retrieveMessageList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("AD");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);
		//추가속성의 additional과 service중에 additional의 의미가 혼동되지 않을까?
		MessageDto messageDto = new MessageDto();
		messageDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Service.selectMessageList", messageDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public MessageAndResourceDto retrieveMessageAndResource(String inputCode) {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("MS");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		MessageDto messageDto = new MessageDto();
		messageDto.setMsgSvcCode(inputCode);
		messageDto.setAdditionalColumns(additionalColumns);
		
		List<Map<String, Object>> resultList = commonDao.selectList("Service.selectMessage", messageDto);
		List<Map<String, Object>> message = resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());

		List<Item> resourceList = commonDao.selectList("Relation.selectResourceList", inputCode);

		Map<String, List<String>> groupResourceList = resourceList.stream()
				.collect(Collectors.groupingBy(Item::getItemTypeNm,
						Collectors.mapping(Item::getUuid, Collectors.toList())));

		MessageAndResourceDto result = new MessageAndResourceDto();
		result.setMessage(message);
		if(!message.isEmpty()) result.setResourceList(groupResourceList);

		return result;
	}
	
	public List<?> retrieveVoiceList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("VO");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);
		//추가속성의 additional과 service중에 additional의 의미가 혼동되지 않을까?
		VoiceDto voiceDto = new VoiceDto();
		voiceDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Service.selectVoiceList", voiceDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public VoiceAndResourceDto retrieveVoiceAndResource(String inputCode) {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("VO");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		VoiceDto voiceDto = new VoiceDto();
		voiceDto.setVicSvcCode(inputCode);

		voiceDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Service.selectVoice", voiceDto);

		List<Map<String, Object>> voice = resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());

		List<Item> resourceList = commonDao.selectList("Relation.selectResourceList", inputCode);

		Map<String, List<String>> groupResourceList = resourceList.stream()
				.collect(Collectors.groupingBy(Item::getItemTypeNm,
						Collectors.mapping(Item::getUuid, Collectors.toList())));

		VoiceAndResourceDto result = new VoiceAndResourceDto();
		result.setVoice(voice);
		if(!voice.isEmpty()) result.setResourceList(groupResourceList);

		return result;
	}
}
