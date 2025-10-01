<template>
  <div class="rounded-lg border border-[#666] flex bg-white mb-2 max-h-[462px]">
    <div class="relative z-10 rounded-lg min-h-[300px] w-full">
      <div class="pt-6 rounded h-full flex flex-col">
        <div class="flex px-6 z-30 flex-wrap justify-between items-center">
          <!---------------------------------------------------------  -->
          <div class="flex flex-nowrap justify-between items-center gap-4">
            <div class="flex flex-nowrap items-center">
              <div class="filter flex items-center gap-2">
                <base-input-text
                  v-model="searchParams.scrnId"
                  :width="'120px'"
                  :placeholder="$t('product_platform.screenEntity.screenId')"
                  :styles="'input-search'"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                />

                <base-input-text
                  v-model="searchParams.scrnNm"
                  :width="'200px'"
                  :placeholder="$t('product_platform.screenEntity.screenName')"
                  :styles="'input-search'"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                />
                <base-input-text
                  v-model="searchParams.scrnPathNm"
                  :width="'200px'"
                  :placeholder="$t('product_platform.screenEntity.screenPath')"
                  :styles="'input-search'"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                />
                <base-input-text
                  v-model="searchParams.rgstUsrNm"
                  :width="'180px'"
                  class="h-[48px] w-[180px]"
                  :placeholder="$t('product_platform.screenEntity.registrant')"
                  :styles="'input-search'"
                  :readonly="true"
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
                        @click="resetValueUser('registrant')"
                      >
                        <delete-icon :fill="'#6B6D70'" />
                      </BaseButton>
                    </div>
                  </template>
                </base-input-text>

                <base-input-text
                  v-model="searchParams.authAprvUsrNm"
                  class="h-[48px] w-[180px]"
                  :width="'180px'"
                  :placeholder="$t('product_platform.screenEntity.approver')"
                  :styles="'input-search'"
                  :readonly="true"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
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
                        @click="resetValueUser('approver')"
                      >
                        <delete-icon :fill="'#6B6D70'" />
                      </BaseButton>
                    </div>
                  </template>
                </base-input-text>

                <base-select
                  v-model="searchParams.actvYn"
                  :width="'120px'"
                  :label="$t('product_platform.screenEntity.enabled')"
                  :density="'comfortable'"
                  :items="enableOptions"
                  :item-title="'title'"
                  :item-value="'value'"
                  class="h-[48px] w-[120px]"
                  :default-item-select-all="false"
                />

                <base-select
                  v-model="searchParams.authCtrlYn"
                  :width="'150px'"
                  :label="$t('product_platform.screenEntity.permissionControl')"
                  :density="'comfortable'"
                  :items="permissionControlOptions"
                  :item-title="'title'"
                  :item-value="'value'"
                  class="h-[48px] w-[150px]"
                  :default-item-select-all="false"
                />
                <SearchAndRefreshButton
                  @handle-search="handleSearch"
                  @handle-refresh="handleResetSearch"
                />
              </div>
            </div>
          </div>
          <!---------------------------------------------------------  -->

          <div class="flex justify-center items-center ml-auto gap-2">
            <BaseButton
              :color="ButtonColorType.Gray"
              class="bg-light-blue-500 text-text-lighter"
              :width="WIDTH_BUTTON.AUTO"
              @click="redirectToMenu"
            >
              <div class="mr-[6px]">
                <menu-icon :fill="'#6B6D70'" />
              </div>
              <span
                class="text-sm font-medium text-text-lighter font-size-base"
                >{{ $t("product_platform.screenEntity.menuManagement") }}</span
              >
            </BaseButton>

            <BaseButton
              :color="ButtonColorType.Gray"
              class="bg-light-blue-500 text-text-lighter"
              @click="handleShowScreenPopup(FORM_TYPE_OPTION.UPDATE)"
            >
              <edit-icon :fill="'#6B6D70'" class="mr-[6px]" />
              {{ $t("product_platform.commonAdmin.edit") }}
            </BaseButton>

            <BaseButton
              :color="ButtonColorType.Secondary"
              @click="handleShowScreenPopup(FORM_TYPE_OPTION.CREATE)"
            >
              <v-icon class="mr-[6px]">mdi-plus</v-icon>
              {{ $t("product_platform.commonAdmin.create") }}
            </BaseButton>
          </div>
          <!---------------------------------------------------------  -->
        </div>

        <!-- table screen -->
        <div>
          <div class="flex justify-space-between px-6 pt-3 pb-2">
            <div class="flex items-center">
              <div class="text-text-primary font-medium">
                {{ $t("product_platform.screenEntity.screenList") }}
              </div>
            </div>
            <div class="text-[13px]">
              <BaseTotalSearchResult
                :total-search="totalSearchItems"
                :total-items="pagination.totalItems"
              />
            </div>
          </div>

          <div class="flex-grow screen-table">
            <DataTableCustom
              v-model:pageSize="pagination.pageSize"
              v-model:current-page="pagination.currentPage"
              :headers="headerTable"
              :data="currentPageData"
              :loading="isLoadingTableData"
              :total-items="pagination.totalItems || 0"
              :total-pages="pagination.totalPages || 0"
              :search-field="selectedValue"
              :class="currentPageData.length > 3 ? 'pt-1 h-[322px]' : 'pt-1'"
              @click-detail="clickDetail"
            />
          </div>
        </div>
      </div>
    </div>
  </div>

  <ScreenPopup
    v-if="openPopupScreen"
    v-model="openPopupScreen"
    :form-type="formType"
    :item-edit="itemSelected"
    @reset-item-selected="resetItemSelect"
  />
  <UserOrgPopup
    v-if="openPopupScreenRegistrant"
    v-model="openPopupScreenRegistrant"
    @selected-item="onSelectItemRegistrant"
  />
  <UserOrgPopup
    v-if="openPopupScreenApprover"
    v-model="openPopupScreenApprover"
    @selected-item="onSelectItemApprover"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";
