<template>
  <BasePopup
    v-model="isOpen"
    :title="$t('product_platform.permissionEntity.search.permissionSearch')"
    :size="DialogSizeType.Medium"
  >
    <template #body>
      <div class="w-[800px] pt-6 gap-2">
        <!-- -------------- start row 1-------------- -->
        <div
          class="flex gap-2 px-6 flex-nowrap items-center w-[752px] h-[48px]"
        >
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.authCd"
              :label="$t('product_platform.permissionEntity.permissionId')"
              :styles="'input-form'"
              class="h-[48px] w-[160px]"
            >
            </base-input-text>
          </div>
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.authNm"
              :label="$t('product_platform.permissionEntity.permissionName')"
              :styles="'input-form'"
              class="h-[48px] w-[340px]"
            >
            </base-input-text>
          </div>
          <div class="flex-1">
            <div class="w-[140px]">
              <base-select
                v-model="searchParams.authKdCd"
                :width="'140px'"
                :label="$t('product_platform.permissionEntity.permissionType')"
                :density="'comfortable'"
                :items="permissionTypeOptionsCp"
                :item-title="'title'"
                :item-value="'value'"
                class="h-[48px] w-[140px]"
                :default-item-select-all="false"
              />
            </div>
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
        <div class="flex justify-between items-center w-ful px-6 mt-3 mb-2">
          <div class="font-weight-medium font-base font-size-base">
            <p>
              {{
                $t("product_platform.permissionEntity.search.permissionList")
              }}
            </p>
          </div>
          <div>
            <BaseTotalSearchResult
              :total-search="totalSearchItems"
              :total-items="pagination.totalItems"
            />
          </div>
        </div>
        <div class="flex max-h-[374px] table-permission-list px-6">
          <DataTableCustom
            v-model:pageSize="pagination.pageSize"
            v-model:current-page="pagination.currentPage"
            class="custom-table"
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
import { useSnackbarStore, usePermissionStore } from "@/store";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";
import useCmcdStore from "@/store/cmcd.store";

const { paginatedItems: currentPageData, pagination } =
  storeToRefs(usePermissionStore());
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
const permissionTypeOptions = ref<any[]>([]);
const searchParams = ref({
  authCd: "",
  authNm: "",
  authKdCd: " ",
});

const { t } = useI18n();
const permissionStore = usePermissionStore();
const useSnackbar = useSnackbarStore();
const { search } = useCmcdStore();

// computed
const isOpen = computed({
  get() {
    return props.modelValue;
  },
  set(newValue) {
    emit("update:modelValue", newValue);
  },
});

const permissionTypeOptionsCp = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    ...permissionTypeOptions.value,
  ];
});

const totalSearchItems = computed(() => {
  return currentPageData.value.length || 0;
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

const clickDetail = (item: any) => {
  itemSelected.value = item;
};

const handleResetSearch = () => {
  searchParams.value = {
    authCd: "",
    authNm: "",
    authKdCd: " ",
  };
  itemSelected.value = null;
  handleSearch();
};

const handleSearch = async () => {
  isLoadingTableData.value = true;

  const { authCd, authNm, authKdCd } = searchParams.value;

  const request: any = {
    authCd: authCd.trim() || null,
    authNm: authNm.trim() || null,
    authKdCd: authKdCd.trim() || null,
    actvYn: "Y",
    authCtrlYn: "Y",
  };
  permissionStore.setCurrentPage(1);
  await permissionStore.fetchPermission(request);
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
    permissionStore.setCurrentPage(1);
  } else {
    useSnackbar.showSnackbar(
      t("product_platform.commonAdmin.plsSelectOne"),
      "error"
    );
  }
};

onMounted(async () => {
  isLoadingTableData.value = true;
  await permissionStore.fetchPermission({
    actvYn: "Y",
    authCtrlYn: "Y",
  });

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
:deep(.custom-table) {
  overflow-y: hidden;
}
:deep(.table-permission-list) {
  .v-table {
    max-height: 273px !important;
  }
  .v-table__wrapper {
    border: solid 1px rgba(230, 233, 237, 1) !important;
    border-radius: 8px !important;
  }
}
</style>
