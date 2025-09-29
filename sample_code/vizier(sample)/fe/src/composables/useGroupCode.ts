import { useI18n } from "vue-i18n";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { httpClient } from "@/utils/http-common";
import { formatDateWithOutSeconds } from "@/utils/format-data";
import { DM_ITEM_CODE } from "@/constants/component";

export const useGroupCode = () => {
  const { t } = useI18n();

  const ITEM_TITLE = "cmcdDetlNm";
  const ITEM_VALUE = "cmcdDetlId";

  const groupCodeData = ref<any>({});
  const error = ref(null);

  const search = async (listCodes: string[]) => {
    if (listCodes?.length) {
      const payload = [] as any[];
      listCodes.forEach((item: any) => {
        if (item && !payload.includes(item)) {
          payload.push(item);
        }
      });
      try {
        const userParam = {
          cmcdGrpIds: payload,
        };

        const cmcdInfo = await httpClient.post(
          "/api/comm/cmcd/v1/cmcdCmcdGrpIdSearch",
          userParam
        );

        if (cmcdInfo != null) {
          error.value = null;
          groupCodeData.value = cmcdInfo.data.data;
        }
      } catch (err: any) {
        error.value = err;
      }
    }
  };

  const getTextDisplay = (value: any, fieldType: any, listData: any[]) => {
    if (!value || value === "[]") return "-";
    if (fieldType === COLUMN_FIELD_TYPE.DL) {
      const valueSelected = listData?.find(
        (item: any) => item[ITEM_VALUE as string] === value
      );
      return valueSelected
        ? `${valueSelected[ITEM_TITLE as string]} (${value})`
        : value.includes("product_platform.")
          ? t(value)
          : value;
    }
    if (fieldType === COLUMN_FIELD_TYPE.DM) {
      const convertedDMCode = JSON.parse(value)
        .map((key) => DM_ITEM_CODE[key as string])
        .join(", ");
      return convertedDMCode;
    }
    if (value.includes("product_platform.")) {
      return t(value);
    }
    return fieldType == COLUMN_FIELD_TYPE.DP
      ? formatDateWithOutSeconds(value)
      : value;
  };

  return { groupCodeData, search, getTextDisplay, ITEM_TITLE, ITEM_VALUE };
};
