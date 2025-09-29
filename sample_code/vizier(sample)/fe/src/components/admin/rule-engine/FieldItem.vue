<!-- eslint-disable vue/no-mutating-props -->
<!-- eslint-disable vue/no-v-html -->
<template>
  <div
    :class="[
      'field-item',
      isActiveField
        ? `!border-[${BORDER_CONFIG.ACTIVE}] border-[2px]`
        : '!border-lighter border-[1px]',
      {
        'zoom-animation': selectedField?.fieldUuid !== props.item.fieldUuid,
      },
      { 'is-expired': props.item.useYn === 'N' },
    ]"
    :draggable="!isEditOrAddNew"
    @dragstart="(event) => handleDragStart(event)"
    @dragend="handleDragEnd"
    @drag="handleDrag"
    @click="handleClickItem"
  >
    <div class="field-item-content">
      <div
        class="field-item-content__type"
        :class="
          props.item.fieldDataType === 'Number'
            ? 'field-item-content__number'
            : 'field-item-content__string'
        "
      >
        {{ props.item.fieldDataType }}
      </div>
      <div
        class="field-item-content__title"
        :class="{ 'add-new-item': props.item.isAddNew }"
      >
        <CustomTooltip :content="props.item.fieldDispName" location="bottom">
          <span v-html="highlightedText(LABEL_SEARCH_TYPE.NAME)" />
        </CustomTooltip>
      </div>
      <BasePopover
        v-if="editUuid !== props.item.fieldUuid"
        :options="actions"
        custom-location="bottom-left"
      >
        <template #activator>
          <div class="label-item__popover">
            <DotsVerticalIcon />
          </div>
        </template>
      </BasePopover>
    </div>
    <DetailPane
      v-if="selectedField?.fieldUuid === props.item.fieldUuid || isEditOrAddNew"
      class="w-full"
    >
      <DetailPaneRow :label="t('product_platform.displayName')">
        <template #value="{ klass }">
          <span v-if="isEditOrAddNew" :class="klass">
            <BaseInputText
              v-model.trim="props.item.fieldDispName"
              class="node-name"
              :maxlength="50"
              required
            />
          </span>
          <span v-else :class="klass">{{ props.item.fieldDispName }}</span>
        </template>
      </DetailPaneRow>
      <DetailPaneRow :label="t('product_platform.keyName')">
        <template #value="{ klass }">
          <span v-if="isEditOrAddNew" :class="klass">
            <BaseInputText
              v-model.trim="props.item.fieldKeyName"
              class="node-name"
              :maxlength="50"
              required
            />
          </span>
          <span v-else :class="klass">{{ props.item.fieldKeyName }}</span>
        </template>
      </DetailPaneRow>
      <DetailPaneRow :label="t('product_platform.Type')">
        <template #value="{ klass }">
          <span v-if="isEditOrAddNew" :class="klass">
            <BaseSelectScroll
              v-model="props.item.fieldDataType"
              :options="[
                {
                  cmcdDetlNm: 'String',
                  cmcdDetlId: 'String',
                },
                {
                  cmcdDetlNm: 'Number',
                  cmcdDetlId: 'Number',
                },
              ]"
              :default-item-select-all="false"
              required
            />
          </span>
          <span v-else :class="klass">{{ props.item.fieldDataType }}</span>
        </template>
      </DetailPaneRow>
    </DetailPane>
    <div v-if="isEditOrAddNew" class="action-buttons">
      <button @click.stop.prevent="handleSave">
        <CustomTooltip
          :content="$t('product_platform.save')"
          is-always-show
          class="!w-auto p-1"
        >
          <SaveIcon fill="#6B6D70" size="18" />
        </CustomTooltip>
      </button>
      <button @click.stop.prevent="handleClose">
        <CustomTooltip
          :content="$t('product_platform.cancel')"
          is-always-show
          class="!w-auto p-1"
        >
          <CloseSmallIcon />
        </CustomTooltip>
      </button>
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
    <BasePopup
      v-if="isShowPopupExpired"
      v-model="isShowPopupExpired"
      content="Do you want to expired?"
      :icon="DialogIconType.Info"
      :cancel-button-text="t('product_platform.btn_no')"
      :submit-button-text="t('product_platform.btn_yes')"
      @on-close="handleCloseExpired"
      @on-submit="handleSubmitExpired"
    />
    <BasePopup
      v-if="isShowPopupEnabled"
      v-model="isShowPopupEnabled"
      content="Do you want to enabled?"
      :icon="DialogIconType.Info"
      :cancel-button-text="t('product_platform.btn_no')"
      :submit-button-text="t('product_platform.btn_yes')"
      @on-close="handleCloseEnabled"
      @on-submit="handleSubmitEnabled"
    />
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import { LABEL_SEARCH_TYPE } from "@/constants/admin/label";
import { DialogIconType } from "@/enums";
import { IFieldItem } from "@/interfaces/admin/rule-field";
import type { ActionType } from "@/interfaces/prod";
import { useSnackbarStore } from "@/store";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
import useRuleFieldStore from "@/store/admin/ruleField.store";
import { highlightText } from "@/utils/format-data";
import EditIcon from "@/components/prod/icons/EditIcon.vue";
import EnableIcon from "@/components/prod/icons/EnableIcon.vue";
import ExpireIcon from "@/components/prod/icons/ExpireIcon.vue";
import SaveIcon from "@/components/prod/icons/SaveIcon.vue";
import DetailPaneRow from "@/components/prod/layout/DetailPaneRow.vue";
import BaseSelectScroll from "@/components/prod/common/BaseSelectScroll.vue";
import { BORDER_CONFIG } from "@/constants/index";

