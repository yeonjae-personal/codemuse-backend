<template>
  <div
    :id="uuid"
    :class="[
      'condition-item zoom-animation',
      isSelected || requiredCondUuids.includes(conditionItem.condUuid!)
        ? `!border-[3px]`
        : 'border-[1px]',
      {
        'is-focused':
          isSelected || requiredCondUuids.includes(conditionItem.condUuid!),
        'is-edit': isEditRuleStructure,
        'is-explained': isExplained,
      },
    ]"
    @click="handleClickConditionItem"
  >
    <div
      :class="[
        'condition-item__field',
        { 'is-expanded': formFields.operator === 'NOT IN' },
      ]"
      @drop="handleDrop($event)"
      @dragover="handleDragover($event)"
      @click="handleConditionClick"
    >
      <div v-if="!formFields.dispName" class="condition-item__field--empty">
        {{ t("product_platform.drag_and_drop_here") }}
      </div>
      <template v-else>
        <div
          class="flex items-center gap-[6px]"
          :class="[
            'condition-item__field-value',
            { 'is-expanded': formFields.operator === 'NOT IN' },
          ]"
        >
          <AlertIcon v-if="isExplained" class="flex-shrink-0" />
          <CustomTooltip :content="formFields.dispName">
            <span class="condition-item__name">{{ formFields.dispName }}</span>
          </CustomTooltip>
        </div>
      </template>
    </div>
    <div
      ref="operatorRef"
      :class="[
        'condition-item__operator',
        {
          'is-focused': isOpenOperatorOption,
          'is-expanded': formFields.operator === 'NOT IN',
        },
      ]"
      @click="handleToggleOperator"
    >
      <component
        :is="selectedOperator.component"
        :color="isOpenOperatorOption ? '#fff' : '#6B6D70'"
      />
    </div>
    <div class="condition-item__value">
      <div
        v-if="isInOrNotInOperator"
        ref="valueRef"
        :class="[
          'condition-item__input',
          { 'is-fail': isConditionFailed || isExplained },
        ]"
        @click="handleToggleValue"
      >
        {{ valueString[0] }}
        <div
          v-if="valueString.length - 1 > 0"
          :class="[
            'condition-item__input-number',
            { 'is-focused': isSelected },
          ]"
        >
          +{{ valueString.length - 1 }}
        </div>
      </div>
      <input
        v-else
        v-model="fieldValue"
        type="text"
        :class="[
          'condition-item__input',
          { 'is-fail': isConditionFailed || isExplained },
        ]"
        :readonly="!isEditRuleStructure || isDragging"
        :placeholder="`-${t('product_platform.value').toLowerCase()}-`"
        @keypress="onlyNumber"
        @blur="handleBlurValue"
      />
    </div>

    <!-- Close btn -->
    <div
      v-if="isSelected && isEditRuleStructure && !isOpenValueOption"
      class="condition-item__close"
      @click="handleDeleteNode"
    >
      <CloseIcon />
    </div>
  </div>

  <!-- Error icon -->
  <div v-if="isConditionFailed" class="condition-item__error">
    <ErrorIcon />
  </div>

  <!-- Operator options -->
  <div
    v-if="isOpenOperatorOption"
    ref="operatorOptionsRef"
    class="operator-options"
  >
    <div v-for="option in options" :key="option.value">
      <component
        :is="option.component"
        :class="[
          'operator-options__item',
          { 'is-expanded': option.value === 'NOT IN' },
        ]"
        @click="handleSelectOperator(option.value)"
      />
    </div>
  </div>

  <!-- Value -->
  <div
    v-if="isOpenValueOption"
    ref="valueOptionsRef"
    class="condition-item-value"
  >
    <LocomotiveComponent
      v-if="fieldValues.length > 0"
      scroll-content-class="flex flex-col gap-2"
      scroll-container-class="!px-0 max-h-[114px]"
      is-stop-propagation-wheel
      :number-scroll-y="currentY"
      :is-scroll-when-add-new="isScrollWhenAddNew"
    >
      <BaseInputText
        v-for="item in fieldValues"
        :ref="`field-item-${item.uuid}`"
        :key="item.uuid"
        v-model="item.value"
        :readonly="!isEditRuleStructure || isDragging"
        @keypress="onlyNumber"
        @enter="handleKeyupEnter"
      />
    </LocomotiveComponent>
    <div v-if="isEditRuleStructure" class="condition-item-value__action">
      <button class="condition-item-value__add" @click="handleAddNewFieldItem">
        <PlusLargeIcon />
      </button>
    </div>
  </div>

  <BasePopup
    v-if="isShowConfirmDelete"
    v-model="isShowConfirmDelete"
    :content="t('product_platform.delete_condition_confirm')"
    :icon="DialogIconType.Info"
    :cancel-button-text="t('product_platform.btn_no')"
    :submit-button-text="t('product_platform.btn_yes')"
    @on-close="handleClosePopupConfirm"
    @on-submit="handleSubmitPopupConfirm"
  />
