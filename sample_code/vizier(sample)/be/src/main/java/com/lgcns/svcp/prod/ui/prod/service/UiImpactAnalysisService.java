package com.lgcns.svcp.prod.ui.prod.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.ui.prod.dto.analysis.ImpactAnalysisResponseDto;
import com.lgcns.svcp.prod.ui.prod.dto.analysis.ItemDto;
import com.lgcns.svcp.prod.ui.prod.dto.analysis.ProductStructureDto;
import com.lgcns.svcp.prod.ui.prod.dto.analysis.download.ProductStructureDownloadDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.SelectOptionDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.excel.ExcelHelper;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service("uiImpactAnalysisService")
@AllArgsConstructor
public class UiImpactAnalysisService {
	private final CommonDao commonDao;
	private final ExcelHelper excelHelper;

	public ImpactAnalysisResponseDto getImpactAnalysisResponseDto(ItemDto itemDto) {
		ItemDto parent = commonDao.select("Ui-impact.getParentProdM", itemDto);
		List<ItemDto> siblings = commonDao.selectList("Ui-impact.getSiblingsProdM", itemDto);

		return ImpactAnalysisResponseDto.builder().parent(parent).siblings(siblings).build();
	}

	public PageResult<?> retrieveItemsPagedList(ItemDto itemDto) {
		return commonDao.selectPagedList("Ui-impact.retrieveItemsPagedList", itemDto);
	}

	public PageResult<?> retrieveProductStructureListView(ProductStructureDto productStructureDto) {
		return commonDao.selectPagedList("Ui-impact.retrieveProductStructureListView", productStructureDto);
	}

	public List<SelectOptionDto> searchInfoItem() {
		return commonDao.selectList("Ui-impact.retrieveSearchInfoItem");
	}

	public void downloadProductStructureListView(ProductStructureDto request, HttpServletResponse response) {
		List<ProductStructureDto> entities = commonDao.selectList("Ui-impact.retrieveProductStructureListView",
				request);

		List<ProductStructureDownloadDto> dataExports = IntStream.range(0, entities.size()).mapToObj(i -> {
			ProductStructureDownloadDto downloadDto = new ProductStructureDownloadDto();
			ProductStructureDto entity = entities.get(i);
			downloadDto.setNo(i + 1);

			downloadDto.setOffrCd(entity.getOffrCd());
			downloadDto.setOffrNm(entity.getOffrNm());

			downloadDto.setCmpCd(entity.getCmpCd());
			downloadDto.setCmpNm(entity.getCmpNm());
			downloadDto.setCmpValdStrtDtm(DateUtil.formatDate(entity.getCmpValdStrtDtm()));
			downloadDto.setCmpValdEndDtm(DateUtil.formatDate(entity.getCmpValdEndDtm()));

			downloadDto.setSvcCd(entity.getSvcCd());
			downloadDto.setSvcNm(entity.getSvcNm());
			downloadDto.setSvcValdStrtDtm(DateUtil.formatDate(entity.getSvcValdStrtDtm()));
			downloadDto.setSvcValdEndDtm(DateUtil.formatDate(entity.getSvcValdEndDtm()));

			return downloadDto;
		}).collect(Collectors.toList());

		String fileName = "Impact-Analysis";

		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName(fileName);
		excelInput.setSheetName(fileName);
		excelInput.setFormatDate(DateConstant.YYYYMMDD);
		excelInput.setDatas(dataExports);

		excelHelper.of(ProductStructureDownloadDto.class).downloadExcel(excelInput, response, false);

	}

	public List<ItemDto> getChildrenProdM(ItemDto request) {
		List<ItemDto> productMasters = commonDao.selectList("Ui-impact.getChildrenProdM", request);

		if (productMasters == null || productMasters.isEmpty()) {
			return productMasters;
		}

		List<String> objUuids = productMasters.stream().map(ItemDto::getProdUuid).toList();

		Map<String, Object> param = Map.of("objUuids", objUuids);
		List<AdditionalDetailDto> additionalDetails = commonDao.selectList("Ui-offer.getOfferAdditionalDetails", param);

		if (additionalDetails == null) {
			return productMasters;
		}

		Map<String, List<AdditionalDetailDto>> additionalMap = additionalDetails.stream()
				.filter(detail -> detail.getObjUuid() != null)
				.collect(Collectors.groupingBy(AdditionalDetailDto::getObjUuid));

		productMasters.forEach(product -> {
			List<AdditionalDetailDto> details = additionalMap.getOrDefault(product.getProdUuid(), List.of());
			product.setAdditional(details);
		});

		return productMasters;
	}
}
