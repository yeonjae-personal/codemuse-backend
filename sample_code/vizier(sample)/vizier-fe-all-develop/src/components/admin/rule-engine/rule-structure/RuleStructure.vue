<template>
  <div :class="['rule-structure', { 'is-expanded': isExpanded }]">
    <div class="rule-structure-header">
      <div class="rule-structure-header__title">
        {{ t("product_platform.ruleStructure") }}
        <span v-if="isExpanded || isShowRuleReport">
          - {{ ruleDetail.ruleName }}</span
        >
      </div>
      <div class="rule-structure-header__action">
        <BaseButton
          :color="ButtonColorType.Gray"
          :width="WIDTH_BUTTON.ICON"
          @click="handleAIClick"
        >
          <AIIcon />
        </BaseButton>
        <BaseButton
          :color="ButtonColorType.Gray"
          :width="WIDTH_BUTTON.ICON"
          @click="handleToggleExpanded"
        >
          <ExpandedIcon v-if="!isExpanded" />
          <CollapsedIcon v-else />
        </BaseButton>
        <BaseButton :color="ButtonColorType.Secondary" @click="handleTestRule">
          <CheckCircle class="mr-[6px]" :size="16" color="#BA1642" />
          {{ $t("product_platform.test") }}
        </BaseButton>
        <template v-if="!isEditRuleStructure">
          <BaseButton
            :color="ButtonColorType.Secondary"
            @click="handleEditRuleStructure"
          >
            <EditIcon class="mr-[6px]" />
            {{ $t("product_platform.edit") }}
          </BaseButton>
        </template>
        <template v-else>
          <BaseButton
            :color="ButtonColorType.Gray"
            @click="handleCancelEditStructure"
          >
            {{ t("product_platform.cancel") }}
          </BaseButton>

          <BaseButton :color="ButtonColorType.Secondary" @click="handleSave">
            <SaveIcon class="mr-[6px]" />
            {{ $t("product_platform.save") }}
          </BaseButton>
        </template>
      </div>
    </div>
    <div
      ref="viewport"
      :class="[
        'rule-structure-area',
        {
          'is-success': passed,
          'is-failed': !passed && isTested,
          'is-dragging': isDragging,
          'is-scrolling': isScrolling,
          'is-edit': isEditRuleStructure,
        },
      ]"
      @mousedown="startDrag"
      @wheel="onScroll"
    >
      <div ref="dragArea" class="drag-area">
        <div ref="dragAreaContent" class="drag-area__content">
          <StartNode @select-type="handleCreateNode($event)" />
          <template v-if="!!ruleStructure">
            <AndConditionGroup
              v-if="isStartAndGroup"
              :key="componentKey"
              :condition="ruleStructure.condition"
            />
            <OrConditionGroup
              v-if="isStartOrGroup"
              :key="componentKey"
              :condition-group="ruleStructure as ConditionGroup"
              :uuid="parentId"
            />
          </template>
          <MessageNode ref="target" />
        </div>
      </div>
    </div>
    <div
      v-if="!targetIsVisible"
      :class="['rule-structure__overflow', { 'is-expanded': isExpanded }]"
    >
      <MessageNode class="message-node-clone" />
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
    <BasePopup
      v-if="isShowPopupSaveConfirm"
      v-model="isShowPopupSaveConfirm"
      :content="t('product_platform.desc_update')"
      :icon="DialogIconType.Info"
      :cancel-button-text="t('product_platform.btn_no')"
      :submit-button-text="t('product_platform.btn_yes')"
      @on-close="handleClosePopupSaveConfirm"
      @on-submit="handleSubmitPopupSaveConfirm"
    />
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import uniqBy from "lodash-es/uniqBy";
import cloneDeep from "lodash-es/cloneDeep";
import { useIntersectionObserver } from "@vueuse/core";
import { useSnackbarStore } from "@/store";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
import { EMPTY_RULE } from "@/constants/admin/rule-engine";
import { ButtonColorType, DialogIconType } from "@/enums";
import AndConditionGroup from "./AndConditionGroup.vue";
import MessageNode from "./MessageNode.vue";
import OrConditionGroup from "./OrConditionGroup.vue";
import StartNode from "./StartNode.vue";
import {
  ConditionGroup,
  Condition,
  LogicType,
  ruleEngineProvider,
} from "@/interfaces/admin/rule-engine";
import { WIDTH_BUTTON } from "@/constants/index";