</template>

<script lang="ts" setup>
import { v4 as uuidv4 } from "uuid";
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
import EqualIcon from "@/components/prod/icons/EqualIcon.vue";
import GreaterOrEqualIcon from "@/components/prod/icons/GreaterOrEqualIcon.vue";
import GreaterThanIcon from "@/components/prod/icons/GreaterThanIcon.vue";
import LessOrEqualIcon from "@/components/prod/icons/LessOrEqualIcon.vue";
import LessThanIcon from "@/components/prod/icons/LessThanIcon.vue";
import NotEqualIcon from "@/components/prod/icons/NotEqualIcon.vue";
import InIcon from "./icon/InIcon.vue";
import NotInIcon from "./icon/NotInIcon.vue";
import {
  ruleEngineProvider,
  type Condition,
  type FieldValues,
  type OperatorOption,
  type OperatorValue,
  type RuleEngineProvider,
} from "@/interfaces/admin/rule-engine";
import type { IFieldItem } from "@/interfaces/admin/rule-field";
import { DialogIconType } from "@/enums";
import { BORDER_CONFIG } from "@/constants/index";

type Props = {
  conditionItem: Condition;
  uuid?: string;
};

defineOptions({
  inheritAttrs: false,
});

const emit = defineEmits(["delete-node"]);
const props = defineProps<Props>();

const { t } = useI18n();

const operatorOptions: Record<OperatorValue, OperatorOption> = {
  "==": { value: "==", component: EqualIcon },
  "!=": { value: "!=", component: NotEqualIcon },
  "<": { value: "<", component: LessThanIcon },
  "<=": { value: "<=", component: LessOrEqualIcon },
  ">": { value: ">", component: GreaterThanIcon },
  ">=": { value: ">=", component: GreaterOrEqualIcon },
  IN: { value: "IN", component: InIcon },
  "NOT IN": { value: "NOT IN", component: NotInIcon },
};

const {
  selectedNodeId,
  failedCondUuids,
  requiredCondUuids,
  testRule,
  testRuleTemp,
  passed,
  passedCondUuids,
  passedMessage,
  isTested,
  isShowRuleTest,
  isShowRuleField,
  isShowRuleList,
  isShowRuleReport,
  isEditRuleStructure,
  aiConditionUuids,
} = storeToRefs(useRuleEngineStore());
const { setSelectedNodeId } = useRuleEngineStore();

const { isDragging } = inject<RuleEngineProvider>(ruleEngineProvider, {
  isDragging: ref<boolean>(false),
});

const formFields = ref<Condition>(props.conditionItem);
const operatorOptionsRef = ref<HTMLElement | null>(null);
const operatorRef = ref<HTMLElement | null>(null);
const valueOptionsRef = ref<HTMLElement | null>(null);
const valueRef = ref<HTMLElement | null>(null);
const isOpenOperatorOption = ref<boolean>(false);
const isShowConfirmDelete = ref<boolean>(false);
const isOpenValueOption = ref<boolean>(false);
const fieldValues = ref<FieldValues[]>([]);
const fieldValue = ref<string>(cloneDeep(props.conditionItem.value) || "");
const isScrollWhenAddNew = ref<boolean>(false);
const instance = getCurrentInstance();
const currentY = ref<number>(0);
const defaultBorderActive = ref(BORDER_CONFIG.ACTIVE);

