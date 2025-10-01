<template>
  <div class="flex justify-center">
    <div class="validation-item-wrapper">
      <div ref="conditionRef" class="condition-attributes">
        <template
          v-for="condition in props.item.conditions"
          :key="condition.id"
        >
          <div class="text-div">{{ condition.itemCodeName }}</div>
          <AttributeTypeViewOnly :item="condition" :parent-id="props.item.id" />
        </template>
      </div>
      <div class="draw-connect-line">
        <ConnectLineCanvas
          v-if="props.item.conditions.length && props.item.actions.length"
          id="custom-validation-connect-line"
          :width="56"
          :height="connectLineHeight"
          :list-coordinates="listCoordinates"
        />
      </div>
      <div ref="actionRef" class="action-attributes">
        <template v-for="action in props.item.actions" :key="action.id">
          <div class="text-div">{{ action.itemCodeName }}</div>
          <AttributeTypeViewOnly
            :item="action"
            :parent-id="props.item.id"
            :show-arrow="
              props.item.conditions.length > 0 && props.item.actions.length > 0
            "
          />
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { ICustomValidationItem } from "@/interfaces/admin/admin";
import AttributeTypeViewOnly from "./AttributeTypeViewOnly.vue";
import ConnectLineCanvas from "./ConnectLineCanvas.vue";

interface Props {
  item: ICustomValidationItem;
  index: number;
}
const props = defineProps<Props>();

const conditionRef = ref();
const actionRef = ref();

const listCoordinates = computed(() => {
  const listCoors: any = [];
  const validIndexC = props.item.conditions.findIndex((item) => !item.disabled);
  const validIndexA = props.item.actions.findIndex((item) => !item.disabled);
  if (
    props.item.actions.filter((item) => !item.disabled).length >
    props.item.conditions.filter((item) => !item.disabled).length
  ) {
    props.item.actions.forEach((item, index) => {
      if (!item.disabled) {
        if (index === 0) {
          listCoors.push([
            [0, 120 * validIndexC + 24],
            [56, 120 * validIndexA + 24],
          ]);
        } else {
          listCoors.push([
            [0, 120 * validIndexC + 24],
            [56, 120 * index + 24],
          ]);
        }
      }
    });
  } else {
    props.item.conditions.forEach((item, index) => {
      if (!item.disabled) {
        if (index === 0) {
          listCoors.push([
            [0, 120 * validIndexC + 24],
            [56, 120 * validIndexA + 24],
          ]);
        } else {
          listCoors.push([
            [0, 120 * index + 24],
            [56, 120 * validIndexA + 24],
          ]);
        }
      }
    });
  }
  return listCoors;
});

const connectLineHeight = computed(() => {
  if (props.item.actions.length === 1 && props.item.conditions.length === 1) {
    return 88;
  }
  const attrLength = Math.max(
    props.item.actions.length,
    props.item.conditions.length
  );
  const height = attrLength * 120 + 24 - 80;
  return height;
});
</script>

<style lang="scss" scoped>
.validation-item-wrapper {
  background: #fff;
  border-radius: 12px;
  padding: 8px 16px 12px;
  box-shadow: 0px 2px 4px 0px #00000005;
  position: relative;
  display: flex;
  .condition-attributes {
    display: flex;
    flex-direction: column;
    min-height: 104px;
    min-width: 360px;
    max-width: 360px;
    .text-div {
      font-size: 13px;
      font-weight: 500;
      line-height: 20px;
      text-transform: capitalize;
      color: #6b6d70;
      padding-left: 4px;
    }
  }

  .action-attributes {
    display: flex;
    flex-direction: column;
    min-height: 104px;
    min-width: 360px;
    max-width: 360px;
    .text-div {
      font-size: 13px;
      font-weight: 500;
      line-height: 20px;
      text-transform: capitalize;
      color: #6b6d70;
      padding-left: 4px;
    }
  }

  &:hover {
    .add-buttons {
      visibility: visible;
    }
  }

  .draw-connect-line {
    min-width: 56px;
    display: flex;
    flex-direction: column;
    margin-top: 20px;
  }
}
.item-temp {
  height: 20px;
  font-size: 13px;
  font-weight: 500;
  color: #6b6d70;
  margin-bottom: 8px;
}
.selected {
  border-color: #3a3b3d;
}
.disabled {
  opacity: 0.5;
}
.disabled-connect-line {
  visibility: hidden;
}
</style>
