<template>
  <cf-card-dropdown
    :title="offer?.prodNm || offer?.dcntNm || offer?.eqipTrmNm"
    :description="offer?.prodCd || offer?.dcntCd || offer?.eqipTrmCd"
    type-bg="linear"
    :active="active"
    :node="{
      hideNodeLeft: true,
      isActiveNodeLeft: false,
      hideNodeRight: true,
      isActiveNodeRight: false,
    }"
    :search-text="searchOfferTextObj.searchText"
    :search-field="searchOfferTextObj.searchField"
    :border-color-action="getHoverColor()"
    :type-of-prod="getTypeOfProd()"
    :icon-color="getIconColor()"
    :actions="listActions(offer)"
    :expired="isExpiredTime(offer.valdEndDtm)"
    :draggable="true"
    :is-new="offer?.isMoved"
    show-icon-status
    editable
    hide-detail
    @dragstart="handleDragStart($event, offer)"
    @dragend="handleDragEnd"
  >
    <template
      v-if="categoryStore.getCategoryCurrentTab === CATEGORY_TABS.DEVICE.TYPE"
      #icon
    >
      <span class="flex justify-center align-center w-[40px] h-[40px]">
        <DeviceIcon />
      </span>
    </template>
  </cf-card-dropdown>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import useCategoryStore from "@/store/category.store";
import useRedirect from "@/composables/useRedirect";
import { CATEGORY_TABS, OFFER_TYPE } from "@/constants/";
import { isExpiredTime } from "@/utils/format-data";
import { useDragStore } from "@/store";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import { OFFER_NODE_TYPE } from "@/constants/category";
import { setHoverColor } from "@/utils/impact-analysis-utils";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import type { ActionType } from "@/interfaces/prod";

const categoryStore = useCategoryStore();
const { searchOfferTextObj, dragOfferUuid } = storeToRefs(useCategoryStore());
const { isDragging, dragOfferType } = storeToRefs(useDragStore());
const { moveOfferSearchPage } = useRedirect();
const { handleDragUserPocket } = useDragUserPocket();

defineProps({
  offer: {
    type: Object as PropType<any>,
    default: () => {},
  },
  draggable: {
    type: Boolean,
    default: false,
  },
  active: {
    type: Boolean,
    default: false,
  },
});

const { t } = useI18n();

const listActions = (item: any): ActionType[] => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      moveOfferSearchPage({
        ...item,
        objUuid: item.prodUuid,
        itemCode: "",
        objCode: item.prodCd,
        itemCodeName: item.prodNm,
        offerType: "",
      });
    },
  },
];

const handleDragStart = (event: DragEvent, item: any): void => {
  dragOfferUuid.value = item.ctgrNodeUuid;
  dragOfferType.value = OFFER_NODE_TYPE;
  isDragging.value = true;
  handleDragUserPocket(event, {
    userPocketType: LARGE_ITEM_CODE.OFFER,
    ...item,
  });
};

const handleDragEnd = () => {
  isDragging.value = false;
  dragOfferType.value = "";
};

const getTypeOfProd = () => {
  switch (categoryStore.getCategoryCurrentTab) {
    case CATEGORY_TABS.PRICE_PLAN.TYPE:
      return "P";
    case CATEGORY_TABS.ADD_ON.TYPE:
      return "A";
    case CATEGORY_TABS.DISCOUNT.TYPE:
      return "D";
    default:
      return "";
  }
};

const getIconColor = () => {
  switch (categoryStore.getCategoryCurrentTab) {
    case CATEGORY_TABS.PRICE_PLAN.TYPE:
      return "#EB7A3D";
    case CATEGORY_TABS.ADD_ON.TYPE:
      return "#9947D3";
    case CATEGORY_TABS.DISCOUNT.TYPE:
      return "#23B27F";
    default:
      return "";
  }
};

const getHoverColor = () => {
  switch (categoryStore.getCategoryCurrentTab) {
    case CATEGORY_TABS.PRICE_PLAN.TYPE:
      return setHoverColor(OFFER_TYPE.PRICEPLAN);
    case CATEGORY_TABS.ADD_ON.TYPE:
      return setHoverColor(OFFER_TYPE.ADDON);
    case CATEGORY_TABS.DISCOUNT.TYPE:
      return setHoverColor(OFFER_TYPE.DISCOUNT);
    case CATEGORY_TABS.DEVICE.TYPE:
      return setHoverColor(OFFER_TYPE.DEVICE);
    default:
      return "";
  }
};
</script>

<style scoped lang="scss">
.option-action-btn {
  color: #6b6d70;
}

.option-action-btn:hover {
  cursor: pointer;
}
</style>
