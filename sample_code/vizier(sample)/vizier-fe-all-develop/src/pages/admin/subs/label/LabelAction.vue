<template>
  <div class="label-list-actions">
    <BaseButton
      :color="ButtonColorType.Gray"
      :width="WIDTH_BUTTON.EXCEL"
      :disabled="downloading"
      @click="handleDownloadFile"
    >
      <DownloadIcon class="mr-[6px]" />
      {{ $t("product_platform.download") }}
    </BaseButton>
    <BaseButton
      :color="ButtonColorType.Gray"
      :width="WIDTH_BUTTON.EXCEL"
      @click="handleOpenPopupUpload"
    >
      <UploadIcon class="mr-[6px]" />
      {{ $t("product_platform.upload") }}
    </BaseButton>
    <BaseButton :color="ButtonColorType.Secondary" @click="addNewLabelItem">
      <AddLabelIcon class="mr-[6px]" />
      {{ $t("product_platform.add") }}
    </BaseButton>
  </div>
  <UploadLabelPopup v-if="isShowUploadPopup" v-model="isShowUploadPopup" />
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import useLabelStore from "@/store/admin/label.store";
import { DOWNLOAD_LABEL_LIST_EXCEL } from "@/api/prod/path";
import { useDownloadFile } from "@/composables/useDownloadFIle";
import DownloadIcon from "@/components/prod/icons/DownloadIcon.vue";
import type { ILabelDownloadParams } from "@/interfaces/admin/label-management";
import { ButtonColorType } from "@/enums";
import { WIDTH_BUTTON } from "@/constants/index";
const UploadLabelPopup = defineAsyncComponent(
  () => import("./UploadLabelPopup.vue")
);

const { locale } = useI18n();
const { searchParams, addNewLabelItem } = useLabelStore();
const { isOpenPopup, isEditing, isAddNew } = storeToRefs(useLabelStore());
const { downloading, downloadFile } = useDownloadFile();

const isShowUploadPopup = ref<boolean>(false);

const handleOpenPopupUpload = (): void => {
  if (isEditing.value || isAddNew.value) {
    isOpenPopup.value = true;
  } else {
    isShowUploadPopup.value = true;
  }
};

const handleDownloadFile = (): void => {
  const params: ILabelDownloadParams = {
    language: locale.value || "en",
    type: searchParams.type,
    value: searchParams.value,
  };
  downloadFile(DOWNLOAD_LABEL_LIST_EXCEL, params, "label", "xlsx", "YYYYMMDD");
};
</script>

<style lang="scss" scoped>
.label-list-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
