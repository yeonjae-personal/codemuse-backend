<!-- eslint-disable vue/no-mutating-props -->
<template>
  <div
    class="attribute-type-wrapper"
    :class="{
      disabled: props.item.disabled,
      'show-arrow': props.showArrow,
    }"
  >
    <div
      :class="[
        'attribute-type-header',
        { required: isRequiredField },
        {
          'selected-item':
            props.item.id === selectedAttribute?.attrId &&
            ['condition', 'action'].includes(props.item.type),
        },
      ]"
    >
      <span class="header-title">{{ $t(props.item.name) }}</span>
      <div class="d-flex">
        <div class="attribute-info" :class="{ 'mr-3': props.parentEdit }">
          <span class="attribute-code">{{ props.item.attrType }}</span>
          <div class="attribute-type">
            <span v-if="props.item.type === 'condition'" class="blue"></span>
            <span v-else class="white"></span>
            <span v-if="props.item.type === 'action'" class="red"></span>
            <span v-else class="white"></span>
          </div>
        </div>
        <BasePopover
          v-if="props.parentEdit && props.item.useYn"
          :options="listActions(props.item)"
          custom-location="bottom-left"
          class="flex-initial"
        >
          <template #activator>
            <DotsVerticalIcon />
          </template>
        </BasePopover>
      </div>
    </div>
    <div class="attribute-type-content">
      <BaseInputText
        v-if="props.item.attrType === 'TF' || props.item.attrType === 'TA'"
        v-model="props.item.value"
        :disabled="!props.parentEdit || props.item.disabled"
        :maxlength="500"
      />
      <div
        v-if="props.item.attrType === 'NF' || props.item.attrType === 'RF'"
        class="number-field"
      >
        <BaseInputText
          v-model="props.item.numberFromField"
          :disabled="!props.parentEdit || props.item.disabled"
          :rules="
            useInputValidation({
              maxLength: Number(props.item.attrMaxLength) || 5,
            })
          "
          :maxlength="Number(props.item.attrMaxLength) || 5"
          counter
          @keypress="onlyNumber"
          @input="changeValueNumber($event, 'min')"
        />
        <span>~</span>
        <BaseInputText
          v-model="props.item.numberToField"
          :disabled="!props.parentEdit || props.item.disabled"
          :rules="numberToFieldRules"
          :maxlength="Number(props.item.attrMaxLength) || 5"
          counter
          @keypress="onlyNumber"
          @input="changeValueNumber($event, 'max')"
        />
      </div>
      <div v-if="props.item.attrType === 'DP'" class="datepicker-field">
        <BaseDateTimePicker
          v-model="props.item.startDateField"
          class="!w-auto"
          :disabled="!props.parentEdit || props.item.disabled"
          enable-time-picker
          input-mode
          :auto-apply="false"
        />
        <span>~</span>
        <BaseDateTimePicker
          v-model="props.item.endDateField"
          :min-date="props.item.startDateField"
          class="!w-auto"
          :disabled="!props.parentEdit || props.item.disabled"
          enable-time-picker
          input-mode
          :auto-apply="false"
        />
      </div>
      <BaseMultiSelect
        v-if="props.item.attrType === 'DM' || props.item.attrType === 'DL'"
        :id="attributeId"
        v-model="props.item.multiSelectField"
        :options="listOptions"
        :is-show-chip="true"
        :disabled-layer-icon="true"
        class="attribute-mutilselect"
        :disabled="!props.parentEdit || props.item.disabled"
        :default-item-select-all="false"
        :disabled-option="parentDisabled || item.disabled"
        :can-drop-up="isShowUpOption"
      />
    </div>
  </div>
  <DateTimePopup
    v-model:open-model="isOpenPopup"
    v-model="dateData"
    :modal-title="$t('product_platform.expire_attribute')"
    :min-end-date="dateData.startDate"
    :disabled-start-date="true"
    :is-show-popup-waring="false"
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    @submit="handleSubmitDialog"
    @close="handleCloseDialog"
  />
  <DateTimePopup
    v-model:open-model="isOpenEnablePopup"
    v-model="dateData"
    :modal-title="$t('product_platform.enable_attribute')"
    :min-start-date="currentDate"
    :min-end-date="dateData.startDate"
    :required-start-date="true"
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    @submit="handleSubmitEnableDialog"
    @close="handleCloseEnableDialog"
  />
