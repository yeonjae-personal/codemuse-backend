<template>
  <div :class="klass">
    <slot></slot>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  span: {
    type: Number as PropType<1 | 2 | 3 | 4>,
    default: 1,
    validator: (value: number) => value > 0 && value <= 4,
  },
});
const klass = computed(() => {
  return props.span ? `col-span-${props.span}` : "";
});
</script>

<style scoped></style>