const {
  ruleStructure,
  ruleDetail,
  ruleMsg,
  ruleStructureTemp,
  tempRuleMsg,
  requiredCondUuids,
  isStartAndGroup,
  isStartOrGroup,
  isRefresh,
  isShowRuleTest,
  isShowRuleField,
  isShowRuleDetail,
  isShowRuleList,
  isShowRuleReport,
  isShowReport,
  isEditRuleStructure,
  passed,
  passedCondUuids,
  passedMessage,
  failedCondUuids,
  isTested,
  isExpanded,
  ruleValidation,
  testRule,
  testRuleTemp,
  ruleReportContent,
} = storeToRefs(useRuleEngineStore());
const {
  createStructureNode,
  updateRuleTest,
  saveRuleStructure,
  collectConditions,
  validateRuleStructure,
  setSelectedNodeId,
  resetRuleStructure,
  aiValidateRule,
} = useRuleEngineStore();
const { showSnackbar } = useSnackbarStore();
const { t } = useI18n();

const target = ref<HTMLDivElement>();
const viewport = ref<HTMLDivElement | null>(null);
const dragArea = ref<HTMLDivElement | null>(null);
const dragAreaContent = ref<HTMLDivElement | null>(null);
const isDragging = ref<boolean>(false);
const isScrolling = ref<boolean>(false);
const componentKey = ref<number>(0);
const startX = ref<number>(0);
const startY = ref<number>(0);
const offsetX = ref<number>(0);
const offsetY = ref<number>(0);
const scrollOffsetY = ref<number>(0);
const animationFrameId = ref<null | number>(null);
const targetIsVisible = ref<boolean>(false);
const isShowPopupCancel = ref<boolean>(false);
const isShowPopupSaveConfirm = ref<boolean>(false);
const cancelType = ref<"cancel" | "cancelAI" | "cancelTest" | null>(null);

provide(ruleEngineProvider, { isDragging });

useIntersectionObserver(target, ([entry]) => {
  nextTick(() => {
    targetIsVisible.value = entry?.isIntersecting || false;
  });
});

const parentId = computed<string>(() => {
  const firstCondition = ruleStructure.value?.condition?.[0] as
    | Condition
    | undefined;
  return firstCondition?.condUuid ?? "";
});

watch(
  () => ruleStructure.value,
  () => {
    nextTick(() => {
      componentKey.value++;
      requiredCondUuids.value = [];
    });
  },
  { deep: true }
);

watch(isRefresh, (value) => {
  if (value) {
    handleRefreshStructure();
    isRefresh.value = false;
  }
});

onBeforeMount(() => {
  updateCSSVariable("--border-width", "2px");
});

onMounted(() => {
  handleRefreshStructure();
  document.addEventListener("mouseup", endDrag);
  window.addEventListener("resize", handleResize);
});

onUnmounted(() => {
  document.removeEventListener("mouseup", endDrag);
  window.removeEventListener("resize", handleResize);
});

const updateCSSVariable = (key: string, value: string): void => {
  document.documentElement.style.setProperty(key, value);
};

const handleResize = (): void => {
  if (!viewport.value || !dragArea.value) return;
  const viewportRect = viewport.value.getBoundingClientRect();
  const canvasRect = dragArea.value.getBoundingClientRect();
  offsetX.value = (viewportRect.width - canvasRect.width) / 2 - 21;
  offsetY.value = 0;
  dragArea.value.style.transform = `translate3d(${Math.round(
    offsetX.value
  )}px, ${Math.round(offsetY.value)}px, 0)`;
};

