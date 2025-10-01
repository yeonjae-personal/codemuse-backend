import { http, HttpResponse } from "msw";
import { API_URL } from "@/constants";
import { componentDetail } from "./data/component";

export const componentHandlers = [
  http.get(`${API_URL}/prod/ui/component/detail`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(componentDetail));
      }, 1000);
    });
  }),
];
