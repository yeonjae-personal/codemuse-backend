<template>
  <BasePopup
    v-model="isOpen"
    :title="$t('product_platform.permissionEntity.group.permissionGroupSearch')"
    :size="DialogSizeType.Medium"
  >
    <template #body>
      <div class="w-[800px] pt-2 gap-2">
        <v-form ref="formRef">
          <div class="flex gap-2 mb-6 px-6 w-ful h-[48px]">
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
              >
              </base-input-text>
            </div>
          </div>

          <div class="flex justify-between items-center w-full px-6">
            <div class="font-weight-medium font-base text-[15px]">
              <p>
                {{
                  $t(
                    "product_platform.permissionEntity.group.listOfPermissions"
                  )
                }}
              </p>
            </div>
            <div class="flex justify-space-between gap-2">
              <BaseButton
                :color="ButtonColorType.Gray"
                :width="WIDTH_BUTTON.AUTO"
                @click="addRow"
              >
                <v-icon class="mr-[6px]">mdi-plus</v-icon>
                {{ $t("product_platform.commonAdmin.addRow") }}
              </BaseButton>
              <BaseButton
                :color="ButtonColorType.Gray"
                :width="WIDTH_BUTTON.AUTO"
                :readonly="dataTable.length === 0"
                @click="deleteRow"
              >
                <delete-icon :fill="'#6B6D70'" class="mr-[6px]" />
                {{ $t("product_platform.commonAdmin.deleteRow") }}
              </BaseButton>
            </div>
          </div>

          <!-- table start here -->
          <div class="!max-h-[264px] mt-3 table-user-list px-6">
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
                    'selected-row': itemSelectedPermission?.key === item.key,
                  }"
                  @click="clickDetailPermission(item)"
                >
                  <td>
                    <SelectionIcon
                      size="18"
                      fill="#6B6D70"
                      :selected="itemSelectedPermission?.key === item.key"
                    />
                  </td>
                  <td>
                    <p1>{{ item.permissionCode || "-" }}</p1>
                  </td>
                  <td>
                    <base-input-text
                      v-model="item.permissionName"
                      :placeholder="
                        $t(
                          'product_platform.permissionEntity.group.permissionName'
                        )
                      "
                      :styles="'input-form'"
                      :readonly="true"
                      :required="true"
                      class="min-h-[32px] max-w-[150px] w-[150px]"
                    >
                      <template #append-inner>
                        <div class="flex flex-row gap-1">
                          <SearchIcon
                            class="cursor-pointer"
                            fill="#6B6D70"
                            @click.stop="showModalSelectPermission(item.key)"
                          />
                        </div>
                      </template>
                    </base-input-text>
                  </td>
                  <td>
                    <p1>{{ item.permissionType || "-" }}</p1>
                  </td>
                  <td>
                    <p1>{{ item.description || "-" }}</p1>
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
  <PermissionListPopup
    v-if="openPopupPermissionList"
    v-model="openPopupPermissionList"
    @selected-item="onSelectItemPermission"
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
import { ButtonColorType, DialogSizeType, DialogIconType } from "@/enums";

import {
  useSnackbarStore,
  useLoadingStore,
  usePermissionGroupStore,
} from "@/store";
import TableAnalysis from "@/pages/admin/subs/TableAnalysis.vue";
import PermissionListPopup from "@/pages/admin/subs/permission/PermissionListPopup.vue";
import { useFormValidation } from "@/composables/useFormValidation";
import { httpClient } from "@/utils/http-common";
import { FormRef } from "@/interfaces/admin/admin";
import { WIDTH_BUTTON } from "@/constants/index";

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
});
const formRef = ref<any>(null);
const { validateForm } = useFormValidation(formRef as unknown as FormRef);
const openPopupPermissionList = ref(false);
const itemSelectedPermission = ref<any>(null);
const itemChoosePermission = ref(null);
const openPopupConfirm = ref(false);
const dataTable = ref<any[]>([]);

