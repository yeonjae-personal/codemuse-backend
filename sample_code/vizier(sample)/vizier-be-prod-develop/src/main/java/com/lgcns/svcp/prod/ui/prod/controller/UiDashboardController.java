package com.lgcns.svcp.prod.ui.prod.controller;

import java.util.List;
import java.util.Map;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.context.UserContext;
import com.lgcns.svcp.prod.enums.SortDirection;
import com.lgcns.svcp.prod.ui.prod.dto.Sorting;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdViewDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.RecentlyWorkSearchPagingDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.SubscribeTop10SearchPagingDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.UserImageDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.CalendarViewRequest;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.DsbdViewPstRequest;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.UserImageSaveRequest;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.CalendarViewRespone;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.DsbdMonthlyOfferResponse;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.DsbdMonthlyUserGroupOfferResponse;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.ItemVolumeRespone;
import com.lgcns.svcp.prod.ui.prod.service.dashboard.UiCalendarService;
import com.lgcns.svcp.prod.ui.prod.service.dashboard.UiDashboardService;
import com.lgcns.svcp.prod.ui.prod.service.dashboard.UiRecentlyWorkService;
import com.lgcns.svcp.prod.ui.prod.service.dashboard.UiSubscriberTop10Service;
import com.lgcns.svcp.prod.ui.prod.service.dashboard.UiUserImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/ui/dashboard")
@Validated
@Tag(name = "UI-dashboard-controller", description = "dashboard controller")
@RequiredArgsConstructor
public class UiDashboardController {

	private final UiDashboardService dashboardService;
	private final UiUserImageService userImageService;
	private final UiCalendarService calendarService;
	private final UiSubscriberTop10Service subscriberService;
	private final UiRecentlyWorkService recentlyWorkService;
	
	@GetMapping
	@Operation(summary = "init data", description = "init data")
	public Map<String, Object> initData() {
		String userId = UserContext.getCurrentUser();
		return dashboardService.initData(userId);
	}

	@GetMapping(value = "/view/{dsbdviewuuid}")
	@Operation(summary = "find view detail", description = "find view detail")
	public DsbdViewDto findDsbdViewById(@PathVariable("dsbdviewuuid") String id) {
		return dashboardService.findViewByUuid(id);
	}
	
	@PostMapping
	@Operation(summary = "save dashboard", description = "save dashboard")
	public void saveDashboard(@Valid @RequestBody List<DsbdViewPstRequest> requests) {
		dashboardService.saveListView(requests);
	}
	
	@PostMapping("/userimage")
	@Operation(summary = "save user image", description = "save user image")
	public List<UserImageDto> saveUserImage(@Valid @RequestBody UserImageSaveRequest request) { 
		return userImageService.saveUserImage(request);
	}
	
	@GetMapping("/userimage")
	@Operation(summary = "get all user image", description = "get all user image")
	public List<UserImageDto> getAllUserImage(@RequestParam("dsbdViewUuid") String uuid) {
		return userImageService.findUserImageByUser(uuid);
	}
	
	@GetMapping("/offers/subscriber-top10")
	@Operation(summary = "get subscriber top 10", description = "get subscriber top 10")
	public Object getSubscriberTop10(@RequestParam String view, @RequestParam(required = false) Integer max,
												@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, 
												@RequestParam(required = false) String type,
												@RequestParam(required = false) String searchBy, @RequestParam(required = false) String searchValue) {
		SubscribeTop10SearchPagingDto searchPaging = new SubscribeTop10SearchPagingDto();
		searchPaging.setView(view);
		searchPaging.setMax(max);
		searchPaging.setOfferTypeName(type);
		searchPaging.setSearchValue(searchValue);
		searchPaging.setSearchBy(searchBy);
		return subscriberService.getSubscribeTop10(searchPaging, page, size);
	}
	
	@GetMapping("/offers/subscriber-top10/export")
	@Operation(summary = "export excel subscriber top 10", description = "export excel subscriber top 10")
	public void exportExcelSubscribe(@RequestParam(required = false) String type, @RequestParam(required = false) String searchBy, 
													@RequestParam(required = false) String searchValue, HttpServletResponse response) {
		Sorting sorting = new Sorting("sub_cnt", SortDirection.desc.toString());
		subscriberService.exportExcel(type, searchBy, searchValue, sorting, response);
	}
	
	@GetMapping("/offers/creation/monthly")
	@Operation(summary = "get monthly report about items", description = "get monthly report about items")
	public Map<String, List<DsbdMonthlyOfferResponse>> getMonthlyReportAboutItems() {
		return dashboardService.getMonthlyReportAboutItems();
	}
	
	@GetMapping("/offers/creation/user/monthly")
	@Operation(summary = "get monthly report about users", description = "get monthly report about users")
	public Map<String, List<DsbdMonthlyUserGroupOfferResponse>> getMonthlyReportAboutUsers() {
		return dashboardService.getMonthlyReportAboutUsers();
	}

	@GetMapping("/items/volume")
	@Operation(summary = "get items volume", description = "get items volume")
	public ItemVolumeRespone getItemsVolume() {
		return dashboardService.getItemsVolume();
	}
	
	@GetMapping("/offers/type")
	@Operation(summary = "get offers type", description = "get offers type")
	public List<String> getOffersType() {
		return dashboardService.getOfferType();
	}
	
	@GetMapping("/calendar")
	@Operation(summary = "get calendar", description ="get calendar")
	public List<CalendarViewRespone> getCalendar(@RequestParam String dsbdViewUuid, @RequestParam String date) {
		return calendarService.getCalendar(dsbdViewUuid, date);
	}
	
	@PostMapping("/calendar")
	@Operation(summary = "save calendar", description = "save calendar")
	public void saveCalendar(@Valid @RequestBody CalendarViewRequest request) {
		calendarService.saveCalendar(request);
	}
	
	@GetMapping("/recent-work")
	@Operation(summary = "get recently work", description = "get recently work")
	public Object getRecentWork(@RequestParam String view, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
										@RequestParam(required = false) String category, @RequestParam(required = false) String type,
										@RequestParam(required = false) String searchBy, @RequestParam(required = false) String searchValue) {
		RecentlyWorkSearchPagingDto searchPaging = new RecentlyWorkSearchPagingDto();
		searchPaging.setView(view);
		searchPaging.setType(type);
		searchPaging.setCategory(category);
		searchPaging.setSearchValue(searchValue);
		searchPaging.setSearchBy(searchBy);
		return recentlyWorkService.getRecentlyWork(searchPaging, page, size);
	}
	
	@GetMapping("/recent-work/export")
	@Operation(summary = "export recently work", description = "export recently work")
	public void exportRecentWork(@RequestParam(required = false) String category, @RequestParam(required = false) String type, @RequestParam(required = false) String searchBy, 
								@RequestParam(required = false) String searchValue, HttpServletResponse response) {
		Sorting sorting = new Sorting("obj_work_dtm", SortDirection.desc.toString());
		recentlyWorkService.exportExcel(category, type, searchBy, searchValue, sorting, response);
	}
}
