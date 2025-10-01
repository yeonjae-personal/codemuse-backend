<template>
  <div
    v-if="conditionGroup.condition.length > 0"
    ref="orConditionRef"
    :class="[
      'or-condition-group',
      {
        'is-single': conditionGroup.condition.length === 1,
        'pb-0': isSingleGroup(),
      },
    ]"
  >
    <div
      v-for="(item, index) in conditionGroup.condition"
      :id="`${uuid}_orConditionItem`"
      :key="item.condUuid"
      :class="[
        'or-condition-group__item',
        {
          'is-middle': isMiddleLine(index),
          'is-success': passedCondUuids.includes(item?.condUuid!),
          'is-success-parent': isDrawSuccessLineTop(item),
        },
      ]"
    >
      <div
        v-show="conditionGroup.condition.length > 1"
        :id="`${uuid}-${index}-top`"
        :class="[
          'or-condition-group__top',
          {
            'is-success': isDrawSuccessLineTop(item),
          },
        ]"
      ></div>
      <template v-if="item.logicType === 'AND'">
        <AndConditionGroup
          :id="`${uuid + '-and-group'}`"
          :condition="item.condition as Condition[]"
          :uuid="`${uuid}-${index}`"
        />
      </template>
      <template v-else>
        <ConditionItem
          class="relative"
          :condition-item="item"
          :uuid="`${uuid}-${index}`"
          @delete-node="handleDeleteNode"
        />
        <SingleConnectionLine
          v-if="conditionGroup.condition.length > 1"
          :is-show-arrow="false"
          :class="{ 'is-middle-line': isShowLine(index) }"
          :is-success="
            isGroupPass({ condition: [item] } as ConditionGroup) &&
            isShowLine(index)
          "
          @select-type="handleCreateNode($event, item.condUuid!)"
        />
      </template>
      <div
        v-show="conditionGroup.condition.length > 1"
        :id="`${uuid}-${index}-bottom`"
        :class="[
          'or-condition-group__bottom',
          { 'is-success': isDrawSuccessLineBottom(item) },
        ]"
      ></div>
    </div>
    <div
      v-if="conditionGroup.condition.length % 2 === 0"
      class="or-condition-group__additional"
    ></div>
    <SingleConnectionLine
      :id="`${uuid + '-last-node'}`"
      :class="[
        'or-condition-group__line',
        {
          'is-small': isLastNode(),
          'is-hidden': !isLastNode(),
          'is-hidden-arrow': isHiddenArrow(),
          'is-success': isGroupPass(conditionGroup),
        },
      ]"
      @select-type="handleCreateGroupNode($event)"
    />
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import isEqual from "lodash-es/isEqual";
import cloneDeep from "lodash-es/cloneDeep";
import { useSnackbarStore } from "@/store";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
import AndConditionGroup from "./AndConditionGroup.vue";
import SingleConnectionLine from "./SingleConnectionLine.vue";
import ConditionItem from "./ConditionItem.vue";
import {
  type Condition,
  type ConditionGroup,
  type LogicType,
} from "@/interfaces/admin/rule-engine";

type Props = {
  conditionGroup: ConditionGroup;
  uuid?: string;
};

const props = defineProps<Props>();

const { t } = useI18n();

const {
  ruleStructure,
  selectedNodeId,
  passedCondUuids,
  isTested,
  isDeletedCondition,
} = storeToRefs(useRuleEngineStore());
const {
  findNextItem,
  createCondition,
  addConditionBelowGroup,
  getNewGroupCondition,
  findConditionByUuid,
  replaceConditionByUuid,
  findCurrentGroup,
  deleteConditionByUuid,
  isGroupPass,
  isGroupFail,
} = useRuleEngineStore();
const { showSnackbar } = useSnackbarStore();

const orConditionRef = ref<HTMLDivElement | null>(null);
const connectionWidth = ref<string>("");

const isShowLine = computed(() => {
  const totalConditions = props.conditionGroup.condition.length;
  return (index: number): boolean => {
    return totalConditions > 2 && index > 0 && index < totalConditions - 1;
  };
});

