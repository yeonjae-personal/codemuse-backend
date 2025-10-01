import {
  getListComponentSearchApi,
  getListOfferSearchApi,
  getComponentDetailApi,
  getComponentResourceImpactAnalystApi,
  getCreateFieldsInfoApi,
  getListResourceType,
  getListComponentSearchApiAdvance,
  getUiComponentAdvancedTable,
} from "@/api/prod/componentApi";
import {
  RequestGetListComponentSearch,
  RequestGetListOfferSearch,
} from "@/interfaces/prod/component";
import { removeUndefinedProperties } from "@/utils/format-data";
import cloneDeep from "lodash-es/cloneDeep";
import { RequiredYn, TypeComponentCode } from "@/enums";
import { FIELD_TYPE } from "@/enums/columnTypes";
import {
  DEFAULT_PAGINATION,
  ITEMS_PAGE_TYPE,
  NM_CD_FIELDS,
  SPACE,
  VIEW_MODE,
} from "@/constants/";
import { RESOURCE_PARAMS_FILTER_DEFAULT } from "@/constants/resource";
import {
  getMultiEntityItemRelation,
  getMultiEntitySearch,
  getMultiEntitySearchInfo,
} from "@/api/prod/extendsApi";
import {
  BaseItemSearchPaneDto,
  BaseSearchPaneParamClass,
} from "@/types/common";
import { IResourceItem } from "@/interfaces/prod/resource";
import { OFFER_TABS_VALUE } from "@/constants/offer";

export const paramsPagingDefault: any = {
  ...new BaseSearchPaneParamClass(undefined, SPACE),
  baseUuid: null,
  offerUuid: null,
  general: null as any,
  additional: null as any,
};

export const paramsFilterOfferSearchDefault = {
  ...new BaseSearchPaneParamClass(),
};

export const paramsFilterResourceDefault = {
  objUuid: undefined,
  page: 1,
  size: 8,
  onlyValidDtm: false,
};

const paramsExtendsEntitySearchDefault: any = {
  itemCode: "LB",
  entityTypeCode: undefined,
  multiEntityCode: undefined,
  multiEntityName: undefined,
  onlyValidDtm: true,
  page: 1,
  size: 10,
};

const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};

export type ResourceTypeCreate = {
  resourceUUID: string;
  resourceCode: string;
  startDate: string;
  endDate: string;
  itemCode: string;
  isShowInfo?: boolean;
  inforItemDetail?: {
    generalDetails: any;
    additionalParams: any[];
  };
};

export type AttributeAdditionalType = {
  attrEngName: string;
  attrLocalName: string;
  attrUUID: string;
  attrValue: string;
  commGroupCode: string;
  fieldTypeCode: FIELD_TYPE;
  lov: [
    {
      title: string;
      value: string;
    },
  ];
};

const defaultValuesCreate = {
  type: TypeComponentCode.Service as TypeComponentCode,
  subType: "",
  general: [] as any,
  additional: [] as any[],
  resource: [] as Array<ResourceTypeCreate>,
  comments: {} as any,
  multiEntity: [] as any[],
};

