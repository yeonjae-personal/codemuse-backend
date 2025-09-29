import { ParamsStructure } from "@/interfaces/prod/structure";
import {
  getStructureComponentListAddApi,
  getStructureApi,
  getStructureComponentDetailApi,
} from "@/api/prod/structureApi";
import { IParamsGetListResourceAdd } from "@/interfaces/prod/resource";
import { getListResourceApi } from "@/api/prod/resourceApi";
import cloneDeep from "lodash-es/cloneDeep";
import { IQueryStructureComponentAddList } from "@/interfaces/prod/offer";
import { DEFAULT_PAGINATION, NM_CD_FIELDS } from "@/constants/";
import {
  getMultiEntityItemRelation,
  getMultiEntitySearchInfo,
} from "@/api/prod/extendsApi";
import { COMPONENT_NAME_TYPE } from "@/constants/component";

const paramsExtendsEntitySearchDefault: any = {
  itemCode: undefined,
  entityTypeCode: undefined,
  multiEntityCode: undefined,
  multiEntityName: undefined,
  page: 1,
  size: 10,
};

const paramsFilterComponentDefault = {
  componentType: " ",
  componentSubType: " ",
  prodItemNm: COMPONENT_NAME_TYPE,
  keyword: "",
  page: 1,
  size: 7,
};

