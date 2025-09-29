export enum TypeComponent {
  Service = "Service",
  Benefit = "Benefit",
  Characteristics = "Characteristics",
  Price = "Price",
}

export interface RequestGetListSubType {
  itemTypeNm: TypeComponent;
}

export interface ItemSubType {
  itemDetlTypeCdNm: string;
  itemDetlTypeCd: string;
}

export interface ResponseGetListSubType {
  ItemSubType: [];
}

export interface RequestGetListComponentSearch {
  componentType?: TypeComponent | string | null; // type
  componentSubType?: string | null; // subtype
  componentName?: string;
  componentCode?: string;
  page?: number;
  size?: number;
  baseUuid?: string;
  offerUUID?: string;
}

export interface ItemComponent {
  columnMeta: any;
  targetRow: number;
  orderBy: "";
  defaultRowSize: 0;
  prodUuid: string;
  prodItemCd: string;
  prodItemNm: string;
  itemTypeNm: TypeComponent;
  itemDetlTypeCd: string;
  rgstUsr: string;
  rgstDtm: string;
  updUsr: string;
  updDtm: string;
  pageSize: number;
}

export interface ResponseGetListComponentSearch {
  targetRow: number;
  orderBy: string;
  defaultRowSize: number;
  rowSize: number;
  pageSize: number;
  rows: number;
  currentPage: number;
  customRowSize: number[];
  list: ItemComponent[];
}

export interface RequestGetListOfferSearch {
  page: number;
  size: number;
  itemCode: string;
  name?: string;
  code?: string;
}

export interface IComponentDetailQuery {
  objUuid: string;
  // itemDetlTypeCd: string;
  // itemCd: string;
}

export interface IComponentDetail {
  itemDetlTypeCd: string;
  itemCd: string;
  itemTypeNm: string;
  prodUuid: string;
  uuid: string;
  code: string;
  itemCode: string;
  name: string;
  subType: string;
  type: string;
  itemType: string;
}

export interface IComponentDetailResponse {
  columnMeta: null;
  targetRow: number;
  orderBy: string;
  defaultRowSize: number;
  prodUuid: string;
  prodItemCd: string;
  prodItemNm: string;
  itemTypeNm: string;
  itemDetlTypeCd: string;
  rgstUsr: string;
  rgstDtm: string;
  updUsr: string;
  updDtm: string;
  customRowSize: number[];
  pageSize: number;
  rowSize: number;
}

export interface IComponentResourceParams {
  // TODO HERE
}

export interface IComponentDetailInfoParams {
  objUuid: string;
}

export interface IAdditionalDetail {
  objUUID: string;
  attrUUID: string;
  attrName: string;
  itemCode: string;
  fieldTypeCode: string;
  commGroupCode: string | null;
  sortNo: string;
  attrVal: string | null;
  attrFullName: string | null;
}
