package com.lgcns.svcp.prod.mapper;

import java.nio.file.Paths;

import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.entity.DsbdUserSetEntity;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.UserImageDto;
import com.lgcns.svcp.prod.util.S3TemplateUtil;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserImageMapper {
	
	private final S3TemplateUtil s3Template;
	
	public UserImageDto entityToDto(DsbdUserSetEntity entity) {
		UserImageDto result = new UserImageDto();
		result.setDsbdViewUuid(entity.getDsbdViewUuid());
		result.setImageSeq(entity.getSeqNo());
		result.setImagePath(s3Template.getObjectUrl(entity.getAttrVal()));
		result.setImageName(Paths.get(entity.getAttrVal()).getFileName().toString());
		return result;
	}
}