type SearchTypeObject = {
  field: string;
  value: string;
  keysCheck: Record<"key" | "name", string>;
};

type Props = {
  item: IFieldItem;
  searchTypeObj: SearchTypeObject;
  isUsed: boolean;
};

const props = withDefaults(defineProps<Props>(), {
  searchTypeObj: () => ({
    value: "",
    field: "",
    keysCheck: { key: "", name: "" },
  }),
});

defineEmits<{
  (event: "selected", item: IFieldItem): void;
}>();

const { t } = useI18n();
const { showSnackbar } = useSnackbarStore();
const { updateRuleField, getListField, updateListField } = useRuleFieldStore();
const { selectedField, editUuid } = storeToRefs(useRuleFieldStore());
const { ruleStructure, ruleStructureTemp } = storeToRefs(useRuleEngineStore());
const { updateFieldByFieldUuid } = useRuleEngineStore();
const isShowPopupCancel = ref(false);
const isShowPopupSaveConfirm = ref(false);
const isShowPopupExpired = ref(false);
const isShowPopupEnabled = ref(false);

const isEditOrAddNew = computed(() => {
  return editUuid.value === props.item.fieldUuid;
});

const highlightedText = computed(() => (type: "key" | "name") => {
  const valueToHighlight =
    type === LABEL_SEARCH_TYPE.NAME
      ? props.item.isAddNew
        ? props.item.fieldDispName || "New Field"
        : props.item.fieldDispName || ""
      : props.item.fieldKeyName;
  if (
    !props.searchTypeObj.value ||
    props.searchTypeObj.field !== props.searchTypeObj.keysCheck[type as string]
  ) {
    return valueToHighlight;
  }
  return highlightText(valueToHighlight, props.searchTypeObj.value);
});

const actions = computed<ActionType[]>(() => {
  const listActions: ActionType[] = [];
  if (props.item.useYn === "Y") {
    if (!props.isUsed) {
      listActions.push({
        name: t("product_platform.actionExpire"),
        icon: ExpireIcon,
        onClick: () => {
          isShowPopupExpired.value = true;
        },
      });
    }
    listActions.push({
      name: t("product_platform.edit"),
      icon: EditIcon,
      iconProps: {
        class: "text-text-lighter",
        fill: "#6B6D70",
      },
      onClick: () => {
        if (editUuid.value && editUuid.value !== props.item.fieldUuid) {
          showSnackbar("Cannot edit item", "error");
          return;
        }
        editUuid.value = props.item.fieldUuid;
      },
    });
  } else {
    listActions.push({
      name: t("product_platform.actionEnable"),
      icon: EnableIcon,
      onClick: () => {
        isShowPopupEnabled.value = true;
      },
    });
  }

  return listActions;
});

