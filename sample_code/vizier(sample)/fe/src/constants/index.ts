/* eslint-disable id-length */
import { SearchBy } from "@/enums";
import {
  CATEGORY_TABS,
  CATEGORY_VIEW_MODE,
  CATEGORY_FIELD,
  OFFER_FIELD,
  CATEGORY_SWITCH_ITEM,
} from "./category";

import {
  TAB_FIELDS,
  NM_CD_FIELDS,
  TARGET_TYPE,
  RESOURCE_TYPE_FIELD,
} from "./impactAnalysis";

const DATE_FORMAT = {
  DATE_FORMAT: "DD/MM/YYYY",
  DATE_TIME_FORMAT_WITHOUT_SECONDS: "YYYY-MM-DD HH:mm",
  TIME_FORMAT: "HH:mm:ss",
  TIME_FORMAT_WITHOUT_SECONDS: "HH:mm",
  YEAR_MONTH_DAY_FORMAT: "yyyy/MM/DD",
  DEFAULT_DATE_FORMAT_DATEPICKER: "yyyy-MM-dd",
  DEFAULT_DATE_FORMAT_DATEPICKER_TIME: "yyyy-MM-dd HH:mm",
  DEFAULT_DATE_FORMAT_DATEPICKER_MOMENT_TIME: "YYYY-MM-DD HH:mm",
  DEFAULT_DATE_FORMAT_MOMENT_OUTPUT: "yyyy-MM-DD HH:mm:ss Z",
  DATE_FORMAT_WITHOUT_TIME_REVERSE: "YYYY-MM-DD",
  DATE_TYPE: "YYYY-MM-DD HH:mm:ss",
};

const NOTI_MODULES = {
  PUBLISH_APPROVE: "PUB",
};

const BORDER_CONFIG = {
  ACTIVE: "#d9325a",
};
const WIDTH_BUTTON = {
  DEFAULT: "96px",
  ICON: "40px",
  EXCEL: "120px",
  POPUP: "170px",
  FOR_INPUT: "32px",
  DUPLICATE_CHECK: "140px",
  AUTO: "auto",
};
const HEIGHT_BUTTON = {
  DEFAULT: "40px",
  FOR_INPUT: "32px",
  DUPLICATE_CHECK: "48px",
};
const VIEW_MODE = {
  GRID: "GRID",
  LIST: "LIST",
};
const MULTI_OR_SINGLE = {
  MULTI: "M",
  SINGLE: "S",
};
const SEARCH_MODE = {
  OPTION1: "OPTION1",
  OPTION2: "OPTION2",
};

const OFFER_TYPE = {
  PRICEPLAN: "PP",
  ADDON: "AO",
  DISCOUNT: "DC",
  DEVICE: "DV",
  RENTAL: "RT",
};

const DETAIL_COMPONENT_NAME = {
  OFFER_SEARCH: "Offer search",
  OFFER_CREATE: "Offer create",
  COMPONENT_SEARCH: "Component search",
  COMPONENT_CREATE: "Component create",
  RESOURCE_SEARCH: "Resource seatch",
  RESOURCE_CREATE: "Resource create",
};

const CUSTOM_VALIDATION_PAGE = {
  OFFER: "O",
  COMPONENT: "C",
  RESOURCE: "R",
  MULTIENTITY: "ME",
};

const OFFER_TYPES_SEARCH_CONVERT = {
  PricePlan: "PP",
  "Add-on": "AO",
  Discount: "DC",
  Devices: "DV",
};

const OFFER_TYPES_CONVERT = {
  AO: "Add-On",
  DC: "Discount",
  DV: "Device",
  PP: "PricePlan",
  CH: "Characteristics",
  SR: "Service",
  BN: "Benefit",
  PR: "Price",
  SE: "Service Element",
  RE: "Rating Element",
  BE: "Billing Element",
  OG: "Offer Group",
  LB: "Line of Business Information",
  AD: "Additional",
  MS: "Message",
  VO: "Voice",
  RD: "Rating Discount",
  AW: "Allowance",
  UC: "Usage Charge",
  RC: "Recurring Charge",
  DR: "Discount Rate",
  SI: "Sales Information",
  BI: "Billing Information",
  QS: "QoS Information",
  DT: "Discount Target",
  SP: "Spam Information",
};

const ITEMS_LIST_VALUE = {
  OFFER: "Offer",
  RESOURCE: "Resource",
};

const YES_NO_VALUE = {
  YES: "Y",
  NO: "N",
};

const DEFAULT_PAGINATION = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
  optionItemPerPage: [],
};

