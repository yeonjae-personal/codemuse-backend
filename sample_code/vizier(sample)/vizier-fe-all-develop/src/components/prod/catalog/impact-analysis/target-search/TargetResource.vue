<template>
  <BaseSelectScroll
    v-model="selectValue"
    :options="detailList"
    :placeholder="$t('product_platform.type')"
    :height="48"
    default-item-select-all
    styles="w-full"
  />
</template>

<script setup lang="ts">
const emit = defineEmits(["update:modelValue"]);
const props = defineProps({
  modalValue: {
    type: String,
    default: () => {},
  },
  detailList: {
    type: Array,
    default: () => [],
  },
});

const selectValue = computed({
  get() {
    return props.modalValue;
  },
  set(val) {
    emit("update:modelValue", val);
  },
});
</script>
