package com.lgcns.svcp.prod.ui.prod.service;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.item.LargeItemDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.MiddleItemDto;

public interface MetadataService {
	List<MiddleItemDto> getMiddleItem(String largeItemCode);
	List<LargeItemDto> getLargeItem();
}
