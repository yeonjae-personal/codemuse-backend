import { http, HttpResponse } from "msw";
import { API_URL } from "../../constants";
import { cmcdList } from "./data";
import { HttpStatusCode } from "axios";

export const cmcdHandlers = [
  http.get(`${API_URL}/comm/cmcd/v1/list`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(
          HttpResponse.json({
            code: "200",
            data: cmcdList,
          })
        );
      }, 1000);
    });
  }),

  http.post(`${API_URL}/comm/cmcd/v1/cmcdGrpId`, () => {
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

    http.post(`${API_URL}/comm/cmcdgrp/v1`, () => {
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

  http.post(`${API_URL}/comm/cmcd/v1/cmcdDetlId`, () => {
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

  http.post(`${API_URL}/comm/cmcddetl/v1`, () => {
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