const INITIAL_TABS = [
  {
    id: "9",
    name: "Dashboard",
    path: "/functions/product-platform",
    rawName: "Dashboard",
    tabName: "Dashboard",
    active: true,
    x: 0,
    y: 0,
    w: 1,
    h: 1,
    i: "9",
    static: false,
    loading: false,
  },
];
const ACTION_TYPE_NAME = {
  ADD: "Add",
  EXPIRED: "Expired",
  ENABLED: "Enabled",
};
const ACTION_TYPE = {
  ADD: "01",
  ENABLED: "02",
  EXPIRED: "03",
  ADD_EXPIRED: "04",
  ENABLED_EXPIRED: "05",
};
const ITEMS_PAGE_TYPE = {
  SEARCH: "Search",
  CREATE: "Create",
};
const ADVENCED_SEARCH_PARAMS_DEFAULT = {
  objCode: "",
  objName: "",
  itemCode: "",
  general: [] as any,
  additional: [] as any,
  onlyValidDtm: false,
  page: 1,
  size: 10,
};
const SPACE = " ";
const ALL = "All";
const ENV_CONFIG = {
  DEV: "development",
  PRODUCTION: "production",
  TEST: "test",
};

const DETAIL_TAB_TYPE = {
  GENERAL: "G",
  ADDITIONAL: "A",
};
const VALIDATION_TYPE = {
  ACTION: "A",
  CONDITION: "C",
};
const CANVAS_DIRECTION = {
  HORIZONTAL: "horizontal",
  VERTICAL: "vertical",
};

const TYPE_CATEGORY_OB_DRAG = {
  MATRIX: "G00055",
  GROUP: "G00056",
  OFFER: "G00057",
  COMPONENT: "G00058",
  RESOURCE: "G00059",
};
const TABS_NAME_COLLECTION = {
  GENERAL: "General",
  ADDITIONAL: "Additional",
  HISTORY: "History",
  RESOURCE: "Resource",
  MULTIENTITY: "Multi-Entity",
  MULTI_ENTITY: "Multi-Entity",
  TREEVIEW: "Tree View",
  FACTOR: "Factor",
  TABLE: "Table",
};
const ITEM_ACTIONS_LIST_TYPE = {
  OPEN_NEW_TAB: "Open New Tab",
  DETAIL: "Detail",
  DUPLICATE: "Duplicate",
  REMOVE: "Remove",
};
const ADDITIONAL_FIELDS_CUSTOM = {
  RATE_TYPE: "LB00000462",
  BASE_FEE: "LB00000068",
  DISP_BASE_FEE: "LB00000302",
  ONE_TIME_FEE: "LB00000376",
  DISP_ONE_TIME_FEE: "LB00000377",
};
const SEARCH_BY_OPTIONS = [
  {
    value: SearchBy.Name,
    title: "Name",
    cmcdDetlId: SearchBy.Name,
    cmcdDetlNm: "Name",
  },
  {
    value: SearchBy.Code,
    title: "Code",
    cmcdDetlId: SearchBy.Code,
    cmcdDetlNm: "Code",
  },
];
export {
  CATEGORY_TABS,
  CATEGORY_VIEW_MODE,
  CATEGORY_FIELD,
  OFFER_FIELD,
  CATEGORY_SWITCH_ITEM,
  TAB_FIELDS,
  NM_CD_FIELDS,
  TARGET_TYPE,
  RESOURCE_TYPE_FIELD,
  OFFER_TYPE,
  DATE_FORMAT,
  VIEW_MODE,
  MULTI_OR_SINGLE,
  SEARCH_MODE,
  ITEMS_LIST_VALUE,
  YES_NO_VALUE,
  DEFAULT_PAGINATION,
  INITIAL_TABS,
  ACTION_TYPE,
  ACTION_TYPE_NAME,
  ITEMS_PAGE_TYPE,
  OFFER_TYPES_SEARCH_CONVERT,
  OFFER_TYPES_CONVERT,
  ADVENCED_SEARCH_PARAMS_DEFAULT,
  SPACE,
  ALL,
  ENV_CONFIG,
  DETAIL_TAB_TYPE,
  CUSTOM_VALIDATION_PAGE,
  VALIDATION_TYPE,
  CANVAS_DIRECTION,
  TYPE_CATEGORY_OB_DRAG,
  DETAIL_COMPONENT_NAME,
  TABS_NAME_COLLECTION,
  ITEM_ACTIONS_LIST_TYPE,
  ADDITIONAL_FIELDS_CUSTOM,
  NOTI_MODULES,
  BORDER_CONFIG,
  SEARCH_BY_OPTIONS,
  WIDTH_BUTTON,
  HEIGHT_BUTTON,
};
