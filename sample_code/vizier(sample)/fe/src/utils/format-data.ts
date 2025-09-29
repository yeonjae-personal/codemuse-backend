import moment from "moment-timezone";
import { DATE_FORMAT, OFFER_TYPES_CONVERT } from "@/constants/";

const convertArrayToObject = (array: Array<any>) => {
  return array.reduce((acc: any, item: any) => {
    acc[item.raw] = item.value == "-" ? null : item.value;
    return acc;
  }, {});
};

const removeUndefinedProperties = (obj: any) => {
  return Object.fromEntries(
    Object.entries(obj).filter(([_, value]) => {
      if (typeof value === "string") {
        return value.trim() !== "";
      }
      return value !== undefined && value !== null;
    })
  );
};

const trimAndRemoveEmptyProperties = (obj: any) => {
  return Object.fromEntries(
    Object.entries(obj)
      .map(([key, value]) => [
        key,
        typeof value === "string" ? value.trim() : value,
      ])
      .filter(([_, value]) => value !== undefined && value !== "")
  );
};

const formatCurrency = (value: any) => {
  if (!value || value.length === 0) {
    return "Free";
  } else {
    let formatString: string;
    switch (value[0].rateApplyTypeCode) {
      case "D":
        formatString = "Won";
        break;
      case "P":
        formatString = "%";
        break;
      default:
        formatString = "Won";
        break;
    }
    if (value.length > 0) {
      if (value[0].bsfAmt && value[0].bsfAmt != "0.00") {
        const bsfAmt = formatNumber(value[0].bsfAmt);
        return `${parseInt(bsfAmt).toLocaleString("ko-KR")} ${formatString}`;
      }
      if (value[0].dcRate && value[0].dcRate != "0.00") {
        const dcRate = formatNumber(value[0].dcRate);
        return `${parseInt(dcRate).toLocaleString("ko-KR")} ${formatString}`;
      }
    }
  }
};

const formatDate = (
  dateTime: string | any,
  type: string | string[] = [
    DATE_FORMAT.DATE_TYPE,
    DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE,
  ],
  format: string = DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE
) => {
  if (!moment(dateTime, type).isValid()) {
    return null;
  }
  const date = moment(dateTime, type);
  return date.format(format);
};

/**
 * Format date with default date type and format
 * @param dateTime
 * @returns formatted date YYYY/MM/DD
 * @example
 * formatDateDefault("2021-08-01T00:00:00.000Z") => "2021/08/01"
 **/
export const formatDateDefault = (dateTime: string | any) => {
  return formatDate(
    dateTime,
    DATE_FORMAT.DATE_TYPE,
    DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE
  );
};

export const formatDateWithOutSeconds = (dateTime: string | any) => {
  return formatDate(
    dateTime,
    DATE_FORMAT.DATE_TYPE,
    DATE_FORMAT.DATE_TIME_FORMAT_WITHOUT_SECONDS
  );
};

const removeTrailingZeros = (num: number | string) => {
  // Convert the number to a string
  const numStr = num.toString();

  // Check if the number ends with "00"
  if (numStr.endsWith("00")) {
    // Remove the trailing "00"
    return numStr.slice(0, -2);
  } else {
    // Return the original number
    return numStr;
  }
};
const formatNumber = (num: string) => {
  return Number(num).toFixed(2);
};

const highlightText = (valueToHighlight: string, searchValue: string) => {
  const escapedSearchText = escapeRegExp(searchValue);
  // eslint-disable-next-line security/detect-non-literal-regexp
  const regex = new RegExp(`(${escapedSearchText})`, "gi");
  return valueToHighlight.replace(regex, '<span class="highlight">$1</span>');
};

const handleDisplayValue = (description: string, value: string) => {
  if (description && value) {
    return `${description} (${value})`;
  } else if (description) {
    return description;
  } else if (value) {
    return value;
  } else {
    return "-";
  }
};

const handleOptionSelect = (array: Array<any>) => {
  const data = array.map((el) => {
    return { id: el.value, name: el.label, value: el.value };
  });
  return data;
};

const findItemByColumnName = (columnType: any, columnName: string) => {
  return columnType.find((item: any) => item.columnName === columnName);
};

const getNameByCode = (code: string, list: any) => {
  return list.find((el: any) => el.itemCode === code)?.itemName || "-";
};

const isExpired = (endDate: string | null) => {
  return !!(
    endDate &&
    moment().diff(
      moment(endDate, [
        DATE_FORMAT.DATE_TYPE,
        DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE,
      ]).endOf("day")
    ) > 0
  );
};
const isExpiredTime = (endDate: string | null | undefined) => {
  return !!(
    endDate &&
    moment().diff(moment(endDate, DATE_FORMAT.DATE_TYPE).endOf("minutes")) > 0
  );
};

const getHasUuidInList = (list: any, uuid: string) => {
  return list.findIndex((el: any) => el.objUuid === uuid);
};

const displayTextArea = (text: String) => {
  if (text) {
    return text.replace(/\n/g, "<br>");
  }
  return "-";
};

const escapeRegExp = (text: String) => {
  return text.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
};

