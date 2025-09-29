import { getListResourceAdvancedApi } from "@/api/prod/resourceApi";
import {
  BaseItemSearchPaneDto,
  BaseSearchPaneParamClass,
} from "@/types/common";
import { removeUndefinedProperties } from "@/utils/format-data";
import { cloneDeep } from "lodash-es";

const paramsResourceSearchDefault: any = {
  ...new BaseSearchPaneParamClass(" "),
};

const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};

const useResourceOBStore = defineStore("ResourceOBStore", {
  state: () => ({
    paramsSearchResource: cloneDeep(paramsResourceSearchDefault),
    resourceList: {
      items: [] as any[],
      pagination: cloneDeep(defaultPagination),
    },
    dragType: "",
  }),
  actions: {
    async getListResourceSearch() {
      const params = {
        itemCode: this.paramsSearchResource.type,
        onlyValidDtm: true,
        page: this.paramsSearchResource.page,
        size: this.paramsSearchResource.size,
        objName:
          this.paramsSearchResource.searchBy === "objName"
            ? this.paramsSearchResource.searchKey
            : undefined,
        objCode:
          this.paramsSearchResource.searchBy === "objCode"
            ? this.paramsSearchResource.searchKey
            : undefined,
      };
      removeUndefinedProperties(params);
      const { data } = await getListResourceAdvancedApi(params);
      const { page, size, totalElements, elements, totalPages } = data;
      this.resourceList.items = elements.map((resource) => {
        const dto = new BaseItemSearchPaneDto(
          resource.objUuid,
          resource.objName,
          resource.objCode,
          resource.itemCode,
          resource.validEndDtm,
          resource.validStartDtm,
          true,
          true,
          false
        );
        return { ...resource, ...dto };
      }) as any;
      this.resourceList.pagination = {
        totalSearchItems: totalElements,
        currentPage: page,
        pageSize: size,
        totalItems: totalElements,
        totalPages: totalPages,
      };
    },
    resetParamListResourceSearch() {
      this.paramsSearchResource = cloneDeep(paramsResourceSearchDefault);
      this.resourceList = {
        items: [],
        pagination: cloneDeep(defaultPagination),
      };
    },
  },
});

export default useResourceOBStore;
