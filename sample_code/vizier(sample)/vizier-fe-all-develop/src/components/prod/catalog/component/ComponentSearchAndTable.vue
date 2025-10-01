<template>
  <SearchPane
    v-if="viewMode === VIEW_MODE.GRID"
    :key="componentKey"
    ref="searchPane"
    title="product_platform.component_search"
    :title-append="titleAppend"
    :container-class="containerClass"
    :model-list="componentSearch.items"
    :pagination="componentSearch.pagination"
    :pane-type="SearchPaneType.Component"
    :pane-col="showOfferSearch ? ColNumber.One : ColNumber.Two"
    :model-param="localParamsFilterComponent"
    :option-types="optionsType"
    :option-sub-types="optionsSubType"
    :selected-item="componentSelected"
    :type-select-require="checkSelectRequire()"
    :show-float-icon-left="showOfferSearch"
    :show-float-icon-right="!showOfferSearch"
    :search-item-actions="listActions"
    :open-popup="openPopup"
    :popup-content="popupContent"
    :item-height="62"
    @on-click-item="selectComponent"
    @on-search="handleSearch"
    @on-reset="handleResetSearch(true)"
    @on-click-float-left="onClose"
    @on-click-float-right="onOpen"
    @close-popup="closePopupSave"
    @submit-popup="handleSubmitDetails"
    @on-change-page="handleChangePage"
    @on-drag-start="handleDragStart"
  >
    <template #search-button-append>
      <SearchDetailButton
        v-if="!isInOfferMode"
        class="bg-white ml-2"
        :is-active="isApplied"
        :disabled="isDisableAdvancedButton"
        @click="handleSearchDetail"
      />
      <div
        v-if="!showOfferSearch"
        class="!h-10 !w-[169px] bg-primary-lightest rounded-lg w-auto p-4 flex items-center justify-center gap-1 ms-2"
        @click="onSearchOffer"
      >
        <SearchIcon fill="#BA1642" />
        <span
          class="text-text-primary font-base font-size-base font-medium cursor-pointer"
          >{{
            !isInOfferMode
              ? $t("product_platform.search_with_offer")
              : $t("product_platform.without_offer")
          }}
        </span>
      </div>
      <div
        v-if="componentSearch.items?.length && !showOfferSearch"
        class="h-10 ml-2 py-4 flex items-center justify-center"
      >
        <SwitchViewTable
          v-model="viewMode"
          class="ms-auto"
          @update:model-value="handleChangeViewMode"
        />
      </div>
    </template>
  </SearchPane>
  <TablePane
    v-if="viewMode === VIEW_MODE.LIST"
    ref="tablePane"
    :pane-type="SearchPaneType.Component"
    :pane-col="ColNumber.Four"
    :model-list="componentSearch.items"
    :pagination="componentSearch.pagination"
    :model-param="localParamsFilterComponent"
    :view-mode="viewMode"
    :is-loading="isLoading"
    :downloading="downloading"
    :option-types="optionsType"
    :option-sub-types="optionsSubType"
    sub-type-select-require
    @update:view-mode="handleChangeViewMode"
    @on-search="handleSearch"
    @on-reset="handleResetSearch(true)"
    @on-change-page="handleChangePage"
    @on-download="handleDownload"
  />
  <AdvancedSearch
    v-if="openSearchDetail"
    v-model="openSearchDetail"
    :type="localParamsFilterComponent.type"
    :model-list="advencedSearchList"
    class-custom="search-detail-component"
    @on-close="closePopupSearchDetail"
    @on-submit="applyPopupSearchDetail"
    @on-reset="handleResetSearchDetail"
  />
