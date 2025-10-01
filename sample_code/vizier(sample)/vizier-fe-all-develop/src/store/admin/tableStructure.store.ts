import {
  getHeaderTable,
  getTableTypes,
  getTableTypesDetails,
  putTableTypesDetails,
  getItemsTable,
  putTableStructure,
} from "@/api/admin/tableStructure/tableStructureApi";
import { DATE_FORMAT, TABS_NAME_COLLECTION } from "@/constants/index";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { BaseItemSearchPaneDto } from "@/types/common";
import { formatDate } from "@/utils/format-data";
import cloneDeep from "lodash-es/cloneDeep";

const tableSearchDefaultParams: any = {
  tableTypeCode: undefined,
  tableTypeName: undefined,
  page: 1,
  size: 16,
};
const tableDetailDefaultParams: any = {
  tableTypeCode: undefined,
  tableName: undefined,
  page: 1,
  size: 10,
};
const tableListDefaultParams: any = {
  page: 1,
  size: 17,
  fieldSearchs: [] as any[],
  fieldSorts: {},
};
const useTableStructureStore = defineStore("tableStructureStore", {
  state: () => ({
    loading: false,
    isRedirectTo: false,
    tableSelected: null as any,
    currentTab: TABS_NAME_COLLECTION.GENERAL,
    isShowTableTypeSearch: true,
    isShowTableTypeDetail: false,
    isEditTable: false,
    isEditTableType: false,
    tableTypeSearchParams: cloneDeep(tableSearchDefaultParams),
    tableTypeDetailParams: cloneDeep(tableDetailDefaultParams),
    tableListParams: cloneDeep(tableListDefaultParams),
    headerListParams: cloneDeep(tableListDefaultParams),
    tableTypeSearchList: [] as any[],
    tableTypeSearchTotal: 0,
    tableSearchTotal: 0,
    selectedTableType: null as any,
    tableTypeGeneralInit: null as any,
    isGetAllTableTypeSearch: false,
    isGetAllTable: false,
    isGetAllTableRecord: false,
    tableTypeDetails: null as any,
    tableTabList: [] as any[],
    headerList: [] as any[],
    tableItemsList: [] as any[],
    tableItemsListTemp: [] as any[],
    lastNameColumnFilter: null as any,
    isHasBeenCalled: false,
  }),
  actions: {
    async getListTableType() {
      try {
        const res = await getTableTypes(this.tableTypeSearchParams);
        const { page, size, totalElements, elements } = res.data;
        if (elements?.length) {
          const tables = elements.map((item) => {
            const dto = new BaseItemSearchPaneDto(
              item.tableTypeCode,
              item.tableTypeName,
              item.tableTypeCode
            );
            return { ...item, ...dto, showAppendIcon: false };
          });
          this.tableTypeSearchList = this.tableTypeSearchList.concat(tables);
          this.tableTypeSearchTotal = totalElements;
        }
        this.isGetAllTableTypeSearch = totalElements <= page * size;
        this.tableTypeSearchParams.size = size;
      } catch (error: any) {
        throw error;
      }
    },
    async getListTableTypeDetail() {
      try {
        const res = await getTableTypesDetails(
          this.tableTypeDetailParams.tableTypeCode,
          this.tableTypeDetailParams
        );
        if (res?.data) {
          this.tableTypeDetails = res.data;
          this.tableTypeGeneralInit = {
            tableTypeCode: res.data?.tableTypeCode,
            tableTypeName: res.data?.tableTypeName,
            useYn: res.data?.useYn,
          };

          if (res.data?.tableStrcDtos) {
            const { page, size, totalElements, elements } =
              res.data?.tableStrcDtos;
            this.tableSearchTotal = totalElements;
            this.tableTabList = elements ?? [];
            this.isGetAllTable = totalElements <= page * size;
          }
        }
      } catch (error: any) {
        throw error;
      }
    },
    async getListTable() {
      try {
        const res = await getTableTypesDetails(
          this.tableTypeDetailParams.tableTypeCode,
          this.tableTypeDetailParams
        );
        if (res?.data?.tableStrcDtos) {
          const { page, size, totalElements, elements } =
            res.data?.tableStrcDtos;
          this.tableSearchTotal = totalElements;
          this.tableTabList = this.tableTabList.concat(elements);
          this.isGetAllTable = totalElements <= page * size;
        }
      } catch (error: any) {
        throw error;
      }
    },
    async putTableTypeDetail() {
      const params = {
        tableTypeCode: this.tableTypeDetails?.tableTypeCode,
        tableTypeName: this.tableTypeDetails?.tableTypeName,
        useYn: this.tableTypeDetails?.useYn,
        sortNo: this.tableTypeDetails?.sortNo,
        tableStrcDtos: null,
      };
      try {
        const res = await putTableTypesDetails(
          this.selectedTableType?.tableTypeCode,
          params
        );
        return res;
      } catch (error: any) {
        throw error;
      }
    },
    resetTableTypeSearch() {
      this.tableTypeSearchParams = cloneDeep(tableSearchDefaultParams);
      this.tableTypeSearchList = [];
      this.tableTypeSearchTotal = 0;
      this.selectedTableType = null;
      this.isGetAllTableTypeSearch = false;
    },
    resetTableSearch() {
      this.tableTypeDetailParams = cloneDeep(tableDetailDefaultParams);
      this.resetTableSearchData();
    },
    resetTableSearchData() {
      this.tableTabList = [];
      this.tableSearchTotal = 0;
      this.tableSelected = null;
      this.isGetAllTable = false;
    },

    async getListHeader(filtering: boolean = false) {
      try {
        const res = await getHeaderTable(
          this.tableSelected?.tableName,
          this.headerListParams
        );
        if (res.data) {
          const lastFilterHeader = filtering
            ? this.headerList?.find((item) => item?.isLastColFilter)
            : null;

          this.headerList = res.data.map((header, index) => {
            if (filtering) {
              const currentHeader = this.headerList[index as number];
              if (!currentHeader?.isLastColFilter) {
                return {
                  ...header,
                  isSort: "",
                  isFilter: currentHeader?.isFilter,
                  fieldValueMin: null,
                  fieldValue: null,
                  fieldValueMax: null,
                  itemsDL: currentHeader?.isFilter
                    ? currentHeader?.itemsDL
                    : [],
                };
              }
              return lastFilterHeader;
            }
            return {
              ...header,
              isSort: "",
              isFilter: false,
              fieldValueMin: null,
              fieldValue: null,
              fieldValueMax: null,
              itemsDL: [],
            };
          });
        }
      } catch (error: any) {
        throw error;
      }
    },
    async getListTableItems() {
      try {
        const res = await getItemsTable(
          this.tableSelected?.tableName,
          this.tableListParams
        );
        if (res?.data) {
          const { page, size, totalElements, elements } = res.data;

          const dataExist =
            elements?.map((item) => {
              return this.headerList.reduce((acc, header) => {
                const key = header.columnName;
                const rawValue = item[key as string];

                if (header.columnType === COLUMN_FIELD_TYPE.DM) {
                  try {
                    acc[key as string] = JSON.parse(rawValue || "[]");
                  } catch {
                    acc[key as string] = [];
                  }
                } else if (header.columnType === COLUMN_FIELD_TYPE.DP) {
                  acc[key as string] = formatDate(
                    rawValue,
                    DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE,
                    DATE_FORMAT.DATE_TYPE
                  );
                } else {
                  acc[key as string] = rawValue !== undefined ? rawValue : null;
                }

                return acc;
              }, {});
            }) || [];

          this.tableItemsList = this.tableItemsList.concat(dataExist);

          this.tableItemsListTemp = this.tableItemsListTemp.concat(
            cloneDeep(dataExist)
          );

          this.isGetAllTableRecord = page * size >= totalElements;

          if (dataExist.length <= totalElements) {
            const objNew = {} as any;
            this.headerList?.forEach((header) => {
              objNew[header.columnName] = null;
            });
            objNew.status = "new";
            if (page === 1) {
              for (let index = 0; index < size - dataExist.length; index++) {
                this.tableItemsList = cloneDeep([
                  ...this.tableItemsList,
                  objNew,
                ]);
                this.tableItemsListTemp = cloneDeep([
                  ...this.tableItemsListTemp,
                  objNew,
                ]);
              }
            } else if (page === size) {
              this.tableItemsList = cloneDeep([...this.tableItemsList, objNew]);
              this.tableItemsListTemp = cloneDeep([
                ...this.tableItemsListTemp,
                objNew,
              ]);
            }
          }
        }
      } catch (error: any) {
        throw error;
      }
    },
    resetTableListParams() {
      this.tableListParams = cloneDeep(tableListDefaultParams);
      this.tableItemsList = [];
      this.tableItemsListTemp = [];
      this.isGetAllTableRecord = false;
    },
    async putTableStructure(data) {
      try {
        const res = await putTableStructure(
          this.tableSelected?.tableName,
          data
        );
        return res;
      } catch (error: any) {
        throw error;
      }
    },
  },
});

export default useTableStructureStore;