</template>

<!-- eslint-disable vue/no-mutating-props -->
<script setup lang="ts">
import BaseDateTimePicker from "@/components/prod/common/BaseDateTimePicker.vue";
import BaseMultiSelect from "@/components/prod/common/BaseMultiSelect.vue";
import BasePopover from "@/components/prod/common/BasePopover.vue";
import DateTimePopup from "@/components/prod/common/DateTimePopup.vue";
import DeleteIcon from "@/components/prod/icons/DeleteIcon.vue";
import DotsVerticalIcon from "@/components/prod/icons/DotsVerticalIcon.vue";
import EnableIcon from "@/components/prod/icons/EnableIcon.vue";
import ExpireIcon from "@/components/prod/icons/ExpireIcon.vue";
import { useInputValidation } from "@/composables/useInputValidation";
import { BORDER_CONFIG, DATE_FORMAT } from "@/constants/";
import { RequiredFieldType } from "@/enums/customValidation";
import { IAttributeItem } from "@/interfaces/admin/admin";
import { useSnackbarStore } from "@/store";
import customValidationStore from "@/store/admin/customValidation.store";
import useCmcdStore from "@/store/cmcd.store";
import { isTime1BeforeTime2 } from "@/utils/format-data";
import moment from "moment-timezone";
import { useI18n } from "vue-i18n";
const { t } = useI18n();

interface Props {
  item: IAttributeItem;
  parentId: string;
  parentEdit: boolean;
  parentDisabled: boolean;
  showArrow?: boolean;
  attributeId: string;
  numberItems: number;
}
const props = defineProps<Props>();
defineEmits(["update:modelValue"]);
const { search } = useCmcdStore();
const {
  deleteAttributeItem,
  updateAttributeItemDate,
  checkEnableAttribute,
  checkDisabledItem,
} = customValidationStore();
const { selectedAttribute } = storeToRefs(customValidationStore());
const { showSnackbar } = useSnackbarStore();
const listOptions = ref([]);
const isOpenPopup = ref(false);
const isOpenEnablePopup = ref(false);
const defaultBorderActive = ref(BORDER_CONFIG.ACTIVE);
// const startDate = ref(props.item.startDate);
// const endDate = ref(props.item.endDate);
const dateData = reactive({
  startDate: props.item.startDate,
  endDate: props.item.endDate,
});

const isShowUpOption = ref<boolean>(false);

const currentDate = computed(() =>
  moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
);

const isRequiredField = computed(
  () => props.item.requiredYn === RequiredFieldType.Yes
);

const handleCloseDialog = () => {
  isOpenPopup.value = false;
};

const numberToFieldRules = computed(() => {
  return [
    ...useInputValidation({
      maxLength: Number(props.item.attrMaxLength) || 5,
    }),
    (value: any) =>
      props.item.numberFromField && value
        ? Number(value) >= Number(props.item.numberFromField) ||
          t("product_platform.maxBiggerMin")
        : true,
  ];
});

const bgColor = computed(() => {
  return props.item.disabled || props.parentDisabled
    ? "#dce0e5"
    : "linear-gradient(105.78deg,#effaff 26.93%,#def5ff 63.74%,#c3e8f7 85.24%,#bce4f5 91.25%)";
});

const boxShadow = computed(() => {
  return props.item.disabled || props.parentDisabled
    ? "none"
    : "6px 8px 10px 0px #0000000a, 3px 3px 4px 0px #0000001f";
});

watch(
  () => props.numberItems,
  (newValue, oldValue) => {
    if (newValue !== oldValue) {
      nextTick(() => {
        calculatorShowUpOption();
      });
    }
  }
);

