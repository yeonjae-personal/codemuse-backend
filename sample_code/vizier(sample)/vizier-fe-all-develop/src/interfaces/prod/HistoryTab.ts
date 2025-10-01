export interface ParamUIHistoryTab {
  objUuid: string;
}
export interface Field {
  label: string;
  value: string;
}
export interface FieldChange {
  workNo: string | number;
  labelId: string;
  beforeValue: string;
  afterValue: string;
  commGroupCode: string | null;
  fieldTypeCode: string | null;
}
export interface StructureChange {
  workNo: string | number;
  workTypeCode: string;
  mctgrItemName: string;
  itemCodeName: string;
  objName: string;
}

export interface Change {
  chgDeptName: string;
  chgUser: string;
  toggle?: boolean;
  changeTypeName: "Structure" | "Attribute";
  fields?: FieldChange[];
  structures?: StructureChange[];
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
export interface ProductHistory {
  selectedItem?: SelectedItem;
  created: HistoryEvent;
  changed?: ChangeLog[];
  ended?: HistoryEvent | null;
}
