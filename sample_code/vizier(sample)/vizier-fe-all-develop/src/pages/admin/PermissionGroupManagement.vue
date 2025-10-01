<template>
  <div class="rounded-lg border border-[#666] flex h-full bg-white">
    <div class="relative z-10 rounded-lg min-h-[300px] w-full">
      <div class="pt-6 pb-3 rounded h-full flex flex-col">
        <div class="flex px-6 z-30 flex-wrap justify-between items-center">
          <div class="flex flex-nowrap justify-between items-center gap-4">
            <div class="flex flex-nowrap items-center">
              <div class="filter flex items-center gap-2 ml-2">
                <base-select
                  v-model="searchParams.authGrpLcls"
                  :width="'180px'"
                  :label="
                    $t(
                      'product_platform.permissionEntity.group.permissionGroupCategory'
                    )
                  "
                  :density="'comfortable'"
                  :items="permGrpCtgOptionsCp"
                  :item-title="'title'"
                  :item-value="'value'"
                  class="h-[48px] w-[180px]"
                  :default-item-select-all="false"
                />

                <base-select
                  v-model="searchParams.authGrpScls"
                  :width="'140px'"
                  :label="
                    $t(
                      'product_platform.permissionEntity.group.permissionGroupSubdivision'
                    )
                  "
                  :density="'comfortable'"
                  :items="permGrpSubdvOptionsCp"
                  :item-title="'title'"
                  :item-value="'value'"
                  class="h-[48px] w-[140px]"
                  :default-item-select-all="false"
                />

                <base-select
                  v-model="searchParams.actvYn"
                  :width="'100'"
                  :label="$t('product_platform.permissionEntity.enabled')"
                  :density="'comfortable'"
                  :items="enableOptions"
                  :item-title="'title'"
                  :item-value="'value'"
                  class="h-[48px] w-[150px]"
                  :default-item-select-all="false"
                />

                <base-input-text
                  v-model="searchParams.rgstUsrNm"
                  :readonly="true"
                  :width="'180px'"
                  :placeholder="
                    $t('product_platform.permissionEntity.registrant')
                  "
                  :styles="'input-search'"
                  class="h-[48px] w-[180px]"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                >
                  <template #append-inner>
                    <div class="flex flex-row gap-1">
                      <BaseButton
                        :color="ButtonColorType.Gray"
                        :width="WIDTH_BUTTON.FOR_INPUT"
                        :height="HEIGHT_BUTTON.FOR_INPUT"
                        @click="showModalSelectRegistrant"
                      >
                        <SearchIcon fill="#6B6D70" />
                      </BaseButton>

                      <BaseButton
                        :color="ButtonColorType.Gray"
                        :width="WIDTH_BUTTON.FOR_INPUT"
                        :height="HEIGHT_BUTTON.FOR_INPUT"
                        @click="resetValueUser()"
                      >
                        <delete-icon :fill="'#6B6D70'" />
                      </BaseButton>
                    </div>
                  </template>
                </base-input-text>

                <base-input-text
                  v-model="searchParams.authGrpNm"
                  width="200px"
                  :placeholder="
                    $t(
                      'product_platform.permissionEntity.group.permissionGroup'
                    )
                  "
                  :styles="'input-search'"
                  class="h-[48px] w-[200px]"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                />
                <SearchAndRefreshButton
                  class="h-[40px] w-[88px]"
                  @handle-search="handleSearch"
                  @handle-refresh="handleResetSearch"
                />
              </div>
            </div>
          </div>

          <div class="flex justify-center items-center ml-auto gap-2">
            <BaseTotalSearchResult
              class="h-[20px] w-[166px]"
              :total-search="totalSearchItems"
              :total-items="pagination.totalItems"
            />
            <BaseButton
              :color="ButtonColorType.Gray"
              class="bg-light-blue-500 text-text-lighter"
              :width="WIDTH_BUTTON.AUTO"
              @click="redirectToUser"
            >
              <menu-icon :fill="'#6B6D70'" class="mr-[6px]" />
              {{ $t("product_platform.permissionEntity.group.userManagement") }}
            </BaseButton>

            <BaseButton
              :color="ButtonColorType.Gray"
              class="bg-light-blue-500 text-text-lighter"
              @click="onShowPopupForm('edit')"
            >
              <edit-icon :fill="'#6B6D70'" class="mr-[6px]" />
              {{ $t("product_platform.commonAdmin.edit") }}
            </BaseButton>

            <BaseButton
              :color="ButtonColorType.Secondary"
              @click="onShowPopupForm('add')"
            >
              <v-icon class="mr-[6px]">mdi-plus</v-icon>
              {{ $t("product_platform.commonAdmin.create") }}
            </BaseButton>

            <BaseButton
              :color="ButtonColorType.Secondary"
              :width="WIDTH_BUTTON.AUTO"
              @click="onShowPopupPermissionCreate"
            >
              <v-icon class="mr-[6px]">mdi-plus</v-icon>
              {{
                $t("product_platform.permissionEntity.group.createPermission")
              }}
            </BaseButton>
          </div>
        </div>

        <!-- table permission group -->
        <div>
          <div class="flex-grow">
            <DataTableCustom
              v-model:pageSize="pagination.pageSize"
              v-model:current-page="pagination.currentPage"
              :headers="headerTable"
              :data="currentPageData"
              :loading="isLoadingTableData"
              :total-items="pagination.totalItems || 0"
              :total-pages="pagination.totalPages || 0"
              class="pt-6"
              :class="'2xl:!max-h-[calc(100vh_-_301px)] !max-h-[calc(100vh_-_349px)]'"
              @click-detail="clickDetail"
            />
          </div>
        </div>
      </div>
    </div>
  </div>

  <UserOrgPopup
    v-if="openPopupScreenRegistrant"
    v-model="openPopupScreenRegistrant"
    @selected-item="onSelectItemRegistrant"
  />
  <PermissionGroupForm
    v-if="openPopupPermissionForm"
    v-model="openPopupPermissionForm"
    :item-edit="itemSelected"
    :form-type="formType"
    @reset-item-selected="resetItemSelect"
  />
  <PermissionCreatePopup
    v-if="openPopupPermissionCreate"
    v-model="openPopupPermissionCreate"
    :item-edit="itemSelected"
    @reset-item-selected="resetItemSelect"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";