import clone from "lodash-es/clone";
import { useScreenStore, useSnackbarStore, useMenuStore } from "@/store";
import { ButtonColorType } from "@/enums";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import { httpClient } from "@/utils/http-common";
import { configPath } from "@/utils/config-path";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";
import ScreenPopup from "@/pages/admin/subs/screen/ScreenPopup.vue";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

defineOptions({
  inheritAttrs: false,
});

const UserOrgPopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/user/UserOrgPopup.vue")
);

const headerTable = computed(() => {
  return [
    {
      title: t("product_platform.commonAdmin.select"),
      align: "start",
      sortable: false,
      key: "screenNo",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.screenId"),
      align: "start",
      sortable: false,
      key: "scrnId",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.screenName"),
      align: "start",
      sortable: false,
      key: "scrnNm",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.description"),
      align: "start",
      sortable: false,
      key: "scrnDscr",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.enabled"),
      align: "center",
      sortable: false,
      key: "enableDisable",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.permissionControl"),
      align: "center",
      sortable: false,
      key: "control",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.screenLinkUrl"),
      align: "start",
      sortable: false,
      key: "scrnLinkUrl",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.screenPath"),
      align: "start",
      sortable: false,
      key: "scrnPathNm",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.registrant"),
      align: "start",
      sortable: false,
      key: "rgstUsrNm",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.approver"),
      align: "start",
      sortable: false,
      key: "authAprvUsrNm",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.revisionDate"),
      align: "start",
      sortable: false,
      key: "updDtm",
      class: "header",
    },
  ];
});

const emit = defineEmits(["selectScrnIdItem"]);

const screenStore = useScreenStore();
const useSnackbar = useSnackbarStore();

const router = useRouter();
const menuStore = useMenuStore();
type AddTabFunction = (item: Object) => void;
const addTab = inject<AddTabFunction>("addTab");
const menuList = inject<any>("menuList");

const { t } = useI18n();

const openPopupScreen = ref(false);

const { paginatedItems: currentPageData, pagination } =
  storeToRefs(useScreenStore());

const totalSearchItems = computed(() => {
  return currentPageData.value.length || 0;
});

const openPopupScreenRegistrant = ref(false);
const openPopupScreenApprover = ref(false);
const isLoadingTableData = ref(false);

const selectedValue = ref("name");
const formType = ref<string>(FORM_TYPE_OPTION.CREATE);
const itemSelected = ref<any>(null);

const clickDetail = (item: any) => {
  itemSelected.value = item;
  emit("selectScrnIdItem", itemSelected.value?.scrnId);
};

const resetItemSelect = (item) => {
  if (!item) {
    screenStore.setCurrentPage(1);
  }
  itemSelected.value = item || null;
  emit("selectScrnIdItem", null);
};

const enableOptions = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    { title: t("product_platform.commonAdmin.enabled"), value: "Y" },
    { title: t("product_platform.commonAdmin.disabled"), value: "N" },
  ];
});

