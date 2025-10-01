<template>
  <BasePopup
    v-model="isOpen"
    :title="$t('product_platform.userEntity.title.userSearch')"
    :size="DialogSizeType.Medium"
  >
    <template #body>
      <div class="w-[800px] pt-6 gap-2">
        <!-- -------------- start row 1-------------- -->
        <div
          class="flex gap-2 px-6 flex-nowrap items-center w-[752px] h-[48px] mb-2"
        >
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.userId"
              :width="'120px'"
              :placeholder="$t('product_platform.userEntity.table.userId')"
              :styles="'input-search'"
              class="h-[48px] w-[120px]"
              @keyup.enter="handleSearch"
              @click:append-inner="handleSearch"
            />
          </div>
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.userNm"
              :width="'260px'"
              :placeholder="$t('product_platform.userEntity.table.userNm')"
              :styles="'input-search'"
              class="h-[48px] w-[260px]"
              @keyup.enter="handleSearch"
              @click:append-inner="handleSearch"
            />
          </div>
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.orgInfo"
              :width="'260px'"
              :placeholder="$t('product_platform.userEntity.table.orgCdNm')"
              :styles="'input-search'"
              class="h-[48px] w-[260px]"
              @keyup.enter="handleSearch"
              @click:append-inner="handleSearch"
            />
          </div>
          <div class="flex-1">
            <div class="w-[88px]">
              <SearchAndRefreshButton
                @handle-search="handleSearch"
                @handle-refresh="handleResetSearch"
              />
            </div>
          </div>
        </div>

        <!-- -------------- start row 2-------------- -->
        <div class="flex justify-between items-center w-ful px-6 pt-3 pb-2">
          <div class="font-weight-medium font-base font-size-base">
            <p>{{ $t("product_platform.userEntity.title.userList") }}</p>
          </div>
          <div>
            <BaseTotalSearchResult
              :total-search="totalSearchItems"
              :total-items="pagination.totalItems"
            />
          </div>
        </div>
        <div class="max-h-[336px] flex-grow table-user-popup px-6">
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
    </template>

    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton @click="handleConfirm()">
          {{ $t("product_platform.commonAdmin.confirm") }}
        </BaseButton>
        <BaseButton :color="ButtonColorType.Gray" @click="closeDialog()">
          {{ t("product_platform.cancel") }}
        </BaseButton>
      </div>
    </template>
  </BasePopup>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { ButtonColorType, DialogSizeType } from "@/enums";

import { useSnackbarStore, useUserStore } from "@/store";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";

const userStore = useUserStore();
const { paginatedItems: currentPageData, pagination } =
  storeToRefs(useUserStore());
const emit = defineEmits(["update:modelValue", "selectedItem"]);
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  selectedItem: {
    type: Object,
    default: () => {},
  },
});

const isLoadingTableData = ref(false);
const itemSelected = ref(null);
const searchParams = ref({
  userId: "",
  userNm: "",
  orgInfo: "",
});

const { t } = useI18n();
const useSnackbar = useSnackbarStore();

// computed
const isOpen = computed({
  get() {
    return props.modelValue;
  },
  set(newValue) {
    emit("update:modelValue", newValue);
  },
});
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

const clickDetail = (item: any) => {
  itemSelected.value = item;
};

const handleResetSearch = async () => {
  searchParams.value = {
    userId: "",
    userNm: "",
    orgInfo: "",
  };
  itemSelected.value = null;
  handleSearch();
};

const handleSearch = async () => {
  isLoadingTableData.value = true;
  const { userId, userNm, orgInfo } = searchParams.value;

  const request: any = {
    userId: userId.trim() || null,
    userNm: userNm.trim() || null,
    orgInfo: orgInfo.trim() || null,
  };
  userStore.setCurrentPage(1);
  await userStore.fetchUserManagement(request);
  itemSelected.value = null;
  isLoadingTableData.value = false;
};

const closeDialog = () => {
  isOpen.value = false;
};

const handleConfirm = () => {
  if (itemSelected.value) {
    emit("selectedItem", itemSelected.value);
    closeDialog();
    userStore.setCurrentPage(1);
  } else {
    useSnackbar.showSnackbar(
      t("product_platform.commonAdmin.plsSelectOne"),
      "error"
    );
  }
};

onMounted(async () => {
  isLoadingTableData.value = true;
  await userStore.fetchUserManagement();
  isLoadingTableData.value = false;
  userStore.setCurrentPage(1);
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
:deep(.v-data-table-header__content) {
  font-family: Noto Sans KR;
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
}
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

:deep(.table-user-popup) {
  .v-table {
    max-height: 278px !important;
  }
  .v-table__wrapper {
    border: solid 1px rgba(230, 233, 237, 1) !important;
    border-radius: 8px !important;
  }
}
</style>
