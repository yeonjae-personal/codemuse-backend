package com.lgcns.svcp.prod.interceptor;


import com.lgcns.svcp.prod.context.RequestContextHolder;
import com.lgcns.svcp.prod.context.UserContext;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.entity.ApiLog;
import com.lgcns.svcp.prod.entity.ChngDataListL;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ApiLoggingInterceptor implements HandlerInterceptor {

    @Value("${external.api.comm}")
    private String commUrl;

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String url    = request.getRequestURI();
        String method = request.getMethod();
        String query  = request.getQueryString();
        String body   = "";
        String userId = UserContext.getCurrentUser();
        String orgNm  = "";

        if (request instanceof ContentCachingRequestWrapper wrapper) {
            body = new String(wrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        }

        ApiLog log = new ApiLog();
        log.setUrl(url);
        log.setMethod(method);
        log.setQuery(query);
        log.setBody(body);
        log.setTimestamp(LocalDateTime.now());
        log.setUserId(userId);

        System.out.println(" postHandle RequestContextHolder RequestContextHolder: " + RequestContextHolder.get());

        if (RequestContextHolder.get() != null
        &&  ( request.getMethod().equals("POST") || request.getMethod().equals("PUT") || request.getMethod().equals("DELETE")) )
        {
            log.setObjCode(RequestContextHolder.get().getCode());
            log.setObjUuid(RequestContextHolder.get().getUuid());
        }

        commonDao.insert("Api-log.insertApiLogTest", log);

        if (RequestContextHolder.get() != null
        &&  ( request.getMethod().equals("POST") || request.getMethod().equals("PUT") || request.getMethod().equals("DELETE")) )
        {
            ChngDataListL ChngDataList = new ChngDataListL();

            ChngDataList.setChngDataCode(RequestContextHolder.get().getCode());
            ChngDataList.setChngDataObjUuid(RequestContextHolder.get().getUuid());
            ChngDataList.setChngDataTypeCode("IMSI");
            ChngDataList.setChngDataStusCode("SAVED");
            ChngDataList.setChngDataRqstUser(userId);
            ChngDataList.setCallApiUrl(url);
            ChngDataList.setCallApiMethod(method);
            ChngDataList.setCallApiQuery(query);
            ChngDataList.setCallApiBody(body);

//            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = commUrl+"/comm/user/userInfo/v1/byUserId?userId=" + userId;
            Map<String, Object> mapObject = (Map<String, Object>) restTemplate.getForObject(resourceUrl, Object.class);

            Map<String, Object> data = null;
            if (mapObject != null && mapObject.get("data") != null) {
                data = (Map<String, Object>) mapObject.get("data");
                orgNm = (String) data.get("orgNm");
            }
            ChngDataList.setChngDataRqstDeptName(orgNm);
            commonDao.insert("Ui-publish.insertChngDataListI", ChngDataList);
        }

        // ThreadLocal 반드시 clear
        RequestContextHolder.clear();
    }
}