const permissionControlOptions = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    { title: t("product_platform.commonAdmin.enabled"), value: "Y" },
    { title: t("product_platform.commonAdmin.disabled"), value: "N" },
  ];
});

const dataListUser = ref([]);

const searchParams = ref({
  scrnId: "",
  scrnNm: "",
  scrnPathNm: "",
  rgstUsrId: "",
  rgstUsrNm: "",
  authAprvUsrId: "",
  authAprvUsrNm: "",
  actvYn: " ",
  authCtrlYn: " ",
});

const handleResetSearch = async () => {
  searchParams.value = {
    scrnId: "",
    scrnNm: "",
    scrnPathNm: "",
    rgstUsrId: "",
    rgstUsrNm: "",
    authAprvUsrId: "",
    authAprvUsrNm: "",
    actvYn: " ",
    authCtrlYn: " ",
  };
  itemSelected.value = null;
  handleSearch();
};

const redirectToMenu = () => {
  let menu = {
    menuId: "78",
    menuNm: "Menu",
    menuLv: "3",
    scrnId: "MenuManagement",
    scrnNm: "MenuManagement",
    scrnLinkUrl: "/admin/biz-support/menu",
    scrnPathNm: "/src/pages/admin/MenuManagement.vue",
    children: null,
  };

  const instance: any = clone(menu);
  instance.path = configPath(instance);
  instance.menuNm = "Admin";
  instance.rawName = "Admin";

  instance.menuNm += ` Biz Support ${menu.menuNm}`;
  instance.rawName += `Biz Support${menu.menuNm}`.replace(/\s+/g, "");
  menuStore.setOpenId([89]);

  if (addTab) {
    addTab(instance);
  }
  if (menuList?.length < 5) {
    router.push(configPath(menu));
    menuStore.setActiveMenu(menu);
  } else {
    useSnackbar.showSnackbar(
      "The screen can open up to maximum of 6.\nPlease close any unnecessary screens and proceed.",
      "warning"
    );
    return;
  }
};

const onSelectItemRegistrant = (item) => {
  searchParams.value = {
    ...searchParams.value,
    rgstUsrId: item.userId,
    rgstUsrNm: item.userNm,
  };
};

const onSelectItemApprover = (item) => {
  searchParams.value = {
    ...searchParams.value,
    authAprvUsrId: item.userId,
    authAprvUsrNm: item.userNm,
  };
};
const showModalSelectRegistrant = () => {
  openPopupScreenRegistrant.value = true;
};

const resetValueUser = (key) => {
  if (key === "registrant") {
    searchParams.value = {
      ...searchParams.value,
      rgstUsrId: "",
      rgstUsrNm: "",
    };
  } else {
    searchParams.value = {
      ...searchParams.value,
      authAprvUsrId: "",
      authAprvUsrNm: "",
    };
  }
};

const showModalSelectApprover = () => {
  openPopupScreenApprover.value = true;
};

const handleShowScreenPopup = (type: string) => {
  if (type === FORM_TYPE_OPTION.UPDATE) {
    if (itemSelected.value) {
      formType.value = type;
      openPopupScreen.value = true;
    } else {
      useSnackbar.showSnackbar(
        t("product_platform.commonAdmin.plsSelectRow"),
        "error"
      );
    }
  } else {
    formType.value = type;
    openPopupScreen.value = true;
  }
};

const handleSearch = async () => {
  isLoadingTableData.value = true;
  clickDetail(null);
  const {
    scrnId,
    scrnNm,
    scrnPathNm,
    rgstUsrId,
    authAprvUsrId,
    actvYn,
    authCtrlYn,
  } = searchParams.value;
  const request: any = {
    scrnId: scrnId.trim() || null,
    scrnNm: scrnNm.trim() || null,
    scrnPathNm: scrnPathNm.trim() || null,
    actvYn: actvYn.trim() || null,
    authCtrlYn: authCtrlYn.trim() || null,
    rgstUsrId: rgstUsrId.trim() || null,
    authAprvUsrId: authAprvUsrId.trim() || null,
  };
  screenStore.setCurrentPage(1);
  await screenStore.fetchScreenManagement(request);
  isLoadingTableData.value = false;
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
  isLoadingTableData.value = true;
  await screenStore.fetchScreenManagement();
  await fetchDataUser();
  isLoadingTableData.value = false;
});

watch(
  () => pagination,
  () => {
    clickDetail(null);
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
:deep(.screen-table) {
  .v-table__wrapper {
    white-space: normal !important;
  }
}
</style>
