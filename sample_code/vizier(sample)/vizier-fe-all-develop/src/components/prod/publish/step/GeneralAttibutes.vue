<template>
  <template v-if="detailList?.length > 0 && detailData">
    <div class="text-text-lighter font-size-base font-medium">
      <DetailPane is-not-rounded-bottom>
        <template v-for="item in nonTextAreaList" :key="item.labelId">
          <DetailPaneRow :label="$t(item.labelId)">
            <template #value="{ klass }">
              <div v-if="!isEdit && !isCreate" :class="klass">
                <CustomTooltip
                  :content="
                    item.colName == 'pubRqstStusCode'
                      ? getTextDisplay(
                          item.attrVal,
                          item.fieldTypeCode,
                          groupCodeList as any
                        )
                      : item.fieldTypeCode == COLUMN_FIELD_TYPE.DP
                        ? formatDateWithOutSeconds(item?.attrVal) || '-'
                        : item.attrVal || '-'
                  "
                />
              </div>
              <div v-else :class="klass">
                <div v-if="item.editYn === RequiredYn.Yes" class="w-full">
                  <BaseDateTimePicker
                    v-if="item.fieldTypeCode == COLUMN_FIELD_TYPE.DP"
                    ref="datePicker"
                    v-model="detailData[item.colName]"
                    :date="item.attrVal"
                    :min-date="currentDate"
                    :required="item.requiredYn === RequiredYn.Yes"
                    :clearable="true"
                    enable-time-picker
                    :auto-apply="false"
                    styles="absolute common-datetime-picker"
                  />
                  <div v-else-if="item.fieldTypeCode == COLUMN_FIELD_TYPE.DL">
                    <BaseSelectScroll
                      :ref="(el) => selectScrollList.push(el)"
                      v-model="detailData[item.colName]"
                      :options="groupCodeList[item.commGroupCode]"
                      :required="item.requiredYn === RequiredYn.Yes"
                      :show-option-null="true"
                    />
                  </div>
                  <BaseInputText
                    v-else-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.NF"
                    v-model="detailData[item.colName]"
                    :styles="'input-edit'"
                    :required="item.requiredYn === RequiredYn.Yes"
                    :counter="
                      checkNumberIsInteger(item.attrMaxLength)
                        ? item.attrMaxLength
                        : Number(item.attrMaxLength)
                    "
                    :maxlength="getMaxLengthField(item, true)"
                    :rules="
                      useInputValidation({
                        required: item.requiredYn === RequiredYn.Yes,
                      })
                    "
                    @keypress="onlyNumber($event, Number(item.attrMaxLength))"
                    @input="changeValueNumber($event, detailData[item.colName])"
                    @blur="handleBlurDecimal(detailData[item.colName])"
                  />
                  <BaseInputText
                    v-else
                    v-model="detailData[item.colName]"
                    :styles="'input-edit'"
                    :required="item.requiredYn === RequiredYn.Yes"
                    :counter="
                      checkNumberIsInteger(item.attrMaxLength)
                        ? item.attrMaxLength
                        : Number(item.attrMaxLength)
                    "
                    :maxlength="Number(item.attrMaxLength)"
                    :rules="
                      useInputValidation({
                        required: item.requiredYn === RequiredYn.Yes,
                        maxLength: item.attrMaxLength,
                      })
                    "
                  />
                </div>
                <div v-else>
                  <CustomTooltip
                    :content="
                      item.colName == 'pubRqstTaskCode' && isCreate
                        ? $t('product_platform.auto_generation')
                        : item.colName == 'pubRqstStusCode'
                          ? getTextDisplay(
                              item.attrVal,
                              item.fieldTypeCode,
                              groupCodeList as any
                            )
                          : item.fieldTypeCode == COLUMN_FIELD_TYPE.DP
                            ? formatDateWithOutSeconds(item?.attrVal) || '-'
                            : item.attrVal || '-'
                    "
                  />
                </div>
              </div>
            </template>
          </DetailPaneRow>
        </template>
      </DetailPane>
      <template v-for="item in textAreaItemList" :key="item.labelId">
        <DetailPane class="mt-2" is-not-rounded-top>
          <DetailPaneRow is-overview :label="$t('LB00000139')">
            <template #value="{ overViewKlass, overtTextKlass }">
              <BaseTextArea
                v-if="(isEdit || isCreate) && item.editYn === RequiredYn.Yes"
                v-model="detailData.ovwCntn"
                :class="overtTextKlass"
                :rules="{
                  maxLength: 500,
                }"
                :maxlength="500"
              />
              <div
                v-else
                :class="overViewKlass"
                v-html="displayTextArea(detailData.ovwCntn)"
              ></div>
            </template>
          </DetailPaneRow>
        </DetailPane>
      </template>
    </div>
  </template>
  <template v-else>
    <div class="h-full w-full flex justify-center items-center">
      <NoData />
    </div>
  </template>
