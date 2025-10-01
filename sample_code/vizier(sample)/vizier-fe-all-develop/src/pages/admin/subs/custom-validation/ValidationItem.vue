<template>
  <div
    class="validation-item-wrapper"
    :class="{ selected: props.item.selected, edit: props.item.isEdit }"
    @click="handleClickValidationItem"
  >
    <div class="d-flex">
      <div
        v-if="showItem"
        class="condition-attributes"
        :class="{ disabled: props.item.disabled }"
      >
        <template
          v-for="condition in props.item.conditions"
          :key="condition.id"
        >
          <div class="text-div">{{ condition.itemCodeName }}</div>
          <AttributeType
            :attribute-id="`condition-${item.id}-${condition.id}`"
            :item="condition"
            :parent-id="props.item.id"
            :parent-edit="props.item.isEdit"
            :parent-disabled="props.item.disabled"
            :number-items="numberItems"
          />
        </template>
      </div>
      <div
        v-if="showItem"
        class="draw-connect-line"
        :class="{
          'disabled-connect-line': props.item.disabled || !showConnectLine,
        }"
      >
        <ConnectLineCanvas
          v-if="props.item.conditions.length && props.item.actions.length"
          id="custom-validation-connect-line"
          :width="81"
          :height="connectLineHeight"
          :list-coordinates="listCoordinates"
        />
      </div>
      <div
        v-if="showItem"
        class="action-attributes"
        :class="{ disabled: props.item.disabled }"
      >
        <template v-for="action in props.item.actions" :key="action.id">
          <div class="text-div">{{ action.itemCodeName }}</div>
          <AttributeType
            :attribute-id="`action-${item.id}-${action.id}`"
            :item="action"
            :parent-id="props.item.id"
            :parent-edit="props.item.isEdit"
            :parent-disabled="props.item.disabled"
            :number-items="numberItems"
            :show-arrow="
              props.item.conditions.length > 0 &&
              props.item.actions.length > 0 &&
              !props.item.disabled &&
              !action.disabled &&
              showConnectLine &&
              listCoordinates.length > 0
            "
          />
        </template>
      </div>
      <ActionButtons :item="props.item" />
      <AddButtons :index="index" />
      <div v-if="!props.item.isEdit && !showItem" class="h-[40px]"></div>
    </div>
    <div
      v-show="props.item.isEdit"
      class="drop-wrapper-area"
      :class="{ 'mt-[12px]': showItem }"
    >
      <ItemDrop
        :key="`condition-${props.item.id}`"
        class-name="!h-10"
        :is-disabled="dragItemType === 'action'"
        @drop="dropCondition($event)"
        @dragover="allowDrop($event)"
      />
      <ItemDrop
        :key="`action-${props.item.id}`"
        class-name="!h-10"
        :is-disabled="dragItemType === 'condition'"
        @drop="dropAction($event)"
        @dragover="allowDrop($event)"
      />
    </div>
  </div>

  <BasePopup
    v-model="openPopup"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_update')"
    :icon="DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    @on-close="closePopupSave"
    @on-submit="handleSaveItem"
  />

  <BasePopup
    v-model="confirmPopup"
    :cancel-button-text="$t('product_platform.cancel')"
    :submit-button-text="$t('product_platform.save')"
    persistent
    @on-close="handleCloseConfirm"
    @on-submit="handleSubmitConfirm"
  >
    <template #body>
      <v-card-text class="!px-6 !pb-2 !pt-4">
        <p>{{ $t("product_platform.theActionExitsInOtherRules") }}</p>
        <p>{{ $t("product_platform.doYouWantToContinueSavingIt") }}</p>
      </v-card-text>
    </template>
  </BasePopup>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import BasePopup from "@/components/prod/common/BasePopup.vue";
import { DialogIconType } from "@/enums";
import type { ICustomValidationItem } from "@/interfaces/admin/admin";
import { useSnackbarStore } from "@/store";
import customValidationStore from "@/store/admin/customValidation.store";
import ActionButtons from "./ActionButtons.vue";
import AddButtons from "./AddButtons.vue";
import AttributeType from "./AttributeType.vue";
import ConnectLineCanvas from "./ConnectLineCanvas.vue";

interface Props {
  item: ICustomValidationItem;
  index: number;
  numberItems: number;
}
const props = defineProps<Props>();
const { dragItemType, customValidationItems } = storeToRefs(
  customValidationStore()
);
const {
  selectedCustomValidationItem,
  checkValidationItemEdit,
  saveCustomValidationItemEdit,
  addAttributeItem,
  checkDuplicateAction,
} = customValidationStore();
const { showSnackbar } = useSnackbarStore();
const { t } = useI18n();

const openPopup = ref(false);
const editItemId = ref();
const confirmPopup = ref<boolean>(false);

const showItem = computed(() => {
  return props.item.conditions.length > 0 || props.item.actions.length > 0;
});

const showConnectLine = computed(() => {
  const actionCount = props.item.actions.filter(
    (item) => !item.disabled
  ).length;
  const conditionCount = props.item.conditions.filter(
    (item) => !item.disabled
  ).length;
  if (actionCount > conditionCount) {
    return conditionCount > 0 ? true : false;
  } else {
    return actionCount > 0 ? true : false;
  }
});

