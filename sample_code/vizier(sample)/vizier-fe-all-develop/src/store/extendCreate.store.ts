import cloneDeep from "lodash-es/cloneDeep";
import { getGroupCreateInfo, postGroup } from "@/api/prod/extendsApi";
import { DETAIL_TAB_TYPE, NM_CD_FIELDS, SPACE } from "@/constants/";
import {
  getProductStructureDetailRootApi,
  getProductsApi,
} from "@/api/prod/productApi";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { getUserInfor } from "@/constants/userInfor";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import {
  BaseItemSearchPaneDto,
  BaseSearchPaneParamClass,
} from "@/types/common";
import { SearchBy } from "@/enums";

const groupCreateDataDefault = {
  generalTab: [] as any,
  additionalTab: [] as any,
  offerTab: [] as any,
  additional: null as any,
};

const paramsExtendsAddOfferDefault = {
  ...new BaseSearchPaneParamClass(SPACE),
  onlyValidDtm: true,
};

const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 7,
  totalItems: 0,
  totalPages: 0,
};
const validationStatusDefault = {
  isGenerationFormValid: false,
  isAdditionalFormValid: false,
  generation: false,
  additional: false,
};

const useExtendCreateStore = defineStore("extendCreateStore", {
  state: () => ({
    isShowAddOffer: false,
    addOfferNmCd: NM_CD_FIELDS[0].value,
    paramsAddOffer: cloneDeep(paramsExtendsAddOfferDefault),
    addOfferList: {
      items: [] as any[],
      pagination: cloneDeep(defaultPagination),
    },
    groupDetailData: cloneDeep(groupCreateDataDefault),
    isResetCreateForm: false,
    generalGroupCode: null,
    additionalGroupCode: null,
    validationStatus: cloneDeep(validationStatusDefault),
    newOfferTabItem: {} as any,
    isViewMode: false,
    currentTab: OFFER_TABS_VALUE.GENERAL,
    groupTypes: [] as any[],
    offerTypesList: [] as any[],
    selectedOfferAddPane: null as any,
    inputValueAddOfferPane: "",
  }),
  actions: {
    async getGroupCreateInfo(itemCode = "OG", isAdd = true) {
      try {
        const userInfor = getUserInfor();
        const { data } = await getGroupCreateInfo({
          itemCode: itemCode,
        });
        if (data) {
          data.general = data.general
            .sort((after, before) => after.sortNo - before.sortNo)
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
              if (item.colName === "obj_code" && isAdd) {
                return {
                  ...item,
                  attrVal: null,
                };
              }
              if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
                return {
                  ...item,
                  attrVal: item.attrVal,
                };
              }
              if (item.colName === "item_code" && isAdd) {
                return {
                  ...item,
                  editYn: "Y",
                };
              }
              return item;
            });
          this.groupDetailData.generalTab = [
            ...data.general,
            ...data.additional?.filter(
              (item) => item.dispTab === DETAIL_TAB_TYPE.GENERAL
            ),
          ];
          this.groupDetailData.additionalTab = data?.additional
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
        }
      } catch (error) {
        throw error;
      }
    },
    async getOfferList() {
      try {
        const params = {
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
        this.addOfferList.items = elements.map((offer) => {
          const dto = new BaseItemSearchPaneDto(
            offer.objUuid,
            offer.objName,
            offer.objCode,
            offer.itemCode,
            offer.validEndDtm,
            offer.validStartDtm,
            true
          );
          return { ...offer, ...dto };
        }) as any;
        this.addOfferList.pagination = {
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
    async updateGroupDetail(param) {
      try {
        return await postGroup(param);
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
    resetGroupCreateData() {
      this.groupDetailData = cloneDeep(groupCreateDataDefault);
      this.getGroupCreateInfo();
    },
    resetParamListOfferSearch() {
      this.paramsAddOffer = cloneDeep(paramsExtendsAddOfferDefault);
      this.addOfferNmCd = NM_CD_FIELDS[0].value;
      this.addOfferList = {
        items: [],
        pagination: cloneDeep(defaultPagination),
      };
    },
    resetValidationStatus() {
      this.validationStatus = cloneDeep(validationStatusDefault);
    },
  },
});

export default useExtendCreateStore;
