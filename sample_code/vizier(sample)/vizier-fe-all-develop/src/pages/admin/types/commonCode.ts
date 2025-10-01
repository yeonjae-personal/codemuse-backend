 

 /**
  * 공통코드 조회 
  * @param {string} cmcdGrpId - 공통코드 그룹 아이디 (en: Common code group ID)
  * @param {string} cmcdGrpNm - 공통코드 그룹 이름 (en: Common code group name)
  * @param {string} cmcdGrpUseYn - 공통코드 그룹 사용 여부 (en: Common code group use Y/N)
  * @param {string} cmcdDetlId - 공통코드 상세 아이디 (en: Common code detail ID)
  * @param {string} cmcdDetlNm - 공통코드 상세 이름 (en: Common code detail name)
  * @param {string} cmcdSortRank - 공통코드 정렬 순위 (en: Common code sort rank)
  * @param {string} cmcdDetlUseYn - 공통코드 상세 사용 여부 (en: Common code detail use Y/N)
  * @param {string} rgstUsr - 등록자 (en: Registered user)
  * @param {string} rgstDtm - 등록일 (en: Registration date)
  * @param {string} updDtm - 수정일 (en: Update date)
  * 
  */
export type CommonCode = {
  cmcdGrpId: string;
  cmcdGrpNm: string;
  cmcdGrpUseYn: string;
  cmcdDetlId: string;
  cmcdDetlNm: string;
  cmcdSortRank: string;
  cmcdDetlUseYn: string;
  rgstUsr: string;
  rgstDtm: string;
  updDtm?: string;
};


