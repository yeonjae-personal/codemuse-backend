<template>
  <SearchPane
    ref="searchPane"
    title="product_platform.offer_search"
    container-class="rounded-lg"
    :pane-type="SearchPaneType.Offer"
    :pane-col="ColNumber.One"
    :model-param="paramsAddOffer"
    :model-list="addOfferList.items"
    :pagination="addOfferList.pagination"
    :option-types="offerTypesList"
    :selected-item="selectedOfferAddPane"
    :search-item-actions="listActions"
    :type-select-require="false"
    :item-height="62"
    :list-item-not-drag="existList"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-click-item="handClickOffer"
    @on-change-page="handleChangePage"
    @on-drag-start="handleDragStart"
  />
</template>

<script setup lang="ts">
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import { SEARCH_CATEGORY } from "@/constants/extendsManager";
import { ColNumber, LargeItemCode, SearchPaneType } from "@/enums";
import {
  useDragStore,
  useExtendCreateStore,
  useExtendSearchStore,
} from "@/store";
import { useI18n } from "vue-i18n";
import useRedirect from "@/composables/useRedirect";
import useDragUserPocket from "@/composables/useDragUserPocket";
import type { ActionType } from "@/interfaces/prod";

const props = defineProps({
  isCreate: {
    type: Boolean,
    default: false,
  },
});
const groupSearchStore = useExtendSearchStore();
const groupCreateStore = useExtendCreateStore();

const selectedStore = computed(() => {
  return props.isCreate ? groupCreateStore : groupSearchStore;
});

const { t } = useI18n();
const {
  paramsAddOffer,
  addOfferList,
  offerTypesList,
  selectedOfferAddPane,
  inputValueAddOfferPane,
  groupDetailData,
} = storeToRefs(selectedStore.value);
const { getOfferList, resetParamListOfferSearch } = selectedStore.value;
const { moveOfferSearchPage } = useRedirect();
const { dragOfferType } = storeToRefs(useDragStore());
const { handleDragUserPocket } = useDragUserPocket();

const existList = computed(() => {
  return groupDetailData.value.offerTab.map((ofr) => ({
    ...ofr,
    itemUnique: ofr.offrUuid,
  }));
});

const listActions = (item: any): ActionType[] => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      moveOfferSearchPage({
        itemCode: item.objCode,
        objCode: item.objCode,
        objUuid: item.objUuid,
        offerType: item.itemCode,
      });
    },
  },
];

const handleSearch = async (size: number, isClick = false, page = 1) => {
  paramsAddOffer.value.page = isClick ? 1 : page;
  paramsAddOffer.value.size = size;
  await getOfferList(SEARCH_CATEGORY.TARGET);
};

const handleResetSearch = () => {
  resetParamListOfferSearch(SEARCH_CATEGORY.TARGET);
  inputValueAddOfferPane.value = null;
};

const handClickOffer = async (value) => {
  selectedOfferAddPane.value = value;
};

const handleChangePage = async (page) => {
  paramsAddOffer.value.page = page;
  await getOfferList(SEARCH_CATEGORY.TARGET);
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
</script>
