package com.lgcns.svcp.prod.ui.prod.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.context.UserContext;
import com.lgcns.svcp.prod.ui.prod.dto.userpocket.UserPocketRequest;
import com.lgcns.svcp.prod.ui.prod.dto.userpocket.UserPocketRespone;
import com.lgcns.svcp.prod.ui.prod.service.UiUserPocketService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Hidden
@CrossOrigin
@RestController
@RequestMapping("/ui/user-pocket")
@Tag(name = "UI-user-pocket-controller", description = "user pocket controller")
@RequiredArgsConstructor
@Validated
public class UiUserPocketController {

	private final UiUserPocketService userPocketService;
	
	@GetMapping
	@Operation(summary = "show list user pocket", description = "show list user pocket")
	public List<UserPocketRespone> getUserPocket() {
		String userId = UserContext.getCurrentUser();
		return userPocketService.getUserPocket(userId);
	}
	
	@PostMapping
	@Operation(summary = "drag user pocket", description = "drag user pocket")
	public void saveUserPocket(@Valid @RequestBody UserPocketRequest request) {
		String userId = UserContext.getCurrentUser();
		userPocketService.save(request, userId);
	}
	
	@DeleteMapping
	@Operation(summary = "delete user pocket", description = "delete user pocket")
	public void deleteUserPocket(@RequestParam String uuid) {
		String userId = UserContext.getCurrentUser();
		userPocketService.delete(uuid, userId);
	}
}
