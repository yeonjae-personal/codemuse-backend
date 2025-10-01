<template>
  <BasePopup
    v-model="isOpen"
    :title="$t('product_platform.orgInfoEntity.title.orgSearch')"
    :size="DialogSizeType.ELarge"
  >
    <template #body>
      <div class="w-[1200px] pt-5 gap-2">
        <!-- -------------- start row 1-------------- -->
        <div class="flex px-6 z-30 flex-wrap justify-between items-center">
          <div class="flex flex-nowrap justify-between items-center gap-4">
            <div class="flex flex-nowrap items-center">
              <div class="filter flex items-center gap-2">
                <base-input-text
                  v-model="searchParams.orgInfo"
                  :width="'200px'"
                  :placeholder="
                    $t('product_platform.orgInfoEntity.search.orgCdNm')
                  "
                  :styles="'input-search'"
                  class="w-[200px] !h-[48px]"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                />
                <base-select
                  v-model="searchParams.orgKdCd"
                  :width="'140px'"
                  :label="$t('product_platform.orgInfoEntity.search.orgType')"
                  :density="'comfortable'"
                  :items="orgTypeOptionsCp"
                  :item-title="'title'"
                  :item-value="'value'"
                  class="h-[48px] w-[140px]"
                  :default-item-select-all="false"
                />
                <base-select
                  v-model="searchParams.orgStatCd"
                  :width="'140px'"
                  :label="$t('product_platform.orgInfoEntity.search.orgStatus')"
                  :density="'comfortable'"
                  :items="orgStatusOptionsCp"
                  :item-title="'title'"
                  :item-value="'value'"
                  class="h-[48px] w-[140px]"
                  :default-item-select-all="false"
                />
                <SearchAndRefreshButton
                  @handle-search="handleSearch"
                  @handle-refresh="handleResetSearch"
                />
              </div>
            </div>
          </div>
        </div>
        <div class="flex items-start w-ful px-6 mt-3 gap-3">
          <div
            class="!w-[360px] tree-menu-list rounded-[12px] overflow-y-auto overflow-item-hidden h-[627px]"
          >
            <div class="pl-5 pr-4">
              <div class="flex justify-between items-center h-[47px] mt-1">
                <h1
                  class="font-medium text-[15px] leading-[22.5px] tracking-[0.005em] txt-menu-tree"
                >
                  {{ $t("product_platform.orgInfoEntity.title.orgTableList") }}
                </h1>
              </div>
            </div>
            <v-treeview
              :key="treeKey"
              :items="treeData"
              :expand-icon="ExpandIcon as any"
              :collapse-icon="CollapseIcon as any"
              :opened="openMenuOnload"
              item-value="id"
              item-text="title"
              activatable
              :open-all="isOpenAll"
              item-children="children"
              :activated="itemActive"
              @update:activated="changeItemActive"
            >
              <template #title="{ item }">
                <div class="menu-item-id">
                  <span class="font-medium text-[15px] ml-2">
                    {{ item.title }}
                  </span>
                </div>
              </template>
            </v-treeview>
          </div>
          <div class="!max-w-[780px]">
            <div v-if="isShowOrgDetail" class="overflow-y-auto bg-clip-content">
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
    </template>

    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton :size="ButtonSizeType.Large" @click="handleConfirm()">
          {{ t("product_platform.commonAdmin.confirm") }}
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
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { ButtonColorType, ButtonSizeType, DialogSizeType } from "@/enums";
import { useSnackbarStore } from "@/store";
import { VTreeview } from "vuetify/labs/VTreeview";
import ExpandIcon from "@/components/prod/icons/ExpandIcon.vue";
import CollapseIcon from "@/components/prod/icons/CollapseIcon.vue";
import useCmcdStore from "@/store/cmcd.store";
import { useOrgSearchStore } from "@/store";
import { convertToTree } from "./OrgUtils.ts";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";

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

const closeDialog = () => {
  isOpen.value = false;
};

