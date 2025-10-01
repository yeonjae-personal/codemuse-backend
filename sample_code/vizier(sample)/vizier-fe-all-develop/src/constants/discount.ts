import { TYPE_PRODUCT } from "./offer";

const DISCOUNT_OPTIONS_TYPE = [
  { id: "1", name: "Name", value: "dcntNm" },
  { id: "2", name: "Code ", value: "dcntCd" },
];

const DISCOUNT_TABS_OFFER_DETAILS = {
  GENERAL: "General",
  ADDITIONAL: "Additional",
  HISTORY: "History",
};
const DISCOUNT_TABS_DISTRIBUTES_DETAILS = {
  GENERAL: "General",
  ADDITIONAL: "Additional",
  RESOURCE: "Resource",
};
const DISCOUNT_TAB_ITEMS = [
  DISCOUNT_TABS_OFFER_DETAILS.GENERAL,
  DISCOUNT_TABS_OFFER_DETAILS.ADDITIONAL,
  DISCOUNT_TABS_OFFER_DETAILS.HISTORY,
];
const DISCOUNT_OPTIONS_SELECT_SEARCH = [
  { id: "1", name: "Name", value: "prodNm" },
  { id: "2", name: "Code ", value: "prodCd" },
  { id: "3", name: "Service ", value: "prodCd" },
  { id: "4", name: "R-Charge ", value: "prodCd" },
];
const DISCOUNT_ADD_TABS = [
  {
    value: TYPE_PRODUCT.CHARACTERISTICS,
    label: "Characteristics",
  },
  {
    value: TYPE_PRODUCT.PRICE,
    label: "Price",
  },
];
const DISCOUNT_HEADERS = [
  {
    title: "No",
    align: "start",
    sortable: false,
    key: "no",
    class: "header",
  },
  {
    title: "objCode",
    key: "objCode",
    align: "start",
    sortable: false,
  },
  {
    title: "objName",
    key: "objName",
    align: "start",
    sortable: false,
  },
  {
    title: "dcTypeCode",
    key: "dcTypeCode",
    align: "start",
    sortable: false,
  },
  {
    title: "pnltOcrcYn",
    key: "pnltOcrcYn",
    align: "start",
    sortable: false,
  },
  {
    title: "dcPriorRank",
    key: "dcPriorRank",
    align: "start",
    sortable: false,
  },
  {
    title: "validStartDtm",
    key: "validStartDtmDc",
    align: "start",
    sortable: false,
  },
  {
    title: "validEndDtm",
    key: "validEndDtmDc",
    align: "start",
    sortable: false,
  },
  {
    title: "dcRate",
    key: "dcRate",
    align: "start",
    sortable: false,
  },
  {
    title: "dailyCalcCode",
    key: "dailyCalcCodeDiscount",
    align: "start",
    sortable: false,
  },
  {
    title: "dcRgstDivCode",
    key: "dcRgstDivCode",
    align: "start",
    sortable: false,
  },
  {
    title: "rgstUser",
    key: "rgstUser",
    align: "start",
    sortable: false,
  },
  {
    title: "rgstDtm",
    key: "rgstDtm",
    align: "start",
    sortable: false,
  },
  {
    title: "updUsr",
    key: "updUser",
    align: "start",
    sortable: false,
  },
  {
    title: "updDtm",
    key: "updDtm",
    align: "start",
    sortable: false,
  },
];

export {
  DISCOUNT_OPTIONS_TYPE,
  DISCOUNT_TABS_OFFER_DETAILS,
  DISCOUNT_TABS_DISTRIBUTES_DETAILS,
  DISCOUNT_TAB_ITEMS,
  DISCOUNT_OPTIONS_SELECT_SEARCH,
  DISCOUNT_ADD_TABS,
  DISCOUNT_HEADERS,
};
