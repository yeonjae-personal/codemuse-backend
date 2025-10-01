import { getSysMsgApi } from "@/api/admin/adminApi";
import { SysMsgSearchRequest } from "@/interfaces/admin/admin";
import cloneDeep from "lodash-es/cloneDeep";
import { useLocalStorage } from "@vueuse/core";
import { INITIAL_TABS } from "@/constants/index";
const menuList = useLocalStorage("tabMenu", INITIAL_TABS);

const useSysMessageStore = defineStore("message", {
  state: () => ({
    searchParams: {
      sysMsgId: "",
      sysMsgCntn: "",
    },
    items: [],
    pagination: {
      totalSearchItems: 0,
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      totalPages: 0,
    },
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
  },
  actions: {
    async fetchSysMessages() {
      try {
        const response = await getSysMsgApi(this.searchParams);
        this.items = cloneDeep(response.data.data);

        this.pagination.totalItems = this.items.length;
        this.pagination.totalPages = Math.ceil(
          this.items.length / this.pagination.pageSize
        );
      } catch (error) {
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
    resetPagination() {
      this.pagination = {
        totalSearchItems: 0,
        currentPage: 1,
        pageSize: 10,
        totalItems: 0,
        totalPages: 0,
      };
    },
    updateSearchParams(params: SysMsgSearchRequest) {
      this.searchParams = params;
    },
    resetSearchParams() {
      this.searchParams = {
        sysMsgId: "",
        sysMsgCntn: "",
      };
    },
  },
});
const checkCurrentMenu = (id: number, newValue: Array<{ id: number }>) => {
  return newValue.some((item) => item.id === id);
};

watch(
  menuList,
  (newValue) => {
    const store = useSysMessageStore();
    if (
      !checkCurrentMenu(
        73,
        newValue.map(({ id }) => ({ id: Number(id) }))
      )
    ) {
      store.resetSearchParams();
      store.resetItems();
    }
  },
  { deep: true }
);

export default useSysMessageStore;
