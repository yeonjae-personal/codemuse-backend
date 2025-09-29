import { httpClient } from "@/utils/http-common";
import * as API_PATH from "@/api/prod/path";
import {
  ParamsUIOfferDetails,
  ParamsUiOfferStructure,
} from "@/interfaces/prod/OfferInterface";

const getUiOfferStructure = (params: ParamsUiOfferStructure) => {
  return httpClient.get(API_PATH.UI_OFFER_STRUCTURE, { params });
};
const getUiOfferDetails = (params: ParamsUIOfferDetails) => {
  return httpClient.get(API_PATH.UI_OFFER_SEARCH_DETAIL, { params });
};
const getUiOfferAdvanced = (params: any) => {
  return httpClient.get(API_PATH.UI_OFFER_ADVANCED, { params });
};
const getUiOfferAdvancedTable = (params: any) => {
  return httpClient.get(API_PATH.UI_OFFER_ADVANCED_DETAIL, { params });
};

export {
  getUiOfferStructure,
  getUiOfferDetails,
  getUiOfferAdvanced,
  getUiOfferAdvancedTable,
};
