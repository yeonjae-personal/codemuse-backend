<template>
  <div class="rounded-lg border border-[#666] flex bg-white mb-2">
    <div class="relative z-10 rounded-lg min-h-[300px] w-full">
      <div class="pt-6 rounded h-full flex flex-col">
        <div class="flex px-6 z-30 flex-wrap justify-between items-center">
          <div class="flex flex-nowrap justify-between items-center gap-4">
            <div class="flex flex-nowrap items-center">
              <div class="filter flex items-center gap-2">
                <base-input-text
                  v-model="searchParams.userId"
                  :width="'160px'"
                  :placeholder="$t('product_platform.userEntity.table.userId')"
                  :styles="'input-search'"
                  class="w-[160px] !h-[48px]"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                />
                <base-input-text
                  v-model="searchParams.userNm"
                  :width="'240px'"
                  :placeholder="$t('product_platform.userEntity.table.userNm')"
                  :styles="'input-search'"
                  class="w-[240px] !h-[48px]"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                />
                <base-input-text
                  v-model="searchParams.orgInfo"
                  :width="'240px'"
                  :placeholder="$t('product_platform.userEntity.table.orgCdNm')"
                  :styles="'input-search'"
                  class="w-[240px] !h-[48px]"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                />
                <SearchAndRefreshButton
                  @handle-search="handleSearch"
                  @handle-refresh="handleResetSearch"
                />
              </div>
            </div>
          </div>

          <div class="flex justify-center items-center ml-auto gap-2">
            <BaseButton
              :color="ButtonColorType.Gray"
              @click="handleShowUserPopup(FORM_TYPE_OPTION.UPDATE)"
            >
              <edit-icon :fill="'#6B6D70'" class="mr-[6px]" />
              {{ $t("product_platform.commonAdmin.edit") }}
            </BaseButton>

            <BaseButton
              :color="ButtonColorType.Secondary"
              @click="handleShowUserPopup(FORM_TYPE_OPTION.CREATE)"
            >
              <v-icon class="mr-[6px]">mdi-plus</v-icon>

              {{ $t("product_platform.commonAdmin.create") }}
            </BaseButton>
          </div>
        </div>

        <!-- -------------- start row 2 -------------- -->
        <div class="flex justify-end items-center w-ful px-6 pt-3">
          <div>
            <BaseTotalSearchResult
              :total-search="totalSearchItems"
              :total-items="pagination.totalItems"
            />
          </div>
        </div>

        <!-- -------------- table user -------------- -->
        <div class="flex-grow table-user px-6 mt-2">
          <DataTableCustom
            v-model:pageSize="pagination.pageSize"
            v-model:current-page="pagination.currentPage"
            :headers="headerTable"
            :data="currentPageData"
            :loading="isLoadingTableData"
            :total-items="pagination.totalItems || 0"
            :total-pages="pagination.totalPages || 0"
            @click-detail="clickDetail"
          />
        </div>
      </div>
    </div>
  </div>

  <UserPopup
    v-if="openPopupUser"
    v-model="openPopupUser"
    :form-type="formType"
    :item-edit="itemSelected"
    @reset-item-selected="resetItemSelect"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { ButtonColorType } from "@/enums";

import { useSnackbarStore, useUserStore } from "@/store";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";
import UserPopup from "@/pages/admin/subs/user/UserPopup.vue";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import { UserManagementSearchParam } from "@/interfaces/admin/admin";

const userStore = useUserStore();
const useSnackbar = useSnackbarStore();
const { paginatedItems: currentPageData, pagination } =
  storeToRefs(useUserStore());

const isLoadingTableData = ref(false);
const itemSelected = ref(null);
const openPopupUser = ref(false);
const { t } = useI18n();
const formType = ref<string>(FORM_TYPE_OPTION.CREATE);
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
      title: t("product_platform.userEntity.table.userId"),
      align: "start",
      sortable: false,
      key: "userId",
      class: "header",
    },
    {
      title: t("product_platform.userEntity.table.userNm"),
      align: "start",
      sortable: false,
      key: "userNm",
      class: "header",
    },
    {
      title: t("product_platform.userEntity.table.userKdCd"),
      align: "start",
      sortable: false,
      key: "userKdCd",
      class: "header",
    },
    {
      title: t("product_platform.userEntity.table.userKdCdNm"),
      align: "center",
      sortable: false,
      key: "userKdCdNm",
      class: "header",
    },
    {
      title: t("product_platform.userEntity.table.orgCd"),
      align: "start",
      sortable: false,
      key: "orgCd",
      class: "header",
    },
    {
      title: t("product_platform.userEntity.table.orgNm"),
      align: "start",
      sortable: false,
      key: "orgNm",
      class: "header",
    },
    {
      title: t("product_platform.userEntity.table.whofStatNm"),
      align: "start",
      sortable: false,
      key: "whofStatNm",
      class: "header",
    },
    {
      title: t("product_platform.userEntity.table.updDtm"),
      align: "start",
      sortable: false,
      key: "updDtm",
      class: "header",
    },
  ];
});

const totalSearchItems = computed(() => {
  return currentPageData.value.length || 0;
});

const searchParams = ref({
  userId: "",
  userNm: "",
  orgInfo: "",
});

const handleSearch = async () => {
  isLoadingTableData.value = true;
  const { userId, userNm, orgInfo } = searchParams.value;
  if (!userId.trim() && !userNm.trim() && !orgInfo.trim()) {
    useSnackbar.showSnackbar(
      t("product_platform.userEntity.message.plsSelectValue"),
      "error"
    );
    isLoadingTableData.value = false;
    return;
  }
  const request: UserManagementSearchParam = {
    userId: userId.trim() || null,
    userNm: userNm.trim() || null,
    orgInfo: orgInfo.trim() || null,
  };
  userStore.setCurrentPage(1);
  await userStore.fetchUserManagement(request);
  itemSelected.value = null;
  isLoadingTableData.value = false;
};

const handleResetSearch = async () => {
  searchParams.value = {
    userId: "",
    userNm: "",
    orgInfo: "",
  };
  isLoadingTableData.value = true;

  userStore.setCurrentPage(1);
  await userStore.fetchUserManagement();
  itemSelected.value = null;
  isLoadingTableData.value = false;
};

const clickDetail = (item: any) => {
  itemSelected.value = item;
};

const resetItemSelect = (item) => {
  if (!item) {
    userStore.setCurrentPage(1);
  }

  itemSelected.value = item || null;
};

const handleShowUserPopup = (type: string) => {
  if (type === FORM_TYPE_OPTION.UPDATE) {
    if (itemSelected.value) {
      formType.value = type;
      openPopupUser.value = true;
    } else {
      useSnackbar.showSnackbar(
        t("product_platform.commonAdmin.plsSelectRow"),
        "error"
      );
    }
  } else {
    formType.value = type;
    openPopupUser.value = true;
  }
};

onMounted(async () => {
  isLoadingTableData.value = true;
  await userStore.fetchUserManagement();
  isLoadingTableData.value = false;
  userStore.setCurrentPage(1);
});
</script>

<style scoped>
:deep(.v-table__wrapper) {
  border: solid 1px rgba(230, 233, 237, 1) !important;
  border-radius: 8px !important;
}
</style>