const handleRefreshStructure = (): void => {
  nextTick(() => {
    if (!viewport.value || !dragArea.value) return;
    const viewportRect = viewport.value.getBoundingClientRect();
    const canvasRect = dragArea.value.getBoundingClientRect();
    offsetX.value = (viewportRect.width - canvasRect.width) / 2 - 21;
    offsetY.value = 0;
    dragArea.value.style.transform = `translate3d(${Math.round(
      offsetX.value
    )}px, ${Math.round(offsetY.value)}px, 0)`;
  });
};

const startDrag = (event: MouseEvent): void => {
  if ((event.target as HTMLElement).tagName === "INPUT") return;
  event.stopPropagation();
  event.stopImmediatePropagation();
  event.preventDefault();
  if (!viewport.value || !dragArea.value || isScrolling.value) return;
  isDragging.value = true;
  startX.value = event.clientX - offsetX.value;
  startY.value = event.clientY - offsetY.value;
  document.addEventListener("mousemove", onDrag);
  document.addEventListener("mouseup", endDrag);
};

const endDrag = (event: MouseEvent): void => {
  event.stopPropagation();
  event.stopImmediatePropagation();
  if (!viewport.value) return;
  isDragging.value = false;
  document.removeEventListener("mousemove", onDrag);
  document.removeEventListener("mouseup", endDrag);
};

const onDrag = (event: MouseEvent): void => {
  nextTick(() => {
    if (!isDragging.value || !dragArea.value || !dragAreaContent.value) return;
    const newOffsetY = event.clientY - startY.value;
    const newOffsetX = event.clientX - startX.value;
    offsetY.value = Math.max(
      Math.min(newOffsetY, 0),
      -(dragArea.value.scrollHeight - dragArea.value.clientHeight)
    );
    offsetX.value = Math.max(
      Math.min(
        newOffsetX,
        dragArea.value.scrollWidth - dragArea.value.clientWidth
      ),
      -(dragArea.value.scrollWidth - dragArea.value.clientWidth)
    );
    dragArea.value.style.transform = `translate3d(${Math.round(
      offsetX.value
    )}px, ${Math.round(offsetY.value)}px, 0)`;
  });
};

const onScroll = (event: WheelEvent): void => {
  if (
    !dragArea.value ||
    isDragging.value ||
    !dragAreaContent.value ||
    event.ctrlKey
  )
    return;
  isScrolling.value = true;
  const newScrollOffsetY = scrollOffsetY.value - event.deltaY * 0.5;
  const maxScrollY = 0;
  scrollOffsetY.value = Math.max(
    Math.min(newScrollOffsetY, maxScrollY),
    -(dragArea.value.scrollHeight - dragArea.value.clientHeight)
  );
  startAnimation();
};

const startAnimation = (): void => {
  if (animationFrameId.value) cancelAnimationFrame(animationFrameId.value);
  animationFrameId.value = requestAnimationFrame(() => {
    if (!dragArea.value) return;
    offsetY.value += (scrollOffsetY.value - offsetY.value) * 0.1;
    dragArea.value.style.transform = `translate3d(${Math.round(
      offsetX.value
    )}px, ${Math.round(offsetY.value)}px, 0)`;
    if (Math.abs(scrollOffsetY.value - offsetY.value) > 0.5) {
      startAnimation();
    } else {
      isScrolling.value = false;
    }
  });
};

const handleCreateNode = (logicType: LogicType): void => {
  createStructureNode(logicType, true);
};

const handleEditRuleStructure = (): void => {
  isEditRuleStructure.value = true;
  setSelectedNodeId("");
};

const handleCancelEditStructure = (): void => {
  cancelType.value = "cancel";
  isShowPopupCancel.value = true;
  setSelectedNodeId("");
};

const handleClosePopupCancel = () => {
  isShowPopupCancel.value = false;
};

