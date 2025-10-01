<template>
  <div
    class="flex bg-base rounded-full p-[6px] w-[82px] justify-center items-center mb-2"
  >
    <div
      class="flex justify-center items-center cursor-pointer rounded-full w-8 h-7 text-text-lighter"
      :class="{
        'bg-[#FFFFFF] text-text-primary': viewMode === SEARCH_MODE.OPTION1,
      }"
      @click="setView(SEARCH_MODE.OPTION1)"
    >
      <search-list-icon-active v-if="modelValue === SEARCH_MODE.OPTION1" />
      <search-list-icon v-else />
    </div>
    <div
      class="flex justify-center items-center cursor-pointer rounded-full w-8 h-7 ml-[6px] text-text-lighter"
      :class="{
        'bg-[#FFFFFF] text-text-primary': viewMode === SEARCH_MODE.OPTION2,
      }"
      icon
      @click="setView(SEARCH_MODE.OPTION2)"
    >
      <search-rank-icon-active v-if="modelValue === SEARCH_MODE.OPTION2" />
      <search-rank-icon v-else />
    </div>
  </div>
</template>

<script setup lang="ts">

import { SEARCH_MODE } from "@/constants/";

const props = defineProps({
  modelValue: {
    type: String,
    default: SEARCH_MODE.OPTION1,
  },
});

const emits = defineEmits(["toggleViewMode", "update:modelValue"]);

const viewMode = computed({
  get() {
    return props.modelValue;
  },
  set(newValue) {
    emits("update:modelValue", newValue);
  },
});

const setView = (mode) => {
  viewMode.value = mode;
  emits("toggleViewMode", mode);
};
</script>
