<template>
  <div v-if="localAdditional?.length">
    <DetailPane v-if="nonTextAreaItemList?.length" is-not-rounded-bottom>
      <DetailPaneRow
        v-for="(item, index) in nonTextAreaItemList?.filter(
          (item) => item.fieldTypeCode !== COLUMN_FIELD_TYPE.TA
        )"
        :key="`additional-${index}`"
        :label="$t(item.labelId)"
        :tooltip-content="
          item.labelDscr ? $t(`${item.labelId}Desc`) : $t(item.labelId)
        "
        :is-always-show="!!item.labelDscr"
      >
        <template #value="{ klass }">
          <div v-if="!isEdit" :class="klass">
            <template v-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.DM">
              <template v-if="item.attrVal?.length">
                <BaseMultiSelect
                  v-model="item.attrVal"
                  :options="optionsMultiselect(item)"
                  :disabled="!isEdit ? true : item.disabled"
                  :required="item.requiredYn === RequiredYn.Yes"
                />
              </template>
              <template v-else>
                <span>-</span>
              </template>
            </template>
            <template v-else>
              <CustomTooltip :content="getDisplayValue(item, groupCodeData)" />
            </template>
          </div>
          <div
            v-else
            :class="[
              klass,
              {
                'h-8': item?.fieldTypeCode !== COLUMN_FIELD_TYPE.DM,
              },
            ]"
          >
            <ValidationWrapper
              v-if="item?.fieldTypeCode === COLUMN_FIELD_TYPE.DP"
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
                styles="absolute common-datetime-picker"
                :min-date="item.minDate || currentDate"
                :max-date="item.maxDate"
                :attr="item.attrUuid"
                :disabled="item.disabled"
                :clearable="item.clearable"
                :required="item.requiredYn === RequiredYn.Yes"
                enable-time-picker
              />
            </ValidationWrapper>
            <ValidationWrapper
              v-else-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.DL"
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
              v-else-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.DM"
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
                :required="item.requiredYn === RequiredYn.Yes"
              />
            </ValidationWrapper>
            <ValidationWrapper
              v-else-if="
                item.fieldTypeCode === COLUMN_FIELD_TYPE.NF ||
                item.fieldTypeCode === COLUMN_FIELD_TYPE.RF
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
                :required="item.requiredYn === RequiredYn.Yes"
                :maxlength="getMaxLengthField(item, true)"
                :attr="item.attrUuid"
                :disabled="item.disabled"
                :counter="
                  checkNumberIsInteger(item.attrMaxLength)
                    ? parseInt(item.attrMaxLength)
                    : Number(item.attrMaxLength)
                "
                :rules="[...getRules(item)]"
                @keypress="onlyNumber($event, Number(item.attrMaxLength))"
                @input="changeValueNumber($event, item)"
                @blur="handleBlurDecimal(item)"
              />
            </ValidationWrapper>
            <ValidationWrapper
              v-else-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.TF"
              :show-custom-validate="item.showRule"
              :pass-data="item"
              :attr="item.attrUuid"
              :is-duplicate="isDuplicate"
              :is-add="isAdd"
            >
              <BaseInputText
                v-model="item.attrVal"
                :styles="'input-edit'"
                :class="{
                  invalidCustom: props.isValidCustom,
                }"
                :maxlength="item.attrMaxLength"
                :required="item.requiredYn === RequiredYn.Yes"
                :rules="
                  useInputValidation({
                    maxLength: item.attrMaxLength,
                    onlyNumbers: [
                      COLUMN_FIELD_TYPE.NF,
                      COLUMN_FIELD_TYPE.RF,
                    ].includes(item.fieldTypeCode),
                  })
                "
                :attr="item.attrUuid"
                :disabled="item.disabled"
                :counter="parseInt(item.attrMaxLength)"
              />
            </ValidationWrapper>
            <BaseDropProduct
              v-else-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.OB"
              :data="item"
              :required="item.requiredYn === RequiredYn.Yes"
              @on-click="
                handleOpenSearchPane(groupCodeData[item?.commGroupCode])
              "
            />
            <ValidationWrapper
              v-else
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
                    onlyNumbers: [
                      COLUMN_FIELD_TYPE.NF,
                      COLUMN_FIELD_TYPE.RF,
                    ].includes(item.fieldTypeCode),
                  })
                "
                :attr="item.attrUuid"
                :disabled="item.disabled"
                :counter="parseInt(item.attrMaxLength)"
              />
            </ValidationWrapper>
          </div>
        </template>
      </DetailPaneRow>
    </DetailPane>
    <!-- TA field -->
    <template v-for="item in textAreaItemList" :key="item.labelId">
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
            />
          </template>
        </DetailPaneRow>
      </DetailPane>
    </template>
  </div>
  <NoData v-else />
