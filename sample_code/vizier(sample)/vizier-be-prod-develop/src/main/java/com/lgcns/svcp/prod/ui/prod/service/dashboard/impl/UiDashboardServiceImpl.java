package com.lgcns.svcp.prod.ui.prod.service.dashboard.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.context.UserContext;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.entity.DsbdItemStatEntity;
import com.lgcns.svcp.prod.entity.DsbdMonthlyOfferEntity;
import com.lgcns.svcp.prod.entity.DsbdMonthlyUserGroupOfferEntity;
import com.lgcns.svcp.prod.entity.DsbdViewEntity;
import com.lgcns.svcp.prod.entity.DsbdViewPstEntity;
import com.lgcns.svcp.prod.entity.external.DsbdListViewEntity;
import com.lgcns.svcp.prod.mapper.DsbdListViewMapper;
import com.lgcns.svcp.prod.mapper.DsbdMonthlyOfferMapper;
import com.lgcns.svcp.prod.mapper.DsbdMonthlyUserGroupOfferMapper;
import com.lgcns.svcp.prod.mapper.DsbdViewMapper;
import com.lgcns.svcp.prod.mapper.DsbdViewPstMapper;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdListViewDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdViewDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.ItemVolumeDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.ItemVolumeGroupDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.DsbdViewPstRequest;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.DsbdMonthlyOfferResponse;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.DsbdMonthlyUserGroupOfferResponse;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.ItemVolumeRespone;
import com.lgcns.svcp.prod.ui.prod.enums.DsbdViewTypeEnums;
import com.lgcns.svcp.prod.ui.prod.enums.OfferType;
import com.lgcns.svcp.prod.ui.prod.service.dashboard.UiDashboardService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UiDashboardServiceImpl implements UiDashboardService {
	
	private final CommonDao commonDao;
	
	private final DsbdViewPstMapper dsbdViewPstMapper;
	
	private final DsbdViewMapper dsbdViewMapper;
	
	private final DsbdMonthlyOfferMapper dsbdMonthlyOfferMapper;
	
	private final DsbdMonthlyUserGroupOfferMapper dsbdMonthlyUserGroupOfferMapper; 
	
	private final DsbdListViewMapper dsbdListViewMapper;
	
	@Override
	public Map<String, Object> initData(String userId) {
		Map<String, Object> results = new HashMap<>();
		List<DsbdListViewEntity> dsbdListViewEntities = commonDao.selectList("ui-dsbd-view-m.findListDsbdView", userId);
		List<DsbdListViewDto> dsbdListViewDtos = dsbdListViewEntities.stream().map(item -> dsbdListViewMapper.entityToDto(item)).toList();
		Map<String, Object> paramsCheckIsUsed = new HashMap<>();
		paramsCheckIsUsed.put("useYn", "Y");
		List<DsbdViewEntity> dsbdViewEntities = commonDao.selectList("ui-dsbd-view-m.findByProperties", paramsCheckIsUsed);
		Map<String, Object> paramsLoadCalendar = new HashMap<>();
		paramsLoadCalendar.put("code", "calendar");
		DsbdViewEntity dsbdCalendarViewEntity  = commonDao.select("ui-dsbd-view-m.findByProperties", paramsLoadCalendar);
		DsbdViewDto dsbdCalendarView = dsbdViewMapper.entityToDto(dsbdCalendarViewEntity);
		List<DsbdViewDto> dsbdViewDtos = dsbdViewEntities.stream().map(item -> {
			item.setCtgrTypeCode(convertDsbdViewTypeCode(item.getCtgrTypeCode()));
			DsbdViewDto dsbdViewDto = dsbdViewMapper.entityToDto(item);
			return dsbdViewDto;
		}).toList();
		results.put("listviewdashboard", dsbdListViewDtos);
		results.put("dsbdviews", dsbdViewDtos);
		results.put("dsbdCalendarView", dsbdCalendarView);
		return results;
	}
	
	@Override
	public DsbdViewDto findViewByUuid(String dsbdViewUuid) {
		Map<String, Object> params = new HashMap<>();
		params.put("dsbdViewUuid", dsbdViewUuid);
		DsbdViewEntity dsbdViewEntity = commonDao.select("ui-dsbd-view-m.findByProperties", params);
		DsbdViewDto result = dsbdViewMapper.entityToDto(dsbdViewEntity);
		result.setCtgrTypeCode(convertDsbdViewTypeCode(dsbdViewEntity.getCtgrTypeCode()));
		return result;
	}
	
	@Transactional
	@Override
	public void saveListView(List<DsbdViewPstRequest> requests) {
		String userId = UserContext.getCurrentUser();
		int isDsbdViewPstExist = commonDao.select("ui-dsbd-view-pst-d.countByUserId", userId);
		if (isDsbdViewPstExist > 0) {
			commonDao.delete("ui-dsbd-view-pst-d.deleteByUserId", userId);
		}
		List<DsbdViewPstEntity> dsbdViewPstEntities = requests.stream().map(item -> {
			DsbdViewPstEntity dsbdViewPstEntity = dsbdViewPstMapper.requestToEntity(item);
			dsbdViewPstEntity.setUserId(userId);
			return dsbdViewPstEntity;
		}).toList();
		commonDao.batchInsert("ui-dsbd-view-pst-d.insert", dsbdViewPstEntities);
	}
	
	@Override
	public Map<String, List<DsbdMonthlyOfferResponse>> getMonthlyReportAboutItems() {
		List<DsbdMonthlyOfferEntity> entities = commonDao.selectList("ui-dsbd-monthly-offer-m.findAll");
		Map<String, List<DsbdMonthlyOfferEntity>> mapEntities = entities.stream().collect(Collectors.groupingBy(DsbdMonthlyOfferEntity::getOfferTypeName));
		Map<String, List<DsbdMonthlyOfferResponse>> result = new HashMap<>();
		for(Map.Entry<String, List<DsbdMonthlyOfferEntity>> entry : mapEntities.entrySet()) { 
			result.put(entry.getKey(), dsbdMonthlyOfferMapper.convertListEntityToResponse(entry.getValue()));
		}
		return result;
	}

	private String convertDsbdViewTypeCode(String ctgrTypeCode) {
		return DsbdViewTypeEnums.valueOf(ctgrTypeCode).getValue();
	}
	
	@Override
	public Map<String, List<DsbdMonthlyUserGroupOfferResponse>> getMonthlyReportAboutUsers() {
		List<DsbdMonthlyUserGroupOfferEntity> entities = commonDao.selectList("ui-dsbd-monthly-user-group-offer-m.findAll");
		Map<String, List<DsbdMonthlyUserGroupOfferEntity>> mapEntities = entities.stream().collect(Collectors.groupingBy(DsbdMonthlyUserGroupOfferEntity::getUserGroupName));
		Map<String, List<DsbdMonthlyUserGroupOfferResponse>> result = new HashMap<>();
		for(Map.Entry<String, List<DsbdMonthlyUserGroupOfferEntity>> entry : mapEntities.entrySet()) { 
			result.put(entry.getKey().replaceAll("\\s", "-"), dsbdMonthlyUserGroupOfferMapper.convertListEntityToResponse(entry.getValue()));
		}
		return result;
	}
	
	@Override
	public ItemVolumeRespone getItemsVolume() {
		int totalItemGroup = 0;
		List<ItemVolumeGroupDto> itemGroupDtos = new ArrayList<>();
		List<DsbdItemStatEntity> dsbdItemStatEntities = commonDao.selectList("ui-dsbd-item-stat-m.findAll");
		Map<String, List<DsbdItemStatEntity>> mapEntities = dsbdItemStatEntities.stream().collect(Collectors.groupingBy(DsbdItemStatEntity::getLctgrItemName));
		for(Map.Entry<String, List<DsbdItemStatEntity>> entry : mapEntities.entrySet()) {
			DsbdItemStatEntity entity = entry.getValue().get(0);
			ItemVolumeGroupDto itemVolumeGroupDto = new ItemVolumeGroupDto();
			itemVolumeGroupDto.setName(entry.getKey());
			itemVolumeGroupDto.setRatio(entity.getLctgrItemRatio());
			if (!entity.getItemCodeName().equals("N/A")) {
				List<ItemVolumeDto> itemVolumeDtos = new ArrayList<>();
				int totalItem = 0;
				for (DsbdItemStatEntity item: entry.getValue()) {
					ItemVolumeDto itemVolumeDto = new ItemVolumeDto(item.getItemCodeName(), item.getItemCodeRatio());
					itemVolumeDtos.add(itemVolumeDto);
					totalItem += item.getItemCnt();
				}
				totalItemGroup += totalItem;
				itemVolumeGroupDto.setTotal(totalItem);
				itemVolumeGroupDto.setItems(itemVolumeDtos);
			}
			totalItemGroup += entity.getItemCnt();
			itemGroupDtos.add(itemVolumeGroupDto);
		}		
		return new ItemVolumeRespone(totalItemGroup, itemGroupDtos);
	}	

	@Override
	public List<String> getOfferType() {
		return Stream.of(OfferType.values()).map(Enum::name).toList();
	}
}
