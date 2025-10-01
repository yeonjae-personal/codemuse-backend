package com.lgcns.svcp.prod.ui.prod.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.entity.UserPocketEntity;
import com.lgcns.svcp.prod.entity.external.UserPocketCustomEntity;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.mapper.UserPocketMapper;
import com.lgcns.svcp.prod.ui.prod.dto.item.LargeItemDto;
import com.lgcns.svcp.prod.ui.prod.dto.userpocket.UserPocketDto;
import com.lgcns.svcp.prod.ui.prod.dto.userpocket.UserPocketRequest;
import com.lgcns.svcp.prod.ui.prod.dto.userpocket.UserPocketRespone;
import com.lgcns.svcp.prod.ui.prod.service.UiUserPocketService;
import com.lgcns.svcp.prod.util.DateUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UiUserPocketServiceImpl implements UiUserPocketService {
	
	private final CommonDao commonDao;
	private final UserPocketMapper userPocketMapper;
	private final MessageSource messageSource;
	
	@Override
	public List<UserPocketRespone> getUserPocket(String userId) {
		List<UserPocketRespone> results = new ArrayList<>();
		List<UserPocketCustomEntity> userPocketCustomEntities = commonDao.selectList("ui-user-pocket-m.getAll", userId);
		Map<String, List<UserPocketCustomEntity>> mapEntities = userPocketCustomEntities.stream()
								.collect(Collectors.groupingBy(UserPocketCustomEntity::getLctgrItemCode));
		for(Map.Entry<String, List<UserPocketCustomEntity>> entry : mapEntities.entrySet()) { 
			UserPocketRespone userPocketRespone = new UserPocketRespone();
			userPocketRespone.setLargeItemCode(entry.getKey());
			LargeItemDto largeItemDto = commonDao.select("Ui-item.getLargeItemByCode", entry.getKey());
			String largeItemName = null;
			if (LocaleContextHolder.getLocale().equals(Locale.KOREAN)) {
				largeItemName = messageSource.getMessage("large.item."+largeItemDto.getName().toLowerCase(), null, LocaleContextHolder.getLocale());
			} else {
				largeItemName = largeItemDto.getName();
			}
			userPocketRespone.setLargeItemName(largeItemName);
			userPocketRespone.setSortNo(largeItemDto.getSortNo());
			List<UserPocketCustomEntity> customEntities = entry.getValue();
			userPocketRespone.setDatas(customEntities.stream().map(item -> {
												UserPocketDto userPocketDto = userPocketMapper.customEntityToDto(item);
												userPocketDto.setItemValidStart(DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM_SS, item.getItemValidStartDtm()));
												userPocketDto.setItemValidEnd(DateUtil.formatDate(DateConstant.YYYY_MM_DD_HH_MM_SS, item.getItemValidEndDtm()));
												return userPocketDto;
										})
										.sorted(Comparator.comparing(UserPocketDto::getName)).toList());
			results.add(userPocketRespone);
		}
		return results.stream().sorted(Comparator.comparingInt(UserPocketRespone::getSortNo)).toList();
	}
	
	@Transactional
	@Override
	public void save(UserPocketRequest request, String userId) {
		Map<String, Object> maps = new HashMap<>();
		maps.put("uuid", request.getUuid());
		maps.put("userId", userId);
		maps.put("validEndDtm", true);
		UserPocketEntity userPocketEntity = commonDao.select("ui-user-pocket-m.findProperties", maps);
		if (userPocketEntity != null) {
			throw new BusinessException("product_platform.itemExist");
		} else {
			List<UserPocketCustomEntity> userPocketCustomEntities = commonDao.selectList("ui-user-pocket-m.getAll", userId);
			Map<String, List<UserPocketCustomEntity>> mapEntities = userPocketCustomEntities.stream()
									.collect(Collectors.groupingBy(UserPocketCustomEntity::getLctgrItemCode));
			int groupQuantity = 0;
			int itemQuantity = 0;
			for(Map.Entry<String, List<UserPocketCustomEntity>> entry : mapEntities.entrySet()) { 
				groupQuantity++;
				itemQuantity += entry.getValue().size();
			}
			if (groupQuantity == 1) {
				if (itemQuantity >= 12) {
					throw new BusinessException("product_platform.maxItemUp");
				}
			} else if (groupQuantity == 2) {
				if (itemQuantity >= 11) {
					throw new BusinessException("product_platform.maxItemUp");
				}
			} else if (groupQuantity == 3) {
				if (itemQuantity >= 10) {
					throw new BusinessException("product_platform.maxItemUp");
				}
			} else if (groupQuantity == 4) {
				if (itemQuantity >= 9) {
					throw new BusinessException("product_platform.maxItemUp");
				}
			} else if (groupQuantity == 5) {
				if (itemQuantity >= 8) {
					throw new BusinessException("product_platform.maxItemUp");
				}
			} 
			insertUserPocket(request, userId);
		}
	}

	private void insertUserPocket(UserPocketRequest request, String userId) {
		UserPocketEntity userPocketEntity = new UserPocketEntity();
		userPocketEntity.setUserId(userId);
		userPocketEntity.setObjUuid(request.getUuid());
		userPocketEntity.setValidStartDtm(new Date());
		commonDao.insert("ui-user-pocket-m.insert", userPocketEntity);
	}

	@Transactional
	@Override
	public void delete(String uuid, String userId) {
		Map<String, Object> maps = new HashMap<>();
		maps.put("uuid", uuid);
		maps.put("userId", userId);
		maps.put("validEndDtm", DateUtil.minusDays(LocalDate.now(), 1));
		commonDao.delete("ui-user-pocket-m.delete", maps);
	}
}
