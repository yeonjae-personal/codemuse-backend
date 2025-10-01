<template>
  <div
    :class="[
      'flex flex-col gap-2 col-span-1 relative z-10 top-0 right-0 bg-white rounded-[12px] pt-6 pb-[12px]',
      { 'is-active': isEditing || isAddNew },
    ]"
  >
    <div class="flex justify-space-between items-center px-6">
      <div
        class="text-text-base text-base font-medium tracking-[0.5px] leading-[23px]"
      >
        {{
          isAddNew
            ? t("product_platform.label_create")
            : isEditing
              ? t("product_platform.label_edit")
              : t("product_platform.label_details")
        }}
      </div>
      <div class="h-10">
        <BaseButton
          v-if="!isEditing && !isAddNew"
          :color="ButtonColorType.Secondary"
          @click="handleEdit"
        >
          <EditIcon class="mr-[6px]" />
          {{ t("product_platform.edit") }}
        </BaseButton>
      </div>
    </div>
    <div class="flex-1 h-full">
      <LocomotiveComponent
        v-if="listLabel.length > 0"
        :key="componentKey"
        scroll-content-class="flex flex-col gap-2"
        scroll-container-class="!px-6 !h-[calc(100vh-290px)]"
      >
        <div
          class="h-[56px] flex gap-2 items-center bg-[#f7f8fa] py[18px] px-3 rounded-t-lg"
        >
          <div class="w-[160px] text-[#6b6d70] text-[13px] tracking-[0.25px]">
            {{ t("product_platform.label_id") }}
          </div>
          <div class="text-text-base text-[13px] tracking-[0.25px]">
            {{
              selectedLabel!.labelId.includes("product_platform")
                ? t(selectedLabel!.labelId)
                : selectedLabel!.labelId
            }}
          </div>
        </div>
        <template
          v-for="(lang, index) in listLanguageLabel"
          :key="lang.langCode"
        >
          <LabelAccordion
            :title="lang.langName"
            :item="lang"
            :is-active="itemSelected === lang.langCode"
            @on-click="handleClick(lang)"
          >
            <BaseInputText
              v-if="lang.langCode === LabelLanguage.English"
              ref="requiredRef"
              v-model.trim="selectedLabel!.items[index].labelName"
              styles="input-edit custom"
              :disabled="!isEditing && !isAddNew"
              :required="
                (isEditing || isAddNew) &&
                lang.langCode === LabelLanguage.English
              "
              :maxlength="200"
              :counter="200"
            />
            <BaseInputText
              v-else
              v-model.trim="selectedLabel!.items[index].labelName"
              styles="input-edit custom"
              :disabled="!isEditing && !isAddNew"
              :required="
                (isEditing || isAddNew) &&
                lang.langCode === LabelLanguage.English
              "
              :maxlength="200"
              :counter="200"
            />
            <BaseTextArea
              v-model.trim="selectedLabel!.items[index].labelDscr"
              class="mt-[6px]"
              :placeholder="t('product_platform.menuEntity.description')"
              :rules="{ maxLength: 500 }"
              :maxlength="500"
              :disabled="!isEditing && !isAddNew"
              counter
            />
          </LabelAccordion>
        </template>
      </LocomotiveComponent>
    </div>
    <div
      v-if="isEditing || isAddNew"
      class="flex justify-end item-center gap-2 px-6"
    >
      <BaseButton :color="ButtonColorType.Gray" @click="handleShowPopupCancel">
        {{ t("common.btn_cancel") }}
      </BaseButton>
      <BaseButton
        :color="ButtonColorType.Secondary"
        @click="handleShowPopupSave"
      >
        <SaveIcon class="mr-[6px]" />
        {{ t("common.btn_save") }}
      </BaseButton>
    </div>
    <ArrowLeftIcon
      class="absolute top-[174px] right-[0] cursor-pointer text-[#525457] hover:text-[#303132]"
      @click="handleClose"
    />
  </div>
  <BasePopup
    v-if="isShowPopupCancel"
    v-model="isShowPopupCancel"
    :content="t('product_platform.desc_cancel')"
    :icon="DialogIconType.Warning"
    :cancel-button-text="t('product_platform.btn_no')"
    :submit-button-text="t('product_platform.btn_yes')"
    @on-close="handleClosePopupCancel"
    @on-submit="handleCancel"
  />
  <BasePopup
    v-if="isShowPopupSave"
    v-model="isShowPopupSave"
    :content="t('product_platform.desc_update')"
    :icon="DialogIconType.Info"
    :cancel-button-text="t('product_platform.btn_no')"
    :submit-button-text="t('product_platform.btn_yes')"
    @on-close="handleClosePopupSave"
    @on-submit="handleSave"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { useLabelStore, useSnackbarStore } from "@/store";
