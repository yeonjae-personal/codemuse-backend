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
        <div class="w-[640px] px-6 pt-6">
          <!-- -------------- start row 1-------------- -->
          <div class="flex gap-4 mb-2 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="vocabForm.vocaNm"
                :required="true"
                label="Vocab"
                :styles="'input-form'"
                :counter="150"
                :rules="useInputValidation({ required: true, maxLength: 150 })"
                :error-messages="messageDuplicate"
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
            <BaseButton
              :color="ButtonColorType.Secondary"
              :width="WIDTH_BUTTON.DUPLICATE_CHECK"
              :height="HEIGHT_BUTTON.DUPLICATE_CHECK"
              :disabled="props.formType === FORM_TYPE_OPTION.UPDATE"
              @click="checkDuplicate"
            >
              {{ $t("product_platform.commonAdmin.duplicateCheck") }}
            </BaseButton>
          </div>

          <!-- -------------- start row 2-------------- -->
          <div class="flex gap-4 mb-2 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="vocabForm.vocaEngAbb"
                :required="true"
                :styles="'input-form'"
                label="Abbreviations"
                :counter="50"
                :rules="useInputValidation({ required: true, maxLength: 50 })"
              >
              </base-input-text>
            </div>
            <div class="flex-1">
              <base-input-text
                v-model="vocabForm.vocaEngNm"
                :required="true"
                :styles="'input-form'"
                label="Vocab English Name"
                :counter="250"
                :rules="useInputValidation({ required: true, maxLength: 250 })"
              >
              </base-input-text>
            </div>
          </div>

          <!-- -------------- start row 3-------------- -->

          <div class="flex gap-4 mb-2 w-[592px] h-[48px]">
            <div class="w-[139px]">
              <base-select
                v-model="vocabForm.stndYn"
                :label="'Standard'"
                :density="'comfortable'"
                :items="vocabYnOption"
                :item-title="'title'"
                :required="true"
                :hide-details="false"
              />
            </div>
          </div>

          <!-- -------------- start row 4-------------- -->

          <div class="flex gap-4 mb-2 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="vocabForm.vocaDscr"
                :styles="'input-form'"
                :required="true"
                label="Explanation"
                :counter="1000"
                :rules="useInputValidation({ required: true, maxLength: 1000 })"
              >
              </base-input-text>
            </div>
          </div>
        </div>
      </v-form>
    </template>

    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton @click="handleValidate">
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
import { useTerminologyStore } from "@/store";
const terminologyStore = useTerminologyStore();
import {
  ButtonColorType,
  ButtonSizeType,
  DialogSizeType,
  DialogIconType,
} from "@/enums";

import { useUser, useSnackbarStore, useLoadingStore } from "@/store";
import { v4 as uuidv4 } from "uuid";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import { httpClient } from "@/utils/http-common";
import { useInputValidation } from "@/composables/useInputValidation";
import cloneDeep from "lodash-es/cloneDeep";
import { useFormValidation } from "@/composables/useFormValidation";
import { FormRef } from "@/interfaces/admin/admin";

import { VOCAB_DIV_CD_TYPE } from "@/constants/admin/terminology";
import {
  FORM_TYPE_OPTION,
  USE_YN_OPTION_CREATE,
  DuplicateCodeStatus,
} from "@/constants/admin/admin";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

const emit = defineEmits(["update:modelValue"]);
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
const title = computed(() => {
  return props.formType === FORM_TYPE_OPTION.CREATE
    ? "Vocab Create"
    : "Vocab Edit";
});
const user = computed(() => {
  return userStore.user;
});

const vocabYnOption = computed(() => {
  return USE_YN_OPTION_CREATE;
});
const isOpen = computed({
  get() {
    return props.modelValue;
  },
  set(newValue) {
    emit("update:modelValue", newValue);
  },
});
const formRef = ref<FormRef | null>(null);
const { validateForm } = useFormValidation(formRef as unknown as FormRef);

const vocabForm = ref({
  vocaId: "",
  vocaNm: "",
  vocaEngAbb: "",
  vocaEngNm: "",
  vocaDscr: "",
  vocaDivsCd: "WO",
  rgstUsr: "",
  rgstDtm: "",
  stndYn: "",
  updUsr: "",
  updDtm: "",
  domnId: "",
});

// domain Name
const isDuplicate = ref<boolean | null>(null);
const isLoadingButton = ref(false);
const messageDuplicate = ref("");

const openPopupConfirm = ref(false);

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
    loadingStore.setLoading(true);
    let response;
    vocabForm.value.vocaDivsCd = VOCAB_DIV_CD_TYPE.WO;

    if (props.formType === FORM_TYPE_OPTION.CREATE) {
      vocabForm.value.vocaId = uuidv4().substring(0, 20);
      vocabForm.value.rgstUsr = user.value.name;
      vocabForm.value.rgstDtm = CommonOrdrUtil.getCurrentTime();

      response = await httpClient.post(`/api/comm/voca/v1`, vocabForm.value);
    } else {
      const requestBody = {
        vocaId: vocabForm.value.vocaId,
        vocaNm: vocabForm.value.vocaNm,
        vocaCstcInfo: vocabForm.value.vocaCstcInfo,
        vocaEngAbb: vocabForm.value.vocaEngAbb,
        vocaEngNm: vocabForm.value.vocaEngNm,
        vocaDscr: vocabForm.value.vocaDscr,
        vocaDivsCd: vocabForm.value.vocaDivsCd,
        domnId: vocabForm.value.domnId,
        rgstUsr: vocabForm.value.rgstUsr,
        rgstDtm: vocabForm.value.rgstDtm,
        updUsr: user.value.name,
        stndYn: vocabForm.value.stndYn,
        updDtm: CommonOrdrUtil.getCurrentTime(),
      };

      response = await httpClient.put(`/api/comm/voca/v1`, requestBody);
    }

    if (response.status === 200) {
      isOpen.value = false;
      useSnackbar.showSnackbar("Successfully saved.", "success");
      await terminologyStore.fetchTerminology({
        srchWord: "",
        stndYn: "",
        vocaDivsCd: [],
      });
    }
  } catch (error: any) {
    if (error?.errorCode === "duplicate") {
      messageDuplicate.value = "동일한 단어명이 존재합니다.";
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
  isLoadingButton.value = true;

  try {
    const response = await httpClient.post(`/api/comm/voca/v1/vocaNm`, {
      vocaNm: vocabForm.value.vocaNm,
      vocaId: vocabForm.value.vocaId,
    });
    isDuplicate.value = response.data === DuplicateCodeStatus.FAIL;
    if (isDuplicate.value) {
      messageDuplicate.value = "동일한 단어명이 존재합니다.";
    } else {
      messageDuplicate.value = "";
    }
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
  } finally {
    isLoadingButton.value = false;
  }
};

onMounted(async () => {
  if (props.formType === FORM_TYPE_OPTION.UPDATE) {
    vocabForm.value = cloneDeep(props.data);
  }
});
</script>
