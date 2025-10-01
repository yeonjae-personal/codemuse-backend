import { getDomainApi } from "@/api/admin/adminApi";
import { DomainSearchParam } from "@/interfaces/admin/admin";
import cloneDeep from "lodash-es/cloneDeep";
import { httpClient } from "@/utils/http-common";
import { useLocalStorage } from "@vueuse/core";
import { INITIAL_TABS } from "@/constants/index";
const menuList = useLocalStorage("tabMenu", INITIAL_TABS);

const useDomainStore = defineStore("domain", {
  state: () => ({
    searchParams: {
      srchWord: "",
      useYn: " ",
    },
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
    async fetchDomains() {
      try {
        const params: DomainSearchParam = {
          srchWord: this.searchParams.srchWord.trim(),
          useYn: this.searchParams.useYn.trim(),
        };
        const response = await getDomainApi(params);
        this.items = cloneDeep(response.data.data);

        this.pagination.totalItems = this.items.length;
        this.pagination.totalPages = Math.ceil(
          this.items.length / this.pagination.pageSize
        );
      } catch (error: any) {
        throw error;
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
        throw error;
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
    resetItems() {
      this.items = [];
    },
    resetSearchParams() {
      this.searchParams = {
        srchWord: "",
        useYn: " ",
      };
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

const checkCurrentMenu = (id: number, newValue: Array<{ id: number }>) => {
  return newValue.some((item) => item.id === id);
};

watch(
  menuList,
  (newValue) => {
    const store = useDomainStore();
    if (
      !checkCurrentMenu(
        74,
        newValue.map(({ id }) => ({ id: Number(id) }))
      )
    ) {
      store.resetSearchParams();
      store.resetItems();
    }
  },
  { deep: true }
);

export default useDomainStore;