import { createNewLabel, updateLabel } from "@/api/prod/labelApi";
import { updateLabelI18n } from "@/utils/fetch-i18n";
import { ButtonColorType, DialogIconType } from "@/enums";
import { LabelLanguage } from "@/enums/labelManagement";
import type { ILabelItem } from "@/interfaces/admin/label-management";
import LabelAccordion from "./LabelAccordion.vue";
import ArrowLeftIcon from "@/components/prod/icons/ArrowLeftIcon.vue";

const { t } = useI18n();

const {
  listLabel,
  listLabelTemp,
  listLanguageLabel,
  selectedLabel,
  componentKey,
  isEditing,
  isAddNew,
} = storeToRefs(useLabelStore());
const { getListLabel } = useLabelStore();
const { showSnackbar } = useSnackbarStore();

const isShowPopupCancel = ref<boolean>(false);
const itemSelected = ref<any>(null);
const isShowPopupSave = ref<boolean>(false);
const isClickClose = ref<boolean>(false);
const requiredRef = ref<any>(null);

const handleEdit = (): void => {
  isEditing.value = true;
};

const handleClick = (item) => {
  itemSelected.value = item.langCode;
};

const handleSave = async (): Promise<void> => {
  const englishItem = selectedLabel.value?.items.find(
    ({ langCode }) => langCode === LabelLanguage.English
  );
  if (requiredRef.value?.length) requiredRef.value[0]?.handleValidate();
  isShowPopupSave.value = false;
  if (!englishItem?.labelName) {
    showSnackbar(t("product_platform.required_field_missing"), "error");
    return;
  }
  try {
    if (isAddNew.value) {
      const data: ILabelItem = {
        labelId: "",
        items:
          selectedLabel.value?.items.filter(
            ({ labelName, labelDscr }) =>
              Boolean(labelName) || Boolean(labelDscr)
          ) || [],
      };
      await createNewLabel(data);
      showSnackbar(t("product_platform.create_label_successfully"), "success");
    } else {
      const data: ILabelItem = {
        ...selectedLabel.value!,
        items:
          selectedLabel.value?.items.filter(
            ({ labelName, labelDscr }) =>
              Boolean(labelName) || Boolean(labelDscr)
          ) || [],
      };
      await updateLabel(data);
      showSnackbar(t("product_platform.update_label_successfully"), "success");
    }
    if (isAddNew.value) {
      if (listLabel.value.length === 14) {
        listLabel.value = listLabelTemp.value;
      } else {
        listLabel.value.shift();
      }
      isAddNew.value = false;
    }
    isEditing.value = false;
    await getListLabel();
    updateLabelI18n(listLabel.value);
  } catch (error: any) {
    if (error.errorCode === "400") {
      showSnackbar(error.errorMsg, "error");
    } else {
      showSnackbar(t("product_platform.internalServerError"), "error");
    }
  }
};

const handleCancel = (): void => {
  if (isClickClose.value || isAddNew.value) {
    selectedLabel.value = null;
    isClickClose.value = false;
  }
  if (isAddNew.value) {
    if (listLabel.value.length === 14) {
      listLabel.value = listLabelTemp.value;
    } else {
      listLabel.value.shift();
    }
    isAddNew.value = false;
  }
  isEditing.value = false;
  isShowPopupCancel.value = false;
};

const handleClose = (): void => {
  isClickClose.value = true;
  handleShowPopupCancel();
};

const handleShowPopupCancel = (): void => {
  if (!isEditing.value && !isAddNew.value) {
    selectedLabel.value = null;
  } else {
    isShowPopupCancel.value = true;
  }
};

const handleShowPopupSave = (): void => {
  isShowPopupSave.value = true;
};

const handleClosePopupCancel = (): void => {
  isShowPopupCancel.value = false;
};

const handleClosePopupSave = (): void => {
  isShowPopupSave.value = false;
};

watch(
  () => selectedLabel.value,
  (val) => {
    if (val) {
      itemSelected.value = null;
    }
  },
  {
    deep: true,
  }
);
</script>

<style lang="scss" scoped>
.is-active {
  border: 1px solid var(--border-border-primary, #d9325a);
  box-shadow: 0px 0px 0px 4px #d9325a29;
}

:deep(.v-input--disabled) {
  background-color: #f0f2f5 !important;
}

:deep(.v-field--disabled) {
  opacity: 1;
  background-color: #f0f2f5 !important;
}
</style>
