import { httpClient } from "@/utils/http-common";
import type { UploadFileResponse } from "@/types/bulk-upload";

export const uploadExcelFile = (url: string, file: File) => {
  const formData = new FormData();
  formData.append("file", file);
  return httpClient.post<UploadFileResponse>(url, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};