const storeObj = () => {
  const showStructureDetail = ref<boolean>(false);
  const showComponentDetail = ref<boolean>(false);
  const showListStructure = ref<boolean>(false);
  const selectedComponent = ref<any>(null);
  const selectedComponentData = ref<any>(null);
  const selectedStructureData = ref<any>(null);
  const hiddenGeneralData = ref<any>(null);
  const structureData = ref<any>(null);
  const isCreated = ref<boolean>(false);
  const isEditProduct = ref<boolean>(false);
  const isDuplicate = ref<boolean>(false);
  const offerCode = ref<any>(null);
  const offerUuid = ref<any>(null);
  const isCreatedStructure = ref<boolean>(false);
  const showActionSave = ref<boolean>(false);
  const structureComponentAddList = ref<any>({
    items: [] as any[],
    pagination: cloneDeep(DEFAULT_PAGINATION),
  });
  const structureListAdd = ref<any[]>([]);
  const listComponentResource = ref<any[]>([]);
  const currentResourceActive = ref<any>(null);
  const paramsResourceFilter = ref<any>({
    componentUUID: null as any,
    page: 1,
    size: 10,
  });
  const totalResourceItem = ref<number>(0);
  const onlyView = ref<boolean>(false);
  const isDragging = ref<boolean>(false);
  const isPendingFinish = ref<boolean>(false);
  const dragOfferType = ref<any>(null);
  const listAdd = ref<any[]>([]);
  const listUpdate = ref<any[]>([]);
  const listComponentDuplicate = ref<any[]>([]);
  const componentEntityList = ref<any[]>([]);
  const entitySearchNmCd = ref<String>(NM_CD_FIELDS[0].value);
  const paramsSearchEntity = ref<any>(
    cloneDeep(paramsExtendsEntitySearchDefault)
  );
  const multiEntityTypes = ref<any[]>();
  const listStructureItem = ref<any[]>();
  const itemEntitySelected = ref<any>();
  const resourceTypes = ref<any[]>();
  const componentTypes = ref<any[]>();
  const paramsFilterComponent = ref({ ...paramsFilterComponentDefault });
  const totalResourceListItem = ref<number>(0);

  const getStructure = async (params: ParamsStructure) => {
    try {
      const res = await getStructureApi(params);
      return res;
    } catch (err) {
      throw err;
    }
  };

  const getStructureComponentDetail = async (params: any) => {
    try {
      const res = await getStructureComponentDetailApi(params);
      return res;
    } catch (err) {
      throw err;
    }
  };
  const getStructureComponentListAdd = async (
    params: IQueryStructureComponentAddList | any
  ) => {
    try {
      const res = await getStructureComponentListAddApi(params);
      return res;
    } catch (err) {
      throw err;
    }
  };

  const updateDataFromList = (updateItem: any) => {
    if (!updateItem || !updateItem.offerType) return;
    let fIndex = -1;
    fIndex = structureData.value
      ?.find((item) => item.mctgrItemCode === updateItem.offerType)
      ?.componentList?.findIndex(
        (item: any) => item.objUuid === updateItem?.objUuid
      );
    if (fIndex !== -1) {
      structureData.value
        ?.find((item) => item.mctgrItemCode === updateItem.offerType)
        ?.componentList.splice(fIndex, 1, updateItem);
    }
  };

  const getComponentResourceList = async (
    params: IParamsGetListResourceAdd
  ) => {
    try {
      const res = await getListResourceApi(params);
      return res;
    } catch (error) {
      throw error;
    }
  };

  const resetStructure = () => {
    showActionSave.value = false;
    showStructureDetail.value = false;
    showComponentDetail.value = false;
    showListStructure.value = false;
    selectedComponent.value = null;
    selectedComponentData.value = null;
    selectedStructureData.value = null;
    hiddenGeneralData.value = null;
    isCreated.value = false;
    structureListAdd.value = [];
    structureData.value = null;
    offerCode.value = null;
    offerUuid.value = null;
    isCreatedStructure.value = false;
    isDuplicate.value = false;
    structureComponentAddList.value = {
      items: [] as any[],
      pagination: cloneDeep(DEFAULT_PAGINATION),
    };
    listComponentResource.value = [];
    listStructureItem.value = [];
    currentResourceActive.value = null;
    paramsResourceFilter.value = {
      componentUUID: null as any,
      page: 1,
      size: 10,
    };
    totalResourceItem.value = 0;
    componentTypes.value = [];
    resetComponentSearch();
    resetDragDrop();
  };

  const resetDragDrop = () => {
    isDragging.value = false;
    dragOfferType.value = "";
    listAdd.value = [];
    listUpdate.value = [];
  };

  const addDataToList = (newItem: any) => {
    listAdd.value.push({
      ...newItem,
      objUuid: newItem.objUUID,
      trgtItemDetlTypeCdNm: "New",
    });
  };

  const updateDataFromListUpdate = (updateItem: any) => {
    const index = listUpdate.value.findIndex(
      (item: any) => item.objUuid === updateItem?.objUuid
    );
    if (index === -1) {
      listUpdate.value.push(updateItem);
    } else {
      listUpdate.value.splice(index, 1, updateItem);
    }
  };

  const updateAddData = (updateItem: any) => {
    const index = listAdd.value.findIndex(
      (item: any) => item.objUuid === updateItem?.objUuid
    );
    listAdd.value.splice(index, 1, updateItem);
  };

  const removeDataFromAdd = (removeItem: any) => {
    const index = listAdd.value.findIndex(
      (item: any) => item.objUuid === removeItem?.objUuid
    );
    listAdd.value.splice(index, 1);
  };

  const getMultiEntityTypes = async () => {
    const { data } = await getMultiEntitySearchInfo();
    multiEntityTypes.value = data;
  };

  const getListComponentEntity = async () => {
    componentEntityList.value = [];
    const res = await getMultiEntityItemRelation({
      objUuid: selectedComponent.value.objUuid,
    });
    if (res?.data) {
      componentEntityList.value = res?.data;
    }
  };
  const resetComponentSearch = async () => {
    paramsFilterComponent.value = { ...paramsFilterComponentDefault };
    structureComponentAddList.value.items = [];
    structureComponentAddList.value.pagination = {};
  };
  return {
    showStructureDetail,
    showComponentDetail,
    showListStructure,
    selectedComponent,
    selectedComponentData,
    selectedStructureData,
    hiddenGeneralData,
    structureData,
    isCreated,
    isEditProduct,
    structureComponentAddList,
    structureListAdd,
    offerCode,
    offerUuid,
    isCreatedStructure,
    listComponentResource,
    currentResourceActive,
    paramsResourceFilter,
    totalResourceItem,
    isDragging,
    dragOfferType,
    listAdd,
    listUpdate,
    listComponentDuplicate,
    multiEntityTypes,
    componentEntityList,
    entitySearchNmCd,
    paramsSearchEntity,
    itemEntitySelected,
    showActionSave,
    isDuplicate,
    listStructureItem,
    onlyView,
    resourceTypes,
    isPendingFinish,
    paramsFilterComponent,
    totalResourceListItem,
    componentTypes,
    resetStructure,
    getStructureComponentListAdd,
    getStructure,
    updateDataFromList,
    getStructureComponentDetail,
    getComponentResourceList,
    resetDragDrop,
    addDataToList,
    updateDataFromListUpdate,
    removeDataFromAdd,
    updateAddData,
    getMultiEntityTypes,
    getListComponentEntity,
    resetComponentSearch,
  };
};
const useStructureStore = defineStore("structureStore", storeObj);
const useDuplicateStructureStore = defineStore(
  "duplicateStructureStore",
  storeObj
);
const useCreateStructureStore = defineStore("createStructureStore", storeObj);

export {
  useStructureStore,
  useCreateStructureStore,
  useDuplicateStructureStore,
};
