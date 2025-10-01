import { LargeItemCode, MiddleItemCode } from "@/enums";

interface Pagination {
  totalSearchItems?: number;
  currentPage: number;
  totalPages: number;
  pageSize: number;
  totalItems?: number;
}

type ActionType = {
  name: string;
  icon?: any;
  iconProps?: any;
  itemClass?: any;
  active?: Boolean;
  onClick: () => void;
};

type Tab = {
  value: string;
  label: string;
  component: any;
  status?: string;
  disable?: boolean;
  props?: any;
  onClick?: (event?: any | Tab) => void;
  events?: EventsObject;
};

type EventsObject = {
  [eventName: string]: (...args: any[]) => void;
};

type SubType =
  | "Service"
  | "Offer"
  | "Characteristics"
  | "Benefit"
  | "Price"
  | "Resource";

interface SubTypeParamsI {
  type?: SubType;
}

interface ParamsGetListOptions {
  groupCode: string;
}

type mItemCode =
  | "BN"
  | "CH"
  | "PC"
  | "SR"
  | "AD"
  | "DE"
  | "DS"
  | "PL"
  | "RB"
  | "RA"
  | "RS";

type lItemCode = "C" | "G" | "O" | "B" | "R";

type itemCode =
  | "AW"
  | "RD"
  | "BI"
  | "DI"
  | "DT"
  | "LB"
  | "QS"
  | "SI"
  | "SP"
  | "DR"
  | "OC"
  | "RC"
  | "UC"
  | "AD"
  | "MS"
  | "VO"
  | "AO"
  | "DV"
  | "DC"
  | "PP"
  | "BE"
  | "RE"
  | "SE";

interface IParamsGetListItemCode {
  mItemCode?: MiddleItemCode;
  lItemCode?: LargeItemCode;
  itemCode?: itemCode;
}

export type {
  Pagination,
  ActionType,
  Tab,
  SubType,
  SubTypeParamsI,
  ParamsGetListOptions,
  mItemCode,
  lItemCode,
  itemCode,
  IParamsGetListItemCode,
};
