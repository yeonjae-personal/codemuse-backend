<template>
  <div class="rounded-lg border border-[#666] flex bg-white">
    <div class="relative z-10 rounded-lg min-h-[300px] w-full">
      <div class="rounded h-full flex flex-col">
        <!-- start table URIs -->
        <div>
          <div class="flex justify-space-between px-6 pt-3 pb-2">
            <div class="flex items-center">
              <div class="text-text-primary font-medium">
                {{ $t("product_platform.screenEntity.url.listOfUrl") }}
              </div>
            </div>
            <div class="text-[13px]">
              <BaseTotalSearchResult
                :total-search="totalSearchItems"
                :total-items="pagination.totalItems"
              />
            </div>
          </div>

          <div class="flex-grow">
            <DataTableCustom
              v-model:pageSize="pagination.pageSize"
              v-model:current-page="pagination.currentPage"
              :headers="headerTable"
              :data="currentPageData"
              :loading="isLoadingTableData"
              :total-items="pagination.totalItems || 0"
              :total-pages="pagination.totalPages || 0"
              :search-field="selectedValue"
              :class="currentPageData.length > 3 ? 'pt-1 h-[264px]' : 'pt-1 '"
              @click-detail="clickDetail"
              @double-clicked="handleRowDoubleClick"
            />
          </div>
        </div>
        <!-- end table URIs -->
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUrlStore } from "@/store";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";

import { useI18n } from "vue-i18n";

const { t } = useI18n();

const props = defineProps({
  scrnId: {
    type: String,
    default: "",
  },
});

const headerTable = computed(() => {
  return [
    {
      title: t("product_platform.screenEntity.url.url"),
      align: "start",
      sortable: false,
      key: "urlAddr",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.url.method"),
      align: "start",
      sortable: false,
      key: "httpMthoCd",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.url.urlName"),
      align: "start",
      sortable: false,
      key: "urlNm",
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
      title: t("product_platform.screenEntity.url.urlDescription"),
      align: "start",
      sortable: false,
      key: "urlDscr",
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
      title: t("product_platform.screenEntity.revisionDate"),
      align: "start",
      sortable: false,
      key: "updDtm",
      class: "header",
    },
  ];
});

const { paginatedItems: currentPageData, pagination } =
  storeToRefs(useUrlStore());
const totalSearchItems = computed(() => {
  return currentPageData.value.length || 0;
});

const isLoadingTableData = ref(false);
const urlStore = useUrlStore();
const selectedValue = ref("name");

const clickDetail = (_item: any) => {
  //
};

const handleRowDoubleClick = (_item: any) => {
  //
};

watch(
  () => props.scrnId,
  async (newValue) => {
    await urlStore.fetchScreenUrlByScrnId(newValue);
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
