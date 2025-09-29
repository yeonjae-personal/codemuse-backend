import { ResourceName } from "@/enums/component";

interface IPayloadGetListResource {
  componentUUID: string | null;
  itemCode: string;
  objName: string | null;
  objCode: string | null;
  page: number;
  size: number;
  keyword: string;
  resourceType: ResourceName | string;
  isExpired?: boolean;
}

interface IResourceGeneral {
  label: string;
  value: string;
  raw: string;
}

interface IResourceAdditional {
  attrEngName: string;
  attrLocalName: string;
  attrUUID: string;
  attrVal: string | null;
  commGroupCode: string | null;
  fieldTypeCode: string;
  itemCode: string;
  rscUUID: string;
  sortNo: string;
}

interface IResourceItem {
  objUuid: string;
  objCode: string;
  endDate: string | null;
  itemCode: string;
  itemCodeList: string | null;
  objName: string | null;
  overview: string | null;
  startDate: string | null;
  typeCode: string;
  relationStartDate: string;
  relationEndDate: string;
  isShowInfo?: boolean;
  inforItemDetail?: {
    general: any;
    additional: IResourceAdditional[];
  };
  uuid?: string;
}

interface IResourceItemAdd {
  rscUUID: string;
  name: string;
  code: string;
  rscStartDate: string;
  rscEndDate: null;
  rscOverview: null;
  itemCodeList?: null;
  isShowInfo?: boolean;
  inforItemDetail?: {
    generalDetails: IResourceGeneral[];
    additionalParams: IResourceAdditional[];
  };
}

interface IParamsGetListResourceAdd {
  componentUUID?: string;
  componentType?: string; // itemTypeNm
  componentSubType?: string; // itemDetlTypeCd
  resourceType?: string;
  rscName?: string;
  rscCode?: string;
  page: number;
  size: number;
  rscItemCode?: string;
  [x: string]: string | number | undefined;
}

interface IParamsGetResourceDetail {
  objUuid: string;
  itemCode: string;
}

export type {
  IPayloadGetListResource,
  IResourceItem,
  IResourceItemAdd,
  IParamsGetListResourceAdd,
  IParamsGetResourceDetail,
  IResourceAdditional,
};
