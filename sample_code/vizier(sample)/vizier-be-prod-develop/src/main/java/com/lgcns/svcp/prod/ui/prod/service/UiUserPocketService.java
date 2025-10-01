package com.lgcns.svcp.prod.ui.prod.service;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.userpocket.UserPocketRequest;
import com.lgcns.svcp.prod.ui.prod.dto.userpocket.UserPocketRespone;

public interface UiUserPocketService {
	List<UserPocketRespone> getUserPocket(String userId);
	void save(UserPocketRequest request, String userId);
	void delete(String uuid, String userId);
}
