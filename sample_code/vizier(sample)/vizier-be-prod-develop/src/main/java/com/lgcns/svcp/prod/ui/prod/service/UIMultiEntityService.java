package com.lgcns.svcp.prod.ui.prod.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.lgcns.svcp.prod.context.RequestContextHolder;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.ui.prod.dto.common.AdditionalMultiValueDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.SelectOptionDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.MultiEntityAdditionalDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.MultiEntityDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.create.CreateEntityReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.EntityItemRelResDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.EntityScopeDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.SaveEntityObjRelReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.search.SearchItemRelationReq;
import com.lgcns.svcp.prod.ui.prod.dto.multiEntity.search.SearchMultiEntityReqDto;
import com.lgcns.svcp.prod.ui.prod.enums.entity.EntityScope;
import com.lgcns.svcp.prod.ui.prod.enums.entity.EntityTypeCode;
import com.lgcns.svcp.prod.util.paging.PageResult;

import lombok.RequiredArgsConstructor;

@Service("uiMultiEntityService")
@RequiredArgsConstructor
public class UIMultiEntityService {

	private final CommonDao commonDao;

	private static final int MAXIMUM_ENTITY_OF_SINGLE_RELATION = 1;

	public List<SelectOptionDto> retrieveSearchInfo() {
		return commonDao.selectList("Ui-multiEntity.retrieveMultiEntitySearchInfo");
	}

	public PageResult<?> retrieveMultiEntityList(SearchMultiEntityReqDto reqDto) {
		return commonDao.selectPagedList("Ui-multiEntity.retrieveMultiEntityList", reqDto);
	}

	public MultiEntityDto retrieveMultiEntityDetail(String entityCode, String entityTypeCode, String langCode) {
		EntityTypeCode typeCode = EntityTypeCode.getEnumFromCode(entityTypeCode);
		Map<String, Object> params = new HashMap<>();
		params.put("entityCode", entityCode);
		params.put("langCode", langCode);
		return switch (typeCode) {
		case EBL -> commonDao.select("Ui-multiEntity.retrieveEBLDetail", params);
		case EDT -> commonDao.select("Ui-multiEntity.retrieveEDTDetail", params);
		case ESC -> commonDao.select("Ui-multiEntity.retrieveESCDetail", params);
		case ETC -> commonDao.select("Ui-multiEntity.retrieveETCDetail", params);
		default -> throw new BusinessException("엔티티타입코드를 확인해주세요");
		};
	}

	public List<MultiEntityAdditionalDto> retrieveCreateInfo(String entityTypeCode, String langCode) {
		Map<String, Object> params = new HashMap<>();
		params.put("entityTypeCode", entityTypeCode);
		params.put("langCode", langCode);
		return commonDao.selectList("Ui-multiEntity.retrieveAdditionalByEntityTypeCode", params);
	}

	@Transactional
	public synchronized Map<String, String> createMultiEntity(CreateEntityReqDto createEntityResDto) {
		return createMultiEntityTransactional(createEntityResDto);
	}

	@Transactional
	public Map<String, String> createMultiEntityTransactional(CreateEntityReqDto createEntityResDto) {
		EntityTypeCode entityTypeCode = EntityTypeCode.getEnumFromCode(createEntityResDto.getEntityTypeCode());
		if (entityTypeCode == EntityTypeCode.UNKNOWN) {
			throw new BusinessException("엔티티타입코드를 확인해주세요");
		}

		String nextCode = commonDao.select("Ui-multiEntity.retrieveNextCodeEntity", entityTypeCode.getPrefix());
		RequestContextHolder.setCode(nextCode);
		createEntityResDto.setEntityCode(nextCode);

		commonDao.insert("Ui-multiEntity.insertEntityMpng", createEntityResDto);

		if (entityTypeCode == EntityTypeCode.EBL) {
			commonDao.insert("Ui-multiEntity.insertEntityBsnLine", createEntityResDto);
		} else if (entityTypeCode == EntityTypeCode.EDT) {
			commonDao.insert("Ui-multiEntity.insertEntityDcTrgt", createEntityResDto);
		} else if (entityTypeCode == EntityTypeCode.ESC) {
			commonDao.insert("Ui-multiEntity.insertEntitySaleCpny", createEntityResDto);
		}

		if (!CollectionUtils.isEmpty(createEntityResDto.getAdditional())) {
			createEntityResDto.getAdditional()
					.forEach(additional -> additional.setEntityCode(createEntityResDto.getEntityCode()));
			saveAdditional(createEntityResDto.getAdditional());
		}

		Map<String, String> res = new HashMap<>();
		res.put("entityCode", nextCode);
		res.put("entityTypeCode", entityTypeCode.getPrefix());
		return res;
	}

