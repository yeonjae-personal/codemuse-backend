import { http, HttpResponse } from "msw";
import { API_URL } from "../../constants";
import { domainList, mockInit } from "./data";
import { HttpStatusCode } from "axios";

export const mockDomainHandlers = [
  http.get(`${API_URL}/comm/domn/v1/list`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(
          HttpResponse.json({
            code: "200",
            data: domainList,
          })
        );
      }, 1000);
    });
  }),

  http.post(`${API_URL}/comm/domn/v1/domnName`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(
          HttpResponse.json({
            code: "200",
            data: [],
          })
        );
      }, 1000);
    });
  }),
  http.get(`${API_URL}/comm/domn/v1/detl`, () => {
    const mockRes = HttpResponse.json(mockInit, { status: HttpStatusCode.Ok });

    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockRes);
      }, 500);
    });
  }),

  http.post(`${API_URL}/comm/domn/v1`, () => {
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
  http.put(`${API_URL}/comm/domn/v1`, () => {
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
