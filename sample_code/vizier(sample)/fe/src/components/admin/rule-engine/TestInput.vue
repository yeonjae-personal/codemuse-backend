<template>
  <div class="bg-white p-[24px] h-full rounded-lg relative">
    <div class="flex justify-between items-center pt-2 pb-4">
      <div class="flex align-center gap-2 items-end">
        <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
          {{ $t("product_platform.testInput") }}
        </h1>
      </div>
    </div>
    <LocomotiveComponent
      scroll-container-class="!h-[calc(100vh-274px)]"
      scroll-content-class="h-full"
      is-dynamic-scroll
    >
      <div :key="componentKey" class="input-wrapper">
        <div v-for="item in testRule" :key="item.fieldUuid" class="info-item">
          <div class="item-key">
            <CustomTooltip :content="item.dispName" location="bottom">
              <span class="item-key__value">{{ item.dispName }}</span>
            </CustomTooltip>
          </div>
          <div class="item-value">
            <BaseInputText
              v-model="item.value"
              :class="{ 'is-failed': isConditionFail(item.keyName) }"
              @keypress="(event) => onlyNumber(event, item)"
            />
          </div>
        </div>
      </div>
      <h1 class="font-medium text-base text-text-base tracking-[0.5px] mt-4">
        {{ $t("product_platform.message") }}
      </h1>
      <div v-if="passed" class="input-success-value">
        {{ passedMessage }}
      </div>
    </LocomotiveComponent>
    <div class="flex justify-end pt-3 gap-2">
      <BaseButton :color="ButtonColorType.Gray" @click="handleCancel">
        {{ t("product_platform.cancel") }}
      </BaseButton>
      <BaseButton :color="ButtonColorType.Secondary" @click="handleExecute">
        {{ $t("product_platform.test") }}
      </BaseButton>
    </div>
    <BasePopup
      v-if="isShowPopupCancel"
      v-model="isShowPopupCancel"
      :content="t('product_platform.desc_cancel')"
      :icon="DialogIconType.Warning"
      :cancel-button-text="t('product_platform.btn_no')"
      :submit-button-text="t('product_platform.btn_yes')"
      @on-close="handleClosePopupCancel"
      @on-submit="handleSubmitPopupCancel"
    />
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import uniqBy from "lodash-es/uniqBy";
import { useSnackbarStore } from "@/store";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
import { ButtonColorType, DialogIconType } from "@/enums";
import { type Condition } from "@/interfaces/admin/rule-engine";
const {
  testRuleStructure,
  validateRuleStructure,
  setSelectedNodeId,
  collectConditions,
  updateRuleTest,
} = useRuleEngineStore();
const { showSnackbar } = useSnackbarStore();
const { t } = useI18n();
const {
  ruleStructure,
  requiredCondUuids,
  testRule,
  testRuleTemp,
  failedCondUuids,
  isShowRuleTest,
  isTested,
  passed,
  passedCondUuids,
  passedMessage,
  isRefresh,
  isShowRuleList,
  isExpanded,
  isDeletedCondition,
} = storeToRefs(useRuleEngineStore());

const failKeyUuids = ref<string[]>([]);
const componentKey = ref<number>(0);
const isShowPopupCancel = ref<boolean>(false);

watch(isDeletedCondition, (value) => {
  if (value) {
    nextTick(() => {
      componentKey.value++;
      if (!ruleStructure.value) return;
      const conditions = collectConditions(ruleStructure.value);
      updateRuleTest(uniqBy(conditions, "keyName"));
      if (conditions.length === 0) {
        isShowRuleTest.value = false;
      }
      isDeletedCondition.value = false;
    });
  }
});

watch(
  failedCondUuids,
  (value) => {
    if (value.length === 0 || !ruleStructure.value) {
      failKeyUuids.value = [];
    } else {
      const conditions = collectConditions(ruleStructure.value);
      const filterCondition = conditions.filter(({ condUuid }) =>
        failedCondUuids.value.includes(condUuid!)
      );
      failKeyUuids.value = [
        ...new Set(filterCondition.map(({ keyName }) => keyName) as string[]),
      ];
    }
  },
  { immediate: true }
);

const isConditionFail = (key: string | undefined): boolean => {
  if (!key) return false;
  return isTested.value && failKeyUuids.value.includes(key);
};

const handleClosePopupCancel = (): void => {
  isShowPopupCancel.value = false;
};

const handleCancel = (): void => {
  isShowPopupCancel.value = true;
};

const handleSubmitPopupCancel = (): void => {
  isTested.value = false;
  passed.value = false;
  passedCondUuids.value = [];
  failedCondUuids.value = [];
  isShowRuleTest.value = false;
  testRule.value = [];
  testRuleTemp.value = [];
  requiredCondUuids.value = [];
  passedMessage.value = null;
  isRefresh.value = true;
  if (!isExpanded.value) {
    isShowRuleList.value = true;
  }
};

const transformData = (): Record<string, string | number> => {
  return testRule.value
    .filter(({ value }) => value !== "")
    .map((field) => ({
      ...field,
      value:
        field.fieldDataType === "Number" ? Number(field.value) : field.value,
    }))
    .reduce<Record<string, string | number>>((acc, item) => {
      acc[item.keyName as string] = item.value as string | number;
      return acc;
    }, {});
};

const handleExecute = async (): Promise<void> => {
  setSelectedNodeId("");
  const invalidConditions = validateRuleStructure(ruleStructure.value!);
  if (invalidConditions.length > 0) {
    requiredCondUuids.value = invalidConditions.map(
      ({ condUuid }) => condUuid!
    );
    showSnackbar(t("product_platform.required_field_missing"), "error");
    return;
  }
  requiredCondUuids.value = [];
  const fields = transformData();
  await testRuleStructure(fields);
};

const onlyNumber = ($event, item: Condition): void => {
  if (item.fieldDataType === "String") return;
  let keyCode = $event.keyCode ? $event.keyCode : $event.which;
  if (keyCode < 48 || keyCode > 57) {
    $event.preventDefault();
  }
};
</script>

<style lang="scss" scoped>
.input-wrapper {
  display: flex;
  flex-direction: column;
  row-gap: 8px;
  padding: 12px;
  background-color: #f7f8fa;
  border-radius: 8px;
  .info-item {
    display: flex;
    padding: 6px 0;
    height: 32px;
    align-items: center;
    .item-key {
      flex: 1;
      padding-right: 8px;
      display: flex;
      align-items: center;
      max-width: 50%;

      &__value {
        text-align: left;
        font-size: 13px;
        font-weight: 500;
        color: #6b6d70;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
    .item-value {
      flex: 1;
    }
  }
}

.input-success-value {
  width: 100%;
  min-height: 41.5px;
  margin-top: 16px;
  padding: 10px 12px;
  border: 1px solid #abefc6;
  background-color: #ecfdf3;
  border-radius: 12px;
  font-family: Noto Sans KR;
  font-weight: 500;
  font-size: 13px;
  line-height: 150%;
  letter-spacing: 0.25px;
  vertical-align: middle;
  color: #079455;
  word-wrap: break-word;
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

:deep(.scroll-container) {
  padding: 0;
}

.is-failed {
  border-color: #e0332d;
}

:deep(.custom-text-field.is-failed .v-field__input) {
  color: #c7291d;
}
</style>