const isActiveField = computed<boolean>(
  () => props.item.fieldUuid === selectedField.value?.fieldUuid
);

const handleClickItem = (): void => {
  if (selectedField.value?.fieldUuid === props.item.fieldUuid) {
    selectedField.value = null;
  } else {
    selectedField.value = props.item;
  }
};

const handleDragStart = (event: DragEvent): void => {
  const hiddenDragImg = document.createElement("div");
  hiddenDragImg.id = "hiddenDragImg";
  hiddenDragImg.style.opacity = "0";

  const dragImg = document.createElement("div");
  dragImg.id = "dragImg";
  dragImg.style.position = "absolute";
  dragImg.style.width = "391px";
  dragImg.style.height = "48px";
  dragImg.style.zIndex = "1000";
  dragImg.style.background = "#F7F8FA";
  dragImg.style.borderRadius = "12px";
  dragImg.style.border = "1px solid #ff8fa1";
  dragImg.style.fontFamily = "Noto Sans KR";
  dragImg.style.display = "flex";
  dragImg.style.justifyContent = "space-between";
  dragImg.style.alignItems = "center";
  dragImg.style.padding = "11px 12px 11px 12px";
  dragImg.style.gap = "8px";

  const itemType = document.createElement("div");
  itemType.style.width = "56px";
  itemType.style.height = "24px";
  itemType.style.borderRadius = "4px";
  itemType.style.padding = "4px 6px 4px 6px";
  itemType.style.fontSize = "11px";
  itemType.style.letterSpacing = "0.25px";
  itemType.style.display = "flex";
  itemType.style.justifyContent = "center";
  itemType.style.alignItems = "center";
  itemType.style.backgroundColor =
    props.item.fieldDataType === "Number" ? "#e8f4fc" : "#f0f2f5";
  itemType.style.color =
    props.item.fieldDataType === "Number" ? "#1570ef" : "#6b6d70";
  itemType.textContent = props.item.fieldDataType;
  dragImg.appendChild(itemType);

  const itemName = document.createElement("div");
  itemName.textContent = props.item.fieldDispName;
  itemName.style.whiteSpace = "nowrap";
  itemName.style.overflow = "hidden";
  itemName.style.textOverflow = "ellipsis";
  itemName.style.fontSize = "15px";
  itemName.style.color = "#3a3b3d";
  itemName.style.flex = "1";
  dragImg.appendChild(itemName);

  document.body.appendChild(hiddenDragImg);
  document.body.appendChild(dragImg);
  event.dataTransfer?.setDragImage(hiddenDragImg, 0, 0);
  event.dataTransfer?.setData("condition-field", JSON.stringify(props.item));
};

const handleDragEnd = (_event: DragEvent): void => {
  const hiddenDragImg = document.getElementById("hiddenDragImg") as HTMLElement;
  const dragImage = document.getElementById("dragImg") as HTMLElement;
  dragImage?.remove();
  hiddenDragImg?.remove();
};

const handleDrag = (event: DragEvent) => {
  const dragImage = document.getElementById("dragImg") as HTMLElement;
  if (dragImage) {
    dragImage.style.left = `${event.pageX}px`;
    dragImage.style.top = `${event.pageY}px`;
  }
};

const handleSave = () => {
  isShowPopupSaveConfirm.value = true;
};

const handleClose = () => {
  isShowPopupCancel.value = true;
};

const handleClosePopupCancel = () => {
  isShowPopupCancel.value = false;
};
const handleSubmitPopupCancel = () => {
  isShowPopupCancel.value = false;
  editUuid.value = null;
  updateListField();
};

const handleClosePopupSaveConfirm = () => {
  isShowPopupSaveConfirm.value = false;
};

