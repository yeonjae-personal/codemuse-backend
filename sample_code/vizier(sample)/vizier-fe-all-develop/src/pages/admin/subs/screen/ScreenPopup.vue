<template>
  <BasePopup
    v-model="isOpen"
    :title="title"
    :size="DialogSizeType.Medium"
    :submit-button-text="t('product_platform.save')"
    :cancel-button-text="t('product_platform.cancel')"
  >
    <template #body>
      <div>
        <v-form ref="formRef">
          <div class="w-[640px] px-6 pt-3 gap-3">
            <!-- -------------- start row 1-------------- -->
            <div class="flex gap-2 mb-3 w-[752px] h-[48px]">
              <div class="flex-1">
                <base-input-text
                  v-model="screenForm.scrnId"
                  :required="true"
                  :label="$t('product_platform.screenEntity.screenId')"
                  :styles="'input-form'"
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
                :disabled="props.formType === FORM_TYPE_OPTION.UPDATE"
                @click="checkDuplicate"
              >
                {{ $t("product_platform.commonAdmin.duplicateCheck") }}
              </BaseButton>
            </div>

            <!-- -------------- start row 2-------------- -->
            <div class="flex gap-2 mb-3 w-[752px] h-[48px]">
              <div class="flex-1">
                <base-input-text
                  v-model="screenForm.scrnNm"
                  :required="true"
                  :styles="'input-form'"
                  :label="$t('product_platform.screenEntity.screenName')"
                  :counter="250"
                  :rules="
                    useInputValidation({ required: true, maxLength: 100 })
                  "
                >
                </base-input-text>
              </div>
            </div>

            <!-- -------------- start row 3-------------- -->
            <div class="flex gap-2 mb-3 w-[752px] h-[48px]">
              <div class="flex-1">
                <base-input-text
                  v-model="screenForm.scrnDscr"
                  :required="true"
                  :styles="'input-form'"
                  :label="$t('product_platform.screenEntity.screenDescription')"
                  :rules="
                    useInputValidation({ required: true, maxLength: 4000 })
                  "
                >
                </base-input-text>
              </div>
            </div>

            <!-- -------------- start row 4-------------- -->

            <div class="flex gap-4 mb-3 w-[752px] h-[48px]">
              <div class="flex-1">
                <BaseButton
                  :color="ButtonColorType.Gray"
                  :height="HEIGHT_BUTTON.DUPLICATE_CHECK"
                  :width="WIDTH_BUTTON.DUPLICATE_CHECK"
                >
                  <span
                    class="text-sm font-medium text-text-lighter font-size-base"
                    >{{ $t("product_platform.screenEntity.enabled") }}</span
                  >
                  <template #append>
                    <v-switch
                      v-model="screenForm.actvYn"
                      hide-details
                      inset
                      class="custom-switch"
                      color="#D9325A"
                    ></v-switch>
                  </template>
                </BaseButton>
                <BaseButton
                  :color="ButtonColorType.Gray"
                  :height="HEIGHT_BUTTON.DUPLICATE_CHECK"
                  :width="WIDTH_BUTTON.AUTO"
                  class="ml-3"
                >
                  <span
                    class="text-sm font-medium text-text-lighter font-size-base"
                    >{{
                      $t("product_platform.screenEntity.permissionControl")
                    }}</span
                  >
                  <template #append>
                    <v-switch
                      v-model="screenForm.authCtrlYn"
                      hide-details
                      inset
                      class="custom-switch"
                      color="#D9325A"
                    ></v-switch>
                  </template>
                </BaseButton>
              </div>
            </div>

            <!-- -------------- start row 5-------------- -->
            <div class="flex gap-2 mb-3 w-[752px] h-[48px]">
              <div class="flex-1">
                <base-input-text
                  v-model="screenForm.scrnLinkUrl"
                  :styles="'input-form'"
                  :label="$t('product_platform.screenEntity.screenLinkUrl')"
                  :counter="250"
                  :rules="useInputValidation({ maxLength: 500 })"
                >
                </base-input-text>
              </div>
            </div>

            <!-- -------------- start row 6-------------- -->
            <div class="flex gap-2 mb-3 w-[752px] h-[48px]">
              <div class="flex-1">
                <base-input-text
                  v-model="screenForm.scrnPathNm"
                  :required="true"
                  :styles="'input-form'"
                  :label="$t('product_platform.screenEntity.screenPath')"
                  :counter="250"
                  :rules="
                    useInputValidation({ required: true, maxLength: 500 })
                  "
                >
                </base-input-text>
              </div>
            </div>

            <!-- -------------- start row 7-------------- -->
            <div class="flex gap-2 mb-3 w-[752px] h-[48px]">
              <div class="flex-1">
                <base-input-text
                  v-model="screenForm.authAprvUsrNm"
                  :label="$t('product_platform.screenEntity.screenApprover')"
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
          <div class="flex justify-between items-center w-ful mx-6 pt-3">
            <div class="font-weight-medium font-base text-[15px]">
              <p>{{ $t("product_platform.screenEntity.url.listOfUrl") }}</p>
            </div>
            <div class="flex justify-space-between gap-3">
              <BaseButton
                :color="ButtonColorType.Gray"
                class="bg-light-blue-500 text-text-lighter"
                :height="HEIGHT_BUTTON.FOR_INPUT"
                :width="WIDTH_BUTTON.AUTO"
                @click="addRow"
              >
                <div class="mr-[6px]">
                  <v-icon>mdi-plus</v-icon>
                </div>
                <span
                  class="text-sm font-medium text-text-lighter font-size-base"
                  >{{ $t("product_platform.commonAdmin.addRow") }}</span
                >
              </BaseButton>
              <BaseButton
                :color="ButtonColorType.Gray"
                class="bg-light-blue-500 text-text-lighter"
                :height="HEIGHT_BUTTON.FOR_INPUT"
                :width="WIDTH_BUTTON.AUTO"
                @click="deleteRow"
              >
                <div class="mr-[6px]">
                  <delete-icon :fill="'#6B6D70'" />
                </div>
                <span
                  class="text-sm font-medium text-text-lighter font-size-base"
                  >{{ $t("product_platform.commonAdmin.deleteRow") }}</span
                >
              </BaseButton>
            </div>
          </div>

          <!-- table start here -->
          <div class="!max-h-[264px] mt-3 table-uri px-6">
            <TableAnalysis
              :headers="headerTable"
              :data="dataTable"
              :is-show-pagination="false"
            >
              <template #item="{ item }">
                <tr
                  :key="item.key"
                  :class="{
                    'selected-row': itemSelectedURI?.key === item.key,
                  }"
                  @click="clickDetailURI(item)"
                >
                  <td>
                    <SelectionIcon
                      size="18"
                      fill="#6B6D70"
                      :selected="itemSelectedURI?.key === item.key"
                    />
                  </td>
                  <td>
                    <base-input-text
                      v-model="item.urlAddr"
                      :required="true"
                      :styles="'input-form'"
                      :rules="
                        useInputValidation({ required: true, maxLength: 500 })
                      "
                      class="h-[32px] w-[106px]"
                      @change="
                        changeValueField(item.key, 'urlAddr', item.urlAddr)
                      "
                    >
                    </base-input-text>
                  </td>
                  <td>
                    <base-select
                      v-model="item.httpMthoCd"
                      :items="httpTypes"
                      :item-title="'title'"
                      :required="true"
                      :item-value="'value'"
                      class="h-[32px] w-[100px]"
                      :default-item-select-all="false"
                      @handle-change-input="
                        changeValueField(
                          item.key,
                          'httpMthoCd',
                          item.httpMthoCd
                        )
                      "
                    />
                  </td>
                  <td>
                    <base-input-text
                      v-model="item.urlNm"
                      :required="true"
                      :styles="'input-form'"
                      :rules="
                        useInputValidation({ required: true, maxLength: 500 })
                      "
                      class="h-[32px] w-[106px]"
                      @change="changeValueField(item.key, 'urlNm', item.urlNm)"
                    >
                    </base-input-text>
                  </td>
                  <td>
                    <base-select
                      v-model="item.authCtrlYn"
                      :items="permissionControlOptions"
                      :item-title="'title'"
                      :required="true"
                      :item-value="'value'"
                      class="h-[32px] w-[100px]"
                      :default-item-select-all="false"
                      @handle-change-input="
                        changeValueField(
                          item.key,
                          'authCtrlYn',
                          item.authCtrlYn
                        )
                      "
                    />
                  </td>
                  <td>
                    <base-input-text
                      v-model="item.urlDscr"
                      :styles="'input-form'"
                      :rules="useInputValidation({ maxLength: 4000 })"
                      class="h-[32px] w-[106px]"
                      @change="
                        changeValueField(item.key, 'urlDscr', item.urlDscr)
                      "
                    >
                    </base-input-text>
                  </td>
                </tr>
              </template>
            </TableAnalysis>
          </div>
        </v-form>
      </div>
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
    v-if="openPopupScreenApprover"
    v-model="openPopupScreenApprover"
    @selected-item="onSelectItemApprover"
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
import { useInputValidation } from "@/composables/useInputValidation";
import { FORM_TYPE_OPTION, DuplicateCodeStatus } from "@/constants/admin/admin";
import { httpClient } from "@/utils/http-common";
import { useFormValidation } from "@/composables/useFormValidation";
import useCmcdStore from "@/store/cmcd.store";
import { useScreenStore } from "@/store";
import { FormRef } from "@/interfaces/admin/admin";
import TableAnalysis from "@/pages/admin/subs/TableAnalysis.vue";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

const UserOrgPopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/user/UserOrgPopup.vue")
);

const emit = defineEmits(["update:modelValue", "resetItemSelected"]);

const props = defineProps({
  formType: {
    type: String,
    default: FORM_TYPE_OPTION.CREATE,
  },
  modelValue: {
    type: Boolean,
    default: false,
  },
  itemEdit: {
    type: Object as PropType<any>,
    default: null,
  },
});

const formRef = ref<any>(null);
const { t } = useI18n();
const useSnackbar = useSnackbarStore();
const loadingStore = useLoadingStore();
const screenStore = useScreenStore();
const { search } = useCmcdStore();

const { validateForm } = useFormValidation(formRef as unknown as FormRef);

// state form
const itemSelectedURI = ref<any>(null);
const openPopupConfirm = ref(false);
const screenForm = ref({
  scrnId: "",
  scrnNm: "",
  scrnDscr: "",
  actvYn: true,
  authCtrlYn: true,
  scrnLinkUrl: "",
  scrnPathNm: "",
  authAprvUsrId: "",
  authAprvUsrNm: "",
});

const isDuplicate = ref<boolean | null>(null);
const isLoadingButton = ref(false);
const openPopupScreenApprover = ref(false);
const mesageDuplicate = ref("");
const dataListUser = ref<any[]>([]);
const httpTypes = ref<any[]>([]);
const permissionControlOptions = ref([
  { title: "Y", value: "Y" },
  { title: "N", value: "N" },
]);

