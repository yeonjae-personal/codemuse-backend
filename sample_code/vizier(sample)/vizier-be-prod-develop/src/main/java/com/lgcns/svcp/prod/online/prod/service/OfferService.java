package com.lgcns.svcp.prod.online.prod.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.online.prod.dto.offer.AddOnAndComponentDto;
import com.lgcns.svcp.prod.online.prod.dto.offer.AddOnDto;
import com.lgcns.svcp.prod.online.prod.dto.offer.DiscountAndComponentDto;
import com.lgcns.svcp.prod.online.prod.dto.offer.DiscountDto;
import com.lgcns.svcp.prod.online.prod.dto.offer.PricePlanAndComponentDto;
import com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto;
import com.lgcns.svcp.prod.online.prod.dto.offer.PricePlanDto;
import com.lgcns.svcp.prod.online.prod.util.MapUtil;
import com.lgcns.svcp.prod.online.prod.dto.common.Item;

import com.lgcns.svcp.prod.dataaccess.CommonDao;

@Component
public class OfferService {
	@Autowired
	private CommonDao commonDao;

	//	public List<ProdMDto> retrieveProdMList(ProdMDto prodMDTO) {
	//		List<ProdMDto> resultList = commonDao.selectList("Offer.retrieveProdMList", prodMDTO);
	//		return resultList;
	//	}

	public int getTotalCounts() {
		return commonDao.select("Offer.retriveCounts");
	}
	
	public List<?> retrievePricePlanList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("PP");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);
		PricePlanDto pricePlanDto = new PricePlanDto();
		pricePlanDto.setAdditionalColumns(additionalColumns);
		
		List<Map<String, Object>> resultList = commonDao.selectList("Offer.selectPricePlanList", pricePlanDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	//	public PricePlanAndComponentDto retrievePricePlanAndComponent() { 
	//		PricePlanDto a = retrievePricePlan();
	//	}

	public List<?> retrieveAddOnList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("AO");
		System.out.println("1");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);
		System.out.println("2");
		AddOnDto addOnDto = new AddOnDto();
		addOnDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Offer.selectAddOnList", addOnDto);
		
		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public List<?> retrieveDiscountList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("DC");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);
		
		DiscountDto discountDto = new DiscountDto();
		discountDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Offer.selectDiscountList", discountDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}


	/**
	 * 
	 * @Author	: 이훈민(A76485@cnspartners.com)
	 * @Date	: 2024. 9. 10.
	 * @MethodName	: retrievePricePlanAndComponent
	 * @Method 설명	: 모바일 요금제코드로 general, additional 속성과 연결된 컴포넌트 리스트 조회
	 * @Param	: PricePlanAndComponentDto
	 * @return	: PricePlanAndComponentDto
	 */
	public PricePlanAndComponentDto retrievePricePlanAndComponent(String inputCode) {
		//ppCode, ppUuid로 할지 결정해야 하며 request를 위한 dto를 구분해야 한다. 09.10
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		//PP를 위한 메소드라 PP를 넣어도 되지만, uuid로 item_code를 받아오는 메소드 있는게 좋아보임
		request.setItemCode("PP");
		// additional 헤더에서 additonal 정보를 가져온다
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);
		
		PricePlanDto pricePlanDto = new PricePlanDto();
		pricePlanDto.setPpCode(inputCode);
		//pricePlanDto에 additonalColumn을 설정해줘야 pricePlan에서 additional 컬럼의 uuid를 모두 조회 할 수 있음
		pricePlanDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Offer.selectPricePlan", pricePlanDto);
		//resultList는 additional의 값이 null일 경우 '키'가 없어서 null값인 키를 설정해줘야 하며, key값을 camelCase로 리턴하는 작업을 한다
		List<Map<String, Object>> pricePlan = resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
		// input의 ppCode에 연결된 컴포넌트 리스트 조회 item_type_nm 까지 가져온다
		List<Item> componentList = commonDao.selectList("Relation.selectComponentList", inputCode);
		// 조회 결과를 item_type_nm으로 그룹화 한다
		Map<String, List<String>> groupComponentList = componentList.stream()
				.collect(Collectors.groupingBy(Item::getItemTypeNm,
						Collectors.mapping(Item::getUuid, Collectors.toList())));

		PricePlanAndComponentDto result = new PricePlanAndComponentDto();
		result.setPricePlan(pricePlan);
		result.setComponentList(groupComponentList);

		return result;
	}
	
	public AddOnAndComponentDto retrieveAddOnAndComponent(String inputCode) {
		//ppCode, ppUuid로 할지 결정해야 하며 request를 위한 dto를 구분해야 한다. 09.10
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		//PP를 위한 메소드라 PP를 넣어도 되지만, uuid로 item_code를 받아오는 메소드 있는게 좋아보임
		request.setItemCode("AO");
		// additional 헤더에서 additonal 정보를 가져온다
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);
		
		AddOnDto addOnDto = new AddOnDto();
		addOnDto.setAddonCode(inputCode);
		//pricePlanDto에 additonalColumn을 설정해줘야 pricePlan에서 additional 컬럼의 uuid를 모두 조회 할 수 있음
		addOnDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Offer.selectAddOn", addOnDto);
		//resultList는 additional의 값이 null일 경우 '키'가 없어서 null값인 키를 설정해줘야 하며, key값을 camelCase로 리턴하는 작업을 한다
		List<Map<String, Object>> addOn = resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
		// input의 ppCode에 연결된 컴포넌트 리스트 조회 item_type_nm 까지 가져온다
		List<Item> componentList = commonDao.selectList("Relation.selectComponentList", inputCode);
		// 조회 결과를 item_type_nm으로 그룹화 한다
		Map<String, List<String>> groupComponentList = componentList.stream()
				.collect(Collectors.groupingBy(Item::getItemTypeNm,
						Collectors.mapping(Item::getUuid, Collectors.toList())));

		AddOnAndComponentDto result = new AddOnAndComponentDto();
		result.setAddOn(addOn);
		result.setComponentList(groupComponentList);

		return result;
	}

	
	
	public DiscountAndComponentDto retrieveDiscountAndComponent(String inputCode) {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("DC");
		// additional 헤더에서 additonal 정보를 가져온다
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);
		
		DiscountDto discountDto = new DiscountDto();
		discountDto.setDcCode(inputCode);
		discountDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Offer.selectDiscount", discountDto);

		List<Map<String, Object>> discount = resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());

		List<Item> componentList = commonDao.selectList("Relation.selectComponentList", inputCode);

		Map<String, List<String>> groupComponentList = componentList.stream()
				.collect(Collectors.groupingBy(Item::getItemTypeNm,
						Collectors.mapping(Item::getUuid, Collectors.toList())));

		DiscountAndComponentDto result = new DiscountAndComponentDto();
		result.setDiscount(discount);
		result.setComponentList(groupComponentList);

		return result;
	}

	//	public List<ProdMDto> retrievePagingProdMList(ProdMDto prodMDTO) {
	//		List<ProdMDto> resultList = commonDao.selectPagedList("Offer.retrieveProdMList", prodMDTO);
	//		return resultList;
	//	}

}

