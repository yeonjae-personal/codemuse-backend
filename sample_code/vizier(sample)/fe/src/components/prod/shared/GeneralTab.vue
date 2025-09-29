<template>
  <div class="h-full">
    <template v-if="localGeneral?.length > 0">
      <DetailPane
        v-if="generalItemList?.length > 0"
        class="pt-6"
        is-not-rounded-bottom
      >
        <DetailPaneRow
          v-for="(item, index) in generalItemList?.filter(
            (item) =>
              item.fieldTypeCode !== COLUMN_FIELD_TYPE.TA &&
              item.fieldTypeCode !== 'HD'
          )"
          :key="`general-${index}`"
        >
          <template #label>
            <div class="w-[45%] !text-[13px] text-ellipsis">
              <CustomTooltip
                :content="fieldDesc(item)"
                :is-always-show="!!item.labelDscr"
              >
                {{
                  item.colName === "item_large_code"
                    ? item.labelId?.includes("product_platform.")
                      ? $t(item.labelId)
                      : $t(`product_platform.${item.labelId}`)
                    : $t(item.labelId)
                }}
              </CustomTooltip>
            </div>
          </template>
          <template #value="{ klass }">
            <div
              v-if="!isEdit || (isEdit && item.editYn === RequiredYn.No)"
              :class="klass"
            >
              <template v-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.DM">
                <template v-if="item.attrVal?.length">
                  <BaseMultiSelect
                    v-model="item.attrVal"
                    :options="optionsMultiselect(item)"
                    :disabled="true"
                  />
                </template>
                <template v-else>
                  <span>-</span>
                </template>
              </template>
              <template v-else>
                <CustomTooltip
                  :content="
                    getDisplayValue(
                      item,
                      groupCodeData,
                      isAdd,
                      createItemLargeCodeListComp,
                      createItemCodeList
                    )
                  "
                />
              </template>
            </div>
            <div
              v-else
              :class="[
                'w-[55%] pl-2 text-text-base font-size-base items-center',
                {
                  'h-8': item?.fieldTypeCode !== COLUMN_FIELD_TYPE.DM,
                },
              ]"
            >
              <BaseDateTimePicker
                v-if="item.fieldTypeCode == COLUMN_FIELD_TYPE.DP"
                :ref="(el) => dateTimePickerList.push(el)"
                v-model="item.attrVal"
                :date="item.attrVal"
                :min-date="currentDate"
                :required="item.requiredYn === RequiredYn.Yes"
                enable-time-picker
                styles="absolute common-datetime-picker"
              />
              <BaseSelectScroll
                v-else-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.DL"
                :ref="(el) => selectScrollList.push(el)"
                v-model="item.attrVal"
                :options="
                  item.colName === 'item_large_code' &&
                  createItemLargeCodeListComp?.length
                    ? createItemLargeCodeListComp
                    : item.colName === 'item_code' && createItemCodeList?.length
                      ? createItemCodeList
                      : groupCodeData[item?.commGroupCode]
                "
                :required="item.requiredYn === RequiredYn.Yes"
                :show-option-null="item.requiredYn === RequiredYn.No"
                only-chevron-down
              />
              <BaseMultiSelect
                v-else-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.DM"
                v-model="item.attrVal"
                :options="optionsMultiselect(item)"
                :required="item.requiredYn === RequiredYn.Yes"
                :disabled="false"
              />
              <template v-else>
                <div
                  v-if="item.colName === 'ctgr_node_uuid'"
                  class="flex items-center gap-1"
                >
                  <div class="!max-w-[180px] ctg-input">
                    <!-- Don't change -->
                    <v-tooltip
                      v-if="item.valName && item?.attrPath?.length"
                      activator="parent"
                      location="bottom"
                      color="red"
                      class="tooltip-ctg"
                      :eager="false"
                    >
                      <template
                        v-for="(path, idx) in item?.attrPath"
                        :key="idx"
                      >
                        <span>{{ path }}</span>
                        <span
                          v-if="idx !== item?.attrPath?.length - 1"
                          class="mx-2"
                          >/</span
                        >
                      </template>
                    </v-tooltip>
                    <BaseInputText
                      v-model="item.valName"
                      styles="input-edit"
                      :counter="parseInt(item.attrMaxLength)"
                      :maxlength="parseInt(item.attrMaxLength)"
                      :required="item.requiredYn === RequiredYn.Yes"
                      :disabled="item.disabled"
                      placeholder="Select Category"
                    />
                  </div>
                  <div class="ctg-btn">
                    <BaseButton
                      :color="ButtonColorType.Gray"
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      @click="onReChooseCategory"
                    >
                      <re-choose-icon />
                    </BaseButton>
                  </div>
                </div>
                <div v-else>
                  <BaseInputText
                    v-model="item.attrVal"
                    styles="input-edit"
                    :counter="parseInt(item.attrMaxLength)"
                    :maxlength="item.attrMaxLength"
                    :required="item.requiredYn === RequiredYn.Yes"
                    :rules="
                      useInputValidation({
                        maxLength: item.attrMaxLength,
                        onlyNumbers:
                          checkNumberIsInteger(item.attrMaxLength) &&
                          [COLUMN_FIELD_TYPE.NF, COLUMN_FIELD_TYPE.RF].includes(
                            item.fieldTypeCode
                          ),
                      })
                    "
                  />
                </div>
              </template>
            </div>
          </template>
        </DetailPaneRow>
      </DetailPane>
      <DetailPane
        v-if="additionalItemList?.length > 0"
        class="mt-2"
        is-not-rounded
      >
        <DetailPaneRow
          v-for="(item, index) in additionalItemList?.filter(
            (item) =>
              item.fieldTypeCode !== COLUMN_FIELD_TYPE.TA &&
              item.fieldTypeCode !== 'HD'
          )"
          :key="`general-${index}`"
          :label="$t(item.labelId)"
          :tooltip-content="$t(`${item.labelId}Desc`)"
          :is-always-show="!!item.labelDscr"
        >
          <template #value="{ klass }">
            <div
              v-if="!isEdit || (isEdit && item.editYn === RequiredYn.No)"
              class="relative text-ellipsis"
              :class="klass"
            >
              <template v-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.DM">
                <template v-if="item.attrVal?.length && groupCodeData">
                  <BaseMultiSelect
                    v-model="item.attrVal"
                    :options="optionsMultiselect(item)"
                    :required="item.requiredYn === RequiredYn.Yes"
                    :disabled="true"
                  />
                </template>
                <template v-else>
                  <span>-</span>
                </template>
              </template>
              <template v-else>
                <CustomTooltip
                  :content="
                    getDisplayValue(
                      item,
                      groupCodeData,
                      isAdd,
                      createItemLargeCodeListComp,
                      createItemCodeList
                    )
                  "
                />
              </template>
            </div>
            <div
              v-else
              :class="[
                'w-[55%] pl-2 text-text-base font-size-base items-center relative',
                {
                  'h-8': item?.fieldTypeCode !== COLUMN_FIELD_TYPE.DM,
                },
              ]"
            >
              <ValidationWrapper
                v-if="item.fieldTypeCode == COLUMN_FIELD_TYPE.DP"
                :show-custom-validate="item.showRule"
                :pass-data="item"
                :attr="item.attrUuid"
                :is-add="isAdd"
                :is-duplicate="isDuplicate"
              >
                <BaseDateTimePicker
                  :ref="(el) => dateTimePickerList.push(el)"
                  v-model="item.attrVal"
                  :date="item.attrVal"
                  :required="item.requiredYn === RequiredYn.Yes"
                  styles="absolute common-datetime-picker"
                  :min-date="item.minDate || currentDate"
                  :max-date="item.maxDate"
                  :attr="item.attrUuid"
                  :disabled="item.disabled"
                  :clearable="item.clearable"
                  enable-time-picker
                />
              </ValidationWrapper>
              <ValidationWrapper
                v-else-if="
                  item.colName === 'item_code' && createItemCodeList?.length
                "
                :show-custom-validate="item.showRule"
                :pass-data="item"
                :attr="item.attrUuid"
                :is-add="isAdd"
                :is-duplicate="isDuplicate"
              >
                <BaseSelect
                  v-model="item.attrVal"
                  density="compact"
                  height="32"
                  styles="catalog-select"
                  :items="createItemCodeList"
                  item-value="itemCode"
                  item-title="itemName"
                  :required="item.requiredYn === RequiredYn.Yes"
                  :rules="[{ required: item.requiredYn === RequiredYn.Yes }]"
                  :default-item-select-all="false"
                  :show-option-null="false"
                  :attr="item.attrUuid"
                  :disabled="item.disabled"
                />
              </ValidationWrapper>
              <ValidationWrapper
                v-else-if="
                  item.fieldTypeCode === COLUMN_FIELD_TYPE.DL && groupCodeData
                "
                :show-custom-validate="item.showRule"
                :pass-data="item"
                :attr="item.attrUuid"
                :is-add="isAdd"
                :is-duplicate="isDuplicate"
              >
                <BaseSelectScroll
                  :ref="(el) => selectScrollList.push(el)"
                  v-model="item.attrVal"
                  :options="getOptions(item.commGroupCode, item)"
                  :required="item.requiredYn === RequiredYn.Yes"
                  :default-item-select-all="false"
                  :show-option-null="
                    item.hasOwnProperty('showOptionNull')
                      ? item.showOptionNull
                      : item.requiredYn === RequiredYn.No
                  "
                  :disabled="item.disabled"
                  only-chevron-down
                />
              </ValidationWrapper>
              <ValidationWrapper
                v-else-if="
                  item.fieldTypeCode === COLUMN_FIELD_TYPE.DM && groupCodeData
                "
                :show-custom-validate="item.showRule"
                :pass-data="item"
                :attr="item.attrUuid"
                :is-add="isAdd"
                :is-duplicate="isDuplicate"
              >
                <BaseMultiSelect
                  :ref="(el) => multiSelectList.push(el)"
                  v-model="item.attrVal"
                  :options="optionsMultiselect(item)"
                  :disabled="item.disabled"
                  :attr="item.attrUuid"
                />
              </ValidationWrapper>
              <ValidationWrapper
                v-else-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.TF"
                :show-custom-validate="item.showRule"
                :pass-data="item"
                :attr="item.attrUuid"
                :is-add="isAdd"
                :is-duplicate="isDuplicate"
              >
                <BaseInputText
                  v-model="item.attrVal"
                  :styles="'input-edit'"
                  :class="{
                    invalidCustom: props.isValidCustom,
                  }"
                  :required="item.requiredYn === RequiredYn.Yes"
                  :maxlength="item.attrMaxLength"
                  :rules="
                    useInputValidation({
                      maxLength: item.attrMaxLength,
                      onlyNumbers:
                        checkNumberIsInteger(item.attrMaxLength) &&
                        [COLUMN_FIELD_TYPE.NF, COLUMN_FIELD_TYPE.RF].includes(
                          item.fieldTypeCode
                        ),
                    })
                  "
                  :attr="item.attrUuid"
                  :disabled="item.disabled"
                  :counter="parseInt(item.attrMaxLength)"
                />
              </ValidationWrapper>
              <ValidationWrapper
                v-else-if="
                  item.fieldTypeCode === COLUMN_FIELD_TYPE.NF ||
                  item.fieldTypeCode === COLUMN_FIELD_TYPE.RF ||
                  isNFType(item)
                "
                :show-custom-validate="item.showRule"
                :pass-data="item"
                :attr="item.attrUuid"
                :is-add="isAdd"
                :is-duplicate="isDuplicate"
              >
                <BaseInputText
                  v-model="item.attrVal"
                  :styles="'input-edit'"
                  :class="{
                    invalidCustom: props.isValidCustom,
                  }"
                  :maxlength="getMaxLengthField(item, true)"
                  :required="item.requiredYn === RequiredYn.Yes"
                  :rules="[...getRules(item)]"
                  :attr="item.attrUuid"
                  :disabled="item.disabled"
                  :counter="
                    checkNumberIsInteger(item.attrMaxLength)
                      ? parseInt(item.attrMaxLength)
                      : Number(item.attrMaxLength)
                  "
                  @keypress="onlyNumber($event, Number(item.attrMaxLength))"
                  @input="changeValueNumber($event, item)"
                  @blur="handleBlurDecimal(item)"
                />
              </ValidationWrapper>
              <BaseDropProduct
                v-else-if="
                  item.fieldTypeCode === COLUMN_FIELD_TYPE.OB && !isNFType(item)
                "
                :data="item"
                :required="item.requiredYn === RequiredYn.Yes"
                @on-click="
                  handleOpenSearchPane(groupCodeData[item?.commGroupCode])
                "
              />
            </div>
          </template>
        </DetailPaneRow>
      </DetailPane>

      <template v-for="item in textAreaItemList" :key="item.key">
        <DetailPane class="mt-2" is-not-rounded-top>
          <DetailPaneRow is-overview :label="$t(item.labelId)">
            <template #value="{ overViewKlass, overtTextKlass }">
              <div
                v-if="!isEdit"
                :class="overViewKlass"
                v-html="displayTextArea(item.attrVal)"
              ></div>
              <BaseTextArea
                v-else
                v-model="item.attrVal"
                :disabled="item.disabled"
                :class="overtTextKlass"
                :rules="{
                  required: item.requiredYn === RequiredYn.Yes,
                  maxLength: item.attrMaxLength,
                }"
                :maxlength="item.attrMaxLength"
                :show-custom-validate="item.showRule"
                :attr="item.attrUuid"
                :pass-data="item"
              />
            </template>
          </DetailPaneRow>
        </DetailPane>
      </template>
    </template>
    <div v-else class="h-full w-full flex justify-center items-center">
      <NoData />
    </div>
  </div>
