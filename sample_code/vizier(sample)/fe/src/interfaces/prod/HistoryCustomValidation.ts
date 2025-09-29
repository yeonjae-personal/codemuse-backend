export interface ParamUIHistoryTab {
  objUuid: string;
}
export interface ParamHistoryCustomValidation {
  validCode: string;
}
export interface Field {
  label: string;
  value: string;
}
export interface ValueChange {
  workNo: string | number;
  labelId: string;
  beforeValue: string;
  afterValue: string;
  commGroupCode: string | null;
  attrUuid: string;
  attrVal: string;
  condType: string;
  validCode: string;
}
export interface StructureChange {
  workNo: string | number;
  workTypeCode: string;
  mctgrItemName: string;
  itemCodeName: string;
  objName: string;
}

export interface AttributeChange {
  attrUuid: string;
  condType: string;
  itemCode: string;
  labelId: string;
  validCode: string;
  workNo: string | number;
  workTypeCode: string;
  itemCodeName?: string;
}

export interface Change {
  chgDeptName: string;
  chgUser: string;
  toggle?: boolean;
  changeTypeName: "Value" | "Attribute";
  values?: ValueChange[];
  attributes?: AttributeChange[];
}

export interface ChangeLog {
  workDate: string;
  records: Change[];
}

export interface HistoryEvent {
  toggle?: boolean;
  workDate: string;
  chgDeptName: string;
  chgUser: string;
}
export interface SelectedItem {
  type: string;
  change?: Change;
}
export interface CustomValidationHistory {
  selectedItem?: SelectedItem;
  created: HistoryEvent;
  changed?: ChangeLog[];
  ended?: HistoryEvent | null;
}
