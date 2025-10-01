package com.lgcns.svcp.prod.entity;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiLog {
	
    private Long id;
    private String url;
    private String method;
    private String query;
    private String body;
    private String objUuid;
    private String objCode;
    private String userId;
    private LocalDateTime timestamp;
}