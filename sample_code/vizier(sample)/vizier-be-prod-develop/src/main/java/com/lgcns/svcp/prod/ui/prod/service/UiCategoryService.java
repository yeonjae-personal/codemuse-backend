package com.lgcns.svcp.prod.ui.prod.service;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.category.CategoryDescriptionDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.CategoryPathDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.CategoryTreeDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.CatgMDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.RequestOfferWithCatgUuidDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.detail.OffersOfCatgegoryReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.tab.CategoryTabDto;
import com.lgcns.svcp.prod.ui.prod.dto.category.update.CategoryTreeUpdateRequestDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.OfferOfLeafNodeDto;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.http.HttpServletResponse;

public interface UiCategoryService {

	List<OfferOfLeafNodeDto> retrieveProdMList(CatgMDto catgMDto);

	List<CategoryTreeDto> retrieveCategoryTreeWithOfferCounts(String ctgrTabUuid);

	PageResult<?> retrieveCategoryPathWithOffer(CategoryPathDto categoryPathDto);

	CategoryDescriptionDto retrieveCategoryDescription(String ctgrTabUuid);

	PageResult<?> retrieveOffersWithCategoryList(RequestOfferWithCatgUuidDto requestOfferWithCatgUuidDto);

	PageResult<?> retrieveOffersOfCatgegory(OffersOfCatgegoryReqDto req);

	void exportExcel(CategoryPathDto categoryPathDto, HttpServletResponse response);

	List<CategoryTabDto> retrieveCatgegoryTabs();

	void ụpdateCategoryTree(CategoryTreeUpdateRequestDto treeUpdateDto);

	List<String> retrievePathInfo(String ctgrNodeUuid);

}
