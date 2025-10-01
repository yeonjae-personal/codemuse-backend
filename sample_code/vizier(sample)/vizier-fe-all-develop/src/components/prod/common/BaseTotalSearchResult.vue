<template>
  <div
    :class="[
      'font-size-base text-text-lighter text-right font-medium',
      className,
    ]"
  >
    {{ isCustom ? textCustom : $t("product_platform.search_result") }}:
    <span class="text-text-base ml-1">{{ props.totalSearch }}</span>
    <!-- TODO: -->
    <!-- <span class="font-normal">{{ ` of ${props.totalItems}` }}</span> -->
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  totalSearch: {
    type: Number,
    default: 0,
  },
  totalItems: {
    type: Number,
    default: 0,
  },
  className: {
    type: String,
    default: "",
  },
  isCustom: {
    type: Boolean,
    default: false,
  },
  textCustom: {
    type: String,
    default: "",
  },
});
</script>
