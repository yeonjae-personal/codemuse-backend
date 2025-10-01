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
                v-model="menuForm.menuId"
                :required="true"
                :label="$t('product_platform.menuEntity.menuId')"
                :rules="useInputValidation({ required: true, maxLength: 50 })"
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
                v-model="menuForm.menuNm"
                class=" "
                :required="true"
                :label="$t('product_platform.menuEntity.menuName')"
                :styles="'input-form'"
                :rules="useInputValidation({ required: true, maxLength: 100 })"
              >
              </base-input-text>
            </div>
          </div>

          <!-- -------------- start row 2-------------- -->
          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="menuForm.hposMenuNm"
                :required="checkValidateTop()"
                :label="$t('product_platform.menuEntity.topMenu')"
                :rules="
                  useInputValidation({
                    maxLength: 50,
                  })
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
                      :readonly="
                        props.formType === FORM_TYPE_OPTION.UPDATE &&
                        props.data?.hposMenuId === null
                      "
                      @click="showPopupMenu"
                    >
                      <search-icon size="20" fill="#6B6D70"></search-icon>
                    </BaseButton>
                    <BaseButton
                      :color="ButtonColorType.Gray"
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      :readonly="
                        props.formType === FORM_TYPE_OPTION.UPDATE &&
                        props.data?.hposMenuId === null
                      "
                      @click="resetValueMenu()"
                    >
                      <DeleteIcon></DeleteIcon>
                    </BaseButton>
                  </div>
                </template>
              </base-input-text>
            </div>
            <div class="flex-1">
              <base-input-text
                v-model="menuForm.scrnNm"
                class=" "
                :label="$t('product_platform.menuEntity.screen')"
                :styles="'input-form'"
                :readonly="true"
                :rules="useInputValidation({ maxLength: 50 })"
              >
                <template #append-inner>
                  <div class="flex justify-space-between gap-2">
                    <BaseButton
                      :color="ButtonColorType.Gray"
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      @click="showPopupScreen"
                    >
                      <search-icon size="20" fill="#6B6D70"></search-icon>
                    </BaseButton>
                    <BaseButton
                      :color="ButtonColorType.Gray"
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      @click="resetValueScreen()"
                    >
                      <DeleteIcon></DeleteIcon>
                    </BaseButton>
                  </div>
                </template>
              </base-input-text>
            </div>
          </div>
          <div class="flex gap-4 mb-3 w-[752px] h-[48px]">
            <div class="flex-1">
              <BaseButton :color="ButtonColorType.Gray" height="48" width="140">
                <span
                  class="text-sm font-medium text-text-lighter font-size-base"
                  >{{ $t("product_platform.menuEntity.enabled") }}</span
                >
                <template #append>
                  <v-switch
                    v-model="menuForm.actvYn"
                    hide-details
                    inset
                    class="custom-switch"
                    color="#D9325A"
                  ></v-switch>
                </template>
              </BaseButton>
              <BaseButton
                :color="ButtonColorType.Gray"
                height="48"
                width="200"
                class="ml-3"
              >
                <span
                  class="text-sm font-medium text-text-lighter font-size-base"
                  >{{
                    $t("product_platform.menuEntity.permissionControl")
                  }}</span
                >
                <template #append>
                  <v-switch
                    v-model="menuForm.authCtrlYn"
                    hide-details
                    inset
                    class="custom-switch"
                    color="#D9325A"
                  ></v-switch>
                </template>
              </BaseButton>
            </div>
          </div>

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="menuForm.menuDscr"
                class=" "
                :label="$t('product_platform.menuEntity.description')"
                :styles="'input-form'"
                :rules="useInputValidation({ maxLength: 4000 })"
              >
              </base-input-text>
            </div>
          </div>

          <div class="flex gap-2 mb-3 w-[592px] h-[48px]">
            <div class="w-[140px]">
              <base-input-text
                v-model="menuForm.sortOrd"
                :label="$t('product_platform.menuEntity.order')"
                :styles="'input-form'"
                :rules="useInputValidation({ maxLength: 15 })"
                :error-messages="mesageInvalidOrder"
                @keypress="onlyNumber"
              >
              </base-input-text>
            </div>
            <div class="flex-1">
              <base-input-text
                v-model="menuForm.authAprvUsrNm"
                :label="$t('product_platform.menuEntity.screenApprover')"
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
                      @click="showModalSelectApprover"
                    >
                      <SearchIcon fill="#6B6D70" />
                    </BaseButton>

                    <BaseButton
                      :color="ButtonColorType.Gray"
                      :width="WIDTH_BUTTON.FOR_INPUT"
                      :height="HEIGHT_BUTTON.FOR_INPUT"
                      @click="resetValueApprover()"
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
  <UserOrgPopup
    v-if="openPopupMenuApprover"
    v-model="openPopupMenuApprover"
    @selected-item="onSelectItemApprover"
  />
  <MenuSearch
    v-if="openPopupMenu"
    v-model="openPopupMenu"
    @selected-item="onSelectItemMenu"
  />
  <ScreenListPopup
    v-if="openPopupScreen"
    v-model="openPopupScreen"
    @selected-item="onSelectItemScreen"
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