</template>
<script setup lang="ts">
import { EXPORT_COMPONENT_EXCEL_PATH } from "@/api/prod/path";
import DuplicateIcon from "@/components/prod/icons/DuplicateIcon.vue";
import { useDownloadFile } from "@/composables/useDownloadFIle";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { COMPONENT_DETAIL_CATEFORY } from "@/constants/component";
import { SPACE, VIEW_MODE } from "@/constants/index";
import { ColNumber, LargeItemCode, SearchBy, SearchPaneType } from "@/enums";
import { useComponentStore, useDragStore, useSnackbarStore } from "@/store";
import { paramsPagingDefault } from "@/store/component.store";
import { filterParamsAdvanced } from "@/utils/format-data";
import { cloneDeep } from "lodash-es";
import { useI18n } from "vue-i18n";
import SearchPane from "../../shared/SearchPane.vue";
import { OFFER_TABS_VALUE } from "@/constants/offer";

const { downloading, downloadFile } = useDownloadFile();
const { handleDragUserPocket } = useDragUserPocket();
const { dragOfferType } = storeToRefs(useDragStore());
const { t, locale } = useI18n();
const useSnackbar = useSnackbarStore();
const storeComponent = useComponentStore();
const {
  resetPaneWhenSelectItem,
  resetParamsFilterComponentSearch,
  setAdvencedSearchList,
  resetComponentSearch,
} = storeComponent;
const {
  showDetail,
  paramsFilterComponent,
  componentSearch,
  componentSelected,
  isDuplicate,
  isEdit,
  showResourceAdd,
  isInOfferMode,
  code,
  selectedOffer,
  resourceParamsFilter,
  showEntitySearch,
  advencedSearchList,
  viewMode,
  componentDetail,
  showOfferSearch,
  isApplied,
  openSearchDetail,
  optionsType,
  optionsSubType,
  resourceListAdd,
  currentTabEdit,
  componentEntityList,
} = storeToRefs(storeComponent);

const emit = defineEmits([
  "search-offer",
  "close-search-offer",
  "open-search-offer",
]);
const popupContent = ref();
const openPopup = ref(false);
const isLoading = ref(false);
const searchPane = ref<InstanceType<typeof SearchPane>>();
const tablePane = ref();
const currentAction = ref("");
const newComponentClick = ref<any>(null);
const localParamsFilterComponent = ref<any>(
  cloneDeep(paramsFilterComponent.value)
);
const isResetSearch = ref(false);
const pagingSize = ref(7);
const componentKey = ref<number>(0);

const isGridMode = computed(() => viewMode.value === VIEW_MODE.GRID);
const containerClass = computed(() =>
  showOfferSearch.value
    ? "rounded-r-lg border-l-[#E6E9ED] border-l-[1px]"
    : "rounded-lg"
);
const isDisableAdvancedButton = computed<boolean>(() => {
  const { type, subType } = localParamsFilterComponent.value;
  return (!type?.trim() && !isInOfferMode.value) || !subType?.trim();
});

const listActions = (item) => [
  {
    name: t("product_platform.duplicate"),
    icon: DuplicateIcon,
    onClick: () => {
      currentAction.value = COMPONENT_DETAIL_CATEFORY.DUPLICATE;
      if (isEdit.value || isDuplicate.value) {
        popupContent.value = !isEdit.value
          ? t("product_platform.groupCancelDuplicate")
          : t("product_platform.groupCancelEdit");
        newComponentClick.value = item;
        openPopup.value = true;
      } else {
        componentDetail.value = {
          general: [],
          additional: [],
          resources: [],
        };
        handleDuplicate(item);
      }
    },
  },
];

const titleAppend = computed(() => {
  return showOfferSearch.value
    ? ""
    : isInOfferMode.value
      ? "product_platform.with_offer_mode"
      : "product_platform.component_pool";
});

const onSearchOffer = () => {
  isInOfferMode.value = !isInOfferMode.value;
  showResourceAdd.value = false;
  storeComponent.resetParamsFilterOffer();
  storeComponent.resetParamsFilterComponent();
  isEdit.value = false;
  isDuplicate.value = false;
  selectedOffer.value = null;
  localParamsFilterComponent.value.offerUuid = null;
  if (isInOfferMode.value) {
    pagingSize.value = 7;
  } else {
    pagingSize.value = 14;
  }
  emit("search-offer");
};

