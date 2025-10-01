package com.lgcns.svcp.prod.auth.service;

import com.lgcns.svcp.prod.util.StringUtilCustom;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public boolean validateUserToken(String token){
        // TODO redis 생성 후 token 인증 연동
        return StringUtilCustom.isBlank(token);
    }

}
