export const USE_YN_OPTION = [
  { title: "ALL", value: " " },
  { title: "Y", value: "Y" },
  { title: "N", value: "N" },
];

export const USE_YN = [
  { title: "Y", value: "Y" },
  { title: "N", value: "N" },
];

export const VOCAB_DIV_CD_TYPE = {
  VO: "VO",
  WO: "WO",
};

export const INITIAL_VOCAB = {
  vocaId: "",
  vocaNm: "",
  vocaEngAbb: "",
  vocaEngNm: "",
  vocaDscr: "",
  vocaDivsCd: "WO",
  rgstUsr: "",
  rgstDtm: "",
  stndYn: "Y",
  updUsr: "",
  updDtm: "",
  domnId: "",
 
 
};

export const TERMINOLOGY_HEADERS = [
  {
    title: "Select",
    align: "start",
    sortable: false,
    key: "no",
    class: "header",
  },
  {
    title: "Term Name",
    align: "start",
    sortable: false,
    key: "vocaNm",
    class: "header",
  },
  {
    title: "Abbreviation",
    align: "start",
    sortable: false,
    key: "vocaEngAbb",
    class: "header",
  },
  {
    title: "Eng. Term Name",
    align: "start",
    sortable: false,
    key: "vocaEngNm",
    class: "header",
  },
  {
    title: "Classification",
    align: "start",
    sortable: false,
    key: "vocaDivsCd",
    class: "header",
  },
  {
    title: "Domain Groups",
    align: "start",
    sortable: false,
    key: "domnGrpNm",
    class: "header",
  },
  {
    title: "Domain Name",
    align: "start",
    sortable: false,
    key: "domnNm",
    class: "header",
  },

  {
    title: "Eng. Domain Name",
    align: "start",
    sortable: false,
    key: "domnEngNm",
    class: "header",
  },
  {
    title: "Type",
    align: "start",
    sortable: false,
    key: "domnDivsNm",
    class: "header",
  },
  {
    title: "Data Length",
    align: "start",
    sortable: false,
    key: "domnLen",
    class: "header",
  },
  {
    title: "Standard",
    align: "start",
    sortable: false,
    key: "stndYn",
    class: "header",
  },
  {
    title: "Registrant Name",
    align: "start",
    sortable: false,
    key: "rgstUsr",
    class: "header",
  },
  {
    title: "Reg. Date and Time",
    align: "start",
    sortable: false,
    key: "rgstDtm",
    class: "header",
  },
  {
    title: "Upd. Date and Time",
    align: "start",
    sortable: false,
    key: "updDtm",
    class: "header",
  },
];

export const TERM_ANALYSIS_HEADERS = [
  {
    align: "start",
    sortable: false,
    key: "isSelect",
    class: "header",
  },
  {
    title: "Vocab Name",
    align: "start",
    key: "vocaNm",
    class: "header",
    sortable: false,
  },
  {
    title: "Abbreviation",
    align: "start",
    key: "vocaEngAbb",
    class: "header",
    sortable: false,
  },
  {
    title: "Vocab",
    align: "start",
    key: "vocaEngNm",
    class: "header",
    sortable: false,
  },
  {
    title: "Explanation",
    align: "start",
    key: "vocaDscr",
    class: "header",
    sortable: false,
  },
];
