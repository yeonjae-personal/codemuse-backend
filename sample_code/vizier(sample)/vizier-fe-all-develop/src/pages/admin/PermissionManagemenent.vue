<template>
  <div class="rounded-lg border border-[#666] flex h-full bg-white">
    <div class="relative z-10 rounded-lg min-h-[300px] w-full">
      <div class="pt-6 pb-3 rounded h-full flex flex-col">
        <div class="flex px-6 z-30 flex-wrap justify-between items-center">
          <div class="flex flex-nowrap justify-between items-center gap-4">
            <div class="flex flex-nowrap items-center">
              <div class="filter flex items-center gap-2 ml-2">
                <base-input-text
                  v-model="searchParams.authCd"
                  :width="'160px'"
                  :placeholder="
                    $t('product_platform.permissionEntity.permissionId')
                  "
                  :styles="'input-search'"
                  class="h-[48px] w-[160px]"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                />

                <base-input-text
                  v-model="searchParams.authNm"
                  :width="'240px'"
                  :placeholder="
                    $t('product_platform.permissionEntity.permissionName')
                  "
                  :styles="'input-search'"
                  class="h-[48px] w-[240px]"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
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
                        @click="resetValueUser('registrant')"
                      >
                        <delete-icon :fill="'#6B6D70'" />
                      </BaseButton>
                    </div>
                  </template>
                </base-input-text>
                <base-input-text
                  v-model="searchParams.authAprvUsrNm"
                  :width="'180px'"
                  :placeholder="
                    $t('product_platform.permissionEntity.approver')
                  "
                  :styles="'input-search'"
                  :readonly="true"
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
                  v-model="searchParams.authKdCd"
                  :width="'140px'"
                  :label="
                    $t('product_platform.permissionEntity.permissionType')
                  "
                  :density="'comfortable'"
                  :items="permissionTypeOptionsCp"
                  :item-title="'title'"
                  :item-value="'value'"
                  class="h-[48px] w-[140px]"
                  :default-item-select-all="false"
                />

                <base-select
                  v-model="searchParams.actvYn"
                  :width="'150px'"
                  :label="$t('product_platform.permissionEntity.enabled')"
                  :density="'comfortable'"
                  :items="enableOptions"
                  :item-title="'title'"
                  :item-value="'value'"
                  class="h-[48px] w-[150px]"
                  :default-item-select-all="false"
                />
                <base-select
                  v-model="searchParams.authCtrlYn"
                  :width="'150px'"
                  :label="
                    $t('product_platform.permissionEntity.permissionControl')
                  "
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

          <div class="flex justify-center items-center ml-auto gap-2">
            <BaseTotalSearchResult
              :total-search="totalSearchItems"
              :total-items="pagination.totalItems"
            />
          </div>
        </div>

        <!-- table permission -->
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
  <UserOrgPopup
    v-if="openPopupScreenApprover"
    v-model="openPopupScreenApprover"
    @selected-item="onSelectItemApprover"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { usePermissionStore } from "@/store";
import { ButtonColorType } from "@/enums";
import useCmcdStore from "@/store/cmcd.store";
import { httpClient } from "@/utils/http-common";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

const UserOrgPopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/user/UserOrgPopup.vue")
);

const permissionStore = usePermissionStore();
const { search } = useCmcdStore();
const { t } = useI18n();

const openPopupScreenRegistrant = ref(false);
const dataListUser = ref([]);
const openPopupScreenApprover = ref(false);
const totalSearchItems = computed(() => {
  return currentPageData.value.length || 0;
});

const headerTable = computed(() => {
  return [
    {
      title: t("product_platform.permissionEntity.permissionId"),
      align: "start",
      sortable: false,
      key: "authCd",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.permissionName"),
      align: "start",
      sortable: false,
      key: "authNm",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.permissionType"),
      align: "start",
      sortable: false,
      key: "authKdCdNm",
      class: "header",
    },
    {
      title: t("product_platform.permissionEntity.description"),
      align: "start",
      sortable: false,
      key: "authDscr",
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
      title: t("product_platform.permissionEntity.permissionControl"),
      align: "center",
      sortable: false,
      key: "control",
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
      title: t("product_platform.permissionEntity.approver"),
      align: "start",
      sortable: false,
      key: "authAprvUsrNm",
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

const enableOptions = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    { title: t("product_platform.commonAdmin.enabled"), value: "Y" },
    { title: t("product_platform.commonAdmin.disabled"), value: "N" },
  ];
});

const permissionTypeOptions = ref<any[]>([]);
const permissionTypeOptionsCp = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    ...permissionTypeOptions.value,
  ];
});

const permissionControlOptions = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    { title: t("product_platform.commonAdmin.enabled"), value: "Y" },
    { title: t("product_platform.commonAdmin.disabled"), value: "N" },
  ];
});

const { paginatedItems: currentPageData, pagination } =
  storeToRefs(usePermissionStore());

const searchParams = ref({
  authCd: "",
  authNm: "",
  rgstUsrId: "",
  rgstUsrNm: "",
  authAprvUsrId: "",
  authAprvUsrNm: "",
  authKdCd: " ",
  actvYn: " ",
  authCtrlYn: " ",
});

const handleResetSearch = () => {
  searchParams.value = {
    authCd: "",
    authNm: "",
    rgstUsrId: "",
    rgstUsrNm: "",
    authAprvUsrId: "",
    authAprvUsrNm: "",
    authKdCd: " ",
    actvYn: " ",
    authCtrlYn: " ",
  };
  handleSearch();
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

const handleSearch = async () => {
  isLoadingTableData.value = true;

  const {
    authCd,
    authNm,
    authKdCd,
    rgstUsrId,
    authAprvUsrId,
    actvYn,
    authCtrlYn,
  } = searchParams.value;

  const request: any = {
    authCd: authCd.trim() || null,
    authNm: authNm.trim() || null,
    rgstUsrId: rgstUsrId.trim() || null,
    authAprvUsrId: authAprvUsrId.trim() || null,
    authKdCd: authKdCd.trim() || null,
    actvYn: actvYn.trim() || null,
    authCtrlYn: authCtrlYn.trim() || null,
  };
  permissionStore.setCurrentPage(1);

  await permissionStore.fetchPermission(request);
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
  await permissionStore.fetchPermission();
  await fetchDataUser();
  // Get list KDCD (Permission type)
  let KDCDArr = await search(["AUTH_KD_CD"]);
  if (KDCDArr) {
    permissionTypeOptions.value = [
      ...KDCDArr?.AUTH_KD_CD.map((item) => ({
        title: item.cmcdDetlNm,
        value: item.cmcdDetlId,
      })),
    ];
  }

  isLoadingTableData.value = false;
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
