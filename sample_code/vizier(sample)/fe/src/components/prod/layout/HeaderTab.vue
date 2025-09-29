<template>
  <div class="bg-base w-full">
    <GridLayout
      v-if="menuTabs.length"
      ref="gridLayoutRef"
      v-model:layout="menuTabs"
      :col-num="7"
      :is-resizable="false"
      :row-height="20"
      :max-rows="1"
      :margin="[7, 7]"
      class="max-w-[1736px] !h-[40px]"
    >
      <GridItem
        v-for="item in menuTabs"
        :key="item.i"
        class="tab-item-alt"
        :class="['tab-item-alt', { active: item.active }]"
        :x="item.x"
        :y="item.y"
        :w="item.w"
        :h="item.h"
        :i="item.i"
        :static="item.static"
        :drag-option="{
          restrict: {
            restriction: 'parent',
            elementRect: { top: 0, left: 0, bottom: 1, right: 1 },
            endOnly: true,
          },
          axis: 'y',
        }"
        @move="handleMoveTab"
        @mousedown="handleActiveTab(item.id)"
      >
        <div class="h-full px-4">
          <span class="tab-item-alt-text">
            <CustomTooltip
              :content="item.name"
              location="bottom"
              is-always-show
            >
              {{ item.tabName }}
            </CustomTooltip>
          </span>
          <button
            v-if="
              ![
                MenuItemID.DashBoard,
                MenuItemID.GroupDuplicate,
                MenuItemID.OfferDuplicate,
                MenuItemID.RelationDuplicate,
              ].includes(item.id.toString()) && !item?.notClose
            "
            class="btn-tab-close"
            @mousedown.stop="closeTab(item.id)"
          >
            <CloseSmallIcon viewBox="1 0 16 16" width="12" height="12" />
          </button>
        </div>
      </GridItem>
    </GridLayout>
  </div>
</template>

<script setup lang="ts">
import { MenuItemID } from "@/enums/redirect";
import { GridItem, GridLayout } from "grid-layout-plus";

const emit = defineEmits([
  "removeTab",
  "handleActiveTab",
  "moveTab",
  "update:modelValue",
]);

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => [],
  },
});

const menuTabs = ref<any[]>(props.modelValue);

watch(
  () => props.modelValue,
  (newVal) => {
    menuTabs.value = newVal;
  }
);

watch(
  () => menuTabs.value,
  (newVal) => {
    emit("update:modelValue", newVal);
  },
  { deep: true }
);

const closeTab = (id: string) => {
  emit("removeTab", id);
};

const handleActiveTab = (id: string) => {
  emit("handleActiveTab", id);
};

const handleMoveTab = (...event: any[]) => {
  emit("moveTab", ...event);
};
</script>

<style scoped lang="scss">
.tab-sections {
  display: flex;
  width: calc(100vw - 95px);
  height: 40px;
  position: relative;
}

.tab-item-alt {
  width: calc(100% / 7);
  min-height: 40px;
  position: absolute;
  gap: 0px;
  border-radius: 0 0 12px 12px;
  background: #dad9e2;
  display: flex;
  align-items: center;
  cursor: pointer !important;
  font-family: "Noto Sans KR";
  font-weight: 500;
  color: #6b6d70;
  user-select: none;
  top: -7px;
  left: 9px;
}

.tab-item-alt > div {
  display: flex;
  width: 100%;
  justify-content: space-between;
  align-items: center;
}

.tab-item-alt-text {
  width: 100%;
  font-size: 15px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.tab-item-alt.active {
  z-index: 1;
  background: rgba(255, 255, 255, 1);
  color: rgba(186, 22, 66, 1);
}

.btn-tab-close {
  width: 15px;
  height: 15px;
  cursor: pointer;
}

.btn-tab-close svg {
  float: right;
}

.btn-tab-close:hover svg {
  transform: scale(1.3);
  opacity: 0.8;
}
</style>
