<template>
  <v-select
    ref="selectRef"
    v-model="selectedValue"
    v-model:menu="menuOpen"
    :items="computedItems"
    :item-title="props.itemTitle"
    :item-value="props.itemValue"
    :label="props?.label"
    :density="props?.density"
    rounded="4"
    dense
    :height="props.height"
    :width="props.width"
    :placeholder="props.placeholder"
    :menu-props="menuProps"
    class="base-select border border-[#E5E7EB] rounded-lg"
    :class="[styles, { required: props.required, disabled: props.disabled }]"
    :required="props.required"
    :hide-details="props.hideDetails"
    :error-messages="props.errorMessages"
    :rules="computedRules"
    :validate-on="props.validateMode"
    :show-option-null="showOptionNull"
    :disabled="disabled"
    @blur="handleBlur"
    @update:model-value="handleChangeInput"
  >
    <template #prepend-item>
      <slot v-if="slots.prependItem" name="prependItem"></slot>
    </template>

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
    <template v-if="required && showRequiredIcon" #prepend-inner>
      <RequiredIcon class="require-icon" />
    </template>
    <template #append-inner>
      <ChevronDown
        size="18"
        class="transition duration-150 ease-out"
        :class="{ 'rotate-180': menuOpen }"
      />
    </template>
    <template #no-data>
      <div class="no-data">{{ $t("product_platform.noDataAvailable") }}</div>
    </template>
  </v-select>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { SPACE, ALL } from "@/constants/";

const slots = useSlots();

const { t, locale } = useI18n();
const props = defineProps({
  attr: {
    type: String,
    default: "",
  },
  modelValue: {
    type: [String, Number],
    default: "",
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
  width: {
    type: String,
    default: "",
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
  styles: {
    type: String,
    default: "",
  },
  errorMessages: {
    type: [String, Array],
    default: () => [],
  },
  validateMode: {
    type: String,
    default: "blur",
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  defaultItemSelectAll: {
    type: Boolean,
    default: true,
  },
  showOptionNull: {
    type: Boolean,
    default: false,
  },
});

const selectRef = ref();
const isFocused = ref<boolean>(false);
const menuOpen = ref<boolean>(false);

const opacityHideDetail = computed(() => {
  return props.hideDetails ? 0 : 1;
});

const emit = defineEmits([
  "update:modelValue",
  "update:valueText",
  "handleChangeInput",
]);

const validateDetailPosition = computed(() => {
  const heightNum = Number(props.height.slice(0, -2)) + 5;
  return heightNum + "px";
});

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
    emit("update:valueText", selectedItem?.name || null);
    emit("update:modelValue", newValue === SPACE ? " " : newValue);
  },
});

const handleBlur = (): void => {
  isFocused.value = true;
};

const handleChangeInput = (value: string) => {
  emit("handleChangeInput", value);
};

const computedRules = computed(() => {
  let baseRules = props.rules;
  if (props.required) {
    baseRules = [
      ...baseRules,
      (vl: any) => !!vl || t("product_platform.validate.requiredFieldInput"),
    ];
  }
  return baseRules;
});

const computedItems = computed(() => {
  if (!props.required && props.defaultItemSelectAll) {
    const defaultItem = {
      [props.itemTitle]: ALL,
      [props.itemValue]: SPACE,
    };
    return [defaultItem, ...props.items];
  }

  if (props.showOptionNull && !props.required) {
    const defaultItem = {
      [props.itemTitle]: SPACE,
      [props.itemValue]: null,
    };
    return [defaultItem, ...props.items];
  }

  return props.items;
});

onMounted(() => {
  if (!props.required && !props.modelValue && props.defaultItemSelectAll) {
    selectedValue.value = SPACE;
  } else if (!props.required && !props.modelValue && props.showOptionNull) {
    selectedValue.value = null;
  }
});

watch(locale, () => {
  nextTick(() => {
    if (isFocused.value && selectRef.value) {
      selectRef.value.validate();
    }
  });
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
    top: -12px;
    background: var(--bg-inverse-bg-darker, #525457);
    transform: rotate(45deg);
    transition: 0.3s;
  }
}

.no-data {
  padding: 4px 16px;
  min-height: 48px;
  display: flex;
  align-items: center;
  font-size: 13px;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  font-family: "Noto Sans KR";
}

:deep().v-select__menu-icon {
  display: none;
}

:deep().v-field__overlay {
  background-color: white !important;
}

:deep().v-field__outline {
  display: none !important;
}

:deep()label.v-label.v-field-label.v-field-label--floating {
  font-size: 10px !important;
  color: #6b6d70 !important;
}
:deep().v-select__selection-text {
  font-size: 12px;
  color: #3a3b3d !important;
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
.v-input--error {
  border-width: 1px !important;
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
  bottom: v-bind(validateDetailPosition);
  right: 0px;
  background: var(--bg-inverse-bg-darker, #525457);
  border-radius: 4px;
  padding: 0px;
  box-shadow: 0px 2px 20px 0px #0000001a;
  transition: 0.3s;
  width: max-content;
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
  margin-right: 0px;
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

:deep(.v-field__field) {
  min-width: 40px;
  max-width: calc(100% - 15px);
}
:deep().v-field__field--appended {
  padding-inline-end: 0px !important;
}
.disabled {
  pointer-events: none;
  color: #bfcbd9;
  cursor: not-allowed;
  background-image: none;
  background-color: #eef1f6;
  border-color: #d1dbe5;
}
</style>
