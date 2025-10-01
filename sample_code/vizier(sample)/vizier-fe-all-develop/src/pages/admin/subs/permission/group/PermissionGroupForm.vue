<template>
  <BasePopup v-model="isOpen" :title="title" :size="DialogSizeType.Medium">
    <template #body>
      <div class="w-[800px] pt-2 gap-2">
        <v-form ref="formRef">
          <!-- -------------- start row 1-------------- -->
          <div class="flex gap-3 mb-3 px-6 w-[752px] h-[48px]">
            <div class="flex-1">
              <base-select
                v-model="permissionForm.authGrpLcls"
                :label="
                  $t(
                    'product_platform.permissionEntity.group.permissionGroupCategory'
                  )
                "
                :density="'comfortable'"
                :items="permGrpCtgOptions"
                :required="true"
                :item-title="'title'"
                :item-value="'value'"
                :disabled="props.formType === FORM_TYPE_OPTION.UPDATE"
                :class="`border border-[#E5E7EB]  h-[48px] w-[370px] ${
                  props.formType === FORM_TYPE_OPTION.UPDATE
                    ? 'base-select-disabled'
                    : 'base-select'
                }`"
              />
            </div>
            <div class="flex-1">
              <base-select
                v-model="permissionForm.authGrpScls"
                :label="
                  $t(
                    'product_platform.permissionEntity.group.permissionGroupSubdivision'
                  )
                "
                :required="true"
                :density="'comfortable'"
                :items="permGrpSubdvOptions"
                :item-title="'title'"
                :item-value="'value'"
                :disabled="props.formType === FORM_TYPE_OPTION.UPDATE"
                :class="`border border-[#E5E7EB]  h-[48px] w-[370px] ${
                  props.formType === FORM_TYPE_OPTION.UPDATE
                    ? 'base-select-disabled'
                    : 'base-select'
                }`"
              />
            </div>
          </div>
          <div class="flex gap-2 mb-3 px-6 w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="permissionGroupName"
                :required="true"
                :readonly="true"
                :styles="'input-form'"
                :label="
                  $t(
                    'product_platform.permissionEntity.group.permissionGroupName'
                  )
                "
                class="h-[48px] w-[752px]"
              >
              </base-input-text>
            </div>
          </div>
          <div class="flex gap-4 mb-6 px-6 w-[592px] h-[48px]">
            <div class="flex-1">
              <BaseButton :color="ButtonColorType.Gray" height="48" width="140">
                <span
                  class="text-sm font-medium text-text-lighter font-size-base"
                  >{{ $t("product_platform.permissionEntity.enabled") }}</span
                >
                <template #append>
                  <v-switch
                    v-model="permissionForm.actvYn"
                    hide-details
                    inset
                    class="custom-switch"
                    color="#D9325A"
                  ></v-switch>
                </template>
              </BaseButton>
            </div>
          </div>

          <!-- -------------- start row 2-------------- -->
          <div class="flex justify-between items-center w-ful px-6">
            <div class="font-weight-medium font-base text-[15px]">
              <p>
                {{
                  $t(
                    "product_platform.permissionEntity.group.listOfAffiliatedUsers"
                  )
                }}
              </p>
            </div>
            <div class="flex justify-space-between gap-2">
              <BaseButton
                :color="ButtonColorType.Gray"
                class="bg-light-blue-500 text-text-lighter"
                :width="WIDTH_BUTTON.AUTO"
                :height="HEIGHT_BUTTON.FOR_INPUT"
                @click="addRow"
              >
                <div class="mr-[4px]">
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
                :width="WIDTH_BUTTON.AUTO"
                :height="HEIGHT_BUTTON.FOR_INPUT"
                :readonly="dataTable.length === 0"
                @click="deleteRow"
              >
                <div class="mr-[4px]">
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
          <div class="!max-h-[264px] mt-3 px-6 table-user-list">
            <TableAnalysis
              :headers="headerTable"
              :data="dataTable"
              :is-show-pagination="false"
              :class="'!max-h-[264px]'"
            >
              <template #item="{ item }">
                <tr
                  :key="item.key"
                  :class="{
                    'selected-row': itemSelectedUser?.key === item.key,
                  }"
                  @click="clickDetailUser(item)"
                >
                  <td>
                    <SelectionIcon
                      size="18"
                      fill="#6B6D70"
                      :selected="itemSelectedUser?.key === item.key"
                    />
                  </td>
                  <td>
                    <p1>{{ item.userId || "-" }}</p1>
                  </td>
                  <td>
                    <base-input-text
                      v-model="item.username"
                      :placeholder="
                        $t('product_platform.permissionEntity.group.username')
                      "
                      :styles="'input-form'"
                      :readonly="true"
                      :required="true"
                      class="min-h-[32px] max-w-[200px] w-[125px]"
                    >
                      <template #append-inner>
                        <div class="flex flex-row gap-1">
                          <SearchIcon
                            class="cursor-pointer"
                            fill="#6B6D70"
                            @click.stop="showModalSelectUser(item.key)"
                          />
                        </div>
                      </template>
                    </base-input-text>
                  </td>
                  <td>
                    <p1>{{ item.userType || "-" }}</p1>
                  </td>
                  <td>
                    <p1> - </p1>
                  </td>
                  <td>
                    <p1>{{ item.organizationCode || "-" }}</p1>
                  </td>
                  <td>
                    <p1>{{ item.organizationName || "-" }}</p1>
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
    v-if="openPopupOrgUser"
    v-model="openPopupOrgUser"
    @selected-item="onSelectItemUser"
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

<!-- eslint-disable id-length -->
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import {
  ButtonColorType,
  ButtonSizeType,
  DialogSizeType,
  DialogIconType,
} from "@/enums";

import {
  useSnackbarStore,
  useLoadingStore,
  usePermissionGroupStore,
} from "@/store";
import useCmcdStore from "@/store/cmcd.store";
import TableAnalysis from "@/pages/admin/subs/TableAnalysis.vue";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import { useFormValidation } from "@/composables/useFormValidation";
import { httpClient } from "@/utils/http-common";
import { FormRef } from "@/interfaces/admin/admin";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

const UserOrgPopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/user/UserOrgPopup.vue")
);

const emit = defineEmits(["update:modelValue", "resetItemSelected"]);
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  itemEdit: {
    type: Object as PropType<any>,
    default: null,
  },
  formType: {
    type: String,
    default: FORM_TYPE_OPTION.CREATE,
  },
});

const formRef = ref<any>(null);
const { validateForm } = useFormValidation(formRef as unknown as FormRef);
const permGrpCtgOptions = ref<any[]>([]);
const permGrpSubdvOptions = ref<any[]>([]);
const openPopupOrgUser = ref(false);
const itemSelectedUser = ref<any>(null);
const itemChooseUser = ref(null);
const openPopupConfirm = ref(false);
const dataListUser = ref<any[]>([]);
const dataTable = ref<any[]>([]);
const permissionForm = ref({
  authGrpLcls: "",
  authGrpScls: "",
  authGrpNm: "",
  actvYn: true,
});

const { t } = useI18n();
const loadingStore = useLoadingStore();
const useSnackbar = useSnackbarStore();
const { search } = useCmcdStore();
const permGrpStore = usePermissionGroupStore();

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
    ? t("product_platform.permissionEntity.group.permissionGroupCreate")
    : t("product_platform.permissionEntity.group.permissionGroupEdit");
});

const headerTable = computed(() => {
  return [
    {
      title: t("product_platform.commonAdmin.select"),
      align: "start",
      sortable: false,
      key: "permissionNo",
      class: "header",
    },
    {
      title: `*${t("product_platform.permissionEntity.group.userID")}`,
      align: "start",
      sortable: false,
      key: "userId",
      class: "header",
    },
    {
      title: `*${t("product_platform.permissionEntity.group.username")}`,
      align: "start",
      sortable: false,
      key: "username",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.group.userType"),
      align: "start",
      sortable: false,
      key: "userType",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.group.orgCode"),
      align: "start",
      sortable: false,
      key: "organizationCode",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.group.orgName"),
      align: "start",
      sortable: false,
      key: "organizationName",
      class: "header",
    },
  ];
});

const permissionGroupName = computed(() => {
  const { authGrpLcls, authGrpScls } = permissionForm.value;
  const category = permGrpCtgOptions.value.find(
    (item) => item.value === authGrpLcls
  )?.title;
  const subdivision = permGrpSubdvOptions.value.find(
    (item) => item.value === authGrpScls
  )?.title;
  return authGrpLcls && authGrpScls ? `${category}-${subdivision}` : "";
});

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

const fetchDataUserByGroupId = async (groupId) => {
  try {
    const response = await httpClient.get(
      `/api/comm/authGrp/authGrpInfo/user/v1?`,
      {
        params: {
          authGrpId: groupId,
        },
      }
    );

    if (response.data.data) {
      response.data.data.forEach((x, index) => {
        const dataUser = dataListUser?.value?.find(
          (y) => y.userId === x.userId
        );
        if (dataUser) {
          dataTable.value.push({
            key: index + 1,
            userId: dataUser.userId,
            username: dataUser.userNm,
            userType: dataUser.userKdCdNm,
            organizationCode: dataUser.orgCd,
            organizationName: dataUser.orgNm,
            authGrbyUserRelId: x.authGrbyUserRelId,
          });
        }
      });
    }
  } catch (error) {
    console.error("Error fetching data:", error);
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

const clickDetailUser = (item: any) => {
  itemSelectedUser.value = item;
};

const showModalSelectUser = (key) => {
  itemChooseUser.value = key;
  openPopupOrgUser.value = true;
};

const addRow = () => {
  dataTable.value = [
    ...dataTable.value,
    {
      key: dataTable.value.length + 1,
      userId: "",
      username: "",
      userType: "",
      organizationCode: "",
      organizationName: "",
    },
  ];
  dataTable.value = dataTable.value.map((x, index) => ({
    ...x,
    key: index + 1,
  }));
  itemSelectedUser.value = null;
};

const deleteRow = () => {
  if (itemSelectedUser.value) {
    dataTable.value = dataTable.value.filter(
      (x) => x.key !== itemSelectedUser.value?.key
    );
    dataTable.value = dataTable.value.map((x, index) => ({
      ...x,
      key: index + 1,
    }));
    itemSelectedUser.value = null;
  } else {
    useSnackbar.showSnackbar(
      t("product_platform.commonAdmin.plsSelectRow"),
      "error"
    );
  }
};

const getPermCtgValue = async () => {
  let permCtgValue = await search(["AUTH_GRP_LCLS"]);
  if (permCtgValue) {
    permGrpCtgOptions.value = [
      ...permCtgValue?.AUTH_GRP_LCLS.map((x) => ({
        title: x.cmcdDetlNm,
        value: x.cmcdDetlId,
      })),
    ];
  }
};

const getPermGrpSubdvValue = async () => {
  let permSubdvValue = await search(["AUTH_GRP_SCLS"]);
  if (permSubdvValue) {
    permGrpSubdvOptions.value = [
      ...permSubdvValue?.AUTH_GRP_SCLS.map((x) => ({
        title: x.cmcdDetlNm,
        value: x.cmcdDetlId,
      })),
    ];
  }
};

const onSelectItemUser = (item) => {
  if (Object.keys(item).length) {
    let checkExisted = dataTable.value.some((x) => x.userId === item.userId);
    if (!checkExisted) {
      dataTable.value = dataTable.value.map((x) => {
        if (x.key === itemChooseUser.value) {
          return {
            ...x,
            userId: item.userId,
            username: item.userNm,
            userType: item.userKdCdNm,
            organizationCode: item.orgCd,
            organizationName: item.orgNm,
          };
        }

        return x;
      });
    } else {
      useSnackbar.showSnackbar(
        t("product_platform.commonAdmin.itemExists"),
        "error"
      );
    }
  }
};

const closeDialog = () => {
  isOpen.value = false;
};

const handleSave = async () => {
  loadingStore.setLoading(true);
  const { valid } = await formRef.value.validate();
  if (!valid) {
    return;
  }

  let dataUsers = dataTable.value.map((x) => ({
    userId: x.userId,
    authGrbyUserRelId: x.authGrbyUserRelId,
  }));

  try {
    let response;
    let categoryName;
    let subdivitionName;
    if (permissionForm.value.authGrpLcls && permissionForm.value.authGrpScls) {
      categoryName = permGrpCtgOptions.value.find(
        (x) => x.value === permissionForm.value.authGrpLcls
      )?.title;
      subdivitionName = permGrpSubdvOptions.value.find(
        (x) => x.value === permissionForm.value.authGrpScls
      )?.title;
    }

    if (props.formType === FORM_TYPE_OPTION.CREATE) {
      const data = {
        authGrpInfo: {
          authGrpId: "",
          authGrpLcls: permissionForm.value?.authGrpLcls,
          authGrpScls: permissionForm.value?.authGrpScls,
          authGrpNm: `${categoryName}-${subdivitionName}`,
          actvYn: permissionForm.value?.actvYn ? "Y" : "N",
        },
        userInfor: dataUsers,
      };

      response = await httpClient.post(
        `/api/comm/authGrp/authGrpInfo/v1`,
        data
      );
    } else {
      const data = {
        authGrpInfo: {
          authGrpId: props.itemEdit?.authGrpId,
          authGrpLcls: permissionForm.value?.authGrpLcls,
          authGrpScls: permissionForm.value?.authGrpScls,
          authGrpNm: `${categoryName}-${subdivitionName}`,
          actvYn: permissionForm.value?.actvYn ? "Y" : "N",
        },
        userInfor: dataUsers,
      };
      response = await httpClient.put(`/api/comm/authGrp/authGrpInfo/v1`, data);
    }
    if (response.status === 200) {
      useSnackbar.showSnackbar(
        t("product_platform.successfully_saved"),
        "success"
      );
      emit("resetItemSelected");
      await permGrpStore.fetchPermissionGroup();
      closeDialog();
    }
  } catch (error: any) {
    if (error?.errorCode === "PERMISSION_GRP_USERID_EXISTS") {
      const userId = error?.data?.userId;
      const authGrpNm = error?.data?.authGrpNm;
      useSnackbar.showSnackbar(
        t("product_platform.permissionEntity.message.userIdExistsAuthGrpNm")
          .replace("[userId]", userId)
          .replace("[groupPermission]", authGrpNm),
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

onMounted(async () => {
  getPermCtgValue();
  getPermGrpSubdvValue();
  if (props.formType === FORM_TYPE_OPTION.UPDATE) {
    await fetchDataUser();
    await fetchDataUserByGroupId(props.itemEdit?.authGrpId);
    permissionForm.value.authGrpLcls = props.itemEdit?.authGrpLcls;
    permissionForm.value.authGrpScls = props.itemEdit?.authGrpScls;
    permissionForm.value.actvYn = props.itemEdit?.actvYn === "Y";
  } else {
    dataTable.value.push({
      key: 1,
      userId: "",
      username: "",
      userType: "",
      organizationCode: "",
      organizationName: "",
    });
  }
});
</script>

<style lang="scss" scoped>
:deep(.v-data-table-header__content) {
  font-family: Noto Sans KR;
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
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
:deep(.base-select.v-input--disabled) {
  background-color: #f0f2f5 !important;
}
:deep(.custom-table) {
  white-space: normal;

  .v-field__input {
    width: 85px !important;
  }

  .v-field__field {
    border-right: solid 1px rgba(220, 224, 229, 1);
  }

  .v-field__append-inner {
    margin-left: 5px;
  }
}
:deep(td .v-input__control) {
  height: 32px !important;

  .v-field__field {
    height: 32px !important;
    padding: unset !important;
  }
  .v-input__details {
    z-index: 999 !important;
  }
}
:deep(table) {
  .v-field__input {
    height: 32px !important;
    padding: 0px 8px !important;
    font-size: 13px !important;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .v-input__details {
    z-index: 999 !important;
  }
}

:deep(.table-user-list) {
  .v-table {
    max-height: 264px !important;
  }
  .v-table__wrapper {
    border: solid 1px rgba(230, 233, 237, 1) !important;
    border-radius: 8px !important;
  }
}
</style>
