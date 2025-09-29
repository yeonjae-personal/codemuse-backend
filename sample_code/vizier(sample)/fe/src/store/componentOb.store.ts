import { getListComponentSearchApi } from "@/api/prod/componentApi";
import { RequestGetListComponentSearch } from "@/interfaces/prod/component";
import {
  BaseItemSearchPaneDto,
  BaseSearchPaneParamClass,
} from "@/types/common";
import { cloneDeep } from "lodash-es";

const paramsComponentSearchOBDefault: any = {
  ...new BaseSearchPaneParamClass("", " "),
  baseUuid: null,
  offerUUID: null,
  onlyValidDtm: true,
};

const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};

const useComponentOBStore = defineStore("ComponentOBStore", {
  state: () => ({
    paramsSearchComponent: cloneDeep(paramsComponentSearchOBDefault),
    componentList: {
      items: [] as any[],
      pagination: cloneDeep(defaultPagination),
    },
    dragType: "",
  }),
  actions: {
    async getListComponentSearch(params: RequestGetListComponentSearch) {
      const { data } = await getListComponentSearchApi(params);
      const { page, size, totalElements, elements, totalPages } = data;
      this.componentList.items = elements.map((component) => {
        const dto = new BaseItemSearchPaneDto(
          component.uuid,
          component.name,
          component.code,
          component.itemCode,
          component.endDate,
          component.startDate,
          true,
          true,
          false,
          component.itemType
        );
        return { ...component, ...dto };
      }) as any;
      this.componentList.pagination = {
        ...this.componentList.pagination,
        totalSearchItems: totalElements,
        currentPage: page,
        pageSize: size,
        totalItems: totalElements,
        totalPages: totalPages,
      };
    },
    resetParamListComponentSearch() {
      this.paramsSearchComponent = cloneDeep(paramsComponentSearchOBDefault);
      this.componentList = {
        items: [],
        pagination: cloneDeep(defaultPagination),
      };
    },
  },
});

export default useComponentOBStore;
