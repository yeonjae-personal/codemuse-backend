<template>
  <v-text-field
    v-model="valueInput"
    :density="density"
    :label="$t(`product_platform.${label}`)"
    :variant="variant"
    :hide-details="hideDetails"
    :single-line="singleLine"
    :class="[className, 'border border-[#E5E7EB] text-sm rounded-lg']"
    :rounded="rounded"
    width="100%"
    :height="height"
    @keyup.enter="handleSearch"
    @click:append-inner="handleSearch"
  >
  </v-text-field>
</template>

<script setup lang="ts">
const props = defineProps({
  modelValue: {
    type: [String, Number],
    default: "",
  },
  density: {
    type: String,
    default: "compact",
  },
  label: {
    type: String,
    default: "Search",
  },
  variant: {
    type: String,
    default: "solo",
  },
  hideDetails: {
    type: Boolean,
    default: true,
  },
  singleLine: {
    type: Boolean,
    default: true,
  },
  rounded: {
    type: String,
    default: "4",
  },
  appendInnerIcon: {
    type: String,
    default: "mdi-magnify",
  },
  className: {
    type: String,
    default: "",
  },
  height: {
    type: String,
    default: "48px",
  },
});

const emit = defineEmits(["update:modelValue", "handle-search"]);

const valueInput = computed({
  // getter
  get() {
    return props.modelValue;
  },
  // setter
  set(newValue) {
    emit("update:modelValue", newValue);
  },
});

const handleSearch = () => {
  emit("handle-search");
};
</script>

<style scoped>
:deep(.v-label) {
  font-size: 13px !important;
  color: #bdc1c7 !important;
}
:deep().v-field {
  border-radius: 8px;
  box-shadow: none !important;
}
:deep().v-input__append {
  background-color: white !important;
  margin: 0;
  border-left: 0;
  border-radius: 0 8px 8px 0;
}
:deep().v-field__input {
  font-size: 13px;
}
</style>
