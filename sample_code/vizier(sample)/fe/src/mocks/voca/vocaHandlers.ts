import { http, HttpResponse } from "msw";
import { HttpStatusCode } from 'axios';
import { API_URL } from '../../constants.js'
import { termList, mockForAnnalApi } from './data'

export const vocaHandlers = [
  http.post(`${API_URL}/comm/voca/v1/list`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json({
          code: "200",
          data: termList,
        }));
      }, 1000);
    });
  }),
  http.post(`${API_URL}/comm/voca/v1/engAbb`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json({
          code: "200",
          data: [],
        }));
      }, 1000);
    });
  }),
  http.post(`${API_URL}/comm/voca/v1`, () => {
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
  http.put(`${API_URL}/comm/voca/v1`, () => {
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
  http.get(`${API_URL}/comm/voca/v1/anal`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json({
          list: mockForAnnalApi
        }));
      }, 1000);
    });
  }),
  http.post(`${API_URL}/comm/voca/v1/vocaNm`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json({
          code: "200",
          data: [],
        }));
      }, 1000);
    });
  }),
];
