package com.lgcns.svcp.prod.online.prod.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto;
import com.lgcns.svcp.prod.online.prod.dto.common.Item;
import com.lgcns.svcp.prod.online.prod.dto.group.OfferGroupAndOfferDto;
import com.lgcns.svcp.prod.online.prod.dto.group.OfferGroupMDto;
import com.lgcns.svcp.prod.online.prod.dto.group.OfferGroupWithOfferDto;
import com.lgcns.svcp.prod.online.prod.dto.group.OfferGroupDto;
import com.lgcns.svcp.prod.online.prod.util.MapUtil;

import com.lgcns.svcp.prod.dataaccess.CommonDao;

@Component
public class GroupService {
	@Autowired
	private CommonDao commonDao;
	//오퍼그룹 전체리스트, 상품코드로 연결되어있는 오퍼그룹만 조회도 가능
	public List<OfferGroupMDto> retrieveOfferGroupMList() {
		List<OfferGroupMDto> resultList = commonDao.selectList("Group.retrieveOfferGroupMList");
		return resultList;
	}
	
	// 오퍼그룹코드-상품코드-시작-종료 새로운 dto, 상품코드로 조회 가능
	public List<OfferGroupWithOfferDto> retrieveOfferGroupWithOfferList(OfferGroupWithOfferDto offerGroupWithOfferDto) {
		List<OfferGroupWithOfferDto> resultList = commonDao.selectList("Group.retrieveOfferGroupWithOfferList", offerGroupWithOfferDto);
		return resultList;
	}
	
	
	public List<?> retrieveOfferGroupList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("OG");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		OfferGroupDto offerGroupDto = new OfferGroupDto();
		offerGroupDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Group.selectOfferGroupList", offerGroupDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	
	public OfferGroupAndOfferDto retrieveOfferGroupAndOffer(String inputCode) {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("OG");

		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		OfferGroupDto offerGroupDto = new OfferGroupDto();
		offerGroupDto.setOfferGroupCode(inputCode);

		offerGroupDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Group.selectOfferGroup", offerGroupDto);

		List<Map<String, Object>> offerGroup = resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());

		List<Item> offerList = commonDao.selectList("Group.selectOfferList", inputCode);

		Map<String, List<String>> groupOfferList = offerList.stream()
				.collect(Collectors.groupingBy(Item::getItemTypeNm,
						Collectors.mapping(Item::getUuid, Collectors.toList())));

		OfferGroupAndOfferDto result = new OfferGroupAndOfferDto();
		result.setOfferGroup(offerGroup);
		result.setOfferList(groupOfferList);

		return result;
	}
}
