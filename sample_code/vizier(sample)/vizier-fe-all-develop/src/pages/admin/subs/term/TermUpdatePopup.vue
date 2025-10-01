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
          <!-- -------------- start row 0-------------- -->

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="voCapForm.vocaNm"
                :styles="'input-form'"
                :required="true"
                label="Term Name"
                :counter="150"
                :rules="useInputValidation({ required: true, maxLength: 150 })"
              >
              </base-input-text>
            </div>
          </div>
          <!-- -------------- start row 1-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="voCapForm.vocaCstcInfo"
                :required="true"
                :styles="'input-form'"
                label="Terminology"
                :counter="100"
                readonly
                :rules="useInputValidation({ required: true, maxLength: 100 })"
              >
                <template #append-inner>
                  <svg
                    width="12"
                    height="14"
                    viewBox="0 0 12 14"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                    @click="handleShowPopupAnalysis"
                  >
                    <path
                      fill-rule="evenodd"
                      clip-rule="evenodd"
                      d="M8.19526 0.528636C8.45561 0.268287 8.87772 0.268287 9.13807 0.528636L11.8047 3.1953C12.0651 3.45565 12.0651 3.87776 11.8047 4.13811L9.13807 6.80478C8.87772 7.06513 8.45561 7.06513 8.19526 6.80478C7.93491 6.54443 7.93491 6.12232 8.19526 5.86197L9.72386 4.33337H0.666667C0.298477 4.33337 1.98682e-08 4.0349 1.98682e-08 3.66671C1.98682e-08 3.29852 0.298477 3.00004 0.666667 3.00004H9.72386L8.19526 1.47145C7.93491 1.2111 7.93491 0.788986 8.19526 0.528636ZM3.80474 7.1953C4.06509 7.45565 4.06509 7.87776 3.80474 8.13811L2.27614 9.66671H11.3333C11.7015 9.66671 12 9.96518 12 10.3334C12 10.7016 11.7015 11 11.3333 11H2.27614L3.80474 12.5286C4.06509 12.789 4.06509 13.2111 3.80474 13.4714C3.54439 13.7318 3.12228 13.7318 2.86193 13.4714L0.195262 10.8048C-0.0650874 10.5444 -0.0650874 10.1223 0.195262 9.86197L2.86193 7.1953C3.12228 6.93495 3.54439 6.93495 3.80474 7.1953Z"
                      fill="#6B6D70"
                    />
                  </svg>
                </template>
              </base-input-text>
            </div>
          </div>

          <!-- -------------- start row 2-------------- -->

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="voCapForm.vocaEngAbb"
                :required="true"
                label="Abbreviation"
                :styles="'input-form'"
                :counter="50"
                :rules="useInputValidation({ required: true, maxLength: 50 })"
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

            <BaseButton
              :color="ButtonColorType.Secondary"
              :width="WIDTH_BUTTON.DUPLICATE_CHECK"
              :height="HEIGHT_BUTTON.DUPLICATE_CHECK"
              :loading="isLoadingButon"
              :disabled="props.formType === FORM_TYPE_OPTION.UPDATE"
              @click="checkDuplicate"
            >
              Duplicate Check
            </BaseButton>
          </div>
        </div>

        <!-- -------------- start row 3-------------- -->

        <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
          <div class="flex-1">
            <base-input-text
              v-model="voCapForm.vocaEngNm"
              :styles="'input-form'"
              :required="true"
              label="Eng. Term Name"
              :counter="250"
              :rules="useInputValidation({ required: true, maxLength: 250 })"
            >
            </base-input-text>
          </div>
        </div>

        <!-- -------------- start row 4-------------- -->

        <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
          <div class="flex-1">
            <base-input-text
              v-model="voCapForm.domnNm"
              label="Domain Name"
              :styles="'input-form'"
              readonly
            >
            </base-input-text>
          </div>
          <BaseButton
            :color="ButtonColorType.Secondary"
            :width="WIDTH_BUTTON.AUTO"
            @click="handleShowDomainLookup"
          >
            Select Domain
          </BaseButton>
        </div>

        <!-- ------------ start row 5---------------------- -->
        <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
          <div class="flex-1">
            <base-input-text
              v-model="voCapForm.vocaDivsCd"
              :styles="'input-form'"
              label="Data Type"
              :counter="10"
              :rules="useInputValidation({ maxLength: 10 })"
            >
            </base-input-text>
          </div>
          <div class="flex-1">
            <base-input-text
              v-model="voCapForm.domnLen"
              :styles="'input-form'"
              label="Data Length"
            >
            </base-input-text>
          </div>

          <div class="flex-1">
            <base-select
              v-model="voCapForm.stndYn"
              :label="'Standard'"
              :density="'comfortable'"
              :items="domainYnOption"
              :item-title="'title'"
              :required="true"
              :hide-details="false"
            />
          </div>
        </div>

        <!-- ------------ start row 6---------------------- -->
        <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
          <div class="flex-1">
            <base-input-text
              v-model="voCapForm.vocaDscr"
              :styles="'input-form'"
              :required="true"
              label="Explanation"
              :counter="1000"
              :rules="useInputValidation({ required: true, maxLength: 1000 })"
            >
            </base-input-text>
          </div>
        </div>
        <!-- ---------------------------------- -->
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
  <TermAnalysisPopup
    v-if="openPopupAnalysis"
    v-model="openPopupAnalysis"
    :data="propsAnalysis"
    @apply="handleApply"
  >
  </TermAnalysisPopup>
  <DomainLookup
    v-if="isShowDomainLookup"
    v-model="isShowDomainLookup"
    @apply-domain-lookup="handleApplyDomainLookup"
  />
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

