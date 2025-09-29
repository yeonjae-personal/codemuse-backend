import { getMatrix } from "@/api/admin/matrix/matrixApi";
import cloneDeep from "lodash-es/cloneDeep";

const matrixSearchDefaultParams: any = {
  matrixCodeName: "",
  page: 1,
  size: 6,
};
const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 6,
  totalItems: 0,
  totalPages: 0,
};
const useMatrixSearchPaneStore = defineStore("matrixSearchPaneStore", {
  state: () => ({
    paramMatrixSearch: cloneDeep(matrixSearchDefaultParams),
    listMatrix: [] as any[],
    matrixSelected: null as any,
    matrixSearchPagination: cloneDeep(defaultPagination),
  }),
  actions: {
    resetParamMatrixSearch() {
      this.matrixSelected = null;
      this.paramMatrixSearch = cloneDeep(matrixSearchDefaultParams);
      this.matrixSearchPagination = cloneDeep(defaultPagination);
      this.listMatrix = [];
    },

    async getListMatrix() {
      try {
        const res = await getMatrix(this.paramMatrixSearch);
        const { page, size, totalElements, elements, totalPages } = res.data;
        if (elements) {
          this.listMatrix = elements.map((item) => ({
            ...item,
            itemDescription: item.matrixCode,
            itemName: item.matrixCodeName,
          }));
          this.matrixSearchPagination = {
            totalSearchItems: totalElements,
            currentPage: page,
            pageSize: size,
            totalItems: totalElements,
            totalPages: totalPages,
          };
        }
      } catch (error: any) {
        throw error;
      }
    },
  },
});

export default useMatrixSearchPaneStore;
