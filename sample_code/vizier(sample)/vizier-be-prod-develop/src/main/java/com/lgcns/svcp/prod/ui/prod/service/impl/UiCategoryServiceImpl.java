package com.lgcns.svcp.prod.ui.prod.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.ui.prod.dto.category.CategoryDescriptionDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.CategoryPathDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.CategoryTreeDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.CatgMDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.CtgrLevelDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.RequestOfferWithCatgUuidDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.detail.OffersOfCatgegoryReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.tab.CategoryTabDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.update.CategoryOfferRelDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.update.CategorySaveDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.update.CategoryTreeUpdateRequestDto;
import com.lgcns.svcp.prod.ui.prod.dto.export.CategoryPathExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.OfferOfLeafNodeDto;
import com.lgcns.svcp.prod.ui.prod.service.UiCategoryService;
import com.lgcns.svcp.prod.util.excel.ExcelHelper;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UiCategoryServiceImpl implements UiCategoryService {
	private final CommonDao commonDao;

	private final ExcelHelper excelHelper;

	private final static String FILE_NAME = "Tree-View";

	private int getCatgMCount(CatgMDto catgMDto) {
		return commonDao.select("Ui-category.getCountCatgM", catgMDto);
	}

	private boolean isLeafCategoryNode(CatgMDto catgMDto) {
		if (getCatgMCount(catgMDto) > 0) {
			return false;
		}
		return true;
	}

	@Override
	public List<OfferOfLeafNodeDto> retrieveProdMList(CatgMDto catgMDto) {
		if (isLeafCategoryNode(catgMDto)) {
			return commonDao.selectList("Ui-category.retrieveProdMByCatgMUuid", catgMDto);
		}
		throw new BusinessException("카테고리 노드가 아닙니다.");
	}

	/**
	 *
	 * @Author : 이훈민(A76485@cnspartners.com)
	 * @Date : 2024. 7. 2.
	 * @MethodName : retrieveCategoryTreeWithProductCounts
	 * @Method 설명 : 카테고리 상품 건수와 함께 트리구조 리턴
	 * @Param :
	 * @return : List<CategoryTreeDto>
	 */
	@Override
	public List<CategoryTreeDto> retrieveCategoryTreeWithOfferCounts(String ctgrTabUuid) {
		List<CategoryTreeDto> categories = commonDao.selectList("Ui-category.retrieveCategoryTreeWithOfferCounts",
				ctgrTabUuid);

		for (CategoryTreeDto category : categories) {
			if (!StringUtils.hasLength(category.getHpstCtgrNodeUuid())) {
				category.setShowChilderen(true);
			}
		}
		Map<String, CategoryTreeDto> categoryMap = categories.stream()
				.collect(Collectors.toMap(CategoryTreeDto::getCtgrNodeUuid, category -> category));
		for (CategoryTreeDto category : categories) {

			if (StringUtils.hasLength(category.getHpstCtgrNodeUuid())) {

				CategoryTreeDto parent = categoryMap.get(category.getHpstCtgrNodeUuid());
				if (parent != null) {
					if (parent.getChildren() == null) {
						parent.setChildren(new ArrayList<>());
					}
					parent.getChildren().add(category);
				}
			}
		}

		categories = categories.stream().filter(category -> !StringUtils.hasLength(category.getHpstCtgrNodeUuid()))
				.collect(Collectors.toList());
		sortRecursive(categories);
		return categories;
	}

	private void sortRecursive(List<CategoryTreeDto> list) {
		if (list == null || list.isEmpty()) {
			return;
		}

		list.sort(Comparator.comparingInt(CategoryTreeDto::getTotalOfferCount).reversed()
				.thenComparing(CategoryTreeDto::getCtgrNodeName));

		for (CategoryTreeDto node : list) {
			sortRecursive(node.getChildren());
		}
	}

	@Override
	public PageResult<?> retrieveCategoryPathWithOffer(CategoryPathDto categoryPathDto) {
		return commonDao.selectPagedList("Ui-category.retrieveCategoryPathWithOffer", categoryPathDto);
	}

	@Override
	public CategoryDescriptionDto retrieveCategoryDescription(String ctgrTabUuid) {
		return commonDao.select("Ui-category.retrieveCategoryDescription", ctgrTabUuid);
	}

	@Override
	public PageResult<?> retrieveOffersWithCategoryList(RequestOfferWithCatgUuidDto requestOfferWithCatgUuidDto) {
		return commonDao.selectPagedList("Ui-category.retrieveOfferMWithCategoryList", requestOfferWithCatgUuidDto);
	}

	@Override
	public PageResult<?> retrieveOffersOfCatgegory(OffersOfCatgegoryReqDto req) {
		return commonDao.selectPagedList("Ui-category.retrieveOffersOfCatgegory", req);
	}

	@Override
	public void exportExcel(CategoryPathDto categoryPathDto, HttpServletResponse response) {
		List<CategoryPathDto> entities = commonDao.selectList("Ui-category.retrieveCategoryPathWithOffer",
				categoryPathDto);
		List<CategoryPathExportDto> dataExports = buildDataExcelExport(entities);
		List<CategoryTabDto> catgegoryTabs = retrieveCatgegoryTabs();
		String ctgrTabName = catgegoryTabs.stream()
				.filter(tab -> tab.getCtgrTabUuid().equals(categoryPathDto.getCtgrTabUuid())).findFirst()
				.orElse(new CategoryTabDto()).getCtgrTabName();

		excelHelper.of(CategoryPathExportDto.class).downloadExcel(buildExcelInput(dataExports, ctgrTabName), response,
				false);
	}

	private List<CategoryPathExportDto> buildDataExcelExport(List<CategoryPathDto> results) {
		return IntStream.range(0, results.size()).mapToObj(i -> {
			CategoryPathDto entity = results.get(i);
			return CategoryPathExportDto.builder().index(i + 1).level1(entity.getLevel1()).level2(entity.getLevel2())
					.level3(entity.getLevel3()).level4(entity.getLevel4()).level5(entity.getLevel5())
					.offerCd(entity.getOfferCd()).offerNm(entity.getOfferNm()).build();
		}).collect(Collectors.toList());
	}

	private ExcelInput buildExcelInput(List<CategoryPathExportDto> datas, String ctgrTabName) {
		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName(String.format("%s(%s)", FILE_NAME, ctgrTabName));
		excelInput.setSheetName(FILE_NAME);
		excelInput.setFormatDate(DateConstant.YYYYMMDD);
		excelInput.setDatas(datas);
		return excelInput;
	}

	@Override
	public List<CategoryTabDto> retrieveCatgegoryTabs() {
		return commonDao.selectList("Ui-category.retrieveCatgegoryTabs");
	}

	@Override
	@Transactional
	public void ụpdateCategoryTree(CategoryTreeUpdateRequestDto treeUpdateDto) {
		List<CtgrLevelDto> ctgrLevels = treeUpdateDto.getCtgrLevels();
		List<CategorySaveDto> categoryTree = treeUpdateDto.getCategoryTreeFlat();
		List<CategoryOfferRelDto> offerRel = treeUpdateDto.getCategoryOfferFlat();

		commonDao.batchUpdate("Ui-category.saveCategoryDescription", ctgrLevels);
		commonDao.batchInsert("Ui-category.saveCatgegoryTree", categoryTree);
		commonDao.batchUpdate("Ui-category.moveCatgegoryOfferRel", offerRel);
	}

	@Override
	public List<String> retrievePathInfo(String ctgrNodeUuid) {
		return commonDao.selectList("Ui-category.retrievePathInfo", ctgrNodeUuid);
	}

}