import {
  useUser,
  useSnackbarStore,
  useLoadingStore,
  useTerminologyStore,
  useDomainPopupStore,
} from "@/store";
import { v4 as uuidv4 } from "uuid";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import { httpClient } from "@/utils/http-common";
import { useInputValidation } from "@/composables/useInputValidation";
import { useFormValidation } from "@/composables/useFormValidation";
import { FormRef } from "@/interfaces/admin/admin";
import cloneDeep from "lodash-es/cloneDeep";
import TermAnalysisPopup from "@/pages/admin/subs/term/TermAnalysisPopup.vue";
import DomainLookup from "@/pages/admin/subs/domain/DomainLookup.vue";
import { VOCAB_DIV_CD_TYPE } from "@/constants/admin/terminology";
import {
  USE_YN_OPTION_CREATE,
  DuplicateCodeStatus,
  FORM_TYPE_OPTION,
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
const domainLookupStore = useDomainPopupStore();
const useSnackbar = useSnackbarStore();
const loadingStore = useLoadingStore();
const terminologyStore = useTerminologyStore();

// computed
const title = computed(() => {
  return props.formType === FORM_TYPE_OPTION.CREATE
    ? "Term Create"
    : "Term Edit";
});
const user = computed(() => {
  return userStore.user;
});
const selectedDomain = computed(() => domainLookupStore.getSelectedItem);
const domainYnOption = computed(() => {
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

const voCapForm = ref<any>({
  vocaNm: "",
  vocaCstcInfo: "",
  vocaEngAbb: "",
  vocaEngNm: "",
  stndYn: "",
  domnNm: "",
  domnDivsCd: "",
  domnLen: "",
  vocaDscr: "",
  vocaDivsCd: "",
  domnId: "",
  vocaId: "",
  updUsr: "",
  updtDtm: "",
});

const isLoadingButon = ref(false);
const mesageDuplicate = ref("");
const isDuplicate = ref(false);
const openPopupAnalysis = ref(false);
const propsAnalysis = ref();
const isShowDomainLookup = ref(false);
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

  try {
    loadingStore.setLoading(true);
    let response;
    voCapForm.value.vocaDivsCd = VOCAB_DIV_CD_TYPE.VO;

    if (props.formType === FORM_TYPE_OPTION.CREATE) {
      voCapForm.value.vocaId = uuidv4().substring(0, 20);
      voCapForm.value.rgstUsr = user.value.name;
      voCapForm.value.rgstDtm = CommonOrdrUtil.getCurrentTime();
      voCapForm.value.updUsr = "";
      voCapForm.value.updtDtm = "";

      response = await httpClient.post(
        `/api/comm/voca/v1/term`,
        voCapForm.value
      );
    } else {
      const requestBody = {
        vocaId: voCapForm.value.vocaId,
        vocaNm: voCapForm.value.vocaNm,
        vocaCstcInfo: voCapForm.value.vocaCstcInfo,
        vocaEngAbb: voCapForm.value.vocaEngAbb,
        vocaEngNm: voCapForm.value.vocaEngNm,
        vocaDscr: voCapForm.value.vocaDscr,
        vocaDivsCd: voCapForm.value.vocaDivsCd,
        domnId: voCapForm.value.domnId,
        rgstUsr: voCapForm.value.rgstUsr,
        rgstDtm: voCapForm.value.rgstDtm,
        updUsr: user.value.name,
        stndYn: voCapForm.value.stndYn,
        updDtm: CommonOrdrUtil.getCurrentTime(),
      };

      response = await httpClient.put(`/api/comm/voca/v1`, requestBody);
    }

    if (response.status === 200) {
      useSnackbar.showSnackbar("Successfully saved.", "success");
      await terminologyStore.fetchTerminology();
      isOpen.value = false;
    }
  } catch (error: any) {
    if (error?.errorCode === "duplicate") {
      mesageDuplicate.value = "동일한 용어영문약자가 존재합니다.";
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

  try {
    const response = await httpClient.post(`/api/comm/voca/v1/engAbb`, {
      vocaEngAbb: voCapForm.value.vocaEngAbb,
      vocaId: voCapForm.value.vocaId,
    });

    isDuplicate.value = response.data === DuplicateCodeStatus.FAIL;

    if (isDuplicate.value) {
      mesageDuplicate.value = "동일한 용어영문약자가 존재합니다.";
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
  if (props.formType === FORM_TYPE_OPTION.UPDATE) {
    voCapForm.value = cloneDeep(props.data);
  }
});

const handleShowPopupAnalysis = () => {
  propsAnalysis.value = {
    analyticalTermName: voCapForm.value.vocaNm,
  };
  openPopupAnalysis.value = true;
};

const handleApply = (item: any) => {
  voCapForm.value.vocaEngAbb = item.termAbbreviation;
  voCapForm.value.vocaEngNm = item.termEnglishName;
  voCapForm.value.vocaCstcInfo = item.termConfigurationInformation;
};

const handleShowDomainLookup = () => {
  isShowDomainLookup.value = true;
};

const handleApplyDomainLookup = () => {
  voCapForm.value.domnNm = selectedDomain.value.domnNm;
  voCapForm.value.vocaDivsCd = selectedDomain.value.domnDivsCd;
  voCapForm.value.domnLen = selectedDomain.value.domnLen;
  voCapForm.value.domnId = selectedDomain.value.domnId;
};
</script>
