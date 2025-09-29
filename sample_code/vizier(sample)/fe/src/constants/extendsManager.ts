const TARGET_LIST = [
  { value: "offer", title: "Offer" },
  { value: "group", title: "Group" },
];
const TARGET_TYPES = {
  OFFER: "offer",
  GROUP: "group",
};
const EXTENDS_VIEW = {
  SIMPLE: "simple",
  DETAIL: "detail",
};
const SEARCH_CATEGORY = {
  OFFER: "Offer Search",
  TARGET: "Target Search",
};
const EXTEND_CATEGORY = {
  LEADER: "Leader",
  FOLLOWER: "Follower",
};
const SELECT_LIST_SIMPLE = [
  { value: "offer", title: "Offer", cmcdDetlId: "offer", cmcdDetlNm: "Offer" },
  {
    value: "relation",
    title: "Relation",
    cmcdDetlId: "relation",
    cmcdDetlNm: "Relation",
  },
];
const SELECT_LIST_DETAIL = [
  { value: "offer", title: "Offer", cmcdDetlId: "offer", cmcdDetlNm: "Offer" },
  { value: "group", title: "Group", cmcdDetlId: "group", cmcdDetlNm: "Group" },
  {
    value: "relation",
    title: "Relation",
    cmcdDetlId: "relation",
    cmcdDetlNm: "Relation",
  },
];
const SELECT_LIST_TYPE = {
  OFFER: "offer",
  GROUP: "group",
  RELATION: "relation",
};
const OFFER_PARAMS_TYPE = {
  PRICE_PLAN: {
    TYPE: "PricePlan",
    VALUE: "PRICEPLAN",
    GENERAL: "pricePlanGeneral",
  },
  ADDON: {
    TYPE: "Add-on",
    VALUE: "ADDON",
    GENERAL: "addonGeneral",
  },
  DEVICE: {
    TYPE: "Device",
    VALUE: "DEVICE",
    GENERAL: "deviceGeneral",
  },
  DISCOUNT: {
    TYPE: "Discount",
    VALUE: "DISCOUNT",
    GENERAL: "discountGeneral",
  },
  RENTAL: {
    TYPE: "Rental",
    VALUE: "RENTAL",
    GENERAL: "rentalGeneral",
  },
};
const GROUP_DETAIL_CATEFORY = {
  DETAIL: "detail",
  DUPLICATE: "duplicate",
};
const ACTION_CATAGORY = {
  ADD: "Add",
  CHANGE: "Change",
};
const DETAIL_CATEGORY = {
  SEARCH: "Search",
  CREATE: "Create",
};
const RELATION_PAGE = {
  MANAGER: "Manager",
  SEARCH: "Search",
  CREATE: "Create",
  DUPLICATE: "Duplicate",
};
const END_DATE_DEFAULT = "9999-12-31 23:59:59";

const GRID_PARAMS_DEFAULT = {
  page: 1,
  size: 10,
  uuid: null,
  category: " ",
  value: "",
  type: "name",
};
export {
  TARGET_TYPES,
  TARGET_LIST,
  EXTENDS_VIEW,
  SEARCH_CATEGORY,
  EXTEND_CATEGORY,
  SELECT_LIST_SIMPLE,
  SELECT_LIST_DETAIL,
  SELECT_LIST_TYPE,
  END_DATE_DEFAULT,
  OFFER_PARAMS_TYPE,
  GROUP_DETAIL_CATEFORY,
  ACTION_CATAGORY,
  DETAIL_CATEGORY,
  RELATION_PAGE,
  GRID_PARAMS_DEFAULT,
};
