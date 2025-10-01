package com.lgcns.svcp.prod.ui.prod.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.context.UserContext;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.ui.prod.dto.publish.aprv.AprvFlowTmptMDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.aprv.AprvFlowTmptMSearchReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.aprv.AprvFlowTmptStepLDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.aprv.AprvFlowTmptSubStepLDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.item.ChngDataListLDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.item.ChngDataListLSearchReqDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.packages.PubAprvMDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.packages.PubAprvStepLDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.packages.PubAprvSubStepLDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.packages.PubPackageDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.packages.PubPrcsTaskMDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.packages.PubRqstTaskLDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.packages.PubRqstTaskMDto;
import com.lgcns.svcp.prod.ui.prod.dto.publish.packages.PubRqstTaskMSearchReqDto;
import com.lgcns.svcp.prod.ui.prod.enums.YesNo;
import com.lgcns.svcp.prod.ui.prod.enums.publish.AprvStepCode;
import com.lgcns.svcp.prod.ui.prod.enums.publish.AprvStusCode;
import com.lgcns.svcp.prod.ui.prod.enums.publish.ChngDataStusCode;
import com.lgcns.svcp.prod.ui.prod.enums.publish.PubPrcsStusCode;
import com.lgcns.svcp.prod.ui.prod.enums.publish.PubPrcsTypeCode;
import com.lgcns.svcp.prod.ui.prod.enums.publish.PubRqstStusCode;
import com.lgcns.svcp.prod.ui.prod.enums.publish.PubRqstTaskDetlStusCode;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.StringUtilCustom;
import com.lgcns.svcp.prod.util.UuidUtil;
import com.lgcns.svcp.prod.util.paging.PageResult;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UIPublishService {
	private final CommonDao commonDao;
	private final UINotificationService uiNotificationService;

	public PageResult<?> searchChngDataDetail(ChngDataListLSearchReqDto reqDto) {
		return commonDao.selectPagedList("Ui-publish.searchChngDataDetail", reqDto);
	}

	public PageResult<?> searchPubRqstTaskMaster(PubRqstTaskMSearchReqDto reqDto) {
		// Trigger updatePubRqstTaskStatus
		commonDao.update("Ui-publish.updatePubRqstTaskStatus");

		return commonDao.selectPagedList("Ui-publish.searchPubRqstTaskMaster", reqDto);
	}

	public PubPackageDto retrievePackage(String pubRqstTaskCode) {
		Map<String, String> params = new HashMap<>();
		params.put("pubRqstTaskCode", pubRqstTaskCode);

		// Trigger updatePubRqstTaskStatus
		commonDao.update("Ui-publish.updatePubRqstTaskStatus", params);

		// General
		PubRqstTaskMDto pubRqstTaskMDto = commonDao.select("Ui-publish.retrievePubRqstTaskM", params);

		// Compose
		List<ChngDataListLDto> chngDataListLDtos = commonDao.selectList("Ui-publish.retrievePubReqTaskLOfPackage",
				params);

		// Approval
		PubAprvMDto pubAprvMDto = getPubAprvMDto(pubRqstTaskCode);
		// Publish
		PubPrcsTaskMDto pubPrcsTaskMDto = commonDao.select("Ui-publish.retrievePubPrcsTaskM", params);

		PubPackageDto pubPackageDto = new PubPackageDto();
		pubPackageDto.setPubRqstTaskMDto(pubRqstTaskMDto);
		pubPackageDto.setChngDataLstDtos(chngDataListLDtos);
		pubPackageDto.setPubAprvMDto(pubAprvMDto);
		pubPackageDto.setPubPrcsTaskMDto(pubPrcsTaskMDto);
		return pubPackageDto;
	}

	private PubAprvMDto getPubAprvMDto(String pubRqstTaskCode) {
		Map<String, String> params = Collections.singletonMap("pubRqstTaskCode", pubRqstTaskCode);
		PubAprvMDto pubAprvMDto = commonDao.select("Ui-publish.retrievePubAprvM", params);
		if (pubAprvMDto != null) {
			String pubAprvUuid = pubAprvMDto.getPubAprvUuid();
			Map<String, String> uuidParam = Collections.singletonMap("pubAprvUuid", pubAprvUuid);

			List<PubAprvStepLDto> stepList = commonDao.selectList("Ui-publish.retrievePubAprvStepL", uuidParam);
			List<PubAprvSubStepLDto> subStepList = commonDao.selectList("Ui-publish.retrievePubAprvSubStepL",
					uuidParam);

			// ===== Sort lists =====
			// Sort steps by sortNo ascending
			if (stepList != null) {
				stepList.sort(Comparator.comparing(PubAprvStepLDto::getSortNo));
			}

			// Sort sub-steps by sortNo ascending, then subSortNo ascending
			if (subStepList != null) {
				subStepList.sort(Comparator.comparing(PubAprvSubStepLDto::getSortNo)
						.thenComparing(PubAprvSubStepLDto::getSubSortNo));
			}

			// ===== Group sub-steps by sortNo while keeping the sorted order =====
			Map<Long, List<PubAprvSubStepLDto>> subsBySortNo = (subStepList == null
					? Collections.<PubAprvSubStepLDto>emptyList()
					: subStepList).stream()
					.collect(Collectors.groupingBy(PubAprvSubStepLDto::getSortNo, LinkedHashMap::new, // preserve sorted
																										// order
							Collectors.toList()));

			// Assign sub-steps to their corresponding step
			if (stepList != null) {
				for (PubAprvStepLDto step : stepList) {
					List<PubAprvSubStepLDto> subs = subsBySortNo.getOrDefault(step.getSortNo(),
							Collections.emptyList());
					step.setPubAprvSubStepLDtos(subs);
				}
				pubAprvMDto.setPubAprvStepLDtos(stepList);
			}
		}

		return pubAprvMDto;
	}

	@Transactional
	public synchronized String createGeneralPublishPackage(PubPackageDto pubPackageDto) {
		return createGeneralPublishPackageTrans(pubPackageDto);
	}

	// 2. Publish Package Create
	@Transactional
	public String createGeneralPublishPackageTrans(PubPackageDto pubPackageDto) {
		String pubRqstTaskCode = commonDao.select("Ui-publish.generateNextPubRqstTaskCode");
		String nowDateTime = LocalDateTime.now().toString();

		PubRqstTaskMDto pubRqstTaskMDto = pubPackageDto.getPubRqstTaskMDto();
		pubRqstTaskMDto.setPubRqstTaskCode(pubRqstTaskCode);
		pubRqstTaskMDto.setPubRqstStusCode(PubRqstStusCode.C);
		pubRqstTaskMDto.setPubPrcsTypeCode(PubPrcsTypeCode.M);
		pubRqstTaskMDto.setCrteDtm(nowDateTime);

		String pubAprvUuid = UuidUtil.generateRandomUUID();
		PubAprvMDto aprvMDto = new PubAprvMDto();
		aprvMDto.setPubAprvUuid(pubAprvUuid);
		aprvMDto.setPubRqstTaskCode(pubRqstTaskCode);
		aprvMDto.setPubAprvDscr(pubRqstTaskMDto.getPubRqstTaskCodeName());
		aprvMDto.setAprvFlowTmptCode("APRV000000");
		aprvMDto.setAprvFlowTmptName("미승인 프로세스");
		aprvMDto.setUseYn(YesNo.Y);

		PubAprvStepLDto aprvStepLDto = new PubAprvStepLDto();
		aprvStepLDto.setPubAprvUuid(pubAprvUuid);
		aprvStepLDto.setSortNo(1);
		aprvStepLDto.setPubAprvStepCode(AprvStepCode.D);
		aprvStepLDto.setAprvStusCode(AprvStusCode.APR);
		aprvStepLDto.setAprvDtm(nowDateTime);
		aprvStepLDto.setLmtTm(0);
		aprvStepLDto.setUseYn(YesNo.Y);

		commonDao.insert("Ui-publish.savePubRqstTaskM", pubRqstTaskMDto);
		commonDao.insert("Ui-publish.savePubAprvM", aprvMDto);
		commonDao.insert("Ui-publish.insertPubAprvStepL", aprvStepLDto);
		return pubRqstTaskCode;
	}

	@Transactional
	public void updateGeneralPublishPackage(PubPackageDto pubPackageDto) {
		PubRqstTaskMDto pubRqstTaskMDto = pubPackageDto.getPubRqstTaskMDto();
		String nowDateTime = LocalDateTime.now().format(DateUtil.SOURCE_FORMATTER);
		String pubRqstTaskCode = pubRqstTaskMDto.getPubRqstTaskCode();

		Map<String, String> params = Collections.singletonMap("pubRqstTaskCode", pubRqstTaskCode);
		boolean isExpr = commonDao.select("Ui-publish.pubRqstTaskMIsExpr", params);
		if (isExpr) {

			String newExprDtm = pubRqstTaskMDto.getExprDtm();
			PubRqstTaskMDto oldPubRqstTaskMDto = commonDao.select("Ui-publish.retrievePubRqstTaskM", params);
			if ((oldPubRqstTaskMDto.getPubRqstStusCode() == PubRqstStusCode.D
					|| oldPubRqstTaskMDto.getPubRqstStusCode() == PubRqstStusCode.I)
					&& (StringUtilCustom.isEmpty(newExprDtm) || DateUtil.isValidStartDateEndDate(nowDateTime,
							newExprDtm.replaceAll("\\s[+-]\\d{2}:\\d{2}$", "")))) {

				PubAprvMDto getPubAprvMDto = getPubAprvMDto(pubRqstTaskCode);
				getPubAprvMDto.setPubAprvRqsttDtm(nowDateTime);

				pubPackageDto.setPubRqstTaskMDto(oldPubRqstTaskMDto);
				pubPackageDto.setPubAprvMDto(getPubAprvMDto);
				saveApprovePublishPackage(pubPackageDto);
			}
		}
		commonDao.update("Ui-publish.savePubRqstTaskM", pubRqstTaskMDto);
	}

	// 3. Publish Package Compose
	@Transactional
	public void saveComposePublishPackage(PubPackageDto pubPackageDto) {
		String pubRqstTaskCode = pubPackageDto.getPubRqstTaskCode();
		Map<String, String> params = Collections.singletonMap("pubRqstTaskCode", pubRqstTaskCode);

		List<ChngDataListLDto> chngDataListLDtos = pubPackageDto.getChngDataLstDtos();
		List<ChngDataListLDto> oldChngDataListLDtos = commonDao.selectList("Ui-publish.retrievePubReqTaskLOfPackage",
				params);

		List<Long> changeDataSeqsToDelete = new ArrayList<>();

		if (oldChngDataListLDtos != null && !oldChngDataListLDtos.isEmpty()) {
			if (chngDataListLDtos == null || chngDataListLDtos.isEmpty()) {
				changeDataSeqsToDelete = oldChngDataListLDtos.stream().map(ChngDataListLDto::getChngDataSeq)
						.filter(Objects::nonNull).collect(Collectors.toList());
			} else {
				Set<Long> newSeqSet = chngDataListLDtos.stream().map(ChngDataListLDto::getChngDataSeq)
						.filter(Objects::nonNull).collect(Collectors.toSet());

				changeDataSeqsToDelete = oldChngDataListLDtos.stream().map(ChngDataListLDto::getChngDataSeq)
						.filter(Objects::nonNull).filter(oldSeq -> !newSeqSet.contains(oldSeq))
						.collect(Collectors.toList());

				chngDataListLDtos.forEach(e -> e.setChngDataStusCode(ChngDataStusCode.SAVED));
				commonDao.batchUpdate("Ui-publish.updateChngDataStusCode", chngDataListLDtos);
			}
			if (!changeDataSeqsToDelete.isEmpty()) {
				commonDao.delete("Ui-publish.deleteOldPubRqstTaskL", changeDataSeqsToDelete);
			}
		}

		if (chngDataListLDtos != null && !chngDataListLDtos.isEmpty()) {
			chngDataListLDtos.forEach(e -> e.setChngDataStusCode(ChngDataStusCode.PACKED));
			commonDao.batchUpdate("Ui-publish.updateChngDataStusCode", chngDataListLDtos);

			List<PubRqstTaskLDto> pubRqstTaskLDtos = chngDataListLDtos.stream().map(e -> {
				PubRqstTaskLDto dto = new PubRqstTaskLDto();
				dto.setPubRqstTaskCode(pubRqstTaskCode);
				dto.setChngDataSeq(e.getChngDataSeq());
				dto.setPubRqstDetlStusCode(PubRqstTaskDetlStusCode.PACKED);
				dto.setVldateFnshYn(YesNo.N);
				return dto;
			}).collect(Collectors.toList());
			commonDao.batchInsert("Ui-publish.savePubRqstTaskL", pubRqstTaskLDtos);
		}

		if (!changeDataSeqsToDelete.isEmpty() || chngDataListLDtos != null && !chngDataListLDtos.isEmpty())
			updatePublicationRequestStatus(pubRqstTaskCode, PubRqstStusCode.M);
	}

	// 4. Publish Package VALIDATE
	@Transactional
	public YesNo validatePublishPackage(PubPackageDto pubPackageDto) {
		// START dummy validate fail
		if (pubPackageDto.getPubRqstTaskMDto().getPubRqstTaskCodeName().contains("fail")) {
			return YesNo.N;
		}
		// END dummy validate fail

		List<PubRqstTaskLDto> pubRqstTaskLDtos = commonDao.selectList("Ui-publish.retrievePubRqstTaskLsts",
				pubPackageDto);
		if (pubRqstTaskLDtos != null) {
			// TODO handle validate

			pubRqstTaskLDtos.forEach(e -> {
				e.setVldateFnshYn(YesNo.Y);
			});
			commonDao.batchInsert("Ui-publish.savePubRqstTaskL", pubRqstTaskLDtos);
		}

		// Update vldate_dtm
		String nowDateTime = LocalDateTime.now().toString();
		PubRqstTaskMDto pubRqstTaskMDto = commonDao.select("Ui-publish.retrievePubRqstTaskM",
				pubPackageDto.getPubRqstTaskMDto());
		pubRqstTaskMDto.setPubRqstStusCode(PubRqstStusCode.V);
		pubRqstTaskMDto.setVldateDtm(nowDateTime);

		commonDao.update("Ui-publish.savePubRqstTaskM", pubRqstTaskMDto);
		return YesNo.Y;
	}

	// 5. Publish Package에 Approval Step 추가
	@Transactional
	public void saveApprovePublishPackage(PubPackageDto pubPackageDto) {
		PubAprvMDto pubAprvMDto = pubPackageDto.getPubAprvMDto();
		if (pubAprvMDto == null) {
			return;
		}

		PubAprvMDto oldAprvMDto = commonDao.select("Ui-publish.retrievePubAprvM", pubPackageDto);
		String pubAprvUuid = oldAprvMDto.getPubAprvUuid();

		// Delete old Approval
		Map<String, String> params = Collections.singletonMap("pubAprvUuid", pubAprvUuid);
		commonDao.delete("Ui-publish.deleteOldPubAprvStepL", params);
		commonDao.delete("Ui-publish.deleteOldPubAprvSubStepL", params);

		// Update new Approval
		pubAprvMDto.setPubAprvUuid(pubAprvUuid);
		commonDao.update("Ui-publish.savePubAprvM", pubAprvMDto);

		List<PubAprvStepLDto> pubAprvStepLDtos = pubAprvMDto.getPubAprvStepLDtos();
		pubAprvStepLDtos.forEach(e -> {
			e.setPubAprvUuid(pubAprvUuid);
			e.setAprvStusCode(AprvStusCode.REQ);
		});
		List<PubAprvStepLDto> filteredSteps = pubAprvStepLDtos.stream().filter(dto -> dto.getSortNo() > 1)
				.collect(Collectors.toList());

		List<PubAprvSubStepLDto> pubAprvSubStepLDtos = filteredSteps.stream()
				.map(PubAprvStepLDto::getPubAprvSubStepLDtos).filter(Objects::nonNull).flatMap(List::stream)
				.collect(Collectors.toList());

		pubAprvSubStepLDtos.forEach(e -> {
			e.setPubAprvUuid(pubAprvUuid);
			e.setAprvStusCode(AprvStusCode.REQ);
			e.setUseYn(YesNo.Y);
		});

		commonDao.batchInsert("Ui-publish.insertPubAprvStepL", filteredSteps);
		commonDao.batchInsert("Ui-publish.savePubAprvSubStepL", pubAprvSubStepLDtos);
	}

	// 6. Publish Package에 Approval 완료
	@Transactional
	public void decidePublishPackage(PubAprvSubStepLDto pubAprvSubStepLDto) {
		String nowDateTime = LocalDateTime.now().toString();
		String pubAprvUuid = pubAprvSubStepLDto.getPubAprvUuid();
		long sortNo = pubAprvSubStepLDto.getSortNo();
		AprvStusCode aprvStusCode = pubAprvSubStepLDto.getAprvStusCode();
		String aprvStusDscr = pubAprvSubStepLDto.getAprvStusDscr();
		String aprvDtm = LocalDateTime.now().toString();

		PubAprvSubStepLDto oldPubAprvSubStepLDto = commonDao.select("Ui-publish.retrievePubAprvSubStepL",
				pubAprvSubStepLDto);
		if (oldPubAprvSubStepLDto == null) {
			throw new BusinessException("Invalid publish approval data");
		}
		if (!pubAprvSubStepLDto.getAprvUser().equals(oldPubAprvSubStepLDto.getAprvUser())
				|| !pubAprvSubStepLDto.getAprvUserDeptCd().equals(oldPubAprvSubStepLDto.getAprvUserDeptCd())) {
			throw new BusinessException("Mismatched user or department code");
		}

		if (!UserContext.getCurrentUser().equals(oldPubAprvSubStepLDto.getAprvUser())) {
			throw new BusinessException("Unauthorized user for approval");
		}

		oldPubAprvSubStepLDto.setAprvDtm(aprvDtm);
		oldPubAprvSubStepLDto.setAprvStusCode(aprvStusCode);
		oldPubAprvSubStepLDto.setAprvStusDscr(aprvStusDscr);
		commonDao.update("Ui-publish.savePubAprvSubStepL", oldPubAprvSubStepLDto);

		PubAprvStepLDto pubAprvStepLDto = new PubAprvStepLDto();
		pubAprvStepLDto.setPubAprvUuid(pubAprvUuid);
		pubAprvStepLDto.setSortNo(sortNo);
		pubAprvStepLDto.setAprvStusCode(aprvStusCode);
		pubAprvStepLDto.setAprvStusDscr(aprvStusDscr);
		pubAprvStepLDto.setAprvDtm(aprvDtm);
		commonDao.update("Ui-publish.updateStusPubAprvStepL", oldPubAprvSubStepLDto);

		// Auto publishedPackage
		String pubRqstTaskCode = commonDao.select("Ui-publish.retrievePubRqstTaskCodeOfStep", pubAprvSubStepLDto);

		boolean isAllPubAprvStepApproved = commonDao.select("Ui-publish.isAllPubAprvStepApproved", pubAprvSubStepLDto);
		if (isAllPubAprvStepApproved) {

			boolean isAutoPublish = commonDao.select("Ui-publish.isAutoPublish",
					Collections.singletonMap("pubRqstTaskCode", pubRqstTaskCode));
			PubPrcsTaskMDto pubPrcsTaskMDto = new PubPrcsTaskMDto();

			PubPackageDto pubPackageDto = new PubPackageDto();
			pubPackageDto.setPubRqstTaskCode(pubRqstTaskCode);
			pubPackageDto.setPubPrcsTaskMDto(pubPrcsTaskMDto);

			publishRequest(pubPackageDto);
			if (isAutoPublish) {
				pubPrcsTaskMDto.setPubPrcsRsvDtm(nowDateTime);
				publishedPackage(pubPackageDto, true);
			}
		}
	}

	// 7. Publish Package에 Publish Request
	private void publishRequest(PubPackageDto pubPackageDto) {
//		String nowDateTime = LocalDateTime.now().toString();
		String pubRqstTaskCode = pubPackageDto.getPubRqstTaskCode();

		PubPackageDto oldPubPackageDto = retrievePackage(pubRqstTaskCode);
		PubRqstTaskMDto pubRqstTaskMDto = oldPubPackageDto.getPubRqstTaskMDto();

		updatePublicationRequestStatus(pubRqstTaskCode, PubRqstStusCode.I);

		PubPrcsTaskMDto prcsTaskMDto = new PubPrcsTaskMDto();
		prcsTaskMDto.setPubRqstTaskCode(pubRqstTaskCode);
		prcsTaskMDto.setPubRqstTaskCodeName(pubRqstTaskMDto.getPubRqstTaskCodeName());
		prcsTaskMDto.setPubPrcsStusCode(PubPrcsStusCode.I);
//		prcsTaskMDto.setPubPrcsRsvDtm(nowDateTime);
//		prcsTaskMDto.setPubPrcsStartDtm(pubPackageDto.getPubPrcsTaskMDto().getPubPrcsStartDtm());
		commonDao.update("Ui-publish.savePubPrcsTaskM", prcsTaskMDto);
	}

	// Request Approval
	@Transactional
	public void requestApproval(PubPackageDto pubPackageDto) {
		String nowDateTime = LocalDateTime.now().toString();
		String pubRqstTaskCode = pubPackageDto.getPubRqstTaskCode();

		PubPackageDto oldPubPackageDto = retrievePackage(pubRqstTaskCode);
		PubRqstTaskMDto pubRqstTaskMDto = oldPubPackageDto.getPubRqstTaskMDto();

		// Save pubPrcsTypeCode
		commonDao.update("Ui-publish.updatePubRqstTypeCode", pubPackageDto.getPubRqstTaskMDto());

		// Save ReqAprvDtm
		PubAprvMDto pubAprvMDto = oldPubPackageDto.getPubAprvMDto();
		pubAprvMDto.setPubAprvRqsttDtm(nowDateTime);
		commonDao.update("Ui-publish.savePubAprvM", pubAprvMDto);

		// Notification for user approval
		Set<String> userApprovals = Optional.ofNullable(pubAprvMDto).map(PubAprvMDto::getPubAprvStepLDtos).stream()
				.flatMap(Collection::stream).filter(Objects::nonNull)
				.flatMap(
						step -> Optional.ofNullable(step.getPubAprvSubStepLDtos()).stream().flatMap(Collection::stream))
				.filter(Objects::nonNull).map(PubAprvSubStepLDto::getAprvUser).filter(Objects::nonNull)
				.collect(Collectors.toSet());

		String notiMsgLabelId = "product_platform.publish%";
		String notiType = "PUB";
		String linkUrl = pubRqstTaskCode;
		String imageUrl = pubRqstTaskMDto.getPubRqstTaskCodeName();
		for (String userId : userApprovals) {
			uiNotificationService.sendNotification(userId, notiMsgLabelId, notiType, linkUrl, imageUrl, userId);
		}
		updatePublicationRequestStatus(pubRqstTaskCode, PubRqstStusCode.I);
	}

	// 8. Published 완료
	@Transactional
	public void publishedPackage(PubPackageDto pubPackageDto, boolean isAuto) {
		String pubRqstTaskCode = pubPackageDto.getPubRqstTaskCode();

		// updateChngDataStusCode
		PubPackageDto oldPubPackageDto = retrievePackage(pubRqstTaskCode);
		List<ChngDataListLDto> chngDataListLDtos = oldPubPackageDto.getChngDataLstDtos();
		if (chngDataListLDtos != null) {
			chngDataListLDtos.forEach(e -> e.setChngDataStusCode(ChngDataStusCode.PUBLISH));
			commonDao.batchUpdate("Ui-publish.updateChngDataStusCode", chngDataListLDtos);
		}

		// updatePublicationRequestStatus
		updatePublicationRequestStatus(pubRqstTaskCode, PubRqstStusCode.P);

		// savePubRqstTaskL
		Map<String, String> params = Collections.singletonMap("pubRqstTaskCode", pubRqstTaskCode);
		List<PubRqstTaskLDto> pubRqstTaskLDtos = commonDao.selectList("Ui-publish.retrievePubRqstTaskLsts", params);
		if (pubRqstTaskLDtos != null) {
			pubRqstTaskLDtos.forEach(e -> e.setPubRqstDetlStusCode(PubRqstTaskDetlStusCode.PUBLISHED));
			commonDao.batchInsert("Ui-publish.savePubRqstTaskL", pubRqstTaskLDtos);
		}

		PubPrcsTaskMDto pubPrcsTaskMDto = oldPubPackageDto.getPubPrcsTaskMDto();
		String pubPrcsRsvDtm = pubPackageDto.getPubPrcsTaskMDto().getPubPrcsRsvDtm();
		if (pubPrcsTaskMDto != null) {
			pubPrcsTaskMDto.setPubPrcsStusCode(PubPrcsStusCode.C);
			if (!isAuto) {
				pubPrcsTaskMDto.setPubPrcsRsvDtm(pubPrcsRsvDtm);
			}
			pubPrcsTaskMDto.setPubPrcsStartDtm(pubPrcsRsvDtm);
			pubPrcsTaskMDto.setPubPrcsEndDtm(oldPubPackageDto.getPubRqstTaskMDto().getDuedDtm());
			pubPrcsTaskMDto.setPubPrcsRslt("Success");
			pubPrcsTaskMDto.setPubPrcsMsg("Success");
			commonDao.update("Ui-publish.savePubPrcsTaskM", pubPrcsTaskMDto);
		}
	}

	public PageResult<?> searchAprvFlowTmptM(AprvFlowTmptMSearchReqDto reqDto) {
		PageResult<AprvFlowTmptMDto> result = commonDao.selectPagedList("Ui-publish.searchAprvFlowTmptM", reqDto);
		List<AprvFlowTmptMDto> aprvFlowTmptMDtos = result.getElements();

		if (aprvFlowTmptMDtos == null) {
			return result;
		}

		List<String> aprvFlowTmptCodes = aprvFlowTmptMDtos.stream().map(AprvFlowTmptMDto::getAprvFlowTmptCode).toList();

		Map<String, List<String>> params = Collections.singletonMap("aprvFlowTmptCodes", aprvFlowTmptCodes);
		List<AprvFlowTmptStepLDto> aprvFlowTmptStepLDtos = commonDao.selectList("Ui-publish.searchAprvFlowTmptStepL",
				params);
		List<AprvFlowTmptSubStepLDto> aprvFlowTmptSubStepLDtos = commonDao
				.selectList("Ui-publish.searchAprvFlowTmptSubStepL", params);

		for (AprvFlowTmptMDto dto : aprvFlowTmptMDtos) {
			String tmptCode = dto.getAprvFlowTmptCode();
			List<AprvFlowTmptStepLDto> steps = aprvFlowTmptStepLDtos.stream()
					.filter(step -> tmptCode.equals(step.getAprvFlowTmptCode())).collect(Collectors.toList());
			dto.setAprvFlowTmptStepLs(steps);

			for (AprvFlowTmptStepLDto step : steps) {
				List<AprvFlowTmptSubStepLDto> subSteps = aprvFlowTmptSubStepLDtos.stream()
						.filter(subStep -> tmptCode.equals(subStep.getAprvFlowTmptCode())
								&& subStep.getSortNo() == step.getSortNo())
						.collect(Collectors.toList());
				step.setAprvFlowTmptSubStepLs(subSteps);
			}
		}
		return result;
	}

	private void updatePublicationRequestStatus(String requestTaskCode, PubRqstStusCode pubRqstStusCode) {
		PubRqstTaskMDto requestTask = new PubRqstTaskMDto();
		requestTask.setPubRqstTaskCode(requestTaskCode);
		requestTask.setPubRqstStusCode(pubRqstStusCode);
		commonDao.update("Ui-publish.updatePubRqstStusCode", requestTask);
	}
}
