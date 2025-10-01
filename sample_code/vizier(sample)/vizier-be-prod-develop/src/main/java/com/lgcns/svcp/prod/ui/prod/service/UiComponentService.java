package com.lgcns.svcp.prod.ui.prod.service;

import static com.lgcns.svcp.prod.constant.SystemConstant.CHG_DEPT_NAME;
import static com.lgcns.svcp.prod.constant.SystemConstant.CHG_USER;
import static com.lgcns.svcp.prod.constant.SystemConstant.ITEM_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.LANG_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.OBJ_CODE;
import static com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID;
import static com.lgcns.svcp.prod.constant.SystemConstant.VALID_END_DTM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.constant.DateConstant;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.enums.ExcelType;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.mapper.ComponentMapper;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.BlngInfoMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.DcntCstcMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.DcntTrgtInfoMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.GroupedBlngInfoMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.GroupedDcntCstcMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.GroupedDcntTrgtInfoMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.GroupedQosInfoDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.GroupedSlinInfoMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.GroupedSpamInfoDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.QosInfoDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.QosMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.QosPlcyRelDDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.SlinInfoMDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.SpamInfoDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.SpamLvwuPlcyDDto;
import com.lgcns.svcp.prod.ui.prod.dto.characteristic.SpamMDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.AdditionalMultiValueDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.component.ComponentCreateInfoRes;
import com.lgcns.svcp.prod.ui.prod.dto.component.ComponentGeneralDto;
import com.lgcns.svcp.prod.ui.prod.dto.component.ComponentResourceReq;
import com.lgcns.svcp.prod.ui.prod.dto.component.CreateComponentReq;
import com.lgcns.svcp.prod.ui.prod.dto.component.addResource.ComponentAddResourceDto;
import com.lgcns.svcp.prod.ui.prod.dto.component.addResource.ComponentAddResourceReq;
import com.lgcns.svcp.prod.ui.prod.dto.component.list.ComponentSearchPagingDto;
import com.lgcns.svcp.prod.ui.prod.dto.component.list.ComponentSearchReq;
import com.lgcns.svcp.prod.ui.prod.dto.component.search.ComponentSearchAdvancedReq;
import com.lgcns.svcp.prod.ui.prod.dto.component.search.ComponentSearchRes;
import com.lgcns.svcp.prod.ui.prod.dto.component.update.ComponentUpdateReq;
import com.lgcns.svcp.prod.ui.prod.dto.component.update.ComponentUpdateResourceReq;
import com.lgcns.svcp.prod.ui.prod.dto.export.ComponentExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.export.ComponentStructureExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.export.MultiEntityExportDto;
import com.lgcns.svcp.prod.ui.prod.dto.history.save.OfferStrcHistDto;
import com.lgcns.svcp.prod.ui.prod.dto.item.Item;
import com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.EntityItemRelResDto;
import com.lgcns.svcp.prod.ui.prod.dto.offer.OfferCreateInfo;
import com.lgcns.svcp.prod.ui.prod.dto.offer.structure.OfferStructureDetailRes;
import com.lgcns.svcp.prod.ui.prod.dto.price.AlowMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.DcntRatMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.GroupedAlowMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.GroupedDcntRatMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.GroupedMfMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.GroupedPdspTossLkgeDDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.GroupedRtngDcntMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.GroupedUsfeMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.MfMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.PdspTossLkgeDDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.RtngDcntMDto;
import com.lgcns.svcp.prod.ui.prod.dto.price.UsfeMDto;
import com.lgcns.svcp.prod.ui.prod.service.admin.UiTableService;
import com.lgcns.svcp.prod.ui.prod.service.customexcel.ComponentExcelHelper;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.UuidUtil;
import com.lgcns.svcp.prod.util.excel.ExcelInput;
import com.lgcns.svcp.prod.util.paging.PageResult;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service("uiComponentService")
@RequiredArgsConstructor
public class UiComponentService {
	private final CommonDao commonDao;
	private final UiTableService uiTableService;
	private final UiCommonService uiCommonService;
	private final UIMultiEntityService uiMultiEntityService;
	private final UIHistoryService uiHistoryService;
	private final ComponentExcelHelper excelHelper;
	private final ComponentMapper componentMapper;
	private final MessageSource messageSource;