const useComponentStore = defineStore("componentStore", () => {
  const offersType = ref<any[]>([]);
  const optionsType = ref<any[]>([]);
  const optionsSubType = ref<any[]>([]);
  const isApplied = ref<boolean>(false);
  const openSearchDetail = ref<boolean>(false);
  const showOfferSearch = ref<boolean>(false);
  const isInOfferMode = ref<boolean>(false);
  const sltItem = ref<any>(null);
  const sltItemData = ref<any[]>([]);
  const isViewMode = ref<boolean>(false);
  const showDetail = ref<boolean>(false);
  const advencedSearchList = ref<any[]>([]);
  const isDuplicate = ref<boolean>(false);
  const showResourceImpactAnalyst = ref<boolean>(false);
  const paramsFilterComponent = ref<any>(cloneDeep(paramsPagingDefault));
  const paramsFilterOffer = ref<any>(cloneDeep(paramsFilterOfferSearchDefault));
  const paramsResourceFilter = ref(paramsFilterResourceDefault);
  const viewMode = ref(VIEW_MODE.GRID);
  const resourceListAdd = ref<any[]>([]);
  const listResource = ref<IResourceItem[] | any>([]);
  const listResourceUpdate = ref<IResourceItem[] | any>([]);
  const totalResourceListItem = ref<number>(0);
  const currentTabCreate = ref(OFFER_TABS_VALUE.GENERAL);
  const currentTabEdit = ref(OFFER_TABS_VALUE.GENERAL);

  const resourcePagination = ref<any>(
    cloneDeep({
      ...DEFAULT_PAGINATION,
      totalPages: 5,
    })
  );
  const componentSearch = ref<any>({
    items: [] as any[],
    pagination: cloneDeep(DEFAULT_PAGINATION),
  });
  const offerSearch = ref<any>({
    items: [] as any[],
    pagination: cloneDeep(DEFAULT_PAGINATION),
  });
  const actionType = ref<string>();
  const componentSelected = ref<any>({
    itemDetlTypeCd: "",
    itemCd: "",
    itemTypeNm: "",
  });
  const componentDetail = ref<any>({
    general: [],
    additional: [],
    resources: [],
  });
  const showResourceAdd = ref<boolean>(false);
  const listAdd = ref<any[]>([]);
  const isEdit = ref<boolean>(false);
  const fieldsInfo = ref<any>({
    additional: [] as any[],
    resources: [] as any[],
  });
  const componentCreateData = ref<any>(cloneDeep(defaultValuesCreate));
  const componentEditData = ref<any>({
    general: {} as any,
    additional: [] as any[],
    resource: [] as any[],
  });
  const componentResourceAnalystImpact = ref<any>({
    resource: null as any,
    componentList: [] as any[],
  });
  const componentDetailMapped = ref<any>({
    generalDetails: [] as any[],
    additionalParams: [] as any[],
    overView: "",
  });
  const code = ref<any>(null);
  const showEntitySearch = ref<boolean>(false);
  const entitySearch = ref<any>({
    items: [] as any[],
    total: 0,
  });
  const showEntitySearchCreate = ref<boolean>(false);
  const entitySearchCreate = ref<any>({
    items: [] as any[],
    total: 0,
  });
  const selectedOffer = ref<any>(null);
  const componentCreated = ref<any>({
    itemDetlTypeCd: "",
    itemCd: "",
    itemTypeNm: "",
  });
  const showResourceAddCreate = ref<boolean>(false);
  const listResourceAdd = ref<any>({
    items: [] as any[],
    total: 0,
    totalSearch: 0,
  });
  const listResourceAddCreate = ref<any>({
    items: [] as any[],
    total: 0,
    totalSearch: 0,
  });
  const resourceParamsFilter = ref<any>(
    cloneDeep(RESOURCE_PARAMS_FILTER_DEFAULT)
  );
  const resourceParamsFilterCreate = ref<any>(
    cloneDeep(RESOURCE_PARAMS_FILTER_DEFAULT)
  );
  const componentEntityList = ref<any[]>([]);
  const entitySearchNmCd = ref<string>(NM_CD_FIELDS[0].value);
  const paramsSearchEntity = ref<any>(
    cloneDeep(paramsExtendsEntitySearchDefault)
  );
  const multiEntityTypes = ref<any[]>();
  const itemEntitySelected = ref<any>();
  const componentCreateEntityList = ref<any[]>([]);
  const entitySearchNmCdCreate = ref<string>(NM_CD_FIELDS[0].value);
  const paramsSearchEntityCreate = ref<any>(
    cloneDeep(paramsExtendsEntitySearchDefault)
  );
  const multiEntityCreateUpdateList = ref<any[]>([]);
  const resourceTypes = ref<any[]>([]);
  const itemEntityCreateSelected = ref<any>();
  const itemResourceDragged = ref<any>();

  const getMultiEntityTypes = async () => {
    const { data } = await getMultiEntitySearchInfo();
    multiEntityTypes.value = data;
  };

  const resetComponentCreateData = () => {
    componentCreateData.value = cloneDeep(defaultValuesCreate);
    componentCreateData.value.subType = " ";
    code.value = null;
    showResourceAdd.value = false;
  };

  const setAdvencedSearchList = async (type: any) => {
    const { data } = await getCreateFieldsInfoApi({
      itemCode: type,
    });
    if (data) {
      advencedSearchList.value = [
        ...data?.additional.filter(
          (item) => item.advSearchYn === RequiredYn.Yes
        ),
      ].map((row) => ({ ...row, fieldName: row.attrUuid }));
    }
  };

  const setSelectedItem = (item: any) => {
    sltItem.value = item;
  };
  const getListResourceTypeByItemCode = async (itemCode: any) => {
    const result = await getListResourceType({
      itemCode,
    });
    resourceTypes.value = result?.data || [];
  };
  const setSelectedItemData = (structureData: any[]) => {
    sltItemData.value = structureData;
  };
  const setShowDetail = (isShow: boolean) => {
    showDetail.value = isShow;
  };

  const resetSelectedItem = () => {
    sltItem.value = {} as any;
    sltItemData.value = [] as any[];
    showDetail.value = false;
    showResourceAdd.value = false;
  };
  const resetPaneWhenSelectItem = () => {
    showDetail.value = false;
    isDuplicate.value = false;
    showResourceAdd.value = false;
    componentSelected.value = null;
  };
  const resetResourceAdd = () => {
    showResourceAdd.value = false;
  };
  const resetAdvancedSearchParams = () => {
    advencedSearchList.value = [];
  };
  const resetParamsFilterComponent = () => {
    isDuplicate.value = false;
    showDetail.value = false;
    paramsFilterComponent.value = cloneDeep(paramsPagingDefault);
    componentSearch.value = {
      items: [],
      pagination: cloneDeep(DEFAULT_PAGINATION),
    };
  };
  const resetComponentSearch = () => {
    componentSearch.value.items = [] as any[];
    componentSearch.value.pagination = cloneDeep(DEFAULT_PAGINATION);
  };
  const resetParamsFilterComponentSearch = () => {
    paramsFilterComponent.value = cloneDeep(paramsPagingDefault);
    componentSearch.value.items = [] as any[];
    resetPaneWhenSelectItem();
  };
  const resetParamFilterOfferSearch = () => {
    selectedOffer.value = null;
    paramsFilterOffer.value = cloneDeep(paramsFilterOfferSearchDefault);
    offerSearch.value.items = [] as any[];
  };
  const setParamsFilterOffer = (params: any) => {
    paramsFilterOffer.value = params;
  };
  const resetParamsFilterOffer = () => {
    selectedOffer.value = null;
    paramsFilterOffer.value = cloneDeep(paramsFilterOfferSearchDefault);
    offerSearch.value = {
      items: [] as any[],
      pagination: cloneDeep(DEFAULT_PAGINATION),
    };
  };

  const getListComponentSearch = async (
    params: RequestGetListComponentSearch
  ) => {
    try {
      const res = await getListComponentSearchApi(params);
      if (!code.value && res?.data) {
        componentSearch.value.items = res.data?.elements.map((com) => {
          const dto = new BaseItemSearchPaneDto(
            com.objUuid,
            com.objName,
            com.objCode,
            com.itemCode,
            com.validEndDtm,
            com.validStartDtm,
            true,
            true,
            false,
            com.mctgrItemCode
          );
          return { ...com, ...dto };
        }) as any;
        componentSearch.value.pagination = {
          ...componentSearch.value.pagination,
          currentPage: res.data?.page,
          pageSize: res.data?.size,
          totalSearchItems: res.data?.totalElements,
          totalItems: res.data?.totalElements,
          totalPages: res.data?.totalPages,
        };
      }
      return res;
    } catch (error) {
      throw error;
    }
  };
  const getListComponentSearchAdvance = async (params: any) => {
    try {
      const res = await getListComponentSearchApiAdvance(params);
      if (!code.value && res?.data) {
        componentSearch.value.items = res?.data?.elements.map((com) => {
          const dto = new BaseItemSearchPaneDto(
            com.objUuid,
            com.objName,
            com.objCode,
            com.itemCode,
            com.validEndDtm,
            com.validStartDtm,
            true,
            true,
            false,
            com.mctgrItemCode
          );
          return { ...com, ...dto };
        }) as any;
        componentSearch.value.pagination = {
          currentPage: res.data?.page,
          pageSize: res.data?.size,
          totalSearchItems: res.data?.totalElements,
          totalItems: res.data?.totalElements,
          totalPages: res.data?.totalPages,
        };
      }
      return res;
    } catch (error) {
      throw error;
    }
  };

  const getDataComponentListView = async (params: any) => {
    try {
      const res = await getUiComponentAdvancedTable(params);
      if (!code.value && res?.data) {
        componentSearch.value.items = res.data?.elements;
        componentSearch.value.pagination = {
          currentPage: res.data?.page,
          pageSize: res.data?.size,
          totalSearchItems: res.data?.totalElements,
          totalItems: res.data?.totalElements,
          totalPages: res.data?.totalPages,
        };
      }
      return res;
    } catch (error) {
      throw error;
    }
  };

  const getComponentDetail = async (params: any) => {
    try {
      const res = await getComponentDetailApi(params);
      return res;
    } catch (error) {
      console.error(error);
      throw error;
    }
  };

  const getComponentResourceImpactAnalyst = async (params: any) => {
    try {
      if (!params) {
        return;
      }
      const { data } = await getComponentResourceImpactAnalystApi(params);
      componentResourceAnalystImpact.value.componentList = data;
    } catch (error) {
      console.error(error);
    }
  };

  const getListOfferSearch = async (params: RequestGetListOfferSearch) => {
    try {
      removeUndefinedProperties(params);
      const res = await getListOfferSearchApi(params);
      offerSearch.value.items = res.data.elements.map((com) => {
        const dto = new BaseItemSearchPaneDto(
          com.objUuid,
          com.objName,
          com.objCode,
          com.itemCode,
          com.validEndDtm,
          com.validStartDtm
        );
        return { ...com, ...dto, expand: false };
      }) as any;
      offerSearch.value.pagination = {
        ...offerSearch.value.pagination,
        currentPage: res.data?.page,
        pageSize: res.data?.size,
        totalSearchItems: res.data?.totalElements,
        totalItems: res.data?.totalElements,
        totalPages: res.data?.totalPages,
      };
    } catch (error) {
      throw error;
    }
  };

  const getListComponentEntity = async (onlyValidDtm = false) => {
    const res = await getMultiEntityItemRelation({
      objUuid: componentSelected.value.objUuid,
      onlyValidDtm,
    });
    if (res?.data) {
      componentEntityList.value = res?.data;
    }
  };

  const setListAdd = (newList: any[]) => {
    listAdd.value = newList;
  };

  const addComponentToList = (item: any) => {
    listAdd.value.push(item);
  };

  const setIsEdit = (newVal: boolean) => {
    isEdit.value = newVal;
  };

  const resetComponentPage = () => {
    resetComponentSearch();
    resetPaneWhenSelectItem();
    resetParamFilterOfferSearch();
    resetParamsFilterComponent();
    resetParamsFilterComponentSearch();
    resetParamsFilterOffer();
    resetResourceAdd();
    resetSelectedItem();
    resetParamListEntitySearch(ITEMS_PAGE_TYPE.SEARCH);
    showEntitySearch.value = false;
    isInOfferMode.value = false;
    listAdd.value = [];
    listResourceAdd.value = {
      items: [] as any[],
      total: 0,
      totalSearch: 0,
    };
    viewMode.value = VIEW_MODE.GRID;
    optionsSubType.value = [];
    showOfferSearch.value = false;
  };

  const resetComponentCreate = () => {
    resetComponentCreateData();
    resetParamListEntitySearch(ITEMS_PAGE_TYPE.CREATE);
    showEntitySearchCreate.value = false;
    componentCreated.value = {
      itemDetlTypeCd: "",
      itemCd: "",
      itemTypeNm: "",
    };
    showEntitySearchCreate.value = false;
    entitySearchCreate.value = {
      items: [] as any[],
      total: 0,
    };
    showResourceAddCreate.value = false;
    showResourceImpactAnalyst.value = false;
    listResourceAddCreate.value = {
      items: [] as any[],
      total: 0,
      totalSearch: 0,
    };
    isViewMode.value = false;
    viewMode.value = VIEW_MODE.GRID;
    currentTabCreate.value = OFFER_TABS_VALUE.GENERAL;
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
      entitySearch.value.items = cloneDeep(elements) as any;
      entitySearch.value.pagination = {
        totalSearchItems: totalElements,
        currentPage: page,
        pageSize: size,
        totalItems: totalElements,
        totalPages: totalPages,
      };
    } else {
      entitySearchCreate.value.items = cloneDeep(elements) as any;
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
  const clearListEntity = (pageType) => {
    if (pageType === ITEMS_PAGE_TYPE.SEARCH) {
      entitySearch.value.items = [];
    } else {
      entitySearchCreate.value.items = [];
    }
  };
  return {
    isInOfferMode,
    sltItem,
    sltItemData,
    isViewMode,
    showDetail,
    isDuplicate,
    showResourceImpactAnalyst,
    paramsFilterComponent,
    paramsFilterOffer,
    resourcePagination,
    componentSearch,
    offerSearch,
    componentSelected,
    componentDetail,
    showResourceAdd,
    listAdd,
    isEdit,
    fieldsInfo,
    componentCreateData,
    componentEditData,
    componentResourceAnalystImpact,
    componentDetailMapped,
    code,
    showEntitySearch,
    entitySearch,
    showEntitySearchCreate,
    entitySearchCreate,
    selectedOffer,
    componentCreated,
    showResourceAddCreate,
    listResourceAdd,
    listResourceAddCreate,
    resourceParamsFilter,
    resourceParamsFilterCreate,
    componentEntityList,
    paramsSearchEntity,
    multiEntityTypes,
    entitySearchNmCd,
    itemEntitySelected,
    componentCreateEntityList,
    entitySearchNmCdCreate,
    paramsSearchEntityCreate,
    multiEntityCreateUpdateList,
    resourceTypes,
    itemEntityCreateSelected,
    actionType,
    advencedSearchList,
    itemResourceDragged,
    viewMode,
    optionsType,
    optionsSubType,
    isApplied,
    showOfferSearch,
    openSearchDetail,
    resourceListAdd,
    listResource,
    listResourceUpdate,
    totalResourceListItem,
    currentTabCreate,
    currentTabEdit,
    offersType,
    paramsResourceFilter,
    resetComponentCreateData,
    setSelectedItem,
    setSelectedItemData,
    getListResourceTypeByItemCode,
    setShowDetail,
    resetSelectedItem,
    resetPaneWhenSelectItem,
    resetResourceAdd,
    resetParamsFilterComponent,
    resetComponentSearch,
    resetParamsFilterComponentSearch,
    resetParamFilterOfferSearch,
    setParamsFilterOffer,
    resetParamsFilterOffer,
    setAdvencedSearchList,
    getListComponentSearch,
    getComponentDetail,
    getComponentResourceImpactAnalyst,
    getListOfferSearch,
    getListComponentEntity,
    setListAdd,
    addComponentToList,
    setIsEdit,
    resetComponentPage,
    resetComponentCreate,
    getEntityList,
    getMultiEntityTypes,
    resetParamListEntitySearch,
    resetAdvancedSearchParams,
    getListComponentSearchAdvance,
    getDataComponentListView,
  };
});

export default useComponentStore;