const formatNumberLocaleString = (value: number, locale = "ko-KR") => {
  return value.toLocaleString(locale);
};
const findItemCodeName = (code: string) => {
  return `${OFFER_TYPES_CONVERT[code as string]} (${code})`;
};
const filterParamsAdvanced = (data: any) => {
  return {
    ...data,
    general: data.general.map((item) => {
      return {
        fieldName: item.fieldName,
        fieldType: item.fieldType,
        fieldValue: item.fieldValue,
        fieldValueMin: item.fieldValueMin,
        fieldValueMax: item.fieldValueMax,
      };
    }),
    additional: data.additional.map((item) => {
      return {
        fieldName: item.fieldName,
        fieldType: item.fieldType,
        fieldValue: item.fieldValue,
        fieldValueMin: item.fieldValueMin,
        fieldValueMax: item.fieldValueMax,
      };
    }),
  };
};

/**
 * Checks if value type String | undefined.
 *
 * @param {string} value - A value type String | undefined.
 * @returns {boolean} - `true` if `value` is type String | undefined.
 */
const isNullOrUndefinedOrEmptyOrBlank = (value: String | undefined) => {
  return (
    value === null || value === undefined || value === "" || value.trim() === ""
  );
};

/**
 * Checks if `time1` is before `time2`.
 *
 * @param {string} time1 - A string representing the first time.
 * @param {string} time2 - A string representing the second time.
 * @param {string} [format='YYYY-MM-DD HH:mm:ss'] - The format of the time strings.
 * @returns {boolean} - `true` if `time1` is before `time2`, otherwise `false`.
 * @throws {Error} - If either of the time strings is invalid according to the provided format.
 */
const isTime1BeforeTime2 = (time1, time2, format = DATE_FORMAT.DATE_TYPE) => {
  if (
    isNullOrUndefinedOrEmptyOrBlank(time1) ||
    isNullOrUndefinedOrEmptyOrBlank(time2)
  ) {
    return;
  }
  const momentTime1 = moment(time1, format);
  const momentTime2 = moment(time2, format);

  if (!momentTime1.isValid() || !momentTime2.isValid()) {
    throw new Error("Invalid date format");
  }

  return momentTime1.isBefore(momentTime2);
};

/**
 * Checks if `time1` is the same as `time2`.
 *
 * @param {string} time1 - A string representing the first time.
 * @param {string} time2 - A string representing the second time.
 * @param {string} [format='YYYY-MM-DD HH:mm:ss'] - The format of the time strings.
 * @returns {boolean} - `true` if `time1` is the same as `time2`, otherwise `false`.
 * @throws {Error} - If either of the time strings is invalid according to the provided format.
 */
const isTime1SameAsTime2 = (time1, time2, format = DATE_FORMAT.DATE_TYPE) => {
  if (
    isNullOrUndefinedOrEmptyOrBlank(time1) ||
    isNullOrUndefinedOrEmptyOrBlank(time2)
  ) {
    return;
  }
  const momentTime1 = moment(time1, format);
  const momentTime2 = moment(time2, format);

  if (!momentTime1.isValid() || !momentTime2.isValid()) {
    throw new Error("Invalid date format");
  }

  return momentTime1.isSame(momentTime2);
};

/**
 * Checks if `time1` is after `time2`.
 *
 * @param {string} time1 - A string representing the first time.
 * @param {string} time2 - A string representing the second time.
 * @param {string} [format='YYYY-MM-DD HH:mm:ss'] - The format of the time strings.
 * @returns {boolean} - `true` if `time1` is after `time2`, otherwise `false`.
 * @throws {Error} - If either of the time strings is invalid according to the provided format.
 */
const isTime1AfterTime2 = (time1, time2, format = DATE_FORMAT.DATE_TYPE) => {
  if (
    isNullOrUndefinedOrEmptyOrBlank(time1) ||
    isNullOrUndefinedOrEmptyOrBlank(time2)
  ) {
    return;
  }
  const momentTime1 = moment(time1, format);
  const momentTime2 = moment(time2, format);

  if (!momentTime1.isValid() || !momentTime2.isValid()) {
    throw new Error("Invalid date format");
  }

  return momentTime1.isAfter(momentTime2);
};

/**
 * Capitalizes the first letter of a given string and converts the rest of the string to lowercase.
 *
 * @param {string} val - The input string to be capitalized.
 * @returns {string} - The string with the first letter capitalized and the rest in lowercase.
 * @example
 * capitalizeFirstLetter("hello"); // returns "Hello"
 * capitalizeFirstLetter("WORLD"); // returns "World"
 */
const capitalizeFirstLetter = (val: String) => {
  return (
    String(val).charAt(0).toUpperCase() + String(val).slice(1).toLowerCase()
  );
};

export {
  removeUndefinedProperties,
  formatCurrency,
  formatDate,
  convertArrayToObject,
  removeTrailingZeros,
  highlightText,
  handleDisplayValue,
  handleOptionSelect,
  findItemByColumnName,
  getNameByCode,
  formatNumber,
  isExpired,
  isExpiredTime,
  getHasUuidInList,
  trimAndRemoveEmptyProperties,
  displayTextArea,
  escapeRegExp,
  formatNumberLocaleString,
  findItemCodeName,
  filterParamsAdvanced,
  isTime1BeforeTime2,
  isTime1SameAsTime2,
  isTime1AfterTime2,
  isNullOrUndefinedOrEmptyOrBlank,
  capitalizeFirstLetter,
};