const orgStore = useOrgSearchStore();
const {
  orgItemsInfo,
  paginatedItems: currentPageData,
  pagination,
} = storeToRefs(orgStore);
const { search } = useCmcdStore();
const treeKey = ref(0);
const itemActive = ref(null);
const itemSelected = ref(null);
const isOpenAll = ref(false);
const orgTypeOptions = ref<any[]>([]);
const orgStatusOptions = ref<any[]>([]);
const isLoadingTableData = ref(false);
const isShowOrgDetail = ref(false);
const isSearch = ref(false);
const searchParams = ref({
  orgInfo: "",
  orgKdCd: " ",
  orgStatCd: " ",
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
      title: t("product_platform.orgInfoEntity.table.orgCd"),
      align: "start",
      sortable: false,
      key: "orgCd",
      class: "header",
    },
    {
      title: t("product_platform.orgInfoEntity.table.orgNm"),
      align: "start",
      sortable: false,
      key: "orgNm",
      class: "header",
    },
    {
      title: t("product_platform.orgInfoEntity.table.orgKdCdNm"),
      align: "start",
      sortable: false,
      key: "orgKdCdNm",
      class: "header",
    },
    {
      title: t("product_platform.orgInfoEntity.table.orgLvCd"),
      align: "start",
      sortable: false,
      key: "orgLvCd",
      class: "header",
    },
    {
      title: t("product_platform.orgInfoEntity.table.orgStatCd"),
      align: "center",
      sortable: false,
      key: "orgStatCdNm",
      class: "header",
    },
    {
      title: t("product_platform.orgInfoEntity.table.tlmdId"),
      align: "start",
      sortable: false,
      key: "tlmdNm",
      class: "header",
    },
    {
      title: t("product_platform.orgInfoEntity.table.validStartDtm"),
      align: "start",
      sortable: false,
      key: "validStartDtm",
      class: "header",
    },
    {
      title: t("product_platform.orgInfoEntity.table.validEndDtm"),
      align: "start",
      sortable: false,
      key: "validEndDtm",
      class: "header",
    },
    {
      title: t("product_platform.orgInfoEntity.table.updDtm"),
      align: "start",
      sortable: false,
      key: "updDtm",
      class: "header",
    },
  ];
});

const orgTypeOptionsCp = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    ...orgTypeOptions.value,
  ];
});

const orgStatusOptionsCp = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    ...orgStatusOptions.value,
  ];
});

const newItem: any[] = [];
const getListOpened = (list) => {
  list.forEach((item) => {
    newItem.push(item.id);
    if (item?.children?.length > 0) {
      getListOpened(item.children);
    }
  });

  return [...new Set(newItem)];
};

const treeData = computed(() => {
  return convertToTree(orgItemsInfo.value);
});

const openMenuOnload = computed(() => {
  if (isSearch.value) {
    treeKey.value++;
    isOpenAll.value = true;
    if (treeData?.value.length > 0) {
      return getListOpened(treeData?.value);
    }
  }
  let arr: any[] = [];
  isOpenAll.value = false;
  if (treeData?.value.length > 0) {
    treeData?.value.forEach((itemLv0) => {
      arr.push(itemLv0.id);
      if (itemLv0.children) {
        itemLv0.children.forEach((itemLv1) => {
          arr.push(itemLv1.id);
        });
      }
    });

    return arr;
  }
});

const clickDetail = (item) => {
  itemSelected.value = item;
};

const resetDefault = () => {
  clickDetail(null);
  orgStore.setCurrentPage(1);
  itemActive.value = null;
  isShowOrgDetail.value = false;
  itemSelected.value = null;
};

const handleResetSearch = async () => {
  searchParams.value = {
    orgInfo: "",
    orgKdCd: " ",
    orgStatCd: " ",
  };
  isLoadingTableData.value = true;
  resetDefault();
  await orgStore.fetchMenuTree();
  isLoadingTableData.value = false;
  isSearch.value = false;
};

