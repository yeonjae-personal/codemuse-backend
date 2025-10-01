package com.lgcns.svcp.prod.ui.prod.service.dashboard.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.context.UserContext;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.entity.DsbdUserSetEntity;
import com.lgcns.svcp.prod.enums.SortDirection;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.CalendarViewDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.CalendarViewRequest;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.CalendarViewRespone;
import com.lgcns.svcp.prod.ui.prod.service.dashboard.UiCalendarService;
import com.lgcns.svcp.prod.util.DateUtil;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UiCalendarServiceImpl implements UiCalendarService {
	
	private final CommonDao commonDao;
	
	@Override
	public List<CalendarViewRespone> getCalendar(String dsbdViewUuid, String dateInput) {
		String userId = UserContext.getCurrentUser();
		List<CalendarViewRespone> responses = new ArrayList<>();
		Set<String> datesByMonthAndYearSet = getDatesByMonthAndYear(userId, dsbdViewUuid, dateInput);
		for (String item: datesByMonthAndYearSet) {
			CalendarViewRespone calendarViewRespone = new CalendarViewRespone();
			List<DsbdUserSetEntity> dates = getDateBySeqAndSort(userId, dsbdViewUuid, item);
			List<CalendarViewDto> calViewDtoes = new ArrayList<>();
			for (DsbdUserSetEntity date: dates) {
				Map<String, Object> paramsFindCalBySeq = new HashMap<>();
				paramsFindCalBySeq.put("dsbdViewUuid", dsbdViewUuid);
				paramsFindCalBySeq.put("userId", userId);
				paramsFindCalBySeq.put("seqNo", date.getSeqNo());
				List<DsbdUserSetEntity> calendars = commonDao.selectList("ui-dsbd-user-set-m.findByProperties", paramsFindCalBySeq);
				CalendarViewDto viewDto = new CalendarViewDto();
				for (DsbdUserSetEntity calendar: calendars) {
					switch (calendar.getAttrKey()) {
            			case "cal_title" -> viewDto.setTitle(calendar.getAttrVal());
            			case "cal_content" -> viewDto.setContent(calendar.getAttrVal());
            			case "cal_color" -> viewDto.setColor(calendar.getAttrVal());
					}
				}
				viewDto.setPostition(date.getSeqNo());
				calViewDtoes.add(viewDto);
			}
			calendarViewRespone.setDate(item);
			calendarViewRespone.setDatas(calViewDtoes);
			responses.add(calendarViewRespone);
		}
		return responses;
	}
	
	private List<DsbdUserSetEntity> getDateBySeqAndSort(String userId, String dsbdViewUuid, String date) {
		Map<String, Object> paramsFindDateBySeqAndSort = new HashMap<>();
		paramsFindDateBySeqAndSort.put("dsbdViewUuid", dsbdViewUuid);
		paramsFindDateBySeqAndSort.put("userId", userId);
		paramsFindDateBySeqAndSort.put("attrVal", date);
		List<String> sorting = new ArrayList<>();
        sorting.add("seq_no");
        sorting.add(SortDirection.asc.toString());
        paramsFindDateBySeqAndSort.put("sorting", sorting);
		return commonDao.selectList("ui-dsbd-user-set-m.findByProperties", paramsFindDateBySeqAndSort);
	}

	private Set<String> getDatesByMonthAndYear(String userId, String dsbdViewUuid, String dateInput) {
		Map<String, Object> paramsFindDateByMonthAndYear = new HashMap<>();
		paramsFindDateByMonthAndYear.put("dsbdViewUuid", dsbdViewUuid);
		paramsFindDateByMonthAndYear.put("userId", userId);
		paramsFindDateByMonthAndYear.put("attrKey", "cal_date");
		paramsFindDateByMonthAndYear.put("monthInput", DateUtil.getMonth(dateInput));
		paramsFindDateByMonthAndYear.put("yearInput", DateUtil.getYear(dateInput));
		List<String> datesByMonthAndYear = commonDao.selectList("ui-dsbd-user-set-m.findListDateByMonthAndYear", paramsFindDateByMonthAndYear);
		return datesByMonthAndYear.stream().collect(Collectors.toSet());
	}

	@Override
	@Transactional
	public void saveCalendar(CalendarViewRequest request) {
		String userId = UserContext.getCurrentUser();
		String dsbdViewUuid = request.getDsbdViewUuid();
		deleteCalendarBeforeInsert(userId, request);
		int seqNo = createSeqForCalendar(userId, request);
		List<CalendarViewDto> calendarViewDtoSorts = request.getDatas().stream()
				.sorted(Comparator.comparingInt(CalendarViewDto::getPostition))
				.collect(Collectors.toList());
		List<DsbdUserSetEntity> dsbdUserSetEntities = new ArrayList<>();
		for (CalendarViewDto item: calendarViewDtoSorts) {
			if (StringUtils.isNotBlank(item.getTitle()) || StringUtils.isNotBlank(item.getContent())) {
				initCalendarData(dsbdUserSetEntities, userId, seqNo, request.getDate(), dsbdViewUuid, "cal_date");
				if (StringUtils.isNotBlank(item.getTitle())) {
					initCalendarData(dsbdUserSetEntities, userId, seqNo, item.getTitle(), dsbdViewUuid, "cal_title");
				}
				if (StringUtils.isNotBlank(item.getContent())) {
					initCalendarData(dsbdUserSetEntities, userId, seqNo, item.getContent(), dsbdViewUuid, "cal_content");
				}
				if (StringUtils.isNotBlank(item.getColor())) {
					initCalendarData(dsbdUserSetEntities, userId, seqNo, item.getColor(), dsbdViewUuid, "cal_color");
				}
				seqNo++;
			}
		}
		commonDao.batchInsert("ui-dsbd-user-set-m.insert", dsbdUserSetEntities);
	}
	
	private int createSeqForCalendar(String userId, CalendarViewRequest request) {
		int seqNo = 0;
		Map<String, Object> paramsFindCalBySeq = new HashMap<>();
		paramsFindCalBySeq.put("dsbdViewUuid", request.getDsbdViewUuid());
		paramsFindCalBySeq.put("userId", userId);
		paramsFindCalBySeq.put("attrKey", "cal_date");
		List<Integer> seqs = commonDao.selectList("ui-dsbd-user-set-m.findSeqs", paramsFindCalBySeq);
		if (seqs != null && !seqs.isEmpty()) {
			seqNo = seqs.get(0) + 1;
		}
		return seqNo;
	}

	private void deleteCalendarBeforeInsert(String userId, CalendarViewRequest request) {
		Map<String, Object> paramsDateQuery = new HashMap<>();
		paramsDateQuery.put("dsbdViewUuid", request.getDsbdViewUuid());
		paramsDateQuery.put("userId", userId);
		paramsDateQuery.put("attrVal", request.getDate());
		List<DsbdUserSetEntity> dateList = commonDao.selectList("ui-dsbd-user-set-m.findByProperties", paramsDateQuery);
		if (dateList != null && !dateList.isEmpty()) {
			List<Map<String, Object>> listParamDelCalBySeq = new ArrayList<>();
			for (DsbdUserSetEntity item: dateList) {
				Map<String, Object> paramsDelCalBySeq = new HashMap<>();
				paramsDelCalBySeq.put("dsbdViewUuid", request.getDsbdViewUuid());
				paramsDelCalBySeq.put("userId", userId);
				paramsDelCalBySeq.put("seqNo", item.getSeqNo());
				listParamDelCalBySeq.add(paramsDelCalBySeq);
			}
			commonDao.batchDelete("ui-dsbd-user-set-m.deleteCalendarBySeq", listParamDelCalBySeq);
			commonDao.delete("ui-dsbd-user-set-m.deleteByProperties", paramsDateQuery);
		}
	}

	private void initCalendarData(List<DsbdUserSetEntity> dsbdUserSetEntities, String userId, int seqNo, String attrValue, String dsbdViewUuid, String attrKey) {
		DsbdUserSetEntity entity = new DsbdUserSetEntity();
		entity.setUserId(userId);
		entity.setDsbdViewUuid(dsbdViewUuid);
		entity.setAttrKey(attrKey);
		entity.setAttrVal(attrValue);
		entity.setSeqNo(seqNo);
		dsbdUserSetEntities.add(entity);
	}
}
