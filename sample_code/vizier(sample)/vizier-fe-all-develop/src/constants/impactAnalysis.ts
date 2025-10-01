import { SearchBy } from "@/enums";

const TARGET_TYPE = {
  OFFER: "Offer",
  COMPONENT: "Component",
  RESOURCE: "Resource",
  GROUP: "Group",
  MULTI_ENTITY: "Multi Entity",
};
const TARGET_TYPE_CODE = {
  OFFER: "O",
  COMPONENT: "C",
  RESOURCE: "R",
};

const TAB_FIELDS = [
  { value: "PricePlan", title: "Price Plan", param: "PRICEPLAN" },
  { value: "Add-on", title: "Add-On", param: "ADDON" },
  { value: "Discount", title: "Discount", param: "DISCOUNT" },
  { value: "Rental", title: "Rental", param: "RENTAL" },
  // { value: "device", title: "Device" },
];

const NM_CD_FIELDS = [
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

const RESOURCE_TYPE_FIELD = [
  { value: "Rating Element", title: "Rating Element" },
  { value: "Billing Element", title: "Billing Element" },
  { value: "Service Element", title: "Service Element" },
];

const TITLE_DETAILS = {
  GENERAL: "General",
  ADDITIONAL: "Additional",
};

export {
  TARGET_TYPE,
  TAB_FIELDS,
  NM_CD_FIELDS,
  RESOURCE_TYPE_FIELD,
  TITLE_DETAILS,
  TARGET_TYPE_CODE,
};