	@Transactional
	public void updateMultiEntity(CreateEntityReqDto saveEntityReqDto) {
		String langCode = null;
		MultiEntityDto oldEntityDto = retrieveMultiEntityDetail(saveEntityReqDto.getEntityCode(),
				saveEntityReqDto.getEntityTypeCode(), langCode);
		if (oldEntityDto == null) {
			throw new BusinessException("멀티엔티티를다시확인해주세요");
		}

		EntityTypeCode typeCode = EntityTypeCode.getEnumFromCode(oldEntityDto.getEntityTypeCode());
		commonDao.update("Ui-multiEntity.updateEntityMpng", saveEntityReqDto);

		if (typeCode == EntityTypeCode.EBL) {
			commonDao.update("Ui-multiEntity.updateEntityBsnLine", saveEntityReqDto);
		} else if (typeCode == EntityTypeCode.EDT) {
			commonDao.update("Ui-multiEntity.updateEntityDcTrgt", saveEntityReqDto);
		} else if (typeCode == EntityTypeCode.ESC) {
			commonDao.update("Ui-multiEntity.updateEntitySaleCpny", saveEntityReqDto);
		}

		if (!CollectionUtils.isEmpty(saveEntityReqDto.getAdditional())) {

			saveEntityReqDto.getAdditional()
					.forEach(additional -> additional.setEntityCode(saveEntityReqDto.getEntityCode()));
			saveAdditional(saveEntityReqDto.getAdditional());
		}

	}

	public List<EntityItemRelResDto> retrieveItemRelation(SearchItemRelationReq req) {
		return commonDao.selectList("Ui-multiEntity.retrieveItemRelation", req);
	}

	@Transactional
	public void insertObjRelation(String objUuid, List<SaveEntityObjRelReqDto> insertEntityObjRelReqDtos) {
		if (insertEntityObjRelReqDtos == null || insertEntityObjRelReqDtos.isEmpty()) {
			return;
		}
		insertEntityObjRelReqDtos.forEach(e -> e.setObjUuid(objUuid));

		List<String> entityCodes = insertEntityObjRelReqDtos.stream().map(SaveEntityObjRelReqDto::getEntityCode)
				.toList();

		List<EntityScopeDto> entityScopeDtos = commonDao.selectList("Ui-multiEntity.retrieveEntityScopeByEntityCode",
				Collections.singletonMap("entityCodes", entityCodes));

		long countSingleEntity = entityScopeDtos.stream().filter(es -> es.getEntityScope() == EntityScope.S).count();

		if (countSingleEntity > MAXIMUM_ENTITY_OF_SINGLE_RELATION) {
			throw new BusinessException("Entity Scope이 Single이므로 2개 이상의 Entity가 존재할 수 없습니다.");
		}
//		} else if (countSingleEntity == MAXIMUM_ENTITY_OF_SINGLE_RELATION) {
//			EntityObjRelResDto oldSingleRel = commonDao.select("Ui-multiEntity.retrieveSingleObjRelationOfItem",
//					objUuid);
//			if (oldSingleRel != null) {
//				EntityScopeDto entityScope = entityScopeDtos.stream().filter(es -> es.getEntityScope() == EntityScope.S)
//						.findFirst().orElse(null);
//
//				SaveEntityObjRelReqDto replaceEntityObjRel = insertEntityObjRelReqDtos.stream()
//						.filter(es -> entityScope.getEntityCode().equals(es.getEntityCode())).findFirst().orElse(null);
//
//				insertEntityObjRelReqDtos.remove(replaceEntityObjRel);
//
//				replaceEntityObjRel.setOldEntityCode(oldSingleRel.getEntityCode());
//				commonDao.update("Ui-multiEntity.replaceObjRelation", replaceEntityObjRel);
//			}
//		}
		commonDao.batchInsert("Ui-multiEntity.insertObjRelation", insertEntityObjRelReqDtos);
	}

	public void updateObjRelation(String objUuid, List<SaveEntityObjRelReqDto> updateEntityObjRelReqDtos) {
		if (updateEntityObjRelReqDtos == null || updateEntityObjRelReqDtos.isEmpty()) {
			return;
		}
		updateEntityObjRelReqDtos.forEach(e -> e.setObjUuid(objUuid));
		commonDao.batchInsert("Ui-multiEntity.updateObjRelation", updateEntityObjRelReqDtos);
	}

	private void saveAdditional(List<MultiEntityAdditionalDto> fullAdditional) {
		List<MultiEntityAdditionalDto> dmList = fullAdditional.stream()
				.filter(obj -> "DM".equals(obj.getFieldTypeCode())).collect(Collectors.toList());

		List<AdditionalMultiValueDto> multiValList = new ArrayList<>();
		List<AdditionalMultiValueDto> multiDelList = new ArrayList<>();

		for (MultiEntityAdditionalDto obj : dmList) {
			List<AdditionalMultiValueDto> multiValues = AdditionalMultiValueDto.parseVals(obj.getEntityCode(),
					obj.getAttrUuid(), obj.getAttrVal());
			if (multiValues != null) {
				multiValList.addAll(multiValues);
			}
			multiDelList.add(new AdditionalMultiValueDto(obj.getEntityCode(), obj.getAttrUuid(), 0, null));
		}

		fullAdditional.forEach(e -> {
			if ("DM".equals(e.getFieldTypeCode())) {
				e.setAttrVal(null);
			}
		});
		commonDao.batchUpdate("Ui-multiEntity.saveAdditional", fullAdditional);

		commonDao.batchDelete("Ui-common.deleteOldAdditionalMultiValue", multiDelList);
		commonDao.batchInsert("Ui-common.insertAdditionalMultiValue", multiValList);
	}
}
