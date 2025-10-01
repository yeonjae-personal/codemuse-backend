package com.lgcns.svcp.prod.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class CORSFilter implements Filter{
	public static final List<String> ALLOWED_ORIGINS = Arrays.asList(
			"http://localhost:5173", //로컬,
			"http://localhost:5174", //로컬,
			"dev-service-billing-797985966.ap-northeast-2.elb.amazonaws.com", // SBUI 도메인
			"http://10.63.166.299:5173" // 제 3자 테스트를 위한 도메인
			);

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		// CORS 설정
		String origin = request.getHeader("Origin");
        if (ALLOWED_ORIGINS.contains(origin)) {
			response.setHeader("Access-Control-Allow-Origin", origin);
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE"); // 허용할 HTTP 메서드
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-User-Id, X-Language, A-TOKEN"); // 허용할 헤더
			response.setHeader("Access-Control-Max-Age", "3600"); // 프리플라이트 요청 캐시 시간(초)
			response.setHeader("Access-Control-Allow-Credentials", "true"); //UI에서 추가한 값
        }

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, res);
		}
	}
}
