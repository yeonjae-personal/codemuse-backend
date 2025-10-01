<template>
  <BasePopup
    v-model="isOpenPopup"
    :size="DialogSizeType.Small"
    :submit-button-text="t('product_platform.upload')"
    :cancel-button-text="t('product_platform.cancel')"
  >
    <template #header>
      <div class="upload-file-header">
        <div class="upload-file-header__title">{{ title }}</div>
        <CloseIcon class="cursor-pointer" @click="handleClosePopup" />
      </div>
    </template>
    <template #body>
      <div class="upload-file">
        <div class="upload-file__description">{{ description }}</div>
        <div
          :class="[
            'upload-file-drag',
            {
              'is-draggable': isDragging,
              'is-disabled': isExistedFileUploaded,
            },
          ]"
          @drop.prevent="handleDropFile"
          @dragover.prevent="handleDragOver"
          @dragleave.prevent="handleDragLeaveLeave"
        >
          <UploadedLabelIcon v-if="isExistedFileUploaded" />
          <UploadLabelIcon v-else />
          <div class="upload-file-drag__description">
            {{
              isExistedFileUploaded
                ? t("product_platform.file_has_been_uploaded")
                : t("product_platform.choose_a_file_or_drag_drop")
            }}
          </div>
          <BaseButton
            v-if="!isExistedFileUploaded"
            :color="ButtonColorType.Gray"
            :width="WIDTH_BUTTON.AUTO"
            @click="handleOpenUploadFile"
          >
            {{ t("product_platform.browse_file") }}
          </BaseButton>
          <input
            ref="uploadLabelRef"
            type="file"
            name="label-upload"
            class="upload-file-drag__input"
            :multiple="false"
            accept=".xls,.xlsx"
            @change="handleChangeUploadFiles"
          />
        </div>
        <div v-if="isExistedFileUploaded" class="upload-file-file">
          <div class="flex align-center gap-1">
            <div class="upload-file-file__name text-ellipsis">
              <CustomTooltip
                :content="fileUploaded?.name"
                location="bottom"
                is-inline
              />
            </div>
            <div class="upload-file-file__size">
              ({{ formatFileSize(fileUploaded?.size!) }})
            </div>
          </div>
          <CloseIcon
            class="upload-file-file__icon cursor-pointer h-[36px]"
            @click="handleRemoveUploadedFile"
          />
        </div>
      </div>
    </template>

    <template #footer>
      <div class="flex gap-3">
        <BaseButton :width="WIDTH_BUTTON.POPUP" @click="handleUploadLabelFile">
          {{ t("product_platform.upload") }}
        </BaseButton>
        <BaseButton
          :color="ButtonColorType.Gray"
          :width="WIDTH_BUTTON.POPUP"
          @click="handleClosePopup"
        >
          {{ t("product_platform.cancel") }}
        </BaseButton>
      </div>
    </template>
  </BasePopup>
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import { useSnackbarStore } from "@/store";
import { ButtonColorType, DialogSizeType } from "@/enums";
import { formatFileSize, isValidFileSize, isValidFileType } from "@/utils/file";
import { ALLOWED_FILE_TYPE, MAX_SIZE_UPLOAD } from "@/constants/admin/label";
import { WIDTH_BUTTON } from "@/constants/index";

type Props = {
  title: string;
  description: string;
  modelValue: boolean;
  onUploadFile: (file: File) => void;
};

const props = defineProps<Props>();

const emit = defineEmits(["update:modelValue"]);

const { t } = useI18n();

const { showSnackbar } = useSnackbarStore();

const fileUploaded = ref<File | null>(null);
const uploadLabelRef = ref<HTMLInputElement | null>(null);
const isDragging = ref<boolean>(false);

const isOpenPopup = computed<boolean>({
  get: () => props.modelValue,
  set: (value: boolean) => emit("update:modelValue", value),
});

const isExistedFileUploaded = computed<boolean>(() => !!fileUploaded.value);

const handleClosePopup = (): void => {
  isOpenPopup.value = false;
};

const handleRemoveUploadedFile = (): void => {
  fileUploaded.value = null;
  if (uploadLabelRef.value) {
    uploadLabelRef.value.value = "";
  }
};

const handleChangeUploadFiles = (_event: Event): void => {
  if (uploadLabelRef.value?.files && uploadLabelRef.value.files.length > 0) {
    const file = uploadLabelRef.value.files[0];
    if (isValidFileType(file, ALLOWED_FILE_TYPE)) {
      showSnackbar(t("product_platform.validate_file_format"), "error");
      return;
    }
    if (isValidFileSize(file, MAX_SIZE_UPLOAD)) {
      showSnackbar(
        t("product_platform.validate_file_size", { size: 5 }),
        "error"
      );
      return;
    }
    fileUploaded.value = file;
  }
};

const handleOpenUploadFile = (): void => {
  if (uploadLabelRef.value) uploadLabelRef.value.click();
};

const handleUploadLabelFile = async (): Promise<void> => {
  if (!fileUploaded.value) {
    showSnackbar("Please attach a file to upload!", "error");
    return;
  }
  props.onUploadFile(fileUploaded.value);
  handleClosePopup();
};

const handleDropFile = (event: DragEvent): void => {
  if (event.dataTransfer && event.dataTransfer.items) {
    const files = [...event.dataTransfer.items];
    if (files.length > 1) {
      showSnackbar(t("product_platform.validate_one_file"), "error");
      return;
    }
    files.forEach((item) => {
      if (item.kind === "file") {
        const file = item.getAsFile();
        if (!file) return;
        if (isValidFileType(file, ALLOWED_FILE_TYPE)) {
          showSnackbar(t("product_platform.validate_file_format"), "error");
          return;
        }
        if (isValidFileSize(file, MAX_SIZE_UPLOAD)) {
          showSnackbar(
            t("product_platform.validate_file_size", { size: 5 }),
            "error"
          );
          return;
        }
        fileUploaded.value = file;
      }
    });
  }
  isDragging.value = false;
};

const handleDragOver = (): void => {
  isDragging.value = true;
};

const handleDragLeaveLeave = (): void => {
  isDragging.value = false;
};
</script>

<style lang="scss" scoped>
.upload-file-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 24px;

  &__title {
    font-family: Noto Sans KR;
    font-weight: 500;
    font-size: 16px;
    line-height: 150%;
    letter-spacing: 0.5px;
    color: #3a3b3d;
  }
}

.upload-file {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 0 24px;
  font-family: Noto Sans KR;

  &__description {
    font-weight: 400;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
    color: #6b6d70;

    &--blue {
      text-decoration: none;
      margin-left: 4px;
      font-weight: 500;
      line-height: 100%;
      letter-spacing: 0.5px;
      color: #1570ef;
      cursor: pointer;
    }
  }
}

.upload-file-drag {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 24px 12px;
  border: 1px dashed #dce0e5;
  border-radius: 12px;
  background-color: #fff;
  transition: all 0.1s ease;

  &.is-draggable {
    background-color: #bdc1c7;
  }

  &.is-disabled {
    pointer-events: none;
  }

  &__description {
    font-family: Noto Sans KR;
    font-weight: 500;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
    text-align: center;
    color: #6b6d70;
  }

  &__input {
    display: none;
  }
}

.upload-file-file {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  background-color: #f7f8fa;
  border-radius: 8px;
  padding-left: 12px;

  &__name,
  &__size {
    font-family: Noto Sans KR;
    font-weight: 500;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
    color: #1570ef;
  }

  &__size,
  &__icon {
    flex-shrink: 0;
  }
}

.text-ellipsis {
  max-width: 250px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}
</style>
