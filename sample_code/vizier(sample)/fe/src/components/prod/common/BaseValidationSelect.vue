<template>
  <v-select
    ref="selectRef"
    v-model="selectedValue"
    :items="computedItems"
    :item-title="itemTitle"
    :item-value="itemValue"
    :label="label"
    :density="density"
    rounded="4"
    dense
    :height="height"
    :placeholder="placeholder"
    :menu-props="menuProps"
    class="base-select border border-[#E5E7EB] rounded-lg"
    :class="{ required: rules?.required }"
    :required="rules?.required"
    :hide-details="hideDetails"
    :error-messages="errorMessages"
    :rules="computedRules"
    :validate-on="validateMode"
    :show-option-null="showOptionNull"
    :disabled="disabled"
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
    <template #append-inner>
      <ChevronDown />
    </template>
  </v-select>
</template>

<script setup lang="ts">
import { useInputValidation } from "@/composables/useInputValidation";
import ChevronDown from "../icons/ChevronDown.vue";

const selectRef = ref<HTMLSelectElement | null>(null);
const props = defineProps({
  disabled: {
    type: Boolean,
    default: false,
  },
  attr: {
    type: String,
    default: "",
  },
  passData: {
    type: Object,
    default: () => ({}),
  },
  showCustomValidate: {
    type: Boolean,
    default: false,
  },
  modelValue: {
    type: [String, Number],
    default: " ",
  },
  rules: {
    type: Object,
    default: () => ({}),
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
    default: false,
  },
  className: {
    type: String,
    default: "base-select border border-[#E5E7EB] rounded-lg",
  },
  errorMessages: {
    type: [String, Array],
    default: () => [],
  },
  validateMode: {
    type: String,
    default: "blur",
  },
  showOptionNull: {
    type: Boolean,
    default: true,
  },
});

const opacityHideDetail = computed(() => {
  return props.hideDetails ? 0 : 1;
});

const computedItems = computed(() => {
  if (props.showOptionNull && !props.rules?.required) {
    const defaultItem = {
      [props.itemTitle]: " ",
      [props.itemValue]: null,
    };
    return [defaultItem, ...props.items];
  }
  return props.items;
});

onMounted(() => {
  if (!props.modelValue && props.showOptionNull && !props.rules?.required) {
    selectedValue.value = null;
  }
});
const emit = defineEmits([
  "update:modelValue",
  "update:valueText",
  "handleChangeInput",
  "open-dialog",
  "close-dialog",
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
  return useInputValidation(props.rules);
});
</script>

<style lang="scss" scoped>
.base-select {
  position: relative;
  background-color: white !important;
  :deep(.v-input__control) {
    border-radius: 8px;
  }
  &::before {
    content: "";
    opacity: 0;
    z-index: 2;
    position: absolute;
    right: 8px;
    top: -9px;
    background: var(--bg-inverse-bg-darker, #525457);
    transform: rotate(45deg);
    transition: 0.3s;
  }
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
  color: #3a3b3d !important;
  font-size: 12px;
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
  color: #bdc1c7 !important;
}

.required {
  border-width: 1px 1px 1px 0px !important;
  :deep().v-field {
    border-width: 0px !important;
  }
  :deep(.v-input__control) {
    border-left-width: 2px;
    border-left-color: #d9325a !important;
  }
}
.required.v-input--error {
  border-width: 1px !important;
  border-left-width: 2px !important;
  border-color: #d9325a !important;
  :deep(.v-input__control) {
    border-width: 0px !important;
  }
  &:hover {
    :deep(.v-input__details) {
      opacity: 1;
      height: auto;
      min-height: 14px;
      padding: 6px 8px;
      > div {
        height: auto;
        min-height: 14px;
      }
    }
    &::before {
      opacity: v-bind(opacityHideDetail);
      width: 10px;
      height: 10px;
    }
  }
}
:deep().v-field {
  border-radius: 8px;
}
:deep().v-field--error.v-field {
  border: 1px solid #d9325a;
}
:deep().v-input__details {
  min-width: 100px;
  height: 0px;
  min-height: 0px;
  opacity: 0;
  position: absolute;
  bottom: 52px;
  right: 0px;
  background: var(--bg-inverse-bg-darker, #525457);
  border-radius: 4px;
  padding: 0px;
  box-shadow: 0px 2px 20px 0px #0000001a;
  transition: 0.3s;
  width: max-content;
  z-index: 3;
  > div {
    min-height: 0px;
    height: 0px;
    color: white !important;
    transition: 0.2s;
  }
  > .v-counter {
    padding-left: 8px;
  }
}
:deep(.v-field__append-inner) {
  margin-right: 10px;
}

.catalog-select {
  :deep().v-input__details {
    bottom: 34px;
  }

  :deep(.v-field__append-inner) {
    margin-right: 0;
  }
}

.catalog-select-filter {
  :deep(.v-field__append-inner) {
    margin-right: 0;
  }
}
.catalog-select-filter-offer {
  &::before {
    top: -5px;
  }
  :deep().v-input__details {
    bottom: 46px;
  }
  :deep(.v-field__append-inner) {
    margin-right: 0;
  }
}

:deep(.v-field__field) {
  min-width: 40px;
  max-width: calc(100% - 15px);
}
</style>
