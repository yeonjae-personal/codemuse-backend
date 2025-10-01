<template>
  <div class="container relative bg-white py-6 pr-2 rounded-[12px] w-[420px]">
    <div class="flex flex-col h-full">
      <div class="pl-5 pr-4">
        <div class="flex justify-between items-center mb-2 h-[40px]">
          <h1
            class="font-medium text-[15px] leading-[22.5px] tracking-[0.005em]"
          >
            {{ $t("product_platform.orgInfoEntity.title.orgTableList") }}
          </h1>
        </div>
      </div>
      <v-treeview
        :key="treeKey"
        :items="treeData"
        :expand-icon="ExpandIcon"
        :collapse-icon="CollapseIcon"
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
  </div>
  <div
    class="rounded-[12px] flex flex-col h-full bg-white flex-grow w-[100vh] relative pt-6"
  >
    <div class="flex px-6 z-30 flex-wrap justify-between items-center">
      <div class="flex flex-nowrap justify-between items-center gap-4">
        <div class="flex flex-nowrap items-center">
          <div class="filter flex items-center gap-2">
            <base-input-text
              v-model="searchParams.orgInfo"
              :width="'200px'"
              :placeholder="$t('product_platform.orgInfoEntity.search.orgCdNm')"
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

      <div class="flex justify-center items-center ml-auto gap-2">
        <BaseButton
          :color="ButtonColorType.Gray"
          @click="showPopupOrgForm(true)"
        >
          <edit-icon :fill="'#6B6D70'" class="mr-[6px]" />
          {{ $t("product_platform.commonAdmin.edit") }}
        </BaseButton>

        <BaseButton
          :color="ButtonColorType.Secondary"
          @click="showPopupOrgForm()"
        >
          <v-icon class="mr-[6px]">mdi-plus</v-icon>
          {{ $t("product_platform.commonAdmin.create") }}
        </BaseButton>
      </div>
    </div>

    <!-- -------------- start row 2 -------------- -->
    <div
      v-if="isShowOrgDetail"
      class="flex justify-end items-center w-ful px-6 pt-3"
    >
      <div>
        <BaseTotalSearchResult
          :total-search="totalSearchItems"
          :total-items="pagination.totalItems"
        />
      </div>
    </div>
    <div
      v-if="isShowOrgDetail"
      class="px-6 pt-3 pb-4 overflow-y-auto bg-clip-content"
    >
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
  <OrgInfoPopup
    v-if="openPopupOrgForm"
    v-model="openPopupOrgForm"
    :data="itemSelected"
    :form-type="formType"
    @reset-item-selected="resetDefault"
  />
</template>

<!-- eslint-disable security/detect-object-injection -->
<script setup>
import { VTreeview } from "vuetify/labs/VTreeview";
import ExpandIcon from "@/components/prod/icons/ExpandIcon.vue";
import CollapseIcon from "@/components/prod/icons/CollapseIcon.vue";
import useCmcdStore from "@/store/cmcd.store";
import { useOrgStore, useSnackbarStore } from "@/store";
import { convertToTree } from "./OrgUtils.ts";
import { ButtonColorType } from "@/enums";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";
import OrgInfoPopup from "./OrgInfoPopup.vue";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import { useI18n } from "vue-i18n";

const orgStore = useOrgStore();
const { t } = useI18n();
const {
  orgItemsInfo,
  paginatedItems: currentPageData,
  pagination,
} = storeToRefs(orgStore);
const { search } = useCmcdStore();
const useSnackbar = useSnackbarStore();
const formType = ref(FORM_TYPE_OPTION.CREATE);
const treeKey = ref(0);
const itemActive = ref(null);
const itemSelected = ref(null);
const isOpenAll = ref(false);
const orgTypeOptions = ref([]);
const orgStatusOptions = ref([]);
const isLoadingTableData = ref(false);
const isShowOrgDetail = ref(false);
const openPopupOrgForm = ref(false);
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

const newItem = [];
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

const totalSearchItems = computed(() => {
  return currentPageData.value.length || 0;
});

const openMenuOnload = computed(() => {
  if (isSearch.value) {
    treeKey.value++;
    isOpenAll.value = true;
    if (treeData?.value.length > 0) {
      return getListOpened(treeData?.value);
    }
  }
  let arr = [];
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

const showPopupOrgForm = (isEdit = false) => {
  if (isEdit) {
    if (itemSelected.value) {
      formType.value = FORM_TYPE_OPTION.UPDATE;
      openPopupOrgForm.value = true;
    } else {
      useSnackbar.showSnackbar(
        t("product_platform.commonAdmin.plsSelectRow"),
        "error"
      );
    }
  } else {
    formType.value = FORM_TYPE_OPTION.CREATE;
    openPopupOrgForm.value = true;
  }
};

const resetDefault = () => {
  clickDetail(null);
  orgStore.setCurrentPage(1);
  itemActive.value = null;
  isShowOrgDetail.value = false;
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
    if (request[item]) {
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
    itemSelected.value = null;
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
<style scoped>
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
</style>
