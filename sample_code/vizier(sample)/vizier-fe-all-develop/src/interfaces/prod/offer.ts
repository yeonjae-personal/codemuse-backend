import { TypeComponent } from "@/enums";

interface ParamsSearchI {
  page: number;
  size: number;
  name?: string | undefined;
  code?: string | undefined;
  itemCode: string | undefined;
}

interface ParamsProductStructureDetailRoot {
  objUuid: string;
}

interface ParamsGetTotalListStructure {
  itemTypeNm: string;
}

interface RequestUpdateProductStructureObject {
  baseUuid: string;
  trgtUuid: string;
  valdStrtDtm: string;
}

interface IOfferDetailInfoParam {
  prodCd: string;
}

interface IResourceDetailInfoParam {
  objUuid: string;
}

interface IDiscountDetailParam {
  dcntCd: string;
}

interface IQueryStructureComponentAddList {
  itemCode?: string | undefined;
  offerItemCode: string;
  componentType?: TypeComponent | null | undefined; // type
  componentSubType?: string | null | undefined; // subtype
  componentName?: string;
  componentCode?: string;
  page: number;
  size: number;
  offerUUID?: string | undefined;
}

interface DownloadPrams {
  itemCode: "PP" | "AO" | "DC" | "DV";
  name: String;
  code: String;
  lang?:"ko" | "en";
}

export type {
  ParamsSearchI,
  ParamsProductStructureDetailRoot,
  ParamsGetTotalListStructure,
  RequestUpdateProductStructureObject,
  IOfferDetailInfoParam,
  IResourceDetailInfoParam,
  IDiscountDetailParam,
  IQueryStructureComponentAddList,
  DownloadPrams,
};
