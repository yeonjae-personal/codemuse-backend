package com.lgcns.svcp.prod.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {

	public static String formatFileName(String name) {
		name = name.replaceAll("\\s", "-");
		name = name.toLowerCase();
		return name;
	}

	public static boolean checkFileFormat(String patternStr, String fileName) {
		if (StringUtils.isNotBlank(fileName)) {
			Pattern pattern = Pattern.compile(patternStr);
			return PatternUtil.matches(pattern, fileName);
		}
		return false;
	}

	public static boolean checkFileSize(int size, int sizeFile) {
		if (sizeFile > 0 && sizeFile <= size) {
			return true;
		}
		return false;
	}

	public static int getFileSize(InputStream inputStream) {
		try {
			return inputStream.available();
		} catch (IOException e) {
			log.error("Error: "+e.getMessage());
			return 0;
		}
	}

	public static byte[] getByteFromBase64(String base64) {
		return Base64.decodeBase64(base64.getBytes());
	}

	public static InputStream getInputStreamFromBase64(String base64) {
		byte[] bytes = Base64.decodeBase64(base64);
		return new ByteArrayInputStream(bytes);
	}

	public static String getImageType(InputStream inputStream) {
		try {
			Tika tika = new Tika();
			String mimeType = tika.detect(inputStream);
			return mimeType.split("/")[1];
		} catch (IOException e) {
			log.error("getImageType IOException Error");
			return null;
		}
	}

	public static String getExtension(String fileName) {
		if (fileName == null) { 
			return null; 
		}
        return fileName.substring(fileName.lastIndexOf(".") + 1);
	}
}
