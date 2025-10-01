const DRAG_PUBLISH_COMPOSE_ITEM_TYPE = "CPI";

const PUBLISH_CODE_TYPE = [
  {
    value: "pubRqstTaskCodeName",
    title: "Name",
    cmcdDetlId: "pubRqstTaskCodeName",
    cmcdDetlNm: "Name",
  },
  {
    value: "pubRqstTaskCode",
    title: "Code",
    cmcdDetlId: "pubRqstTaskCode",
    cmcdDetlNm: "Code",
  },
  {
    value: "pubRqstTaskPubr",
    title: "Publisher",
    cmcdDetlId: "pubRqstTaskPubr",
    cmcdDetlNm: "Publisher",
  },
  {
    value: "pubRqstTaskPubrDeptCd",
    title: "Approver",
    cmcdDetlId: "pubRqstTaskPubrDeptCd",
    cmcdDetlNm: "Approver",
  },
];

const APPROVAL_CODE_TYPE = [
  {
    cmcdDetlId: "aprvFlowTmptName",
    cmcdDetlNm: "Name",
  },
  {
    cmcdDetlId: "aprvFlowTmptCode",
    cmcdDetlNm: "Code",
  },
  {
    cmcdDetlId: "aprvUser",
    cmcdDetlNm: "Approver",
  },
];

const PUBLISH_ITEM_CODE_TYPE = [
  {
    value: "chngDataCodeName",
    title: "Name",
    cmcdDetlId: "chngDataCodeName",
    cmcdDetlNm: "Name",
  },
  {
    value: "chngDataCode",
    title: "Code",
    cmcdDetlId: "chngDataCode",
    cmcdDetlNm: "Code",
  },
  {
    value: "chgUser",
    title: "User",
    cmcdDetlId: "chgUser",
    cmcdDetlNm: "User",
  },
  {
    value: "chgDeptName",
    title: "Department",
    cmcdDetlId: "chgDeptName",
    cmcdDetlNm: "Department",
  },
];

const PUBLISH_COMPOSE_ITEM_TYPE_LIST = [
  {
    cmcdDetlId: "OFFER",
    cmcdDetlNm: "Offer",
  },
  {
    cmcdDetlId: "OFFER_STRUCTURE",
    cmcdDetlNm: "Offer Structure",
  },
  {
    cmcdDetlId: "GROUP",
    cmcdDetlNm: "Group",
  },
  {
    cmcdDetlId: "GROUP_STRUCTURE",
    cmcdDetlNm: "Group Structure",
  },
  {
    cmcdDetlId: "RESOURCE",
    cmcdDetlNm: "Resource",
  },
  {
    cmcdDetlId: "COMPONENT",
    cmcdDetlNm: "Component",
  },
  {
    cmcdDetlId: "COMPONENT_STRUCTURE",
    cmcdDetlNm: "Component Structure",
  },
  {
    cmcdDetlId: "RELATION",
    cmcdDetlNm: "Relation",
  },
  {
    cmcdDetlId: "MTL_ENTITY",
    cmcdDetlNm: "Multi Entity",
  },
  {
    cmcdDetlId: "OFFER_REL",
    cmcdDetlNm: "Offer Relation",
  },
  {
    cmcdDetlId: "CATEGORY",
    cmcdDetlNm: "Category",
  },
  {
    cmcdDetlId: "TBL_STRUCT",
    cmcdDetlNm: "Table",
  },
  {
    cmcdDetlId: "FACTOR",
    cmcdDetlNm: "Factor",
  },
  {
    cmcdDetlId: "MATRIX",
    cmcdDetlNm: "Matrix",
  },
  {
    cmcdDetlId: "LABEL",
    cmcdDetlNm: "Label",
  },
];

