<template>
  <div
    class="grid grid-cols-2"
    :class="[viewMode === VIEW_MODE.GRID ? 'col-span-2' : 'col-span-4']"
  >
    <SearchPane
      v-if="displayForm.offerSearch"
      ref="searchPane"
      title="product_platform.offer_search"
      container-class="!bg-lighter rounded-l-lg"
      :pane-type="SearchPaneType.Offer"
      :pane-col="ColNumber.One"
      :model-param="paramsSearchOffer"
      :model-list="offerList.items"
      :pagination="offerList.pagination"
      :option-types="offerTypesList"
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
    <GroupSearchAndTable ref="groupSearchRef" />
  </div>
</template>
<script lang="ts" setup>
import { getProductStructureDetailRootApi } from "@/api/prod/productApi";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { SEARCH_CATEGORY } from "@/constants/extendsManager";
import { OFFER_TYPE, SPACE, VIEW_MODE } from "@/constants/index";
import { ColNumber, LargeItemCode, SearchPaneType } from "@/enums";
import { useDragStore, useExtendSearchStore, useSnackbarStore } from "@/store";
import { useI18n } from "vue-i18n";

const useSnackbar = useSnackbarStore();
const {
  displayForm,
  paramsSearchOffer,
  paramsSearchGroup,
  offerList,
  offerTypesList,
  selectedOffer,
  inputValue,
  viewMode,
  isOfferSearchGroup,
  isResetValue,
} = storeToRefs(useExtendSearchStore());
const {
  getOfferList,
  resetParamListOfferSearch,
  resetParamListGroupSearch,
  getGroupList,
} = useExtendSearchStore();
const { dragOfferType } = storeToRefs(useDragStore());
const { handleDragUserPocket } = useDragUserPocket();
const searchPane = ref();
const groupSearchRef = ref<any>();

const { t } = useI18n();

const handleSearch = async (pageSize: number, isClick: Boolean, page = 1) => {
  if (isClick) {
    searchPane?.value?.validate();
    selectedOffer.value = null;
  }
  if (
    !paramsSearchOffer.value?.type ||
    paramsSearchOffer.value?.type === SPACE
  ) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }
  paramsSearchOffer.value.page = isClick ? 1 : page;
  paramsSearchOffer.value.size = pageSize;
  await getOfferList(SEARCH_CATEGORY.OFFER);
};

const handleResetSearch = () => {
  searchPane?.value?.resetValidate();
  selectedOffer.value = null;
  resetParamListOfferSearch(SEARCH_CATEGORY.OFFER);
  inputValue.value = null;
  resetParamListGroupSearch();
  displayForm.value.groupDetail = false;
};

const handClickOffer = async (value) => {
  selectedOffer.value = value;
  paramsSearchGroup.value.childOffrUuid = selectedOffer.value.objUuid;
  paramsSearchGroup.value.offerGroupTypeCode = convertOfferType(
    selectedOffer.value.itemCode
  );
  isOfferSearchGroup.value = true;
  isResetValue.value = false;
  paramsSearchGroup.value.page = 1;
  paramsSearchGroup.value.size = paramsSearchOffer.value.size;
  displayForm.value.groupDetail = false;
  displayForm.value.groupDuplicate = false;
  displayForm.value.addOffer = false;
  await getGroupList();
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

const convertOfferType = (itemCode) => {
  if (!itemCode) {
    return "";
  }
  switch (itemCode) {
    case OFFER_TYPE.PRICEPLAN:
      return "P";
    case OFFER_TYPE.ADDON:
      return "A";
    case OFFER_TYPE.DISCOUNT:
      return "D";
    case OFFER_TYPE.DEVICE:
      return "V";
    default:
      return "";
  }
};

const handleChangePage = async (page) => {
  if (paramsSearchOffer.value.page === page) return;
  paramsSearchOffer.value.page = page;
  await getOfferList(SEARCH_CATEGORY.OFFER);
};

const handleChangeSize = async (size) => {
  if (paramsSearchOffer.value.size === size) return;
  paramsSearchOffer.value.size = size;
  await getOfferList(SEARCH_CATEGORY.OFFER);
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

const callListGroupSearch = () => {
  return groupSearchRef.value?.searchGroupList();
};
defineExpose({ fetchList: callListGroupSearch });
</script>
