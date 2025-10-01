export const USE_YN_OPTION = [
  { title: "ALL", value: " " },
  { title: "Y", value: "Y" },
  { title: "N", value: "N" },
];

export const DOMAIN_HEADERS = [
  {
    title: "Select",
    align: "start",
    sortable: false,
    key: "no",
    class: "header",
    
  },
  {
    title: "Domain Names", // 도메인 명
    align: "start",
    sortable: false,
    key: "domnNm",
    class: "header",
  },
  {
    title: "Domain Group", // 도메인 그룹 명
    align: "start",
    sortable: false,
    key: "domnGrpNm",
    class: "header",
  },
  {
    title: "English Domain Name", // 도메인 영문 명
    align: "start",
    sortable: false,
    key: "domnEngNm",
    class: "header",
  },
  {
    title: "Domain Type", // 도메인 구분 명
    align: "start",
    sortable: false,
    key: "domnDivsNm",
    class: "header",
  },
  {
    title: "Domain Length", // 도메인 길이
    align: "start",
    sortable: false,
    key: "domnLen",
    class: "header",
  },
  {
    title: "Registrant Name", // 등록자
    align: "start",
    sortable: false,
    key: "rgstUsr",
    class: "header",
  },
  {
    title: "Reg. Date and Time", // 등록 일시
    align: "start",
    sortable: false,
    key: "rgstDtm",
    class: "header",
  },
  {
    title: "Revision Date", // 수정 일시
    align: "start",
    sortable: false,
    key: "updDtm",
    class: "header",
  }
];


export const DOMAIN_LOOKUP_HEADERS = [
  {
    title: "Select",
    align: "start",
    key: "no",
    class: "header",
    sortable: false,
    
  },
  {
    title: "Domain Names", // 도메인 명
    align: "start",
    key: "domnNm",
    class: "header",
    sortable: false,
  },
  {
    title: "Domain Group", // 도메인 그룹 명
    align: "start",
    key: "domnGrpNm",
    class: "header",
    sortable: false,
  },
  {
    title: "English Domain Name", // 도메인 영문 명
    align: "start",
    key: "domnEngNm",
    class: "header",
    sortable: false,
  },
  {
    title: "Domain Type", // 도메인 구분 명
    align: "start",
    key: "domnDivsNm",
    class: "header",
    sortable: false,
  },
  {
    title: "Domain Length", // 도메인 길이
    align: "start",
    key: "domnLen",
    class: "header",
    sortable: false,
  }
  
];


export const INITIAL_DOMAIN = {
  domnId: "",
  domnNm: "",
  domnEngNm: "",
  domnDivsCd: "",
  domnGrpCd: "",
  useYn: "",
  domnLen: "",
  domnDscr: "",
  rgstUsr: "",
  rgstDtm: "",
  updUsr: "",
  updDtm: "",
};