const PUBLISH_COMPOSE_ITEM_TYPE = {
  OFFER: "OFFER",
  OFFER_STRT: "OFFER_STRUCTURE",
  GROUP: "GROUP",
  GROUP_STRT: "GROUP_STRUCTURE",
  RESOURCE: "RESOURCE",
  COMPONENT: "COMPONENT",
  COMPONENT_STRT: "COMPONENT_STRUCTURE",
  RELATION: "RELATION",
  MTL_ENTITY: "MTL_ENTITY",
  OFFER_REL: "OFFER_REL",
  CATEGORY: "CATEGORY",
  TABLE: "TBL_STRUCT",
  FACTOR: "FACTOR",
  MATRIX: "MATRIX",
  LABEL: "LABEL",
};
const PUBLISH_FLOW_STATUS = {
  DEACTIVE: "deactive",
  COMPLETE: "complete",
};
const PUBLISH_CODE_STATUS = {
  CREATED: "C",
  COMPOSED: "M",
  EXPIRED: "E",
  INPROGRESS: "I",
  DELAY: "D",
  PUBLISH_REQUEST: "R",
  PUBLISH: "P",
  VALIDATED: "V",
  PROD_TRANSFER: "O",
};
const APPROVAL_CODE_STATUS = {
  MOBILE: "MOBILE",
  HOME: "HOME",
  B2B: "B2B",
  NON_APRV: "NON_APRV",
};

const PUBLISH_TABS_VALUE = {
  GENERAL_ATTRIBUTES: "GENERAL ATTRIBUTES",
  COMPOSE_PACKAGE: "COMPOSE PACKAGE",
  APPROVAL_FLOW: "APPROVAL FLOW",
  PUBLISH: "PUBLISH",
};

const CODE_REVIEW_APPROVAL = {
  DESIGN: "D",
  PRICING_REVIEW: "P",
  IT_REVIEW: "I",
  EXCUTE_APPROVAL: "E",
  COMPLETE_APPROVAL: "C",
};

const CODE_ACTION_REJECT_APPROVE = {
  APPROVE: "APR",
  REJECT: "REJ",
  REQUEST: "REQ",
};

const PUBLISH_MODE = {
  MANUAL: "M",
  AUTO: "A",
};

const getColorStatusApproval = (code) => {
  switch (code) {
    case APPROVAL_CODE_STATUS.MOBILE:
      return "#d9325a";
    case APPROVAL_CODE_STATUS.HOME:
      return "#17b26a";
    case APPROVAL_CODE_STATUS.B2B:
      return "#914abd";
    case APPROVAL_CODE_STATUS.NON_APRV:
      return "#2e90fa";
    default:
      return "#FEE5E7";
  }
};

const getColorStatusPublish = (code) => {
  switch (code) {
    case PUBLISH_CODE_STATUS.CREATED:
      return {
        bg: "#f7f8fa",
        text: "#6b6d70",
        border: "#e6e9ed",
      };
    case PUBLISH_CODE_STATUS.INPROGRESS:
      return {
        bg: "#fef6ee",
        text: "#e04f16",
        border: "#f9dbaf",
      };
    case PUBLISH_CODE_STATUS.COMPOSED:
      return {
        bg: "#fbf2ff",
        text: "#833fb2",
        border: "#d9b0f5",
      };
    case PUBLISH_CODE_STATUS.VALIDATED:
      return {
        bg: "#e8f4fc",
        text: "#1570ef",
        border: "#b2ddff",
      };
    case PUBLISH_CODE_STATUS.DELAY:
      return {
        bg: "#fef3f2",
        text: "#c7291d",
        border: "#fecdca",
      };
    case PUBLISH_CODE_STATUS.EXPIRED:
      return {
        bg: "#262729",
        text: "#ffffff",
        border: "#3a3b3d",
      };
    case PUBLISH_CODE_STATUS.PUBLISH_REQUEST:
      return {
        bg: "#e1ebfc",
        text: "#4054b2",
        border: "#88a9e3",
      };
    case PUBLISH_CODE_STATUS.PUBLISH:
      return {
        bg: "#e6e9ed",
        text: "#6b6d70",
        border: "#e6e9ed",
      };
    case PUBLISH_CODE_STATUS.PROD_TRANSFER:
      return {
        bg: "#edfaf8",
        text: "#0f9396",
        border: "#a4ebe5",
      };
    default:
      return {
        bg: "#FEE5E7",
        text: "#000",
        border: "#FEE5E7",
      };
  }
};

const INIT_PUBLISH_PACKAGE_CREATE = {
  pubRqstTaskCode: null,
  pubRqstTaskCodeName: null,
  pubRqstTaskPubr: null,
  pubRqstTaskPubrDeptCd: null,
  pubRqstStusCode: null,
  crteDtm: null,
  vldateDtm: null,
  pubPrcsStartDtm: null,
  duedDtm: null,
  exprDtm: null,
  ovwCntn: "",
};

