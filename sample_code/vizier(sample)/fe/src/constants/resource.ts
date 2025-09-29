import { ResourceName } from "@/enums/component.ts";
import { BaseSearchPaneParamClass } from "@/types/common";
import { SPACE } from "./index";

const PRODUCT_ITEM_CODE = "prodItemCd";
const PRODUCT_ITEM_NAME = "prodItemNm";

const RESOURCE_ITEM_CODE = {
  RATING_ELEMENT: "RE",
  BILLING_ELEMENT: "BE",
  SERVICE_ELEMENT: "SE",
};

const RESOURCE_PARAMS_FILTER_DEFAULT = {
  componentUUID: null,
  itemCode: " ",
  resourceName: null,
  resourceCode: null,
  keyword: "",
  resourceType: ResourceName.Name,
  page: 1,
  size: 8,
};

const PARAMS_DEFAULT_COMPONENT = {
  ...new BaseSearchPaneParamClass(undefined, SPACE),
};

export {
  RESOURCE_ITEM_CODE,
  RESOURCE_PARAMS_FILTER_DEFAULT,
  PRODUCT_ITEM_CODE,
  PRODUCT_ITEM_NAME,
  PARAMS_DEFAULT_COMPONENT,
};
