import { getScreenListApi } from "@/api/admin/adminApi";
import { ScreenManagementSearchParam } from "@/interfaces/admin/admin";
import cloneDeep from "lodash-es/cloneDeep";

const useScreenStore = defineStore("screen", {
  state: () => ({
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
    async fetchScreenManagement(params?: ScreenManagementSearchParam) {
      try {
        const response = await getScreenListApi(params);
        this.items = cloneDeep(
          response.data.data.map((item) => ({
            ...item,
            enableDisable: item.actvYn === "Y" ? true : false,
            control: item.authCtrlYn === "Y" ? true : false,
          }))
        );

        this.pagination.totalItems = this.items.length;
        this.pagination.totalPages = Math.ceil(
          this.items.length / this.pagination.pageSize
        );
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

    resetPagination() {
      this.pagination = {
        totalSearchItems: 0,
        currentPage: 1,
        pageSize: 10,
        totalItems: 0,
        totalPages: 0,
      };
    },
  },
});

export default useScreenStore;
