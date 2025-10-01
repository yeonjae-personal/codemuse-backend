<template>
  <div v-if="condition.length > 0" class="and-condition-group">
    <div
      v-for="(item, index) in condition"
      :id="`${item.condUuid}-${index}`"
      :key="item.condUuid"
      class="and-condition-group__item"
    >
      <template v-if="item.logicType === 'OR'">
        <OrConditionGroup
          :condition-group="item as ConditionGroup"
          :uuid="getFirstConditionUuid(item as ConditionGroup, index)"
        />
      </template>
      <template v-else>
        <ConditionItem
          class="relative"
          :condition-item="item"
          :uuid="uuid || item.condUuid"
          @delete-node="handleDeleteNode($event)"
        />
        <SingleConnectionLine
          :class="{
            'is-hidden-arrow': isLastItem(item),
            'is-small-line': isSmallLine(item),
          }"
          :is-success="passedCondUuids.includes(item.condUuid!)"
          @select-type="handleCreateNode($event, item.condUuid!)"
        />
      </template>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { v4 as uuidv4 } from "uuid";
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import { useSnackbarStore } from "@/store";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
import ConditionItem from "./ConditionItem.vue";
import OrConditionGroup from "./OrConditionGroup.vue";
import SingleConnectionLine from "./SingleConnectionLine.vue";
import {
  type Condition,
  type ConditionGroup,
  type LogicType,
} from "@/interfaces/admin/rule-engine";

type Props = {
  condition: Condition[];
  uuid?: string;
};

const props = defineProps<Props>();

const { t } = useI18n();

const { ruleStructure, selectedNodeId, passedCondUuids, isDeletedCondition } =
  storeToRefs(useRuleEngineStore());
const {
  addConditionBelowTarget,
  createCondition,
  findCurrentGroup,
  findNextConditionByUuid,
  findConditionGroupByUuid,
  getNewGroupCondition,
  deleteConditionByUuid,
} = useRuleEngineStore();
const { showSnackbar } = useSnackbarStore();

const isLastItem = (item: Condition): boolean => {
  const lastItem = props.condition[props.condition.length - 1];
  const isSameUuid = lastItem.condUuid === item.condUuid;
  const currentIndex = props.condition.findIndex(
    (value) => value.condUuid === item.condUuid
  );
  const isNextGroup = !!props.condition[currentIndex + 1]?.logicType || false;
  return isSameUuid || isNextGroup;
};

const isSmallLine = (item: Condition): boolean => {
  const group = findConditionGroupByUuid(ruleStructure.value!, item.condUuid!);
  if (group?.condition.length) {
    return (
      group.condition[group.condition.length - 1].condUuid === item.condUuid
    );
  }
  return false;
};

const getFirstConditionUuid = (item: ConditionGroup, index: number): string => {
  return (item.condition?.[0]?.condUuid || uuidv4()) + `-${index}`;
};

const handleCreateNode = (logicType: LogicType, targetUuid: string): void => {
  if (logicType === "AND") {
    const newCondition = createCondition() as Condition;
    addConditionBelowTarget(ruleStructure.value!, targetUuid, newCondition);
    return;
  }
  const nextCondition = findNextConditionByUuid(
    ruleStructure.value!,
    targetUuid
  );
  const currentGroup = findConditionGroupByUuid(
    ruleStructure.value!,
    targetUuid!
  );
  const currentIndex =
    currentGroup?.condition.findIndex((item) => item.condUuid === targetUuid) ??
    -1;
  const nextGroupCondition = currentGroup?.condition[currentIndex + 1];
  if (!!nextCondition && nextCondition.logicType === "OR") {
    const newCondition = createCondition() as Condition;
    nextCondition.condition = [
      ...(nextCondition.condition ?? []),
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
  addConditionBelowTarget(ruleStructure.value!, targetUuid, newGroup);
  selectedNodeId.value = newGroup.condition[0].condUuid!;
};

const handleDeleteNode = (nodeUuid: string): void => {
  const currentGroup = findConditionGroupByUuid(ruleStructure.value!, nodeUuid);
  const isExitsOneCondition =
    currentGroup?.condition.filter((item) => !item.logicType).length === 1;
  const isHasGroupCondition =
    currentGroup?.condition.filter(({ logicType }) => logicType).length === 1;
  if (isHasGroupCondition && isExitsOneCondition) {
    const parentGroup = findCurrentGroup(ruleStructure.value!, currentGroup!);
    const group = cloneDeep(
      currentGroup?.condition.filter((item) => item.condUuid !== nodeUuid)
    );
    parentGroup?.condition.forEach((item) => {
      if (item?.condition) {
        item.condition.forEach((item1, index) => {
          if (item1?.logicType === "AND") {
            const newGroup: Condition[] = group![0].condition!.map(
              (condition) => condition
            );

            item.condition!.splice(index, 1, ...newGroup);
          }
        });
      }
    });
  } else {
    deleteConditionByUuid(ruleStructure.value!, nodeUuid);
  }
  showSnackbar(t("product_platform.delete_condition_successfully"), "success");
  setTimeout(() => {
    isDeletedCondition.value = true;
  }, 100);
};
</script>

<style lang="scss" scoped>
.and-condition-group {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  &__item {
    position: relative;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
}
</style>
