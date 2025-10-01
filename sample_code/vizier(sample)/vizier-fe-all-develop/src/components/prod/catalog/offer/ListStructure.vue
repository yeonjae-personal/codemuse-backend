<template>
  <SearchPane
    ref="searchPaneRef"
    title="product_platform.component_search"
    :model-list="structureComponentAddList.items || []"
    :pagination="structureComponentAddList?.pagination"
    :pane-col="ColNumber.One"
    container-class="z-10 col-span-1"
    show-float-icon-right
    :item-height="62"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-click-float-right="closeListStructure"
    @on-change-page="handleChangePage"
  >
    <template #custom-form>
      <div class="mt-2">
        <div class="grid grid-cols-2 gap-2">
          <BaseSelectScroll
            v-model="paramsFilterComponent.componentType"
            :options="optionTypeFilter || []"
            :placeholder="$t('product_platform.Type')"
            default-item-select-all
            :height="48"
          />
          <BaseSelectScroll
            v-model="paramsFilterComponent.componentSubType"
            :options="optionsSubType || []"
            :placeholder="$t('product_platform.subType')"
            default-item-select-all
            :height="48"
          />
        </div>

        <div class="grid grid-cols-3 gap-x-2 mt-2">
          <BaseSelectScroll
            v-model="paramsFilterComponent.prodItemNm"
            :options="optionsSearchType || []"
            :height="48"
          />
          <div class="col-span-2">
            <BaseInputSearch
              v-model="paramsFilterComponent.keyword"
              label="search"
              density="comfortable"
              hide-details
              rounded="4"
              single-line
              variant="solo"
              @keyup.enter="handleEnterSearch"
            />
          </div>
        </div>
      </div>
    </template>
    <template #custom-search-item="{ item }">
      <cf-card-dropdown
        :key="`${componentKey}_${item.objCode}`"
        :item="item"
        :title="item.objName"
        :description="item.objCode"
        type-bg="linear"
        :border-color-action="setHoverColor(item.componentType)"
        :display-border-left="
          !checkComponentExist(item.objUUID)
            ? setHoverColor(item.componentType)
            : undefined
        "
        :search-text="paramsFilterComponent.keyword"
        :search-field="
          paramsFilterComponent.prodItemNm === COMPONENT_NAME_TYPE
            ? SearchBy.Name
            : SearchBy.Code
        "
        show-icon-status
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
        :expired="isExpiredTime(item?.validEndDtm)"
        :active="item.objUUID === currentProUuid"
        :draggable="!checkComponentExist(item.objUUID)"
        :disable="checkComponentExist(item.objUUID)"
        @on-click-card="handleShowComponentDetail($event, item)"
        @on-click-show-detail="collapseComponentItem($event, item)"
        @dragstart="handleDragStart($event, item)"
        @dragend="handleDragEnd"
      >
        <template #detail>
          <ProductGrid
            :data="item.inforItemDetail"
            :type="LARGE_ITEM_CODE.COMPONENT"
          />
        </template>
      </cf-card-dropdown>
    </template>
  </SearchPane>
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import { STRUCTURE_ITEM_SCREEN } from "@/constants/offer";
import {
  useStructureStore,
  useSnackbarStore,
  useProductsStore,
  useCreateStructureStore,
  useProductsCreateStore,
  useProductsDuplicateStore,
  useDuplicateStructureStore,
} from "@/store";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import {
  COMPONENT_CODE_TYPE,
  COMPONENT_NAME_TYPE,
} from "@/constants/component";
import { DETAIL_TAB_TYPE, OFFER_TYPE } from "@/constants/";
import { getComponentSearchType } from "@/api/prod/componentApi";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { TypeComponent } from "@/interfaces/prod/component";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { OfferTypes } from "@/enums/offer";
import { setHoverColor } from "@/utils/impact-analysis-utils";
import { isExpiredTime } from "@/utils/format-data";
import useDragUserPocket from "@/composables/useDragUserPocket";
import SearchPane from "../../shared/SearchPane.vue";
import { ColNumber, SearchBy } from "@/enums";

const props = defineProps({
  screen: {
    type: String,
    default: STRUCTURE_ITEM_SCREEN.OFFER,
  },
  isAdd: {
    type: Boolean,
    default: false,
  },
  isDuplicate: {
    type: Boolean,
    default: false,
  },
  type: {
    type: String,
    default: OFFER_TYPE.PRICEPLAN,
  },
});

const emit = defineEmits(["close-list-structure"]);

const route = useRoute();
const { t } = useI18n();

