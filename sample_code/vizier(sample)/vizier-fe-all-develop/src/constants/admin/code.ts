 
export const USE_YN_OPTION = [
  { title: "ALL", value: " " },
  { title: "Y", value: "Y" },
  { title: "N", value: "N" },
];
 
 
export const SEARCH_TYPE_OPTION = [
  { title: "ALL", value: " " },
  { title: "Group Name", value: "grpNm" }, // 코드그룹명
  { title: "Detail Name", value: "detlNm" }, // 코드상세명
];


export const COLUMN_HEADERS = [
  {
    title: "Select",
    align: "start",
    key: "codeNo",
    class: "header",
    sortable: false,
  },
  {
    title: "Code Group ID",
    align: "start",

    key: "cmcdGrpId",
    class: "header",
     sortable: false,
  },
  {
    title: "Code Group Name",
    align: "start",
    key: "cmcdGrpNm",
    class: "header",
     sortable: false,
  },
  {
    title: "Code Group Usage",
    align: "start",

    key: "cmcdGrpUseYn",
    class: "header",
     sortable: false,
  },
  {
    title: "Code Details ID",
    align: "start",

    key: "cmcdDetlId",
    class: "header",
     sortable: false,
  },
  {
    title: "Sort Rank",
    align: "start",

    key: "cmcdSortRank",
    class: "header",
     sortable: false,
  },
  {
      title: "Code Details Usage",
      align: "start",

      key: "cmcdDetlUseYn",
      class: "header",
       sortable: false,
  },

  {
    title: "Registrant Name",
    align: "start",
    key: "rgstUsr",
    class: "header",
     sortable: false,
  },
  {
    title: "Reg. Date and Time",
    align: "start",
    key: "rgstDtm",
    class: "header",
     sortable: false,
  },
  {
    title: "Revision Date",
    align: "start",
    key: "updDtm",
    class: "header",
     sortable: false,
  },
];
 
export const CODE_TYPE = {
  CODE_GROUP: "group",
  CODE_DETAIL: "detail",
};
 