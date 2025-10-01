<template>
  <v-text-field
    ref="inputRef"
    v-model="valueInput"
    :rules="computedRules"
    :label="props.label"
    :placeholder="props.placeholder"
    :required="props.required"
    :type="props.type"
    :error-messages="props.errorMessages"
    :counter="props.counter || false"
    :maxlength="props.maxlength"
    :minlength="props.minlength"
    :disabled="props.disabled"
    :readonly="props.readonly"
    variant="solo"
    bg-color="#fff"
    class="custom-text-field"
    :class="[
      props.styles,
      { required: props.required, counter: props.counter },
    ]"
    :hide-details="props.hideDetails"
    :validate-on="props.validateMode"
    :autofocus="autofocus"
    @blur="handleBlur"
    @keyup.enter="handleKeyupEnter"
  >
    <template v-if="!checkNumberIsInteger(props.maxlength)" #counter>
      <div>{{ showDataCounterDecimal }}</div>
    </template>
    <template #append-inner>
      <slot name="append-inner"></slot>
    </template>
    <template v-if="required && showRequiredIcon" #prepend-inner>
      <RequiredIcon class="require-icon" />
    </template>
  </v-text-field>
</template>

<script setup lang="ts">
import { checkNumberIsInteger } from "@/utils/extend-utils";
import { useI18n } from "vue-i18n";
import RequiredIcon from "../icons/RequiredIcon.vue";

const inputContain = ref<HTMLDivElement | null>(null);

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
  label: {
    type: String,
    default: "",
  },
  placeholder: {
    type: String,
    default: "",
  },
  required: {
    type: Boolean,
    default: false,
  },
  type: {
    type: String,
    default: "text",
  },
  errorMessages: {
    type: [String, Array],
    default: () => [],
  },
  counter: {
    type: [Boolean, Number],
    default: false,
  },
  maxlength: {
    type: [Number, String],
    default: null,
  },
  minlength: {
    type: [Number, String],
    default: null,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  readonly: {
    type: Boolean,
    default: false,
  },
  refName: {
    type: String,
    default: "input",
  },
  showRequiredIcon: {
    type: Boolean,
    default: false,
  },
  styles: {
    type: [String, Array],
    default: "",
  },
  hideDetails: {
    type: Boolean,
    default: false,
  },
  validateMode: {
    type: String,
    default: "blur",
  },
  inputWidth: {
    type: String,
    default: "auto",
  },
  autofocus: {
    type: Boolean,
    default: false,
  },
});

const inputRef = ref();
const isFocused = ref<boolean>(false);

const handleValidate = (): void => {
  nextTick(() => {
    if (inputRef.value) inputRef.value.validate();
  });
};

defineExpose({ inputElement: inputRef, handleValidate });

const emit = defineEmits(["update:modelValue", "blur", "enter"]);

const showDataCounterDecimal = computed(() => {
  let fistNum = String(props.maxlength)?.split(".")[0]; //10
  let secondNum = String(props.maxlength)?.split(".")[1]; //2
  let valueDefault = `${Number(fistNum) - Number(secondNum) - 1}.${secondNum}`;

  if (!valueInput.value) {
    // return `${Number(fistNum) - 1}.${secondNum} / ${valueDefault}`;
    return `0.0 / ${valueDefault}`;
  }

  if (String(valueInput.value).includes(".")) {
    let fistVal = String(valueInput.value)?.split(".")[0];
    let secondVal = String(valueInput.value)?.split(".")[1];

    if (secondVal) {
      return `${fistVal.length}.${secondVal.length} / ${valueDefault}`;
    }
    return `${String(valueInput.value).length - 1}.0 / ${valueDefault}`;
  }

  return `${String(valueInput.value).length}.0 / ${valueDefault}`;
});

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

const validateDetailPosition = computed(() => {
  return inputContain.value?.clientHeight
    ? inputContain.value?.clientHeight + 3 + "px"
    : "37px";
});

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

const handleBlur = (): void => {
  isFocused.value = true;
  emit("blur");
};

const handleKeyupEnter = () => {
  emit("enter");
};