watch(
  () => props.item.startDateField,
  () => {
    if (
      isTime1BeforeTime2(props.item.endDateField, props.item.startDateField)
    ) {
      props.item.endDateField = "";
    }
  }
);

onMounted(async () => {
  const getListOptions = async (code: string) => {
    const response = await search([code]);
    listOptions.value = response[code as string].map(
      (item: { cmcdDetlNm: any; cmcdDetlId: any }) => {
        return {
          name: item.cmcdDetlNm,
          label: item.cmcdDetlNm,
          value: item.cmcdDetlId,
        };
      }
    );
  };
  if (["DM", "DL"].includes(props.item.attrType)) {
    await getListOptions(props.item.code);
  }
  nextTick(() => {
    calculatorShowUpOption();
  });
});

const calculatorShowUpOption = (): void => {
  const element = document.getElementById(`${props.attributeId}`);
  const bottomElement = document.getElementById("bottom-lomcomotive");
  if (!element || !bottomElement) return;
  const elementRect = element.getBoundingClientRect();
  const bottomRect = bottomElement.getBoundingClientRect();
  const numberOptions =
    listOptions.value.length >= 5 ? 5 : listOptions.value.length;
  const optionHeight = numberOptions * 20 + 28 + (numberOptions - 1) * 12;
  isShowUpOption.value = elementRect.bottom + optionHeight > bottomRect.bottom;
};

const handleSubmitDialog = () => {
  if (!dateData.endDate) {
    showSnackbar(t("product_platform.required_end_date"), "error");
    return;
  }
  updateAttributeItemDate(
    props.item,
    props.parentId,
    props.item.startDate,
    dateData.endDate
  );
  isOpenPopup.value = false;
};

const handleCloseEnableDialog = () => {
  isOpenEnablePopup.value = false;
};

const handleSubmitEnableDialog = () => {
  if (!dateData.startDate) {
    showSnackbar(t("product_platform.required_start_date"), "error");
    return;
  }
  const isDisabledItem = checkDisabledItem(
    dateData.startDate,
    dateData.endDate
  );
  const isValidStartDate = moment(dateData.startDate).isBefore(
    currentDate.value
  );
  if (isDisabledItem || isValidStartDate) {
    showSnackbar(t("product_platform.pleaseInputCorrectDateToEnable"), "error");
    return;
  }
  updateAttributeItemDate(
    props.item,
    props.parentId,
    dateData.startDate,
    dateData.endDate
  );
  isOpenEnablePopup.value = false;
};

const listActions = (attrItem: { temp: any; disabled: any }) => {
  const actions = [] as any;
  if (attrItem.temp) {
    actions.push({
      name: t("product_platform.actionRemove"),
      icon: DeleteIcon,
      iconProps: {
        class: "text-text-lighter",
      },
      onClick: () => {
        deleteAttributeItem(attrItem, props.parentId);
      },
    });
  } else {
    if (attrItem.disabled) {
      actions.push({
        name: t("product_platform.actionEnable"),
        icon: EnableIcon,
        iconProps: {
          class: "text-text-lighter",
        },
        onClick: () => {
          const message = checkEnableAttribute(props.item, props.parentId);
          if (message) {
            showSnackbar(message, "error");
          } else {
            dateData.startDate = props.item.startDate;
            dateData.endDate = props.item.endDate;
            isOpenEnablePopup.value = true;
          }
        },
      });
    } else {
      actions.push({
        name: t("product_platform.actionExpire"),
        icon: ExpireIcon,
        iconProps: {
          class: "text-text-lighter",
        },
        onClick: () => {
          dateData.startDate = props.item.startDate;
          dateData.endDate = props.item.endDate;
          isOpenPopup.value = true;
        },
      });
    }
  }
  return actions;
};

