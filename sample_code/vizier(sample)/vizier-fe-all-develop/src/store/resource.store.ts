import cloneDeep from "lodash-es/cloneDeep";
import {
  IParamsGetListResourceAdd,
  IParamsGetResourceDetail,
  IResourceItem,
} from "@/interfaces/prod/resource";
import { getListComponentSearchApi } from "@/api/prod/componentApi.ts";
import { RequestGetListComponentSearch } from "@/interfaces/prod/component";
import {
  createResourceApi,
  getFormatCreateResourceApi,
  getListResourceApi,
  getResourceDetailApi,
  updateResourceApi,
  getListResourceAddApi,
  getListResourceAdvancedApi,
  getListResourceAdvancedDetailApi,
} from "@/api/prod/resourceApi";
import {
  PARAMS_DEFAULT_COMPONENT,
  PRODUCT_ITEM_NAME,
} from "@/constants/resource";
import {
  ADVENCED_SEARCH_PARAMS_DEFAULT,
  DEFAULT_PAGINATION,
  DETAIL_TAB_TYPE,
  ITEMS_PAGE_TYPE,
  NM_CD_FIELDS,
  VIEW_MODE,
} from "@/constants/";
import {
  getMultiEntityItemRelation,
  getMultiEntitySearch,
  getMultiEntitySearchInfo,
} from "@/api/prod/extendsApi";
import { removeUndefinedProperties } from "@/utils/format-data";
import { getUserInfor } from "@/constants/userInfor";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { BaseSearchPaneParamClass } from "@/types/common";
const paramsFilterComponentDefault: any = {
  type: null,
  subType: null,
  searchType: "prodItemNm",
  keyword: "",
  baseUuid: null,
  page: 1,
  size: 10,
};

const defaultResourceDetailMapped = {
  general: [] as any[],
  additional: [] as any[],
  startDate: "",
};

const resourceParamsFilterDefault: any = {
  componentUUID: null,
  itemCode: "",
  name: null,
  code: null,
  keyword: "",
  resourceType: PRODUCT_ITEM_NAME,
  page: 1,
  size: 7,
};

const paramsExtendsEntitySearchDefault: any = {
  itemCode: undefined,
  entityTypeCode: undefined,
  multiEntityCode: undefined,
  multiEntityName: undefined,
  onlyValidDtm: true,
  page: 1,
  size: 7,
};

const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};

