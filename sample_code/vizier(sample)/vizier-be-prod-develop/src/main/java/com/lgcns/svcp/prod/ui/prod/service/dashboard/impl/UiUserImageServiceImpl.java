package com.lgcns.svcp.prod.ui.prod.service.dashboard.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.constant.DashboardConstant;
import com.lgcns.svcp.prod.context.UserContext;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.entity.DsbdUserSetEntity;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.mapper.UserImageMapper;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.UserImageDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.UserImageRequest;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.UserImageSaveRequest;
import com.lgcns.svcp.prod.ui.prod.service.dashboard.UiUserImageService;
import com.lgcns.svcp.prod.util.FileUtil;
import com.lgcns.svcp.prod.util.S3TemplateUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UiUserImageServiceImpl implements UiUserImageService {
	
	private final CommonDao commonDao;
	private final S3TemplateUtil s3Template;
	private final UserImageMapper userImageMapper;
	private final MessageSource messageSource;
	
	@Override
	@Transactional
	public List<UserImageDto> saveUserImage(UserImageSaveRequest request) {
		String userId = UserContext.getCurrentUser();
		String uuid = request.getDsbdViewUuid();
		for (UserImageRequest item: request.getRequests()) {
			if (StringUtils.isNotBlank(item.getImageBase64()) && StringUtils.isNotBlank(item.getImageName()) 
											&& item.getImageSeq() != null) {
				InputStream inputStream = FileUtil.getInputStreamFromBase64(item.getImageBase64());
				String path = buildImagePath(item.getImageName());
				Map<String, Object> params = new HashMap<>();
				params.put("dsbdViewUuid", uuid);
				params.put("userId", userId);
				params.put("seqNo", item.getImageSeq());
				params.put("attrKey", DashboardConstant.USER_IMAGE_ATTR_KEY);
				int isImageExist = commonDao.select("ui-dsbd-user-set-m.countByProperty", params);
				if (isImageExist == 0) {
					Map<String, Object> paramsImageExistByPath = new HashMap<>();
					paramsImageExistByPath.put("dsbdViewUuid", uuid);
					paramsImageExistByPath.put("attrVal", path);
					paramsImageExistByPath.put("attrKey", DashboardConstant.USER_IMAGE_ATTR_KEY);
					int isImageExistByPath = commonDao.select("ui-dsbd-user-set-m.countByProperty", paramsImageExistByPath);
					if (isImageExistByPath > 0) {
						throw new BusinessException(messageSource.getMessage("system.error.file.exist", null, LocaleContextHolder.getLocale()));
					}
					DsbdUserSetEntity userImageEntity = initUserImage(userId, path, item);
					userImageEntity.setDsbdViewUuid(uuid);
					s3Template.createObject(path, inputStream);
					commonDao.insert("ui-dsbd-user-set-m.insert", userImageEntity);
				} else {
					DsbdUserSetEntity existsUserImageEntity = commonDao.select("ui-dsbd-user-set-m.findByProperties", params);
					s3Template.deleteObject(existsUserImageEntity.getAttrVal());
					existsUserImageEntity.setAttrVal(path);
					commonDao.update("ui-dsbd-user-set-m.updateTemporary", existsUserImageEntity);
					s3Template.createObject(path, inputStream);
				}
			} else if (StringUtils.isEmpty(item.getImageBase64()) && StringUtils.isEmpty(item.getImageName()) 
									&& item.getImageSeq() != null) {
				Map<String, Object> params = new HashMap<>();
				params.put("dsbdViewUuid", uuid);
				params.put("userId", userId);
				params.put("seqNo", item.getImageSeq());
				params.put("attrKey", DashboardConstant.USER_IMAGE_ATTR_KEY);
				int isImageExist = commonDao.select("ui-dsbd-user-set-m.countByProperty", params); 
				if (isImageExist == 0) {
					continue;
				} else {
					deleteUserImage(item.getImageSeq(), uuid);
				}
			} else if (StringUtils.isEmpty(item.getImageBase64()) && StringUtils.isNotBlank(item.getImageName()) 
									&& item.getImageSeq() != null) {
				continue;
			} else {
				throw new BusinessException("Data is incorrect format");
			}
		}
		return this.findUserImageByUser(uuid);
	}
	
	private void deleteUserImage(Integer imageSeq, String uuid) {
		String userId = UserContext.getCurrentUser();
		Map<String, Object> params = new HashMap<>();
		params.put("dsbdViewUuid", uuid);
		params.put("userId", userId);
		params.put("seqNo", imageSeq);
		params.put("attrKey", DashboardConstant.USER_IMAGE_ATTR_KEY);
		DsbdUserSetEntity userImageEntity = commonDao.select("ui-dsbd-user-set-m.findByProperties", params);
		if (userImageEntity != null) {
			s3Template.deleteObject(userImageEntity.getAttrVal());
			commonDao.delete("ui-dsbd-user-set-m.deleteByProperties", params);
		}
	}
	
	@Override
	public List<UserImageDto> findUserImageByUser(String uuid) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", UserContext.getCurrentUser());
		params.put("dsbdViewUuid", uuid);
		params.put("attrKey", DashboardConstant.USER_IMAGE_ATTR_KEY);
		List<DsbdUserSetEntity> userImages = commonDao.selectList("ui-dsbd-user-set-m.findByProperties", params);
		List<UserImageDto> results = userImages.stream().map(item -> userImageMapper.entityToDto(item)).toList();
		return results;
	}
	
	private String buildImagePath(String imageName) {
		String formatName = FileUtil.formatFileName(imageName);
		long timestamp = System.currentTimeMillis();
		return DashboardConstant.FOLDER_USER_IMAGE_UPLOAD + formatName + "_" + timestamp;
	}
	
	private DsbdUserSetEntity initUserImage(String userId, String path, UserImageRequest item) {
		DsbdUserSetEntity userImageEntity = new DsbdUserSetEntity();
		userImageEntity.setUserId(userId);
		userImageEntity.setAttrVal(path);
		userImageEntity.setAttrKey(DashboardConstant.USER_IMAGE_ATTR_KEY);
		userImageEntity.setSeqNo(item.getImageSeq());
		return userImageEntity;
	}
}