const handleChangeViewMode = (value) => {
  viewMode.value = value;
  showDetail.value = false;
  componentSelected.value = null;
  isEdit.value = false;
  showEntitySearch.value = false;
  localParamsFilterComponent.value.page = 1;
  localParamsFilterComponent.value.size = isGridMode.value ? 14 : 10;
  paramsFilterComponent.value = cloneDeep(localParamsFilterComponent.value);
  resetComponentSearch();
  if (!localParamsFilterComponent.value.subType.trim()) return;
  getListComponent();
};

const handleSearchDetail = async () => {
  if (!localParamsFilterComponent.value.type && !isInOfferMode.value) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }

  if (
    localParamsFilterComponent.value?.subType &&
    localParamsFilterComponent.value?.subType.trim()
  ) {
    if (!isApplied.value) {
      localParamsFilterComponent.value.page = 1;
      localParamsFilterComponent.value.size = isGridMode.value ? 14 : 10;
      componentSearch.value.pagination.currentPage = 1;
      paramsFilterComponent.value = {
        ...cloneDeep(localParamsFilterComponent.value),
        general: paramsFilterComponent.value.general,
        additional: paramsFilterComponent.value.additional,
      };
      await setAdvencedSearchList(paramsFilterComponent.value.subType);
    }
    openSearchDetail.value = true;
  } else {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_subType"),
      "error"
    );
  }
};

const handleSearch = async (
  size: number,
  isClick: boolean = true,
  page = 1
): Promise<void> => {
  if (isClick) {
    isResetSearch.value = false;
    isEdit.value = false;
    componentSearch.value.pagination.currentPage = 1;
    code.value = null;
  }
  localParamsFilterComponent.value.page = isClick ? 1 : page;
  localParamsFilterComponent.value.size = size;
  paramsFilterComponent.value = {
    ...cloneDeep(localParamsFilterComponent.value),
    general: paramsFilterComponent.value.general,
    additional: paramsFilterComponent.value.additional,
  };
  await getListComponent(size);
  if (isClick) {
    closeComponentDetail();
    isDuplicate.value = false;
    showEntitySearch.value = false;
    showResourceAdd.value = false;
  }
};

const getListComponent = async (pageSize?: number): Promise<void> => {
  tablePane?.value?.validate();
  searchPane?.value?.validate();
  if (
    (!localParamsFilterComponent.value.type && !isInOfferMode.value) ||
    (!isGridMode.value && !localParamsFilterComponent.value.subType.trim())
  ) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }
  if (!paramsFilterComponent.value?.offerUuid && isInOfferMode.value) return;
  const { type, subType, searchBy, searchKey, page, size, offerUuid } =
    paramsFilterComponent.value;
  let params = {
    offerUuid: offerUuid,
    mctgrItemCode: type,
    itemCode: subType,
    objName: searchBy === SearchBy.Name ? searchKey : undefined,
    objCode: searchBy === SearchBy.Code ? searchKey : undefined,
    page: page,
    size: pageSize ? pageSize : size,
  } as any;

  if (isApplied.value) {
    params = {
      ...filterParamsAdvanced(paramsFilterComponent.value),
      ...params,
    };
  }

  try {
    isLoading.value = true;
    if (isGridMode.value) {
      await storeComponent.getListComponentSearchAdvance(params);
    } else {
      await storeComponent.getDataComponentListView(params);
    }
    if (code.value) {
      const newItem = componentSearch.value.items.filter(
        (item: any) => item?.code === code.value
      )[0];
      if (newItem) {
        selectComponent(newItem);
      } else {
        const tempParams = cloneDeep(params);
        tempParams.offerUUID = undefined;
        tempParams.componentType =
          componentSelected.value?.itemType || undefined;
        tempParams.componentSubType =
          componentSelected.value?.itemCode || undefined;
        tempParams.componentCode = code.value;
        tempParams.page = 1;
        const newItemData =
          await storeComponent.getListComponentSearch(tempParams);
        selectComponent(newItemData.data.data?.dataList[0]);
      }
    }
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
  } finally {
    isLoading.value = false;
  }
};