const isConditionFailed = computed<boolean>(
  () =>
    isTested.value &&
    failedCondUuids.value.includes(props.conditionItem.condUuid!)
);

const selectedOperator = computed<Pick<OperatorOption, "component">>(() => {
  return { component: operatorOptions[formFields.value.operator!]?.component };
});

const isInOrNotInOperator = computed<boolean>(() =>
  ["NOT IN", "IN"].includes(props.conditionItem.operator!)
);

const isExplained = computed<boolean>(() =>
  aiConditionUuids.value.includes(props.conditionItem.condUuid!)
);

const options = computed<OperatorOption[]>(() => {
  if (formFields.value.fieldDataType === "Number") {
    return Object.values(operatorOptions) as OperatorOption[];
  }
  return [
    { value: "==", component: EqualIcon },
    { value: "!=", component: NotEqualIcon },
    { value: "IN", component: InIcon },
    { value: "NOT IN", component: NotInIcon },
  ];
});

const isSelected = computed<boolean>(
  () => selectedNodeId.value === props.conditionItem.condUuid
);

const valueString = computed<string[]>(() =>
  fieldValues.value.map(({ value }) => value).filter(Boolean)
);

watch(isInOrNotInOperator, (value) => {
  if (value) {
    fieldValues.value = generateItems(fieldValue.value);
  }
});

watch(isOpenValueOption, (value) => {
  if (!value) {
    formFields.value.value = valueString.value.toString();
    if (fieldValues.value.length > 3) {
      const oldFields = fieldValues.value.slice(0, 3);
      const newFields = fieldValues.value
        .slice(3)
        .filter((item) => item.value !== "");
      fieldValues.value = [...oldFields, ...newFields];
    }
  }
});

onBeforeMount(() => {
  if (isInOrNotInOperator.value) {
    fieldValues.value = generateItems(fieldValue.value);
  }
});

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});

const handleClosePopupConfirm = (): void => {
  isShowConfirmDelete.value = false;
};

const handleSubmitPopupConfirm = (): void => {
  isTested.value = false;
  passedCondUuids.value = [];
  failedCondUuids.value = [];
  passed.value = false;
  passedMessage.value = null;
  emit("delete-node", props.conditionItem.condUuid);
  isShowConfirmDelete.value = false;
};

const generateItems = (valueString: string): FieldValues[] => {
  const values = valueString?.trim()?.split(",").filter(Boolean);
  const numItems = Math.max(values.length, 3);
  return Array.from({ length: numItems }, (_, index) => ({
    uuid: uuidv4(),
    value: values[index as number] || "",
  }));
};

const handleAddNewFieldItem = (): void => {
  isScrollWhenAddNew.value = false;
  const newField = {
    uuid: uuidv4(),
    value: "",
  };
  fieldValues.value = [...fieldValues.value, newField];
  nextTick(() => {
    isScrollWhenAddNew.value = true;
    const element =
      instance?.proxy?.$refs[`field-item-${newField.uuid}`]![0]?.$el;
    currentY.value = element.offsetTop + 35;
    setTimeout(() => {
      const inputElement = element.querySelector("input");
      if (inputElement) inputElement.focus();
    }, 1000);
  });
};

const onlyNumber = (event: any): void => {
  if (props.conditionItem.fieldDataType === "String") return;
  let keyCode = event.keyCode ? event.keyCode : event.which;
  if (keyCode < 48 || keyCode > 57) {
    event.preventDefault();
  }
};

const handleBlurValue = (): void => {
  formFields.value.value = fieldValue.value;
};

const handleDeleteNode = (): void => {
  const requiredFields = ["keyName", "dispName", "operator", "value"];
  const isEmptyAllField = requiredFields.every(
    (field) => !props.conditionItem[field as keyof Condition]
  );
  if (isEmptyAllField) {
    isTested.value = false;
    passedCondUuids.value = [];
    failedCondUuids.value = [];
    passedMessage.value = null;
    passed.value = false;
    emit("delete-node", props.conditionItem.condUuid);
    return;
  }
  isShowConfirmDelete.value = true;
};

