import moment from "moment-timezone";
import { httpClient } from "./http-common";
import { useGlobal } from "@/store";

export class CommonOrdrUtil {
  public static getCurrentTime = () => {
    return moment(new Date()).tz("Asia/Seoul").format("YYYY-MM-DDTHH:mm:ss");
  };

  public static execute = async (
    mthd: string, // ex) GET
    uri: string, // ex) /api/ordr/ordrevetitem/v1/evetitemevet
    reqData: any
  ) => {
    let retVal;

    if (mthd === "GET") {
      let queryParam = "?";
      queryParam += CommonOrdrUtil.jsonToQueryString(reqData);
      retVal = await httpClient.get(uri + queryParam);
    } else if (mthd === "POST") {
      retVal = await httpClient.post(uri, reqData);
    } else if (mthd === "PUT") {
      retVal = await httpClient.put(uri, reqData);
    }

    if (retVal?.status === 200) {
      if (retVal?.data?.errorCode == "400") {
        await CommonOrdrUtil.showErrorAlert(retVal.data);
        return;
      }
      retVal = CommonOrdrUtil.changeDateTimeFormat(retVal.data);
    } else {
      await CommonOrdrUtil.showErrorAlert({
        errorMsg: "서버와 통신 중 오류가 발생했습니다.",
      });
      return;
    }
    return retVal;
  };

  private static changeDateTimeFormat = (data: any) => {
    if (data) {
      if (Array.isArray(data)) {
        data.forEach((item: any) => {
          for (const key in item) {
            const item2 = item[key as string];
            if (typeof item2 === "object") {
              for (const key2 in item2) {
                item2[key2 as string] = CommonOrdrUtil.parsingDatetime(
                  item2,
                  key2
                );
              }
            } else {
              item[key as string] = CommonOrdrUtil.parsingDatetime(item, key);
            }
          }
        });
      } else if (typeof data === "object") {
        for (const key in data) {
          data[key as string] = CommonOrdrUtil.parsingDatetime(data, key);
        }
      }
    }
    return data;
  };

  private static parsingDatetime(data: any, key: string) {
    if (
      key.endsWith("Dtm") &&
      data[key as string] &&
      data[key as string].includes("T")
    ) {
      return data[key as string].replace("T", " ");
    }
    return data[key as string];
  }

  private static jsonToQueryString(json: any): string {
    return Object.keys(json)
      .map((key) => {
        if (json[key as string] !== null) {
          return `${encodeURIComponent(key)}=${encodeURIComponent(
            json[key as string]
          )}`;
        }
        return "";
      })
      .filter((query) => query !== "")
      .join("&");
  }

  private static showErrorAlert = async (data: any) => {
    const globalStore = useGlobal();
    await globalStore.openAlertMessage({
      text: data.errorMsg,
      width: "400",
      show: true,
      title: "",
      class: "",
      isAlert: true,
    });
  };
}
