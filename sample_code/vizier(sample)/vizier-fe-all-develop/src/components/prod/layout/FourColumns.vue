<template>
  <div :class="klass">
    <slot></slot>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  isFull: {
    type: Boolean,
    default: false,
  },
});
const klass = `w-full h-full grid grid-cols-${
  props.isFull ? 1 : 4
} grid-rows-1 gap-4`;
</script>

<style scoped></style>
