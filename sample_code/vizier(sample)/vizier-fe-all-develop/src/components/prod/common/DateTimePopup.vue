<script setup lang="ts">
import { formatDate, isExpired } from "@/utils/format-data";
import { DATE_FORMAT } from "@/constants/index";
import moment from "moment-timezone";

const props = defineProps({
  modelValue: {
    type: Object as PropType<{
      startDate: string;
      endDate: string;
    }>,
    default: () => ({}),
  },
  openModel: {
    type: Boolean,
    default: false,
  },
  disabledStartDate: {
    type: Boolean,
    default: false,
  },
  modalTitle: {
    type: String,
    default: "Add",
  },
  textBtnCancel: {
    type: String,
    default: "Cancel",
  },
  textBtnAdd: {
    type: String,
    default: "Save",
  },
  minStartDate: {
    type: String,
    default: "",
  },
  minEndDate: {
    type: String,
    default: null,
  },
  requiredStartDate: {
    type: Boolean,
    default: false,
  },
  requiredEndDate: {
    type: Boolean,
    default: false,
  },
  enableTimePicker: {
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
  isShowPopupWaring: {
    type: Boolean,
    default: true,
  },
});

const emits = defineEmits([
  "update:modelValue",
  "close",
  "submit",
  "update:openModel",
]);

const startDateRef = ref<any>(null);
const endDateRef = ref<any>(null);

const date = computed({
  get() {
    return props.modelValue;
  },
  set(newVal) {
    emits("update:modelValue", formatDate(newVal));
  },
});

const isOpenModal = computed({
  get() {
    return props.openModel;
  },
  set(newVal) {
    emits("update:openModel", newVal);
  },
});

const currentDate = computed(() =>
  moment().startOf("day").format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
);

const getMinEndDate = computed({
  get() {
    if (props.modelValue.startDate) {
      return isExpired(props.modelValue.startDate)
        ? currentDate.value
        : props.modelValue.startDate;
    }
    return currentDate.value;
  },
  set() {},
});

const handleClose = () => {
  emits("close");
};

const handleSubmit = async () => {
  startDateRef.value.validation();
  endDateRef.value.validation();
  emits("submit");
};

watch(
  () => date.value.startDate,
  (oldValue, newValue) => {
    if (newValue && oldValue !== newValue) {
      date.value.endDate = "";
      getMinEndDate.value =
        formatDate(
          newValue,
          DATE_FORMAT.DATE_TYPE,
          DATE_FORMAT.DATE_TIME_FORMAT_WITHOUT_SECONDS
        ) ?? "";
    }
  },
  { deep: true }
);
</script>

<template>
  <base-popup
    v-model="isOpenModal"
    persistent
    :submit-button-text="textBtnAdd"
    :cancel-button-text="textBtnCancel"
    :title="modalTitle"
    @on-close="handleClose"
    @on-submit="handleSubmit"
  >
    <template #body>
      <v-card-text class="!px-6 !pb-2 !pt-4">
        <div>
          <div v-if="isShowPopupWaring" class="flex gap-1 items-center">
            <circle-info-icon />
            <p class="text-text-lighter text-xs">
              {{ $t("product_platform.startDateCanNotBeInThePast") }}
            </p>
          </div>
          <div class="mt-2 flex items-center gap-2">
            <BaseDateTimePicker
              ref="startDateRef"
              v-model="date.startDate"
              :placeholder="$t('product_platform.startDate')"
              :min-date="minStartDate || currentDate"
              :disabled="disabledStartDate"
              :required="requiredStartDate"
              :input-mode="inputMode"
              :auto-apply="autoApply"
              :enable-time-picker="enableTimePicker"
            />
            ~
            <BaseDateTimePicker
              ref="endDateRef"
              v-model="date.endDate"
              :placeholder="$t('product_platform.endDate')"
              :min-date="getMinEndDate"
              :required="requiredEndDate"
              :input-mode="inputMode"
              :auto-apply="autoApply"
              :enable-time-picker="enableTimePicker"
            />
          </div></div
      ></v-card-text>
    </template>
  </base-popup>
</template>
