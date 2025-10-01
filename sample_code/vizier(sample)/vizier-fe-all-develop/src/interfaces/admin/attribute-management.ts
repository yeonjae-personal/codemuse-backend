import type { IBaseResponse, ILabelSearchParams } from "./label-management";

export interface IItemView {
  itemCode: string;
  itemName: string;
  cmcdDetlId: string;
  cmcdDetlNm: string;
  middleItemCode: string;
  middleItemName: string;
  largeItemCode: string;
  largeItemName: string;
  sortNo: number;
  middleSortNo: number;
  largeSortNo: number;
  useYn: string;
  items: IItemView[];
}

export interface IMiddleItem {
  itemCode: string;
  itemName: string;
  largeItemCode: string;
  largeItemName: string;
  middleItemCode: string;
  middleItemName: string;
  sortNo: number;
  useYn: string;
  items: IItemView[];
}

export interface IItemsViewList {
  code: string;
  name: string;
  sortNo: string;
  middleItems: IMiddleItem[];
  items: IItemView[];
}

export interface ILoupItems {
  upperItems: {
    itemCode: string;
    itemName: string;
  }[];
  lowerItems: {
    itemCode: string;
    itemName: string;
  }[];
}

export interface IItemDetail {
  general: {
    itemCode: string;
    itemName: string;
    useYn: string;
    largeItemCode: string;
    largeItemName: string;
    middleItemCode: string;
    middleItemName: string;
    upperItems: {
      itemCode: string;
      itemName: string;
    }[];
    lowerItems: {
      itemCode: string;
      itemName: string;
    }[];
  };
  additionals: {
    attrUuid: string;
    itemCode: string;
    fieldTypeCode: string;
    commGroupCode: string;
    sortNo: string;
    useYn: string;
    attrMaxLength: number | null;
    requiredYn: string;
    labelId: string;
    dispTab: string;
    dispCardYn: string | null;
    advSearchYn: string;
  }[];
  treeView: {
    rgstDtm: string;
    ctgrNodeUuid: string;
    ctgrNodeName: string;
    chgDeptName: string;
    chgUser: string;
    ctgrOvwCntn: string;
  };
}

export interface IItemViewRequest {
  general: {
    itemCode: string;
    itemName: string;
    useYn: string;
    largeItemCode: string;
    largeItemName: string;
    middleItemCode: string;
    middleItemName: string;
    sortNo: number | null;
    upperItems: {
      itemCode: string;
      itemName: string;
    }[];
    lowerItems: {
      itemCode: string;
      itemName: string;
    }[];
  };
  additionals: {
    attrUuid: string | null;
    itemCode: string;
    fieldTypeCode: string;
    commGroupCode: string | null;
    sortNo: null;
    useYn: string;
    attrMaxLength: number | null;
    requiredYn: string;
    labelId: string;
    dispTab: string;
    dispCardYn: string | null;
    advSearchYn: string;
  }[];
  treeView: {
    rgstDtm?: string;
    ctgrNodeUuid: string;
    ctgrNodeName: string;
    chgDeptName: string;
    chgUser: string;
    ctgrOvwCntn: string;
    ctgrTabUuid: string;
  } | null;
}

export interface IAttributeCodeItem {
  cmcdDetlId: string;
  cmcdDetlNm: string;
}

export interface IAttributeCode {
  cmcdGrpId: string;
  cmcdGrpNm: string;
  cmcdDetlId: string | null;
  cmcdDetlNm: string | null;
  items: IAttributeCodeItem[];
}

export interface IAttributeCodeSearchList
  extends IBaseResponse<IAttributeCode> {}

export interface IAttributeCodeParams extends ILabelSearchParams {
  fieldType?: string;
  totalSearchItems: number;
}