import clone from "lodash-es/clone";
import useCmcdStore from "@/store/cmcd.store";
import { httpClient } from "@/utils/http-common";
import { configPath } from "@/utils/config-path";
import { useMenuStore } from "@/store";
import { usePermissionGroupStore, useSnackbarStore } from "@/store";
import { ButtonColorType } from "@/enums";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";
import PermissionCreatePopup from "@/pages/admin/subs/permission/group/PermissionCreatePopup.vue";
import PermissionGroupForm from "@/pages/admin/subs/permission/group/PermissionGroupForm.vue";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

const UserOrgPopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/user/UserOrgPopup.vue")
);

const permGrpStore = usePermissionGroupStore();
const useSnackbar = useSnackbarStore();
const router = useRouter();
const menuStore = useMenuStore();
type AddTabFunction = (item: Object) => void;
const addTab = inject<AddTabFunction>("addTab");
const menuList = inject<any>("menuList");

const { search } = useCmcdStore();
const { t } = useI18n();
const formType = ref<string>(FORM_TYPE_OPTION.CREATE);

const totalSearchItems = computed(() => {
  return currentPageData.value.length || 0;
});

const enableOptions = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    { title: t("product_platform.commonAdmin.enabled"), value: "Y" },
    { title: t("product_platform.commonAdmin.disabled"), value: "N" },
  ];
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
      title: t(
        "product_platform.permissionEntity.group.permissionGroupCategory"
      ),
      align: "start",
      sortable: false,
      key: "authGrpLclsNm",
      class: "header",
    },
    {
      title: t(
        "product_platform.permissionEntity.group.permissionGroupSubdivision"
      ),
      align: "start",
      sortable: false,
      key: "authGrpSclsNm",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.group.permissionGroupName"),
      align: "start",
      sortable: false,
      key: "authGrpNm",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.enabled"),
      align: "center",
      sortable: false,
      key: "enableDisable",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.group.numberOfUser"),
      align: "start",
      sortable: false,
      key: "usrCnt",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.group.numberOfPermissions"),
      align: "start",
      sortable: false,
      key: "authCnt",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.registrant"),
      align: "start",
      sortable: false,
      key: "rgstUsrNm",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.revisionDate"),
      align: "start",
      sortable: false,
      key: "updDtm",
      class: "header",
    },
  ];
});

const isLoadingTableData = ref(false);
const itemSelected = ref(null);
const openPopupScreenRegistrant = ref(false);
const openPopupPermissionForm = ref(false);
const openPopupPermissionCreate = ref(false);
const dataListUser = ref<any[]>([]);
const permGrpSubdvOptions = ref<any[]>([]);
const permGrpCtgOptions = ref<any[]>([]);

const permGrpCtgOptionsCp = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    ...permGrpCtgOptions.value,
  ];
});