// state table
const headerTable = computed(() => {
  return [
    {
      title: t("product_platform.commonAdmin.select"),
      align: "start",
      sortable: false,
      key: "urlNo",
      class: "header",
    },
    {
      title: `*${t("product_platform.screenEntity.url.url")}`,
      align: "start",
      sortable: false,
      key: "urlAddr",
      class: "header",
    },
    {
      title: `*${t("product_platform.screenEntity.url.method")}`,
      align: "start",
      sortable: false,
      key: "httpMthoCd",
      class: "header",
    },
    {
      title: `*${t("product_platform.screenEntity.url.urlName")}`,
      align: "start",
      sortable: false,
      key: "urlNm",
      class: "header",
    },
    {
      title: `*${t("product_platform.screenEntity.permissionControl")}`,
      align: "start",
      sortable: false,
      key: "authCtrlYn",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.url.urlDescription"),
      align: "start",
      sortable: false,
      key: "urlDscr",
      class: "header",
    },
  ];
});
const dataTable = ref<any[]>([]);

const title = computed(() => {
  return props.formType === FORM_TYPE_OPTION.CREATE
    ? t("product_platform.screenEntity.screenCreate")
    : t("product_platform.screenEntity.screenEdit");
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

// method

const handleValidate = async () => {
  const firstErrorFieldId = await validateForm();
  if (screenForm.value?.scrnId && props.formType === FORM_TYPE_OPTION.CREATE) {
    await checkDuplicate();
  }
  if (isDuplicate.value) {
    useSnackbar.showSnackbar(
      t("product_platform.screenEntity.message.duplicate"),
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

const changeValueField = (key, keyValue, value) => {
  dataTable.value = dataTable.value.map((i) => {
    if (i.key === key) {
      return {
        ...i,
        [keyValue]: value,
      };
    }

    return i;
  });
};

const showModalSelectApprover = () => {
  openPopupScreenApprover.value = true;
};

const clickDetailURI = (item: any) => {
  itemSelectedURI.value = item;
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

const resetValueApprover = () => {
  screenForm.value = {
    ...screenForm.value,
    authAprvUsrId: "",
    authAprvUsrNm: "",
  };
};

const onSelectItemApprover = (item) => {
  screenForm.value = {
    ...screenForm.value,
    authAprvUsrId: item.userId,
    authAprvUsrNm: item.userNm,
  };
};

const handleSave = async () => {
  loadingStore.setLoading(true);
  const { valid } = await formRef.value.validate();
  if (!valid) {
    return;
  }

  try {
    let response;

    if (props.formType === FORM_TYPE_OPTION.CREATE) {
      const data = {
        scrnInfo: {
          ...screenForm.value,
          actvYn: screenForm.value?.actvYn ? "Y" : "N",
          authCtrlYn: screenForm.value?.authCtrlYn ? "Y" : "N",
          authAprvUsrId: screenForm.value?.authAprvUsrId,
        },
        urlInfo: dataTable.value,
      };

      response = await httpClient.post(`/api/comm/user/scrnInfo/v1`, data);
    } else {
      const data = {
        scrnInfo: {
          ...screenForm.value,
          actvYn: screenForm.value?.actvYn ? "Y" : "N",
          authCtrlYn: screenForm.value?.authCtrlYn ? "Y" : "N",
          authAprvUsrId: screenForm.value?.authAprvUsrId,
        },
        urlInfo: dataTable.value,
      };
      response = await httpClient.put(`/api/comm/user/scrnInfo/v1`, data);
    }
    if (response.status === 200) {
      useSnackbar.showSnackbar(
        t("product_platform.successfully_saved"),
        "success"
      );
      emit("resetItemSelected");
      await screenStore.fetchScreenManagement();
      closeDialog();
    }
  } catch (error: any) {
    if (error?.errorCode === "duplicate") {
      useSnackbar.showSnackbar(
        t("product_platform.screenEntity.message.duplicate") as string,
        "error"
      );
    } else {
      useSnackbar.showSnackbar(error?.errorMsg as string, "error");
    }
    emit("resetItemSelected", props.itemEdit);
  } finally {
    openPopupConfirm.value = false;
    loadingStore.setLoading(false);
  }
};

const addRow = () => {
  dataTable.value = [
    ...dataTable.value,
    {
      key: dataTable.value.length + 1,
      urlAddr: "",
      httpMthoCd: "GET",
      urlNm: "",
      authCtrlYn: "Y",
      urlDscr: "",
    },
  ];
  dataTable.value = dataTable.value.map((i, index) => ({
    ...i,
    key: index + 1,
  }));
  itemSelectedURI.value = null;
};

const deleteRow = () => {
  if (itemSelectedURI.value) {
    dataTable.value = dataTable.value.filter(
      (i) => i.key !== itemSelectedURI.value?.key
    );
    dataTable.value = dataTable.value.map((i, index) => ({
      ...i,
      key: index + 1,
    }));
    itemSelectedURI.value = null;
  } else {
    useSnackbar.showSnackbar(
      t("product_platform.commonAdmin.plsSelectRow"),
      "error"
    );
  }
};

const closeDialog = () => {
  isOpen.value = false;
};

const checkDuplicate = async () => {
  isLoadingButton.value = true;

  try {
    const response = await httpClient.post(
      `/api/comm/user/scrnInfo/checkDuplicate/v1`,
      {
        scrnId: screenForm.value?.scrnId,
      }
    );

    isDuplicate.value = response.data === DuplicateCodeStatus.FAIL;
    if (isDuplicate.value) {
      mesageDuplicate.value = t(
        "product_platform.screenEntity.message.duplicate"
      );
    } else {
      mesageDuplicate.value = "";
    }
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
  } finally {
    isLoadingButton.value = false;
  }
};

const fetchDataUrlByScreenId = async (screenId) => {
  try {
    const response = await httpClient.get(
      `/api/comm/scrn/urlInfo/list/v1/${screenId}`
    );

    if (response.data) {
      response.data.forEach((i, index) => {
        dataTable.value.push({
          key: index + 1,
          urlAddr: i.urlAddr,
          httpMthoCd: i.httpMthoCd,
          urlNm: i.urlNm,
          authCtrlYn: i.authCtrlYn,
          urlDscr: i.urlDscr,
          scrbUrlId: i.scrbUrlId,
        });
      });
    }
  } catch (error) {
    console.error("Error fetching data:", error);
  }
};

// lifecycle
onMounted(async () => {
  await fetchDataUser();
  // Get list KDCD (Permission type)
  let httpArr = await search(["HTTP_MTHO_CD"]);
  if (httpArr) {
    httpTypes.value = [
      ...httpArr?.HTTP_MTHO_CD.map((i) => ({
        title: i.cmcdDetlNm,
        value: i.cmcdDetlId,
      })),
    ];
  }
  if (props.formType === FORM_TYPE_OPTION.UPDATE) {
    await fetchDataUrlByScreenId(props.itemEdit?.scrnId);
    screenForm.value.scrnId = props.itemEdit?.scrnId;
    screenForm.value.scrnNm = props.itemEdit?.scrnNm;
    screenForm.value.scrnDscr = props.itemEdit?.scrnDscr;
    screenForm.value.actvYn = props.itemEdit?.actvYn === "Y";
    screenForm.value.authCtrlYn = props.itemEdit?.authCtrlYn === "Y";
    screenForm.value.scrnLinkUrl = props.itemEdit?.scrnLinkUrl;
    screenForm.value.scrnPathNm = props.itemEdit?.scrnPathNm;
    screenForm.value.authAprvUsrId = props.itemEdit?.authAprvUsrId;
    screenForm.value.authAprvUsrNm = props.itemEdit?.authAprvUsrNm;
  } else {
    dataTable.value.push({
      key: 1,
      urlAddr: "",
      httpMthoCd: "GET",
      urlNm: "",
      authCtrlYn: "Y",
      urlDscr: "",
    });
  }
});
</script>

<style lang="scss" scoped>
:deep(.custom-table) {
  white-space: normal;
}

/** custom switch button **/
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
:deep(.v-data-table-header__content) {
  font-family: Noto Sans KR;
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
}

:deep(.custom-table) {
  white-space: normal;

  .v-field__append-inner {
    margin-left: 20px;
  }
}
:deep(td .v-input__control) {
  height: 30px !important;
  .v-field {
    height: 30px !important;
  }

  .v-field__field {
    height: 30px !important;
    padding: unset !important;
    min-width: 50px !important;
  }
  .v-input__details {
    z-index: 999 !important;
  }
}
:deep(table) {
  .v-select {
    .v-select__selection {
      margin-inline-start: 10px !important;
    }

    .v-input__details {
      bottom: 35px !important;
    }
  }
  .custom-text-field {
    .v-field__input {
      padding-left: 10px !important;
      width: 85px;
    }
  }
  .v-field__input {
    height: 30px !important;
    padding: 0px !important;
    font-size: 13px !important;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    min-height: unset !important;
  }

  .v-input__details {
    z-index: 999 !important;
  }
}
:deep(.table-uri) {
  .v-table {
    max-height: 264px !important;
  }
  .v-table__wrapper {
    border: solid 1px rgba(230, 233, 237, 1) !important;
    border-radius: 8px !important;
  }
}
</style>