const isMiddleLine = computed(() => {
  const totalConditions = props.conditionGroup.condition.length;
  return (index: number): boolean => {
    return (
      totalConditions > 1 &&
      (totalConditions % index) + 1 === 1 &&
      index < totalConditions - 1
    );
  };
});

watch(
  () => props.conditionGroup,
  (oldValue, newValue) => {
    if (!isEqual(oldValue, newValue)) {
      nextTick(() => {
        handleCalculatorGroupWidth();
      });
    }
  },
  { deep: true }
);

onMounted(() => {
  nextTick(() => {
    handleCalculatorGroupWidth();
  });
});

const isDrawSuccessLineTop = (item: Condition | ConditionGroup): boolean => {
  if (!isTested.value || !item) return false;
  if ("logicType" in item) {
    return (
      isGroupPass(item as ConditionGroup) || isGroupFail(item as ConditionGroup)
    );
  }
  return (
    isGroupPass({ condition: [item] } as ConditionGroup) ||
    isGroupFail({ condition: [item] } as ConditionGroup)
  );
};

const isDrawSuccessLineBottom = (item: Condition | ConditionGroup): boolean => {
  if (!isTested.value || !item) return false;
  if ("logicType" in item) {
    return isGroupPass(item as ConditionGroup);
  }
  return isGroupPass({ condition: [item] } as ConditionGroup);
};

const handleCalculatorGroupWidth = (): void => {
  const parents = document.querySelectorAll<HTMLElement>(
    `[id^='${props.uuid}_orConditionItem']`
  );
  const parentWidths = Array.from(parents).map((node) => node.offsetWidth);
  if (parentWidths.length <= 1) return;
  const maxParentWidth = Math.max(...parentWidths);
  parents.forEach((node) => {
    if (node.offsetWidth === maxParentWidth) return;
    node.style.width = `${maxParentWidth}px`;
  });
  const length = props.conditionGroup.condition.length;
  const firstNode = document.getElementById(`${props.uuid}-${0}`);
  const secondNode = document.getElementById(`${props.uuid}-${length - 1}`);
  if (!firstNode || !secondNode) {
    connectionWidth.value = "0px";
    return;
  }
  const rectFirst = firstNode.getBoundingClientRect();
  const rectSecond = secondNode.getBoundingClientRect();
  connectionWidth.value = `${Math.abs(
    ((rectSecond.right - rectFirst.left) * 1.25 - 296) / 2
  )}px`;
  parentWidths.forEach((_item, index) => {
    if (index === 0) {
      // first item
      const firstTop = document.getElementById(`${props.uuid}-${0}-top`);
      const firstBottom = document.getElementById(`${props.uuid}-${0}-bottom`);
      if (!firstTop || !firstBottom) return;
      firstTop.style.left = `${maxParentWidth / 2 - 1}px`;
      firstTop.style.borderRadius = "16px 0 0 0";
      firstTop.style.borderLeft = "var(--border-width) solid #bdc1c7";
      firstBottom.style.left = `${maxParentWidth / 2 - 1}px`;
      firstBottom.style.borderRadius = "0 0 0 16px";
      firstBottom.style.borderLeft = "var(--border-width) solid #bdc1c7";
    } else if (index > 0 && index < parentWidths.length - 1) {
      const midIndex = Math.floor(parentWidths.length / 2);
      if (parentWidths.length % 2 === 1 && index === midIndex) {
        const elementIds = [
          `${props.uuid}-${index}-top`,
          `${props.uuid}-${index}-bottom`,
        ];
        const elements = elementIds.map((id) => document.getElementById(id));
        if (elements.some((el) => !el)) return;
        elements.forEach((el) => {
          el!.style.width = "2.5px";
          el!.style.borderLeft = `var(--border-width) solid #bdc1c7`;
        });
      } else {
        const elementIds = [
          `${props.uuid}-${index}-top`,
          `${props.uuid}-${index}-bottom`,
        ];
        const elements = elementIds.map((id) => document.getElementById(id));
        if (elements.some((el) => !el)) return;
        elements.forEach((el) => {
          el!.style.width = `${maxParentWidth / 2 + 20}px`; // 20 for gap
          if (index < midIndex) {
            el!.style.left = `${maxParentWidth / 2 - 1}px`;
            el!.style.borderLeft = `var(--border-width) solid #bdc1c7`;
          } else {
            el!.style.right = `${maxParentWidth / 2}px`;
            el!.style.borderRight = `var(--border-width) solid #bdc1c7`;
          }
        });
      }
    } else if (index === parentWidths.length - 1) {
      // last item
      const secondTop = document.getElementById(
        `${props.uuid}-${length - 1}-top`
      );
      const secondBottom = document.getElementById(
        `${props.uuid}-${length - 1}-bottom`
      );
      if (!secondTop || !secondBottom) return;
      secondTop.style.right = `${maxParentWidth / 2 - 1}px`;
      secondTop.style.borderRadius = "0 16px 0 0";
      secondTop.style.borderRight = "var(--border-width) solid #bdc1c7";
      secondBottom.style.right = `${maxParentWidth / 2 - 0.5}px`;
      secondBottom.style.borderRadius = "0 0 16px 0";
      secondBottom.style.borderRight = "var(--border-width) solid #bdc1c7";
    }
  });
};

