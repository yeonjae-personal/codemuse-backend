const CATEGORY_TABS = {
  PRICE_PLAN: {
    VALUE: "PP",
    TYPE: "PRICEPLAN",
  },
  ADD_ON: {
    VALUE: "AO",
    TYPE: "ADDON",
  },
  DISCOUNT: {
    VALUE: "DC",
    TYPE: "DISCOUNT",
  },
  DEVICE: {
    VALUE: "DV",
    TYPE: "DEVICE",
  },
};

const CATEGORY_VIEW_MODE = {
  TREE: "tree",
  LIST: "list",
};

const CATEGORY_FIELD = [
  {
    value: "ctgrNodeName",
    title: "Name",
    cmcdDetlNm: "Name",
    cmcdDetlId: "ctgrNodeName",
  },
];
const OFFER_FIELD = [
  {
    value: "offerNm",
    title: "Name",
    cmcdDetlNm: "Name",
    cmcdDetlId: "offerNm",
  },
  {
    value: "offerCd",
    title: "Code",
    cmcdDetlNm: "Code",
    cmcdDetlId: "offerCd",
  },
];
const CATEGORY_SWITCH_ITEM = {
  leftItemValue: CATEGORY_VIEW_MODE.TREE,
  rightItemValue: CATEGORY_VIEW_MODE.LIST,
};
const CATEGORY_EDIT_ACTIONS = {
  ADD: "add",
  REMOVE: "remove",
  CHANGE_NODE: "change",
  COLLAPSE: "collapse",
};
const OFFER_NODE_TYPE = "Offer Node";
export {
  CATEGORY_TABS,
  CATEGORY_VIEW_MODE,
  CATEGORY_FIELD,
  OFFER_FIELD,
  CATEGORY_SWITCH_ITEM,
  CATEGORY_EDIT_ACTIONS,
  OFFER_NODE_TYPE,
};
