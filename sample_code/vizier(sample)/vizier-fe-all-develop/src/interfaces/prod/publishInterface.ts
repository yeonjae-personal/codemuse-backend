interface ComposeItem {
  callApiBody: String;
  callApiMethod: String;
  callApiQuery: any;
  callApiUrl: String;
  chngDataCode: string;
  chngDataCodeName: string;
  chngDataObjUuid?: string;
  chngDataItemCode?: string;
  chngDataRqstUser: String;
  chngDataSeq: Number;
  chngDataStusCode: String;
  chngDataTypeCode: string;
  rgstDtm: String;
  rgstUser: String;
  updDtm: String;
  updUser: String;
  dragType?: String;
  isAdd?: boolean;
}

export type { ComposeItem };
