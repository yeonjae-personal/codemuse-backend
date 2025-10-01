package com.lgcns.svcp.prod.ui.prod.service.dashboard;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.dashboard.UserImageDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.UserImageSaveRequest;

public interface UiUserImageService {
	List<UserImageDto> saveUserImage(UserImageSaveRequest request);
	List<UserImageDto> findUserImageByUser(String uuid);
}
