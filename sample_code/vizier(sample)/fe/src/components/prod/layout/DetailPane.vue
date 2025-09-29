<template>
  <div
    class="bg-lighter p-3 text-text-lighter font-medium font-size-base flex flex-col gap-2 detail-pane"
    :class="klass"
  >
    <slot></slot>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  isNotRoundedBottom: {
    type: Boolean,
    default: false,
  },
  isNotRoundedTop: {
    type: Boolean,
    default: false,
  },
  isNotRounded: {
    type: Boolean,
    default: false,
  },
});
const klass = computed(() => {
  return props.isNotRounded
    ? ""
    : props.isNotRoundedBottom
      ? "rounded-t-lg"
      : props.isNotRoundedTop
        ? "rounded-b-lg"
        : "rounded-lg";
});
</script>

<style scoped></style>
