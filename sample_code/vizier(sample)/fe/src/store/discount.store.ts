import cloneDeep from "lodash-es/cloneDeep";
import { TypeComponentCode } from "@/enums";
import { DEFAULT_PAGINATION } from "@/constants/";

const useDiscounStore = defineStore("discountStore", () => {
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
  const offerCode = ref<any>(null);
  const offerUuid = ref<any>(null);
  const isCreatedStructure = ref<boolean>(false);
  const isDragging = ref<boolean>(false);
  const dragOfferType = ref<any>(null);
  const showActionSave = ref<boolean>(false);
  const isDuplicate = ref<boolean>(false);
  const structureComponentAddList = ref<any>({
    items: [] as any[],
    pagination: cloneDeep(DEFAULT_PAGINATION),
  });
  const listAdd = ref<any[]>([]);
  const listUpdate = ref<any[]>([]);
  const listComponentDuplicate = ref<any[]>([]);
  const listComponentResource = ref<any[]>([]);
  const currentResourceActive = ref<any>(null);
  const paramsResourceFilter = ref<any>({
    objUuid: null as any,
    page: 1,
    size: 10,
    isExpired: true,
  });
  const totalResourceItem = ref<number>(0);

  const updateDataFromList = (updateItem: any) => {
    if (!updateItem || !updateItem.offerType) return;

    let fIndex = -1;

    switch (updateItem.offerType) {
      case TypeComponentCode.Characteristics:
        fIndex = structureData.value?.characteristics?.findIndex(
          (item: any) => item.objUUID === updateItem?.objUUID
        );
        if (fIndex !== -1) {
          structureData.value.characteristics.splice(fIndex, 1, updateItem);
        }
        break;
      case TypeComponentCode.Benefit:
        fIndex = structureData.value?.benefit?.findIndex(
          (item: any) => item.objUUID === updateItem?.objUUID
        );
        if (fIndex !== -1) {
          structureData.value.benefit.splice(fIndex, 1, updateItem);
        }
        break;
      case TypeComponentCode.Service:
        fIndex = structureData.value?.service?.findIndex(
          (item: any) => item.objUUID === updateItem?.objUUID
        );
        if (fIndex !== -1) {
          structureData.value.service.splice(fIndex, 1, updateItem);
        }
        break;
      case TypeComponentCode.Price:
        fIndex = structureData.value?.price?.findIndex(
          (item: any) => item.objUUID === updateItem?.objUUID
        );
        if (fIndex !== -1) {
          structureData.value.price.splice(fIndex, 1, updateItem);
        }
        break;
    }
  };

  const resetStructure = () => {
    showStructureDetail.value = false;
    showComponentDetail.value = false;
    showListStructure.value = false;
    selectedComponent.value = null;
    selectedComponentData.value = null;
    selectedStructureData.value = null;
    hiddenGeneralData.value = null;
    isCreated.value = false;
    structureData.value = null;
    offerCode.value = null;
    offerUuid.value = null;
    isCreatedStructure.value = false;
    structureComponentAddList.value = {
      items: [] as any[],
      pagination: cloneDeep(DEFAULT_PAGINATION),
    };
    listComponentResource.value = [];
    currentResourceActive.value = null;
    paramsResourceFilter.value = {
      objUuid: null as any,
      page: 1,
      size: 10,
      isExpired: true,
    };
    totalResourceItem.value = 0;
    resetDragDrop();
  };

  const resetDragDrop = () => {
    isDragging.value = false;
    dragOfferType.value = null;
    listAdd.value = [];
    listUpdate.value = [];
  };

  const addDataToList = (newItem: any) => {
    listAdd.value.push({
      ...newItem,
      trgtItemDetlTypeCdNm: "New",
    });
  };

  const updateDataFromListUpdate = (updateItem: any) => {
    const index = listUpdate.value.findIndex(
      (item: any) => item.objUUID === updateItem?.objUUID
    );
    if (index === -1) {
      listUpdate.value.push(updateItem);
    } else {
      listUpdate.value.splice(index, 1, updateItem);
    }
  };

  const updateAddData = (updateItem: any) => {
    const index = listAdd.value.findIndex(
      (item: any) => item.objUUID === updateItem?.objUUID
    );
    listAdd.value.splice(index, 1, updateItem);
  };

  const removeDataFromAdd = (removeItem: any) => {
    const index = listAdd.value.findIndex(
      (item: any) => item.objUUID === removeItem?.objUUID
    );
    listAdd.value.splice(index, 1);
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
    offerCode,
    offerUuid,
    isCreatedStructure,
    isDragging,
    dragOfferType,
    listAdd,
    listUpdate,
    listComponentDuplicate,
    listComponentResource,
    currentResourceActive,
    paramsResourceFilter,
    totalResourceItem,
    showActionSave,
    isDuplicate,
    resetStructure,
    updateDataFromList,
    resetDragDrop,
    addDataToList,
    updateDataFromListUpdate,
    removeDataFromAdd,
    updateAddData,
  };
});

export default useDiscounStore;