const onlyNumber = ($event: {
  keyCode: any;
  which: any;
  preventDefault: () => void;
}) => {
  let keyCode = $event.keyCode ? $event.keyCode : $event.which;
  if (keyCode < 48 || keyCode > 57) {
    $event.preventDefault();
  }
};

const changeValueNumber = (event: { target: { value: any } }, type: string) => {
  const vowelsRegex = /^\d+$/;
  let valueOld = event.target.value;
  if (type === "min") {
    props.item.numberFromField = valueOld
      .split("")
      .filter((value: string) => vowelsRegex.test(value))
      .join()
      .replaceAll(",", "");
  } else {
    props.item.numberToField = valueOld
      .split("")
      .filter((value: string) => vowelsRegex.test(value))
      .join()
      .replaceAll(",", "");
  }
};
</script>
<style lang="scss" scoped>
.attribute-type-wrapper {
  min-height: 88px;
  border-radius: 8px;
  padding: 4px;
  display: flex;
  flex-direction: column;
  row-gap: 8px;
  font-family: "Noto Sans KR";
  position: relative;
  border: 1px solid transparent;
  margin-bottom: 12px;
  &:last-child {
    margin-bottom: 0;
  }
  .attribute-type-header {
    height: 40px;
    // border: 1px solid transparent;
    border-radius: 6px;
    padding: 6px 12px 6px 16px;
    background: v-bind(bgColor);
    // background: ;
    box-shadow: v-bind(boxShadow);
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
    &:before {
      position: absolute;
      top: 0;
      left: 0;
      content: "";
      width: 100%;
      height: 100%;
      border-radius: 8px;
      border-left: 1px solid #b2ddff;
    }
    &.required {
      &:before {
        position: absolute;
        top: 0;
        left: 0;
        content: "";
        width: 10px;
        height: 100%;
        border-radius: 8px;
        border-left: 2px solid #e0332d;
      }
    }

    .header-title {
      font-size: 13px;
      font-weight: 500;
      color: #3a3b3d;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    .attribute-info {
      display: flex;
      align-items: center;
      .attribute-code {
        font-size: 13px;
        line-height: 19.5px;
        letter-spacing: 0.25px;
        margin-right: 14px;
        color: #6b6d70;
      }
      .attribute-type {
        display: flex;
        flex-direction: column;
        row-gap: 4px;
        .blue {
          width: 4px;
          height: 4px;
          border-radius: 50%;
          background: #4054b2;
        }
        .red {
          width: 4px;
          height: 4px;
          border-radius: 50%;
          background: #d9325a;
        }
        .white {
          width: 4px;
          height: 4px;
          border-radius: 50%;
          background: transparent;
        }
        .gray {
          width: 4px;
          height: 4px;
          border-radius: 50%;
          background: #fff;
        }
      }
    }
  }
  .attribute-type-content {
    padding: 0 8px;

    .number-field {
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: #6b6d70;
    }
    .datepicker-field {
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: #6b6d70;
    }
  }
}

.show-arrow {
  &::before {
    display: block;
    content: "";
    width: 0;
    height: 0;
    border-top: 5px solid transparent;
    border-left: 10px solid #bdc1c7;
    border-bottom: 5px solid transparent;
    position: absolute;
    left: -7px;
    top: 22px;
  }
}

.disabled {
  opacity: 0.5;
}

.selected-item {
  border: 2px solid v-bind(defaultBorderActive) !important;
}

:deep(.border-lighter) {
  height: 33px;
}

:deep(.multi-select .open) {
  position: absolute;
  z-index: 999;
  border: solid 1px #dce0e5;
  width: 330px;
  background: #fff;
  margin-top: -3px;
}
:deep(.v-textarea .v-field__input) {
  -webkit-mask-image: unset;
}
:deep(.number-field .v-input) {
  width: auto;
}
:deep(.v-field__field) {
  height: 32px;
}
:deep(.v-field) {
  height: 32px;
}
:deep(.custom-text-field .v-input__control) {
  height: 32px;
}
:deep(.dp__input) {
  height: 32px;
}
</style>
