<template>
  <div class="rounded-[12px] flex flex-col h-full bg-white relative">
    <HeadForm :offer-duplicate-mode="offerDuplicateMode" />
    <ExtendsDetail :offer-duplicate-mode="offerDuplicateMode" />
    <ArrowLeftIcon
      v-if="showArrowLeft"
      class="absolute top-[174px] left-[0px] cursor-pointer text-[#525457] hover:text-[#303132] rolate"
      @click="onOpenSideForm"
    />
  </div>
</template>

<script setup lang="ts">
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
const { sideDisplay, isEdit } = storeToRefs(selectedStore.value);

const showArrowLeft = computed(() => {
  if (props.offerDuplicateMode) {
    return !sideDisplay.value.relationSearch && !sideDisplay.value.targetSearch;
  }
  return isEdit.value
    ? !sideDisplay.value.relationSearch
    : !sideDisplay.value.offerSearch;
});

const onOpenSideForm = () => {
  if (!props.offerDuplicateMode) {
    if (isEdit.value) {
      sideDisplay.value.relationSearch = true;
    } else {
      sideDisplay.value.offerSearch = true;
    }
  } else {
    sideDisplay.value.relationSearch = true;
  }
};

watch(
  () => isEdit.value,
  (val) => {
    if (!val) {
      sideDisplay.value.targetSearch = false;
      sideDisplay.value.relationSearch = false;
    }
  }
);
</script>

<style lang="scss" scoped>
.rolate {
  transform: rotate(180deg);
}
</style>
