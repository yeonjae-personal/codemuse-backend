<template>
  <v-select
    v-model="selectedValue"
    :items="props.items"
    :item-title="props.itemTitle"
    :item-value="props.itemValue"
    :label="props?.label"
    :density="props?.density"
    rounded="4"
    dense
    :height="props.height"
    append-inner-icon="mdi-chevron-down"
    :placeholder="props.placeholder"
    :menu-props="menuProps"
    class="base-select rounded-lg custom-text-field"
    :required="props.required"
    :hide-details="props.hideDetails"
    :rules="computedRules"
    @update:model-value="handleChangeInput"
  >
    >
    <template #item="{ item, props: itemProps }">
      <v-list-item
        v-bind="itemProps"
        density="compact"
        :height="listItemHeight"
        base-color="#6B6D70"
        color="#BA1642"
      >
        <template #title>
          <span :style="{ 'font-size': itemListFontSize }">{{
            item.title
          }}</span>
        </template>
      </v-list-item>
    </template>
  </v-select>
</template>

<script setup lang="ts">
const props = defineProps({
  modelValue: {
    type: [String, Number],
    default: null,
  },
  rules: {
    type: Array,
    default: () => [],
  },
  valueText: {
    type: [String, Number],
    default: null,
  },
  items: {
    type: Array,
    default: () => [],
  },
  label: {
    type: String,
    default: "",
  },
  itemTitle: {
    type: String,
    default: "name",
  },
  itemValue: {
    type: String,
    default: "value",
  },
  placeholder: {
    type: String,
    default: "",
  },
  density: {
    type: String,
    default: "comfortable", // comfortable, compact, cozy
  },
  required: {
    type: Boolean,
    default: false,
  },
  showRequiredIcon: {
    type: Boolean,
    default: false,
  },
  height: {
    type: String,
    default: "48px",
  },
  listItemHeight: {
    type: String,
    default: "40px",
  },
  itemListFontSize: {
    type: String,
    default: "13px",
  },
  menuProps: {
    type: Object,
    default: () => ({
      contentClass: "base-select-content",
    }),
  },
  hideDetails: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits([
  "update:modelValue",
  "update:valueText",
  "handleChangeInput",
]);

const selectedValue = computed({
  // getter
  get() {
    return props.modelValue || null;
  },
  // setter
  set(newValue) {
    const selectedItem = props.items.filter(
      (item: any) => item.value === newValue
    )[0] as any;
    emit("update:modelValue", newValue);
    emit("update:valueText", selectedItem?.name || null);
  },
});

const handleChangeInput = () => {
  emit("handleChangeInput");
};
const computedRules = computed(() => {
  let baseRules = props.rules;
  if (props.required) {
    baseRules = [...baseRules, (vl: any) => !!vl || "This field is required"];
  }
  return baseRules;
});
</script>

<style lang="scss" scoped>
.base-select {
  background-color: white !important;
}

:deep().v-select__menu-icon {
  display: none;
}

:deep().v-field__overlay {
  background-color: white;
}

:deep().v-field__outline {
  display: none !important;
}

:deep()label.v-label.v-field-label.v-field-label--floating {
  font-size: 10px !important;
  color: #6b6d70 !important;
}
:deep().v-select__selection-text {
  font-size: 13px;
}
:deep().v-list-item {
  margin-bottom: 0 !important;
}
:deep().v-field__input {
  font-size: 13px;
}
:deep(

  ).v-list-item.v-list-item--link.v-theme--light.v-list-item--density-default.v-list-item--one-line.rounded-0.v-list-item--variant-text {
  margin-bottom: 0 !important;
  font-size: 10px !important;
}

:deep(.v-label) {
  font-size: 13px !important;
  /** color: #bdc1c7 !important; **/
}
:deep(.v-select__selection-text) {
  color: #3a3b3d !important;
}

.custom-text-field :deep() .v-input__control {
  box-shadow: none !important;
  border: 1px solid #dce0e5;
  height: 48px;
  border-radius: 8px;
}

.custom-text-field :deep(.v-field--error) {
  color: #d9325a;
  border: 1px solid #d9325a;
  border-radius: 8px;
}

.custom-text-field :deep(.v-input__details) {
  padding-inline: 0px;
}
</style>
