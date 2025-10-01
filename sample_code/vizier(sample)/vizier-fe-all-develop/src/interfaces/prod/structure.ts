interface ParamsListStructure {
  page: number;
  size: number;
  offerType: string;
  itemTypeNm?: string;
  prodItemNm?: string;
  prodItemCd?: string;
  subType?: string;
}

interface ParamsStructure {
  objUuid: string;
  onlyValidDtm?: Boolean;
}

interface ParamsStructureDetail {
  itemDetlTypeCd: string;
  itemCd: string;
}

export type { ParamsListStructure, ParamsStructure, ParamsStructureDetail };