const listCoordinates = computed(() => {
  const listCoors: any = [];
  const validIndexC = props.item.conditions.findIndex((item) => !item.disabled);
  const validIndexA = props.item.actions.findIndex((item) => !item.disabled);
  const height = 28;
  const width = 81;
  const itemHeight = 123.5;
  if (
    props.item.actions.filter((item) => !item.disabled).length >
    props.item.conditions.filter((item) => !item.disabled).length
  ) {
    props.item.actions.forEach((item, index) => {
      if (!item.disabled) {
        if (index === 0) {
          listCoors.push([
            [0, itemHeight * validIndexC + height],
            [width, itemHeight * validIndexA + height],
          ]);
        } else {
          listCoors.push([
            [0, itemHeight * validIndexC + height],
            [width, itemHeight * index + height],
          ]);
        }
      }
    });
  } else {
    props.item.conditions.forEach((item, index) => {
      if (!item.disabled) {
        if (index === 0) {
          listCoors.push([
            [0, itemHeight * validIndexC + height],
            [width, itemHeight * validIndexA + height],
          ]);
        } else {
          listCoors.push([
            [0, itemHeight * index + height],
            [width, itemHeight * validIndexA + height],
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
  const height = attrLength * 120 + 28 - 80;
  return height;
});

const handleClickValidationItem = () => {
  const { isValid, id } = checkValidationItemEdit(props.item.id);
  if (isValid) {
    openPopup.value = true;
    editItemId.value = id;
    return;
  }
  selectedCustomValidationItem(props.item.id);
};

const closePopupSave = () => {
  saveCustomValidationItemEdit("cancel");
  selectedCustomValidationItem(props.item.id);
  openPopup.value = false;
  resetHeight();
};

const handleSaveItem = async () => {
  try {
    const currentItem = customValidationItems.value.find((item) => item.isEdit);
    if (!currentItem) return;
    if (checkDuplicateAction(currentItem.id)) {
      openPopup.value = false;
      confirmPopup.value = true;
    } else {
      await saveCustomValidationItemEdit("save");
      selectedCustomValidationItem(props.item.id);
      showSnackbar(t("product_platform.saveSuccessfully"), "success");
      openPopup.value = false;
    }
  } catch (error: any) {
    showSnackbar(error.errorMsg, "error");
  }
};

const handleSubmitConfirm = async () => {
  try {
    await saveCustomValidationItemEdit("save");
    selectedCustomValidationItem(props.item.id);
    showSnackbar(t("product_platform.saveSuccessfully"), "success");
  } catch (error: any) {
    showSnackbar(error.errorMsg, "error");
  } finally {
    confirmPopup.value = false;
  }
};

const handleCloseConfirm = () => {
  confirmPopup.value = false;
};

const handleAddConditionItem = (item) => {
  const message = addAttributeItem(item);
  if (message) showSnackbar(message, "error");
};

const handleAddActionItem = (item) => {
  const message = addAttributeItem(item);
  if (message) showSnackbar(message, "error");
};

const resetHeight = () => {
  const element = document.querySelector(`.memo-${editItemId.value}`) as any;
  if (element) {
    nextTick(() => {
      element.value = element.value.replace(/^\s*$(?:\r\n?|\n)/gm, "");
      element.style.height = "5px";
      element.style.height = element.scrollHeight + "px";
      editItemId.value = "";
    });
  }
};

const allowDrop = (event: DragEvent): void => {
  event.preventDefault();
};

const dropCondition = (event: DragEvent): void => {
  event.preventDefault();
  if (dragItemType.value === "condition") {
    try {
      const item = event.dataTransfer?.getData("item")
        ? JSON.parse(event.dataTransfer.getData("item"))
        : null;
      if (!item) return;
      handleAddConditionItem(item);
    } catch (error) {
      showSnackbar(t("product_platform.cannotDragItemHereError"), "error");
    }
  }
};

const dropAction = (event: DragEvent): void => {
  event.preventDefault();
  if (dragItemType.value === "action") {
    try {
      const item = event.dataTransfer?.getData("item")
        ? JSON.parse(event.dataTransfer.getData("item"))
        : null;
      if (!item) return;
      handleAddActionItem(item);
    } catch (error) {
      showSnackbar(t("product_platform.cannotDragItemHereError"), "error");
    }
  }
};
</script>

<style lang="scss" scoped>
.validation-item-wrapper {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow:
    4px 4px 40px 0px #1b2e5c14,
    4px 4px 18px -4px #1b2e5c1f;
  position: relative;
  display: flex;
  flex-direction: column;
  border: 0.5px solid transparent;

  .condition-attributes {
    display: flex;
    flex-direction: column;
    min-height: 104px;
    // min-width: 360px;
    // max-width: 360px;
    flex: 1;
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
    flex: 1;
    // min-width: 360px;
    // max-width: 360px;
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
    min-width: 81px;
    max-width: 81px;
    margin-top: 20px;
    display: flex;
    flex-direction: column;
    transform: translateX(-4px);
  }

  .drop-wrapper-area {
    display: flex;
    justify-content: space-between;
    column-gap: 81px;
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
  border-color: #bdc1c7;
}
.edit {
  border-color: #88a9e3;
}
.disabled {
  opacity: 0.5;
}
.disabled-connect-line {
  margin-top: 0 !important;
  visibility: hidden;
}
</style>
