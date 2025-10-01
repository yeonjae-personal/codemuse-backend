export interface ParamsUIExtendsDependencyTarget {
  targetUuid: String;
  onlyValidDtm?: Boolean;
  includeGroup?: Boolean;
}
export interface ParamsUIExtendsDependencyRelation {
  dpdcRelCd: String | undefined;
  dpdcRelNm: String | undefined;
  page: number;
  size: number;
}
export interface ParamsUIExtendsDependencyGroup {
  offerGroupTypeCode: String | undefined;
  offrGrpNm: String | undefined;
  offrGrpCd: String | undefined;
  childOffrUuid: String | undefined;
  offerGroupTypeCode: string | undefined;
  page: number;
  size: number;
}
export interface ParamsUIExtendsDependencyAddOffer {
  baseUuid: String;
  trgtUuid: String;
  dpdcRelUuid: String;
  parentUuid?: String;
  validStartDtm: String;
  validEndDtm?: String | null;
}
export interface ParamsUIExtendsDependencyAddGroup {
  rgstUser?: String;
  rgstDtm?: String;
  updUser?: String;
  updDtm?: String;
  groupUuid: String;
  offerUuid: String;
  validStartDtm?: String;
  validEndDtm?: String;
  isNew?: boolean;
}
export interface ParamsUIExtendsDependencyTargetPost {
  addOffrDpdcLst: ParamsUIExtendsDependencyAddOffer[];
  updateOffrDpdcLst: ParamsUIExtendsDependencyAddOffer[];
}
