<template>
  <div
    class="grid grid-cols-2"
    :class="[viewMode === VIEW_MODE.GRID ? 'col-span-2' : 'col-span-4']"
  >
    <SearchPane
      v-if="showOfferSearch"
      ref="searchPane"
      title="product_platform.offer_search"
      container-class="!bg-lighter rounded-l-lg"
      :pane-type="SearchPaneType.Offer"
      :pane-col="ColNumber.One"
      :model-param="paramsFilterOffer"
      :model-list="offerSearch.items"
      :pagination="offerSearch.pagination"
      :option-types="offersType"
      :selected-item="selectedOffer"
      :item-height="62"
      @on-search="handleSearch"
      @on-reset="handleResetSearch"
      @on-click-item="handClickOffer"
      @on-click-show-detail="handClickShowDetail"
      @on-change-page="handleChangePage"
      @on-change-page-size="handleChangeSize"
      @on-drag-start="handleDragStart"
    />
    <ComponentSearchAndTable
      ref="componentSearchRef"
      @search-offer="onSearchOffer"
      @close-search-offer="closeOfferSearch"
      @open-search-offer="openOfferSearch"
    />
  </div>
</template>
<script lang="ts" setup>
import { getProductStructureDetailRootApi } from "@/api/prod/productApi";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { SPACE, VIEW_MODE } from "@/constants/index";
import { ColNumber, LargeItemCode, SearchBy, SearchPaneType } from "@/enums";
import { useComponentStore, useDragStore, useSnackbarStore } from "@/store";
import { useI18n } from "vue-i18n";

const useSnackbar = useSnackbarStore();
const {
  paramsFilterOffer,
  paramsFilterComponent,
  offerSearch,
  showResourceAdd,
  isInOfferMode,
  selectedOffer,
  showOfferSearch,
  viewMode,
  offersType,
  showDetail,
  isDuplicate,
  showEntitySearch,
  isEdit,
} = storeToRefs(useComponentStore());
const {
  getListOfferSearch,
  resetParamsFilterComponentSearch,
  resetParamFilterOfferSearch,
  getListComponentSearchAdvance,
} = useComponentStore();
const { t } = useI18n();

const { dragOfferType } = storeToRefs(useDragStore());
const { handleDragUserPocket } = useDragUserPocket();
const searchPane = ref<any>();
const componentSearchRef = ref<any>();

const handleSearch = async (
  pageSize: number,
  isClick: boolean = true,
  page = 1
) => {
  if (isClick) {
    searchPane?.value?.validate();
    selectedOffer.value = null;
  }
  if (
    !paramsFilterOffer.value?.type ||
    paramsFilterOffer.value?.type === SPACE
  ) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }
  paramsFilterOffer.value.page = isClick ? 1 : page;
  paramsFilterOffer.value.size = pageSize;
  getListOffer();
};

const handleResetSearch = () => {
  searchPane?.value?.resetValidate();
  selectedOffer.value = null;
  resetParamFilterOfferSearch();
  resetParamsFilterComponentSearch();
};

const handClickOffer = async (item) => {
  selectedOffer.value = item;
  showResourceAdd.value = false;
  isDuplicate.value = false;
  showDetail.value = false;
  showEntitySearch.value = false;
  isInOfferMode.value = true;
  isEdit.value = false;
  paramsFilterComponent.value.offerUuid = item.itemUnique;
  const params = {
    offerUuid: item.itemUnique,
    page: 1,
    size: paramsFilterOffer.value.size,
  };
  getListComponentSearchAdvance(params);
};

const handClickShowDetail = async (value, event) => {
  if (event) {
    const { data } = await getProductStructureDetailRootApi({
      objUuid: value.objUuid,
    });
    value.itemDetail = data;
  }
  value.expand = event;
};

const getListOffer = async () => {
  try {
    const { type, searchBy, searchKey, page, size } = paramsFilterOffer.value;

    const params = {
      name: searchBy === SearchBy.Name ? searchKey : undefined,
      code: searchBy === SearchBy.Code ? searchKey : undefined,
      itemCode: type,
      page,
      size,
      onlyValidDtm: true,
    };

    await getListOfferSearch(params);
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
  }
};

const handleChangePage = async (page) => {
  if (paramsFilterOffer.value.page === page) return;
  paramsFilterOffer.value.page = page;
  await getListOffer();
};

const handleChangeSize = async (size) => {
  if (paramsFilterOffer.value.size === size) return;
  paramsFilterOffer.value.size = size;
  await getListOffer();
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

const onSearchOffer = () => {
  if (!isInOfferMode.value) {
    showOfferSearch.value = false;
  } else {
    showOfferSearch.value = true;
    resetParamFilterOfferSearch();
    searchPane.value?.resetValidate?.();
  }
};

const closeOfferSearch = () => {
  showOfferSearch.value = false;
};
const openOfferSearch = () => {
  showOfferSearch.value = true;
};
const callListComponentSearch = (size) => {
  return componentSearchRef.value?.getListComponent(size);
};
defineExpose({ fetchList: callListComponentSearch });
</script>
