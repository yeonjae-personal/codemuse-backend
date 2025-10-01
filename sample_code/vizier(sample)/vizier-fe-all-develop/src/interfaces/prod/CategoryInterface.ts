export interface ParamsUiCategoryOffer {
  page: number;
  size: number;
  itemCode: string;
  catgNm: string;
  offerCd: string;
  offerNm: string;
}
export interface ParamsUiCategoryProducts {
  catgUuid: string;
  itemCode: string;
}
export interface ParamsUiCategorySearch {
  catgUuid: String;
}
export interface ParamsUiCategoryTree {
  itemCode: string;
}

export interface ParamsUiCategoryDescription {
  itemCode: string;
}
export interface CategoryOfferTreeSearchParam {
  itemCode: string;
  offerNm: string;
  offerCd: string;
}
export interface  DownloadParamsCategoryOffers {
  itemCode: string;
  catgNm: string;
  offerCd: string;
  offerNm: string;
  language?: "ko" | "en";
}
 