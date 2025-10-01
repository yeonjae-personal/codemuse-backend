import {
  getFactorTypes,
  getFactorTypeDetail,
  getFactorDetail,
  putFactorDetail,
  postFactorDetail,
  putFactorTypeDetail,
  getFactorSearch,
  getFactorSearchInfo,
} from "@/api/admin/factor/factorApi";
import { TABS_NAME_COLLECTION } from "@/constants/index";
import { BaseItemSearchPaneDto } from "@/types/common";
import cloneDeep from "lodash-es/cloneDeep";

const factorTypeDefaultParams: any = {
  factorTypeName: "",
  factorTypeCode: "",
  page: 1,
  size: 14,
};

const factorSearchDefaultParams: any = {
  factorTypeCode: "",
  factorName: "",
  page: 1,
  size: 9,
  useYn: "Y",
};

const factorTypeDetailDefaultParams: any = {
  factorTypeCode: "",
  factorTypeName: "",
  page: 1,
  size: 8,
};

const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 8,
  totalItems: 0,
  totalPages: 0,
};
const defaultPaginationFactorDetail = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};
const defaultPaginationFactorSearch = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};

const useFactorStore = defineStore("factorStore", {
  state: () => ({
    currentTab: TABS_NAME_COLLECTION.GENERAL,
    factorsType: [] as any,
    factorsTypeTotal: 0,
    factorsSearch: {
      items: [] as any[],
      pagination: cloneDeep(defaultPaginationFactorSearch),
    },
    factorInfoSearchList: [] as any,
    factorsSearchTotal: 0,
    factorTypeSelected: null as any,
    factorSelected: null as any,
    factorBeforeEditIndex: null as any,
    factorTypeDetail: {
      factorTypeCode: "",
      factorTypeName: "",
      useYn: "",
      items: [] as any[],
      factorLst: [] as any[],
      factorSearchLst: {} as any,
      pagination: cloneDeep(defaultPagination),
    },
    factorTypeDetailBeforeEdit: null as any,
    factorDetail: null as any,
    factorDetailBeforeEdit: null as any,
    paramFilter: cloneDeep(factorTypeDefaultParams),
    paramFilterFactorSearch: cloneDeep(factorSearchDefaultParams),
    paramFilterDetail: cloneDeep(factorTypeDetailDefaultParams),
    isEditFactorTypeDetail: false,
    isEditFactorDetail: false,
    isCreateFactorDetail: false,
    isAddNewFactorChild: false,
    paginationFactorDetail: cloneDeep(defaultPaginationFactorDetail),
  }),
  actions: {
    resetParamFactorType() {
      this.factorsType = [];
      this.factorTypeSelected = null;
      this.factorTypeDetail = {
        factorTypeCode: "",
        factorTypeName: "",
        useYn: "",
        items: [] as any[],
        factorLst: [] as any[],
        factorSearchLst: {} as any,
        pagination: cloneDeep(defaultPagination),
      };
      this.factorDetail = null;
      this.isEditFactorTypeDetail = false;
      this.isCreateFactorDetail = false;
      this.isAddNewFactorChild = false;
      this.isEditFactorDetail = false;
      this.paramFilter = cloneDeep(factorTypeDefaultParams);
    },
    resetParamFactorTypeDetail() {
      this.factorDetail = null;
      this.factorSelected = null;
      this.paramFilterDetail = {
        ...factorTypeDetailDefaultParams,
        factorTypeCode: this.factorTypeSelected?.factorTypeCode,
      };
    },
    resetParamFactorSearch() {
      this.paramFilterFactorSearch = cloneDeep(factorSearchDefaultParams);
    },

    async getListFactorsType() {
      try {
        const res = await getFactorTypes(this.paramFilter);
        const { size, totalElements, elements } = res.data;
        if (elements) {
          this.factorsType =
            elements.map((item) => {
              const dto = new BaseItemSearchPaneDto(
                item.factorTypeCode,
                item.factorTypeName,
                item.factorTypeCode,
              );
              return { ...item, ...dto };
            }) || [];
          this.factorsTypeTotal = totalElements;
        }
        this.paramFilter.size = size;
      } catch (error: any) {
        throw error;
      }
    },
    async getListFactorsSearch() {
      try {
        const res = await getFactorSearch(this.paramFilterFactorSearch);
        const { page, size, totalElements, elements, totalPages } = res.data;
        if (elements) {
          this.factorsSearch = {
            items: elements || [],
            pagination: {
              totalSearchItems: totalElements,
              currentPage: page,
              pageSize: size,
              totalItems: totalElements,
              totalPages: totalPages,
            },
          };
          this.factorsSearchTotal = totalElements;
        }
        this.paramFilterFactorSearch.size = size;
      } catch (error: any) {
        throw error;
      }
    },
    async getFactorSearchInfo() {
      try {
        const res = await getFactorSearchInfo();
        if (res.data) {
          this.factorInfoSearchList = res.data;
        }
      } catch (error: any) {
        throw error;
      }
    },
    async getDetailFactorType() {
      this.factorTypeDetailBeforeEdit = null;
      try {
        const res = await getFactorTypeDetail(this.paramFilterDetail);

        if (res.data) {
          const dataRes = res.data;
          dataRes.factorLst.forEach((element, index) => {
            element["index"] = index;
          });

          this.factorTypeDetail = {
            ...dataRes,
            pagination: {
              totalSearchItems: dataRes?.factorLst?.length,
              currentPage: 1,
              pageSize: this.isEditFactorTypeDetail ? 7 : 8,
              totalItems: dataRes?.factorLst?.length,
              totalPages: Math.ceil(
                dataRes?.factorLst?.length /
                  (this.isEditFactorTypeDetail ? 7 : 8)
              ),
            },
          };
        }
      } catch (error: any) {
        throw error;
      }
    },
    async getDetailFactor(factorCode) {
      try {
        const res = await getFactorDetail({ factorCode: factorCode });

        if (res.data) {
          this.factorDetail = { ...res.data, isFetch: true };
        }
      } catch (error: any) {
        throw error;
      }
    },
    async putDetailFactor(param) {
      try {
        const res = await putFactorDetail(param);
        return res.data;
      } catch (error: any) {
        throw error;
      }
    },
    async postDetailFactor(param) {
      try {
        const res = await postFactorDetail(param);
        return res.data;
      } catch (error: any) {
        throw error;
      }
    },
    async putDetailFactorType(param) {
      try {
        const res = await putFactorTypeDetail(param);
        return res;
      } catch (error: any) {
        throw error;
      }
    },
  },
});

export default useFactorStore;
