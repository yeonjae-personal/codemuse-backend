import { getDomainApi } from "@/api/admin/adminApi";
import { DomainSearchParam } from "@/interfaces/admin/admin";
import cloneDeep from "lodash-es/cloneDeep";
import { httpClient } from "@/utils/http-common";

const useDomainPopupStore = defineStore("domainPopup", {
  state: () => ({
    items: [],
    pagination: {
      totalSearchItems: 0,
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      totalPages: 0,
    },
    selectedItem: {
      domnNm: "",
      domnDivsCd: "",
      domnLen: "",
    },
    domainTypeOption: [],
    domainGroupOption: [],
    error: {},
  }),
  getters: {
    paginatedItems: (state) => {
      const start =
        (state.pagination.currentPage - 1) * state.pagination.pageSize;
      const end = start + state.pagination.pageSize;
      const itemIncurrentPage = state.items.slice(start, end);
      return itemIncurrentPage;
    },

    getPagination(state: any) {
      return state.pagination;
    },

    getError(state: any) {
      return state.error;
    },

    getSelectedItem(state: any) {
      return state.selectedItem;
    },
    getDomainTypeOption(state: any) {
      return state.domainTypeOption;
    },
    getDomainGroupOption(state: any) {
      return state.domainGroupOption;
    },
  },
  actions: {
    async fetchDomains(params: DomainSearchParam) {
      try {
        const response = await getDomainApi(params);
        this.items = cloneDeep(response.data.data);
        this.pagination.totalItems = this.items.length;
        this.pagination.totalPages = Math.ceil(
          this.items.length / this.pagination.pageSize
        );
      } catch (error: any) {
        this.error = error;
        console.error("Failed to fetch items:", error);
        throw error.response?.data;
      }
    },

    async fetchDomainOption() {
      try {
        const response = await httpClient.get(`/api/comm/domn/v1/detl`);
        const data = response.data.data;

        this.domainTypeOption = data.domn_divs_cd.map((item: any) => {
          return {
            title: item.label,
            value: item.value,
          };
        });

        this.domainGroupOption = data.domn_grp_cd.map((item: any) => {
          return {
            title: item.label,
            value: item.value,
          };
        });
      } catch (error: any) {
        this.error = error;
        console.error("Failed to fetch items:", error);
        throw error.response?.data;
      }
    },

    setCurrentPage(page: number) {
      this.pagination.currentPage = page;
    },

    setPageSize(size: number) {
      this.pagination.pageSize = size;
      this.pagination.currentPage = 1;
      this.pagination.totalPages = Math.ceil(
        this.pagination.totalItems / this.pagination.pageSize
      );
    },
    resetPagination() {
      this.pagination = {
        totalSearchItems: 0,
        currentPage: 1,
        pageSize: 10,
        totalItems: 0,
        totalPages: 0,
      };
    },

    setSelectedDomain(item: any) {
      this.selectedItem = item;
    },
  },
});

export default useDomainPopupStore;