	public MfMDto retrieveMfM(MfMDto mfMDto) {
		return commonDao.select("Ui-component.retrieveMfM", mfMDto);
	}

	public void updateMfM(MfMDto mfMDto) {
		MfMDto existMfM = commonDao.select("Ui-component.retrieveMfM", mfMDto.getBasfCd());
		if (existMfM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updateMfM", mfMDto);

		if (result == -1) {
			throw new BusinessException("수정 중 오류 발생");
		}
	}

	public RtngDcntMDto retrieveRtngDcntM(RtngDcntMDto rtngDcntMDto) {
		return commonDao.select("Ui-component.retrieveRtngDcntM", rtngDcntMDto);
	}

	public void updateRtngDcntMDto(RtngDcntMDto rtngDcntMDto) {
		RtngDcntMDto existRtngDcntM = commonDao.select("Ui-component.retrieveRtngDcntM", rtngDcntMDto.getRtngDcntCd());
		if (existRtngDcntM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updateRtngDcntM", rtngDcntMDto);

		if (result == -1) {
			throw new BusinessException("수정 중 오류 발생");
		}
	}

	public UsfeMDto retrieveUsfeM(UsfeMDto usfeMDto) {
		return commonDao.select("Ui-component.retrieveUsfeM", usfeMDto);
	}

	public void updateUsfeM(UsfeMDto usfeMDto) {
		UsfeMDto existUsfeM = commonDao.select("Ui-component.retrieveUsfeM", usfeMDto.getUsfeCd());
		if (existUsfeM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updateUsfeM", usfeMDto);

		if (result == -1) {
			throw new BusinessException("수정 중 오류 발생");
		}
	}

	public DcntRatMDto retrieveDcntRatM(DcntRatMDto dcntRatMDto) {
		return commonDao.select("Ui-component.retrieveDcntRatM", dcntRatMDto);
	}

	public void updateDcntRatM(DcntRatMDto dcntRatMDto) {
		DcntRatMDto existDcntRatM = commonDao.select("Ui-component.retrieveDcntRatM", dcntRatMDto.getDcntRatCd());
		if (existDcntRatM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updateDcntRatM", dcntRatMDto);

		if (result == -1) {
			throw new BusinessException("수정 중 오류 발생");
		}
	}

	public PdspTossLkgeDDto retrievePdspTossLkgeD(PdspTossLkgeDDto pdspTossLkgeDDto) {
		return commonDao.select("Ui-component.retrievePdspTossLkgeD", pdspTossLkgeDDto);
	}

	public void updatePdspTossLkgeD(PdspTossLkgeDDto pdspTossLkgeDDto) {
		PdspTossLkgeDDto existPdspTossLkgeDM = commonDao.select("Ui-component.retrievePdspTossLkgeD",
				pdspTossLkgeDDto.getPdspCd());
		if (existPdspTossLkgeDM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updatePdspTossLkgeD", pdspTossLkgeDDto);

		if (result == -1) {
			throw new BusinessException("수정 중 오류 발생");
		}
	}

	public AlowMDto retrieveAlowM(AlowMDto alowMDto) {
		return commonDao.select("Ui-component.retrieveAlowM", alowMDto);
	}

	public void updateAlowM(AlowMDto alowMDto) {
		AlowMDto existAlowM = commonDao.select("Ui-component.retrieveAlowM", alowMDto.getAlowCd());
		if (existAlowM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updateAlowM", alowMDto);

		if (result == -1) {
			throw new BusinessException("수정 중 오류 발생");
		}
	}

	public BlngInfoMDto retrieveBlngInfoM(BlngInfoMDto blngInfoMDto) {
		return commonDao.select("Ui-component.retrieveBlngInfoM", blngInfoMDto);
	}

	public void updateBlngInfoM(BlngInfoMDto blngInfoMDto) {
		BlngInfoMDto existBlngInfoM = commonDao.select("Ui-component.retrieveBlngInfoM", blngInfoMDto.getBlngInfoCd());
		if (existBlngInfoM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updateBlngInfoM", blngInfoMDto);

		if (result == -1) {
			throw new BusinessException("수정 중 오류 발생");
		}
	}

	public SlinInfoMDto retrieveSlinInfoM(SlinInfoMDto slngInfoMDto) {
		return commonDao.select("Ui-component.retrieveSlinInfoM", slngInfoMDto);
	}

	public void updateSlinInfoM(SlinInfoMDto slinInfoMDto) {
		SlinInfoMDto existSlinInfoM = commonDao.select("Ui-component.retrieveSlinInfoM", slinInfoMDto.getSlinInfoCd());
		if (existSlinInfoM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updateSlinInfoM", slinInfoMDto);

		if (result == -1) {
			throw new BusinessException("수정 중 오류 발생");
		}
	}

	public SpamInfoDto retrieveSpamInfo(SpamMDto spamMDto) {
		return commonDao.select("Ui-component.retrieveSpamInfo", spamMDto);
	}

	public void updateSpamInfo(SpamMDto spamMDto) {
		SpamInfoDto existSpamM = commonDao.select("Ui-component.retrieveSpamInfo", spamMDto.getSpamCd());
		if (existSpamM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updateSpamInfo", spamMDto);

		if (result == -1) {
			throw new BusinessException("수정 중 오류 발생");
		}
	}

	public List<SpamLvwuPlcyDDto> retrieveSpamLvwuPlcyDList(SpamMDto spamMDto) {
		return commonDao.selectList("Ui-component.retrieveSpamLvwuPlcyDList", spamMDto);
	}

	public QosInfoDto retrieveQosInfo(QosMDto qosMDto) {
		return commonDao.select("Ui-component.retrieveQosInfo", qosMDto);
	}

	public void updateQosM(QosMDto qosMDto) {
		QosInfoDto existQosM = commonDao.select("Ui-component.retrieveQosInfo", qosMDto.getQosCd());
		if (existQosM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updateQosInfo", qosMDto);

		if (result == -1) {
			throw new BusinessException("수정 중 오류 발생");
		}
	}

	public List<QosPlcyRelDDto> retrieveQosPlcyRelDList(QosMDto qosMDto) {
		return commonDao.selectList("Ui-component.retrieveQosPlcyRelDList", qosMDto);
	}

	public DcntCstcMDto retrieveDcntCstcM(DcntCstcMDto dcntCstcMDto) {
		return commonDao.select("Ui-component.retrieveDcntCstcM", dcntCstcMDto);
	}

	public void updateDcntCstcM(DcntCstcMDto dcntCstcMDto) {
		DcntCstcMDto existDcntCstcM = commonDao.select("Ui-component.retrieveDcntCstcM", dcntCstcMDto.getDcntCstcCd());
		if (existDcntCstcM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updateDcntCstcM", dcntCstcMDto);

		if (result == -1) {
			throw new BusinessException("수정 중 오류 발생");
		}
	}

	public List<DcntTrgtInfoMDto> retrieveDcntTrgtInfoM(DcntTrgtInfoMDto dcntTrgtInfoMDto) {
		return commonDao.selectList("Ui-component.retrieveDcntTrgtInfoM", dcntTrgtInfoMDto);
	}

	// 할인대상정보는 List라 업데이트 UI 필요 07.18
	public void updateDcntTrgtInfoM(DcntTrgtInfoMDto dcntTrgtInfoMDto) {
		DcntTrgtInfoMDto existDcntTrgtInfoM = commonDao.select("Ui-component.retrieveDcntTrgtInfoM",
				dcntTrgtInfoMDto.getDcntTrgtInfoCd());
		if (existDcntTrgtInfoM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updateDcntTrgtInfoM", dcntTrgtInfoMDto);

		if (result == -1) {
			throw new BusinessException("수정 중 오류 발생");
		}
	}

	public GroupedUsfeMDto getGroupedUsfeM(UsfeMDto usfeMDto) {
		UsfeMDto rawDto = retrieveUsfeM(usfeMDto);
		// 테이블 생성전 임시
		rawDto.setType("Offer > Component > Price > Usage Charge");
		return new GroupedUsfeMDto(rawDto);
	}

	public GroupedAlowMDto getGroupedAlowM(AlowMDto alowMDto) {
		AlowMDto rawDto = retrieveAlowM(alowMDto);
		// 테이블 생성전 임시
		rawDto.setType("Offer > Component > Benefit > Allowance");
		return new GroupedAlowMDto(rawDto);
	}

	public GroupedMfMDto getGroupedMfM(MfMDto mfMDto) {
		MfMDto rawDto = retrieveMfM(mfMDto);
		// 테이블 생성전 임시
		rawDto.setType("Offer > Component > Price > Recurring Charge");
		return new GroupedMfMDto(rawDto);
	}

	public GroupedRtngDcntMDto getGroupedRtngDcntM(RtngDcntMDto rtngDcntMDto) {
		RtngDcntMDto rawDto = retrieveRtngDcntM(rtngDcntMDto);
		// 테이블 생성전 임시
		rawDto.setType("Offer > Component > Benefit > Rating Discount");
		return new GroupedRtngDcntMDto(rawDto);
	}

	public GroupedDcntRatMDto getGroupedDcntRatM(DcntRatMDto dcntRatMDto) {
		DcntRatMDto rawDto = retrieveDcntRatM(dcntRatMDto);
		// 테이블 생성전 임시
		rawDto.setType("Offer > Component > Benefit > Discount");
		return new GroupedDcntRatMDto(rawDto);
	}

	public GroupedPdspTossLkgeDDto getGroupedPdspTossLkgeD(PdspTossLkgeDDto pdspTossLkgeDDto) {
		PdspTossLkgeDDto rawDto = retrievePdspTossLkgeD(pdspTossLkgeDDto);
		// 테이블 생성전 임시
		rawDto.setType("Offer > Component > Service");
		return new GroupedPdspTossLkgeDDto(rawDto);
	}

	public GroupedBlngInfoMDto getGroupedBlngInfoM(BlngInfoMDto blngInfoMDto) {
		BlngInfoMDto rawDto = retrieveBlngInfoM(blngInfoMDto);

		// 테이블 생성전 임시
		rawDto.setType("Offer > Component > Characteristic > Billing Information");
		return new GroupedBlngInfoMDto(rawDto);
	}

	public GroupedSlinInfoMDto getGroupedSlinInfoM(SlinInfoMDto slinInfoMDto) {
		SlinInfoMDto rawDto = retrieveSlinInfoM(slinInfoMDto);

		// 테이블 생성전 임시
		rawDto.setType("Offer > Component > Characteristic > Sales Information");
		GroupedSlinInfoMDto groupedDto = new GroupedSlinInfoMDto(rawDto);
		// select 결과에 null이면 null로 처리하는 메서드 필요
		groupedDto.setAdditionalParams(null);
		return groupedDto;
	}

	public GroupedSpamInfoDto getGroupedSpamInfo(SpamMDto spamMDto) {
		SpamInfoDto rawDto = retrieveSpamInfo(spamMDto);
		List<SpamLvwuPlcyDDto> spamLvwuPlcyDDto = retrieveSpamLvwuPlcyDList(spamMDto);
		// rawDto.setSpamLvwuPlcyDDto(spamLvwuPlcyDDto);
		rawDto.setType("Offer > Component > Characteristic > SPAM Information");
		GroupedSpamInfoDto groupedDto = new GroupedSpamInfoDto(rawDto);
		groupedDto.getGeneralDetails().setLvwuPlcyCd(spamLvwuPlcyDDto.get(0).getLvwuPlcyCd());
		// select 결과에 null이면 null로 처리하는 메서드 필요
		groupedDto.setAdditionalParams(null);
		return groupedDto;
	}

	public GroupedQosInfoDto getGroupedQosInfo(QosMDto qosMDto) {
		QosInfoDto rawDto = retrieveQosInfo(qosMDto);
		List<QosPlcyRelDDto> qosPlcyRelDDto = retrieveQosPlcyRelDList(qosMDto);
		// rawDto.setQosPlcyRelDDto(qosPlcyRelDDto);
		rawDto.setType("Offer > Component > Characteristic > QoS Information");

		GroupedQosInfoDto groupedDto = new GroupedQosInfoDto(rawDto);
		groupedDto.getGeneralDetails().setQosPlcyCd(qosPlcyRelDDto.get(0).getQosPlcyCd());
		groupedDto.setAdditionalParams(null);

		return groupedDto;
	}

	public GroupedDcntCstcMDto getGroupedDcntCstcM(DcntCstcMDto dcntCstcMDto) {
		DcntCstcMDto rawDto = retrieveDcntCstcM(dcntCstcMDto);

		// 테이블 생성전 임시
		rawDto.setType("Offer > Component > Characteristic > Discount Information");
		return new GroupedDcntCstcMDto(rawDto);
	}

	public List<GroupedDcntTrgtInfoMDto> getGroupedDcntTrgtInfoM(DcntTrgtInfoMDto dcntTrgtInfoMDto) {
		List<DcntTrgtInfoMDto> rawDtoList = retrieveDcntTrgtInfoM(dcntTrgtInfoMDto);
		List<GroupedDcntTrgtInfoMDto> test = new ArrayList<GroupedDcntTrgtInfoMDto>();
		for (DcntTrgtInfoMDto rawDto : rawDtoList) {
			rawDto.setType("Offer > Component > Characteristic > Discount Information");
			GroupedDcntTrgtInfoMDto groupedDto = new GroupedDcntTrgtInfoMDto(rawDto);
			test.add(groupedDto);
		}
		// 테이블 생성전 임시

		return test;
	}

	public ComponentCreateInfoRes getCreateComponentForm(String itemCode) {
		Map<String, String> param = Collections.singletonMap(ITEM_CODE, itemCode);

		ComponentCreateInfoRes response = new ComponentCreateInfoRes();
		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-item.retrieveAdditional", param);
		uiTableService.populateTableColumnKeyValues(additional);
		response.setGeneral(general);
		response.setAdditional(additional);

		List<EntityItemRelResDto> entityItemInfos = commonDao.selectList("Ui-multiEntity.retrieveItemRelationInfo",
				param);
		response.setEntityItemInfos(entityItemInfos);

		return response;
	}

	@Transactional
	public Map<String, String> createComponent(CreateComponentReq req) {
		return createComponentTransactional(req);
	}

	@Transactional
	public Map<String, String> createComponentTransactional(CreateComponentReq req) {
		Map<String, String> generalParams = req.getGeneralParam();
		String objUuid = UuidUtil.generateRandomUUID();
		String objCode = uiCommonService.generateNextItemCode(generalParams);

		generalParams.put(OBJ_UUID, objUuid);
		generalParams.put(OBJ_CODE, objCode);

		int itemMpngResult = commonDao.insert("Ui-item.insertItemMapping", generalParams);
		if (itemMpngResult < 1) {
			throw new RuntimeException("Create Component general fail");
		}

		List<AdditionalDetailDto> additional = req.getAdditional();
		if (additional != null) {
			additional.forEach(add -> add.setObjUuid(objUuid));
			saveAdditional(additional);
		}

		List<ComponentResourceReq> resourceList = req.getResourceList();
		if (resourceList != null) {

			resourceList.forEach(rsc -> rsc.setComponentUUID(objUuid));
			commonDao.batchInsert("Ui-component.insertResourceComponent", resourceList);
		}
		uiMultiEntityService.insertObjRelation(objUuid, req.getInsertEntityObjRels());

		return new HashMap<>() {
			{
				put(OBJ_UUID, objUuid);
				put("code", objCode);
			}
		};
	}

	private void saveAdditional(List<AdditionalDetailDto> fullAdditional) {
		List<AdditionalDetailDto> dmList = fullAdditional.stream().filter(obj -> "DM".equals(obj.getFieldTypeCode()))
				.collect(Collectors.toList());

		List<AdditionalMultiValueDto> multiValList = new ArrayList<>();
		List<AdditionalMultiValueDto> multiDelList = new ArrayList<>();

		for (AdditionalDetailDto obj : dmList) {
			List<AdditionalMultiValueDto> multiValues = AdditionalMultiValueDto.parseVals(obj.getObjUuid(),
					obj.getAttrUuid(), obj.getAttrVal());
			if (multiValues != null) {
				multiValList.addAll(multiValues);
			}
			multiDelList.add(new AdditionalMultiValueDto(obj.getObjUuid(), obj.getAttrUuid(), 0, null));
		}

		fullAdditional.forEach(e -> {
			if ("DM".equals(e.getFieldTypeCode())) {
				e.setAttrVal(null);
			}
		});
		commonDao.batchUpdate("Ui-component.saveComponentAdditional", fullAdditional);

		commonDao.batchDelete("Ui-common.deleteOldAdditionalMultiValue", multiDelList);
		commonDao.batchInsert("Ui-common.insertAdditionalMultiValue", multiValList);
	}

	public List<ComponentGeneralDto> getComponentImpactAnalysis(String resourceUUID) {
		return commonDao.selectList("Ui-component.getComponentImpactAnalysis",
				Collections.singletonMap("uuid", resourceUUID));

	}

	public ItemMappingDetailDto getComponentDetail(String objUuid, String language) {
		Map<String, String> param = new HashMap<>();
		param.put(OBJ_UUID, objUuid);
		if (language != null) {
			param.put(LANG_CODE, language);
		}
		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-component.getComponentDetailAdditional", param);
		uiTableService.populateTableColumnKeyValues(additional);
		ItemMappingDetailDto response = new ItemMappingDetailDto();
		response.setGeneral(general);
		response.setAdditional(additional);

		return response;
	}

	public PageResult<?> getResourceAddForComponent(ComponentAddResourceReq addResourceReq) {
		ComponentAddResourceDto addResourceDto = new ComponentAddResourceDto();

		addResourceDto.setSize(addResourceReq.getSize());
		addResourceDto.setPage(addResourceReq.getPage());

		if (addResourceReq.getComponentUUID() != null) {
			addResourceDto.setComponentUUID(addResourceReq.getComponentUUID());
		}

		if (Objects.nonNull(addResourceReq.getItemCode())) {
			addResourceDto.setItemCode(addResourceReq.getItemCode());
		}
		addResourceDto.setName(addResourceReq.getName());
		addResourceDto.setCode(addResourceReq.getCode());
		addResourceDto.setComponentCreateType(addResourceReq.getComponentCreateType());

		return commonDao.selectPagedList("Ui-component.getComponentAddResourceGeneral", addResourceDto);
	}

	public PageResult<?> getComponentList(ComponentSearchReq req) {
		ComponentSearchPagingDto searchDto = new ComponentSearchPagingDto();
		searchDto.setCode(req.getCode());
		searchDto.setName(req.getName());
		searchDto.setOfferUUID(req.getOfferUUID());
		searchDto.setOnlyValidDtm(req.isOnlyValidDtm());
		if (req.getComponentType() != null) {
			searchDto.setType(req.getComponentType().name());
		}
		if (req.getComponentSubType() != null) {
			searchDto.setSubType(req.getComponentSubType().name());
		}

		searchDto.setSize(req.getSize());
		searchDto.setPage(req.getPage());

		return commonDao.selectPagedList("Ui-component.getComponentSearchAll",
				searchDto);
	}

	@Transactional
	public void updateComponent(ComponentUpdateReq req) {
		Map<String, String> generalParams = req.getGeneralParam();
		List<AdditionalDetailDto> additional = req.getAdditional();

		String endDate = null;
		if (generalParams.containsKey(VALID_END_DTM)) {
			endDate = generalParams.get(VALID_END_DTM);
		}

		// Get itemCode and convert to enum ComponentItemCode
		if (!generalParams.containsKey(ITEM_CODE) || generalParams.get(ITEM_CODE) == null) {
			throw new BusinessException("Component Type Not Found");
		}

		// Save history
		long workNo = DateUtil.generateWorkNo();
		String objUuid = generalParams.get(OBJ_UUID);
		String language = null;
		ItemMappingDetailDto oldComponent = getComponentDetail(objUuid, language);
		uiHistoryService.saveHistoryGeneral(workNo, generalParams, oldComponent);
		uiHistoryService.saveHistoryAdditional(workNo, req, oldComponent.getAdditional());

		saveHistoryStruc(workNo, req);

		List<ComponentUpdateResourceReq> currentResources = commonDao
				.selectList("Ui-component.getAllCurrentResourceByComponentUUID", generalParams);
		List<String> currentResourceUUID = currentResources.stream().map(ComponentUpdateResourceReq::getResourceUUID)
				.toList();
		List<ComponentUpdateResourceReq> insertResources = req.getResources().stream()
				.filter(cpn -> !currentResourceUUID.contains(cpn.getResourceUUID())).toList();
		List<ComponentUpdateResourceReq> updateResources = req.getResources().stream()
				.filter(cpn -> currentResourceUUID.contains(cpn.getResourceUUID())).toList();
		int update = commonDao.batchUpdate("Ui-component.updateCurrentResourceByComponentUUID", updateResources);
		int insert = commonDao.batchInsert("Ui-component.insertResourceComponent", insertResources);
		if (!req.getResources().isEmpty() && ((update + insert) != req.getResources().size())) {
			throw new BusinessException("Update Resource for component Fail!");
		}

		if (endDate != null) {
			Date curEndDdate = commonDao.select("Ui-common.getEndDateByObjUUID", generalParams);
			Date reqEndDate = DateUtil.convertToDateByDatabaseFormat(endDate);
			if (curEndDdate == null || curEndDdate.compareTo(reqEndDate) != 0) {
				int updateRelationWithResource = commonDao.update("Ui-component.updateComponentResourceRelationDate",
						generalParams);

				int updateRelationWithOffer = commonDao.update("Ui-component.updateComponentOfferRelationDate",
						generalParams);
			}
		}

		int generalResultItemMpng = commonDao.update("Ui-item.updateItemMapping", generalParams);

		if (generalResultItemMpng < 1) {
			throw new BusinessException("Update fail! Component Not Found");
		}

		if (additional != null) {
			additional.forEach(add -> add.setObjUuid(objUuid));
			saveAdditional(additional);
		}

		uiMultiEntityService.insertObjRelation(generalParams.get(OBJ_UUID), req.getInsertEntityObjRels());
		uiMultiEntityService.updateObjRelation(generalParams.get(OBJ_UUID), req.getUpdateEntityObjRels());
	}

	private void saveHistoryStruc(long workNo, ComponentUpdateReq updateReq) {
		List<ComponentUpdateResourceReq> resourceRel = updateReq.getResources();
		if (CollectionUtils.isEmpty(resourceRel)) {
			return;
		}
		Map<String, String> generalParam = updateReq.getGeneralParam();

		String objUuid = generalParam.get(OBJ_UUID);
		String chgDeptName = generalParam.get(CHG_DEPT_NAME);
		String chgUser = generalParam.get(CHG_USER);

		List<OfferStrcHistDto> insertOfferGroupRelList = resourceRel.stream()
				.map(rel -> new OfferStrcHistDto(workNo, objUuid, rel.getResourceUUID(), rel.getValidStartDtm(),
						rel.getValidEndDtm(), rel.getWorkTypeCode(), chgDeptName, chgUser))
				.collect(Collectors.toList());
		uiHistoryService.saveHistoryOfferStruc(insertOfferGroupRelList);
	}

	public List<Item> retrieveAllTypeComponent() {
		return commonDao.selectList("Ui-component.retrieveAllTypeComponent");
	}

	public PageResult<?> retrieveComponentsAdvanced(ComponentSearchAdvancedReq searchAdvancedReq) {
		return commonDao.selectPagedList("Ui-component.retrieveComponentsAdvanced", searchAdvancedReq);
	}

	public PageResult<?> retrieveComponentsAdvancedDetail(ComponentSearchAdvancedReq searchAdvancedReq) {
		return commonDao.selectPagedList("Ui-component.retrieveComponentsAdvancedWithDetail", searchAdvancedReq);
	}

	public void export(ComponentSearchAdvancedReq req, HttpServletResponse response) {
		List<ComponentSearchRes> components = commonDao.selectList("Ui-component.retrieveComponentsAdvanced", req);

		List<String> objUuids = components.stream().map(ComponentSearchRes::getObjUuid).toList();
		Map<String, Object> param = Map.of("objUuids", objUuids, LANG_CODE, req.getLanguage());

		List<GeneralDetailDto> generals = commonDao.selectList("Ui-item.retrieveGenerals", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-component.getComponentDetailAdditionals",
				param);

		Map<String, List<GeneralDetailDto>> generalMap = generals.parallelStream().filter(g -> g.getObjUuid() != null)
				.collect(Collectors.groupingBy(GeneralDetailDto::getObjUuid));

		Map<String, List<AdditionalDetailDto>> additionalMap = additional.parallelStream()
				.filter(a -> a.getObjUuid() != null).collect(Collectors.groupingBy(AdditionalDetailDto::getObjUuid));

		components.forEach(offer -> {
			String objUuid = offer.getObjUuid();
			offer.setGeneral(generalMap.getOrDefault(objUuid, List.of()));
			offer.setAdditional(additionalMap.getOrDefault(objUuid, List.of()));
		});

		List<ComponentExportDto> results = IntStream.range(0, components.size()).parallel().mapToObj(i -> {
			ComponentExportDto exportDto = componentMapper.componentToExportDto(components.get(i));
			exportDto.setIndex(i + 1);
			return exportDto;
		}).toList();

		ItemMappingDetailDto createInfo = getOfferCreateInfo(req.getItemCode(), req.getLanguage());
		ComponentExportDto header = new ComponentExportDto();
		header.setGeneral(createInfo.getGeneral());
		header.setAdditional(createInfo.getAdditional());
		List<ComponentStructureExportDto> structures = commonDao
				.selectList("Ui-resource.getResourceGeneralForComponentDetails", param);
		if (structures != null && !structures.isEmpty()) {
			int i1 = 0;
			for (ComponentStructureExportDto structureExportDto : structures) {
				structureExportDto.setNo(i1 + 1);
				i1++;
			}
		} else {
			structures = null;
		}
		List<MultiEntityExportDto> multiEntities = new ArrayList<>();
		if (req.getMctgrItemCode().equalsIgnoreCase("BN") || req.getMctgrItemCode().equalsIgnoreCase("CH")) {
			for (String uuid : objUuids) {
				List<EntityItemRelResDto> dtos = commonDao.selectList("Ui-multiEntity.retrieveItemRelationExcel", uuid);
				for (EntityItemRelResDto entityItemRelResDto : dtos) {
					if (entityItemRelResDto.getMultiEntityExportDtos() != null
							&& !entityItemRelResDto.getMultiEntityExportDtos().isEmpty()) {
						multiEntities.addAll(entityItemRelResDto.getMultiEntityExportDtos());
					}
				}
			}
			if (multiEntities != null && !multiEntities.isEmpty()) {
				int i = 0;
				for (MultiEntityExportDto multiEntityExportDto : multiEntities) {
					multiEntityExportDto.setNo(i + 1);
					i++;
				}
			} else {
				multiEntities = null;
			}
		}
		Item item = commonDao.select("Ui-item.getItemByItemCode", req.getItemCode());
		excelHelper.downloadExcel(buildExcelInput(results, header, structures, multiEntities, req.getMctgrItemCode(), item.getItemName()),
				response, false);
	}

	private ExcelInput buildExcelInput(List<?> datas, ComponentExportDto header,
			List<ComponentStructureExportDto> structures, List<MultiEntityExportDto> multiEntities, String itemCode, String itemName) {
		ExcelInput excelInput = new ExcelInput();
		excelInput.setExtention(ExcelType.xlsx.toString());
		excelInput.setFileName("component");
		excelInput.setSheetName("Component");
		excelInput.setFormatDate(DateConstant.YYYYMMDD);
		excelInput.setDatas(datas);
		Object[] objectArray = new Object[5];
		objectArray[0] = header;
		objectArray[1] = structures;
		objectArray[2] = multiEntities;
		objectArray[3] = itemCode;
		objectArray[4] = itemName;
		excelInput.setObject(objectArray);
		return excelInput;
	}

	public OfferCreateInfo getOfferCreateInfo(String itemCode, String langCode) {
		Map<String, String> param = Map.of(ITEM_CODE, itemCode, LANG_CODE, langCode);

		OfferCreateInfo response = new OfferCreateInfo();
		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDto> additional = commonDao.selectList("Ui-item.retrieveAdditional", param);
		List<OfferStructureDetailRes> structures = commonDao.selectList("Ui-offer.retreiveStructureInfo", param);

		uiTableService.populateTableColumnKeyValues(additional);
		// add category field
		GeneralDetailDto ctgrField = new GeneralDetailDto();
		ctgrField.setColName("ctgr_node_uuid");
		ctgrField.setFieldTypeCode("TF");
		ctgrField.setLabelId("categoryNode");
		general.add(ctgrField);

		response.setGeneral(general);
		response.setAdditional(additional);
		response.setStructures(structures);

		return response;
	}
}
