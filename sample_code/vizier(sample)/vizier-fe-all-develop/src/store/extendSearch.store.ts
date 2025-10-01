import cloneDeep from "lodash-es/cloneDeep";
import {
  getGroup,
  getGroupDetail,
  getGroupListView,
  postGroup,
  putGroup,
} from "@/api/prod/extendsApi";
import { SEARCH_CATEGORY } from "@/constants/extendsManager";
import {
  getProductStructureDetailRootApi,
  getProductsApi,
} from "@/api/prod/productApi";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { ACTION_TYPE, DETAIL_TAB_TYPE, SPACE, VIEW_MODE } from "@/constants/";
import { getUserInfor } from "@/constants/userInfor";
import { SearchBy } from "@/enums";
import {
  BaseItemSearchPaneDto,
  BaseSearchPaneParamClass,
} from "@/types/common";

const paramsExtendsOfferSearchDefault = {
  ...new BaseSearchPaneParamClass(SPACE),
  onlyValidDtm: true,
};
const paramsExtendsGroupSearchDefault = {
  ...new BaseSearchPaneParamClass(),
  offerGroupTypeCode: null as any,
  childOffrUuid: null as any,
};
const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};
const validationStatusDefault = {
  isGenerationFormValid: false,
  isAdditionalFormValid: false,
  generation: false,
  additional: false,
};
const useExtendSearchStore = defineStore("extendSearchStore", {
  state: () => ({
    displayForm: {
      offerSearch: false,
      groupDetail: false,
      groupDuplicate: false,
      addOffer: false,
    },
    selectedGroup: null as any,
    selectedOffer: null as any,
    selectedOfferAddPane: null as any,
    inputValue: null as any,
    inputValueAddOfferPane: null as any,
    offerSearchNmCd: SearchBy.Name,
    addOfferNmCd: SearchBy.Name,
    viewMode: VIEW_MODE.GRID,
    paramsSearchOffer: cloneDeep(paramsExtendsOfferSearchDefault),
    paramsAddOffer: cloneDeep(paramsExtendsOfferSearchDefault),
    paramsSearchGroup: cloneDeep(paramsExtendsGroupSearchDefault),
    groupList: {
      items: [] as any[],
      pagination: cloneDeep(defaultPagination),
    },
    offerList: {
      items: [] as any[],
      pagination: cloneDeep(defaultPagination),
    },
    addOfferList: {
      items: [] as any[],
      pagination: cloneDeep(defaultPagination),
    },
    groupDetailData: {
      generalTab: [] as any[],
      additionalTab: [] as any[],
      historyTab: [],
      offerTab: [] as any[],
      offerTabPagination: {
        currentPage: 1,
      },
    },
    isEdit: false,
    isDuplicate: false,
    isResetGroupDetail: false,
    isOfferSearchGroup: false,
    actionType: "",
    validationStatus: cloneDeep(validationStatusDefault),
    offerTypesList: [] as any[],
    isResetValue: false,
    groupTypeList: [] as any[],
  }),
  actions: {
    async getGroupList() {
      try {
        const params = {
          itemCode: this.paramsSearchGroup.type,
          [this.paramsSearchGroup.searchBy]: this.paramsSearchGroup.searchKey,
          page: this.paramsSearchGroup.page,
          size: this.paramsSearchGroup.size,
          offerGroupTypeCode: this.paramsSearchGroup.offerGroupTypeCode,
          childOffrUuid: this.paramsSearchGroup.childOffrUuid,
        };
        const res = await getGroup(params);
        const { page, size, totalElements, elements, totalPages } = res.data;
        this.groupList.items = elements.map((grp) => {
          const dto = new BaseItemSearchPaneDto(
            grp.objUuid,
            grp.objName,
            grp.objCode,
            grp.itemCode,
            grp.validEndDtm,
            grp.validStartDtm,
            true
          );
          return { ...grp, ...dto };
        }) as any;
        this.groupList.pagination = {
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
    async getGroupListView() {
      try {
        const params = {
          itemCode: this.paramsSearchGroup.type,
          [this.paramsSearchGroup.searchBy]: this.paramsSearchGroup.searchKey,
          offrGrpCd:
            this.paramsSearchGroup.searchBy === SearchBy.Code
              ? this.paramsSearchGroup.searchKey
              : null,
          offrGrpNm:
            this.paramsSearchGroup.searchBy === SearchBy.Name
              ? this.paramsSearchGroup.searchKey
              : null,
          page: this.paramsSearchGroup.page,
          size: this.paramsSearchGroup.size,
          offerGroupTypeCode: this.paramsSearchGroup.offerGroupTypeCode,
          childOffrUuid: this.paramsSearchGroup.childOffrUuid,
        };
        const res = await getGroupListView(params);
        const { page, size, totalElements, elements, totalPages } = res.data;
        this.groupList.items = elements.map((grp) => {
          const dto = new BaseItemSearchPaneDto(
            grp.objUuid,
            grp.objName,
            grp.objCode,
            grp.itemCode,
            grp.validEndDtm,
            grp.validStartDtm,
            true
          );
          return { ...grp, ...dto };
        }) as any;
        this.groupList.pagination = {
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
    async getOfferList(category: String) {
      try {
        const params =
          category === SEARCH_CATEGORY.OFFER
            ? {
                itemCode: this.paramsSearchOffer.type,
                name:
                  this.paramsSearchOffer.searchBy === SearchBy.Name
                    ? this.paramsSearchOffer.searchKey
                    : undefined,
                code:
                  this.paramsSearchOffer.searchBy === SearchBy.Code
                    ? this.paramsSearchOffer.searchKey
                    : undefined,
                page: this.paramsSearchOffer.page,
                size: this.paramsSearchOffer.size,
                onlyValidDtm: this.paramsSearchOffer.onlyValidDtm,
              }
            : {
                itemCode: this.paramsAddOffer.type,
                name:
                  this.paramsAddOffer.searchBy === SearchBy.Name
                    ? this.paramsAddOffer.searchKey
                    : undefined,
                code:
                  this.paramsAddOffer.searchBy === SearchBy.Code
                    ? this.paramsAddOffer.searchKey
                    : undefined,
                page: this.paramsAddOffer.page,
                size: this.paramsAddOffer.size,
                onlyValidDtm: this.paramsAddOffer.onlyValidDtm,
              };
        const { data } = await getProductsApi(params);
        const { page, size, totalElements, elements, totalPages } = data;
        const customElement = elements.map((offer) => {
          const dto = new BaseItemSearchPaneDto(
            offer.objUuid,
            offer.objName,
            offer.objCode,
            offer.itemCode,
            offer.validEndDtm,
            offer.validStartDtm,
            category !== SEARCH_CATEGORY.OFFER
          );
          return { ...offer, ...dto };
        }) as any;
        if (category === SEARCH_CATEGORY.OFFER) {
          this.offerList.items = customElement;
          this.offerList.pagination = {
            totalSearchItems: totalElements,
            currentPage: page,
            pageSize: size,
            totalItems: totalElements,
            totalPages: totalPages,
          };
        } else {
          this.addOfferList.items = customElement;
          this.addOfferList.pagination = {
            totalSearchItems: totalElements,
            currentPage: page,
            pageSize: size,
            totalItems: totalElements,
            totalPages: totalPages,
          };
        }
      } catch (error) {
        throw error;
      }
    },
    async getGroupDetailInfo(isAdd = false) {
      const { data } = await getGroupDetail(this.selectedGroup.objUuid);
      if (data) {
        const userInfor = getUserInfor();
        data.general = data.general
          .sort((current, next) => current.sortNo - next.sortNo)
          .map((item) => {
            if (item.colName === "chg_dept_name" && isAdd) {
              return {
                ...item,
                attrVal: userInfor.chgDeptName,
              };
            }
            if (item.colName === "chg_user" && isAdd) {
              return {
                ...item,
                attrVal: userInfor.chgUser,
              };
            }
            if (["obj_code", "obj_name"].includes(item.colName) && isAdd) {
              return {
                ...item,
                attrVal: null,
              };
            }

            if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
              if (item.colName === "valid_end_dtm" && isAdd) {
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
            return item;
          });
        this.groupDetailData.generalTab = [
          ...data.general,
          ...data?.additional.filter(
            ({ dispTab }) => dispTab === DETAIL_TAB_TYPE.GENERAL
          ),
        ];
        this.groupDetailData.additionalTab = data?.additional
          .filter(({ dispTab }) => dispTab !== DETAIL_TAB_TYPE.GENERAL)
          .map((item: any) => ({
            ...item,
            attrVal:
              item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                ? JSON.parse(item?.attrVal)?.filter((value: any) =>
                    value.trim()
                  ) || []
                : item.attrVal,
          }));
        // Filter expired date (validEndDtm must > current date)
        this.groupDetailData.offerTab = data.childOffr
          .filter((item) => {
            if (isAdd) {
              const currentDate = new Date();
              if (!!item?.validEndDtm) {
                const endDtm = new Date(item.validEndDtm);
                return endDtm > currentDate;
              } else {
                return true;
              }
            }
            return true;
          })
          .map((item) => ({
            ...item,
            ...new BaseItemSearchPaneDto(
              item.offrUuid,
              item.offrNm,
              item.offrCd,
              item.offrType,
              item.itemValidEndDtm,
              item.itemValidStartDtm
            ),
            workTypeCode: isAdd ? ACTION_TYPE.ADD : null,
          }));
      }
    },
    async updateGroupDetail(param) {
      try {
        const valueGroupData = cloneDeep(param);

        if (this.isEdit && !this.isDuplicate) {
          return await putGroup(valueGroupData);
        } else {
          valueGroupData.dplcTrgtUuid = valueGroupData.objUuid;
          return await postGroup(valueGroupData);
        }
      } catch (error) {
        throw error;
      }
    },
    async updateOfferGroupDuplicate(param) {
      try {
        const valueGroupData = cloneDeep(param);
        return await putGroup(valueGroupData);
      } catch (error) {
        throw error;
      }
    },
    async getOfferDetail(params: any) {
      try {
        const res = await getProductStructureDetailRootApi(params);
        return res;
      } catch (error) {
        throw error;
      }
    },
    resetParamListGroupSearch() {
      this.paramsSearchGroup = cloneDeep(paramsExtendsGroupSearchDefault);
      this.groupList = {
        items: [],
        pagination: cloneDeep(defaultPagination),
      };
    },
    resetParamListOfferSearch(category: String) {
      if (category === SEARCH_CATEGORY.OFFER) {
        this.paramsSearchOffer = cloneDeep(paramsExtendsOfferSearchDefault);
        this.offerSearchNmCd = SearchBy.Name;
        this.offerList = {
          items: [],
          pagination: cloneDeep(defaultPagination),
        };
      } else {
        this.paramsAddOffer = cloneDeep(paramsExtendsOfferSearchDefault);
        this.addOfferNmCd = SearchBy.Name;
        this.addOfferList = {
          items: [],
          pagination: cloneDeep(defaultPagination),
        };
      }
    },
    resetValidationStatus() {
      this.validationStatus = cloneDeep(validationStatusDefault);
    },
  },
});

export default useExtendSearchStore;
