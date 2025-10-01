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