const handleClickConditionItem = (): void => {
  if (!isEditRuleStructure.value) return;
  setSelectedNodeId(props.conditionItem.condUuid!);
};

const handleSelectOperator = (operator: OperatorValue): void => {
  formFields.value.operator = operator;
  isOpenOperatorOption.value = false;
};

const handleToggleOperator = (): void => {
  if (!formFields.value.fieldDataType || !isEditRuleStructure.value) return;
  isOpenOperatorOption.value = !isOpenOperatorOption.value;
};

const handleToggleValue = (): void => {
  if (!isInOrNotInOperator.value) return;
  isOpenValueOption.value = !isOpenValueOption.value;
};

const handleKeyupEnter = (): void => {
  isOpenValueOption.value = false;
};

const handleDrop = (event: DragEvent): void => {
  event.preventDefault();
  if (!event.dataTransfer) return;
  const conditionField =
    (JSON.parse(event.dataTransfer.getData("condition-field")) as IFieldItem) ||
    null;
  if (conditionField) {
    setSelectedNodeId(props.conditionItem.condUuid!);
    formFields.value.fieldDataType = conditionField.fieldDataType;
    formFields.value.dispName = conditionField.fieldDispName;
    formFields.value.keyName = conditionField.fieldKeyName;
    formFields.value.fieldUuid = conditionField.fieldUuid;
    formFields.value.value = "";
    nextTick(() => {
      const isValidOperator = options.value
        .map(({ value }) => value)
        .includes(formFields.value.operator as OperatorValue);
      if (!isValidOperator) {
        formFields.value.operator = "";
      }
    });
  }
};

const handleDragover = (event: DragEvent): void => {
  event.preventDefault();
};

const handleClickOutside = (event: Event): void => {
  if (
    operatorOptionsRef.value &&
    operatorRef.value &&
    !operatorOptionsRef.value.contains(event.target as Node) &&
    !operatorRef.value.contains(event.target as Node)
  ) {
    isOpenOperatorOption.value = false;
  }
  if (
    isInOrNotInOperator.value &&
    valueOptionsRef.value &&
    valueRef.value &&
    !valueOptionsRef.value.contains(event.target as Node) &&
    !valueRef.value.contains(event.target as Node)
  ) {
    isOpenValueOption.value = false;
  }
};

const handleConditionClick = () => {
  if (!isEditRuleStructure.value) return;
  isShowRuleField.value = true;
  isShowRuleTest.value = false;
  isShowRuleList.value = false;
  isShowRuleReport.value = false;
  if (testRule.value.length > 0) {
    testRuleTemp.value = cloneDeep(testRule.value);
  }
};
</script>

