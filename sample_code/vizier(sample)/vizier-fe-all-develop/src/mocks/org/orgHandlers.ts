import { http, HttpResponse } from "msw";
import { API_URL } from "../../constants";
import { orgList, orgListResForSearchOrgCd } from "./data";
import { HttpStatusCode } from "axios";
// import { HttpStatusCode } from "axios";

export const orgHandlers = [
  http.get(`${API_URL}/comm/org/orgInfo/v1/mgmt/list`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(
          HttpResponse.json({
            code: "200",
            data: orgList,
          })
        );
      }, 1000);
    });
  }),
  http.get(`${API_URL}/comm/org/orgInfo/v1/list`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(
          HttpResponse.json({
            code: "200",
            data: orgList,
          })
        );
      }, 1000);
    });
  }),

  http.get(`${API_URL}/comm/org/orgInfo/v1/:orgCd`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(
          HttpResponse.json({
            code: "200",
            data: orgListResForSearchOrgCd,
          })
        );
      }, 1000);
    });
  }),

  // check duplicate
  http.post(`${API_URL}/comm/org/orgInfo/v1/check-duplicated`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(
          HttpResponse.json({
            code: "200",
            // data: true,
            data: false,
          })
        );
      }, 1000);
    });
  }),
  // create
  http.post(`${API_URL}/comm/org/orgInfo/v1`, () => {
    const mockRes = new HttpResponse(
      JSON.stringify({
        code: "200",
      }),
      {
        status: HttpStatusCode.Ok,
      }
    )
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockRes);
      }, 5000);
    });
  }),
  // update
  http.put(`${API_URL}/comm/org/orgInfo/v1`, () => {
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