import { useSnackbarStore, useLoadingStore } from "@/store";
import { httpClient } from "@/utils/http-common";
import { useInputValidation } from "@/composables/useInputValidation";
import { CODE_TYPE } from "@/constants/admin/code";
import { useFormValidation } from "@/composables/useFormValidation";
import MenuSearch from "@/pages/admin/subs/menu/MenuSearch.vue";
import ScreenListPopup from "@/pages/admin/subs/screen/ScreenListPopup.vue";
import { FORM_TYPE_OPTION, DuplicateCodeStatus } from "@/constants/admin/admin";
import { useMenuStoreInfo } from "@/store";
import {
  API_MENU_CREATE_EDIT_PATH,
  API_CHECK_DUPLICATE_MENU_PATH,
} from "@/api/admin/path";
import { FormRef } from "@/interfaces/admin/admin";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

const UserOrgPopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/user/UserOrgPopup.vue")
);

const emit = defineEmits(["update:modelValue", "reset-item-edit"]);
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
    type: Object,
    default: null,
  },
});

const formRef = ref<any>(null);
const { t } = useI18n();
const useSnackbar = useSnackbarStore();
const loadingStore = useLoadingStore();
const menuStoreInfo = useMenuStoreInfo();
const { validateForm } = useFormValidation(formRef as unknown as FormRef);

const title = computed(() => {
  return props.formType === FORM_TYPE_OPTION.CREATE
    ? t("product_platform.menuEntity.menuCreate")
    : t("product_platform.menuEntity.menuEdit");
});

const menuForm = ref({
  menuId: "",
  menuNm: "",
  scrnNm: "",
  actvYn: true,
  authCtrlYn: true,
  menuDscr: "",
  sortOrd: "",
  authAprvUsrNm: "",
  authAprvUsrId: "",
  hposMenuId: "",
  hposMenuLvNo: "",
  hposMenuNm: "",
  scrnId: "",
  parentId: "",
  parentLvNo: "",
  parentNm: "",
  menuLvNo: "",
  rgstUsrNm: "",
});

// domain Name
const isDuplicate = ref<boolean | null>(null);
const mesageDuplicate = ref("");
const mesageInvalidOrder = ref("");
const openPopupMenuApprover = ref(false);
const openPopupMenu = ref(false);
const openPopupScreen = ref(false);
const openPopupConfirm = ref(false);
const dataListUser = ref([]);

