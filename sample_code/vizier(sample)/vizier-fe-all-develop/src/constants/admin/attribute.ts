import { LABEL_SEARCH_TYPE } from "./label";

export const DEFAULT_VALUE_ITEM_DETAIL = {
  generalInfo: {
    item: "",
    itemCode: "",
    itemName: "",
    largeItemCode: "",
    largeItemName: "",
    middleItemCode: "",
    middleItemName: "",
    used: false,
    isNew: true,
    sortNo: null,
    upperItems: [],
    lowerItems: [],
  },
  additionalInfo: [],
  treeViewInfo: {
    nodeName: "",
    nodeId: "",
    responsibleDept: "",
    responsibleUser: "",
    creationDate: "",
    overview: "",
    ctgrTabUuid: "",
  },
};

export const DEFAULT_SEARCH_PARAMS = {
  value: "",
  type: LABEL_SEARCH_TYPE.NAME,
  page: 1,
  size: 7,
  totalSearchItems: 0,
};

export const DEFAULT_PAGINATION = {
  currentPage: 1,
  pageSize: 7,
  totalItems: 0,
  totalPages: 0,
};