const useResourceStore = defineStore("resourceStore", () => {
  const pagination = ref(cloneDeep(DEFAULT_PAGINATION));
  const listResource = ref<IResourceItem[] | any>([]);
  const listResourceSearch = ref<IResourceItem[] | any>([]);
  const totalResourceListItem = ref<number>(0);
  const listResourceUpdate = ref<IResourceItem[] | any>([]);
  const resourceListAdd = ref<any[]>([]);
  const paramsFilterComponent = ref<any>(
    cloneDeep(paramsFilterComponentDefault)
  );
  const resourceAdvencedParams = ref(cloneDeep(ADVENCED_SEARCH_PARAMS_DEFAULT));
  const resourceParamsFilter = ref(cloneDeep(resourceParamsFilterDefault));
  const resourceSelected = ref<any>(null);
  const resourceDetail = ref<any>(null);
  const isEdit = ref<boolean>(false);
  const isDuplicate = ref<boolean>(false);
  const showDetail = ref<boolean>(false);
  const showResourceDuplicate = ref<boolean>(false);
  const formatResource = ref<any>(null);
  const itemCodeList = ref<any[]>([]);
  const isInComponentMode = ref<boolean>(false);
  const showComponentSearch = ref<boolean>(false);
  const isViewOnly = ref<boolean>(false);
  const viewMode = ref(VIEW_MODE.GRID);
  const resourceItemCodeList = ref<any[]>([]);
  const resourceDetailMapped = ref<{
    general: any[];
    additional: any[];
    startDate: any;
  }>(cloneDeep(defaultResourceDetailMapped));
  const resourceCode = ref<any>(null);
  const resourceDetailCloneMapped = ref<any>(null);
  const componentSearch = ref<any>({
    items: [] as any[],
    pagination: cloneDeep(DEFAULT_PAGINATION),
  });
  const componentSelected = ref<any>(null);
  const resourceCodeCreate = ref<any>(null);
  const resourceEntityList = ref<any[]>([]);
  const entitySearchNmCd = ref<string>(NM_CD_FIELDS[0].value);
  const paramsSearchEntity = ref<any>(
    cloneDeep(paramsExtendsEntitySearchDefault)
  );
  const multiEntityTypes = ref<any[]>();
  const multiEntityInsertList = ref<any[]>([]);
  const multiEntityUpdateList = ref<any[]>([]);
  const itemEntitySelected = ref<any>();
  const entitySearch = ref<any>({
    items: [] as any[],
    total: 0,
  });
  const showEntitySearch = ref<boolean>(false);
  const resourceCreateEntityList = ref<any[]>([]);
  const entitySearchNmCdCreate = ref<string>(NM_CD_FIELDS[0].value);
  const paramsSearchEntityCreate = ref<any>(
    cloneDeep(paramsExtendsEntitySearchDefault)
  );
  const multiEntityCreateUpdateList = ref<any[]>([]);
  const itemEntityCreateSelected = ref<any>();
  const entitySearchCreate = ref<any>({
    items: [] as any[],
    total: 0,
  });
  const showEntitySearchCreate = ref<boolean>(false);
  const advencedSearchList = ref<any[]>([]);
  const isComponentGroupSearch = ref<boolean>(false);
  const paramsFilterComponentGroup = ref<BaseSearchPaneParamClass>(
    cloneDeep(PARAMS_DEFAULT_COMPONENT)
  );
  const isResetValue = ref<boolean>(false);

  const setAdvencedSearchList = async (type: any) => {
    const { data } = await getFormatCreateResourceApi(type);
    if (data) {
      advencedSearchList.value = [...data?.additional].map((row) => ({
        ...row,
        fieldName: row.attrUuid,
      }));
    }
  };

  const getMultiEntityTypes = async () => {
    const { data } = await getMultiEntitySearchInfo();
    multiEntityTypes.value = data;
  };

  const getResourceDetail = async (params: IParamsGetResourceDetail) => {
    const res = await getResourceDetailApi(params);
    return res;
  };

  const resetSelectedItem = () => {
    resourceSelected.value = null;
  };

  const resetResourceSearch = () => {
    pagination.value = cloneDeep(DEFAULT_PAGINATION);
    resourceDetail.value = null;
    isEdit.value = false;
    resourceDetailCloneMapped.value = null;
    resourceDetailMapped.value = cloneDeep(defaultResourceDetailMapped);
    resourceCode.value = null;
    componentSearch.value = {
      items: [] as any[],
      pagination: cloneDeep(DEFAULT_PAGINATION),
    };
    componentSelected.value = null;
    listResourceSearch.value = null;
    isInComponentMode.value = false;
    showComponentSearch.value = false;
    showResourceDuplicate.value = false;
    showEntitySearch.value = false;
    resetParamListEntitySearch(ITEMS_PAGE_TYPE.SEARCH);
    resetSelectedItem();
    resetPane();
    resetResourceFilter();
    viewMode.value = VIEW_MODE.GRID;
  };

  const resetResourceCreate = () => {
    resourceCodeCreate.value = null;
    showEntitySearchCreate.value = false;
    resourceItemCodeList.value = [];
    resetParamListEntitySearch(ITEMS_PAGE_TYPE.CREATE);
    resetCreateResource();
  };

  const getResourceList = async (params: any, resourceMode = true) => {
    try {
      const res = await getListResourceApi(params);
      if (resourceMode) {
        if (res?.data) {
          if (!listResourceSearch?.value) {
            listResourceSearch.value = {
              items: res.data?.list,
            };
          } else {
            listResourceSearch.value.items = res.data?.elements;
          }
          listResourceSearch.value.pagination = {
            ...listResourceSearch.value.pagination,
            currentPage: res?.data?.page,
            pageSize: res?.data?.size,
            totalSearchItems: res?.data?.totalElements || null,
            totalItems: res?.data?.totalElements || null,
            totalPages: res?.data?.totalPages || null,
          };
        }
      }
      return res;
    } catch (error) {
      throw error;
    }
  };
  const getResourceAdvancedList = async (params: any) => {
    try {
      const res = await getListResourceAdvancedApi(params);
      if (res?.data) {
        if (!listResourceSearch?.value) {
          listResourceSearch.value = {
            items: res.data?.elements,
          };
        } else {
          listResourceSearch.value.items = res.data?.elements;
        }
        listResourceSearch.value.pagination = {
          ...listResourceSearch.value.pagination,
          currentPage: res?.data?.page,
          pageSize: res?.data?.size,
          totalSearchItems: res?.data?.totalElements || null,
          totalItems: res?.data?.totalElements || null,
          totalPages: res?.data?.totalPages || null,
        };
      }
      return res;
    } catch (error) {
      throw error;
    }
  };

  const getDataResourceListView = async (params: any) => {
    try {
      const res = await getListResourceAdvancedDetailApi(params);
      if (res?.data) {
        if (!listResourceSearch?.value) {
          listResourceSearch.value = {
            items: res.data?.elements,
          };
        } else {
          listResourceSearch.value.items = res.data?.elements;
        }
        listResourceSearch.value.pagination = {
          ...listResourceSearch.value.pagination,
          currentPage: res?.data?.page,
          pageSize: res?.data?.size,
          totalSearchItems: res?.data?.totalElements || null,
          totalItems: res?.data?.totalElements || null,
          totalPages: res?.data?.totalPages || null,
        };
      }
      return res;
    } catch (error) {
      throw error;
    }
  };

  const getResourceListAdd = async (params: IParamsGetListResourceAdd) => {
    try {
      const res = await getListResourceAddApi(params);
      return res;
    } catch (error) {
      throw error;
    }
  };

  const resetFilter = () => {
    paramsFilterComponent.value = cloneDeep(paramsFilterComponentDefault);
    listResource.value = [];
    pagination.value = cloneDeep(DEFAULT_PAGINATION);
  };

  const resetResourceFilter = () => {
    resourceParamsFilter.value = cloneDeep(resourceParamsFilterDefault);
    resourceAdvencedParams.value = cloneDeep(ADVENCED_SEARCH_PARAMS_DEFAULT);
  };

  const resetPane = () => {
    showDetail.value = false;
    resourceSelected.value = null;
  };
  const resetCreateResource = () => {
    formatResource.value = null;
    isViewOnly.value = false;
  };

  const getFormatResourceCreate = async (itemCode: string) => {
    const userInfor = getUserInfor();
    try {
      const { data } = await getFormatCreateResourceApi(itemCode);
      if (data) {
        data.general = data.general
          .sort((after, before) => after.sortNo - before.sortNo)
          .map((item) => {
            if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
              return {
                ...item,
                attrVal: item.attrVal,
              };
            } else if (
              ["valid_end_dtm", "obj_name", "obj_code"].includes(item.colName)
            ) {
              return {
                ...item,
                attrVal: null,
              };
            } else if (item.colName === "chg_dept_name") {
              return {
                ...item,
                attrVal: userInfor.chgDeptName,
              };
            } else if (item.colName === "chg_user") {
              return {
                ...item,
                attrVal: userInfor.chgUser,
              };
            } else if (item.colName === "item_code") {
              return {
                ...item,
                editYn: "Y",
                fieldTypeCode: COLUMN_FIELD_TYPE.DL,
              };
            }
            return item;
          });
      }
      formatResource.value = {
        general: [
          ...data.general,
          ...data?.additional.filter(
            (item) => item.dispTab === DETAIL_TAB_TYPE.GENERAL
          ),
        ],
        additional: data?.additional
          .filter((item) => item.dispTab !== DETAIL_TAB_TYPE.GENERAL)
          .map((item: any) => ({
            ...item,
            attrVal:
              item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                ? JSON.parse(item?.attrVal)?.filter((value: any) =>
                    value.trim()
                  ) || []
                : item.attrVal,
          })),
      };
      resourceCreateEntityList.value = data?.entityItemInfos.map((item) => ({
        ...item,
        objRel: [],
      }));
    } catch (error) {
      throw error;
    }
  };

  const createNewResource = async (payload: any) => {
    try {
      const res = await createResourceApi(payload);
      return res;
    } catch (error) {
      throw error;
    }
  };

  const updateResource = async (payload: any) => {
    try {
      const res = await updateResourceApi(payload);
      return res;
    } catch (error) {
      throw error;
    }
  };

  const getListComponentSearch = async (
    params: RequestGetListComponentSearch
  ) => {
    try {
      const res = await getListComponentSearchApi(params);
      return res;
    } catch (err) {
      throw err;
    }
  };

  const resetComponentPage = () => {
    resetFilter();
    resourceListAdd.value = [];
  };

  const resetComponentCreate = () => {
    resetFilter();
  };
  const getListResourceEntity = async () => {
    if (resourceSelected?.value?.objUuid) {
      const res = await getMultiEntityItemRelation({
        objUuid: resourceSelected?.value?.objUuid,
        onlyValidDtm: showResourceDuplicate.value,
      });
      if (res?.data) {
        resourceEntityList.value = res?.data;
      }
    }
  };
  const getEntityList = async (pageType = ITEMS_PAGE_TYPE.SEARCH) => {
    clearListEntity(pageType);
    const params =
      pageType === ITEMS_PAGE_TYPE.SEARCH
        ? paramsSearchEntity.value
        : paramsSearchEntityCreate.value;
    removeUndefinedProperties(params);
    const { data } = await getMultiEntitySearch(params);
    const { elements, page, size, totalElements, totalPages } = data;
    if (pageType === ITEMS_PAGE_TYPE.SEARCH) {
      entitySearch.value.items = elements as any;
      entitySearch.value.pagination = {
        totalSearchItems: totalElements,
        currentPage: page,
        pageSize: size,
        totalItems: totalElements,
        totalPages: totalPages,
      };
    } else {
      entitySearchCreate.value.items = elements as any;
      entitySearchCreate.value.pagination = {
        totalSearchItems: totalElements,
        currentPage: page,
        pageSize: size,
        totalItems: totalElements,
        totalPages: totalPages,
      };
    }
  };
  const resetParamListEntitySearch = (pageType = ITEMS_PAGE_TYPE.SEARCH) => {
    if (pageType === ITEMS_PAGE_TYPE.SEARCH) {
      paramsSearchEntity.value = {
        ...paramsSearchEntity.value,
        entityTypeCode: undefined,
        multiEntityCode: undefined,
        multiEntityName: undefined,
        page: 1,
        size: 10,
      };
      entitySearchNmCd.value = NM_CD_FIELDS[0].value;
      entitySearch.value = {
        items: [],
        pagination: cloneDeep(defaultPagination),
      };
    } else {
      paramsSearchEntityCreate.value = {
        ...paramsSearchEntityCreate.value,
        entityTypeCode: undefined,
        multiEntityCode: undefined,
        multiEntityName: undefined,
        page: 1,
        size: 10,
      };
      entitySearchNmCdCreate.value = NM_CD_FIELDS[0].value;
      entitySearchCreate.value = {
        items: [],
        pagination: cloneDeep(defaultPagination),
      };
    }
  };
  const resetUpdateListEntity = (pageType = ITEMS_PAGE_TYPE.SEARCH) => {
    if (pageType === ITEMS_PAGE_TYPE.SEARCH) {
      multiEntityInsertList.value = [];
      multiEntityUpdateList.value = [];
    } else {
      multiEntityCreateUpdateList.value = [];
    }
  };
  const clearListEntity = (pageType) => {
    if (pageType === ITEMS_PAGE_TYPE.SEARCH) {
      entitySearch.value.items = [];
    } else {
      entitySearchCreate.value.items = [];
    }
  };
  return {
    pagination,
    resourceSelected,
    resourceDetail,
    isEdit,
    isDuplicate,
    showDetail,
    listResource,
    totalResourceListItem,
    resourceListAdd,
    paramsFilterComponent,
    resourceDetailMapped,
    showResourceDuplicate,
    formatResource,
    itemCodeList,
    resourceCode,
    isInComponentMode,
    showComponentSearch,
    resourceDetailCloneMapped,
    componentSearch,
    componentSelected,
    resourceCodeCreate,
    listResourceUpdate,
    listResourceSearch,
    resourceEntityList,
    entitySearch,
    paramsSearchEntity,
    multiEntityTypes,
    entitySearchNmCd,
    multiEntityInsertList,
    multiEntityUpdateList,
    showEntitySearch,
    itemEntitySelected,
    resourceCreateEntityList,
    entitySearchCreate,
    entitySearchNmCdCreate,
    paramsSearchEntityCreate,
    multiEntityCreateUpdateList,
    itemEntityCreateSelected,
    resourceParamsFilter,
    showEntitySearchCreate,
    resourceAdvencedParams,
    advencedSearchList,
    isComponentGroupSearch,
    paramsFilterComponentGroup,
    isResetValue,
    viewMode,
    isViewOnly,
    resourceItemCodeList,
    getResourceDetail,
    resetSelectedItem,
    resetResourceSearch,
    resetResourceCreate,
    resetFilter,
    resetResourceFilter,
    resetPane,
    getFormatResourceCreate,
    createNewResource,
    getResourceList,
    resetCreateResource,
    updateResource,
    getResourceListAdd,
    getListComponentSearch,
    resetComponentPage,
    resetComponentCreate,
    getEntityList,
    getMultiEntityTypes,
    resetParamListEntitySearch,
    getListResourceEntity,
    resetUpdateListEntity,
    getResourceAdvancedList,
    getDataResourceListView,
    setAdvencedSearchList,
  };
});

export default useResourceStore;
