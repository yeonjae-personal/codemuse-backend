/**
 *
 * Mock for /prod/ui/extends/dependency/follower
 *
 * method: GET
 * url: http://dev.service-billing.com/prod/ui/extends/dependency/follower?targetCd=PPMS00026
 */
export const expandDependencyFollowerData = [
  {
    dpdcRelCode: "LCFRA",
    dpdcRelName: "Auto-expired on cancellation/modification",
    offrUuid: "5d70c0e4-9a3e-4aa5-9f4d-06168be691e1",
    offrCd: "DCMS00035",
    offrNm: "25% Optional Discount",
    grpUuid: null,
    grpCd: null,
    grpNm: null,
    validStartDtm: "2024-09-13 12:00:00",
    validEndDtm: "2024-09-14 12:00:00",
    childOffr: null,
  },
  {
    dpdcRelCode: "LCFRA",
    dpdcRelName: "Auto-expired on cancellation/modification",
    offrUuid: "5d70c0e4-9a3e-4aa5-9f4d-06168be691e1",
    offrCd: "DCMS00036",
    offrNm: "25% Optional Discount",
    grpUuid: null,
    grpCd: null,
    grpNm: null,
    validStartDtm: "2024-09-13 12:00:00",
    validEndDtm: "2024-09-14 12:00:00",
    childOffr: null,
  }
 
];


// data init create offer
export const initCreateOfferPricePlan = {
  "data": {
      "general": {
          "objUuid": null,
          "objCode": null,
          "objName": null,
          "itemCode": null,
          "validStartDtm": null,
          "validEndDtm": null,
          "offerTypeCode": null,
          "dplcTrgtUuid": null,
          "chgDeptName": null,
          "chgUser": null,
          "custTypeCode": null,
          "ageDivCode": null,
          "ppOvwCntn": null,
          "saleValidStartDtm": null,
          "saleValidEndDtm": null
      },
      "additional": [
          {
              "objUUID": null,
              "attrUUID": "01241617-4807-11ef-b489-0a089307ec1d",
              "itemCode": "PP",
              "fieldTypeCode": "NF",
              "commGroupCode": null,
              "attrVal": null,
              "attrMaxlength": "5",
              "requiredYn": "N",
              "labelName": "Cancel Restriction Time",
              "sortNo": 3
          },
          {
              "objUUID": null,
              "attrUUID": "038f01b1-4807-11ef-b489-0a089307ec1d",
              "itemCode": "PP",
              "fieldTypeCode": "NF",
              "commGroupCode": null,
              "attrVal": null,
              "attrMaxlength": "5",
              "requiredYn": "N",
              "labelName": "Rejoin Limit Period",
              "sortNo": 4
          },
          {
              "objUUID": null,
              "attrUUID": "0f21b0da-4807-11ef-b489-0a089307ec1d",
              "itemCode": "PP",
              "fieldTypeCode": "TF",
              "commGroupCode": null,
              "attrVal": null,
              "attrMaxlength": "500",
              "requiredYn": "N",
              "labelName": "Comments",
              "sortNo": 6
          },
          {
              "objUUID": null,
              "attrUUID": "97bde171-480b-11ef-b489-0a089307ec1d",
              "itemCode": "PP",
              "fieldTypeCode": "DL",
              "commGroupCode": "G00008",
              "attrVal": null,
              "attrMaxlength": null,
              "requiredYn": "N",
              "labelName": "Membership Discount",
              "sortNo": 5
          },
          {
              "objUUID": null,
              "attrUUID": "fa5d6198-4806-11ef-b489-0a089307ec1d",
              "itemCode": "PP",
              "fieldTypeCode": "DL",
              "commGroupCode": "G00008",
              "attrVal": null,
              "attrMaxlength": null,
              "requiredYn": "Y",
              "labelName": "SMS Notification",
              "sortNo": 1
          },
          {
              "objUUID": null,
              "attrUUID": "fb1e984c-4806-11ef-b489-0a089307ec1d",
              "itemCode": "PP",
              "fieldTypeCode": "DL",
              "commGroupCode": "G00005",
              "attrVal": null,
              "attrMaxlength": null,
              "requiredYn": "N",
              "labelName": "Product Grade",
              "sortNo": 2
          }
      ]
  },
  "code": 200,
  "message": "Success",
  "status": "200 OK",
  "timestamp": 1729758787347
};

/**
 *
 * Mock for /prod/ui/extends/dependency/relation/definition
 *
 * method: GET
 * url: http://dev.service-billing.com/prod/ui/extends/dependency/relation/definition?dpdcRelCode=LCFRA
 */
export const expandDependencyRelationDefinitionData = {
  rgstUser: "Hoon",
  rgstDtm: "2024-08-28 01:50:20",
  updUser: "Hoon",
  updDtm: "2024-09-13 14:31:58",
  dpdcRelCode: "LCFRA",
  dpdcRelName: "Auto-expired on cancellation/modification",
  eventCode: "C",
  actionCode: "R",
  prcsTypeCode: "A",
  cndtCode: null,
  dpdcRelOvwCntn:
    "Cancellation or modification of the Leader offer automatically expires the Follower offer.",
  validStartDtm: "2024-08-28 01:50:20",
  validEndDtm: "9999-12-31 23:59:59",
  rgstDeptName: "CSS Common Digital Service",
  eventName: "Cancel",
  actionName: "Remove",
  prcsTypeName: "Auto",
  cndtName: null,
};


// /prod/ui/extends/dependency/target?targetCd=PPMS00069
export const expandDependencyTargetGroupData = 
{
  "offerGroupUuid": "18f86142-c6a1-489d-9372-1c627400e330",
  "offerGroupName": "데이터 선물받기 대상요금제",
  "offerGroupType": "Group",
  "offerGroupSubType": "PricePlan Group",
  "offerGroupCode": "CHDT000006",
  "prpsCntnCntn": "Group is required",
  "Organization": "CSS Common Digital Service",
  "rqstDeptName": "Mobile Team #1",
  "rgstDeptName": "CSS Common Digital Service",
  "dplcTrgtUuid": null,
  "validStartDtm": "2023-01-01 00:00:00",
  "validEndDtm": null,
  "offerGroupOvwCntn": "All LTE plans managed in the solution must be included in the designated Group. When creating a new LTE plan, it must be included in this Group",
}