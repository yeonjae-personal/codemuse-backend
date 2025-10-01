<template>
  <BasePopup
    v-model="isOpen"
    :title="title"
    :size="DialogSizeType.XMedium"
    :submit-button-text="t('product_platform.save')"
    :cancel-button-text="t('product_platform.cancel')"
  >
    <template #body>
      <v-form ref="formRef">
        <div class="w-[640px] px-6 pt-6 gap-3">
          <!-- -------------- start row 1-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="orgInfo.orgCd"
                :required="true"
                :label="$t('product_platform.orgInfoEntity.table.orgCd')"
                :rules="
                  useInputValidation({
                    required: true,
                    maxLength: 10,
                    onlyNumbers: true,
                  })
                "
                :styles="'input-form'"
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
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="orgInfo.orgNm"
                :required="true"
                :label="$t('product_platform.orgInfoEntity.table.orgNm')"
                :styles="'input-form'"
                :rules="
                  useInputValidation({
                    required: true,
                    maxLength: 100,
                    engKorNumHasSpaceRule: true,
                  })
                "
              >
              </base-input-text>
            </div>
          </div>
          <!-- -------------- start row 3-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-select
                v-model="orgInfo.orgKdCd"
                :label="$t('product_platform.orgInfoEntity.search.orgType')"
                :density="'comfortable'"
                :items="orgKdCdOptions"
                :required="true"
                :item-title="'title'"
                :item-value="'value'"
                :class="`border border-[#E5E7EB] ${
                  props.formType === FORM_TYPE_OPTION.UPDATE
                    ? 'base-select-disabled'
                    : 'base-select'
                }`"
              />
            </div>
            <div class="flex-1">
              <base-select
                v-model="orgInfo.orgLvCd"
                :label="
                  $t('product_platform.orgInfoEntity.createEdit.orgLevel')
                "
                :required="true"
                :density="'comfortable'"
                :items="orgLvCdOptions"
                :item-title="'title'"
                :item-value="'value'"
                :error-messages="messageCheckLeve"
                :class="`border border-[#E5E7EB] ${
                  props.formType === FORM_TYPE_OPTION.UPDATE
                    ? 'base-select-disabled'
                    : 'base-select'
                }`"
              />
            </div>
            <div class="flex-1">
              <base-select
                v-model="orgInfo.orgStatCd"
                :label="
                  $t('product_platform.orgInfoEntity.createEdit.orgStatus')
                "
                :required="true"
                :density="'comfortable'"
                :items="statCdOptions"
                :item-title="'title'"
                :item-value="'value'"
                :class="`border border-[#E5E7EB]${
                  props.formType === FORM_TYPE_OPTION.UPDATE
                    ? 'base-select-disabled'
                    : 'base-select'
                }`"
              />
            </div>
          </div>

          <!-- -------------- start row 4-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="orgInfo.tlmdNm"
                :label="
                  $t('product_platform.orgInfoEntity.createEdit.teamLeader')
                "
                :styles="'input-form'"
                :readonly="true"
              >
                <template #append-inner>
                  <div class="flex justify-space-between gap-2">
                    <BaseButton
                      :color="ButtonColorType.Gray"
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      @click="showPopupSelectUser"
                    >
                      <search-icon size="20" fill="#6B6D70"></search-icon>
                    </BaseButton>
                    <BaseButton
                      :color="ButtonColorType.Gray"
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      @click="resetSelectUser()"
                    >
                      <DeleteIcon></DeleteIcon>
                    </BaseButton>
                  </div>
                </template>
              </base-input-text>
            </div>
            <div class="flex-1">
              <base-input-text
                v-model="orgInfo.hposOrgNm"
                :label="
                  $t('product_platform.orgInfoEntity.createEdit.parentOrg')
                "
                :styles="'input-form'"
                :readonly="true"
              >
                <template #append-inner>
                  <div class="flex justify-space-between gap-2">
                    <BaseButton
                      :color="ButtonColorType.Gray"
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      @click="showPopupSelectOrg"
                    >
                      <search-icon size="20" fill="#6B6D70"></search-icon>
                    </BaseButton>
                    <BaseButton
                      :color="ButtonColorType.Gray"
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      @click="resetSelectOrg()"
                    >
                      <DeleteIcon></DeleteIcon>
                    </BaseButton>
                  </div>
                </template>
              </base-input-text>
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
  <UserOrgPopup
    v-if="isOpenPopupSelectUser"
    v-model="isOpenPopupSelectUser"
    @selected-item="onSelectItemUser"
  />
  <OrgSearch
    v-if="isOpenPopupSelectOrg"
    v-model="isOpenPopupSelectOrg"
    @selected-item="onSelectItemOrg"
  />
  <base-popup
    v-model="openPopupConfirm"
    :icon="DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.commonAdmin.confirmSave')"
    @on-submit="handleSave"
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
import { httpClient } from "@/utils/http-common";
import { useInputValidation } from "@/composables/useInputValidation";
import { useFormValidation } from "@/composables/useFormValidation";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import useCmcdStore from "@/store/cmcd.store";
import { useSnackbarStore, useLoadingStore, useOrgStore } from "@/store";
import {
  API_CHECK_DUPLICATE_ORG_INFO_PATH,
  API_ORG_CREATE_EDIT_PATH,
} from "@/api/admin/path";
import { FormRef } from "@/interfaces/admin/admin";
import OrgSearch from "@/pages/admin/subs/org/OrgSearch.vue";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";
const UserOrgPopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/user/UserOrgPopup.vue")
);

const formRef = ref<any>(null);
const { validateForm } = useFormValidation(formRef as unknown as FormRef);
const { search } = useCmcdStore();
const useSnackbar = useSnackbarStore();
const loadingStore = useLoadingStore();
const orgStore = useOrgStore();
const { t } = useI18n();
const emit = defineEmits(["update:modelValue", "resetItemSelected"]);
const props = defineProps({
  data: {
    type: Object,
    default: null,
  },
  modelValue: {
    type: Boolean,
    default: false,
  },
  formType: {
    type: String,
    default: FORM_TYPE_OPTION.CREATE,
  },
});

const closeDialog = () => {
  isOpen.value = false;
};
const isOpenPopupSelectUser = ref(false);
const isOpenPopupSelectOrg = ref(false);
const openPopupConfirm = ref(false);
const title = computed(() => {
  return props.formType === FORM_TYPE_OPTION.CREATE
    ? t("product_platform.orgInfoEntity.title.orgCreate")
    : t("product_platform.orgInfoEntity.title.orgEdit");
});
const isOpen = computed({
  // getter
  get() {
    return props.modelValue;
  },
  // setter
  set(newValue) {
    emit("update:modelValue", newValue);
  },
});

const orgKdCdOptions = ref<any[]>([]);
const orgLvCdOptions = ref<any[]>([]);
const statCdOptions = ref<any[]>([]);
const isDuplicate = ref<boolean | null>(null);
const messageDuplicate = ref("");
const messageCheckLeve = ref("");
const isLoadingButton = ref(false);
const orgInfo = ref({
  orgCd: "",
  orgNm: "",
  orgKdCd: "",
  orgLvCd: "",
  tlmdId: "",
  tlmdNm: "",
  hposOrgCd: "",
  hposOrgNm: "",
  hposOrgLvCd: "",
  orgStatCd: "",
  orgCdPathNm: "",
  orgNmPathNm: "",
  rgstUsr: "",
  rgstDtm: "",
  updUsr: "",
  updDtm: "",
});

const showPopupSelectUser = () => {
  isOpenPopupSelectUser.value = true;
};
const onSelectItemUser = (itemUser) => {
  orgInfo.value.tlmdId = itemUser.userId;
  orgInfo.value.tlmdNm = itemUser.userNm;
};
const resetSelectUser = () => {
  orgInfo.value.tlmdId = "";
  orgInfo.value.tlmdNm = "";
};

const showPopupSelectOrg = () => {
  isOpenPopupSelectOrg.value = true;
};
const onSelectItemOrg = (itemOrg) => {
  orgInfo.value.hposOrgCd = itemOrg.orgCd;
  orgInfo.value.hposOrgNm = itemOrg.orgNm;
  orgInfo.value.hposOrgLvCd = itemOrg.orgLvCd;
};
const resetSelectOrg = () => {
  orgInfo.value.hposOrgCd = "";
  orgInfo.value.hposOrgNm = "";
  orgInfo.value.hposOrgLvCd = "";
};

const handleSave = async () => {
  loadingStore.setLoading(true);
  const { valid } = await formRef.value.validate();
  if (!valid) {
    return;
  }

  try {
    let response;
    const org = {
      req: {
        ...orgInfo.value,
        orgNm: orgInfo.value.orgNm.trim(),
      },
    };
    if (props.formType === FORM_TYPE_OPTION.CREATE) {
      response = await httpClient.post(API_ORG_CREATE_EDIT_PATH, org.req);
    } else {
      response = await httpClient.put(API_ORG_CREATE_EDIT_PATH, org.req);
    }
    if (response.status === 200) {
      useSnackbar.showSnackbar(
        t("product_platform.successfully_saved"),
        "success"
      );
      emit("resetItemSelected");
      await orgStore.fetchMenuTree({});
      closeDialog();
    }
  } catch (error: any) {
    if (error?.errorCode === "duplicate") {
      messageDuplicate.value = t(
        "product_platform.orgInfoEntity.message.duplicate"
      );
    } else {
      useSnackbar.showSnackbar(error?.errorMsg as string, "error");
    }
  } finally {
    openPopupConfirm.value = false;
    loadingStore.setLoading(false);
  }
};

const handleValidate = async () => {
  const firstErrorFieldId = await validateForm();
  if (orgInfo.value?.orgCd && props.formType === FORM_TYPE_OPTION.CREATE) {
    await checkDuplicate();
  }
  if (isDuplicate.value) {
    useSnackbar.showSnackbar(
      t("product_platform.orgInfoEntity.message.duplicate"),
      "error"
    );
    return;
  }

  if (orgInfo.value.orgLvCd) {
    if (!checkLevel()) {
      useSnackbar.showSnackbar(
        t("product_platform.orgInfoEntity.message.levelIncorrect"),
        "error"
      );
      return;
    }
  }

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

const checkDuplicate = async () => {
  isLoadingButton.value = true;
  try {
    const response = await httpClient.post(API_CHECK_DUPLICATE_ORG_INFO_PATH, {
      orgCd: orgInfo.value.orgCd,
    });

    isDuplicate.value = response.data.data;
    if (isDuplicate.value) {
      messageDuplicate.value = t(
        "product_platform.orgInfoEntity.message.duplicate"
      );
    } else {
      messageDuplicate.value = "";
    }
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
  } finally {
    isLoadingButton.value = false;
  }
};

const checkLevel = () => {
  let isValid = false;
  if (!orgInfo.value?.hposOrgLvCd && orgInfo.value.orgLvCd == "1") {
    isValid = true;
  }
  if (
    orgInfo.value?.hposOrgLvCd &&
    Number(orgInfo.value.orgLvCd) === Number(orgInfo.value.hposOrgLvCd) + 1
  ) {
    isValid = true;
  }
  messageCheckLeve.value = isValid
    ? ""
    : t("product_platform.orgInfoEntity.message.levelIncorrect");
  return isValid;
};

onMounted(async () => {
  // Get list Organization Type Code.
  let kdCdOption = await search(["ORG_KD_CD"]);
  if (kdCdOption?.ORG_KD_CD) {
    orgKdCdOptions.value = [
      ...kdCdOption?.ORG_KD_CD.map((item) => ({
        title: item.cmcdDetlNm,
        value: item.cmcdDetlId,
      })),
    ];
  }
  // Get list Organization Level.
  let lvCdOptions = await search(["ORG_LV_CD"]);
  if (lvCdOptions?.ORG_LV_CD) {
    orgLvCdOptions.value = [
      ...lvCdOptions?.ORG_LV_CD?.map((item) => ({
        title: item.cmcdDetlNm,
        value: item.cmcdDetlId,
      })),
    ];
  }
  //Get Organization Status.
  let statusCdOptions = await search(["ORG_STAT_CD"]);
  if (statusCdOptions?.ORG_STAT_CD) {
    statCdOptions.value = [
      ...statusCdOptions?.ORG_STAT_CD.map((item) => ({
        title: item.cmcdDetlNm,
        value: item.cmcdDetlId,
      })),
    ];
  }

  if (props.formType === FORM_TYPE_OPTION.UPDATE) {
    orgInfo.value.orgCd = props.data?.orgCd;
    orgInfo.value.orgNm = props.data?.orgNm;
    orgInfo.value.orgKdCd = props.data?.orgKdCd;
    orgInfo.value.orgLvCd = props.data?.orgLvCd;
    orgInfo.value.tlmdId = props.data?.tlmdId
      ? props.data.tlmdId === "-"
        ? null
        : props.data.tlmdId
      : null;
    orgInfo.value.tlmdNm = props.data?.tlmdNm
      ? props.data.tlmdNm === "-"
        ? null
        : props.data.tlmdNm
      : null;
    orgInfo.value.hposOrgCd = props.data?.hposOrgCd
      ? props.data.hposOrgCd === "-"
        ? null
        : props.data.hposOrgCd
      : null;
    orgInfo.value.hposOrgNm = props.data?.hposOrgNm
      ? props.data.hposOrgNm === "-"
        ? null
        : props.data.hposOrgNm
      : null;
    orgInfo.value.hposOrgLvCd = props.data?.hposOrgLvCd
      ? props.data.hposOrgLvCd === "-"
        ? null
        : props.data.hposOrgLvCd
      : null;
    orgInfo.value.orgStatCd = props.data?.orgStatCd;
    orgInfo.value.orgCdPathNm = props.data?.orgCdPathNm;
    orgInfo.value.orgNmPathNm = props.data?.orgNmPathNm;
    orgInfo.value.rgstUsr = props.data?.rgstUsr;
    orgInfo.value.rgstDtm = props.data?.rgstDtm;
    orgInfo.value.updUsr = props.data?.updUsr;
    orgInfo.value.updDtm = props.data?.updDtm;
  }
});
</script>
