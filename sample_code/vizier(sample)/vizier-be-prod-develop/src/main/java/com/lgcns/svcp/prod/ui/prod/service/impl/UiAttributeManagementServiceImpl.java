package com.lgcns.svcp.prod.ui.prod.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.entity.CtgrNodeEntity;
import com.lgcns.svcp.prod.entity.CtgrTabEntity;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.mapper.CtgrNodeMapper;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeAdditionalDto;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeGeneralDto;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeItemDto;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeItemStrcDto;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeMiddleItemDto;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeUpperLowerItemsDto;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeViewDto;
import com.lgcns.svcp.prod.ui.prod.dto.attribute.TreeViewDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.CtgrLevelDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.Item;
import com.lgcns.svcp.prod.ui.prod.service.UiAttributeManagementService;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.UuidUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UiAttributeManagementServiceImpl implements UiAttributeManagementService {
	
	private final CommonDao commonDao;
	private final CtgrNodeMapper ctgrNodeMapper; 

	@Override
	public List<AttributeViewDto> getData() {
		List<AttributeViewDto> results = new ArrayList<>();
		List<Item> items = commonDao.selectList("Ui-item.getListAttributeInAdmin");
		Map<String, List<Item>> mapLargeItems = items.stream().collect(Collectors.groupingBy(Item::getLargeItemCode));
		for(Map.Entry<String, List<Item>> entry : mapLargeItems.entrySet()) {
			Item largeItem = entry.getValue().get(0);
			AttributeViewDto attributeViewDto = new AttributeViewDto();
			attributeViewDto.setName(largeItem.getLargeItemName());
			attributeViewDto.setCode(largeItem.getLargeItemCode());
			attributeViewDto.setSortNo(largeItem.getLargeSortNo());
			if (entry.getKey().equals("C")) {
				List<AttributeMiddleItemDto> middleItems = new ArrayList<>();
				Map<String, List<Item>> mapMiddleItems = entry.getValue().stream().collect(Collectors.groupingBy(Item::getMiddleItemCode));
				for(Map.Entry<String, List<Item>> entryMiddle : mapMiddleItems.entrySet()) {
					Item middleItem = entryMiddle.getValue().get(0);
					AttributeMiddleItemDto attributeMiddleItemDto = new AttributeMiddleItemDto();
					attributeMiddleItemDto.setItemCode(middleItem.getMiddleItemCode());
					attributeMiddleItemDto.setItemName(middleItem.getMiddleItemName());
					attributeMiddleItemDto.setLargeItemCode(largeItem.getLargeItemCode());
					attributeMiddleItemDto.setLargeItemName(largeItem.getLargeItemName());
					attributeMiddleItemDto.setSortNo(middleItem.getMiddleSortNo());
					List<Item> itemSorted = entryMiddle.getValue().stream().sorted(Comparator.comparingInt(Item::getSortNo)).toList();
					attributeMiddleItemDto.setItems(itemSorted);
					middleItems.add(attributeMiddleItemDto);
				}
				middleItems = middleItems.stream().sorted(Comparator.comparingInt(AttributeMiddleItemDto::getSortNo)).toList();
				attributeViewDto.setMiddleItems(middleItems);
			} else {
				List<Item> itemSorted = entry.getValue().stream().sorted(Comparator.comparingInt(Item::getSortNo)).toList();
				attributeViewDto.setItems(itemSorted);
			}
			results.add(attributeViewDto);
		}
		results = results.stream().sorted(Comparator.comparingInt(AttributeViewDto::getSortNo)).toList();
		return results;
	}

	@Override
	public AttributeDetailDto getDetail(String itemCode) {
		AttributeDetailDto result = new AttributeDetailDto();
		AttributeGeneralDto general = commonDao.select("Ui-item.getItemGeneralAttribute", itemCode);
		if (general.getLargeItemCode().equals("C")) {
			List<Item> upperItems = commonDao.selectList("Ui-common.getUpperItems", itemCode);
			general.setUpperItems(upperItems);
			List<Item> lowerItems = commonDao.selectList("Ui-common.getLowerItems", itemCode);
			general.setLowerItems(lowerItems);
		} else if (general.getLargeItemCode().equals("R")) {
			List<Item> upperItems = commonDao.selectList("Ui-common.getUpperItems", itemCode);
			general.setUpperItems(upperItems);
		} else if (general.getLargeItemCode().equals("O")) {
			List<Item> lowerItems = commonDao.selectList("Ui-common.getLowerItems", itemCode);
			general.setLowerItems(lowerItems);
		}
		result.setGeneral(general);
		List<AttributeAdditionalDto> additionals = commonDao.selectList("UI.getAdditionalByItem", itemCode);
		result.setAdditionals(additionals);
		if (general.getLargeItemCode().equals("O")) {
			List<TreeViewDto> treeViewDtoes = commonDao.selectList("Ui-category.findTreeView", general.getItemName());
			if (treeViewDtoes != null && !treeViewDtoes.isEmpty()) {
				TreeViewDto treeViewDto = treeViewDtoes.get(0);
				treeViewDto.setRgstDtm(DateUtil.formatDate(DateConstant.YYYY_MM_DD, treeViewDto.getRgstDtmOrigin()));
				result.setTreeView(treeViewDto);
			}
		}
		return result;
	}

	@Override
	public AttributeUpperLowerItemsDto getUpperOrLowerItems(String largeItem) {
		AttributeUpperLowerItemsDto result = new AttributeUpperLowerItemsDto();
		if (largeItem.equals("C")) {
			List<Item> upperItems = commonDao.selectList("Ui-item.getUpperOrLowerItems", "O");
			result.setUpperItems(upperItems);
			List<Item> lowerItems = commonDao.selectList("Ui-item.getUpperOrLowerItems", "R");
			result.setLowerItems(lowerItems);
		} else if (largeItem.equals("R")) {
			List<Item> upperItems = commonDao.selectList("Ui-item.getUpperOrLowerItems", "C");
			result.setUpperItems(upperItems);
		} else if (largeItem.equals("O")) {
			List<Item> lowerItems = commonDao.selectList("Ui-item.getUpperOrLowerItems", "C");
			result.setLowerItems(lowerItems);
		}
		return result;
	}

	@Transactional
	@Override
	public synchronized void save(AttributeDetailDto request) {
		saveWithSynchronized(request);
	}

	private void saveWithSynchronized(AttributeDetailDto request) {
		int existItemCode = commonDao.select("Ui-item.checkExistItemCode", request.getGeneral().getItemCode().toLowerCase());
		int existItemName = commonDao.select("Ui-item.checkExistItemName", request.getGeneral().getItemName().toLowerCase());
		validation(request, existItemCode, existItemName);
		saveItem(request.getGeneral());
		saveTreeView(request.getGeneral(), request.getTreeView());
		saveUpperOrLowerItem(request.getGeneral(), request.getGeneral().getLowerItems(), request.getGeneral().getUpperItems());
		saveAdditional(request.getAdditionals());
	}
	
	private void saveTreeView(AttributeGeneralDto general, TreeViewDto treeViewDto) {
		if (general.getLargeItemCode().equals("O")) {
			if (StringUtils.isNotBlank(treeViewDto.getCtgrTabUuid())) {
				//update
				CtgrTabEntity ctgrTabEntity = new CtgrTabEntity();
				ctgrTabEntity.setCtgrTabName(general.getItemName());
				ctgrTabEntity.setUseYn(general.getUseYn());
				ctgrTabEntity.setCtgrTabUuid(treeViewDto.getCtgrTabUuid());
				commonDao.update("Ui-category.updateCtgrTab", ctgrTabEntity);
				CtgrNodeEntity ctgrNodeEntity = ctgrNodeMapper.treeViewDtoToEntity(treeViewDto);
				ctgrNodeEntity.setUseYn(general.getUseYn());
				commonDao.update("Ui-category.updateCtgrNode", ctgrNodeEntity);
			} else {
				//insert tab
				CtgrTabEntity ctgrTabEntity = new CtgrTabEntity();
				String ctgrTabUuid = UuidUtil.generateRandomUUID();
				ctgrTabEntity.setCtgrTabUuid(ctgrTabUuid);
				ctgrTabEntity.setCtgrTabName(general.getItemName());
				ctgrTabEntity.setUseYn(general.getUseYn());
				List<Integer> sortNo = commonDao.selectList("Ui-category.findTabSeq");
				ctgrTabEntity.setSortNo(sortNo.get(0) + 1);
				commonDao.insert("Ui-category.insertCtgrTab", ctgrTabEntity);
				//insert node
				CtgrNodeEntity ctgrNodeEntity = ctgrNodeMapper.treeViewDtoToEntity(treeViewDto);
				ctgrNodeEntity.setUseYn(general.getUseYn());
				ctgrNodeEntity.setCtgrTabUuid(ctgrTabUuid);
				ctgrNodeEntity.setTclsCtgrYn("Y");
				commonDao.insert("Ui-category.insertCtgrNode", ctgrNodeEntity);
				//insert description
				commonDao.batchInsert("Ui-category.saveCategoryDescriptionInAttribute", getCtgrLevels(ctgrTabUuid));
			}
		}
	}

	private void saveItem(AttributeGeneralDto general) {
		AttributeItemDto itemDto = initDataItem(general);
		if (general.getSortNo() != null) {
			commonDao.update("Ui-item.update", itemDto);
		} else {
			commonDao.insert("Ui-item.insert", itemDto);
		}
	}
	
	private void saveUpperOrLowerItem(AttributeGeneralDto general, List<Item> lowerItems, List<Item> upperItems) {
		List<AttributeItemStrcDto> attributeItemStrcDtos = new ArrayList<>();
		if (general.getLargeItemCode().equals("C")) {
			List<Item> checkUpperItems = commonDao.selectList("Ui-common.getUpperItems", general.getItemCode());
			if (checkUpperItems != null && !checkUpperItems.isEmpty()) {
				commonDao.delete("Ui-item-strc-d.deleteUpperItem", general.getItemCode());
			}
			for (Item item: upperItems) {
				AttributeItemStrcDto attributeItemStrcDto = new AttributeItemStrcDto();
				attributeItemStrcDto.setBaseItemCode(item.getItemCode());
				attributeItemStrcDto.setTrgtItemCode(general.getItemCode());
				attributeItemStrcDto.setStrcTypeCode(item.getStrcTypeCode());
				attributeItemStrcDtos.add(attributeItemStrcDto);
			}
			List<Item> checkLowerItems = commonDao.selectList("Ui-common.getLowerItems", general.getItemCode());
			if (checkLowerItems != null && !checkLowerItems.isEmpty()) {
				commonDao.delete("Ui-item-strc-d.deleteLowerItem", general.getItemCode());
			}
			for (Item item: lowerItems) {
				AttributeItemStrcDto attributeItemStrcDto = new AttributeItemStrcDto();
				attributeItemStrcDto.setBaseItemCode(general.getItemCode());
				attributeItemStrcDto.setTrgtItemCode(item.getItemCode());
				attributeItemStrcDto.setStrcTypeCode(item.getStrcTypeCode());
				attributeItemStrcDtos.add(attributeItemStrcDto);
			}
		} else if (general.getLargeItemCode().equals("R")) {
			List<Item> checkUpperItems = commonDao.selectList("Ui-common.getUpperItems", general.getItemCode());
			if (checkUpperItems != null && !checkUpperItems.isEmpty()) {
				commonDao.delete("Ui-item-strc-d.deleteUpperItem", general.getItemCode());
			}
			for (Item item: upperItems) {
				AttributeItemStrcDto attributeItemStrcDto = new AttributeItemStrcDto();
				attributeItemStrcDto.setBaseItemCode(item.getItemCode());
				attributeItemStrcDto.setTrgtItemCode(general.getItemCode());
				attributeItemStrcDto.setStrcTypeCode(item.getStrcTypeCode());
				attributeItemStrcDtos.add(attributeItemStrcDto);
			}
		} else if (general.getLargeItemCode().equals("O")) {
			List<Item> checkLowerItems = commonDao.selectList("Ui-common.getLowerItems", general.getItemCode());
			if (checkLowerItems != null && !checkLowerItems.isEmpty()) {
				commonDao.delete("Ui-item-strc-d.deleteLowerItem", general.getItemCode());
			}
			for (Item item: lowerItems) {
				AttributeItemStrcDto attributeItemStrcDto = new AttributeItemStrcDto();
				attributeItemStrcDto.setBaseItemCode(general.getItemCode());
				attributeItemStrcDto.setTrgtItemCode(item.getItemCode());
				attributeItemStrcDto.setStrcTypeCode(item.getStrcTypeCode());
				attributeItemStrcDtos.add(attributeItemStrcDto);
			}
		}
		commonDao.batchInsert("Ui-item-strc-d.insert", attributeItemStrcDtos);
	}

	private void saveAdditional(List<AttributeAdditionalDto> additionals) {
		List<AttributeAdditionalDto> listAdditionalUpdates = new ArrayList<>();
		List<AttributeAdditionalDto> listAdditionalAdds = new ArrayList<>();
		int seq = 1;
		for (AttributeAdditionalDto additionalDto: additionals) {
			additionalDto.setSortNo(seq);
			if (additionalDto.getAttrUuid() != null && StringUtils.isNotBlank(additionalDto.getAttrUuid())) {
				listAdditionalUpdates.add(additionalDto);
			} else {
				listAdditionalAdds.add(additionalDto);
			}
			seq++;
		}
		commonDao.batchUpdate("ui-add-attr-header-m.update", listAdditionalUpdates);
		commonDao.batchInsert("ui-add-attr-header-m.insert", listAdditionalAdds);
	}

	private AttributeItemDto initDataItem(AttributeGeneralDto general) {
		AttributeItemDto result = new AttributeItemDto();
		result.setItemCode(general.getItemCode().toUpperCase());
		result.setItemCodeName(general.getItemName());
		result.setLctgrItemCode(general.getLargeItemCode());
		result.setUseYn(general.getUseYn());
		result.setMctgrItemCode(general.getMiddleItemCode());
		if (general.getSortNo() == null) {
			Map<String, Object> sortParams = new HashMap<>();
			sortParams.put("largeItemCode", general.getLargeItemCode());
			sortParams.put("middleItemCode", general.getMiddleItemCode());
			List<Integer> sortNumbers = commonDao.selectList("Ui-item.findMaxItemSortNo", sortParams);
			if (sortNumbers != null && !sortNumbers.isEmpty()) {
				result.setSortNo(sortNumbers.get(0) + 1);
			} else {
				result.setSortNo(1);
			}
		}
		return result;
	}

	private void validation(AttributeDetailDto request, int existItemCode, int existItemName) {
		if (request.getGeneral().getSortNo() == null) {
			//ko: 아이템 코드 종복되었습니다 / 아이템 명 종복되었습니다
			if (existItemCode > 0) {
				throw new BusinessException("Item Code is duplicated");
			} else if (existItemName > 0) {
				throw new BusinessException("Item Name is duplicated");
			}
		} else {
			for (AttributeAdditionalDto additionalDto: request.getAdditionals()) {
				if (additionalDto.getAttrUuid() == null || StringUtils.isEmpty(additionalDto.getAttrUuid())) {
					Map<String, Object> params = new HashMap<>();
					params.put("labelId", additionalDto.getLabelId());
					params.put("itemCode", additionalDto.getItemCode());
					int countLabel = commonDao.select("ui-add-attr-header-m.countByLabelIdAndItemCode", params);
					if (countLabel > 0) {
						//ko: 라벨 ID 종복되었습니다
						throw new BusinessException("Label ID is duplicated");
					}
				}
			}
		}
	}
	
	private List<CtgrLevelDto> getCtgrLevels(String ctgrTabUuid) {
        List<CtgrLevelDto> ctgrLevels = new ArrayList<>();
        ctgrLevels.add(convertLevel("1", ctgrTabUuid, "오퍼 분류"));
        ctgrLevels.add(convertLevel("2", ctgrTabUuid, "통신 사업자"));
        ctgrLevels.add(convertLevel("3", ctgrTabUuid, "부가상품 분류 3"));
        ctgrLevels.add(convertLevel("4", ctgrTabUuid, "부가상품 분류 4"));
        ctgrLevels.add(convertLevel("5", ctgrTabUuid, "부가상품 분류 5 수정"));
        return ctgrLevels;
    }
	
	private CtgrLevelDto convertLevel(String levelNo, String ctgrTabUuid, String cntn) {
        CtgrLevelDto ctgrLevel = new CtgrLevelDto();
        ctgrLevel.setCtgrTabUuid(ctgrTabUuid);
        ctgrLevel.setLevelNo(levelNo);
        ctgrLevel.setLevelDscrCntn(cntn);
        return ctgrLevel;
    }
}