const handleCreateNode = (logicType: LogicType, targetUuid: string): void => {
  const currentItem = findConditionByUuid(ruleStructure.value!, targetUuid);
  if (currentItem) {
    if (logicType === "AND") {
      const newCondition = createCondition() as Condition;
      const newGroup: ConditionGroup = {
        logicType: "AND",
        condition: [currentItem, newCondition],
      };
      replaceConditionByUuid(
        ruleStructure.value!,
        targetUuid,
        cloneDeep(newGroup)
      );
      selectedNodeId.value = newCondition.condUuid!;
      return;
    }
    const newOrGroup = getNewGroupCondition(logicType, 2);
    const newGroup: ConditionGroup = {
      logicType: "AND",
      condition: [currentItem, newOrGroup],
    };
    replaceConditionByUuid(
      ruleStructure.value!,
      targetUuid,
      cloneDeep(newGroup)
    );
    selectedNodeId.value = newOrGroup.condition[0].condUuid!;
  }
};

const findNextConditionAfterGroup = (
  parentGroup: ConditionGroup,
  targetGroup: ConditionGroup
): Condition | ConditionGroup | null => {
  if (!parentGroup?.condition) return null;
  for (let i = 0; i < parentGroup.condition.length; i++) {
    const item = parentGroup.condition[i as number];
    if (item === targetGroup) {
      return parentGroup.condition[i + 1];
    }
    if (item.logicType && item.condition?.length) {
      const nestedResult = findNextConditionAfterGroup(
        item as ConditionGroup,
        targetGroup
      );
      if (nestedResult) {
        return nestedResult;
      }
    }
  }
  return null;
};

const handleCreateGroupNode = (logicType: LogicType): void => {
  const nextItem = findNextItem(ruleStructure.value!, props.conditionGroup);
  const currentGroup = findCurrentGroup(
    ruleStructure.value!,
    props.conditionGroup!
  );
  const nextGroupCondition = findNextConditionAfterGroup(
    currentGroup!,
    props.conditionGroup
  );
  if (logicType === "AND") {
    const newCondition = createCondition() as Condition;
    addConditionBelowGroup(
      ruleStructure.value!,
      props.conditionGroup,
      newCondition
    );
    selectedNodeId.value = newCondition.condUuid!;
    return;
  }
  if (!!nextItem && nextItem.logicType === "OR") {
    const newCondition = createCondition() as Condition;
    nextItem.condition = [
      ...(nextItem.condition ?? []),
      newCondition,
    ] as Condition[];
    selectedNodeId.value = newCondition.condUuid!;
    return;
  }
  if (!!nextGroupCondition && nextGroupCondition.logicType === "OR") {
    const newCondition = createCondition() as Condition;
    nextGroupCondition.condition = [
      ...(nextGroupCondition.condition ?? []),
      newCondition,
    ] as Condition[];
    selectedNodeId.value = newCondition.condUuid!;
    return;
  }
  const newGroup = getNewGroupCondition(logicType, 2);
  addConditionBelowGroup(ruleStructure.value!, props.conditionGroup, newGroup);
  selectedNodeId.value = newGroup.condition[0].condUuid!;
};

