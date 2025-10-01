<template>
  <v-dialog v-model="loadingStore.loading" max-width="260" persistent contained>
    <v-progress-circular
      color="pink"
      indeterminate="disable-shrink"
      size="50"
      width="3"
    ></v-progress-circular>
  </v-dialog>

  <BasePopup
    v-model="isOpen"
    :title="title"
    :size="DialogSizeType.XMedium"
    :submit-button-text="t('product_platform.save')"
    :cancel-button-text="t('product_platform.cancel')"
  >
    <template #body>
      <v-form ref="formRef">
        <div
          v-if="codeType === CODE_TYPE.CODE_GROUP"
          class="w-[640px] px-6 pt-6 gap-3"
        >
          <!-- -------------- start row 0-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="commonCodeForm.cmcdGrpId"
                class=" "
                :required="true"
                label="Code Group ID"
                :counter="50"
                :rules="useInputValidation({ required: true, maxLength: 50 })"
                :error-messages="mesageDuplicate"
                :styles="'input-form'"
              >
                <template
                  v-if="isDuplicate !== null && isDuplicate === false"
                  #append-inner
                >
                  <v-icon color="success"> mdi-check-circle </v-icon>
                </template>
              </base-input-text>
            </div>

            <BaseButton
              :color="ButtonColorType.Secondary"
              :width="WIDTH_BUTTON.DUPLICATE_CHECK"
              :height="HEIGHT_BUTTON.DUPLICATE_CHECK"
              @click="checkDuplicate"
            >
              Duplicate Check
            </BaseButton>
          </div>

          <!-- -------------- start row 1-------------- -->

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="commonCodeForm.cmcdGrpNm"
                :required="true"
                label="Code Group Name"
                :counter="50"
                :rules="useInputValidation({ required: true, maxLength: 50 })"
                :styles="'input-form'"
                :hide-details="false"
              >
              </base-input-text>
            </div>
          </div>

          <!-- -------------- start row 2-------------- -->

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="w-[150px]">
              <base-select
                v-model="commonCodeForm.useYn"
                :label="'Usage'"
                :density="'comfortable'"
                :items="commonCodeYnOption"
                :item-title="'title'"
                :required="true"
                :hide-details="false"
              />
            </div>
          </div>
        </div>

        <div v-else class="w-[640px] px-6 pt-6 gap-3">
          <!-- -------------- start row 0-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="labelCodeGroupInfo"
                class=" "
                :required="true"
                label="Code Group Information"
                :counter="50"
                :styles="'input-form'"
                :disabled="true"
              >
              </base-input-text>
            </div>
          </div>

          <!-- -------------- start row 1-------------- -->

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="commonCodeForm.cmcdDetlId"
                :required="true"
                label="Code ID"
                :rules="useInputValidation({ required: true, maxLength: 50 })"
                :styles="'input-form'"
                :error-messages="mesageDuplicate"
              >
                <template
                  v-if="isDuplicate !== null && isDuplicate === false"
                  #append-inner
                >
                  <v-icon color="success"> mdi-check-circle </v-icon>
                </template>
              </base-input-text>
            </div>

            <BaseButton
              :color="ButtonColorType.Secondary"
              :width="WIDTH_BUTTON.DUPLICATE_CHECK"
              :height="HEIGHT_BUTTON.DUPLICATE_CHECK"
              @click="checkDuplicate"
            >
              Duplicate Check
            </BaseButton>
          </div>

          <!-- -------------- start row 2-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="commonCodeForm.cmcdDetlNm"
                class=" "
                :required="true"
                label="Code Details Name"
                :counter="50"
                :rules="useInputValidation({ required: true, maxLength: 50 })"
                :styles="'input-form'"
              >
              </base-input-text>
            </div>
          </div>

          <!-- -------------- start row 3-------------- -->

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="w-[150px]">
              <base-input-text
                v-model="commonCodeForm.cmcdSortRank"
                class=" "
                :required="true"
                label="Sorting Rank"
                :counter="50"
                :styles="'input-form'"
                :rules="useInputValidation({ required: true, maxLength: 50 })"
              >
              </base-input-text>
            </div>
            <div class="w-[150px]">
              <base-select
                v-model="commonCodeForm.cmcdDetlUseYn"
                :label="'Usage'"
                :density="'comfortable'"
                :items="commonCodeYnOption"
                :item-title="'title'"
                :required="true"
                :hide-details="false"
              />
            </div>
          </div>
        </div>
      </v-form>
    </template>

    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton :size="ButtonSizeType.Large" @click="handleValidate">
          {{ t("product_platform.save") }}
        </BaseButton>
        <BaseButton
          :size="ButtonSizeType.Large"
          :color="ButtonColorType.Gray"
          @click="closeDialog()"
        >
          {{ t("product_platform.cancel") }}
        </BaseButton>
      </div>
    </template>
  </BasePopup>
  <base-popup
    v-model="openPopupConfirm"
    :icon="DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="'Do you want to save?'"
    @on-submit="handleSubmit"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import {
  ButtonColorType,
  ButtonSizeType,
  DialogSizeType,
  DialogIconType,
} from "@/enums";

import { useUser, useSnackbarStore, useLoadingStore } from "@/store";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import { httpClient } from "@/utils/http-common";
import { useInputValidation } from "@/composables/useInputValidation";
import { useFormValidation } from "@/composables/useFormValidation";
import { CODE_TYPE } from "@/constants/admin/code";
import {
  FORM_TYPE_OPTION,
  USE_YN_OPTION_CREATE,
  DuplicateCodeStatus,
} from "@/constants/admin/admin";
import { FormRef } from "@/interfaces/admin/admin";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

