import { httpClient } from "@/utils/http-common";
import {
  API_TABLE_MANAGEMENT,
  API_TABLE_HEADER_PATH,
  API_TABLE_ITEMS_PATH,
} from "../path";

const getTableTypes = (params) => {
  return httpClient.get(API_TABLE_MANAGEMENT, { params });
};

const getTableTypesDetails = (tableCode, params) => {
  return httpClient.get(`${API_TABLE_MANAGEMENT}/${tableCode}`, { params });
};

const putTableTypesDetails = (tableCode, params) => {
  return httpClient.put(`${API_TABLE_MANAGEMENT}/${tableCode}`, params);
};

const getHeaderTable = (tableName, params) => {
  return httpClient.post(`${API_TABLE_HEADER_PATH}/${tableName}`, params);
};

const getItemsTable = (tableName, params) => {
  return httpClient.post(`${API_TABLE_ITEMS_PATH}/${tableName}`, params);
};
const putTableStructure = (tableName, data) => {
  return httpClient.put(`${API_TABLE_ITEMS_PATH}/${tableName}`, data);
};

export {
  getTableTypes,
  getTableTypesDetails,
  putTableTypesDetails,
  getHeaderTable,
  getItemsTable,
  putTableStructure,
};
