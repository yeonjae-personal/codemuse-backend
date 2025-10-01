/* eslint-disable id-length */
import {
  getMatrix,
  getHeader,
  getListTable,
  putMatrixDetail,
  postMatrixDetail,
} from "@/api/admin/matrix/matrixApi";
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
const useMatrixStructureStore = defineStore("matrixStructureStore", {
  state: () => ({
    paramMatrixSearch: cloneDeep(matrixSearchDefaultParams),
    listMatrix: [] as any[],
    matrixSelected: null as any,
    matrixSearchPagination: cloneDeep(defaultPagination),
    isCreate: false,
    isEdit: false,
    isShowMatrixSearch: true,
    isShowFactorSearch: false,
    isBuilder: false,
    isMultiEdit: false,
    headersTableMatrix: [] as any[],
    listTableMatrix: [] as any[],
    listTableMatrixTemp: [] as any[],
    listTempItems: [] as any[],
    matrixBuilderFactors: [] as any[],
    builderFactorCols: 5,
  }),
  actions: {
    resetParamMatrixSearch() {
      this.matrixSelected = null;
      this.paramMatrixSearch = cloneDeep(matrixSearchDefaultParams);
      this.matrixSearchPagination = cloneDeep(defaultPagination);
      this.listMatrix = [];
    },
    resetDataTableMatrix() {
      this.headersTableMatrix = [];
      this.listTableMatrix = [];
      this.listTableMatrixTemp = [];
    },

    async getHeaderTableMatrix(matrixCode: any = null) {
      try {
        const res = await getHeader({
          matrixCode,
        });
        const res1 = cloneDeep(res.data);
        const res2 = cloneDeep(res.data);
        if (res.data) {
          this.headersTableMatrix = [
            ...res1.map((xxx) => ({
              ...xxx,
              factorValues: xxx.factorValues.filter((factor) => factor.inUse),
              isFilter: false,
              valueSort: null,
            })),
            {
              seqNo: cloneDeep(res.data).length + 1,
              factorCode: "VALUE",
              factorName: "value",
            },
          ];

          this.matrixBuilderFactors = cloneDeep(
            res2.map((item: any, index) => ({
              ...item,
              x: item.x ?? index % this.builderFactorCols,
              y: item.y ?? Math.ceil(index / this.builderFactorCols),
              w: 1,
              h: 1,
              i: item.factorCode,
              loading: false,
            }))
          );
        }
      } catch (error: any) {
        throw error;
      }
    },
    async getListTableMatrix(
      matrixCode: any = null,
      builderReqDto = null as any
    ) {
      try {
        const res = await getListTable(matrixCode, builderReqDto);
        if (res.data) {
          this.listTableMatrix =
            res.data?.map((item, index) => ({
              ...item,
              rowId: index + 1,
              measureDDtos: [
                ...item.measureDDtos,
                {
                  factorCode: "VALUE",
                  factorValueName: item.matrixNumValue,
                },
              ],
            })) || [];
          this.listTableMatrixTemp = this.listTableMatrix;
        }
      } catch (error: any) {
        throw error;
      }
    },
    async getListMatrix() {
      try {
        const res = await getMatrix(this.paramMatrixSearch);
        const { page, size, totalElements, elements, totalPages } = res.data;
        if (elements) {
          this.listMatrix = elements || [];
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

    async putDetailMatrix(matrixCode, param) {
      try {
        const res = await putMatrixDetail(matrixCode, param);
        return res;
      } catch (error: any) {
        throw error;
      }
    },
    async postDetailMatrix(param) {
      try {
        const res = await postMatrixDetail(param);
        return res;
      } catch (error: any) {
        throw error;
      }
    },
  },
});

export default useMatrixStructureStore;