const PUBLISH_GENERAL_ATTRIBUTES_FORM_LIST = Object.freeze([
  {
    objUuid: null,
    colName: "pubRqstTaskCode",
    fieldTypeCode: "TF",
    editYn: "N",
    sortNo: 0,
    useYn: "Y",
    attrMaxLength: null,
    requiredYn: null,
    labelId: "LB00000177",
    attrVal: null,
    labelName: null,
    commGroupCode: "",
  },
  {
    objUuid: null,
    colName: "pubRqstTaskCodeName",
    fieldTypeCode: "TF",
    editYn: "Y",
    sortNo: 0,
    useYn: "Y",
    attrMaxLength: 500,
    requiredYn: "Y",
    labelId: "LB00000137",
    attrVal: null,
    labelName: null,
    commGroupCode: "",
  },
  {
    objUuid: null,
    colName: "pubRqstTaskPubr",
    fieldTypeCode: "TF",
    editYn: "N",
    sortNo: 0,
    useYn: "Y",
    attrMaxLength: null,
    requiredYn: "N",
    labelId: "LB00000463",
    attrVal: null,
    labelName: null,
    commGroupCode: "",
  },
  {
    objUuid: null,
    colName: "pubRqstStusCode",
    fieldTypeCode: "DL",
    editYn: "N",
    sortNo: 0,
    useYn: "Y",
    attrMaxLength: null,
    requiredYn: "N",
    labelId: "LB00000464",
    attrVal: null,
    labelName: null,
    commGroupCode: "G00061",
  },
  {
    objUuid: null,
    colName: "crteDtm",
    fieldTypeCode: "DP",
    editYn: "N",
    sortNo: 0,
    useYn: "Y",
    attrMaxLength: null,
    requiredYn: "N",
    labelId: "LB00000465",
    attrVal: null,
    labelName: null,
    commGroupCode: "",
  },
  {
    objUuid: null,
    colName: "vldateDtm",
    fieldTypeCode: "DP",
    editYn: "N",
    sortNo: 0,
    useYn: "Y",
    attrMaxLength: null,
    requiredYn: "N",
    labelId: "LB00000466",
    attrVal: null,
    labelName: null,
    commGroupCode: "",
  },
  {
    objUuid: null,
    colName: "pubPrcsStartDtm",
    fieldTypeCode: "DP",
    editYn: "N",
    sortNo: 0,
    useYn: "Y",
    attrMaxLength: null,
    requiredYn: "N",
    labelId: "LB00000467",
    attrVal: null,
    labelName: null,
    commGroupCode: "",
  },
  {
    objUuid: null,
    colName: "duedDtm",
    fieldTypeCode: "DP",
    editYn: "Y",
    sortNo: 0,
    useYn: "Y",
    attrMaxLength: null,
    requiredYn: "Y",
    labelId: "LB00000468",
    attrVal: null,
    labelName: null,
    commGroupCode: "",
  },
  {
    objUuid: null,
    colName: "exprDtm",
    fieldTypeCode: "DP",
    editYn: "Y",
    sortNo: 0,
    useYn: "Y",
    attrMaxLength: null,
    requiredYn: "N",
    labelId: "LB00000469",
    attrVal: null,
    labelName: null,
    commGroupCode: "",
  },
  {
    objUuid: null,
    colName: "ovwCntn",
    fieldTypeCode: "TA",
    editYn: "Y",
    sortNo: 0,
    useYn: "Y",
    attrMaxLength: null,
    requiredYn: "N",
    labelId: "LB00000139",
    attrVal: null,
    labelName: null,
    commGroupCode: "",
  },
]);
export {
  DRAG_PUBLISH_COMPOSE_ITEM_TYPE,
  PUBLISH_CODE_TYPE,
  PUBLISH_FLOW_STATUS,
  PUBLISH_TABS_VALUE,
  INIT_PUBLISH_PACKAGE_CREATE,
  PUBLISH_GENERAL_ATTRIBUTES_FORM_LIST,
  APPROVAL_CODE_TYPE,
  PUBLISH_ITEM_CODE_TYPE,
  PUBLISH_CODE_STATUS,
  APPROVAL_CODE_STATUS,
  CODE_REVIEW_APPROVAL,
  CODE_ACTION_REJECT_APPROVE,
  PUBLISH_COMPOSE_ITEM_TYPE,
  PUBLISH_COMPOSE_ITEM_TYPE_LIST,
  PUBLISH_MODE,
  getColorStatusPublish,
  getColorStatusApproval,
};