const selectComponent = (componentItem: any): void => {
  currentAction.value = COMPONENT_DETAIL_CATEFORY.DETAIL;
  if (isEdit.value || isDuplicate.value) {
    popupContent.value = !isEdit.value
      ? t("product_platform.groupCancelDuplicate")
      : t("product_platform.groupCancelEdit");
    newComponentClick.value = componentItem;
    openPopup.value = true;
  } else {
    componentDetail.value = {};
    componentEntityList.value = [];
    currentTabEdit.value = OFFER_TABS_VALUE.GENERAL;
    resetPaneWhenSelectItem();
    showDetail.value = true;
    componentSelected.value = {
      itemDetlTypeCd: componentItem.itemCode,
      itemCd: componentItem.code,
      itemTypeNm: componentItem.name,
      prodUuid: componentItem.itemUnique,
      ...componentItem,
    };
    resourceParamsFilter.value.componentUUID = componentItem.itemUnique;
    isEdit.value = false;
    showEntitySearch.value = false;
  }
};

const handleChangePage = async (page) => {
  paramsFilterComponent.value.page = page;
  code.value = null;
  await getListComponent();
};

const handleDuplicate = async (item) => {
  selectComponent(item);
  isDuplicate.value = true;
  // showDetail.value = false;
  code.value = null;
};

const handleResetSearch = (isReset: boolean = false): void => {
  tablePane?.value?.resetValidate();
  searchPane?.value?.resetValidate();
  localParamsFilterComponent.value = cloneDeep(paramsPagingDefault);
  resetParamsFilterComponentSearch();
  localParamsFilterComponent.value.offerUUID = selectedOffer.value?.objUuid;
  isDuplicate.value = false;
  optionsSubType.value = [];
  componentSelected.value = null;
  showEntitySearch.value = false;
  isApplied.value = false;
  isResetSearch.value = isReset;
  storeComponent.resetAdvancedSearchParams();
};

const handleCalculatePage = (newPageSize: number): number => {
  const page = localParamsFilterComponent.value.page || 1;
  const size = localParamsFilterComponent.value.size || 14;
  const currentTotal = (page - 1) * size + 1;
  const newPage = Math.ceil(currentTotal / newPageSize);
  return newPage ? newPage : 1;
};

const onClose = async () => {
  emit("close-search-offer");
  if (!selectedOffer.value?.objUuid) {
    isInOfferMode.value = false;
  } else {
    if (isResetSearch.value) return;
    nextTick(() => {
      searchPane.value?.calcTotalItem();
      const size = searchPane.value?.totalItem || 14;
      const page = handleCalculatePage(size);
      localParamsFilterComponent.value.page = page;
      handleSearch(size, false);
      componentKey.value++;
    });
  }
};

const onOpen = async () => {
  emit("open-search-offer");
  isInOfferMode.value = true;
  showResourceAdd.value = false;
  isDuplicate.value = false;
  if (selectedOffer.value?.objUuid) {
    if (isResetSearch.value) return;
    nextTick(() => {
      searchPane.value?.calcTotalItem();
      const size = searchPane.value?.totalItem || 7;
      const page = handleCalculatePage(size);
      localParamsFilterComponent.value.page = page;
      handleSearch(size, false);
    });
  }
};

const closePopupSave = () => {
  openPopup.value = false;
};

