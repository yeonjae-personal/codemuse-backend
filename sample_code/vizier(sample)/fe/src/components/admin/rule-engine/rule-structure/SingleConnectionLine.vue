<template>
  <div
    :class="[
      'single-connection-line',
      { 'is-show-arrow': isShowArrow, 'is-success': isSuccess },
    ]"
    @mouseover="handleMouseOver"
    @mouseout="handleMouseOut"
  >
    <div
      v-if="isEditRuleStructure"
      ref="addNodeRef"
      :class="[
        'single-connection-line__add',
        { 'is-hover': isHovered || isEmptyRule || isShowAddMenu },
      ]"
      @click="handleToggleMenu"
    >
      <PlusSmallIcon :size="14" />
    </div>
    <div v-if="isShowAddMenu" ref="addNodeMenuRef" class="add-node-menu">
      <div
        v-for="option in addRuleOptions"
        :key="option.value"
        class="add-node-menu__item"
        @click="handleSelectType(option.value)"
      >
        {{ option.title }}
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
import type { AddNodeOption, LogicType } from "@/interfaces/admin/rule-engine";

type Props = {
  isShowArrow?: boolean;
  isEmptyRule?: boolean;
  isSuccess?: boolean;
};

const props = withDefaults(defineProps<Props>(), {
  isShowArrow: true,
  isEmptyRule: false,
});

const emit = defineEmits(["select-type"]);
const {
  isEditRuleStructure,
  passedCondUuids,
  failedCondUuids,
  passed,
  passedMessage,
  isTested,
} = storeToRefs(useRuleEngineStore());

const addNodeRef = ref<HTMLElement | null>(null);
const addNodeMenuRef = ref<HTMLElement | null>(null);
const isShowAddMenu = ref<boolean>(false);
const isHovered = ref<boolean>(false);

const addRuleOptions = computed<AddNodeOption[]>(() => [
  {
    title: "“AND” Condition",
    value: "AND",
  },
  {
    title: "“OR” Condition",
    value: "OR",
  },
]);

watch(
  () => props.isEmptyRule,
  (value) => {
    if (!value) isHovered.value = false;
  }
);

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});

const handleSelectType = (type: LogicType): void => {
  emit("select-type", type);
  isShowAddMenu.value = false;
  isHovered.value = false;
  passedCondUuids.value = [];
  passedMessage.value = null;
  failedCondUuids.value = [];
  passed.value = false;
  isTested.value = false;
};

const handleToggleMenu = (): void => {
  isShowAddMenu.value = !isShowAddMenu.value;
};

const handleMouseOver = (): void => {
  if (!props.isEmptyRule || !isShowAddMenu.value) {
    isHovered.value = true;
  }
};

const handleMouseOut = (): void => {
  if (!props.isEmptyRule || !isShowAddMenu.value) {
    isHovered.value = false;
  }
};

const handleClickOutside = (event: Event): void => {
  if (
    addNodeRef.value &&
    addNodeMenuRef.value &&
    !addNodeRef.value.contains(event.target as Node) &&
    !addNodeMenuRef.value.contains(event.target as Node)
  ) {
    isShowAddMenu.value = false;
  }
};
</script>

<style lang="scss" scoped>
.single-connection-line {
  position: relative;
  width: 0px;
  height: 41px;
  padding: 0 30px;
  background-color: transparent;
  display: flex;
  justify-content: center;
  z-index: 3;

  &:before {
    content: "";
    position: absolute;
    width: 2px;
    height: 40px;
    top: 1px;
    border-left: var(--border-width) solid transparent;
  }

  &.is-show-arrow {
    &::before {
      border-color: #bdc1c7;
    }

    &::after {
      content: "";
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      border-left: 5px solid transparent;
      border-right: 5px solid transparent;
      border-top: 8px solid #bdc1c7;
    }
  }

  &.is-middle-line {
    &::before {
      border-color: #bdc1c7;
    }
  }

  &.is-success {
    &::before {
      border-color: #17b26a;
    }

    &::after {
      border-top-color: #17b26a;
    }
  }

  &.is-hidden {
    &::after {
      border-color: transparent;
    }
  }

  &.is-hidden-arrow {
    &::before {
      height: 100%;
    }

    &::after {
      border-top-color: transparent;
    }
  }

  &.is-small-line {
    &::before {
      border-color: transparent;
    }
  }

  &__add {
    position: absolute;
    right: 4px;
    top: 50%;
    transform: translateY(-50%);
    display: flex;
    align-items: center;
    justify-content: center;
    width: 22px;
    height: 22px;
    border-radius: 50%;
    background-color: #dce0e5;
    transition: all 0.2s linear;
    opacity: 0;
    visibility: hidden;
    pointer-events: none;
    cursor: pointer;

    &.is-hover {
      opacity: 1;
      visibility: visible;
      pointer-events: all;
    }
  }
}

.add-node-menu {
  position: absolute;
  top: 38px;
  left: 32px;
  width: 130px;
  background-color: #fff;
  box-shadow: 0px 0px 16px 0px #7493ce3d;
  border-radius: 12px;
  z-index: 2;

  &__item {
    padding: 8px 12px;
    font-family: Noto Sans KR;
    font-weight: 400;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
    color: #3a3b3d;
    cursor: pointer;
    transition: all 0.2s linear;

    &:hover {
      background-color: #e9ebf0;
    }
  }
}
</style>
