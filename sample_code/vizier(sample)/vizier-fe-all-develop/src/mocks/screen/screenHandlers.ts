import { http, HttpResponse } from "msw";
import { generateMockDataScreens, generateMockDataUrls } from './data.js'
import { getFullUrl } from "../utilsMock.js";
import { API_ADMIN_SCREEN_PATH_LIST, API_ADMIN_URL_PATH_LIST } from "@/api/admin/path/index.js";



export const screenHandlers = [
  http.get(getFullUrl(API_ADMIN_SCREEN_PATH_LIST), () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json({
          code: "200",
          data: generateMockDataScreens(50),
        }));
      }, 1000);
    });
  }),
  http.get(getFullUrl(API_ADMIN_URL_PATH_LIST), () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json({
          code: "200",
          data: generateMockDataUrls(50),
        }));
      }, 1000);
    });
  }),
];
