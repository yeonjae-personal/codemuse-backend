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
        <div class="w-[640px] px-6 pt-6 overflow-auto">
          <!-- -------------- start row 1-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="domainForm.domnNm"
                :required="true"
                label="Domain Name"
                :styles="'input-form'"
                :counter="150"
                :rules="useInputValidation({ required: true, maxLength: 150 })"
                :error-messages="menageDuplicate"
                :disabled="isUpdateMode"
              >
                <template v-if="showIconSuccess" #append-inner>
                  <v-icon color="success"> mdi-check-circle </v-icon>
                </template>
              </base-input-text>
            </div>

            <BaseButton
              :color="ButtonColorType.Secondary"
              :width="WIDTH_BUTTON.DUPLICATE_CHECK"
              :height="HEIGHT_BUTTON.DUPLICATE_CHECK"
              :loading="isLoadingButton"
              :disabled="isUpdateMode"
              @click="checkDuplicate"
            >
              Duplicate Check
            </BaseButton>
          </div>

          <!-- -------------- start row 2-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="domainForm.domnEngNm"
                :required="true"
                :styles="'input-form'"
                label="English Domain Name"
                readonly
                :counter="250"
                :rules="useInputValidation({ required: true, maxLength: 250 })"
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

            <div class="flex-1">
              <base-select
                v-model="domainForm.domnGrpCd"
                :label="'Domain Groups'"
                :density="'comfortable'"
                :items="domainGroupOption"
                :item-title="'title'"
                :required="true"
                :hide-details="false"
              />
            </div>
          </div>

          <!-- -------------- start row 3-------------- -->

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-select
                v-model="domainForm.domnDivsCd"
                :label="'Domain Type'"
                :density="'comfortable'"
                :items="domainTypeOption"
                :item-title="'title'"
                :required="true"
                :hide-details="false"
              />
            </div>
            <div class="flex-1">
              <base-select
                v-model="domainForm.useYn"
                :label="'Usage'"
                :density="'comfortable'"
                :items="domainYnOption"
                :item-title="'title'"
                :required="true"
                :hide-details="false"
              />
            </div>

            <div class="flex-1">
              <base-input-text
                v-model="domainForm.domnLen"
                :styles="'input-form'"
                label="Data Length"
                :rules="
                  useInputValidation({ maxLength: 10, onlyNumbers: true })
                "
              >
              </base-input-text>
            </div>
          </div>

          <!-- -------------- start row 4-------------- -->

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="domainForm.domnDscr"
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
        <BaseButton :size="ButtonSizeType.Large" @click="handleValidate()">
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
  <TermAnalysisPopup
    v-if="openPopupAnalysis"
    v-model="openPopupAnalysis"
    :data="propsAnalysis"
    @apply="handleApply"
  >
  </TermAnalysisPopup>

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
  useDomainStore,
} from "@/store";
import { v4 as uuidv4 } from "uuid";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import { httpClient } from "@/utils/http-common";
import { useInputValidation } from "@/composables/useInputValidation";
import cloneDeep from "lodash-es/cloneDeep";
import TermAnalysisPopup from "@/pages/admin/subs/term/TermAnalysisPopup.vue";
import {
  FORM_TYPE_OPTION,
  USE_YN_OPTION_CREATE,
  DuplicateCodeStatus,
} from "@/constants/admin/admin";
import { useFormValidation } from "@/composables/useFormValidation";
import { FormRef } from "@/interfaces/admin/admin";
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
    type: Object as PropType<any>,
    default: null,
  },
});

const { t } = useI18n();
const userStore = useUser();
const useSnackbar = useSnackbarStore();
const loadingStore = useLoadingStore();
const domainStore = useDomainStore();
const { domainTypeOption, domainGroupOption } = storeToRefs(useDomainStore());

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
    ? "Domain Create"
    : "Domain Edit";
});
const user = computed(() => {
  return userStore.user;
});
const showIconSuccess = computed(
  () => menageDuplicate.value === "" && isDuplicate.value !== null
);

const domainYnOption = computed(() => {
  return USE_YN_OPTION_CREATE;
});

const isUpdateMode = computed(() => props.formType === FORM_TYPE_OPTION.UPDATE);

// form
const openPopupAnalysis = ref(false);
const propsAnalysis = ref();
const formRef = ref<FormRef | null>(null);
const { validateForm } = useFormValidation(formRef as unknown as FormRef);

const domainForm = ref({
  domnId: "",
  domnNm: "",
  domnEngNm: "",
  domnGrpCd: "",
  domnDivsCd: "",
  domnLen: "",
  domnDscr: "",
  useYn: "",
  rgstUsr: "",
  rgstDtm: "",
  updtUsr: "",
  updtDtm: "",
});

// duplicate check
const isLoadingButton = ref(false);
const isDuplicate = ref<boolean | null>(null);
const menageDuplicate = ref("");

const openPopupConfirm = ref(false);

// method
const handleSubmit = async () => {
  openPopupConfirm.value = false;
  try {
    loadingStore.setLoading(true);
    let response;

    if (props.formType === FORM_TYPE_OPTION.CREATE) {
      domainForm.value.domnId = uuidv4().substring(0, 20);

      response = await httpClient.post(`/api/comm/domn/v1`, domainForm.value);
    } else {
      const payload = {
        domnId: domainForm.value.domnId,
        domnNm: domainForm.value.domnNm,
        domnEngNm: domainForm.value.domnEngNm,
        domnDivsCd: domainForm.value.domnDivsCd,
        domnGrpCd: domainForm.value.domnGrpCd,
        useYn: domainForm.value.useYn,
        domnLen: domainForm.value.domnLen,
        domnDscr: domainForm.value.domnDscr,
        rgstUsr: domainForm.value.rgstUsr,
        rgstDtm: domainForm.value.rgstDtm,
        updUsr: user.value.name,
        updDtm: CommonOrdrUtil.getCurrentTime(),
      };
      response = await httpClient.put(`/api/comm/domn/v1`, payload);
    }

    if (response.status === 200) {
      useSnackbar.showSnackbar("Successfully saved.", "success");
      await domainStore.fetchDomains();
      isOpen.value = false;
    }
  } catch (error: any) {
    if (error?.errorCode === "duplicate") {
      menageDuplicate.value = "동일한 도메인명이 존재합니다";
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
  isLoadingButton.value = true;

  try {
    const response = await httpClient.post(`/api/comm/domn/v1/domnName`, {
      domnId: domainForm.value.domnId,
      domnNm: domainForm.value.domnNm,
    });

    isDuplicate.value = response.data === DuplicateCodeStatus.FAIL;
    if (isDuplicate.value) {
      menageDuplicate.value = "동일한 도메인명이 존재합니다";
    } else {
      menageDuplicate.value = "";
    }
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
  } finally {
    isLoadingButton.value = false;
  }
};

onMounted(async () => {
  await domainStore.fetchDomainOption();
  if (props.formType === FORM_TYPE_OPTION.UPDATE) {
    domainForm.value = cloneDeep(props.data);
  }
});

const handleShowPopupAnalysis = () => {
  propsAnalysis.value = {
    analyticalTermName: domainForm.value.domnNm,
  };

  openPopupAnalysis.value = true;
};

const handleApply = (item: any) => {
  domainForm.value.domnEngNm = item.termAbbreviation;
};
</script>
