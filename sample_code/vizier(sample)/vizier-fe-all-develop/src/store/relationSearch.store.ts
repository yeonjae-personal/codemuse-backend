import { cloneDeep } from "lodash-es";
import { removeUndefinedProperties } from "@/utils/format-data";
import {
  getExtendsDependencyRelationDefinition,
  initCreateInfoApi,
  getRelationSearchAdvanced,
  putRelation,
  postRelation,
  getRelationCreateInfo,
} from "@/api/prod/extendsApi";
import { NM_CD_FIELDS } from "@/constants/impactAnalysis";
import {
  ADVENCED_SEARCH_PARAMS_DEFAULT,
  DETAIL_TAB_TYPE,
} from "@/constants/index";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { getUserInfor } from "@/constants/userInfor";
import { OFFER_TABS_VALUE } from "@/constants/offer";

const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};

const createRelationStore = (storeName) => {
  return defineStore(storeName, {
    state: () => ({
      isEdit: false,
      isDuplicate: false,
      isViewMode: false,
      isApplied: false,
      isShowRelationDetail: false,
      currentTab: OFFER_TABS_VALUE.DEFINITION,
      selectedNmCdRelationSearch: NM_CD_FIELDS[0].value,
      paramsExtendsRelationSearch: cloneDeep(ADVENCED_SEARCH_PARAMS_DEFAULT),
      extendRelationSearch: {
        items: [],
        pagination: cloneDeep(defaultPagination),
      },
      relationCodeList: null as any,
      selectedRelation: null as any,
      relationDetail: {
        generalTab: null as any,
        additionalTab: null as any,
        historyTab: [],
      },
      advancedSearchList: [] as any,
      initInput: null as any,
    }),
    actions: {
      async getRelationSearch() {
        try {
          removeUndefinedProperties(this.paramsExtendsRelationSearch);
          const res = await getRelationSearchAdvanced(
            this.paramsExtendsRelationSearch
          );
          const { page, size, totalElements, elements, totalPages } = res.data;
          this.extendRelationSearch.items = elements as any;
          this.extendRelationSearch.pagination = {
            totalSearchItems: totalElements,
            currentPage: page,
            pageSize: size,
            totalItems: totalElements,
            totalPages: totalPages,
          };
        } catch (error) {
          throw error;
        }
      },
      resetRelationParamSearch() {
        this.selectedNmCdRelationSearch = NM_CD_FIELDS[0].value;
        this.paramsExtendsRelationSearch = cloneDeep(
          ADVENCED_SEARCH_PARAMS_DEFAULT
        );
        this.extendRelationSearch.items = [];
        this.selectedRelation = null;
        this.isShowRelationDetail = false;
        this.isEdit = false;
        this.isDuplicate = false;
      },
      async getExtendsDependencyRelationDefinitionDetail(
        dpdcRelUuid: any,
        isAdd = false,
        isCreate = false,
        itemCode = "OR"
      ) {
        try {
          const { data } = !isCreate
            ? await getExtendsDependencyRelationDefinition(dpdcRelUuid)
            : await getRelationCreateInfo({ itemCode });
          const userInfor = getUserInfor();
          const resData = !isCreate ? data : data;
          this.relationDetail.generalTab = [
            ...resData.general.map((item) => {
              if (item.colName === "chg_dept_name" && (isAdd || isCreate)) {
                return {
                  ...item,
                  attrVal: userInfor.chgDeptName,
                };
              }
              if (item.colName === "chg_user" && (isAdd || isCreate)) {
                return {
                  ...item,
                  attrVal: userInfor.chgUser,
                };
              }
              if (item.colName === "item_code" && isCreate) {
                return {
                  ...item,
                  editYn: "Y",
                };
              }
              if (
                ["obj_code", "obj_name"].includes(item.colName) &&
                (isAdd || isCreate)
              ) {
                return {
                  ...item,
                  attrVal: null,
                };
              }

              if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
                if (item.colName === "valid_end_dtm" && (isAdd || isCreate)) {
                  return {
                    ...item,
                    attrVal: null,
                  };
                }
                return {
                  ...item,
                  attrVal: item.attrVal,
                };
              }
              return {
                ...item,
                attrVal:
                  item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                    ? JSON.parse(item?.attrVal)?.filter((value: any) =>
                        value.trim()
                      ) || []
                    : item.attrVal,
              };
            }),
            ...resData.additional.filter(
              (item) => item.dispTab === DETAIL_TAB_TYPE.GENERAL
            ),
          ];
          this.relationDetail.additionalTab = resData.additional
            .filter((item) => item.dispTab !== DETAIL_TAB_TYPE.GENERAL)
            .map((item: any) => ({
              ...item,
              attrVal:
                item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                  ? JSON.parse(item?.attrVal)?.filter((value: any) =>
                      value.trim()
                    ) || []
                  : item.attrVal,
            }));
        } catch (error) {
          throw error;
        }
      },

      async setAdvancedSearchList() {
        const { data } = await initCreateInfoApi({
          itemCode: "OR",
        });
        if (data) {
          this.advancedSearchList = [...data?.additional].map((row) => ({
            ...row,
            fieldName: row.attrUuid,
          }));
        }
      },

      async upsetRelation(param: any) {
        try {
          if (this.isEdit && !this.isDuplicate) {
            return await putRelation(param);
          } else {
            return await postRelation(param);
          }
        } catch (error) {
          throw error;
        }
      },
    },
  });
};

const useRelationSearchStore = createRelationStore("relationSearchStore");
const useRelationCreateStore = createRelationStore("relationCreateStore");

export { useRelationSearchStore, useRelationCreateStore };
