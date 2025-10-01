<template>
  <v-dialog v-model="loadingStore.loading" max-width="260" persistent contained>
    <v-progress-circular
      color="pink"
      indeterminate="disable-shrink"
      size="50"
      width="3"
      class="ml-[95px]"
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
        <div class="w-[640px] px-6 pt-6 gap-2">
          <!-- -------------- start row 1-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="requestForm.sysMsgId"
                :required="true"
                label="System Message ID"
                :styles="'input-form'"
                :counter="150"
                :rules="useInputValidation({ required: true, maxLength: 150 })"
                :error-messages="mesageDuplicate"
                :disabled="props.formType === FORM_TYPE_OPTION.UPDATE"
              >
                <template
                  v-if="isDuplicate !== null && isDuplicate === false"
                  #append-inner
                >
                  <v-icon color="success"> mdi-check-circle </v-icon>
                </template>
              </base-input-text>
            </div>
            <div class="w-[140px]">
              <base-select
                v-model="langObject"
                :label="'Language'"
                :density="'comfortable'"
                :items="langOption"
                :item-title="'title'"
                :disabled="props.formType === FORM_TYPE_OPTION.UPDATE"
                :class="`border border-[#E5E7EB] rounded-lg ${
                  props.formType === FORM_TYPE_OPTION.UPDATE
                    ? 'base-select-disabled'
                    : 'base-select'
                }`"
              />
            </div>

            <BaseButton
              :color="ButtonColorType.Secondary"
              :width="WIDTH_BUTTON.DUPLICATE_CHECK"
              :height="HEIGHT_BUTTON.DUPLICATE_CHECK"
              :disabled="props.formType === FORM_TYPE_OPTION.UPDATE"
              :loading="isLoadingButon"
              @click="checkDuplicate"
            >
              Duplicate Check
            </BaseButton>
          </div>

          <!-- -------------- start row 2-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[120px]">
            <div class="flex-1">
              <BaseInputTextArea
                v-model="requestForm.sysMsgCntn"
                :counter="2000"
                :maxlength="2000"
                :rules="useInputValidation({ required: true, maxLength: 2000 })"
                :label="'System Message Content'"
                rows="5"
                :styles="'input-form'"
                :required="true"
              ></BaseInputTextArea>
            </div>
          </div>
        </div>
      </v-form>
    </template>

    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton @click="handleValidate()">
          {{ t("product_platform.save") }}
        </BaseButton>
        <BaseButton :color="ButtonColorType.Gray" @click="closeDialog()">
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
import cloneDeep from "lodash-es/cloneDeep";
import { useSysMessageStore } from "@/store";
import { SYS_MSG_LANG_CD } from "@/constants/admin/sysMessage";
import { FORM_TYPE_OPTION, DuplicateCodeStatus } from "@/constants/admin/admin";
import { SysMsgFormRequest } from "@/pages/admin/types/message";
import { useFormValidation } from "@/composables/useFormValidation";
import { FormRef } from "@/interfaces/admin/admin";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

const sysMessageStore = useSysMessageStore();

const emit = defineEmits([
  "update:modelValue",
  "closePopup",
  "resetSelectedItem",
]);
const props = defineProps({
  formType: {
    type: String,
    default: FORM_TYPE_OPTION.CREATE,
  },
  modelValue: {
    type: Boolean,
    default: false,
  },
  data: {
    type: Object,
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
const title = computed(() => {
  return props.formType === FORM_TYPE_OPTION.CREATE
    ? "System Message Create"
    : "System Message Edit";
});

const user = computed(() => {
  return userStore.user;
});

const langOption = computed(() => {
  return SYS_MSG_LANG_CD;
});

const formRef = ref<FormRef | null>(null);
const { validateForm } = useFormValidation(formRef as unknown as FormRef);

const requestForm = ref<SysMsgFormRequest>({
  sysMsgId: "",
  sysMsgLangCd: "",
  sysMsgCntn: "",
  rgstUsr: "",
  rgstDtm: "",
  updUsr: "",
  updDtm: "",
});

const isDuplicate = ref<boolean | null>(null);
const isLoadingButon = ref(false);
const mesageDuplicate = ref("");

const langObject = ref("en");

const openPopupConfirm = ref(false);

// method

const handleSubmit = async () => {
  openPopupConfirm.value = false;

  try {
    loadingStore.setLoading(true);
    let response;
    requestForm.value.sysMsgLangCd = langObject.value;

    if (props.formType === FORM_TYPE_OPTION.CREATE) {
      requestForm.value.rgstUsr = user.value.name;
      requestForm.value.rgstDtm = CommonOrdrUtil.getCurrentTime();
      response = await httpClient.post(
        `/api/comm/sysmsg/v1`,
        requestForm.value
      );
    } else {
      const payload = {
        sysMsgId: requestForm.value.sysMsgId,
        sysMsgLangCd: langObject.value,
        sysMsgCntn: requestForm.value.sysMsgCntn,
        rgstUsr: requestForm.value.rgstUsr,
        rgstDtm: requestForm.value.rgstDtm,
        updUsr: user.value.name,
        updDtm: CommonOrdrUtil.getCurrentTime(),
      };

      response = await httpClient.put(`/api/comm/sysmsg/v1`, payload);
    }

    if (response.status === 200) {
      useSnackbar.showSnackbar("Successfully saved.", "success");
      await sysMessageStore.fetchSysMessages();
      isOpen.value = false;
    }
  } catch (error: any) {
    if (error?.errorCode === "duplicate") {
      mesageDuplicate.value = "동일한 시스템메시지가 존재합니다.";
      openPopupConfirm.value = false;
    } else {
      useSnackbar.showSnackbar(error?.errorMsg as string, "error");
      isOpen.value = false;
    }
  } finally {
    loadingStore.setLoading(false);
  }
};

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

const closeDialog = () => {
  isOpen.value = false;
};

const checkDuplicate = async () => {
  isLoadingButon.value = true;

  try {
    const response = await httpClient.post(
      `/api/comm/sysmsg/v1/check-duplicated`,
      {
        sysMsgId: requestForm.value.sysMsgId,
        sysMsgLangCd: langObject.value,
      }
    );
    isDuplicate.value = response.data !== DuplicateCodeStatus.OK;
    if (isDuplicate.value) {
      mesageDuplicate.value = "동일한 시스템메시지가 존재합니다.";
    } else {
      mesageDuplicate.value = "";
    }
  } catch (error: any) {
    if (isDuplicate.value) {
      mesageDuplicate.value = "동일한 시스템메시지가 존재합니다.";
    } else {
      mesageDuplicate.value = "";
    }
    // useSnackbar.showSnackbar(error?.errorMsg as string, "error");
  } finally {
    isLoadingButon.value = false;
  }
};

onMounted(async () => {
  if (props.formType === FORM_TYPE_OPTION.UPDATE) {
    requestForm.value = cloneDeep(props.data);
    langObject.value = props.data.sysMsgLangCd;
  }
});
</script>

<style lang="scss" scoped>
:deep(.base-select.v-input--disabled) {
  background-color: #f0f2f5 !important;
}
</style>
