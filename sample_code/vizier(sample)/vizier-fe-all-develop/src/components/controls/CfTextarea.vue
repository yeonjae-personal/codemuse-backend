<script setup lang="ts">
const props = defineProps({
  model: {
    type: [String, Number],
    default: "",
  },
  counter: {
    type: Number,
    default: 0,
  },
  label: {
    type: String,
    default: undefined,
  },
  bgColor: {
    type: String,
    default: undefined,
  },
  color: {
    type: String,
    default: undefined,
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
    default: undefined,
  },
  prependIcon: {
    type: String,
    default: undefined,
  },
  clearable: {
    type: Boolean,
    default: false,
  },
  autoGrow: {
    type: Boolean,
    default: false,
  },
});

const internalValue = ref(props.model);

const emit = defineEmits(["update:model"]);
const emitUpdateModel = () => {
  emit("update:model", internalValue.value);
};
</script>

<template>
  <v-textarea
    v-model="internalValue"
    :counter="props.counter"
    :label="props.label"
    :bg-color="props.bgColor"
    :color="props.color"
    :variant="props.variant"
    :prepend-icon="props.prependIcon"
    :clearable="props.clearable"
    :auto-grow="props.autoGrow"
    @update:model-value="emitUpdateModel"
  ></v-textarea>
</template>

<style scoped></style>
