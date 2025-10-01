<template>
  <div class="category-information">
    <div class="flex flex-col gap-2 px-1">
      <cf-card-dropdown
        v-for="(item, index) in categoryOfferListTree"
        :key="index"
        :title="item.objName"
        :description="item.objCode"
        :style="{ 'z-index': 999 }"
        type-bg="linear"
        :border-color-action="getHoverColor()"
        :type-of-prod="getTypeOfProd()"
        :icon-color="getIconColor()"
        hide-detail
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
        show-icon-status
        editable
        draggable
        :actions="listActions(item)"
        :active="item.objUuid === selectedOffer?.objUuid"
        :expired="isExpiredTime(item.validEndDtm)"
        @on-click-card="onChooseCard(item)"
        @dragstart="handleDragStart($event, item)"
        @dragend="handleDragEnd"
      >
        <template v-if="item.itemCode === CATEGORY_TABS.DEVICE.VALUE" #icon>
          <span class="flex justify-center align-center w-[40px] h-[40px]">
            <DeviceIcon />
          </span>
        </template>
      </cf-card-dropdown>
    </div>
  </div>
  <BasePagination
    v-if="pagination.totalPages > 0"
    :pagination="pagination"
    class-name="mt-5"
    @on-change-page="handleChangePage"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { CATEGORY_TABS, OFFER_TYPE } from "@/constants/index";
import { useCategoryStore, useDragStore } from "@/store";
import { setHoverColor } from "@/utils/impact-analysis-utils";
import { isExpiredTime } from "@/utils/format-data";
import useRedirect from "@/composables/useRedirect";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import type { ActionType } from "@/interfaces/prod";

const { t } = useI18n();

const categoryStore = useCategoryStore();
const { isDragging, dragOfferType } = storeToRefs(useDragStore());
const { moveOfferSearchPage } = useRedirect();
const { handleDragUserPocket } = useDragUserPocket();

const selectedOffer = ref<any>(null);

const categoryOfferListTree = computed(
  () => categoryStore.getCategoryOfferListTreeData.items
);
const pagination = computed(
  () => categoryStore.getCategoryOfferListTreeData.pagination
);

const listActions = (item: any): ActionType[] => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      moveOfferSearchPage(item);
    },
  },
];

const handleChangePage = async (pageNo) => {
  const ctgrNodeUuid = categoryStore.getCategoryOfferListTreeData.ctgrNodeUuid;
  await categoryStore.getCategoryOfferListTree({
    ctgrNodeUuid: ctgrNodeUuid,
    page: pageNo,
    size: 7,
  });
};

const onChooseCard = (item) => {
  selectedOffer.value = item;
};

const getTypeOfProd = () => {
  const currentTab = categoryStore.getCategoryCurrentTab;
  switch (currentTab) {
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
  const currentTab = categoryStore.getCategoryCurrentTab;
  switch (currentTab) {
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
    default:
      return "";
  }
};

const handleDragStart = (event: DragEvent, item: any) => {
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
</script>

<style scoped>
.category-information {
  display: flex;
  flex-direction: column;
  padding: 8px 8px 0px;
  gap: 8px;
  height: calc(100vh - 365px);
  overflow-y: auto;
  scrollbar-width: thin;
}
</style>
