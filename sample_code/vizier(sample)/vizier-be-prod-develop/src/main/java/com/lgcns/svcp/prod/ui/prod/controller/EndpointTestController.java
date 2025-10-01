package com.lgcns.svcp.prod.ui.prod.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/endpoint")
public class EndpointTestController {

    @PostMapping("/test")
    public ResponseEntity<Map<String, Object>> receivePost(@RequestBody Map<String, Object> body) {
        String userId = (String) body.get("userId");

        log.info("Endpoint TEST!!! POST 요청 수신: userId = {}", userId);

        // 응답 구성
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("receivedUserId", userId);
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}
