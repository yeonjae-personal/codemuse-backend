import { http, HttpResponse } from "msw";
import { API_URL } from "../../constants";
import { sysmsgList } from "./data";
import { HttpStatusCode } from "axios";

export const sysmsgHandlers = [
  http.get(`${API_URL}/comm/sysmsg/v1/list`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(
          HttpResponse.json({
            code: "200",
            data: sysmsgList,
          })
        );
      }, 1000);
    });
  }),

  // check duplicate
  http.post(`${API_URL}/comm/sysmsg/v1/check-duplicated`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(
          HttpResponse.json({
            code: "200",
            data: false,
          })
        );
      }, 1000);
    });
  }),
  // create
  http.post(`${API_URL}/comm/sysmsg/v1`, () => {
    const mockRes = new HttpResponse(
      JSON.stringify({
        code: "200",
      }),
      {
        status: HttpStatusCode.Ok,
      }
    );
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockRes);
      }, 5000);
    });
  }),
  // update
  http.put(`${API_URL}/comm/sysmsg/v1`, () => {
    const mockRes = new HttpResponse(
      JSON.stringify({
        code: "200",
      }),
      {
        status: HttpStatusCode.Ok,
      }
    );
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockRes);
      }, 5000);
    });
  }),
];