watch(locale, () => {
  nextTick(() => {
    if (isFocused.value && inputRef.value) {
      inputRef.value.validate();
    }
  });
});
</script>

<style scoped lang="scss">
.custom-text-field {
  position: relative;
  border-radius: 8px;
  border: 1px solid #dce0e5;
  :deep(.v-input__control) {
    box-shadow: none !important;
    height: 48px;
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

  &.is-active {
    border-color: #d9325a !important;
  }
}

:deep(.v-input__control) {
  border-radius: 8px;
}
:deep().v-input {
  width: v-bind(inputWidth);
}
:deep().v-field {
  height: 46px;
  border-radius: 8px;
  box-shadow: none !important;
}
:deep().v-field__field {
  height: 48px;
  align-items: center;
  border: none;
}
:deep().v-field__input {
  width: 170px;
  padding: 12px 13.5px;
  font-family: "Noto Sans KR", sans-serif !important;
  font-size: 13px;
  color: #3a3b3d;
}

.input-form:deep().v-field-label {
  font-size: 13px;
  color: #bdc1c7;
}
.input-form:deep().v-field__input {
  padding: 20px 16px 1px 16px;
}
.input-form:deep().v-field-label--floating {
  font-size: 9px !important;
}

.input-form:deep().v-field--disabled {
  opacity: 1 !important;
  background-color: #f0f2f5 !important;
}

.input-form:deep(.v-field--disabled .v-field-label--floating) {
  color: #6b6d70 !important;
}

.input-form:deep(.v-text-field .v-input__details) {
  padding-inline: 0 !important;
}

.input-search:deep().v-input__details {
  display: none;
}
.input-edit :deep() .v-input__control {
  height: 32px;
}
.input-edit:deep().v-field {
  height: 30px;
  border-radius: 8px;
  box-shadow: none !important;
}
.input-edit:deep().v-field__field {
  height: 32px;
  align-items: center;
  border: none;
}

/* :deep(.v-field-label)::after {
  content: " *";
  color: red;
  font-weight: bold;
  opacity: v-bind(addAsterisk);
  top: 10px;
} */

.v-field-label {
  font-size: 13px;
  margin-bottom: 8px; /* Adjust this value to control spacing */
}

.v-field-label--floating {
  margin-bottom: 4px; /* Adjust this value to control spacing for floating label */
}

.required {
  border-left-width: 0px !important;
  :deep(.v-input__control) {
    border-left-width: 2px;
    border-left-color: #d9325a !important;
  }
  :deep().v-field {
    border-width: 0px !important;
  }
}

.v-input--error {
  border-width: 1px !important;
  border-color: #d9325a !important;
  :deep(.v-input__control) {
    border-width: 0px !important;
  }
}
.counter,
.v-input--error {
  &:hover {
    :deep(.v-input__details) {
      opacity: 1;
      height: auto;
      min-height: 14px;
      padding: 6px 15px;
      > div {
        height: auto;
        min-height: 14px;
      }
    }
    &::before {
      opacity: 1;
      width: 10px;
      height: 10px;
    }
  }
}
:deep().v-input__details {
  min-width: 80px;
  height: 0px;
  min-height: 0px;
  width: max-content !important;
  opacity: 0;
  position: absolute;
  bottom: v-bind(validateDetailPosition);
  right: 0px;
  background: var(--bg-inverse-bg-darker, #525457);
  border-radius: 4px;
  padding: 0px;
  box-shadow: 0px 2px 20px 0px #0000001a;
  transition: 0.3s;
  > div {
    min-height: 0px;
    height: 0px;
    color: white !important;
    transition: 0.2s;
  }
  > .v-counter {
    padding-left: 8px;
    display: inline-block !important;
    height: auto !important;
  }
}

:deep().v-field--error:not(.v-field--disabled) .v-label.v-field-label {
  color: #bdc1c7;
}

.catalog-input {
  :deep().v-input__details {
    bottom: 34px;
  }

  :deep(.v-field__append-inner) {
    margin-right: 0;
  }
}
</style>