const handleSubmitDetails = () => {
  componentDetail.value = {};
  componentEntityList.value = [];
  currentTabEdit.value = OFFER_TABS_VALUE.GENERAL;
  if (currentAction.value === COMPONENT_DETAIL_CATEFORY.DUPLICATE) {
    isDuplicate.value = false;
    isEdit.value = false;
    handleDuplicate(newComponentClick.value);
  } else {
    resetPaneWhenSelectItem();
    if (
      componentSelected.value?.itemUnique &&
      componentSelected.value.itemUnique !== newComponentClick.value?.itemUnique
    ) {
      resourceListAdd.value = [];
    }
    showDetail.value = true;
    componentSelected.value = {
      itemDetlTypeCd: newComponentClick.value?.itemCode,
      itemCd: newComponentClick.value?.code,
      itemTypeNm: newComponentClick.value?.name,
      itemUnique: newComponentClick.value?.itemUnique,
      ...newComponentClick.value,
    };
    resourceParamsFilter.value.componentUUID =
      newComponentClick.value?.itemUnique;
    isEdit.value = false;
    showEntitySearch.value = false;
  }
  closePopupSave();
};

const checkSelectRequire = () => {
  if (showOfferSearch.value || isInOfferMode.value) {
    return false;
  }
  return true;
};

const handleDownload = async () => {
  try {
    if (
      !paramsFilterComponent.value.type?.trim() ||
      !paramsFilterComponent.value.subType?.trim()
    ) {
      tablePane.value?.validate();
      useSnackbar.showSnackbar(
        t("product_platform.required_field_missing"),
        "error"
      );
      return;
    }
    const { type, subType, searchBy, searchKey, objUuid } =
      paramsFilterComponent.value;
    const offerUuid = objUuid || selectedOffer.value?.objUuid;
    let params: any = {
      offerUuid: offerUuid,
      mctgrItemCode: type,
      itemCode: subType,
      objName: searchBy === SearchBy.Name ? searchKey : undefined,
      objCode: searchBy === SearchBy.Code ? searchKey : undefined,
      language: locale.value || "en",
    };
    if (isApplied.value) {
      params = {
        ...params,
        ...filterParamsAdvanced(paramsFilterComponent.value),
      };
    }
    await downloadFile(EXPORT_COMPONENT_EXCEL_PATH, params, "Component");
  } catch (error: any) {
    useSnackbar.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const handleResetSearchDetail = async () => {
  isApplied.value = false;
  closePopupSearchDetail();
  await getListComponent();
};

const applyPopupSearchDetail = async (event) => {
  isApplied.value = true;
  showResourceAdd.value = false;
  localParamsFilterComponent.value.page = 1;
  localParamsFilterComponent.value.size = isGridMode.value ? 14 : 10;
  paramsFilterComponent.value = cloneDeep(localParamsFilterComponent.value);
  closeComponentDetail();
  isDuplicate.value = false;
  showEntitySearch.value = false;
  paramsFilterComponent.value.general = event.general;
  paramsFilterComponent.value.additional = event.additional;
  await getListComponent();
  openSearchDetail.value = false;
};

const closeComponentDetail = () => {
  showDetail.value = false;
  componentSelected.value = null;
  code.value = null;
};

const closePopupSearchDetail = () => {
  openSearchDetail.value = false;
};

const handleDragStart = ({
  event,
  item,
}: {
  event: DragEvent;
  item: any;
}): void => {
  dragOfferType.value = item.itemType.charAt(0);
  handleDragUserPocket(event, { userPocketType: LargeItemCode.Offer, ...item });
};

watch(
  paramsFilterComponent,
  (value) => {
    localParamsFilterComponent.value = cloneDeep(value);
  },
  { deep: true }
);

watch(
  [() => localParamsFilterComponent.value.type, () => optionsType.value],
  ([value, _newList], [_oldValue, _oldList]) => {
    nextTick(() => {
      optionsSubType.value =
        optionsType.value.find((type) => type.value === value)?.children ?? [];
      if (value !== _oldValue) {
        localParamsFilterComponent.value.subType = SPACE;
      }
    });
  },
  { deep: true }
);

defineExpose({ handleResetSearch, getListComponent });
</script>