const handleSubmitPopupCancel = (): void => {
  if (!cancelType.value) return;
  switch (cancelType.value) {
    case "cancel": {
      handleRefreshStructure();
      resetRuleStructure();
      setSelectedNodeId("");
      isShowPopupCancel.value = false;
      isEditRuleStructure.value = false;
      isShowRuleField.value = false;
      isShowRuleTest.value = false;
      isShowRuleReport.value = false;
      if (!isExpanded.value) {
        isShowRuleList.value = true;
        isShowRuleDetail.value = true;
      }
      break;
    }
    case "cancelTest": {
      handleCancelTestValue();
      isShowPopupCancel.value = false;
      break;
    }
    case "cancelAI": {
      handleCancelAIValue();
      isShowPopupCancel.value = false;
      break;
    }
    default:
      break;
  }
};

const handleClosePopupSaveConfirm = (): void => {
  isShowPopupSaveConfirm.value = false;
};

const handleSubmitPopupSaveConfirm = async (): Promise<void> => {
  isShowPopupSaveConfirm.value = false;
  const isSaveSuccess = await saveRuleStructure();
  if (isSaveSuccess) {
    isShowRuleField.value = false;
    isShowRuleReport.value = false;
    isShowRuleList.value = true;
    isShowRuleDetail.value = true;
    isEditRuleStructure.value = false;
    passedCondUuids.value = [];
    isShowRuleTest.value = false;
    passedMessage.value = null;
    failedCondUuids.value = [];
    passed.value = false;
    isTested.value = false;
    ruleValidation.value = null;
    isExpanded.value = false;
    ruleReportContent.value = "";
    tempRuleMsg.value = cloneDeep(ruleMsg.value);
    ruleStructureTemp.value = cloneDeep(ruleStructure.value);
    showSnackbar(t("product_platform.saveSuccessfully"), "success");
  } else {
    showSnackbar(t("product_platform.dashboard.saveFailed"), "error");
  }
};

const handleAIClick = (): void => {
  setSelectedNodeId("");
  if (
    !ruleStructure.value ||
    JSON.stringify(ruleStructure.value) === JSON.stringify(EMPTY_RULE)
  ) {
    showSnackbar(t("product_platform.please_add_least_condition"), "error");
    return;
  }
  const invalidConditions = validateRuleStructure(ruleStructure.value!);
  if (invalidConditions.length > 0) {
    requiredCondUuids.value = invalidConditions.map(
      ({ condUuid }) => condUuid!
    );
    showSnackbar(t("product_platform.required_field_missing"), "error");
    return;
  }
  if (passedCondUuids.value.length || failedCondUuids.value.length) {
    cancelType.value = "cancelTest";
    isShowPopupCancel.value = true;
    return;
  }
  handleCancelTestValue();
};

const handleCancelTestValue = (): void => {
  isShowRuleReport.value = true;
  isShowRuleList.value = false;
  testRuleTemp.value = [];
  testRule.value = [];
  isShowRuleDetail.value = false;
  isShowRuleField.value = false;
  isShowRuleTest.value = false;
  isShowReport.value = false;
  passedCondUuids.value = [];
  passedMessage.value = null;
  failedCondUuids.value = [];
  passed.value = false;
  isTested.value = false;
  aiValidateRule();
};

const handleToggleExpanded = (): void => {
  isExpanded.value = !isExpanded.value;
  if (isExpanded.value) {
    isShowRuleList.value = false;
    isShowRuleDetail.value = false;
    isShowRuleReport.value = false;
  } else {
    isShowRuleList.value = true;
    isShowRuleDetail.value = true;
  }
  isShowRuleField.value = false;
  isShowRuleTest.value = false;
};

const handleTestRule = (): void => {
  if (
    !ruleStructure.value ||
    JSON.stringify(ruleStructure.value) === JSON.stringify(EMPTY_RULE)
  ) {
    showSnackbar(t("product_platform.please_add_least_condition"), "error");
    return;
  }
  const conditions = collectConditions(ruleStructure.value);
  const isEmptyAllCondition = conditions.every((item) => !!item.fieldUuid);
  if (!isEmptyAllCondition) {
    showSnackbar(
      t("product_platform.please_add_fields_for_condition"),
      "error"
    );
    return;
  }
  setSelectedNodeId("");
  updateRuleTest(uniqBy(conditions, "keyName"));
  if (!!ruleValidation.value || !!ruleReportContent.value) {
    cancelType.value = "cancelAI";
    isShowPopupCancel.value = true;
    return;
  }
  handleCancelAIValue();
};

