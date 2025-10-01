interface ParamsSearchI {
  page: number;
  size: number;
  prodNm?: string | undefined;
  prodKdCd?: string | undefined;
  prodCd?: string | undefined;
}
interface ParamsDiscountStructureDetailRoot {
  prodCd?: string;
  dcntCd?: string;
}

interface IDiscountDetail {
  generalDetails: Array<any>;
  additionalParams: Array<any>;
}

interface Item {
  label: string;
  value: string | number;
  raw: string;
  columnType: string;
}

interface IDiscountRootDetail extends IDiscountDetail {
  generalDetails: Array<Item | any>;
  additionalParams: Array<Item | any>;
  overView: string;
  comment: string;
}

interface IDiscountRootDetailPayload {
  type: string;
  dcntCd: string;
  dcntNm: string;
  prty: string;
  prodKdCd: string;
  dcntValdStrtDtm: string;
  dcntValdEndDtm: string;
  rgstUsr: string;
  rgstDtm: string;
  updUsr: string;
  updDtm: string;
  dcntGrpRepKdCd: string;
  overView: string;
  comment: string;
}

export type { 
  ParamsSearchI,
  ParamsDiscountStructureDetailRoot,
  IDiscountDetail,
  IDiscountRootDetail,
  IDiscountRootDetailPayload
};
