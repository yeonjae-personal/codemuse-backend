/**
 * @description Create or update status
 * - create: Create 
 * - update: Update 
 */
export const FORM_TYPE_OPTION = {
    CREATE: "create",
    UPDATE: "update",
  };
   

/**
 * @description Use YN option
 * - Y: Yes
 * - N: No
 * - ALL: All
 **/
export const USE_YN_OPTION_CREATE = [
    { title: "Y", value: "Y" },
    { title: "N", value: "N" },
];

/**
 * @description enum for Duplicate 
 * - OK: OK
 * - FAIL: FAIL
 **/
export enum DuplicateCodeStatus {
  OK = "ok",
  FAIL = "fail"
}