const handleCancelAIValue = (): void => {
  isShowRuleField.value = false;
  isShowRuleList.value = false;
  isShowRuleReport.value = false;
  isShowRuleTest.value = true;
  ruleValidation.value = null;
  ruleReportContent.value = "";
};

const handleSave = (): void => {
  setSelectedNodeId("");
  if (
    !ruleStructure.value ||
    JSON.stringify(ruleStructure.value) === JSON.stringify(EMPTY_RULE)
  ) {
    showSnackbar(t("product_platform.please_add_least_condition"), "error");
    return;
  }
  const invalidConditions = validateRuleStructure(ruleStructure.value!);
  if (invalidConditions.length > 0) {
    requiredCondUuids.value = invalidConditions.map(
      ({ condUuid }) => condUuid!
    );
    showSnackbar(t("product_platform.required_field_missing"), "error");
    return;
  }
  if (
    JSON.stringify(ruleStructure.value) ===
      JSON.stringify(ruleStructureTemp.value) &&
    ruleMsg.value === tempRuleMsg.value
  ) {
    isEditRuleStructure.value = false;
    isShowRuleField.value = false;
    isShowRuleList.value = true;
    isShowRuleDetail.value = true;
    isShowRuleReport.value = false;
    isShowRuleTest.value = false;
    passedCondUuids.value = [];
    failedCondUuids.value = [];
    passedMessage.value = null;
    passed.value = false;
    isExpanded.value = false;
    isTested.value = false;
    ruleValidation.value = null;
    ruleReportContent.value = "";
    return;
  }
  requiredCondUuids.value = [];
  isShowPopupSaveConfirm.value = true;
};
</script>

<style lang="scss" scoped>
.rule-structure {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
  height: 100%;
  padding: 12px;
  background-color: #fff;
  border-radius: 12px;
  transition: all 0.2s linear;

  &.is-expanded {
    max-width: 100%;
  }

  &__overflow {
    width: calc(100% - 26px);
    max-width: 850px;
    height: 70px;
    background-color: #f0f2f5;
    position: absolute;
    bottom: 14px;
    border-radius: 12px;
    left: 50%;
    transform: translateX(-50%);

    &.is-expanded {
      max-width: 100%;
    }
  }
}

.rule-structure-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 12px 0;

  &__title {
    font-family: Noto Sans KR;
    font-weight: 500;
    font-size: 15px;
    line-height: 150%;
    letter-spacing: 0.5%;
    color: #3a3b3d;
  }

  &__action {
    display: flex;
    gap: 8px;
    align-items: center;
    justify-content: flex-end;
  }
}

.rule-structure-area {
  flex-grow: 1;
  position: relative;
  width: 100%;
  height: 100%;
  padding: 20px;
  background-color: #f0f2f5;
  border: 1px solid transparent;
  border-radius: 8px;
  overflow: hidden;
  cursor: grab;
  transition: all 0.1s linear;

  &.is-dragging {
    cursor: grabbing;
  }

  &.is-scrolling {
    cursor: move;
  }

  &.is-edit,
  &.is-failed {
    box-shadow: 0px 0px 0px 4px #d9325a29;
    border-color: #d9325a;
  }

  &.is-success {
    box-shadow: 0px 0px 0px 4px #17b26a29;
    border-color: #17b26a;
  }
}

.drag-area {
  position: absolute;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  flex-direction: column;
  width: calc(100% - 40px);
  height: calc(100% - 40px);
  zoom: 0.8;
  will-change: transform;

  &__content {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-direction: column;
  }
}

.message-node-clone {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  zoom: 80%;

  &::after,
  &::before {
    content: none;
  }
}
</style>
