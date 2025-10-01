<template>
  <template
    v-if="
      (detailList?.length || textAreaItemList?.length) &&
      selectedEntityDetails?.additional?.length > 0
    "
  >
    <div class="text-text-lighter font-size-base font-medium">
      <DetailPane v-if="detailList.length > 0" is-not-rounded-bottom>
        <template v-for="(item, index) in detailList" :key="index">
          <DetailPaneRow
            v-if="item.fieldTypeCode !== COLUMN_FIELD_TYPE.TA"
            :label="$t(item.labelId)"
            :tooltip-content="$t(`${item.labelId}Desc`)"
            :is-always-show="!!item.labelDscr"
          >
            <template #value="{ klass }">
              <div v-if="!isEdit" :class="klass">
                <template v-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.DM">
                  <template v-if="item.attrVal?.length">
                    <BaseMultiSelect
                      v-model="item.attrVal"
                      :options="
                        optionsMultiselect(
                          selectedEntityDetails.additional[index].commGroupCode
                        )
                      "
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
                      getTextDisplay(
                        item.attrVal,
                        item.fieldTypeCode,
                        groupCodeList[item.commGroupCode]
                      )
                    "
                  />
                </template>
              </div>
              <div v-else :class="klass">
                <div class="text-text-base font-size-base font-normal w-full">
                  <BaseDateTimePicker
                    v-if="item.fieldTypeCode == COLUMN_FIELD_TYPE.DP"
                    ref="datePicker"
                    v-model="selectedEntityDetails.additional[index].attrVal"
                    :date="item.value"
                    :min-date="currentDate"
                    :required="item.requiredYn === RequiredYn.Yes"
                    :clearable="true"
                    enable-time-picker
                    :auto-apply="false"
                    input-mode
                    styles="absolute common-datetime-picker"
                  />
                  <BaseSelectScroll
                    v-else-if="item.fieldTypeCode == COLUMN_FIELD_TYPE.DL"
                    :ref="(el) => selectScrollList.push(el)"
                    v-model="selectedEntityDetails.additional[index].attrVal"
                    :options="groupCodeList[item.commGroupCode]"
                    :required="item.requiredYn === RequiredYn.Yes"
                    :show-option-null="true"
                  />
                  <BaseMultiSelect
                    v-else-if="
                      selectedEntityDetails.additional[index].fieldTypeCode ===
                      COLUMN_FIELD_TYPE.DM
                    "
                    :ref="(el) => multiSelectList.push(el)"
                    v-model="selectedEntityDetails.additional[index].attrVal"
                    :required="
                      selectedEntityDetails.additional[index].requiredYn ===
                      RequiredYn.Yes
                    "
                    :options="
                      optionsMultiselect(
                        selectedEntityDetails.additional[index].commGroupCode
                      )
                    "
                    :disabled="false"
                  />
                  <BaseInputText
                    v-else-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.NF"
                    v-model="selectedEntityDetails.additional[index].attrVal"
                    :styles="'input-edit'"
                    :required="item.requiredYn === RequiredYn.Yes"
                    :counter="Number(item.attrMaxLength)"
                    :maxlength="getMaxLengthField(item, true)"
                    :rules="
                      useInputValidation({
                        required: item.requiredYn === RequiredYn.Yes,
                      })
                    "
                    @keypress="onlyNumber($event, Number(item.attrMaxLength))"
                    @input="
                      changeValueNumber(
                        $event,
                        selectedEntityDetails.additional[index]
                      )
                    "
                    @blur="
                      handleBlurDecimal(selectedEntityDetails.additional[index])
                    "
                  />
                  <BaseInputText
                    v-else
                    v-model="selectedEntityDetails.additional[index].attrVal"
                    :styles="'input-edit'"
                    :required="item.requiredYn === RequiredYn.Yes"
                    :counter="Number(item.attrMaxLength)"
                    :maxlength="Number(item.attrMaxLength)"
                    :rules="
                      useInputValidation({
                        required: item.requiredYn === RequiredYn.Yes,
                        maxLength: item.attrMaxLength,
                      })
                    "
                  />
                </div>
              </div>
            </template>
          </DetailPaneRow>
        </template>
      </DetailPane>
      <template v-for="(item, index) in textAreaItemList" :key="item.key">
        <DetailPane
          v-if="item.fieldTypeCode === 'TA'"
          class="mt-2"
          :is-not-rounded-top="detailList.length > 0"
        >
          <DetailPaneRow is-overview :label="$t(item.labelId)">
            <template #value="{ overViewKlass, overtTextKlass }">
              <BaseTextArea
                v-if="isEdit"
                v-model="selectedEntityDetails.additional[index].attrVal"
                :class="overtTextKlass"
                :rules="{
                  required: item.requiredYn === RequiredYn.Yes,
                  maxLength: item.attrMaxLength,
                }"
                :required="item.requiredYn === RequiredYn.Yes"
                :maxlength="item.attrMaxLength"
              />
              <div
                v-else
                :class="overViewKlass"
                v-html="displayTextArea(item?.attrVal)"
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
import DetailPane from "@/components/prod/layout/DetailPane.vue";
import DetailPaneRow from "@/components/prod/layout/DetailPaneRow.vue";
import { useGroupCode } from "@/composables/useGroupCode";
import { useInputValidation } from "@/composables/useInputValidation";
import { DETAIL_CATEGORY } from "@/constants/extendsManager";
import { DATE_FORMAT } from "@/constants/index";
import { RequiredYn } from "@/enums";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { useMultiEntityCreateStore, useMultiEntitySearchStore } from "@/store";
import {
  checkNumberIsInteger,
  formatDataTypeDecimal,
} from "@/utils/extend-utils";
import { displayTextArea } from "@/utils/format-data";
import moment from "moment-timezone";

