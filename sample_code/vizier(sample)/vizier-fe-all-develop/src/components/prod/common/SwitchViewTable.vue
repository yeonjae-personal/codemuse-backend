<template>
  <div
    class="flex bg-base rounded-full px-[6px] py-[4px] w-[78px] justify-center items-center"
  >
    <div
      class="flex justify-center items-center cursor-pointer rounded-full w-8 h-8 text-text-lighter"
      :class="{
        'bg-[#FFFFFF] text-text-primary': viewMode === VIEW_MODE.GRID,
      }"
      @click="setView(VIEW_MODE.GRID)"
    >
      <grid-icon />
    </div>
    <div
      class="flex justify-center items-center cursor-pointer rounded-full w-8 h-8 ml-[6px] text-text-lighter"
      :class="{
        'bg-[#FFFFFF] text-text-primary': viewMode === VIEW_MODE.LIST,
      }"
      icon
      @click="setView(VIEW_MODE.LIST)"
    >
      <list-view-icon />
    </div>
  </div>
</template>

<script setup lang="ts">
import { VIEW_MODE } from "@/constants/";

const props = defineProps({
  modelValue: {
    type: String,
    default: VIEW_MODE.GRID,
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
