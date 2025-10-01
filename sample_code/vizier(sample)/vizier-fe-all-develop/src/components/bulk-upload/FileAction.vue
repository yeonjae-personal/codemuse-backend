<template>
  <div :class="['d-flex items-center gap-2', className]">
    <BaseButton
      :color="ButtonColorType.Gray"
      :width="WIDTH_BUTTON.EXCEL"
      :disabled="isDownloading"
      @click="onDownloadFile"
    >
      <DownloadIcon class="mr-[6px]" />
      {{ $t("product_platform.download") }}
    </BaseButton>
    <!-- TODO -->
    <BaseButton
      v-if="false"
      :color="ButtonColorType.Gray"
      :width="WIDTH_BUTTON.EXCEL"
      @click="handleOpenPopupUpload"
    >
      <UploadIcon class="mr-[6px]" />
      {{ $t("product_platform.upload") }}
    </BaseButton>
  </div>
  <UploadFilePopup
    v-if="isShowUploadPopup"
    v-model="isShowUploadPopup"
    :title="title"
    :description="description"
    @upload-file="onUploadFile"
  />
</template>

<script setup lang="ts">
import { WIDTH_BUTTON } from "@/constants/index";
import { ButtonColorType } from "@/enums";
import { useI18n } from "vue-i18n";

const UploadFilePopup = defineAsyncComponent(
  () => import("./UploadFilePopup.vue")
);
type Props = {
  title: string;
  description: string;
  className?: string;
  isDownloading?: boolean;
  onDownloadFile: () => Promise<void>;
  onUploadFile: (file: File) => void;
};

withDefaults(defineProps<Props>(), {
  className: "",
  isDownloading: false,
  onSuccess: () => {},
});

const { t } = useI18n();

const isShowUploadPopup = ref<boolean>(false);

const handleOpenPopupUpload = (): void => {
  isShowUploadPopup.value = true;
};
</script>
