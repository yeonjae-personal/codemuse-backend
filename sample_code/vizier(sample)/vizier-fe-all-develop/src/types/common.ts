import { RequiredYn, SearchBy } from "@/enums";
import type { ILanguageList } from "@/interfaces/admin/label-management";
import type { MenuItem } from "@/interfaces/prod/menu";

export interface CommonFields {
  rgstUsr: string;
  rgstDtm: string;
  updUsr: string;
  updDtm: string;
}

export type Options = {
  value: string;
  label: string;
};

export interface TreeViewNodeItem {
  id: unknown;
  value: boolean;
  path: unknown[];
}

export type AddTabFunction = (item: MenuItem) => void;

export type BaseItemSearchPane = {
  itemUnique: string;
  itemName: string;
  itemDescription?: string;
  itemType?: string;
  itemDetail?: any;
  isNew?: boolean;
  validEndDtm?: string;
};

export type BaseSearchPaneParam = {
  type?: string;
  subType?: string;
  searchBy?: SearchBy;
  searchKey?: string;
  page?: number;
  size?: number;
};

export type ComponentItem = {
  middleType: string;
  offerType: string;
  code: string;
  type: string;
  itemId: string;
  name: string;
  subType: string;
};

export type ResourceItem = {
  itemCode: string;
  objCode: string;
};

export type CategoryItem = {
  itemCode: string;
  objUuid: string;
};

export type MatrixItem = {
  matrixCodeName: string;
  matrixCode: string;
};

export type FactorItem = {
  factorName: string;
  factorCode: string;
  factorTypeCode: string;
};

export type TableItem = {
  tableTypeCode: string;
  tableName: string;
};

export type MultiEntityItem = {
  itemCode: string;
  multiEntityCode: string;
  entityTypeCode: string;
};

export type MultiEntityTabItem = {
  entityScope: string;
  entityTypeCode: string;
  entityTypeName: string;
  itemCode: string;
  multiEntityExportDtos?: any;
  objRel: MultiEntityChildItem[];
  sortNo: number;
};

export type MultiEntityChildItem = {
  entityCode: string;
  entityName: string;
  entityTypeCode: string;
  itemValidEndDtm: string;
  itemValidStartDtm: string;
  objUuid: string;
  validEndDtm: string;
  validStartDtm: string;
  isAdd?: boolean;
  isUpdate?: boolean;
  expand?: boolean;
  detail?: any;
};

export type ResourceTabItem = {
  itemCode: string;
  objUuid: string;
  objCode: string;
  objName: string;
  validEndDtm: string;
  validStartDtm: string;
  relationEndDate: string;
  relationStartDate: string;
  isAdd?: boolean;
  isUpdate?: boolean;
  expand?: boolean;
  detail?: any;
  componentUUID?: string;
  resourceUUID?: string;
  workTypeCode?: string;
};

export type OfferTabItem = {
  offrType: string;
  offrUuid: string;
  offrCd: string;
  offrNm: string;
  validEndDtm: string;
  validStartDtm: string;
  itemValidEndDtm: string;
  itemValidStartDtm: string;
  isAdd?: boolean;
  isUpdate?: boolean;
  expand?: boolean;
  detail?: any;
  workTypeCode?: string;
  offerGroupUuid?: string;
};

export type OfferItem = {
  itemCode: string;
  objCode: string;
  objUuid: string;
  itemCodeName?: string;
  offerType?: string;
};

export type RelationItem = {
  objCode: string;
  objUuid: string;
};

export type TableHeader = {
  key: string;
  title: string;
  width?: string;
  sortable?: boolean;
  align?:
    | "start"
    | "end"
    | "left"
    | "right"
    | "center"
    | "justify"
    | "match-parent";
  class?: string;
  filter?: boolean;
  isActiveMenu?: boolean;
  children?: TableHeader[];
};

export type TableColumnFilter = {
  isChecked: boolean;
  value: string;
  name: string;
};

export type TableOptionColumnFilter = {
  [key: string]: TableColumnFilter;
};

export type DropDownOption = {
  cmcdDetlId: string | number;
  cmcdDetlNm: string;
};

export type AppProvider = {
  onBulkUploadFile: (
    url: string,
    file: File,
    routePath: string,
    callback: () => Promise<void>
  ) => Promise<void>;
};

export type BasePaginationType = {
  currentPage: number;
  totalPages: number;
  pageSize: number;
  totalItems?: number;
  totalSearchItems?: number;
};

export const appProvider = Symbol() as InjectionKey<AppProvider>;

export type LanguageProvider = {
  languageList: Ref<ILanguageList[]>;
};

export const languageProvider = Symbol() as InjectionKey<LanguageProvider>;

export class BaseSearchPaneParamClass implements BaseSearchPaneParam {
  type?: string;
  subType?: string;
  searchBy: SearchBy;
  searchKey?: string;
  page?: number;
  size?: number;

  constructor(
    type: string = "",
    subType: string | undefined = undefined,
    searchBy: SearchBy = SearchBy.Name,
    searchKey: string = "",
    page: number = 1,
    size: number = 10
  ) {
    this.type = type;
    this.subType = subType;
    this.searchBy = searchBy;
    this.searchKey = searchKey;
    this.page = page;
    this.size = size;
  }
}

export class BaseItemSearchPaneDto {
  itemUnique: string;
  itemName: string;
  itemDescription?: string;
  itemType?: string;
  validEndDtm?: string;
  validStartDtm?: string;
  editable?: boolean;
  showAppendIcon?: boolean;
  isNew?: boolean;
  itemLargeType?: string;
  itemDetail?: any;
  expand?: boolean;
  useYn?: string;

  constructor(
    itemUnique: string,
    itemName: string,
    itemDescription?: string,
    itemType?: string,
    validEndDtm?: string,
    validStartDtm?: string,
    editable?: boolean,
    showAppendIcon?: boolean,
    isNew?: boolean,
    itemLargeType?: string,
    itemDetail?: any,
    expand?: boolean,
    useYn?: string
  ) {
    this.itemUnique = itemUnique;
    this.itemName = itemName;
    this.itemDescription = itemDescription;
    this.itemType = itemType;
    this.itemDetail = itemDetail;
    this.itemLargeType = itemLargeType;
    this.isNew = isNew;
    this.editable = editable;
    this.showAppendIcon = showAppendIcon ?? true;
    this.expand = expand ?? false;
    this.useYn = useYn ?? RequiredYn.Yes;
    this.validEndDtm = validEndDtm;
    this.validStartDtm = validStartDtm;
  }
}
