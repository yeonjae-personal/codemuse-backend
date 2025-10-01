<template>
  <div class="flex !min-h-[32px] detail-pane-row" :class="wrapperKlass">
    <span v-if="isOverview" class="font-medium text-[#3a3b3d]">{{
      label
    }}</span>
    <slot v-else name="label">
      <div v-if="label" class="w-[45%] !text-[13px] text-ellipsis">
        <CustomTooltip :content="tooltipContent" :is-always-show="isAlwaysShow">
          {{ label }}
        </CustomTooltip>
      </div>
    </slot>
    <slot
      name="value"
      :klass="klass"
      :over-view-klass="overViewKlass"
      :overt-text-klass="overTextKlass"
    >
      <div v-if="value" class="w-[55%] pl-2 text-text-base font-normal">
        {{ value }}
      </div>
    </slot>
  </div>
</template>

<script setup lang="ts">
import CustomTooltip from "../common/CustomTooltip.vue";
const props = defineProps({
  label: {
    type: String,
    default: "",
  },
  tooltipContent: {
    type: String,
    default: "",
  },
  isAlwaysShow: {
    type: Boolean,
    default: false,
  },
  value: {
    type: [String, Number],
    default: "",
  },
  isOverview: {
    type: Boolean,
    default: false,
  },
});
const wrapperKlass = computed(() => {
  return props.isOverview ? "flex-col items-start" : "items-center";
});
const klass = "w-[55%] pl-2 text-text-base font-normal";
const overViewKlass =
  "preserve-line-breaks tracking-[0.25px] w-full text-[13px] mt-1.5 break-all";
const overTextKlass = "font-size-base text-text-base w-full mt-1.5";
</script>

<style scoped></style>