const props = defineProps({
  isEdit: {
    type: Boolean,
    default: false,
  },
  category: {
    type: String,
    default: DETAIL_CATEGORY.SEARCH,
  },
  groupCodeList: {
    type: Object,
    default: () => {},
  },
});

const multiEntitySearchStore = useMultiEntitySearchStore();
const multiEntityCreateStore = useMultiEntityCreateStore();
const { entityDetailData, selectedEntityDetails } = storeToRefs(
  props.category === DETAIL_CATEGORY.SEARCH
    ? multiEntitySearchStore
    : multiEntityCreateStore
);
const { getTextDisplay } = useGroupCode();

const datePicker = ref();
const selectScrollList = ref([]);
const multiSelectList = ref([]);

const optionsMultiselect = computed(() => (commGroupCode: string) => {
  return (
    props.groupCodeList[commGroupCode as string]?.map((option: any) => ({
      ...option,
      label: option.cmcdDetlNm,
      value: option.cmcdDetlId,
    })) || []
  );
});

const getMaxLengthField = (item, getAllValue = false) => {
  let maxlength = item.attrMaxLength;
  if (maxlength) {
    if (checkNumberIsInteger(maxlength)) {
      return maxlength;
    }
    return getAllValue
      ? Number(maxlength) + 1
      : Number((maxlength + "").split(".")[0]) + 1;
  }
  return null;
};

const onlyNumber = ($event, maxlength = null) => {
  let keyCode = $event.keyCode ? $event.keyCode : $event.which;

  if (maxlength && !checkNumberIsInteger(maxlength)) {
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

  item.attrVal = formatDataTypeDecimal(valueOld, isDecimal, item.attrMaxLength);
};

const handleBlurDecimal = (item) => {
  let isDecimal = !checkNumberIsInteger(item.attrMaxLength);
  if (isDecimal) {
    let arr = item?.attrVal.split("");

    if (arr[arr.length - 1] === ".") {
      item.attrVal = arr
        .filter((xxx) => xxx !== ".")
        .join()
        .replaceAll(",", "");
    }
  }
};

const currentDate = computed(() =>
  moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
);

const detailList = computed(() => {
  return entityDetailData.value.additionalTab
    .filter((item: any) => item.fieldTypeCode !== COLUMN_FIELD_TYPE.TA)
    .map((item: any) => ({
      ...item,
      attrVal:
        item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
          ? JSON.parse(item?.attrVal)?.filter((value: any) => value.trim()) ||
            []
          : item.attrVal,
    }));
});

const textAreaItemList = computed(() => {
  return entityDetailData.value.additionalTab.filter(
    (item) => item.fieldTypeCode === COLUMN_FIELD_TYPE.TA
  );
});

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
};

defineExpose({
  validationAllSelect,
  resetValidationAllSelect,
});
</script>
<style scoped lang="scss">
:deep().v-field {
  height: 32px;
  display: flex;
  align-items: center;
}
:deep().v-field__field {
  text-overflow: ellipsis;
  width: 100%;
}
.common-datetime-picker :deep().dp__pointer {
  height: 32px;
}
</style>