</template>
<script setup lang="ts">
import { useInputValidation } from "@/composables/useInputValidation";
import { DATE_FORMAT } from "@/constants/index";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { RequiredYn } from "@/enums";
import { useGroupCode } from "@/composables/useGroupCode";
import { displayTextArea, formatDateWithOutSeconds } from "@/utils/format-data";
import {
  checkNumberIsInteger,
  formatDataTypeDecimal,
} from "@/utils/extend-utils";
import moment from "moment-timezone";
import DetailPane from "../../layout/DetailPane.vue";
import DetailPaneRow from "../../layout/DetailPaneRow.vue";

const emit = defineEmits(["update:detailModal"]);
const props = defineProps({
  isEdit: {
    type: Boolean,
    default: false,
  },
  isCreate: {
    type: Boolean,
    default: false,
  },
  detailModal: {
    type: Object,
    default: () => {},
  },
  detailList: {
    type: Array,
    default: () => [],
  },
  groupCodeList: {
    type: Object,
    default: () => {},
  },
});
const { getTextDisplay } = useGroupCode();
const datePicker = ref();
const selectScrollList = ref<any[]>([]);

const currentDate = computed(() => {
  return moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE);
});

const detailData = computed({
  get() {
    return props.detailModal;
  },
  set(newVal) {
    emit("update:detailModal", newVal);
  },
});
const nonTextAreaList = computed<any>(() =>
  props.detailList.filter(
    (row: any) => row.fieldTypeCode !== COLUMN_FIELD_TYPE.TA
  )
);
const textAreaItemList = computed<any>(() => {
  return props.detailList?.filter(
    (row: any) => row.fieldTypeCode === COLUMN_FIELD_TYPE.TA
  );
});
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

const changeValueNumber = (event, item) => {
  let valueOld = event.target.value;
  let isDecimal = !checkNumberIsInteger(item.attrMaxLength);

  item = formatDataTypeDecimal(valueOld, isDecimal, item.attrMaxLength);
};

const handleBlurDecimal = (item) => {
  let isDecimal = !checkNumberIsInteger(item.attrMaxLength);
  if (isDecimal) {
    let arr = item.split("");

    if (arr[arr.length - 1] === ".") {
      item = arr
        .filter((xxx) => xxx !== ".")
        .join()
        .replaceAll(",", "");
    }
  }
};

const validationAllSelect = () => {
  if (selectScrollList.value?.length) {
    selectScrollList.value.forEach((comp: any) => {
      comp?.validate?.();
    });
  }
};
const resetValidationAllSelect = () => {
  if (selectScrollList.value?.length) {
    selectScrollList.value.forEach((comp: any) => {
      comp?.resetValidate?.();
    });
  }
};

defineExpose({
  validationAllSelect,
  resetValidationAllSelect,
});
</script>
<style scoped>
:deep().v-field {
  height: 32px;
  display: flex;
  align-items: center;
}
:deep().v-field__field {
  width: 100%;
  text-overflow: ellipsis;
}
.common-datetime-picker :deep().dp__pointer {
  height: 32px;
}
</style>
