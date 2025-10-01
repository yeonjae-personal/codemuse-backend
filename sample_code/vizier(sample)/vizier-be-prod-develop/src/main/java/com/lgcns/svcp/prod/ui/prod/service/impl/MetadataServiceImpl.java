package com.lgcns.svcp.prod.ui.prod.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.entity.MiddleItemEntity;
import com.lgcns.svcp.prod.mapper.MetadataMapper;
import com.lgcns.svcp.prod.ui.prod.dto.item.LargeItemDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.MiddleItemDto;
import com.lgcns.svcp.prod.ui.prod.service.MetadataService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MetadataServiceImpl implements MetadataService {
	
	private final CommonDao commonDao;
	private final MetadataMapper metadataMapper;
	
	@Override
	public List<MiddleItemDto> getMiddleItem(String largeItemCode) {
		Map<String, Object> maps = new HashMap<>();
		maps.put("largeItemCode", largeItemCode);
		List<MiddleItemEntity> middleItemEntities = commonDao.selectList("Ui-item.getMiddleItem", maps);
		return middleItemEntities.stream().map(item -> metadataMapper.middleItemEntityToDto(item)).toList();
	}

	@Override
	public List<LargeItemDto> getLargeItem() {
		return commonDao.selectList("Ui-item.getLargeItem");
	}
}
