// http://dev.service-billing.com/prod/ui/components?itemDetlTypeCd=RD&itemCd=OPRD00001

export const data = {
  generalDetails: {
    type: "Offer > Component > Benefit > Rating Discount",
    rtngDcntCd: "OPRD00001",
    rtngDcntNm: "과금할인-M2M할인[STD   5]",
    ratDivsCd: "D",
    rtngDcntRatAplyUval: null,
    rtngDcntRat: "5.00",
    prty: "112",
    validStartDtm: "2019-02-26 00:00:00",
    validEndDtm: null,
  },
  additionalParams: {
    asgnCondDivsCd: "M2M",
    maxPermCnt: null,
  },
};

// http://dev.service-billing.com/prod/ui/resource/detail?prodUuid=038e82dd-d04f-4424-b71d-81c29816a683

export const data1 = {
  general: {
    svcFctrNm: "LTE비즈데이터",
    svcFctrClssCd: null,
    svcFctrClssDetlCd: "GNR",
    svcFctrLnwlEtcCd: "WRLS",
    svcFctrCallKdCd: "WDATA",
    svcFctrCallKdDetlCd: "WRL10",
    validEndDtm: null,
    rgstUsr: "prod",
    rgstDtm: "2024-04-09 00:31:11",
    updUsr: "prod",
    updDtm: "2024-04-09 00:31:11",
  },
  additional: {
    usePlcyYn: "Y",
    rawSvcFctrCd: "RSRE00155",
    svcFctrKdDetlCd: "WRL10",
    svcFctrKdCd: "RSRE00155",
    rtmSysAplyYn: "Y",
  },
};
