<template>
  <div>
    <v-text-field
      ref="selectRef"
      v-model="valueInput"
      :rules="computedRules"
      :label="label"
      :placeholder="placeholder"
      :required="rules?.required"
      :type="type"
      :error-messages="errorMessages"
      :counter="rules?.maxLength || false"
      :maxlength="maxlength"
      :minlength="minlength"
      :disabled="disabled"
      :readonly="readonly"
      variant="solo"
      bg-color="#fff"
      class="custom-text-field"
      :class="[
        styles,
        { required: rules?.required, showDetail: computedRules.length > 0 },
      ]"
      :hide-details="hideDetails"
      :validate-on="validateMode"
    >
      <template #append-inner>
        <slot name="append-inner"></slot>
      </template>
    </v-text-field>
  </div>
</template>

<script setup lang="ts">
import { useInputValidation } from "@/composables/useInputValidation";

const selectRef = ref<HTMLSelectElement | null>(null);
const props = defineProps({
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
    default: "",
  },
  rules: {
    type: Object,
    default: () => ({}),
  },
  label: {
    type: String,
    default: "",
  },
  placeholder: {
    type: String,
    default: "",
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
  styles: {
    type: String,
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
});

const emit = defineEmits(["update:modelValue"]);

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

const computedRules = computed(() => {
  return useInputValidation(props.rules);
});
</script>

<style scoped lang="scss">
.custom-text-field {
  position: relative;
  border-radius: 8px !important;
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
    top: -10px;
    background: var(--bg-inverse-bg-darker, #525457);
    transform: rotate(45deg);
    transition: 0.3s;
  }
}
:deep(.v-input__control) {
  border-radius: 8px;
}
:deep().v-input {
  width: 170px;
}
:deep().offer-search-long.v-input {
  width: 320px !important;
}

:deep().v-field {
  height: 46px;
  border-radius: 8px;
  box-shadow: none !important;
}
:deep().v-field__field {
  height: 48px;
  align-items: center;
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
  border-radius: 8px;
  box-shadow: none !important;
}
.input-edit:deep().v-field__field {
  align-items: center;
  border: none;
}
.v-field-label {
  font-size: 13px;
  margin-bottom: 8px; /* Adjust this value to control spacing */
}

.v-field-label--floating {
  margin-bottom: 4px; /* Adjust this value to control spacing for floating label */
}

:deep().v-field--error {
  border: 1px solid #d9325a;
}

.required :deep(.v-field) {
  border-left: 2px solid #d9325a !important;
}

.showDetail {
  &:hover {
    :deep() {
      .v-input__details {
        opacity: 1;
        height: auto;
        min-height: 14px;
        padding: 6px 8px;
        > div {
          height: auto;
          min-height: 14px;
        }
      }
    }

    :deep(.v-counter) {
      display: block !important;
    }

    &::before {
      opacity: 1;
      width: 10px;
      height: 10px;
    }
  }
}

.required {
  :deep(.v-input__control) {
    border-width: 0px;
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
      opacity: 1;
      width: 10px;
      height: 10px;
    }
  }
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
