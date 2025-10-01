package com.lgcns.svcp.prod.tomcatError.controller;

import com.lgcns.svcp.prod.advice.ErrorResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class TomcatErrorController {

    @GetMapping()
    public ResponseEntity<?> handleException() {
        ErrorResponseBody body = new ErrorResponseBody();
        body.setErrorCode("403");
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

}
