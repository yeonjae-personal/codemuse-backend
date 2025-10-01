<template>
  <VueDatePicker
    ref="datepicker"
    v-model="date"
    :teleport="teleportValue"
    :enable-time-picker="enableTimePicker"
    :time-picker-inline="enableTimePicker"
    :readonly="readonly"
    :disabled="disabled"
    :placeholder="placeholder"
    :format="currentFormat"
    :min-date="minDateFormat"
    :max-date="maxDate"
    :clearable="clearable"
    :data-content="errorMessage"
    :text-input="inputMode"
    :input-class-name="`!rounded-[8px] !pl-3 !pr-10 !py-[12.5px] !text-[13px] !leading-[19.5px] !placeholder:text-[#BDC1C7] border-base 
      ${isInvalid && '!border-[1px] !border-[#d9325a]'}
      `"
    :auto-apply="autoApply"
    :class="[
      styles,
      {
        '!border-l-[2px] !border-l-[#d9325a] rounded-[8px]':
          required && !isInvalid,
        'field-error': isInvalid,
      },
    ]"
    :start-time="startTime"
    class="custom-date-picker"
    @closed="handleClosed"
    @open="() => (isSelected = false)"
    @cleared="handleClear"
    @invalid-select="handleInvalid"
    @date-update="handleDateCalendarUpdate"
    @internal-model-change="handleInternalValue"
  >
    <template #action-buttons>
      <button
        type="button"
        class="dp__action_button dp__action_cancel"
        @click.stop="() => datepicker?.closeMenu()"
      >
        Cancel
      </button>
      <button
        type="button"
        class="dp__action_button dp__action_select"
        data-test="select-button"
        :disabled="!isDateValid"
        @click="handleSelectDate"
      >
        Select
      </button>
    </template>
  </VueDatePicker>
</template>

<script setup lang="ts">
import { formatDate } from "@/utils/format-data";
import { DATE_FORMAT } from "@/constants/";
import moment from "moment-timezone";
import { useI18n } from "vue-i18n";
import { DatePickerInstance } from "@vuepic/vue-datepicker";
import { useSnackbarStore } from "@/store";

const props = defineProps({
  // eslint-disable-next-line vue/require-default-prop
  attr: {
    type: String,
    default: "",
  },
  modelValue: {
    type: String,
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
  enableTimePicker: {
    type: Boolean,
    default: false,
  },
  placeholder: {
    type: String,
    default: "",
  },
  format: {
    type: String,
    default: DATE_FORMAT.DEFAULT_DATE_FORMAT_DATEPICKER,
  },
  formatEnableTime: {
    type: String,
    default: DATE_FORMAT.DEFAULT_DATE_FORMAT_DATEPICKER_TIME,
  },
  formatOutput: {
    type: String,
    default: DATE_FORMAT.DEFAULT_DATE_FORMAT_MOMENT_OUTPUT,
  },
  minDate: {
    type: String,
    default: null,
  },
  maxDate: {
    type: String,
    default: null,
  },
  required: {
    type: Boolean,
    default: false,
  },
  clearable: {
    type: Boolean,
    default: true,
  },
  styles: {
    type: String,
    default: "",
  },
  teleportValue: {
    type: Boolean,
    default: true,
  },
  inputMode: {
    type: Boolean,
    default: false,
  },
  autoApply: {
    type: Boolean,
    default: false,
  },
});

const snackbarStore = useSnackbarStore();
const isInvalid = ref(false);
const startTime = ref({ hours: 0, minutes: 0 });
const datepicker = ref<DatePickerInstance>(null);
const isDateValid = ref(true);
const isSelected = ref(false);
const date = ref<any>(props.modelValue);
const emit = defineEmits(["update:modelValue", "error"]);
const { t } = useI18n();

const errorMessage = computed<string>(() =>
  t("product_platform.validate.requiredFieldInput")
);

const minDateFormat = computed(() => {
  return moment(props.minDate, DATE_FORMAT.DATE_TYPE).format(
    DATE_FORMAT.DATE_TIME_FORMAT_WITHOUT_SECONDS
  );
});

const currentFormat = computed(() => {
  return props.enableTimePicker ? props.formatEnableTime : props.format;
});

const handleSelectDate = () => {
  if (date.value) {
    emit(
      "update:modelValue",
      formatDate(
        date.value,
        DATE_FORMAT.DATE_TIME_FORMAT_WITHOUT_SECONDS,
        DATE_FORMAT.DEFAULT_DATE_FORMAT_MOMENT_OUTPUT
      )
    );
    isSelected.value = true;
    datepicker?.value?.toggleMenu();
  } else {
    snackbarStore.showSnackbar(t("common.invalid_datetime"), "error");
  }
};

const checkDateValid = (value) => {
  if (!value) {
    isDateValid.value = false;
    snackbarStore.showSnackbar(t("common.invalid_datetime"), "error");
  } else {
    if (minDateFormat.value && value < new Date(minDateFormat.value)) {
      isDateValid.value = false;
      return false;
    }
    if (props.maxDate && value > new Date(props.maxDate)) {
      isDateValid.value = false;
      return false;
    }
    isDateValid.value = true;
  }
  return isDateValid.value;
};

const handleDateCalendarUpdate = (modelData) => {
  if (modelData === null) {
    return;
  }
  const formatValue = moment(modelData)
    .startOf("day")
    .format(DATE_FORMAT.DATE_TIME_FORMAT_WITHOUT_SECONDS);
  checkDateValid(modelData);
  date.value = formatValue;
};

const handleInvalid = () => {
  snackbarStore.showSnackbar(t("common.invalid_datetime"), "error");
};

const handleInternalValue = (value) => {
  if (value === null) {
    return;
  }
  const formatValue = moment(value).format(
    DATE_FORMAT.DATE_TIME_FORMAT_WITHOUT_SECONDS
  );
  const valid = checkDateValid(value);
  if (valid) {
    date.value = formatValue;
  }
};

const handleClear = async () => {
  date.value = null;
  emit("update:modelValue", date.value);
  await closeCalendalModal();
};

const handleClosed = async () => {
  if (!isSelected.value) {
    datepicker.value?.clearValue();
    date.value = props.modelValue;
    emit("update:modelValue", date.value);
  }
  await closeCalendalModal();
};

const closeCalendalModal = async () => {
  if (!props.required) return;
  await validation();
};

const validation = async () => {
  setTimeout(() => {
    isInvalid.value = props.required && !date.value;
    emit("error", isInvalid.value);
  }, 0);
  return isInvalid.value;
};

const resetValidation = () => {
  isInvalid.value = false;
};

watch(
  () => props.minDate,
  (newVal) => {
    if (
      newVal &&
      moment(props.modelValue, DATE_FORMAT.DATE_TYPE).diff(
        moment(newVal, [
          DATE_FORMAT.DATE_TYPE,
          DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE,
        ])
      ) < 0
    ) {
      date.value = formatDate(
        newVal,
        DATE_FORMAT.DATE_TYPE,
        DATE_FORMAT.DEFAULT_DATE_FORMAT_MOMENT_OUTPUT
      ) as any;
    }
  }
);

watch(
  () => props.modelValue,
  (val) => {
    date.value = val;
  }
);

defineExpose({ validation, resetValidation });
</script>

<style lang="scss" scoped>
:deep(.dp__input_icon) {
  inset-inline-start: unset;
  right: 0;
  padding-left: 0px;
}
:deep(.dp__clear_icon) {
  padding-right: 0px;
  right: 18%;
}
.VueDatePicker:hover::before {
  content: attr(title);
  position: absolute;
  background-color: #fff;
  border: 1px solid #ddd;
  padding: 5px;
  z-index: 1;
}

.custom-date-picker {
  font-family: "Noto Sans KR", sans-serif !important;
  position: relative;
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
  &::after {
    content: attr(data-content);
    min-width: 100px;
    height: fit-content;
    display: none;
    position: absolute;
    bottom: 36px;
    right: 0px;
    background: var(--bg-inverse-bg-darker, #525457);
    border-radius: 4px;
    box-shadow: 0px 2px 20px 0px #0000001a;
    transition: 0.3s;
    color: white;
    font-size: 12px;
    line-height: 17px;
    padding: 6px 8px;
    width: max-content;
    z-index: 3;
  }
}
.field-error {
  &:hover {
    &::before {
      opacity: 1;
      width: 10px;
      height: 10px;
    }
    &::after {
      display: block;
    }
  }
}

:deep(.dp__input) {
  color: #6d6b70;
}

.custom-date-picker :deep().dp__pointer {
  height: 34px !important;
  font-weight: 400 !important;
}
:deep().dp__input {
  height: 34px !important;
  font-weight: 400 !important;
  font-size: 13px !important;
  color: #3a3b3d !important;
  font-family: "Noto Sans KR", sans-serif !important;
}
</style>
