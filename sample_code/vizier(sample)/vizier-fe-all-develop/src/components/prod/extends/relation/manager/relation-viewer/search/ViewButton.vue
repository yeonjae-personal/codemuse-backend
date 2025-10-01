<template>
  <div
    class="flex rounded-[999px] bg-white border border-[#DCE0E5] text-[13px] font-weight-medium cursor-pointer text-center"
  >
    <div
      class="w-[120px] py-[10px] px-[15px] border-r border-r-[#E6E9ED] text-[#6B6D70]"
      :class="{ '!text-[#BA1642]': extendsView === EXTENDS_VIEW.SIMPLE }"
      @click="handleChangeView(EXTENDS_VIEW.SIMPLE)"
    >
      {{ $t("product_platform.relation.simpleTitle") }}
    </div>
    <div
      class="w-[120px] py-[10px] px-[15px] border-l border-l-[#E6E9ED] text-[#6B6D70]"
      :class="{ '!text-[#BA1642]': extendsView === EXTENDS_VIEW.DETAIL }"
      @click="handleChangeView(EXTENDS_VIEW.DETAIL)"
    >
      {{ $t("product_platform.relation.groupTitle") }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { EXTENDS_VIEW } from "@/constants/extendsManager";
import {
  useExtendManagerStore,
  useRelationManagerDuplicateStore,
} from "@/store";

const props = defineProps({
  offerDuplicateMode: {
    type: Boolean,
    default: false,
  },
});
const extendManagerStore = useExtendManagerStore();
const relationManagerDuplicateStore = useRelationManagerDuplicateStore();
const selectedStore = computed(() =>
  props.offerDuplicateMode ? relationManagerDuplicateStore : extendManagerStore
);

const { extendsView, sideDisplay } = storeToRefs(selectedStore.value);
const { resetDetailViewData, getGroupBySelectedItem } = selectedStore.value;

const handleChangeView = (value) => {
  extendsView.value = value;
  resetDetailViewData();
  getGroupBySelectedItem(
    extendsView.value === EXTENDS_VIEW.SIMPLE,
    props.offerDuplicateMode
  );
  if (value === EXTENDS_VIEW.DETAIL) {
    sideDisplay.value.offerSearch = false;
  } else {
    sideDisplay.value.offerSearch = true;
  }
};
</script>
