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
                v-model="userForm.userId"
                :required="true"
                :label="$t('product_platform.userEntity.table.userId')"
                :rules="
                  useInputValidation({
                    required: true,
                    maxLength: 20,
                    engNumRule: true,
                  })
                "
                :styles="'input-form'"
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
              :disabled="props.formType === FORM_TYPE_OPTION.UPDATE"
              @click="checkDuplicate"
            >
              {{ $t("product_platform.commonAdmin.duplicateCheck") }}
            </BaseButton>
          </div>

          <!-- -------------- start row 1-------------- -->

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="userForm.userNm"
                :required="true"
                :label="$t('product_platform.userEntity.table.userNm')"
                :styles="'input-form'"
                :rules="
                  useInputValidation({
                    required: true,
                    maxLength: 20,
                    engKorHasSpaceRule: true,
                  })
                "
              >
              </base-input-text>
            </div>
          </div>

          <!-- -------------- start row 2-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="userForm.pw"
                :required="true"
                :label="$t('product_platform.userEntity.createEdit.password')"
                :styles="'input-form'"
                :rules="
                  useInputValidation({
                    required: true,
                    maxLength: 50,
                    pwRule: true,
                  })
                "
                :type="isShowPass ? 'text' : 'password'"
              >
                <template #append-inner>
                  <div
                    class="cursor-pointer"
                    @mousedown.prevent
                    @mouseup.prevent
                    @click="togglePass"
                  >
                    <template v-if="isShowPass"><eye-on /></template>
                    <template v-else><eye-off /></template>
                  </div>
                </template>
              </base-input-text>
            </div>
          </div>

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="userForm.pwVerify"
                :required="true"
                :label="
                  $t(
                    'product_platform.userEntity.createEdit.passwordVefification'
                  )
                "
                :styles="'input-form'"
                :rules="
                  useInputValidation({
                    required: true,
                    maxLength: 50,
                    pwRule: true,
                  })
                "
                :type="isShowPassVerify ? 'text' : 'password'"
              >
                <template #append-inner>
                  <div
                    class="cursor-pointer"
                    @mousedown.prevent
                    @mouseup.prevent
                    @click="togglePassVerify"
                  >
                    <template v-if="isShowPassVerify"><eye-on /></template>
                    <template v-else><eye-off /></template>
                  </div>
                </template>
              </base-input-text>
            </div>
          </div>

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-select
                v-model="userForm.userKdCd"
                :label="$t('product_platform.userEntity.table.userKdCdNm')"
                :required="true"
                :density="'comfortable'"
                :default-item-select-all="false"
                :items="userKdCdOptions"
                :item-title="'title'"
                :item-value="'value'"
                :rules="useInputValidation({ required: true })"
              />
            </div>
            <div class="flex-1">
              <base-select
                v-model="userForm.whofStatCd"
                :label="$t('product_platform.userEntity.table.whofStatNm')"
                :required="true"
                :density="'comfortable'"
                :default-item-select-all="false"
                :items="whofStatCdOptions"
                :item-title="'title'"
                :item-value="'value'"
                :rules="useInputValidation({ required: true })"
              />
            </div>
          </div>
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="userForm.orgNm"
                :label="
                  $t('product_platform.userEntity.createEdit.affiliation')
                "
                :styles="'input-form'"
                :required="true"
                :readonly="true"
              >
                <template #append-inner>
                  <div class="flex flex-row gap-1">
                    <BaseButton
                      :color="ButtonColorType.Gray"
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      @click="showModalSelectOrg"
                    >
                      <SearchIcon fill="#6B6D70" />
                    </BaseButton>

                    <BaseButton
                      :color="ButtonColorType.Gray"
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      @click="resetValuePopupOrg"
                    >
                      <delete-icon :fill="'#6B6D70'" />
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
  <OrgSearch
    v-if="openPopupOrg"
    v-model="openPopupOrg"
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

import { useSnackbarStore, useLoadingStore, useUserStore } from "@/store";
import { httpClient } from "@/utils/http-common";
import { useInputValidation } from "@/composables/useInputValidation";
import { CODE_TYPE } from "@/constants/admin/code";
import { useFormValidation } from "@/composables/useFormValidation";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import OrgSearch from "@/pages/admin/subs/org/OrgSearch.vue";
import useCmcdStore from "@/store/cmcd.store";
import {
  API_CREATE_EDIT_USER_PATH,
  API_CHECK_DUPLICATE_USER_PATH,
} from "@/api/admin/path";
import { FormRef } from "@/interfaces/admin/admin";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

const emit = defineEmits(["update:modelValue", "resetItemSelected"]);
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
  itemEdit: {
    type: Object,
    default: null,
  },
});

const formRef = ref<any>(null);
const { t } = useI18n();
const userStore = useUserStore();
const useSnackbar = useSnackbarStore();
const loadingStore = useLoadingStore();
const { search } = useCmcdStore();
const { validateForm } = useFormValidation(formRef as unknown as FormRef);

const title = computed(() => {
  return props.formType === FORM_TYPE_OPTION.CREATE
    ? t("product_platform.userEntity.title.userCreate")
    : t("product_platform.userEntity.title.userEdit");
});

const userForm = ref({
  userId: "",
  userNm: "",
  pw: "",
  pwVerify: "",
  userKdCd: "",
  userKdCdNm: "",
  whofStatCd: "",
  whofStatNm: "",
  orgCd: "",
  orgNm: "",
});

