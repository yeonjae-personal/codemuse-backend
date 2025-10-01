package com.lgcns.svcp.prod.ui.prod.service.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.context.RequestContextHolder;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.ui.prod.dto.admin.factor.FactorDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.factor.FactorTypeDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.factor.FactorValueDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.factor.SearchFactorReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.admin.factor.SearchFactorTypeReqDto;
import com.lgcns.svcp.prod.util.StringUtilCustom;
import com.lgcns.svcp.prod.util.paging.PageResult;

import lombok.RequiredArgsConstructor;

@Service("uiFactorService")
@RequiredArgsConstructor
public class UiFactorService {
	private final CommonDao commonDao;
	private final MessageSource messageSource;

	public PageResult<?> searchFactorType(SearchFactorTypeReqDto reqDto) {
		return commonDao.selectPagedList("Ui-factor.searchFactorType", reqDto);
	}

	public PageResult<?> searchFactor(SearchFactorReqDto reqDto) {
		return commonDao.selectPagedList("Ui-factor.searchFactor", reqDto);
	}

	public FactorTypeDto retrieveFactorType(SearchFactorReqDto reqDto) {
		FactorTypeDto resulTypeDto = commonDao.select("Ui-factor.retrieveFactorType", reqDto.getFactorTypeCode());
		if (resulTypeDto != null) {
			PageResult<?> factorSearchLst = searchFactor(reqDto);
			List<FactorDto> factorLst = commonDao.selectList("Ui-factor.searchFactor", reqDto);
			resulTypeDto.setFactorSearchLst(factorSearchLst);
			resulTypeDto.setFactorLst(factorLst);
		}
		return resulTypeDto;
	}

	@Transactional
	public void updateFactorType(FactorTypeDto factorTypeDto) {
		// handle data
		String factorTypeCode = factorTypeDto.getFactorTypeCode();
		List<FactorDto> factorLst = factorTypeDto.getFactorLst();
		List<FactorValueDto> factorValueLst = new ArrayList<>();

		validateUniqueFactorName(factorLst);

		Iterator<FactorDto> it = factorLst.iterator();
		while (it.hasNext()) {
			FactorDto ft = it.next();
			ft.setFactorTypeCode(factorTypeCode);

			String factorCode = ft.getFactorCode();
			if (StringUtilCustom.isBlank(factorCode)) {
				// insert new Factor
				factorCode = commonDao.select("Ui-factor.generateNextFactorCode");
				RequestContextHolder.setCode(factorCode);
				ft.setFactorCode(factorCode);
				commonDao.insert("Ui-factor.saveFactor", ft);
				it.remove();
			}

			List<FactorValueDto> fvLst = ft.getFactorValueLst();
			if (fvLst == null) {
				continue;
			}
			if (hasDuplicateFactorValueName(fvLst)) {
				throw new BusinessException("Factor value명이 중복되었습니다.");
			}
			for (FactorValueDto fv : fvLst) {
				fv.setFactorCode(factorCode);
			}
			factorValueLst.addAll(fvLst);
		}

		// Update FactorType
		commonDao.update("Ui-factor.updateFactorType", factorTypeDto);

		// Update Factor
		commonDao.batchInsert("Ui-factor.saveFactor", factorLst);

		// Update FactorValue
		commonDao.batchInsert("Ui-factor.saveFactorValue", factorValueLst);
	}

	private void validateUniqueFactorName(List<FactorDto> factorList) {
		if (factorList == null || factorList.isEmpty()) {
			return;
		}

		Map<String, Long> nameCount = factorList.stream()
				.collect(Collectors.groupingBy(f -> f.getFactorName().toUpperCase(), Collectors.counting()));

		List<String> duplicates = nameCount.entrySet().stream().filter(entry -> entry.getValue() > 1)
				.map(Map.Entry::getKey).collect(Collectors.toList());

		if (duplicates.isEmpty()) {
			List<String> factorNames = factorList.stream()
					.filter(factor -> factor.getFactorCode() == null || factor.getFactorCode().isEmpty())
					.map(FactorDto::getFactorName).collect(Collectors.toList());

			if (!factorNames.isEmpty()) {
				duplicates = commonDao.selectList("Ui-factor.findExistingFactorNames",
						Collections.singletonMap("factorNames", factorNames));
			}
		}

		if (!duplicates.isEmpty()) {
			throw new BusinessException(messageSource.getMessage("factor.name.duplicate",
					new Object[] { String.join(", ", duplicates) }, LocaleContextHolder.getLocale()));
		}
	}

	private boolean hasDuplicateFactorValueName(List<FactorValueDto> list) {
		if (list == null || list.isEmpty()) {
			return false;
		}

		Set<String> seen = new HashSet<>();
		for (FactorValueDto obj : list) {
			String nameLower = obj.getFactorValueName().toLowerCase();
			if (!seen.add(nameLower)) {
				return true;
			}
		}
		return false;
	}

	public List<FactorTypeDto> searchFactorInfo(SearchFactorTypeReqDto reqDto) {
		return commonDao.selectList("Ui-factor.searchFactorType", reqDto);
	}

}
