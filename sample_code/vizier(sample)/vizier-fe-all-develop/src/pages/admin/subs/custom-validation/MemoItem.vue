<!-- eslint-disable vue/no-mutating-props -->
<template>
  <div
    class="memo-item-wrapper"
    :class="{
      selected: props.item.selected,
      edit: props.item.isEdit,
    }"
    @click="handleClickMemoItem"
  >
    <textarea
      v-model="props.item.value"
      :class="style"
      :readonly="!props.item.isEdit"
      :maxlength="500"
      :spellcheck="false"
      @input="autoGrow($event)"
    ></textarea>
    <ActionButtons :item="props.item" @on-cancel="handleCancel" />
    <AddButtons :index="index" />
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
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { DialogIconType } from "@/enums";
import { ICustomValidationItem } from "@/interfaces/admin/admin";
import { useSnackbarStore } from "@/store";
import customValidationStore from "@/store/admin/customValidation.store";
import ActionButtons from "./ActionButtons.vue";
import AddButtons from "./AddButtons.vue";
import BasePopup from "@/components/prod/common/BasePopup.vue";
const {
  selectedCustomValidationItem,
  checkValidationItemEdit,
  saveCustomValidationItemEdit,
} = customValidationStore();

interface Props {
  item: ICustomValidationItem;
  index: number;
}
const props = defineProps<Props>();
const { showSnackbar } = useSnackbarStore();
const { t } = useI18n();

const openPopup = ref(false);
const editItemId = ref();

const style = computed(() => {
  const klass: string[] = ["memo-content", `memo-${props.item.id}`];
  if (props.item.disabled) {
    klass.push("disabled");
  } else {
    klass.push("readonly");
  }
  return klass.join(" ");
});

const handleClickMemoItem = () => {
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
    await saveCustomValidationItemEdit("save");
    selectedCustomValidationItem(props.item.id);
    showSnackbar(t("product_platform.saveSuccessfully"), "success");
  } catch (error: any) {
    showSnackbar(error.errorMsg, "error");
  }
  openPopup.value = false;
};

watch(
  () => props.item.isEdit,
  (newVal) => {
    if (newVal) {
      const element = document.querySelector(`.memo-${props.item.id}`) as any;
      if (element) {
        element.focus();
        element.selectionStart = element.value.length;
        element.scrollTop = element.scrollHeight;
      }
    }
  }
);

onMounted(() => {
  const element = document.querySelector(`.memo-${props.item.id}`) as any;
  if (element) {
    element.style.height = "20px";
    element.style.height = element.scrollHeight + "px";
  }
});

const handleCancel = () => {
  resetHeight();
};

const resetHeight = () => {
  const element = document.querySelector(
    `.memo-${editItemId.value || props.item.id}`
  ) as any;
  if (element) {
    nextTick(() => {
      element.value = element.value.replace(/^\s*$(?:\r\n?|\n)/gm, "");
      element.style.height = "5px";
      element.style.height = element.scrollHeight + "px";
      editItemId.value = "";
    });
  }
};

const autoGrow = (event) => {
  event.target.value = event.target.value.replace(/^\s*$(?:\r\n?|\n)/gm, "");
  event.target.style.height = "5px";
  event.target.style.height = event.target.scrollHeight + "px";
};
</script>
<style lang="scss" scoped>
.memo-item-wrapper {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0px 2px 4px 0px #00000005;
  position: relative;
  border: 1px solid transparent;
  padding: 11px 16px;
  display: flex;
  box-shadow:
    4px 4px 40px 0px #1b2e5c14,
    4px 4px 18px -4px #1b2e5c1f;
  &:hover {
    .add-buttons {
      visibility: visible;
    }
  }
  .memo-content {
    border: none;
    outline: none;
    font-size: 13px;
    font-family: Noto Sans KR;
    font-weight: 400;
    line-height: 150%;
    letter-spacing: 0.25px;
    width: 100%;
    resize: none;
    overflow: hidden;
    height: 20px;
    cursor: default;
  }
}
.selected {
  border-color: #bdc1c7;
}
.edit {
  border-color: #88a9e3;
  .memo-content {
    cursor: text;
  }
}
.disabled {
  opacity: 0.5;
}
</style>
