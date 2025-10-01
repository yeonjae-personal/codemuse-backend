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
  rules: {
    type: Array as PropType<Array<(v: any) => string | boolean>>,
    default: () => [],
  },
  label: {
    type: String,
    default: "",
  },
  placeholder: {
    type: String,
    default: "",
  },
  hint: {
    type: String,
    default: "",
  },
  type: {
    type: String,
    default: "input",
  },
  variant: {
    type: [
      String as () =>
        | "underlined"
        | "filled"
        | "outlined"
        | "plain"
        | "solo"
        | "solo-inverted"
        | "solo-filled"
        | undefined,
    ],
    default: "underlined",
  },
  color: {
    type: String,
    default: "",
  },
  specialAction: {
    type: [
      String as () =>
        | "toUpperCase"
        | "toLowerCase"
        | "",
      
    ],
    default: "",
  }
});

const internalValue = ref(props.model);

const emit = defineEmits(["update:model"]);
const emitUpdateModel = () => {
  emit("update:model", internalValue.value);
};

watch(
  () => internalValue.value,
  (newVal: string | number) => {
    switch (props.specialAction) {
      case "toUpperCase":
        toUpperCase(newVal as string);
        break;
      case "toLowerCase":
        toLowerCase(newVal as string);
        break;
    
      default:
        break;
    }
  },
);

const toUpperCase = (val: string) => {
  internalValue.value = val.toUpperCase();
}
const toLowerCase = (val:string) => {
  internalValue.value = val.toLowerCase();
}
</script>

<template>
  <!-- :counter="counter" -->
  <v-text-field
    v-model="internalValue"
    :rules="rules"
    :label="label"
    :color="color"
    :placeholder="placeholder"
    :type="type"
    :hint="hint"
    :variant="props.variant"
    @update:model-value="emitUpdateModel"
  ></v-text-field>
</template>

<style scoped></style>
