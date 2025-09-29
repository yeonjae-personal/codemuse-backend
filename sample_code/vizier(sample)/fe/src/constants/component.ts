/* eslint-disable id-length */
import { TYPE_PRODUCT } from "./offer";
import { ResourceType } from "@/enums/component";
import { MiddleItemCode } from "@/enums";

const COMPONENTS_TYPE = [
  {
    name: "product_platform.middleCategory.characteristics",
    value: MiddleItemCode.Characteristics,
  },
  {
    name: "product_platform.middleCategory.benefit",
    value: MiddleItemCode.Benefit,
  },
  {
    name: "product_platform.middleCategory.service",
    value: MiddleItemCode.Service,
  },

  {
    name: "product_platform.middleCategory.price",
    value: MiddleItemCode.Price,
  },
];

const COMPONENTS_LAGRE_TYPE = [
  {
    cmcdDetlNm: "product_platform.middleCategory.service",
    cmcdDetlId: MiddleItemCode.Service,
  },
  {
    cmcdDetlNm: "product_platform.middleCategory.benefit",
    cmcdDetlId: MiddleItemCode.Benefit,
  },
  {
    cmcdDetlNm: "product_platform.middleCategory.price",
    cmcdDetlId: MiddleItemCode.Price,
  },
  {
    cmcdDetlNm: "product_platform.middleCategory.characteristics",
    cmcdDetlId: MiddleItemCode.Characteristics,
  },
];

const COMPONENT_NAME_TYPE = "componentName";
const COMPONENT_CODE_TYPE = "componentCode";

const COMPONENT_ACTION_TYPE = {
  ADD: "Add",
  EXPIRED: "Expired",
  ENABLED: "Enabled",
};

const COMPONENT_DETAIL_CATEFORY = {
  DETAIL: "detail",
  DUPLICATE: "duplicate",
};

const OFFERS_SUB_TYPE = {
  [TYPE_PRODUCT.BENEFIT]: [
    {
      subType_cd: "AW",
      subType_nm: "Allowance",
    },
    {
      subType_cd: "RD",
      subType_nm: "Rating Discount",
    },
  ],
  [TYPE_PRODUCT.CHARACTERISTICS]: [
    {
      subType_cd: "LB",
      subType_nm: "Line of Business Information",
    },
    {
      subType_cd: "BI",
      subType_nm: "Billing Information",
    },
    {
      subType_cd: "QS",
      subType_nm: "QoS Information",
    },
    {
      subType_cd: "SI",
      subType_nm: "Sales Information",
    },
    {
      subType_cd: "SP",
      subType_nm: "SPAM Information",
    },
    {
      subType_cd: "DI",
      subType_nm: "Discount Information",
    },
    {
      subType_cd: "DT",
      subType_nm: "Discount Target",
    },
  ],
  [TYPE_PRODUCT.PRICE]: [
    {
      subType_cd: "RC",
      subType_nm: "Recurring Charge",
    },
    {
      subType_cd: "UC",
      subType_nm: "Usage Charge",
    },
    {
      subType_cd: "DR",
      subType_nm: "Discount Rate",
    },
  ],
  [TYPE_PRODUCT.SERVICE]: [
    {
      subType_cd: "VO",
      subType_nm: "Voice",
    },
    {
      subType_cd: "MS",
      subType_nm: "Message",
    },
    {
      subType_cd: "AD",
      subType_nm: "Additional",
    },
  ],
};

const RESOURCES_TYPE = [
  { name: "Rating Element", value: ResourceType.RatingElement },
  { name: "Billing Element", value: ResourceType.BillingElement },
  { name: "Service Element", value: ResourceType.ServiceElement },
];

const DM_ITEM_CODE = {
  SC: "Subcriber Count",
  PR: "Price Range",
  WRLS: "Wireless",
  HL: "Premium Plan",
  LP: "Low Rate Plan",
  PP: "PPS Plan",
  ZB: "Other Corp Plan",
  ML: "Mid-tier Plan",
  ZL: "Other Plans",
  LL: "Service Plan",
  Y: "Yes",
  N: "No",
  IN: "Individual",
  HH: "Household",
  CO: "Company",
  "N/A": "Not Applicable",
};

export {
  COMPONENTS_TYPE,
  COMPONENTS_LAGRE_TYPE,
  OFFERS_SUB_TYPE,
  COMPONENT_DETAIL_CATEFORY,
  RESOURCES_TYPE,
  COMPONENT_ACTION_TYPE,
  COMPONENT_NAME_TYPE,
  COMPONENT_CODE_TYPE,
  DM_ITEM_CODE,
};