const isDuplicate = ref<boolean | null>(null);
const mesageDuplicate = ref("");
const openPopupOrg = ref(false);
const openPopupConfirm = ref(false);
const isShowPass = ref(false);
const isShowPassVerify = ref(false);
const userKdCdOptions = ref<any[]>([]);
const whofStatCdOptions = ref<any[]>([]);

const handleValidate = async () => {
  const firstErrorFieldId = await validateForm();
  if (userForm.value?.userId && props.formType === FORM_TYPE_OPTION.CREATE) {
    await checkDuplicate();
  }

  if (isDuplicate.value) {
    useSnackbar.showSnackbar(
      t("product_platform.userEntity.message.userDuplicate"),
      "error"
    );
    return;
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

const togglePass = () => {
  isShowPass.value = !isShowPass.value;
};

const togglePassVerify = () => {
  isShowPassVerify.value = !isShowPassVerify.value;
};

const handleSave = async () => {
  const { valid } = await formRef.value.validate();
  if (!valid) {
    return;
  }

  const isMappingPass = userForm.value?.pw === userForm.value?.pwVerify;
  if (!isMappingPass) {
    openPopupConfirm.value = false;
    useSnackbar.showSnackbar(
      t("product_platform.userEntity.message.pwNotMatch"),
      "error"
    );
    return;
  }

  try {
    loadingStore.setLoading(true);
    let response;

    const data = {
      userInfo: {
        userId: userForm.value?.userId,
        userNm: userForm.value?.userNm,
        pw: userForm.value?.pw,
        userKdCd: userForm.value?.userKdCd,
        whofStatCd: userForm.value?.whofStatCd,
        orgCd: userForm.value?.orgCd,
      },
    };

    if (props.formType === FORM_TYPE_OPTION.CREATE) {
      response = await httpClient.post(
        API_CREATE_EDIT_USER_PATH,
        data.userInfo
      );
    } else {
      response = await httpClient.put(API_CREATE_EDIT_USER_PATH, data.userInfo);
    }

    if (response.status === 200) {
      userStore.fetchUserManagement();
      useSnackbar.showSnackbar(
        t("product_platform.successfully_saved"),
        "success"
      );
      if (props.formType === FORM_TYPE_OPTION.UPDATE) {
        emit("resetItemSelected");
      }
      closeDialog();
    }
  } catch (error: any) {
    if (error?.errorCode === "duplicate") {
      mesageDuplicate.value = t(
        "product_platform.userEntity.message.userDuplicate"
      );
    } else {
      useSnackbar.showSnackbar(error?.errorMsg as string, "error");
    }
  } finally {
    openPopupConfirm.value = false;
    loadingStore.setLoading(false);
  }
};

const isOpen = computed({
  get() {
    return props.modelValue;
  },
  set(newValue) {
    emit("update:modelValue", newValue);
  },
});

const closeDialog = () => {
  isOpen.value = false;
};

const checkDuplicate = async () => {
  loadingStore.setLoading(true);

  try {
    const response = await httpClient.post(API_CHECK_DUPLICATE_USER_PATH, {
      userId: userForm?.value?.userId,
    });

    isDuplicate.value = response.data.data;
    if (isDuplicate.value) {
      mesageDuplicate.value = t(
        "product_platform.userEntity.message.userDuplicate"
      );
    } else {
      mesageDuplicate.value = "";
    }
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
  } finally {
    loadingStore.setLoading(false);
  }
};

const showModalSelectOrg = () => {
  openPopupOrg.value = true;
};

const onSelectItemOrg = (item) => {
  userForm.value = {
    ...userForm.value,
    orgCd: item.orgCd,
    orgNm: item.orgNm,
  };
};

const resetValuePopupOrg = () => {
  userForm.value = {
    ...userForm.value,
    orgCd: "",
    orgNm: "",
  };
};

const fetchUserKdCdOptions = async () => {
  let userTypeValue = await search(["USER_KD_CD"]);
  if (userTypeValue) {
    userKdCdOptions.value = [
      ...userTypeValue?.USER_KD_CD.map((item) => ({
        title: item.cmcdDetlNm,
        value: item.cmcdDetlId,
      })),
    ];
  }
};

const fetchWhofStatCdOptions = async () => {
  let userTypeValue = await search(["WHOF_STAT_CD"]);
  if (userTypeValue) {
    whofStatCdOptions.value = [
      ...userTypeValue?.WHOF_STAT_CD.map((item) => ({
        title: item.cmcdDetlNm,
        value: item.cmcdDetlId,
      })),
    ];
  }
};

onMounted(async () => {
  await fetchUserKdCdOptions();
  await fetchWhofStatCdOptions();
  if (props.formType === FORM_TYPE_OPTION.UPDATE) {
    userForm.value.userId = props.itemEdit?.userId;
    userForm.value.userNm = props.itemEdit?.userNm;
    userForm.value.pw = props.itemEdit?.pw;
    userForm.value.pwVerify = props.itemEdit?.pw;
    userForm.value.userKdCd = props.itemEdit?.userKdCd;
    userForm.value.userKdCdNm = props.itemEdit?.userKdCdNm;
    userForm.value.whofStatCd = props.itemEdit?.whofStatCd;
    userForm.value.whofStatNm = props.itemEdit?.whofStatNm;
    userForm.value.orgCd = props.itemEdit?.orgCd;
    userForm.value.orgNm = props.itemEdit?.orgNm;
  }
});
</script>

<style lang="scss" scoped></style>
