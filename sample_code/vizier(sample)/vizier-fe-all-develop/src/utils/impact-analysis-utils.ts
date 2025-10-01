import { OFFER_TYPE } from "@/constants/";
import { OFFER_PARAMS_TYPE } from "@/constants/extendsManager";
import { MIDDLE_ITEM_CODE, TYPE_PRODUCT } from "@/constants/offer";

const setIconType = (code?: string) => {
  if (!code) {
    return;
  }
  return code.charAt(0);
};
const setIconColor = (code?: string) => {
  if (!code) {
    return;
  }
  switch (code) {
    case OFFER_TYPE.PRICEPLAN:
    case OFFER_PARAMS_TYPE.PRICE_PLAN.TYPE:
      return "#EB7A3D";
    case OFFER_TYPE.ADDON:
    case OFFER_PARAMS_TYPE.ADDON.TYPE:
    case "Add-on":
      return "#9947D3";
    case OFFER_TYPE.DISCOUNT:
    case OFFER_PARAMS_TYPE.DISCOUNT.TYPE:
      return "#23B27F";
    case OFFER_TYPE.RENTAL:
    case OFFER_PARAMS_TYPE.RENTAL.TYPE:
      return "#666666";
  }
};
const setHoverColor = (code?: string) => {
  if (!code) {
    return;
  }
  switch (code) {
    case OFFER_TYPE.PRICEPLAN:
    case OFFER_PARAMS_TYPE.PRICE_PLAN.TYPE:
    case TYPE_PRODUCT.CHARACTERISTICS:
    case MIDDLE_ITEM_CODE.CHARACTERISTICS:
      return "pink";
    case OFFER_TYPE.ADDON:
    case OFFER_PARAMS_TYPE.ADDON.TYPE:
    case "Add-on":
      return "purple";
    case OFFER_TYPE.DISCOUNT:
    case OFFER_PARAMS_TYPE.DISCOUNT.TYPE:
    case TYPE_PRODUCT.BENEFIT:
    case MIDDLE_ITEM_CODE.BENEFIT:
      return "green";
    case MIDDLE_ITEM_CODE.SERVICE:
    case TYPE_PRODUCT.SERVICE:
    case OFFER_TYPE.DEVICE:
      return "blue";
    case MIDDLE_ITEM_CODE.PRICE:
    case TYPE_PRODUCT.PRICE:
    case "PR":
    case "OG":
    case "CG":
      return "yellow";
    case OFFER_TYPE.RENTAL:
    case "Rental":
    case "RT":
      return "teal";
  }
};

export { setIconType, setIconColor, setHoverColor };
