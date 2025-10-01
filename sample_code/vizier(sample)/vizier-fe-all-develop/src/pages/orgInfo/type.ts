import { CommonFields } from "@/types/common";

export type OrgSearchRequest = {
  orgInfo: string;
  orgKdCd: string;
  orgStatCd: string;
};

export type OrgResponse = {
  title?: string;
  orgCd: string;
  orgNm: string;
  orgKdCd: string;
  orgKdNm: string;
  orgStatCd: string;
  orgStatNm: string;
  tlmdId: string;
  tlmdNm: string;
  orgCdAllPathNm: string;
  orgNmAllPathNm: string;
  validStartDtm: string;
  validEndDtm: string;
  updDtm: string;
  hposOrgCd: string | null;
  orgLvCd: string;
};

export interface OrgInfoRequest extends CommonFields {
  orgNm: string;
  orgCd: string;
  orgKdCd: string;
  orgLvCd: string;
  tlmdId: string;
  tlmdNm: string;
  hposOrgCd: string;
  hposOrgNm: string;
  orgStatCd: string;
  orgCdPathNm: string;
  orgNmPathNm: string;
}

export type TreeNode = {
  id: string;
  title: string;
  level: string;
  parentId: string | null;
  children?: TreeNode[];
  orgCdAllPathNm: string | undefined;
  orgNmAllPathNm: string | undefined;
  expanded: boolean;
};
