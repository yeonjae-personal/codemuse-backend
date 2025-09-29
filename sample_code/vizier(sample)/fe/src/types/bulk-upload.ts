export interface UploadFileRowData {
  index: number | string | null;
  type: string | null;
  name: string | null;
  code: string | null;
  result: string | null;
  message: string | null;
  messageCode: string | null;
  no?: number;
}

export interface UploadFileResponse {
  itemQuantity: number;
  itemQuantitySucess: number;
  itemQuantityFail: number;
  fileName: string;
  datas: UploadFileRowData[];
}