const productStore = useProductsStore();
const productCreateStore = useProductsCreateStore();
const structureStore = useStructureStore();
const createStructureStore = useCreateStructureStore();
const productsDuplicateStore = useProductsDuplicateStore();
const duplicateStructureStore = useDuplicateStructureStore();
const { getStructureComponentListAdd, getStructureComponentDetail } =
  useStructureStore();
const { showSnackbar } = useSnackbarStore();
const { handleDragUserPocket } = useDragUserPocket();

const componentKey = ref<number>(0);

const selectedStore = computed(() => {
  if (props.isAdd) {
    return createStructureStore;
  } else if (props.isDuplicate) {
    return duplicateStructureStore;
  }
  return structureStore;
});

const productStoreSelected = computed(() => {
  if (props.isAdd) {
    return productCreateStore;
  } else if (props.isDuplicate) {
    return productsDuplicateStore;
  }
  return productStore;
});

const {
  componentTypes,
  dragOfferType,
  isDragging,
  listAdd,
  offerUuid,
  listComponentDuplicate,
  structureData,
  listStructureItem,
  paramsFilterComponent,
  structureComponentAddList,
} = storeToRefs(selectedStore.value);
const { selectedProduct } = storeToRefs(productStoreSelected.value);

const searchPaneRef = ref<InstanceType<typeof SearchPane>>();
const currentProUuid = ref<any>(null);
const optionsSubType = ref([]);

const optionsSearchType = [
  {
    name: "Name",
    value: COMPONENT_NAME_TYPE,
    cmcdDetlId: COMPONENT_NAME_TYPE,
    cmcdDetlNm: "Name",
  },
  {
    name: "Code",
    value: COMPONENT_CODE_TYPE,
    cmcdDetlId: COMPONENT_CODE_TYPE,
    cmcdDetlNm: "Code",
  },
];

const checkComponentExist = computed(() => (componentUuid: string) => {
  return !![...listAdd.value, ...listComponentDuplicate.value].find(
    (item: any) => item.objUUID === componentUuid
  );
});

const optionTypeFilter = computed(() => {
  if (componentTypes.value) {
    let arrParent = [] as any;
    if (props.isAdd) {
      // handle case offer create
      arrParent = listStructureItem.value?.map((i) => i.mctgrItemCode);
    } else {
      // handle case offer search
      arrParent = structureData.value?.map((i) => i.mctgrItemCode);
    }
    return componentTypes.value?.filter((i) => arrParent?.includes(i.value));
  }
  return [];
});

const offerItemCode = computed(() => (path: string) => {
  const mapRouteToPath = {
    "/functions/product-platform/create-product/discount": OfferTypes.DISCOUNT,
    "/functions/product-platform/create-product/add-on": OfferTypes.ADD_ON,
    "/functions/product-platform/create-product/price-plan":
      OfferTypes.PRICE_PLAN,
  };
  return mapRouteToPath[path as string];
});

watch(
  () => paramsFilterComponent.value.componentType as TypeComponent,
  (value: TypeComponent) => {
    paramsFilterComponent.value.componentSubType = " ";
    getListSubType(value);
  }
);

onMounted(async () => {
  if (!componentTypes.value?.length) {
    const res = await getComponentSearchType();
    componentTypes.value = groupByMiddleItemCode(res.data);
  }
  if (
    selectedProduct.value &&
    !structureComponentAddList.value?.items?.length
  ) {
    await getListComponent();
  }
});

const handleShowComponentDetail = async (event, item: any) => {
  currentProUuid.value = item.objUUID;
  if (event.isShowDetail) {
    await getDetailComponentItem(item);
    item["isShowExpand"] = true;
  }
};

const collapseComponentItem = async (event, item: any) => {
  if (event) {
    if (!item["isShowExpand"]) {
      await getDetailComponentItem(item);
    }
    item["isShowExpand"] = !item["isShowExpand"];
  }
};

