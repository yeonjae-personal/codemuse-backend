<script setup lang="ts">
const props = defineProps({
  label: {
    type: String,
    default: "",
  },
  variant: {
    type: String as () =>
      | "filled"
      | "outlined"
      | "plain"
      | "underlined"
      | "solo"
      | "solo-inverted"
      | "solo-filled"
      | undefined,
    default: "underlined",
  },
  items: {
    type: Array<any>,
    required: true,
  },
  model: {
    type: Object as PropType<any>,
    required: true,
  },
  itemTitle: {
    type: String,
    default: undefined,
  },
  itemValue: {
    type: String,
    default: undefined,
  },
  isResetValue: {
    type: Boolean,
    default: false,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
});

const internalValue = ref(props.model);

const emit = defineEmits(["update:model"]);
const emitUpdateModel = () => {
  emit("update:model", internalValue.value);
};

watch(
  () => props.isResetValue,
  () => (internalValue.value = null)
);
</script>

<template>
  <v-combobox
    v-model="internalValue"
    :label="props.label"
    :items="props.items"
    :item-title="props.itemTitle"
    :item-value="props.itemValue"
    :variant="props.variant"
    :disabled="props.disabled"
    @update:model-value="emitUpdateModel"
  ></v-combobox>
</template>

<style scoped>
/*  */
</style>
