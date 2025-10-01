<template>
  <div
    v-if="props.mainItem"
    class="item-menu cursor-pointer"
    :class="{ selected: props.isOpenChild }"
  >
    <span class="item-menu-name">{{ props.name }}</span>
    <BasePopover
      v-if="isEdit"
      :options="listActions()"
      custom-location="bottom-left"
      class="flex-initial"
    >
      <template #activator>
        <DotsVerticalIcon />
      </template>
    </BasePopover>
    <span class="action-button">
      <v-btn
        ref="nodeRef"
        width="24px"
        height="24px"
        :color="selectedColor"
        density="comfortable"
        class="custom-btn"
        icon="mdi-play"
        @click="onClickActionButton"
      >
      </v-btn>
    </span>
  </div>
  <div
    v-else
    :class="[
      'item-menu-rounded ',
      props.selected && isHideActionButton ? 'rounded-selected' : '',
      props.isUsed ? '' : 'disabled',
      ...props.roundedClass,
    ]"
    :style="{ marginBottom: `${props.marginBottom}px` }"
    @click="onClickItem"
  >
    <span :class="[props.isNew ? 'item-menu-name-new' : 'item-menu-name']">
      <CustomTooltip>
        {{
          props.isNew
            ? props.name || t("product_platform.new_item")
            : props.name
        }}
        <template #content>
          {{
            props.isNew
              ? props.name || t("product_platform.new_item")
              : props.name
          }}
        </template>
      </CustomTooltip>
    </span>
    <BasePopover
      v-if="isEdit && !isHideActionButton"
      :options="listActions()"
      custom-location="bottom-left"
      class="flex-initial"
    >
      <template #activator>
        <DotsVerticalIcon />
      </template>
    </BasePopover>
    <span v-if="!isHideActionButton" class="action-button">
      <v-btn
        ref="nodeRef"
        width="24px"
        height="24px"
        :color="selectedColor"
        density="comfortable"
        class="custom-btn"
        icon="mdi-play"
        @click="onClickActionButton"
      >
      </v-btn>
    </span>
    <span v-if="props.isNew" class="is-new"></span>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import BasePopover from "@/components/prod/common/BasePopover.vue";
import PlusSmallIcon from "@/components/prod/icons/PlusSmallIcon.vue";

const props = defineProps({
  id: {
    type: String,
    default: "",
  },
  name: {
    type: String,
    default: "",
  },
  selected: {
    type: Boolean,
    default: false,
  },
  isOpenChild: {
    type: Boolean,
    default: false,
  },
  isHideActionButton: {
    type: Boolean,
    default: false,
  },
  isNew: {
    type: Boolean,
    default: false,
  },
  isEdit: {
    type: Boolean,
    default: false,
  },
  isUsed: {
    type: Boolean,
    default: false,
  },
  mainItem: {
    type: Boolean,
    default: false,
  },
  roundedClass: {
    type: Array<String>,
    default: [],
  },
  marginBottom: {
    type: Number,
    default: 12,
  },
});

const emit = defineEmits(["onOpenChild", "onSelected", "onAddNew"]);
const { t } = useI18n();

const selectedColor = computed(() => {
  return props.isOpenChild ? "#D9325A" : "#52545766";
});

const listActions = () => {
  const actions = [] as any;
  actions.push({
    name: t("product_platform.selectBoxItem"),
    icon: PlusSmallIcon,
    iconProps: {
      class: "text-text-lighter",
    },
    onClick: () => {
      emit("onAddNew", props.id);
    },
  });
  return actions;
};

const onClickActionButton = (event) => {
  event.stopPropagation();
  emit("onOpenChild", props.id);
};

const onClickItem = () => {
  emit("onSelected", props.id);
};
</script>
<style scoped lang="scss">
.item-menu {
  min-height: 48px;
  height: 48px;
  padding: 12px 12px 12px 16px;
  border-radius: 12px;
  background-color: #ffffff;
  box-shadow: 0px 6px 16px 0px #2d307c0a;
  box-shadow: 0px -16px 16px 0px #395bc20a inset;
  color: #6b6d70;
  font-size: 13px;
  font-weight: 500;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  .item-menu-name {
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }
  .action-button {
    position: absolute;
    right: -12px;
    top: 50%;
    transform: translateY(-50%);
  }
}

.item-menu-rounded {
  min-height: 48px;
  max-height: 48px;
  padding: 12px 14px;
  border-radius: 999px;
  background-color: #ffffff;
  border: 1px solid transparent;
  box-shadow: 0px 6px 16px 0px #2d307c0a;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  .item-menu-name {
    color: #3a3b3d;
    font-size: 13px;
    font-weight: 500;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    text-align: center;
    flex: 1;
  }
  .item-menu-name-new {
    color: #bdc1c7;
    font-size: 13px;
    font-weight: 400;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    text-align: center;
    flex: 1;
  }
  .action-button {
    position: absolute;
    right: -12px;
    top: 50%;
    transform: translateY(-50%);
    display: flex;
    align-items: center;
  }
  .is-new {
    position: absolute;
    right: 12px;
    top: 10px;
    display: inline-block;
    height: 10px;
    width: 10px;
    background-color: #ea4f3a;
    border-radius: 50%;
  }
}

.disabled {
  border: 1px solid #f0f2f5;
  background-color: #e9ebf0;
  box-shadow:
    0px -16px 16px 0px rgba(57, 91, 194, 0.04) inset,
    0px 6px 16px 0px rgba(45, 48, 124, 0.04);
  .item-menu-name {
    opacity: 0.32;
  }
}

.selected {
  color: #ba1642;
}
.rounded-selected {
  border-color: #ff8fa1;
}

:deep(.mdi-play::before) {
  font-size: 14px !important;
  margin: auto;
  color: #ffff;
}
</style>
