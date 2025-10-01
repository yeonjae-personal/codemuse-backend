export interface IRuleFieldSearchParams {
  type: string;
  value: string;
  page: number;
  size: number;
}

export interface IPagination {
  currentPage: number;
  pageSize: number;
  totalItems: number;
  totalPages: number;
  totalSearchItems: number;
  totalSearch?: number;
}

export interface IBaseResponse<T> extends IPagination {
  data: T[];
}

export interface IFieldItem {
  fieldUuid: string;
  fieldDispName: string;
  fieldKeyName: string;
  fieldDataType: string;
  useYn: string;
  isAddNew?: boolean;
}

export interface IRuleFieldSearchList {
  elements: IFieldItem[];
  totalElements: number;
  page: number;
  size: number;
  totalPages: number;
  totalSearch?: number;
}
