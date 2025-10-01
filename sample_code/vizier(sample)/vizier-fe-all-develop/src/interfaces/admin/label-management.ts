export interface ILabelSearchParams {
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
  totalSearch?: number;
  totalSearchItems?: number;
}

export interface IBaseResponse<T> extends IPagination {
  data: T[];
}

export interface ILabelItemLang {
  labelName: string;
  langCode: string;
  regionCode: string;
  labelCode: string | null;
  labelDscr: string;
}

export interface ILabelItem {
  labelId: string;
  labelType?: string;
  items: ILabelItemLang[];
}

export interface ILabelSearchList {
  elements: ILabelItem[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ILabelDownloadParams {
  language: string;
  type: string;
  value: string;
}

export interface ILanguageList {
  langCode: string;
  regionCode: string;
  langName: string;
}