const handleSubmitPopupSaveConfirm = async () => {
  isShowPopupSaveConfirm.value = false;
  if (
    !props.item.fieldDispName ||
    !props.item.fieldKeyName ||
    !props.item.fieldDataType
  ) {
    showSnackbar(t("product_platform.required_field_missing"), "error");
    return;
  }
  const response = await updateRuleField(props.item);
  if (response?.status === 200) {
    updateFieldByFieldUuid(ruleStructure.value!, props.item);
    updateFieldByFieldUuid(ruleStructureTemp.value!, props.item);
    showSnackbar(t("product_platform.saveSuccessfully"), "success");
    getListField();
    editUuid.value = null;
  } else {
    showSnackbar(t("product_platform.dashboard.saveFailed"), "error");
  }
};

const handleCloseExpired = () => {
  isShowPopupExpired.value = false;
};

const handleSubmitExpired = () => {
  isShowPopupExpired.value = false;
  updateRuleField({ ...props.item, useYn: "N" });
  setTimeout(() => {
    getListField();
  }, 500);
};

const handleCloseEnabled = () => {
  isShowPopupEnabled.value = false;
};

const handleSubmitEnabled = () => {
  isShowPopupEnabled.value = false;
  updateRuleField({ ...props.item, useYn: "Y" });
  setTimeout(() => {
    getListField();
  }, 500);
};
</script>

<style lang="scss" scoped>
.field-item {
  font-family: "Noto Sans KR";
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;
  gap: 10px;
  padding: 11px 12px;
  box-shadow:
    0px 6px 16px 0px #2d307c0a,
    0px -16px 16px 0px #395bc20a inset;
  border-radius: 12px;
  min-height: 48px;
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;

  &__popover {
    flex-shrink: 0;
  }

  .field-item-content {
    display: flex;
    gap: 8px;
    width: 100%;
    &__type {
      width: 56px;
      height: 24px;
      border-radius: 4px;
      padding: 4px 6px;
      font-size: 11px;
      letter-spacing: 0.25px;
      display: flex;
      justify-content: center;
      align-items: center;
    }

    &__number {
      background-color: #e8f4fc;
      color: #1570ef;
    }

    &__string {
      background-color: #f0f2f5;
      color: #6b6d70;
    }

    &__title {
      font-weight: 400;
      font-size: 15px;
      line-height: 150%;
      letter-spacing: 0.5px;
      color: #3a3b3d;
      flex: 1;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
    .add-new-item {
      color: #bdc1c7;
    }
  }

  .field-item-extend {
    height: 136px;
    width: 100%;
    display: flex;
    flex-direction: column;
    border-radius: 12px;
    gap: 8px;
    padding: 12px;
    background-color: #f7f8fa;
    font-size: 13px;
    &__row {
      display: flex;
      gap: 8px;
      padding: 6px 0;
      align-items: center;
      height: 32px;
      .item-key {
        width: 104px;
        color: #6b6d70;
        font-weight: 500;
      }
      .item-value {
        flex: 1;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        font-weight: 400;
      }
    }
  }

  .action-buttons {
    display: flex;
    border-radius: 6px;
    border: 1px solid #dce0e5;
    box-shadow: 0px 4px 8px 0px #00000014;
    position: absolute;
    top: -16px;
    right: 12px;
    background: #fff;
    > button {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 30px;
      height: 30px;
      border-right: 1px solid #dce0e5;
      &:first-child {
        border-radius: 6px 0 0 6px;
      }
      &:last-child {
        border: none;
        border-radius: 0 6px 6px 0;
      }
      &:hover {
        background: #f7f8fa;
      }
    }
  }
}

.drag-item {
  height: 50px;
  border: 1px solid red;
  background: #eee;
}

.is-expired {
  background-color: #e9ebf0;
  .field-item-content {
    opacity: 0.4;
  }
}

//text field
:deep(.v-field__field) {
  height: 30px;
}
:deep(.v-field__input) {
  min-height: 0;
  height: 30px;
  padding: 0 16px;
}

:deep(.custom-text-field .v-field) {
  height: 30px;
  padding-right: 0;
}
:deep(.custom-text-field .v-input__control) {
  height: 30px;
}
:deep() .highlight {
  background-color: yellow;
}
</style>
