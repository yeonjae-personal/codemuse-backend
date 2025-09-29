<template>
  <v-textarea
    :ref="props.refName"
    v-model="valueInput"
    :rules="computedRules"
    :label="props.label"
    :placeholder="props.placeholder"
    :required="props.required"
    :type="props.type"
    :error-messages="props.errorMessages"
    :counter="props.counter"
    :maxlength="props.maxlength"
    :minlength="props.minlength"
    :disabled="props.disabled"
    :readonly="props.readonly"
    variant="solo"
    bg-color="#fff"
    no-resize
    class="custom-text-field"
    :class="[props.styles, { required: props.required }]"
    :hide-details="props.hideDetails"
    :validate-on="props.validateMode"
  >
    <template #append-inner>
      <slot name="append-inner"></slot>
    </template>
    <template v-if="required && showRequiredIcon" #prepend-inner>
      <RequiredIcon class="require-icon" />
    </template>
  </v-textarea>
</template>

<script setup lang="ts">
import RequiredIcon from "../icons/RequiredIcon.vue";

const props = defineProps({
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
  let baseRules = props.rules;
  if (props.required) {
    baseRules = [...baseRules, (vl: any) => !!vl || ""];
  }
  return baseRules;
});
</script>

<style lang="scss" scoped>
.custom-text-field {
  position: relative;
  :deep(.v-input__control) {
    box-shadow: none !important;
    border: 1px solid #dce0e5;
  }
  &::before {
    content: "";
    opacity: 0;
    z-index: 2;
    position: absolute;
    right: 8px;
    top: -10px;
    background: var(--bg-inverse-bg-darker, #525457);
    width: 10px;
    height: 10px;
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
:deep().v-field {
  border-radius: 8px;
  box-shadow: none !important;
}
:deep().v-field__field {
  align-items: center;
  border: none;
}
:deep().v-field__input {
  width: 170px;
  font-family: "Noto Sans KR", sans-serif !important;
  font-size: 13px;
  color: #3a3b3d;
}

:deep().v-field--error {
  border: 1px solid #d9325a;
}

.v-field-label {
  font-size: 13px;
  margin-bottom: 8px;
}

.v-field-label--floating {
  margin-bottom: 4px;
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

.required :deep(.v-field) {
  border-left: 2px solid #d9325a;
}
:deep(.v-field.v-field--error) {
  border: 1px solid #d9325a;
}

.required.v-input--error {
  &:hover {
    :deep(.v-input__details) {
      opacity: 1;
    }
    &::before {
      opacity: 1;
    }
  }
}
:deep().v-input__details {
  min-width: 100px;
  opacity: 0;
  position: absolute;
  bottom: 127px;
  right: 0px;
  background: var(--bg-inverse-bg-darker, #525457);
  border-radius: 4px;
  padding: 6px 8px;
  box-shadow: 0px 2px 20px 0px #0000001a;
  transition: 0.3s;
}

:deep(.v-input__details .v-messages) {
  color: white !important;
}

:deep(.v-input__details .v-messages) {
  color: white !important;
}

:deep(.v-input__details .v-counter) {
  color: white !important;
  padding-left: 8px;
}

:deep().v-field--error:not(.v-field--disabled) .v-label.v-field-label {
  color: #bdc1c7;
}
</style>
