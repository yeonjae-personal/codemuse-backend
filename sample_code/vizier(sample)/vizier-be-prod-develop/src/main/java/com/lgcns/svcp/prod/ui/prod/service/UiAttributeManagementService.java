package com.lgcns.svcp.prod.ui.prod.service;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeUpperLowerItemsDto;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeViewDto;

public interface UiAttributeManagementService {
	List<AttributeViewDto> getData();
	AttributeDetailDto getDetail(String itemCode);
	AttributeUpperLowerItemsDto getUpperOrLowerItems(String largeItem);
	void save(AttributeDetailDto request);
}