const handleValidate = async () => {
  const firstErrorFieldId = await validateForm();
  if (menuForm.value?.menuId && props.formType === FORM_TYPE_OPTION.CREATE) {
    await checkDuplicate();
  }

  if (isDuplicate.value) {
    useSnackbar.showSnackbar(
      t("product_platform.menuEntity.message.duplicate"),
      "error"
    );
    return;
  }
  if (menuForm.value?.sortOrd && !isValidOrder()) {
    useSnackbar.showSnackbar(
      t("product_platform.menuEntity.message.invalidOrder"),
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
const handleSave = async () => {
  loadingStore.setLoading(true);
  const { valid } = await formRef.value.validate();
  if (!valid) {
    return;
  }

  try {
    loadingStore.setLoading(true);
    let response;

    const data = {
      menuInfo: {
        menuId: menuForm.value?.menuId,
        menuNm: menuForm.value?.menuNm,
        hposMenuId: menuForm.value?.hposMenuId,
        hposMenuLvNo: Number(menuForm.value?.hposMenuLvNo),
        actvYn: menuForm.value?.actvYn ? "Y" : "N",
        authCtrlYn: menuForm.value?.authCtrlYn ? "Y" : "N",
        menuDscr: menuForm.value?.menuDscr,
        sortOrd: menuForm.value?.sortOrd ? Number(menuForm.value?.sortOrd) : 1,
        scrnId: menuForm.value?.scrnId,
        authAprvUsrId: menuForm.value?.authAprvUsrId,
      },
    };

    if (props.formType === FORM_TYPE_OPTION.CREATE) {
      response = await httpClient.post(
        API_MENU_CREATE_EDIT_PATH,
        data.menuInfo
      );
    } else {
      response = await httpClient.put(API_MENU_CREATE_EDIT_PATH, data.menuInfo);
    }

    if (response.status === 200) {
      useSnackbar.showSnackbar(
        t("product_platform.successfully_saved"),
        "success"
      );
      await menuStoreInfo.fetchMenuTree();
      if (props.formType === FORM_TYPE_OPTION.UPDATE) {
        emit("reset-item-edit");
      }
      closeDialog();
    }
  } catch (error: any) {
    if (error?.errorCode === "duplicate") {
      mesageDuplicate.value = t(
        "product_platform.menuEntity.message.duplicate"
      );
    } else {
      getMessageMenuError(error);
    }
  } finally {
    openPopupConfirm.value = false;
    loadingStore.setLoading(false);
  }
};

const getMessageMenuError = (error: any) => {
  switch (error?.errorCode) {
    case "MENU_001":
      useSnackbar.showSnackbar(
        t("product_platform.menuEntity.message.sameMenuId"),
        "error"
      );
      break;

    case "MENU_002":
      useSnackbar.showSnackbar(
        t("product_platform.menuEntity.message.unableChildMenu"),
        "error"
      );
      break;

    case "MENU_003":
      useSnackbar.showSnackbar(
        t("product_platform.menuEntity.message.valueMenuExisted"),
        "error"
      );
      break;

    default:
      useSnackbar.showSnackbar(error?.errorMsg as string, "error");
      break;
  }
};

const isValidOrder = () => {
  let isValid = true;
  if (Number(menuForm.value?.sortOrd) < 1) {
    isValid = false;
  }
  mesageInvalidOrder.value = isValid
    ? ""
    : t("product_platform.menuEntity.message.invalidOrder");
  return isValid;
};

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

const closeDialog = () => {
  isOpen.value = false;
};
const onlyNumber = ($event) => {
  let keyCode = $event.keyCode ? $event.keyCode : $event.which;
  if (keyCode < 48 || keyCode > 57) {
    $event.preventDefault();
  }
};

const checkValidateTop = () => {
  if (props.formType === FORM_TYPE_OPTION.UPDATE) {
    return props.data?.hposMenuId !== null;
  }
  return true;
};

const checkDuplicate = async () => {
  loadingStore.setLoading(true);

  try {
    const response = await httpClient.post(API_CHECK_DUPLICATE_MENU_PATH, {
      menuId: menuForm?.value?.menuId,
    });

    isDuplicate.value = response.data.data === DuplicateCodeStatus.FAIL;
    if (isDuplicate.value) {
      mesageDuplicate.value = t(
        "product_platform.menuEntity.message.duplicate"
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

const showModalSelectApprover = () => {
  openPopupMenuApprover.value = true;
};

const showPopupMenu = () => {
  openPopupMenu.value = true;
};

const showPopupScreen = () => {
  openPopupScreen.value = true;
};

const onSelectItemApprover = (item) => {
  menuForm.value = {
    ...menuForm.value,
    authAprvUsrId: item.userId,
    authAprvUsrNm: item.userNm,
  };
};

const resetValueApprover = () => {
  menuForm.value = {
    ...menuForm.value,
    authAprvUsrId: "",
    authAprvUsrNm: "",
  };
};

const onSelectItemMenu = (item) => {
  menuForm.value = {
    ...menuForm.value,
    hposMenuId: item.menuId,
    hposMenuNm: item.menuNm,
    hposMenuLvNo: item.menuLvNo,

    parentId: item.menuId,
    parentLvNo: item.menuLvNo,
    parentNm: item.menuNm,
  };
};

const resetValueMenu = () => {
  menuForm.value = {
    ...menuForm.value,
    hposMenuId: "",
    hposMenuLvNo: "",
    hposMenuNm: "",
    parentId: "",
    parentLvNo: "",
    parentNm: "",
  };
};

const onSelectItemScreen = (item) => {
  menuForm.value = {
    ...menuForm.value,
    scrnNm: item.scrnNm,
    scrnId: item.scrnId,
  };
};

const resetValueScreen = () => {
  menuForm.value = {
    ...menuForm.value,
    scrnNm: "",
    scrnId: "",
  };
};

const fetchDataUser = async () => {
  try {
    const response = await httpClient.get(`/api/comm/user/userInfo/v1/list`);

    if (response.data.data) {
      dataListUser.value = response.data.data;
    }
  } catch (error) {
    console.error("Error fetching data:", error);
  }
};

onMounted(async () => {
  await fetchDataUser();
  if (props.formType === FORM_TYPE_OPTION.UPDATE) {
    menuForm.value.menuId = props.data?.menuId;
    menuForm.value.menuNm = props.data?.menuNm;
    menuForm.value.hposMenuNm = props.data?.parentNm;
    menuForm.value.hposMenuId = props.data?.hposMenuId;
    menuForm.value.hposMenuLvNo = props.data?.parentLvNo;
    menuForm.value.actvYn = props.data?.actvYn;
    menuForm.value.authCtrlYn = props.data?.authCtrlYn;
    menuForm.value.menuDscr = props.data?.menuDscr;
    menuForm.value.sortOrd = props.data?.sortOrd;
    menuForm.value.authAprvUsrNm = props.data?.authAprvUsrNm;
    menuForm.value.authAprvUsrId = props.data?.authAprvUsrId;
    menuForm.value.scrnNm = props.data?.scrnNm;
    menuForm.value.scrnId = props.data?.scrnId;
    menuForm.value.parentId = props.data?.parentId;
    menuForm.value.parentLvNo = props.data?.parentLvNo;
    menuForm.value.parentNm = props.data?.parentNm;
    menuForm.value.rgstUsrNm = props.data?.rgstUsrNm;
    menuForm.value.menuLvNo = props.data?.menuLvNo;
  }
});
</script>

<style lang="scss" scoped>
:deep(.v-switch--inset .v-switch__thumb) {
  height: 16px;
  width: 16px;
  left: -1px !important;
}

:deep(.v-switch--inset .v-switch__track) {
  height: 20px;
  min-width: 36px;
}
:deep(.v-switch__track) {
  opacity: 1;
  background-color: rgb(220 224 228);
}
</style>
