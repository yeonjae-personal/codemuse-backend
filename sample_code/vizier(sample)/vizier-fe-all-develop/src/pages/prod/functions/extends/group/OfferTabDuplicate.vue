<template>
  <div v-if="listOffer?.length" class="grid grid-cols-1 gap-3 py-2">
    <template v-for="item in listOffer" :key="item.offrUuid">
      <cf-card-dropdown
        :title="item.offrNm"
        :description="item.offrCd"
        type-bg="linear"
        hide-detail
        :detail-height="'230px'"
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
        :border-color-action="setHoverColor(item.offrType)"
        :type-of-prod="setIconType(item.offrType)"
        :icon-color="setIconColor(item.offrType)"
        :show-icon-status="checkIsNew(item) || item.itemRemoved"
        editable
        draggable
        :disable="item.itemRemoved"
        :actions="listActions(item)"
        :active="item.offrUuid === localOfferActive?.offrUuid || item.itemNew"
        :style="{ 'z-index': 999 }"
        :is-new="checkIsNew(item)"
        @on-click-card="handleClick(item)"
        @dragstart="
          handleDragUserPocket($event, {
            userPocketType: LARGE_ITEM_CODE.OFFER,
            ...item,
          })
        "
      />
    </template>
  </div>
  <template v-else>
    <div class="h-full w-full flex justify-center items-center">
      <NoData />
    </div>
  </template>
</template>
<script setup lang="ts">
import { useOfferDuplicateProcessStore } from "@/store";
import {
  setIconColor,
  setIconType,
  setHoverColor,
} from "@/utils/impact-analysis-utils";
import { useI18n } from "vue-i18n";
import useRedirect from "@/composables/useRedirect";
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import EnableIcon from "@/components/prod/icons/EnableIcon.vue";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import type { ActionType } from "@/interfaces/prod";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { LARGE_ITEM_CODE } from "@/constants/offer";

const { t } = useI18n();

const { groupDetailData, offerDuplicated } = storeToRefs(
  useOfferDuplicateProcessStore()
);
const { moveOfferSearchPage } = useRedirect();
const { handleDragUserPocket } = useDragUserPocket();

const listOffer = ref<any>();
const localOfferActive = ref<any>(null);

const listActions = (item: any): ActionType[] => {
  const openNewTabAction = {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      moveOfferSearchPage({
        itemCode: item?.offrType || "",
        objCode: item?.offrCd || "",
        offerType: item?.offrType || "",
        objUuid: item?.offrUuid || "",
      });
    },
  };
  if (item.itemRemoved) {
    return [
      {
        name: t("product_platform.actionEnable"),
        icon: EnableIcon,
        onClick: () => {
          item.itemRemoved = !item.itemRemoved;
        },
      },
      openNewTabAction,
    ];
  }
  return [
    {
      name: t("product_platform.actionRemove"),
      icon: TrashIcon,
      onClick: () => {
        item.itemRemoved = !item.itemRemoved;
      },
    },
    openNewTabAction,
  ];
};

const handleClick = (item) => {
  localOfferActive.value = item;
};

const checkIsNew = (item) => {
  return item?.offrUuid === offerDuplicated?.value?.objUuid;
};

watch(
  () => groupDetailData.value.offerTab,
  (val) => {
    listOffer.value = val;
  },
  { deep: true, immediate: true }
);
</script>

<style scoped>
:deep().v-field {
  height: 32px;
  display: flex;
  align-items: center;
}
:deep().v-field__field {
  text-overflow: ellipsis;
  width: 170px;
}
.offer-edit-datetime-picker :deep().dp__pointer {
  height: 32px;
}
.detail-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.category-information {
  display: flex;
  flex-direction: column;
  padding: 8px 8px 0px;
  gap: 8px;
  height: calc(100vh - 370px);
  overflow-y: auto;
  scrollbar-width: thin;
}

.new-mark {
  width: 8px;
  height: 8px;
  position: absolute;
  top: 14px;
  right: 10px;
  background: #ea4f3a;
  border-radius: 999px;
}
</style>