const redirectToUser = () => {
  let menuUser = {
    children: null,
    menuId: "88",
    menuLv: "3",
    menuNm: "User Management",
    scrnId: "COMMU002M",
    scrnLinkUrl: "/admin/org/user/user-management",
    scrnNm: "COMMU002M",
    scrnPathNm: "/src/pages/admin/UserManagement.vue",
  };

  const instance: any = clone(menuUser);
  instance.path = configPath(instance);
  instance.menuNm = "Admin";
  instance.rawName = "Admin";

  instance.menuNm += ` Org/User ${menuUser.menuNm}`;
  instance.rawName += `Org/User${menuUser.menuNm}`.replace(/\s+/g, "");
  menuStore.setOpenId([86]);

  if (addTab) {
    addTab(instance);
  }
  if (menuList?.length < 5) {
    router.push(configPath(menuUser));
    menuStore.setActiveMenu(menuUser);
  } else {
    useSnackbar.showSnackbar(
      "The screen can open up to maximum of 6.\nPlease close any unnecessary screens and proceed.",
      "warning"
    );
    return;
  }
};

const permGrpSubdvOptionsCp = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    ...permGrpSubdvOptions.value,
  ];
});

const { paginatedItems: currentPageData, pagination } = storeToRefs(
  usePermissionGroupStore()
);

const searchParams = ref({
  authGrpLcls: " ",
  authGrpScls: " ",
  actvYn: " ",
  rgstUsrId: "",
  rgstUsrNm: "",
  authGrpNm: "",
});

const handleResetSearch = () => {
  searchParams.value = {
    authGrpLcls: " ",
    authGrpScls: " ",
    actvYn: " ",
    rgstUsrId: "",
    rgstUsrNm: "",
    authGrpNm: "",
  };
  itemSelected.value = null;
  handleSearch();
};

const handleSearch = async () => {
  isLoadingTableData.value = true;
  const { authGrpLcls, authGrpScls, actvYn, rgstUsrId, authGrpNm } =
    searchParams.value;
  const request: any = {
    authGrpLcls: authGrpLcls.trim() || null,
    authGrpScls: authGrpScls.trim() || null,
    actvYn: actvYn.trim() || null,
    rgstUsrId: rgstUsrId.trim() || null,
    authGrpNm: authGrpNm.trim() || null,
  };
  permGrpStore.setCurrentPage(1);
  await permGrpStore.fetchPermissionGroup(request);
  isLoadingTableData.value = false;
};

const showModalSelectRegistrant = () => {
  openPopupScreenRegistrant.value = true;
};
const onShowPopupForm = (type) => {
  if (type === "edit") {
    if (itemSelected.value) {
      formType.value = FORM_TYPE_OPTION.UPDATE;
      openPopupPermissionForm.value = true;
    } else {
      useSnackbar.showSnackbar(
        t("product_platform.commonAdmin.plsSelectRow"),
        "error"
      );
    }
  } else {
    formType.value = FORM_TYPE_OPTION.CREATE;
    openPopupPermissionForm.value = true;
  }
};
const onShowPopupPermissionCreate = () => {
  if (itemSelected.value) {
    openPopupPermissionCreate.value = true;
  } else {
    useSnackbar.showSnackbar(
      t("product_platform.commonAdmin.plsSelectRow"),
      "error"
    );
  }
};
const clickDetail = (item: any) => {
  itemSelected.value = item;
};
const resetItemSelect = (item) => {
  if (!item) {
    permGrpStore.setCurrentPage(1);
  }

  itemSelected.value = item || null;
};

const resetValueUser = () => {
  searchParams.value = {
    ...searchParams.value,
    rgstUsrId: "",
    rgstUsrNm: "",
  };
};

const onSelectItemRegistrant = (item) => {
  searchParams.value = {
    ...searchParams.value,
    rgstUsrId: item.userId,
    rgstUsrNm: item.userNm,
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

const getPermCtgValue = async () => {
  let permCtgValue = await search(["AUTH_GRP_LCLS"]);
  if (permCtgValue) {
    permGrpCtgOptions.value = [
      ...permCtgValue?.AUTH_GRP_LCLS.map((item) => ({
        title: item.cmcdDetlNm,
        value: item.cmcdDetlId,
      })),
    ];
  }
};

const getPermGrpSubdvValue = async () => {
  let permSubdvValue = await search(["AUTH_GRP_SCLS"]);
  if (permSubdvValue) {
    permGrpSubdvOptions.value = [
      ...permSubdvValue?.AUTH_GRP_SCLS.map((item) => ({
        title: item.cmcdDetlNm,
        value: item.cmcdDetlId,
      })),
    ];
  }
};

onMounted(async () => {
  isLoadingTableData.value = true;
  await permGrpStore.fetchPermissionGroup();
  getPermCtgValue();
  getPermGrpSubdvValue();
  await fetchDataUser();
  isLoadingTableData.value = false;
});

watch(
  () => pagination,
  () => {
    itemSelected.value = null;
  },
  { deep: true, immediate: true }
);
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
