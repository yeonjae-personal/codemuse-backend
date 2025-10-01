import { DownloadParamsImpact } from "@/interfaces/prod/ImpactAnalysisInterface";

import axios from "axios";
import { removeUndefinedProperties } from "@/utils/format-data.ts";
import { DownloadPrams } from "@/interfaces/prod/offer.ts";
import { DownloadParamsCategoryOffers } from "@/interfaces/prod/CategoryInterface";
import moment from "moment-timezone";
import QueryString from "qs";
import type { ILabelDownloadParams } from "@/interfaces/admin/label-management";

export const useDownloadFile = () => {
  const downloading = ref(false);
  const downloadError: string | undefined | null | any = ref(null);
  const DATE_TYPE_DOWNLOAD = "YYYYMMDDHHmmss";

  const downloadFile = async (
    url: string,
    params:
      | DownloadPrams
      | DownloadParamsImpact
      | DownloadParamsCategoryOffers
      | ILabelDownloadParams
      | any,
    fileName: string,
    fileExtension: "xlsx" | "xls" = "xlsx",
    dateFormat: string = DATE_TYPE_DOWNLOAD
  ) => {
    const date = moment().format(dateFormat);
    try {
      downloading.value = true;
      const paramsDownload = removeUndefinedProperties(params);

      // const env = window.__APP_CONFIG__?.API_PROD_URL;
      const env = import.meta.env.VITE_API_PROD_URL;
      const response = await axios({
        method: "get",
        url: `${env}${url}`,
        params: paramsDownload,
        responseType: "blob",
        paramsSerializer: {
          serialize: (params) =>
            QueryString.stringify(params, {
              allowDots: true,
              arrayFormat: "indices",
            }),
        },
      });
      const link = document.createElement("a");
      link.href = URL.createObjectURL(response.data);
      link.download = `${fileName}_${date}.${fileExtension}`;
      link.click();
      setTimeout(() => {
        URL.revokeObjectURL(link.href);
      }, 0);
    } catch (error) {
      downloadError.value = error;
      throw error;
    } finally {
      downloading.value = false;
    }
  };

  const downloadFilePost = async (
    url: string,
    params:
      | DownloadPrams
      | DownloadParamsImpact
      | DownloadParamsCategoryOffers
      | ILabelDownloadParams
      | any,
    fileName: string,
    fileExtension: "xlsx" | "xls" = "xlsx",
    dateFormat: string = DATE_TYPE_DOWNLOAD
  ) => {
    const date = moment().format(dateFormat);
    try {
      downloading.value = true;
      const paramsDownload = removeUndefinedProperties(params);

      // const env = window.__APP_CONFIG__?.API_PROD_URL;
      const env = import.meta.env.VITE_API_PROD_URL;
      await axios
        .post(`${env}${url}`, paramsDownload, { responseType: "blob" })
        .then(function (response) {
          const link = document.createElement("a");
          link.href = URL.createObjectURL(response.data);
          link.download = `${fileName}_${date}.${fileExtension}`;
          link.click();
          setTimeout(() => {
            URL.revokeObjectURL(link.href);
          }, 0);
        });
    } catch (error) {
      downloadError.value = error;
      throw error;
    } finally {
      downloading.value = false;
    }
  };

  return { downloadFile, downloadFilePost, downloading, downloadError };
};
