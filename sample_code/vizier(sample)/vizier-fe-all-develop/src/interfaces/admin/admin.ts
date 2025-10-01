import {
  DisplayAttributeTab,
  RequiredFieldType,
} from "@/enums/customValidation";

export interface DomainSearchParam {
  srchWord?: string;
  useYn?: string;
}
export interface CmcdRequestParam {
  srchType: string;
  srchWord: string;
  useYn: string;
}
export interface SysMsgSearchRequest {
  sysMsgId: string;
  sysMsgCntn: string;
}

export interface PermissionSearchParam {
  authCd: string;
  authNm: string;
  authKdCd: string;
  rgstUsrId: string;
  authAprvUsrId: string;
  actvYn: string;
  authCtrlYn: string;
}

export interface PermissionGroupSearchParam {
  authGrpLcls: string;
  authGrpScls: string;
  authGrpNm: string;
  actvYn: string;
  rgstUsrId: string;
}

export interface TerminologySearchParam {
  srchWord?: string;
  vocaDivsCd?: string[];
  stndYn?: string;
}

export interface ScreenManagementSearchParam {
  scrnId?: string;
  scrnNm?: string;
  scrnPathNm?: string;
  rgstUsrId?: string;
  authAprvUsrId?: string;
  actvYn?: string;
  authCtrlYn?: string;
}

export interface UserManagementSearchParam {
  userId: string;
  userNm: string;
  orgInfo: string;
}

export interface FormError {
  id: string;
  errorMessages: string[];
}

export interface FormRef {
  value: {
    validate: () => Promise<{ valid: boolean }>;
    errors: FormError[];
  };
}

export interface PermissionSearchParam {
  authCd: string;
  authNm: string;
  authKdCd: string;
  rgstUsrId: string;
  authAprvUsrId: string;
  actvYn: string;
  authCtrlYn: string;
}

export interface PermissionSearchPopupParam {
  authCd: string;
  authNm: string;
  authKdCd: string;
}

export interface MenuSearchParam {
  menuId: string;
  scrnId: string;
  menuNm: string;
  authCtrlYn: string;
  rgstUsrId: string;
  authAprvUsrId: string;
}

export interface IAttributeItem {
  id: string;
  name: string;
  code: string;
  attrType: string;
  type: "condition" | "action";
  data: string;
  value: string;
  selected: boolean;
  disabled: boolean;
  temp: boolean;
  startDate: string;
  endDate: string;
  action?: boolean;
  condition?: boolean;
  startDateField?: string;
  endDateField?: string;
  numberFromField?: string;
  numberToField?: string;
  multiSelectField?: Array<unknown>;
  conditionItem?: string;
  conditionType?: string;
  rangeStartVal?: string;
  rangeEndVal?: string;
  labelId?: string;
  textCntn?: string;
  sort?: number;
  useYn?: string;
  largeItemCode?: string;
  itemCodeName?: string;
  rangeStartDtm?: string;
  rangeEndDtm?: string;
  rangeStartDtmStr?: string;
  rangeEndDtmStr?: string;
  dispTab: DisplayAttributeTab.General | DisplayAttributeTab.Additional;
  requiredYn: RequiredFieldType.Yes | RequiredFieldType.No;
  commGroupCode: string | null;
  attrMaxLength: string | null;
  multipleValues: string[];
}

export interface ICustomValidationItem {
  id: string;
  sort: number;
  type: "validation" | "memo";
  value: string;
  conditions: IAttributeItem[];
  actions: IAttributeItem[];
  selected: boolean;
  disabled: boolean;
  temp: boolean;
  isEdit: boolean;
  startDate: string;
  endDate: string;
  clone?: boolean;
}