<style lang="scss" scoped>
.condition-item {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  width: 300px;
  height: 40px;
  border-radius: 8px;
  background-color: #fff;
  cursor: pointer;
  transition: all 0.2s linear;
  z-index: 2;

  &.is-focused {
    border-color: v-bind(defaultBorderActive);
  }

  &.is-edit {
    &:hover {
      // border-color: v-bind(defaultBorderActive);
    }
  }

  &.is-explained {
    &::before {
      content: "";
      position: absolute;
      width: 100%;
      height: 100%;
      border-radius: 8px;
      border-left: 3px solid #e0332d;
      z-index: -1;
    }
  }

  &__field {
    flex: 1 1 160px;
    display: flex;
    align-items: center;
    max-width: 160px;
    width: 100%;
    height: 100%;
    padding: 10px 0 10px 12px;
    color: #3a3b3d;
    font-family: Noto Sans KR;
    font-weight: 500;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;

    &--empty {
      font-weight: 400;
      color: #bdc1c7;
    }

    &.is-expanded {
      max-width: 140px;
    }

    &-value {
      max-width: 160px;
      width: 100%;

      &.is-expanded {
        max-width: 140px;
      }
    }
  }

  &__name {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    color: #3a3b3d;
    font-family: Noto Sans KR;
    font-weight: 500;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
  }

  &__operator {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 24px;
    height: 24px;
    border-radius: 50%;
    background-color: #fff;
    border: 1px solid #e6e9ed;
    padding: 6px;
    flex-shrink: 0;
    cursor: pointer;
    transition: all 0.2s linear;

    &.is-expanded {
      width: 58px;
      border-radius: 999px;
    }

    &.is-focused {
      background-color: #525457;
    }

    &::before,
    &::after {
      content: "";
      position: absolute;
      width: 1px;
      height: 8px;
      background-color: #e6e9ed;
      left: 50%;
      transform: translateX(-50%);
    }

    &::before {
      top: -8px;
    }

    &::after {
      bottom: -8px;
    }
  }

  &__value {
    position: relative;
    flex: 1 1 100px;
    display: flex;
    align-items: center;
    max-width: 100px;
    width: 100%;
    height: 100%;
    padding: 10px 12px 10px 0;
  }

  &__input {
    width: 100%;
    height: 20px;
    border: none;
    outline: none;
    font-family: Noto Sans KR;
    font-weight: 400;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
    text-align: center;
    color: #3a3b3d;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    transition: all 0.2s linear;

    &.is-fail {
      color: #c7291d;
    }

    &[type="number"] {
      -moz-appearance: textfield;
    }

    &::-webkit-outer-spin-button,
    &::-webkit-inner-spin-button {
      -webkit-appearance: none;
    }

    &::placeholder {
      color: #bdc1c7;
    }

    &-number {
      position: absolute;
      top: 50%;
      transform: translateY(-50%);
      right: -11px;
      display: flex;
      align-items: center;
      justify-content: center;
      width: 20px;
      height: 20px;
      border-radius: 50%;
      font-family: Noto Sans KR;
      font-weight: 400;
      font-size: 9px;
      letter-spacing: 0.5px;
      text-align: center;
      color: #3a3b3d;
      z-index: 99;
      background: #fff;
      border: 1px solid #dce0e5;
      cursor: pointer;
      transition: all 0.2s linear;

      &.is-focused {
        border-color: v-bind(defaultBorderActive);
      }
    }
  }

  &__close {
    position: absolute;
    top: -23px;
    left: auto;
    right: 10px;
    background: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    border: 1px solid #dce0e5;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.2s linear;

    &:hover {
      background-color: #e9ebf0;
    }
  }

  &__error {
    position: absolute;
    top: 10px;
    left: calc(50% - 175px);
    background: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    border: 1px solid #e0332d;
    box-shadow: 0px 0px 0px 4px #e0332d29;
    z-index: 99;
  }
}

.operator-options {
  position: absolute;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  box-shadow: 0px 0px 16px 0px rgba(116, 147, 206, 0.2392156863);
  border-radius: 12px;
  background-color: #fff;
  z-index: 999;
  top: 38px;
  left: calc(50% + 16px);

  &__item {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 6px;
    width: 24px;
    height: 24px;
    border: 1px solid #e6e9ed;
    border-radius: 50%;
    cursor: pointer;
    transition: all 0.2s linear;

    &:hover {
      background-color: #e9ebf0;
    }

    &.is-expanded {
      width: 58px;
      border-radius: 999px;
    }
  }
}

.condition-item-value {
  position: absolute;
  top: 0px;
  left: calc(50% + 65px);
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 180px;
  border-radius: 12px;
  background-color: #fff;
  padding: 8px;
  box-shadow:
    0px 18px 88px -4px #18274b1f,
    0px 8px 32px -6px #18274b0f;
  z-index: 999;

  &__action {
    display: flex;
    justify-content: center;
  }

  &__add {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow:
      2px 1px 6px 0px #1e265b3d,
      2px 1px 18px 0px #1518421c inset,
      -5px -2px 6px 0px #ffffffa3 inset;
  }
}

:deep(.custom-text-field .v-field__field) {
  height: 30px;
}

:deep(.custom-text-field .v-field) {
  height: 30px;
}

:deep(.custom-text-field .v-field__input) {
  padding: 0 0 0 16px;
  height: 30px;
  min-height: 30px;
}

:deep(.custom-text-field .v-input__control) {
  height: 30px;
}
</style>