const handleSearch = async () => {
  let checkChange = false;
  const { orgInfo, orgKdCd, orgStatCd } = searchParams.value;

  const request = {
    orgInfo: orgInfo.trim() || null,
    orgKdCd: orgKdCd.trim() || null,
    orgStatCd: orgStatCd.trim() || null,
  };
  Object.keys(request).forEach((item) => {
    if (request[item as string]) {
      checkChange = true;
    }
  });
  if (checkChange) {
    isLoadingTableData.value = true;
    resetDefault();
    await orgStore.fetchMenuTree(request);
    isLoadingTableData.value = false;
    isSearch.value = true;
  } else {
    handleResetSearch();
  }
};

const changeItemActive = async (items) => {
  if (items[0]) {
    itemActive.value = items[0];
    await fetchDataWithOrgCd(items[0]);
  }
};

const fetchDataWithOrgCd = async (orgCd) => {
  const { orgInfo, orgKdCd, orgStatCd } = searchParams.value;

  const request = {
    orgInfo: orgInfo.trim() || null,
    orgKdCd: orgKdCd.trim() || null,
    orgStatCd: orgStatCd.trim() || null,
  };
  isShowOrgDetail.value = true;

  await orgStore.fetchOrgDetail(orgCd, isSearch.value ? request : null);
};

const handleConfirm = () => {
  if (itemSelected.value) {
    emit("selectedItem", itemSelected.value);
    closeDialog();
  } else {
    useSnackbar.showSnackbar(
      t("product_platform.orgInfoEntity.message.plsSelectOrg"),
      "error"
    );
  }
};

watch(
  () => orgItemsInfo,
  () => {
    itemActive.value = null;
  },
  { deep: true, immediate: true }
);

onMounted(async () => {
  await orgStore.fetchMenuTree();

  let orgTypeArr = await search(["ORG_KD_CD"]);
  let orgStatusArr = await search(["ORG_STAT_CD"]);
  if (orgTypeArr) {
    orgTypeOptions.value = [
      ...orgTypeArr?.ORG_KD_CD.map((item) => ({
        title: item.cmcdDetlNm,
        value: item.cmcdDetlId,
      })),
    ];
  }
  if (orgStatusArr) {
    orgStatusOptions.value = [
      ...orgStatusArr?.ORG_STAT_CD.map((item) => ({
        title: item.cmcdDetlNm,
        value: item.cmcdDetlId,
      })),
    ];
  }
});
</script>

<style lang="scss" scoped>
.content {
  padding-bottom: 1rem;
  max-height: calc(100vh - 453px);
}

:deep().v-field {
  border-radius: 8px;
  box-shadow: none !important;
}

.v-text-field:deep() {
  font-size: 13px !important;
}

.v-field__input {
  font-size: 13px;
}

:deep(.v-label) {
  font-size: 13px !important;
}

:deep(input) {
  font-size: 13px;
  color: #3a3b3d;
}

.custom-margin-total-search-result {
  margin-bottom: 4px !important;
}

/** TreeView */
:deep(.v-treeview-item:hover) {
  color: #ba1642;
  background-color: #fff0f2;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

:deep(.v-list-item:not(:has(.v-list-item--active))) {
  font-weight: 500;
  color: #6b6d70;
  font-family: "Noto Sans KR";
}

:deep(.v-list-item-title:hover) {
  color: #ba1642;
  font-weight: bold;
}

:deep(.v-list-item--active) {
  color: #ba1642 !important;
  font-weight: bold;
  font-family: "Noto Sans KR";
}

:deep(.v-table__wrapper) {
  border: solid 1px rgba(230, 233, 237, 1) !important;
  border-radius: 8px !important;
}
:deep(.tree-menu-list) {
  border: 1px solid rgba(230, 233, 237, 1);
}
:deep(.txt-menu-tree) {
  font-family: "Noto Sans KR";
}
</style>
