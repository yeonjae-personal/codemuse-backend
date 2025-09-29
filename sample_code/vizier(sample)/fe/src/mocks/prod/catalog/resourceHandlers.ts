
import { http, HttpResponse } from "msw";
import { API_URL } from "@/constants";
import { listResourceByComponent } from "./data/resources";

export const resourceHandlers = [

  // http://dev.service-billing.com/prod/ui/resource/get-by-type?componentUUID=xxx...
  http.get(`${API_URL}/prod/ui/resource/get-by-type`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(listResourceByComponent));
      }, 1000);
    });
  }),
];
