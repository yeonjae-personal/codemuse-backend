package com.lgcns.svcp.prod.util;

import com.lgcns.svcp.prod.context.RequestContextHolder;

import java.util.UUID;

public class UuidUtil {
	public static String generateRandomUUID() {
        String randomUuid = UUID.randomUUID().toString();
        RequestContextHolder.setUuid(randomUuid);
        return randomUuid;
    }
}