const { t } = useI18n();
const loadingStore = useLoadingStore();
const useSnackbar = useSnackbarStore();
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

const permissionGroupName = computed(() => {
  if (props.itemEdit?.authGrpNm) {
    return props.itemEdit?.authGrpNm;
  }
  return "";
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
      title: `*${t("product_platform.permissionEntity.group.permissionCode")}`,
      align: "start",
      sortable: false,
      key: "permissionCode",
      class: "header",
    },
    {
      title: `*${t("product_platform.permissionEntity.group.permissionName")}`,
      align: "start",
      sortable: false,
      key: "permissionName",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.group.permissionType"),
      align: "start",
      sortable: false,
      key: "permissionType",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.description"),
      align: "start",
      sortable: false,
      key: "description",
      class: "header",
    },
  ];
});

const fetchDataPermissionByGroupId = async (groupId) => {
  try {
    const response = await httpClient.get(
      `/api/comm/authGrp/authGrpInfo/auth/list/v1?`,
      {
        params: {
          authGrpId: groupId,
        },
      }
    );

    if (response.data) {
      response.data.forEach((x, index) => {
        dataTable.value.push({
          ...x,
          key: index + 1,
          permissionCode: x.authCd,
          permissionName: x.authNm,
          permissionType: x.authKdCdNm,
          description: x.authDscr,
        });
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

const clickDetailPermission = (item: any) => {
  itemSelectedPermission.value = item;
};

const showModalSelectPermission = (key) => {
  itemChoosePermission.value = key;
  openPopupPermissionList.value = true;
};

const addRow = () => {
  dataTable.value = [
    ...dataTable.value,
    {
      key: dataTable.value.length + 1,
      permissionCode: "",
      permissionName: "",
      permissionType: "",
      description: "",
    },
  ];
  dataTable.value = dataTable.value.map((x, index) => ({
    ...x,
    key: index + 1,
  }));

  itemSelectedPermission.value = null;
};

const deleteRow = () => {
  if (itemSelectedPermission.value) {
    dataTable.value = dataTable.value.filter(
      (x) => x.key !== itemSelectedPermission.value?.key
    );
    dataTable.value = dataTable.value.map((x, index) => ({
      ...x,
      key: index + 1,
    }));
    itemSelectedPermission.value = null;
  } else {
    useSnackbar.showSnackbar(
      t("product_platform.commonAdmin.plsSelectRow"),
      "error"
    );
  }
};

const onSelectItemPermission = (item) => {
  let checkExisted = dataTable.value.some(
    (x) => x.permissionCode === item.authCd
  );
  if (!checkExisted) {
    dataTable.value = dataTable.value.map((x) => {
      if (x.key === itemChoosePermission.value) {
        return {
          ...item,
          key: x.key,
          permissionCode: item.authCd,
          permissionName: item.authNm,
          permissionType: item.authKdCdNm,
          description: item.authDscr,
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
  let dataPermissions = dataTable.value.map((x) => ({
    authCd: x.authCd,
    authNM: x.authNm,
    authKdCd: x.authKdCd,
    authKdCdNm: x.authKdCdNm,
    authDscr: x.authDscr,
    authGrbyAuthRelId: x.authGrbyAuthRelId,
  }));
  try {
    let response;

    const data = {
      authGrpInfo: {
        authGrpId: props.itemEdit?.authGrpId,
      },
      authInfo: dataPermissions,
    };

    response = await httpClient.post(
      `/api/comm/authGrp/authGrpInfo/auth/v1`,
      data
    );

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
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
    emit("resetItemSelected", props.itemEdit);
  } finally {
    loadingStore.setLoading(false);
    openPopupConfirm.value = false;
  }
};

onMounted(async () => {
  await fetchDataPermissionByGroupId(props.itemEdit?.authGrpId);
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
