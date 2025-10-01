const CSTKDCD_MAPPING = {
  null: "제한없음",
  II: "개인",
  IJ: "개인+외국인+개인사업자",
  NF: "개인+개인사업자+법인사업자",
  EG: "개인사업자+법인사업자",
  GG: "법인사업자",
  IF: "개인+외국인",
};

const PROD_AGE_DIVSCD_MAPPING = {
  null: "연령제한없음",
  DEF: "연령제한없음",
  O18: "만19세 이상",
  U12: "만12세 이하",
  U18: "만18세 이하 ~ 만4세 이상",
  O15: "만15세 이상",
  O65: "만 65세 이상",
  O80: "만80세 이상",
  O60: "만60세 이상",
};

const OPTION_ITEMS_PER_PAGE = [
  { value: 10, title: "10", cmcdDetlId: 10, cmcdDetlNm: "10" },
  { value: 20, title: "20", cmcdDetlId: 20, cmcdDetlNm: "20" },
  { value: 50, title: "50", cmcdDetlId: 50, cmcdDetlNm: "50" },
];

const DEFAULT_PAGINATION = {
  totalSearchItems: null,
  currentPage: 1,
  pageSize: 10,
  totalItems: null,
  totalPages: null,
};

const DEFAULT_PAGINATION_OFFER = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 15,
  totalItems: 0,
  totalPages: 0,
};

export {
  CSTKDCD_MAPPING,
  PROD_AGE_DIVSCD_MAPPING,
  OPTION_ITEMS_PER_PAGE,
  DEFAULT_PAGINATION,
  DEFAULT_PAGINATION_OFFER,
};
