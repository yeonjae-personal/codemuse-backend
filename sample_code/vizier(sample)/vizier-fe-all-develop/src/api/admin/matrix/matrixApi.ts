import { httpClient } from "@/utils/http-common";
import {
  API_MATRIX_SEARCH_PATH,
  API_MATRIX_HEADER_TABLE_PATH,
  API_MATRIX_LIST_TABLE_PATH,
  API_MATRIX_UPDATE_PATH,
  API_MATRIX_UPLOAD_PATH,
} from "../path";

const getMatrix = (params) => {
  return httpClient.get(API_MATRIX_SEARCH_PATH, { params });
};

// get list header in table  matrix detail
const getHeader = (params) => {
  return httpClient.get(API_MATRIX_HEADER_TABLE_PATH, { params });
};

// get list items in table  matrix detail
const getListTable = (matrixCode, params) => {
  return httpClient.post(`${API_MATRIX_LIST_TABLE_PATH}/${matrixCode}`, params);
};

// update matrix detail
const putMatrixDetail = (matrixCode, params) => {
  return httpClient.put(`${API_MATRIX_UPDATE_PATH}/${matrixCode}`, params);
};

// insert matrix detail
const postMatrixDetail = (params) => {
  return httpClient.post(API_MATRIX_UPDATE_PATH, params);
};

export const uploadMatrixList = (file: File, data: any) => {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("data", JSON.stringify(data));
  return httpClient.post(API_MATRIX_UPLOAD_PATH, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};

export {
  getMatrix,
  getHeader,
  getListTable,
  putMatrixDetail,
  postMatrixDetail,
};
