import { http, HttpResponse } from "msw";
import { API_URL } from "../../constants";
import { userList } from "./data";
import { HttpStatusCode } from "axios";
// import { HttpStatusCode } from "axios";
// userInfo

export const userHandlers = [
  http.get(`${API_URL}/comm/user/userInfo/v1/mgmt/list`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(
          HttpResponse.json({
            code: "200",
            data: userList,
          })
        );
      }, 1000);
    });
  }),


  http.get(`${API_URL}/comm/user/userInfo/v1/list`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(
          HttpResponse.json({
            code: "200",
            data: userList,
          })
        );
      }, 1000);
    });
  }),

 // init selecbox for COMMU003P

  // check duplicate  comm/user/userInfo/v1/check-duplicated
  http.post(`${API_URL}/comm/user/userInfo/v1/check-duplicated`, () => {
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
  http.post(`${API_URL}/comm/user/userInfo/v1`, () => {
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
  http.put(`${API_URL}/comm/user/userInfo/v1`, () => {
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
