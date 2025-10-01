import { CommonFields } from "@/types/common";

export type userRequest = {
  userId: string;
  userNm: string;
  orgInfo: string;
};

export type userResponse = {
    userId: string;
    userNm: string;
    userKdCd: string;
    userKdCdNm: string;
    orgCd: string;
    orgNm: string;
    whofStatCd: string;
    whofStatNm: string;
    updDtm: string;
};
export interface UserInfoRequest extends CommonFields {
  userId: string,
  userNm: string,
  pw: string,
  userKdCd: string,
  orgCd: string,
  orgNm: string,
  whofStatCd: string,
};

