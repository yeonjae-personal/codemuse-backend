package com.lgcns.svcp.prod.filters;

import com.lgcns.svcp.prod.auth.service.AuthService;
import com.lgcns.svcp.prod.properties.AuthProperties;
import com.lgcns.svcp.prod.util.CookieUtil;
import com.lgcns.svcp.prod.util.StringUtilCustom;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class TokenAuthFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (StringUtilCustom.isIncludes(uri, AuthProperties.ALLOW_URIS) || StringUtilCustom.isContains(uri, AuthProperties.ALLOW_INCLUDED_URIS)|| StringUtilCustom.isStartWith(uri, AuthProperties.WEBSOCKET_URIS)) {
            filterChain.doFilter(request, response);
        }else {
            String aCookieToken = CookieUtil.getCookieValue(request, "aToken");
            String aToken = request.getHeader("A-TOKEN");
            if(!aToken.equals(aCookieToken)){
                request.getRequestDispatcher("/error").forward(request, response);
            }else if(!authService.validateUserToken(aToken)){
                filterChain.doFilter(request, response);
            }else{
                request.getRequestDispatcher("/error").forward(request, response);
            }
        }
    }
}