const isLastNode = (): boolean => {
  let isValid = false;
  const group = findCurrentGroup(ruleStructure.value!, props.conditionGroup);
  group?.condition.forEach((item, index) => {
    if (item?.condition) {
      item.condition.some((child) => {
        if (JSON.stringify(child) === JSON.stringify(props.conditionGroup)) {
          isValid = index === 0 || index === group?.condition.length - 1;
        }
      });
    }
  });
  return isValid;
};

const isHiddenArrow = (): boolean => {
  const nextItem = findNextItem(ruleStructure.value!, props.conditionGroup);
  return (
    props.conditionGroup.condition.length <= 1 && nextItem?.logicType === "OR"
  );
};

const isSingleGroup = (): boolean => {
  return (
    props.conditionGroup?.logicType === "OR" &&
    props.conditionGroup?.condition.length === 1 &&
    props.conditionGroup?.condition[0].logicType === "AND"
  );
};

const handleDeleteNode = (nodeUuid: string): void => {
  deleteConditionByUuid(ruleStructure.value!, nodeUuid);
  isDeletedCondition.value = true;
  showSnackbar(t("product_platform.delete_condition_successfully"), "success");
};
</script>

<style lang="scss" scoped>
.or-condition-group {
  position: relative;
  display: flex;
  justify-content: center;
  flex-wrap: nowrap;
  gap: 40px;
  padding: 40px 0;
  background-color: #f0f2f5;

  &.is-single {
    padding-top: 0;
  }

  &__top {
    position: absolute;
    top: -40px;
    height: 40px;
    width: v-bind(connectionWidth);
    background-color: transparent;
    border-top: var(--border-width) solid #bdc1c7;
    z-index: 0;
    transition: all 0.2s linear;

    &.is-success {
      border-color: #17b26a !important;
      z-index: 1;
    }
  }

  &__bottom {
    position: absolute;
    bottom: -2px;
    height: 100%;
    width: v-bind(connectionWidth);
    background-color: transparent;
    border-bottom: var(--border-width) solid #bdc1c7;
    z-index: 0;
    transition: all 0.2s linear;

    &.is-success {
      border-color: #17b26a !important;
      z-index: 1;
    }
  }

  &__line {
    position: absolute;
    bottom: 0;
    height: 40px;

    &.is-small {
      &::before {
        border-color: transparent;
      }

      &::after {
        border-color: transparent;
      }
    }
  }

  &__additional {
    position: absolute;
    top: 1.5px;
    width: 10px;
    height: calc(100% - 41.2px);
    left: 50%;
    transform: translateX(-50%);
    background: #f0f2f5;
    z-index: 2;
  }

  &__item {
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;

    &::before {
      content: "";
      position: absolute;
      top: -7px;
      left: auto;
      border-left: 5px solid transparent;
      border-right: 5px solid transparent;
      border-top: 8px solid #bdc1c7;
      z-index: 0;
    }

    &::after {
      content: "";
      position: absolute;
      top: -7px;
      left: auto;
      border-left: var(--border-width) solid #bdc1c7;
      z-index: 0;
    }

    &.is-middle {
      &::after {
        top: -40px;
        height: 40px;
        z-index: 2;
        width: 2px;
      }
    }

    &.is-success {
      &::before {
        border-top-color: #17b26a;
      }

      &::after {
        border-color: #17b26a;
      }
    }

    &.is-success-parent {
      &::before {
        border-top-color: #17b26a;
      }

      &::after {
        border-color: #17b26a;
      }
    }
  }
}

@media screen and (min-width: 2550px) {
  .or-condition-group__additional {
    top: 2px;
    height: calc(100% - 42px);
  }
}

@media screen and (min-width: 6000px) {
  .or-condition-group__additional {
    width: 16px;
  }
}

@media screen and (min-width: 5000px) {
  .or-condition-group__bottom {
    bottom: -5px;
  }
}
</style>
