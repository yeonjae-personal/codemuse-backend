<template>
  <BasePopup
    v-model="isOpen"
    :class-custom="classCustom"
    :title="$t('product_platform.advanced_search')"
    :size="DialogSizeType.ESmall"
    :submit-button-text="$t('product_platform.apply')"
    :cancel-button-text="$t('product_platform.cancel')"
    persistent
    is-custom-position
    @on-close="closePopupSearchDetail"
    @on-submit="applyPopupSearchDetail"
  >
    <template #header>
      <v-card-title>
        <div class="flex justify-between items-center">
          <p>{{ $t("product_platform.advanced_search") }}</p>
          <div class="flex gap-6 items-center mr-6 justify-between">
            <div>
              <switch-view-search v-model="viewModeSearch" class="ms-auto" />
            </div>
            <close-icon
              class="cursor-pointer mt-[-47px] mr-[-40px]"
              @click="closePopupSearchDetail"
            />
          </div>
        </div>
      </v-card-title>
    </template>
    <template #body>
      <LocomotiveComponent
        scroll-content-class="pb-[8px] "
        scroll-container-class="!px-[0px] min-h-[470px] max-h-[480px]"
      >
        <v-form v-model="isFormValid" class="w-full flex-grow-1">
          <div v-if="advencedSearchList?.length">
            <div
              v-show="viewModeSearch === SEARCH_MODE.OPTION1"
              :class="`ml-6 flex gap-2 flex-col  pr-5 ${
                advencedSearchList[0].fieldTypeCode === COLUMN_FIELD_TYPE.NF
                  ? 'pt-11'
                  : 'pt-3'
              }`"
            >
              <template v-for="(row, index) in advencedSearchList" :key="index">
                <!-- Price -->
                <div
                  v-if="row.fieldTypeCode === COLUMN_FIELD_TYPE.RF"
                  class="flex items-center justify-between gap-3 mb-[2px] !min-h-[36px]"
                >
                  <div
                    class="flex align-center h-[100px] font-medium text-text-base font-size-base font-base !w-[160px]"
                  >
                    {{ $t(`${row.labelId}`) }}
                  </div>

                  <div
                    class="bg-lighter rounded-[8px] h-[100px] pt-[32px] px-[8px]"
                  >
                    <BaseRangeSlider
                      v-model="row.attrVal"
                      v-model:min-value="row.fieldValueMin"
                      v-model:max-value="row.fieldValueMax"
                      min-width="268px"
                      :min="row.min"
                      :max="row.max"
                      :item="row"
                      thumb-color="#D9325A"
                    />
                  </div>
                  <div>
                    <RefreshButton
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      class="bg-white reset-btn"
                      @click="handleResetField(row)"
                    />
                  </div>
                </div>
                <!-- Price -->

                <!-- Multi Value -->
                <div
                  v-else-if="
                    [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
                      row.fieldTypeCode
                    )
                  "
                  class="flex items-start justify-between gap-3 !min-h-[36px]"
                  :class="{
                    'last-dl': index === advencedSearchList.length - 1,
                  }"
                >
                  <div
                    class="flex align-center h-[32px] font-medium text-text-base font-size-base font-base !w-[160px]"
                  >
                    {{ $t(`${row.labelId}`) }}
                  </div>
                  <div v-if="optionsMultiselect(row.commGroupCode)">
                    <BaseMultiSelect
                      v-model="row.attrVal"
                      class="!w-[298px]"
                      :options="optionsMultiselect(row.commGroupCode)"
                      :disabled-layer-icon="true"
                      is-show-chip
                      can-drop-up
                    />
                  </div>
                  <div>
                    <RefreshButton
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      class="bg-white reset-btn"
                      @click="handleResetField(row)"
                    />
                  </div>
                </div>
                <!-- Multi Value -->

                <!-- Type Date -->
                <div
                  v-else-if="row.fieldTypeCode === COLUMN_FIELD_TYPE.DP"
                  class="flex items-center justify-between gap-3 !min-h-[36px]"
                >
                  <div
                    class="flex align-center h-[34px] font-medium text-text-base font-size-base font-base !w-[160px]"
                  >
                    {{ $t(`${row.labelId}`) }}
                  </div>
                  <div
                    class="flex items-center gap-1 font-medium text-text-base font-size-base font-base"
                  >
                    <div>
                      <CustomTooltip
                        location="top left"
                        :disabled="!row.fieldValueMin"
                        is-always-show
                      >
                        <BaseDateTimePicker
                          v-model="row.fieldValueMin"
                          enable-time-picker
                          input-mode
                          :auto-apply="false"
                          :format-enable-time="
                            DATE_FORMAT.DEFAULT_DATE_FORMAT_DATEPICKER
                          "
                          :format="
                            DATE_FORMAT.DEFAULT_DATE_FORMAT_DATEPICKER_TIME
                          "
                          styles="absolute offer-edit-datetime-picker !max-w-[141px] rounded-[12px]"
                        />
                        <template #content>
                          <span class="uppercase">
                            {{ formatDateWithOutSeconds(row.fieldValueMin) }}
                          </span>
                        </template>
                      </CustomTooltip>
                    </div>
                    ~
                    <div>
                      <CustomTooltip
                        location="top right"
                        :disabled="!row.fieldValueMax"
                        is-always-show
                      >
                        <BaseDateTimePicker
                          v-model="row.fieldValueMax"
                          enable-time-picker
                          input-mode
                          :auto-apply="false"
                          :format="
                            DATE_FORMAT.DEFAULT_DATE_FORMAT_DATEPICKER_TIME
                          "
                          :format-enable-time="
                            DATE_FORMAT.DEFAULT_DATE_FORMAT_DATEPICKER
                          "
                          styles="absolute offer-edit-datetime-picker !max-w-[141px] rounded-[12px]"
                        />
                        <template #content>
                          <span class="uppercase">
                            {{ formatDateWithOutSeconds(row.fieldValueMax) }}
                          </span>
                        </template>
                      </CustomTooltip>
                    </div>
                  </div>
                  <div>
                    <RefreshButton
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      class="bg-white reset-btn"
                      @click="handleResetField(row)"
                    />
                  </div>
                </div>
                <!-- Type Date -->

                <!-- Type Number -->
                <div
                  v-else-if="row.fieldTypeCode === COLUMN_FIELD_TYPE.NF"
                  class="flex items-center justify-between gap-3 !min-h-[36px]"
                >
                  <div
                    class="flex align-center h-[32px] font-medium text-text-base font-size-base font-base !w-[160px]"
                  >
                    {{ $t(`${row.labelId}`) }}
                  </div>
                  <div
                    class="flex items-center gap-1 font-medium text-text-base font-size-base font-base"
                  >
                    <BaseInputText
                      v-model="row.fieldValueMin"
                      styles="!max-w-[141px]"
                      :maxlength="getMaxLengthField(row, true)"
                      :counter="
                        checkNumberIsInteger(row.attrMaxLength)
                          ? parseInt(row.attrMaxLength)
                          : Number(row.attrMaxLength)
                      "
                      @keypress="onlyNumber($event, Number(row.attrMaxLength))"
                      @keydown.enter="applyPopupSearchDetail"
                      @input="changeValueNumber($event, row, 'min')"
                      @blur="handleBlurDecimal(row, 'min')"
                    />
                    ~
                    <BaseInputText
                      v-model="row.fieldValueMax"
                      styles="!max-w-[141px]"
                      :maxlength="getMaxLengthField(row, true)"
                      :counter="
                        checkNumberIsInteger(row.attrMaxLength)
                          ? parseInt(row.attrMaxLength)
                          : Number(row.attrMaxLength)
                      "
                      @keypress="onlyNumber($event, Number(row.attrMaxLength))"
                      @keydown.enter="applyPopupSearchDetail"
                      @input="changeValueNumber($event, row, 'max')"
                      @blur="handleBlurDecimal(row, 'max')"
                    />
                  </div>
                  <div>
                    <RefreshButton
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      class="bg-white reset-btn"
                      @click="handleResetField(row)"
                    />
                  </div>
                </div>
                <!-- Type Number -->

                <!-- Type Text -->
                <div
                  v-else-if="
                    [COLUMN_FIELD_TYPE.TF, COLUMN_FIELD_TYPE.TA].includes(
                      row.fieldTypeCode
                    )
                  "
                  class="flex align-center h-[32px] flex items-center justify-between gap-3 !min-h-[36px]"
                >
                  <div
                    class="font-medium text-text-base font-size-base font-base !w-[160px]"
                  >
                    {{ $t(`${row.labelId}`) }}
                  </div>
                  <div>
                    <BaseInputText
                      v-model="row.attrVal"
                      styles="!w-[298px]"
                      :counter="parseInt(row.attrMaxLength)"
                      :required="false"
                      :maxlength="row.attrMaxLength"
                      :rules="
                        useInputValidation({
                          maxLength: row.attrMaxLength,
                        })
                      "
                      @keydown.enter="applyPopupSearchDetail"
                    />
                  </div>
                  <div>
                    <RefreshButton
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      class="bg-white reset-btn"
                      @click="handleResetField(row)"
                    />
                  </div>
                </div>
                <!-- Type Text -->

                <!-- Type Area -->
                <!-- <div
                v-else-if="row.fieldTypeCode === COLUMN_FIELD_TYPE.TA"
                class="mb-[2px] flex align-center h-[32px] flex items-center justify-between gap-3 !min-h-[36px]"
              >
                <div
                  class="font-medium text-text-base font-size-base font-base !w-[160px]"
                >
                  {{ $t(`product_platform.${row.labelId}`) }}
                </div>
                <div>
                  <BaseTextArea
                    v-model="row.attrVal"
                    class="!w-[298px] type-area advanced-search-ta"
                    :rules="{
                      required: row.requiredYn === RequiredYn.Yes,
                      maxLength: row.attrMaxLength,
                    }"
                  />
                </div>
                <div>
                  <RefreshButton
                  :width="WIDTH_BUTTON.FOR_INPUT"
                  :height="HEIGHT_BUTTON.FOR_INPUT"
                    class="bg-white reset-btn"
                    @click="handleResetField(row)"
                  />
                </div>
              </div> -->
                <!-- Type Area -->
              </template>
            </div>
            <div
              v-show="viewModeSearch === SEARCH_MODE.OPTION2"
              class="mx-6 pt-3 flex gap-2 flex-col min-h-[470px] max-h-[480px] query-search"
            >
              <div
                class="flex justify-between items-center font-medium text-text-base font-size-base font-base !min-h-[36px]"
              >
                Query Search
                <RefreshButton
                  :width="WIDTH_BUTTON.FOR_INPUT"
                  :height="HEIGHT_BUTTON.FOR_INPUT"
                  class="bg-white reset-btn"
                />
              </div>
              <BaseTextArea class="!max-h-[120px]" readonly />
            </div>
          </div>

          <div v-else>
            <div
              class="min-h-[470px] max-h-[480px] flex items-center no-data-layout"
            >
              <NoData />
            </div>
          </div>
        </v-form>
      </LocomotiveComponent>
    </template>
  </BasePopup>
</template>

<script setup lang="ts">
import isEmpty from "lodash-es/isEmpty";
import { useGroupCode } from "@/composables/useGroupCode";
import { useInputValidation } from "@/composables/useInputValidation";
import {
  SEARCH_MODE,
  DATE_FORMAT,
  WIDTH_BUTTON,
  HEIGHT_BUTTON,
} from "@/constants/";
import { DialogSizeType, RequiredYn } from "@/enums";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import {
  checkNumberIsInteger,
  formatDataTypeDecimal,
} from "@/utils/extend-utils";
import {
  formatDateDefault,
  formatDateWithOutSeconds,
} from "@/utils/format-data";

const { groupCodeData, search } = useGroupCode();

const emits = defineEmits([
  "onClose",
  "onSubmit",
  "onReset",
  "update:modelValue",
  "update:modelList",
]);
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  modelList: {
    type: Array,
    default: () => [],
  },
  type: {
    type: String,
    default: null,
  },
  classCustom: {
    type: String,
    default: null,
  },
});

const isOpen = computed({
  get() {
    return props.modelValue;
  },
  set(newValue) {
    emits("update:modelValue", newValue);
  },
});

const advencedSearchList = computed<any>({
  get() {
    return props.modelList.filter(
      (item: any) => item?.advSearchYn === RequiredYn.Yes
    );
  },
  set(newValue) {
    emits("update:modelList", newValue);
  },
});

const isFormValid = ref(false);
const viewModeSearch = ref(SEARCH_MODE.OPTION1);

onMounted(async () => {
  moveRFToTop(advencedSearchList.value);
  if (advencedSearchList.value?.length) {
    const commonCodeList = advencedSearchList.value.reduce((codeArr, item) => {
      if (
        [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
          item.fieldTypeCode
        ) &&
        item?.commGroupCode
      ) {
        codeArr.push(item.commGroupCode);
      }
      return codeArr;
    }, []);
    if (commonCodeList?.length) {
      await search(commonCodeList);
    }
  }
});

const optionsMultiselect = computed(() => (commGroupCode: string) => {
  return (
    groupCodeData.value[commGroupCode as string]?.map((option: any) => ({
      label: option.cmcdDetlNm,
      value: option.cmcdDetlId,
    })) || []
  );
});

const handleResetField = (row: any) => {
  row.attrVal = [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
    row.fieldTypeCode
  )
    ? []
    : null;
  row.fieldValueMin = null;
  row.fieldValueMax = null;
};

const onlyNumber = ($event, maxLength = 0) => {
  let keyCode = $event.keyCode ? $event.keyCode : $event.which;

  if (maxLength && !checkNumberIsInteger(maxLength)) {
    if (keyCode !== 46 && (keyCode < 48 || keyCode > 57)) {
      $event.preventDefault();
    }
  } else {
    if (keyCode < 48 || keyCode > 57) {
      $event.preventDefault();
    }
  }
};

const getMaxLengthField = (item, getAllValue = false) => {
  let maxLength = item.attrMaxLength;
  if (maxLength) {
    if (checkNumberIsInteger(maxLength)) {
      return maxLength;
    }
    return getAllValue
      ? Number(maxLength) + 1
      : Number((maxLength + "").split(".")[0]) + 1;
  }
  return null;
};

const changeValueNumber = (event, row, type) => {
  let valueOld = event.target.value;
  let isDecimal = !checkNumberIsInteger(row.attrMaxLength);

  if (type === "min") {
    row.fieldValueMin = formatDataTypeDecimal(
      valueOld,
      isDecimal,
      row.attrMaxLength
    );
  } else {
    row.fieldValueMax = formatDataTypeDecimal(
      valueOld,
      isDecimal,
      row.attrMaxLength
    );
  }
};
const handleBlurDecimal = (row, type) => {
  let isDecimal = !checkNumberIsInteger(row.attrMaxLength);
  if (isDecimal) {
    if (type === "min") {
      let arr = row.fieldValueMin?.split("");

      if (arr[arr.length - 1] === ".") {
        row.fieldValueMin = arr
          .filter((xxx) => xxx !== ".")
          .join()
          .replaceAll(",", "");
      }
    } else {
      let arr = row.fieldValueMax?.split("");

      if (arr[arr.length - 1] === ".") {
        row.fieldValueMax = arr
          .filter((xxx) => xxx !== ".")
          .join()
          .replaceAll(",", "");
      }
    }
  }
};

const applyPopupSearchDetail = () => {
  const advencedSearchListFilter = advencedSearchList.value.filter(
    (row) =>
      row.fieldValueMin ||
      row.fieldValueMax ||
      ([
        COLUMN_FIELD_TYPE.DL,
        COLUMN_FIELD_TYPE.DM,
        COLUMN_FIELD_TYPE.TA,
        COLUMN_FIELD_TYPE.TF,
      ].includes(row.fieldTypeCode) &&
        !isEmpty(row.attrVal))
  );
  const additional = advencedSearchListFilter
    .filter((item) => item.dispTab)
    .map((el: any) => {
      if (
        [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(el.fieldTypeCode)
      ) {
        return { ...el, attrVal: JSON.stringify(el.attrVal) };
      } else if (el.fieldTypeCode === COLUMN_FIELD_TYPE.RF) {
        return { ...el, attrVal: null };
      } else {
        return el;
      }
    });
  const general = advencedSearchListFilter.filter((item) => !item.dispTab);
  if (general?.length || additional?.length) {
    let paramSearch = {
      general: general.map((item) => {
        // TODO: We'll apply update the params with type is DP when API finish
        if (item?.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
          return {
            ...item,
            fieldValueMin: formatDateDefault(item?.fieldValueMin),
            fieldValueMax: formatDateDefault(item?.fieldValueMax),
          };
        }
        return {
          ...item,
          fieldType: item?.fieldTypeCode,
          fieldName: item?.colName,
          fieldValue: item?.attrVal,
        };
      }),
      additional: additional.map((item) => {
        // TODO: We'll apply update the params with type is DP when API finish
        if (item?.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
          return {
            ...item,
            fieldValueMin: formatDateDefault(item?.fieldValueMin),
            fieldValueMax: formatDateDefault(item?.fieldValueMax),
          };
        }
        return {
          ...item,
          fieldType: item?.fieldTypeCode,
          fieldName: item?.attrUuid,
          fieldValue: item?.attrVal,
        };
      }),
    };
    emits("onSubmit", paramSearch);
  } else {
    emits("onReset");
  }
};

const closePopupSearchDetail = () => {
  emits("onClose");
};

const moveRFToTop = (arr) => {
  const rfList = arr?.filter(
    (item) => item.fieldTypeCode === COLUMN_FIELD_TYPE.RF
  );
  if (rfList?.length) {
    rfList.forEach((rf) => {
      const index = arr.findIndex((item) => item?.labelId === rf?.labelId);
      if (index !== -1) {
        const obj = arr.splice(index, 1);
        arr.unshift(...obj);
      }
    });
  }
};

onMounted(() => {
  document.addEventListener("keydown", (event) => {
    if (event.key === "Escape") {
      emits("onClose");
    }
  });
});
</script>

<style scoped lang="scss">
:deep(.type-area .v-field) {
  height: 32px !important;
}
:deep(.type-area .v-input__control) {
  border-radius: 12px;
}
:deep(.type-area .v-field__input) {
  border-radius: 12px;
  max-height: 32px !important;
  padding: 4px 16px;
  min-height: unset !important;
  -webkit-mask-image: unset;
}
:deep(.v-slider-thumb__label::before) {
  display: none !important;
}

:deep(.multi-select) {
  min-height: 34px !important;
}

:deep(.multi-select .open) {
  position: absolute;
  z-index: 99999;
  border: solid 1px #dce0e5;
  width: 298px;
  left: -1px;
  background: #fff;
  border-top-color: transparent;
  border-bottom-right-radius: 12px;
  border-bottom-left-radius: 12px;
}
:deep(.multi-select .open.open-up) {
  border-top-color: #dce0e5;
  border-bottom-right-radius: 0px;
  border-bottom-left-radius: 0px;
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
}
:deep(.dp__clear_icon) {
  padding-right: 10px;
  margin-top: 1px;
}
:deep(.custom-text-field) {
  border-radius: 12px !important;
}
:deep(.custom-text-field .v-input__control) {
  height: 32px !important;
  overflow: hidden;
}
:deep(.v-field) {
  height: 32px !important;
  border-radius: 12px;
}
:deep(.v-field__field) {
  height: 32px !important;
}
:deep(.dp__input) {
  height: 34px !important;
  border-radius: 12px !important;
}
.advanced-search-ta :deep(.v-input__details) {
  bottom: 38px !important;
}
.no-data-layout :deep(p) {
  font-weight: bold !important;
}
.reset-btn {
  width: 34px !important;
  height: 34px !important;
  :deep() svg {
    width: 15px !important;
  }
  :deep() .v-btn__content {
    padding-left: 5px;
  }
}

:deep(.query-search .v-field) {
  height: 120px !important;
}
:deep(.query-search .v-field__field) {
  height: 120px !important;
}
</style>