const getDetailComponentItem = async (item: any) => {
  try {
    const res = await getStructureComponentDetail({
      objUuid: item?.objUUID,
    });
    const general = [
      ...res.data?.general.map((i) => {
        if (i.colName === "item_code") {
          return {
            ...i,
            fieldTypeCode: COLUMN_FIELD_TYPE.DL,
          };
        }
        return i;
      }),
      ...res.data?.additional.filter(
        (i) => i.dispTab === DETAIL_TAB_TYPE.GENERAL
      ),
    ];

    const additional = res.data?.additional.filter(
      (i) => i.dispTab !== DETAIL_TAB_TYPE.GENERAL
    );

    item["inforItemDetail"] = {
      general: general,
      additional: additional,
    };
  } catch (error: any) {
    item["inforItemDetail"] = null as any;
    showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const handleDragStart = (event: DragEvent, item: any): void => {
  dragOfferType.value = item?.itemCode || "";
  isDragging.value = true;
  handleDragUserPocket(event, {
    userPocketType: LARGE_ITEM_CODE.COMPONENT,
    ...item,
  });
};

const handleDragEnd = () => {
  isDragging.value = false;
};

const handleSearch = (pageSize?: number, isClick = false, page = 1): void => {
  paramsFilterComponent.value.page = isClick ? 1 : page;
  getListComponent(pageSize);
};

const handleResetSearch = () => {
  paramsFilterComponent.value = {
    componentType: " ",
    componentSubType: " ",
    prodItemNm: COMPONENT_NAME_TYPE,
    keyword: "",
    page: 1,
    size: 7,
  };
  structureComponentAddList.value.items = [];
  structureComponentAddList.value.pagination = {};
  optionsSubType.value = [];
};

const handleChangePage = (page: number) => {
  paramsFilterComponent.value.page = page;
  getListComponent();
};

const getListComponent = async (pageSize?: number) => {
  try {
    if (!pageSize) {
      searchPaneRef.value?.calcTotalItem();
    }
    const params = {
      [paramsFilterComponent.value.prodItemNm]: paramsFilterComponent.value
        .keyword
        ? paramsFilterComponent.value.keyword
        : undefined,
      componentType: paramsFilterComponent.value.componentType || undefined,
      itemCode: paramsFilterComponent.value.componentSubType || undefined,
      offerUUID: props.isAdd
        ? offerUuid.value || undefined
        : props.isDuplicate && selectedProduct.value
          ? selectedProduct.value?.dplcTrgtUuid
          : selectedProduct.value && selectedProduct.value.objUuid
            ? selectedProduct.value.objUuid
            : undefined,
      page: paramsFilterComponent.value.page,
      size: pageSize ? pageSize : searchPaneRef.value?.totalItem || 7,
      offerItemCode: offerItemCode.value(route.path)
        ? offerItemCode.value(route.path)
        : undefined,
    };

    const res = await getStructureComponentListAdd(params);

    if (res?.data) {
      componentKey.value++;
      structureComponentAddList.value.items = res.data.elements;
      structureComponentAddList.value.pagination = {
        ...structureComponentAddList.value.pagination,
        totalSearchItems: res.data.totalElements,
        totalItems: res.data.totalElements,
        totalPages: res.data.totalPages,
        pageSize: res.data.size,
        currentPage: res.data.page,
      };
    }
  } catch (error: any) {
    showSnackbar(error?.errorMsg as string, "error");
  }
};

const closeListStructure = () => {
  emit("close-list-structure");
};

const concatValueMultiSingle = (item) => {
  if (item.singleAcceptCode) {
    return (item.multipleAcceptCode || [])?.concat(item.singleAcceptCode);
  }
  return item.multipleAcceptCode;
};

const getListSubType = async (type: any) => {
  if (!type) return;
  try {
    const { data } = await getListItemCodeApi({
      mItemCode: type,
    });

    let itemByType = null;
    if (props.isAdd) {
      // handle case offer create
      itemByType = listStructureItem.value?.find(
        (i) => i.mctgrItemCode === type
      );
    } else {
      // handle case offer search
      itemByType = structureData.value.find((i) => i.mctgrItemCode === type);
    }
    if (itemByType) {
      let concatMultiSingle = concatValueMultiSingle(itemByType);

      optionsSubType.value = data?.filter(
        (i) => concatMultiSingle?.some((t) => t === i.itemCode)
      );
    } else {
      optionsSubType.value = [];
    }
  } catch (error: any) {
    showSnackbar(error?.errorMsg as string, "error");
  }
};

const groupByMiddleItemCode = (data) => {
  const grouped = data.reduce((acc, item) => {
    let group = acc.find((group) => group.value === item.middleItemCode);
    if (!group) {
      group = {
        name: item.middleItemName,
        value: item.middleItemCode,
        cmcdDetlId: item.middleItemCode,
        cmcdDetlNm: item.middleItemName,
        sortNo: item.middleSortNo,
        children: [],
      };
      acc.push(group);
    }
    group.children.push({
      name: item.itemName,
      value: item.itemCode,
      cmcdDetlId: item.itemCode,
      cmcdDetlNm: item.itemName,
      sortNo: item.sortNo,
    });
    return acc;
  }, []);

  // Sort groups by sortNo
  grouped.sort((cur, next) => cur.sortNo - next.sortNo);

  // Sort children within each group by sortNo
  grouped.forEach((group) => {
    group.children.sort((cur, next) => cur.sortNo - next.sortNo);
  });

  return grouped;
};

const handleEnterSearch = () => {
  searchPaneRef.value?.handleSearch();
};
</script>