const emit = defineEmits(["update:modelValue"]);
const props = defineProps({
  formType: {
    type: String,
    default: FORM_TYPE_OPTION.CREATE,
  },
  codeType: {
    type: String,
    default: CODE_TYPE.CODE_DETAIL,
  },
  modelValue: {
    type: Boolean,
    default: false,
  },
  data: {
    type: Object as PropType<any>,
    default: null,
  },
});

const { t } = useI18n();
const userStore = useUser();
const useSnackbar = useSnackbarStore();
const loadingStore = useLoadingStore();

// computed
const isOpen = computed({
  get() {
    return props.modelValue;
  },
  set(newValue) {
    emit("update:modelValue", newValue);
  },
});

const codeType = computed(() => {
  return props.codeType;
});

const title = computed(() => {
  return props.codeType === CODE_TYPE.CODE_GROUP
    ? "Common Code Group Create"
    : "Common Code Details Create";
});
const messageDuplicate = computed(() => {
  return props.codeType === CODE_TYPE.CODE_GROUP
    ? "Duplicate Code Group"
    : "Duplicate Code Details";
});

const labelCodeGroupInfo = computed(() => {
  return `${props.data?.cmcdGrpNm} / ${props.data?.cmcdGrpId}`;
});

const user = computed(() => {
  return userStore.user;
});

const commonCodeYnOption = computed(() => {
  return USE_YN_OPTION_CREATE;
});

const formRef = ref<FormRef | null>(null);
const { validateForm } = useFormValidation(formRef as unknown as FormRef);

const commonCodeForm = ref<any>({
  cmcdGrpId: "",
  cmcdGrpNm: "",
  useYn: "Y",
  cmcdDetlId: "",
  cmcdDetlNm: "",
  cmcdSortRank: "",
  cmcdDetlUseYn: "",
  rgstUsr: "",
  rgstDtm: "",
  updDtm: "",
});

const isDuplicate = ref<boolean | null>(null);
const isLoadingButon = ref(false);
const mesageDuplicate = ref("");

const openPopupConfirm = ref(false);

// method
const handleValidate = async () => {
  const firstErrorFieldId = await validateForm();

  if (firstErrorFieldId) {
    const firstErrorField = document.getElementById(
      firstErrorFieldId.toString()
    );
    if (firstErrorField) {
      firstErrorField.focus();
    }
  } else {
    openPopupConfirm.value = true;
  }
};

const handleSubmit = async () => {
  openPopupConfirm.value = false;
  loadingStore.setLoading(true);

  try {
    let params;
    let response;

    if (props.codeType === CODE_TYPE.CODE_GROUP) {
      params = {
        cmcdGrpId: commonCodeForm.value.cmcdGrpId,
        cmcdGrpNm: commonCodeForm.value.cmcdGrpNm,
        useYn: commonCodeForm.value.useYn,
        rgstUsr: user.value.name,
        rgstDtm: CommonOrdrUtil.getCurrentTime(),
      };
      response = await httpClient.post(`/api/comm/cmcdgrp/v1`, params);
    } else {
      params = {
        cmcdGrpId: commonCodeForm.value.cmcdGrpId,
        cmcdDetlId: commonCodeForm.value.cmcdDetlId,
        cmcdDetlNm: commonCodeForm.value.cmcdDetlNm,
        cmcdSortRank: commonCodeForm.value.cmcdSortRank,
        useYn: commonCodeForm.value.cmcdDetlUseYn,
        rgstUsr: commonCodeForm.value.rgstUsr,
        rgstDtm: commonCodeForm.value.rgstDtm,
      };
      response = await httpClient.post(`/api/comm/cmcddetl/v1`, params);
    }

    if (response.status === 200) {
      useSnackbar.showSnackbar("Successfully saved.", "success");
      isOpen.value = false;
    }
  } catch (error: any) {
    if (error?.errorCode === "duplicate") {
      useSnackbar.showSnackbar(messageDuplicate.value, "error");
    } else {
      useSnackbar.showSnackbar(error?.errorMsg as string, "error");
      isOpen.value = false;
    }
  } finally {
    loadingStore.setLoading(false);
  }
};

const closeDialog = () => {
  isOpen.value = false;
};

const checkDuplicate = async () => {
  isLoadingButon.value = true;
  let response;

  try {
    if (props.codeType === CODE_TYPE.CODE_GROUP) {
      response = await httpClient.post(`/api/comm/cmcd/v1/cmcdGrpId`, {
        cmcdGrpId: commonCodeForm.value.cmcdGrpId,
      });
    } else {
      response = await httpClient.post(`/api/comm/cmcd/v1/cmcdDetlId`, {
        cmcdGrpId: commonCodeForm.value.cmcdGrpId,
        cmcdDetlId: commonCodeForm.value.cmcdDetlId,
      });
    }

    isDuplicate.value = response.data === DuplicateCodeStatus.FAIL;

    if (isDuplicate.value) {
      mesageDuplicate.value = messageDuplicate.value;
    } else {
      mesageDuplicate.value = "";
    }
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
  } finally {
    isLoadingButon.value = false;
  }
};

onMounted(async () => {
  if (props.codeType === CODE_TYPE.CODE_DETAIL) {
    commonCodeForm.value.cmcdGrpNm = props.data?.cmcdGrpNm;

    commonCodeForm.value.cmcdGrpId = props.data?.cmcdGrpId;
  } else {
    commonCodeForm.value = {
      cmcdGrpId: "",
      cmcdGrpNm: "",
      useYn: "",
      rgstUsr: "",
      rgstDtm: "",
      updUsr: "",
      updDtm: "",
    };
  }
});
</script>

<style lang="scss" scoped>
.custom-button {
  width: 140px;
  height: 48px;
  color: #ba1642;
  background-color: #fff0f2;
}

.custom-row {
  gap: 16px;
}
</style>
