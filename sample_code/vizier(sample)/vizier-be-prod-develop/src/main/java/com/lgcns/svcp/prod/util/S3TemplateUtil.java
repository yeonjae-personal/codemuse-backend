package com.lgcns.svcp.prod.util;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;

@Component
public class S3TemplateUtil {
	
	@Value("${spring.cloud.aws.region.bucket-name}")
    private String bucketName;
	
	@Value("${spring.cloud.aws.region.object-url}")
    private String objectUrl;
	
	private final S3Template s3Template;
	
	public S3TemplateUtil(S3Template s3Template) {
        this.s3Template = s3Template;
    }
	
	public void createObject(String path, InputStream inputStream) {
		s3Template.upload(bucketName, path, inputStream);
	}
	
	public void deleteObject(String path) {
		s3Template.deleteObject(bucketName, path);
	}
	
	public S3Resource getObject(String path) {
		return s3Template.download(bucketName, path);
	}
	
	public String getObjectUrl(String path) {
		return objectUrl + path;
	}
}