</template>
<script setup lang="ts">
import { useGroupCode } from "@/composables/useGroupCode";
import { useInputValidation } from "@/composables/useInputValidation";
import { DATE_FORMAT } from "@/constants/index";
import { RequiredYn } from "@/enums";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import customValidationStore from "@/store/admin/customValidation.store";
import { displayTextArea, formatDateWithOutSeconds } from "@/utils/format-data";
import moment from "moment-timezone";
import ValidationWrapper from "@/pages/admin/subs/custom-validation/ValidationWrapper.vue";
import { useI18n } from "vue-i18n";
import {
  checkNumberIsInteger,
  formatDataTypeDecimal,
} from "@/utils/extend-utils";
import DetailPane from "../layout/DetailPane.vue";
import DetailPaneRow from "../layout/DetailPaneRow.vue";

const { groupCodeData, search, getTextDisplay } = useGroupCode();

const emit = defineEmits(["update:modelValue"]);
const props = defineProps({
  triggerMountedTab: {
    type: Boolean,
    default: false,
  },
  isDuplicate: {
    type: Boolean,
    default: false,
  },
  isValidCustom: {
    type: Boolean,
    default: false,
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
  type: {
    type: String,
    default: "",
  },
  page: {
    type: String,
    default: "",
  },
  pageName: {
    type: String,
    default: "",
  },
  subType: {
    type: String,
    default: "",
  },
});
const { listRulesItem } = storeToRefs(customValidationStore());
const { setCustomValidationItemsView } = customValidationStore();
const { t } = useI18n();
const selectScrollList = ref<any[]>([]);
const multiSelectList = ref<any[]>([]);
const dateTimePickerList = ref<any>([]);

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

const localAdditional: any = computed({
  get() {
    return props.modelValue;
  },
  set(newVal) {
    emit("update:modelValue", newVal);
  },
});

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

const currentDate = computed(() => moment().format(DATE_FORMAT.DATE_TYPE));

const textAreaItemList: any = computed(() => {
  return localAdditional.value?.filter(
    (item: any) => item.fieldTypeCode === COLUMN_FIELD_TYPE.TA
  );
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

const changeValueNumber = (event, item) => {
  let valueOld = event.target.value;
  let isDecimal = !checkNumberIsInteger(item.attrMaxLength);

  item.attrVal = formatDataTypeDecimal(valueOld, isDecimal, item.attrMaxLength);
};

const nonTextAreaItemList: any = computed(() => {
  return localAdditional.value?.filter(
    (item: any) => item.fieldTypeCode !== COLUMN_FIELD_TYPE.TA
  );
});

const handleOpenSearchPane = (data) => {
  if (loadComponent && data?.length) {
    const object = data[0];
    const path = object.cmcdDetlNm.replaceAll("\\", "/");
    loadComponent(path);
  }
};

const getDisplayValue = (item, groupCodeData) => {
  switch (item.fieldTypeCode) {
    case COLUMN_FIELD_TYPE.DP:
      return formatDateWithOutSeconds(item?.attrVal) ?? "-";
    case COLUMN_FIELD_TYPE.OB:
      return item?.obName;
    default:
      return getTextDisplay(
        item.attrVal,
        item.fieldTypeCode,
        groupCodeData[item?.commGroupCode || ""]
      );
  }
};

watch(
  listRulesItem,
  (newListRulesItem) => {
    setCustomValidationItemsView(newListRulesItem);
  },
  { immediate: true }
);

watch(
  () => localAdditional.value,
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
  {
    immediate: true,
    deep: true,
  }
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
</script>

<style scoped>
.v-tab.v-tab.v-btn {
  color: #bdc1c7;
  font-size: 13px;
}
:deep().v-field {
  height: 32px;
  display: flex;
  align-items: center;
}

.common-datetime-picker :deep().dp__pointer {
  height: 32px;
  font-size: 13px !important;
  color: #3a3b3d !important;
  font-family: "Noto Sans KR", sans-serif !important;
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
