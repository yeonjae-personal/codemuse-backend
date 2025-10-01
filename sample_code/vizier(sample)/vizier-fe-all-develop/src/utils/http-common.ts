import { LOGIN_PATH } from "@/api/prod/path";
import axios, { AxiosError } from "axios";
import i18n from "@/i18n";
import { removeUndefinedProperties } from "@/utils/format-data";
import QueryString from "qs";
import { useCookies } from "vue3-cookies";
const { cookies } = useCookies();


declare global {
  interface Window {
    __APP_CONFIG__: {
      API_COMM_URL: string;
      API_PROD_URL: string;
      API_VIZIER_AI_URL: string;
    };
  }
}
// const API_COMM_URL =
//   window.__APP_CONFIG__?.API_COMM_URL || "http://dev.service-billing.com/";
// const API_PROD_URL =
//   window.__APP_CONFIG__?.API_PROD_URL || "http://dev.service-billing.com/";
// const API_VIZIER_AI_URL =
//   window.__APP_CONFIG__.API_VIZIER_AI_URL ||
//   "http://vizierai.duckdns.org:8000/";

const API_PROD_URL = import.meta.env.VITE_API_PROD_URL;
const API_COMM_URL = import.meta.env.VITE_API_COMM_URL;
const API_VIZIER_AI_URL = import.meta.env.VITE_API_VIZIER_AI_URL;

export const httpClient = axios.create({
  baseURL: import.meta.env.PROD ? "" : import.meta.env.VITE_API_LOCAL_URL,
  withCredentials: true, // CORS 요청에 대한 인증 정보 전송
  responseType: "json",
});

//encode params
httpClient.defaults.paramsSerializer = (params) => {
  return QueryString.stringify(removeUndefinedProperties(params), {
    allowDots: true,
    arrayFormat: "indices",
  });
};

//interceptor http handle response
httpClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError<any>) => {
    setTimeout(() => {
      if (
        error.response &&
        error.response.status === 401 &&
        window.location.pathname !== LOGIN_PATH
      ) {
        window.location.href = LOGIN_PATH;
      }
    }, 500);
    if (error?.response?.data && error.response.status === 500) {
      error.response.data.errorMsg = i18n.global.t(
        "product_platform.messServerError"
      );
    }
    return Promise.reject(
      (error.response && error.response.data) || "Wrong service!"
    );
  }
);
//요청 전처리(interceptors) 추가 (BFF 구성 이전 임시 적용-prod는삭제)
httpClient.interceptors.request.use(
  (config) => {
    const user = JSON.parse(localStorage.getItem("userInfo") || "{}");
    const apiUrl = config.url || "";
    const prefix = apiUrl.substring(0, 4); // 요청한 API의 앞 4자리 추출하여 URL 설정
    const currentMode = import.meta.env.MODE;

    // 요청 URL 재설정(BFF 수정시 변경 필요)
    if (apiUrl.startsWith("/api")) {
      if (currentMode === "localhost") {
        config.url = API_COMM_URL + apiUrl.substring(5);
      } else {
        config.url = API_COMM_URL + apiUrl;
      }
    } else if (apiUrl.startsWith("prod")) {
      config.url = API_PROD_URL + apiUrl;
    } else if (apiUrl.startsWith("rules")) {
      config.url = API_VIZIER_AI_URL + apiUrl;
    } else {
      const serverUrl = determineDevServerUrl(prefix); // 각각 다른 서버 URL 결정
      config.url = serverUrl + apiUrl.substring(4, apiUrl.length); // 요청 URL 재설정(앞단 /api 경로삭제)
    }
    if (
      user?.userId &&
      config.url?.toLowerCase().startsWith(API_PROD_URL.toLowerCase())
    ) {
      // config.headers["X-Org-Nm"] = user?.orgNm;
      const locale = i18n.global.locale as any;
      config.headers["X-Language"] = locale?.value || "en";
    }
    if (
      user?.userId
    ) {
      // Add the X-UserId header to the request
      config.headers["X-User-ID"] = user?.userId;
    }
    const atoken = cookies.get("aToken")
    if(atoken){
      config.headers["A-TOKEN"] = cookies.get("aToken");
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 각각 다른 서버 URL 결정하는 함수
export function determineDevServerUrl(prefix: string): string {
  //alert("prefix : " + prefix);
  switch (prefix) {
    case "comm":
      return import.meta.env.VITE_API_LOCAL_COMM_URL;
    case "ordr":
      return import.meta.env.VITE_API_LOCAL_ORDR_URL;
    case "prod":
      return import.meta.env.VITE_API_LOCAL_PROD_URL;
    case "cust":
      return import.meta.env.VITE_API_LOCAL_CUST_URL;
    case "bill":
      return import.meta.env.VITE_API_LOCAL_BILL_URL;
    case "paym":
      return import.meta.env.VITE_API_LOCAL_PAYM_URL;
    // 기타 케이스에 대한 처리
    default:
      return import.meta.env.VITE_API_LOCAL_COMM_URL;
  }
}