</template>
<script setup lang="ts">
import { useGroupCode } from "@/composables/useGroupCode";
import { useInputValidation } from "@/composables/useInputValidation";
import {
  ADDITIONAL_FIELDS_CUSTOM,
  DATE_FORMAT,
  DETAIL_TAB_TYPE,
  HEIGHT_BUTTON,
  WIDTH_BUTTON,
} from "@/constants/";
import { RequiredYn, ButtonColorType } from "@/enums";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import ValidationWrapper from "@/pages/admin/subs/custom-validation/ValidationWrapper.vue";
import customValidationStore from "@/store/admin/customValidation.store";
import {
  checkNumberIsInteger,
  formatDataTypeDecimal,
} from "@/utils/extend-utils";
import { displayTextArea, formatDateWithOutSeconds } from "@/utils/format-data";
import moment from "moment-timezone";
import { useI18n } from "vue-i18n";
import DetailPane from "../layout/DetailPane.vue";
import DetailPaneRow from "../layout/DetailPaneRow.vue";

const props = defineProps({
  triggerMountedTab: {
    type: Boolean,
    default: false,
  },
  isDuplicate: {
    type: Boolean,
    default: false,
  },
  componentAdditionalData: {
    type: Object,
    default: () => ({}),
  },
  isValidCustom: {
    type: Boolean,
    default: false,
  },
  type: {
    type: String,
    default: "",
  },
  pageName: {
    type: String,
    default: "",
  },
  page: {
    type: String,
    default: "",
  },
  subType: {
    type: String,
    default: "",
  },
  modelValue: {
    type: Array,
    default: () => [],
  },
  isAdd: {
    type: Boolean,
    default: false,
  },
  isEdit: {
    type: Boolean,
    default: false,
  },
  createItemLargeCodeList: {
    type: Array,
    default: () => [],
  },
  createItemCodeList: {
    type: Array,
    default: () => [],
  },
});
const { listRulesItem } = storeToRefs(customValidationStore());
const { setCustomValidationItemsView } = customValidationStore();
const { groupCodeData, search, getTextDisplay } = useGroupCode();
const { t } = useI18n();
const emit = defineEmits(["update:modelValue"]);
const selectScrollList = ref<any[]>([]);
const multiSelectList = ref<any[]>([]);
const dateTimePickerList = ref<any[]>([]);
const rateType = ref("");

