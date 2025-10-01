package com.lgcns.svcp.prod.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Component
public class RequestGuardFilter extends OncePerRequestFilter {

    private static final Pattern PATH_ALLOW =
            Pattern.compile("^[A-Za-z0-9._~!$&'()*+,;=:@/\\-`\\[\\]{}|^<>\\\\]*$");

    // TODO xss 임시
    private static final Pattern FORBIDDEN =
            Pattern.compile("(?i)(<script|javascript:|data:text/html|onerror=|onload=)");

    // 과도한 URI로 인한 DoS 방지
    private static final int MAX_URI_LENGTH = 4096;
    private static final int MAX_QS_LENGTH  = 4096;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String rawUri = req.getRequestURI();
        String rawQs  = req.getQueryString();

        if ((rawUri != null && rawUri.length() > MAX_URI_LENGTH) ||
            (rawQs  != null && rawQs.length()  > MAX_QS_LENGTH)) {
            reject(res, 400, "URI too long");
            return;
        }

        String normUri = rawUri != null ? rawUri.replaceAll("/{2,}", "/")
                				.replaceAll("[\\t\\n\\r]", "") : "";

        if (!PATH_ALLOW.matcher(normUri).matches()) {
            reject(res, 400, "Disallowed characters in path");
            return;
        }

        String qsForCheck = rawQs == null ? "" : rawQs;
        String decodedQs = safeDecodeOnce(qsForCheck);

        if (FORBIDDEN.matcher(normUri).find() || FORBIDDEN.matcher(decodedQs).find()) {
            reject(res, 400, "Forbidden pattern detected");
            return;
        }

        chain.doFilter(req, res);
    }

    private static String safeDecodeOnce(String s) {
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return s;
        }
    }

    private static void reject(HttpServletResponse res, int code, String msg) throws IOException {
        res.setStatus(code);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"status\":" + code + ",\"message\":\"" + msg + "\"}");
    }
}