const fieldDesc = computed(() => {
  return (item) =>
    item.colName === "item_large_code"
      ? item.labelId?.includes("product_platform.")
        ? t(`${item.labelId}Desc`)
        : t(`product_platform.${item.labelId}Desc`)
      : t(`${item.labelId}Desc`);
});

const onlyNumber = ($event, maxLength: any = null) => {
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

const handleBlurDecimal = (item) => {
  let isDecimal = !checkNumberIsInteger(item.attrMaxLength);
  if (isDecimal) {
    let arr = item?.attrVal?.split("") || [];

    if (arr[arr?.length - 1] === ".") {
      item.attrVal = arr
        .filter((xxx) => xxx !== ".")
        .join()
        .replaceAll(",", "");
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
      : Number((maxLength + "")?.split(".")[0]) + 1;
  }
  return null;
};

const changeValueNumber = (event, item) => {
  let valueOld = event.target.value;
  let isDecimal = !checkNumberIsInteger(item.attrMaxLength);

  item.attrVal = formatDataTypeDecimal(valueOld, isDecimal, item.attrMaxLength);
};

const onReChooseCategory = () => {
  redirectCategoryPage();
};

const createItemLargeCodeListComp = computed(() => {
  return props.createItemLargeCodeList.map((item: any) => ({
    ...item,
    cmcdDetlNm: t(item.cmcdDetlNm),
  }));
});

const getOptions = (commGroupCode: string, field: any) => {
  if (field?.attrRefTableName) {
    return field?.tableColumns || [];
  } else {
    let result: any[] = groupCodeData.value[commGroupCode as string] || [];
    if (field?.multipleValues?.length) {
      result = result.filter((item) =>
        field.multipleValues.includes(item.cmcdDetlId)
      );
    }
    return result;
  }
};

const getDisplayValue = (
  item,
  groupCodeData,
  isAdd,
  createItemLargeCodeListComp,
  createItemCodeList
) => {
  if (item?.colName === "obj_code" && (isAdd || !item?.attrVal)) {
    return t("product_platform.auto_generation");
  }

  if (
    groupCodeData &&
    (item.fieldTypeCode === COLUMN_FIELD_TYPE.DL ||
      item.fieldTypeCode === COLUMN_FIELD_TYPE.DM)
  ) {
    let thirdParam;
    if (item?.colName === "item_large_code") {
      thirdParam = createItemLargeCodeListComp;
    } else if (item?.colName === "item_code") {
      thirdParam = createItemCodeList;
    } else {
      thirdParam = groupCodeData[item?.commGroupCode];
    }
    return getTextDisplay(item?.attrVal, item?.fieldTypeCode, thirdParam);
  }

  if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
    return formatDateWithOutSeconds(item?.attrVal) || "";
  }

  if (item?.colName === "ctgr_node_uuid") {
    return item?.valName;
  }

  return item?.attrVal ?? "-";
};

const validateMinMax = (
  value: string | number,
  min: number,
  max: number
): true | string => {
  const numVal = typeof value === "string" ? parseFloat(value) : value;
  if (isNaN(numVal)) return true;
  if (min !== null && max === null) {
    return numVal >= min || `${t("product_platform.wrongMinValue")} ${min}`;
  } else if (min === null && max !== null) {
    return numVal <= max || `${t("product_platform.wrongMaxValue")} ${max}`;
  } else if (min !== null && max !== null) {
    return (
      (numVal >= min && numVal <= max) ||
      `${t("product_platform.wrongRange")} ${min} - ${max}`
    );
  }
  return true;
};

const getRules = (item) => {
  if (
    (item.minVal && (item.minVal !== null || item.maxVal !== null)) ||
    (item.maxVal && (item.minVal !== null || item.maxVal !== null))
  )
    return [
      (value: string | number) =>
        validateMinMax(value, item.minVal, item.maxVal),
    ];
  return [];
};

const localGeneral = computed({
  get() {
    return props.modelValue;
  },
  set(newVal) {
    emit("update:modelValue", newVal);
  },
});

const generalItemList: any = computed(() => {
  return localGeneral.value?.filter(
    (item: any) => !item.dispTab && item.fieldTypeCode !== COLUMN_FIELD_TYPE.TA
  );
});

const additionalItemList: any = computed(() => {
  return localGeneral.value?.filter(
    (item: any) =>
      item.dispTab === DETAIL_TAB_TYPE.GENERAL &&
      item.fieldTypeCode !== COLUMN_FIELD_TYPE.TA
  );
});

const textAreaItemList: any = computed(() => {
  return localGeneral.value?.filter(
    (item: any) => item.fieldTypeCode === COLUMN_FIELD_TYPE.TA
  );
});

const currentDate = computed(() =>
  moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
);

const optionsMultiselect = computed(() => (item: any) => {
  let result =
    groupCodeData.value[item.commGroupCode]?.map((option: any) => ({
      label: option.cmcdDetlNm,
      value: option.cmcdDetlId,
    })) || [];
  if (item?.multipleValues?.length) {
    result = result.filter((field) =>
      item.multipleValues.includes(field.value)
    );
  }
  return result;
});

const handleOpenSearchPane = (data) => {
  if (loadComponent && data?.length) {
    const object = data[0];
    const path = object.cmcdDetlNm.replaceAll("\\", "/");
    loadComponent(path);
  }
};

watch(
  listRulesItem,
  (newListRulesItem) => {
    setCustomValidationItemsView(newListRulesItem);
  },
  { immediate: true }
);

const isNFType = (item) => {
  return (
    (rateType.value === "FX" &&
      [
        ADDITIONAL_FIELDS_CUSTOM.BASE_FEE,
        ADDITIONAL_FIELDS_CUSTOM.ONE_TIME_FEE,
      ].includes(item.labelId)) ||
    (!rateType.value &&
      [
        ADDITIONAL_FIELDS_CUSTOM.BASE_FEE,
        ADDITIONAL_FIELDS_CUSTOM.ONE_TIME_FEE,
      ].includes(item.labelId))
  );
};

const dataTypeString = computed(() => {
  return JSON.stringify(additionalItemList.value);
});

watch(dataTypeString, (newValueStr, oldValueStr) => {
  if (!props.isEdit) return;
  let newValue = JSON.parse(newValueStr);
  let oldValue = JSON.parse(oldValueStr);
  const rateTypeNewVal =
    newValue.find((item) => item.labelId === ADDITIONAL_FIELDS_CUSTOM.RATE_TYPE)
      ?.attrVal || "";
  const rateTypeOldVal =
    oldValue.find((item) => item.labelId === ADDITIONAL_FIELDS_CUSTOM.RATE_TYPE)
      ?.attrVal || "";

  if (rateTypeNewVal !== rateTypeOldVal) {
    // Base Fee
    const dispBaseFee = additionalItemList.value.find(
      (item) => item.labelId === ADDITIONAL_FIELDS_CUSTOM.DISP_BASE_FEE
    );
    const baseFee = additionalItemList.value.find(
      (item) => item.labelId === ADDITIONAL_FIELDS_CUSTOM.BASE_FEE
    );
    if (baseFee) {
      baseFee.attrVal = "";
      baseFee.obName = "";
    }
    if (dispBaseFee) {
      dispBaseFee.attrVal = "";
    }

    // One Time Fee
    const dispOneTimeFee = additionalItemList.value.find(
      (item) => item.labelId === ADDITIONAL_FIELDS_CUSTOM.DISP_ONE_TIME_FEE
    );
    const oneTimeFee = additionalItemList.value.find(
      (item) => item.labelId === ADDITIONAL_FIELDS_CUSTOM.ONE_TIME_FEE
    );
    if (oneTimeFee) {
      oneTimeFee.attrVal = "";
      oneTimeFee.obName = "";
    }
    if (dispOneTimeFee) {
      dispOneTimeFee.attrVal = "";
    }
  }
});

watch(
  () => additionalItemList.value,
  (newVal) => {
    if (newVal?.length) {
      rateType.value =
        newVal.find(
          (item) => item.labelId === ADDITIONAL_FIELDS_CUSTOM.RATE_TYPE
        )?.attrVal || "";
      // Base Fee
      const dispBaseFee = newVal.find(
        (item) => item.labelId === ADDITIONAL_FIELDS_CUSTOM.DISP_BASE_FEE
      );
      const baseFee = newVal.find(
        (item) => item.labelId === ADDITIONAL_FIELDS_CUSTOM.BASE_FEE
      );
      if (dispBaseFee && baseFee) {
        if (rateType.value === "RT") {
          dispBaseFee.attrMaxLength = null;
          dispBaseFee.attrVal = baseFee.obName || "";
          dispBaseFee.disabled = true;
        } else {
          baseFee.attrMaxLength = 11;
          dispBaseFee.attrMaxLength = 11;
          dispBaseFee.disabled = false;
        }
      }
      // One Time Fee
      const dispOneTimeFee = newVal.find(
        (item) => item.labelId === ADDITIONAL_FIELDS_CUSTOM.DISP_ONE_TIME_FEE
      );
      const oneTimeFee = newVal.find(
        (item) => item.labelId === ADDITIONAL_FIELDS_CUSTOM.ONE_TIME_FEE
      );
      if (dispOneTimeFee && oneTimeFee) {
        if (rateType.value === "RT") {
          dispOneTimeFee.attrMaxLength = null;
          dispOneTimeFee.attrVal = oneTimeFee.obName || "";
          dispOneTimeFee.disabled = true;
        } else {
          oneTimeFee.attrMaxLength = 11;
          dispOneTimeFee.attrMaxLength = 11;
          dispOneTimeFee.disabled = false;
        }
      }
    }
  },
  { deep: true }
);

watch(
  () => localGeneral.value,
  async (newVal: any, oldVal: any) => {
    if (newVal.length && JSON.stringify(newVal) !== JSON.stringify(oldVal)) {
      const commonCodeList = newVal.reduce((codeArr, item) => {
        if (
          [
            COLUMN_FIELD_TYPE.DL,
            COLUMN_FIELD_TYPE.DM,
            COLUMN_FIELD_TYPE.OB,
          ].includes(item.fieldTypeCode) &&
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
  },
  { immediate: true, deep: true }
);
const validationAllSelect = () => {
  if (selectScrollList.value?.length) {
    selectScrollList.value.forEach((comp: any) => {
      comp?.validate?.();
    });
  }
  if (multiSelectList.value?.length) {
    multiSelectList.value.forEach((comp: any) => {
      comp?.validate?.();
    });
  }
  if (dateTimePickerList.value?.length) {
    dateTimePickerList.value.forEach((comp: any) => {
      comp?.validation?.();
    });
  }
};
const resetValidationAllSelect = () => {
  if (selectScrollList.value?.length) {
    selectScrollList.value.forEach((comp: any) => {
      comp?.resetValidate?.();
    });
  }
  if (multiSelectList.value?.length) {
    multiSelectList.value.forEach((comp: any) => {
      comp?.resetValidate?.();
    });
  }
  if (dateTimePickerList.value?.length) {
    dateTimePickerList.value.forEach((comp: any) => {
      comp?.resetValidation?.();
    });
  }
};

defineExpose({
  validationAllSelect,
  resetValidationAllSelect,
});

const loadComponent = inject<any>("handleLoadComponent", null);
const redirectCategoryPage = inject<any>("redirectCategoryPage", null);
</script>
<style scoped>
:deep().v-field {
  height: 32px;
  display: flex;
  align-items: center;
}
:deep().v-field__field {
  text-overflow: ellipsis;
  width: 150px;
}
.common-datetime-picker :deep().dp__pointer {
  height: 32px;
  font-size: 13px !important;
  color: #3a3b3d !important;
  font-family: "Noto Sans KR", sans-serif !important;
}
.ctg-btn :deep().v-btn--size-default {
  min-width: unset !important;
}
.ctg-input :deep().v-field--disabled {
  opacity: unset !important;
  background-color: #f0f2f5 !important;
  font-family: "Noto Sans KR", sans-serif !important;
}
.tooltip-ctg :deep().v-overlay__content {
  background-color: #ffffff !important;
  color: #6b6d70;
  border: 1px solid #dce0e5;
  border-radius: 8px;
}
:deep(.multi-select) {
  min-height: 34px !important;
}

:deep(.multi-select .open) {
  position: absolute;
  z-index: 99999;
  border: solid 1px #dce0e5;
  width: 100%;
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
</style>
