<template>
  <div class="w-full !h-[calc(100%_-_54px)] p-6 pt-0">
    <DataTable
      v-model:pageSize="tableData.itemsPerPage"
      :headers="tableHeaders"
      :data="tableData.items"
      :pagination="{
        currentPage: tableData.currentPage,
        pageSize: tableData.itemsPerPage,
        totalPages: Math.ceil(tableData.totalItems / tableData.itemsPerPage),
      }"
      :disable-change="loadingPagination"
      :total-search-items="tableData.totalItems"
      @on-change-page="handleChangePage"
    >
      <template #level1="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.level1">
            <span
              v-html="
                highlightText(
                  item.level1,
                  categoryStore.getSearchListParams.ctgrNodeName
                )
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #level2="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.level2">
            <span
              v-html="
                highlightText(
                  item.level2,
                  categoryStore.getSearchListParams.ctgrNodeName
                )
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #level3="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.level3">
            <span
              v-html="
                highlightText(
                  item.level3,
                  categoryStore.getSearchListParams.ctgrNodeName
                )
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #level4="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.level4">
            <span
              v-html="
                highlightText(
                  item.level4,
                  categoryStore.getSearchListParams.ctgrNodeName
                )
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #level5="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.level5">
            <span
              v-html="
                highlightText(
                  item.level5,
                  categoryStore.getSearchListParams.ctgrNodeName
                )
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #offerCd="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.offerCd">
            <span
              v-html="
                highlightText(
                  item.offerCd,
                  categoryStore.getSearchListParams.offerCd
                )
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #offerNm="{ item, align }">
        <div :style="{ width: calculateWidth(), textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.offerNm">
            <span
              v-html="
                highlightText(
                  item.offerNm,
                  categoryStore.getSearchListParams.offerNm
                )
              "
            />
          </CustomTooltip>
        </div>
      </template>
    </DataTable>
  </div>
</template>

<script setup lang="ts">
import { useCategoryStore } from "@/store";
import { CATEGORY_VIEW_MODE } from "@/constants/";
import { useI18n } from "vue-i18n";
import { TableHeader } from "@/types/common";

const { t } = useI18n();
const props = defineProps({
  tab: {
    type: String,
    default: "",
    require: true,
  },
});

const categoryStore = useCategoryStore();
const { loadingPagination } = storeToRefs(useCategoryStore());

const tableData = ref({
  itemsPerPage: 10,
  currentPage: 1,
  totalItems: 0,
  pageSize: 1,
  optionItemPerPage: [],
  items: [],
});

const tableHeaders = computed<TableHeader[]>(() => [
  { key: "no", title: t("product_platform.no"), width: "80px", align: "start" },
  {
    key: "level1",
    title: t("product_platform.level1"),
    align: "start",
    width: "190px",
  },
  {
    key: "level2",
    title: t("product_platform.level2"),
    align: "start",
    width: "190px",
  },
  {
    key: "level3",
    title: t("product_platform.level3"),
    align: "start",
    width: "190px",
  },
  {
    key: "level4",
    title: t("product_platform.level4"),
    align: "start",
    width: "190px",
  },
  {
    key: "level5",
    title: t("product_platform.level5"),
    align: "start",
    width: "190px",
  },
  {
    key: "offerCd",
    title: t("product_platform.offerCode"),
    align: "start",
    width: "190px",
  },
  {
    key: "offerNm",
    title: t("product_platform.offerName"),
    align: "start",
  },
]);

watch(
  () => tableData.value.itemsPerPage,
  () => {
    categoryStore.setSearchOfferPageSize(1, tableData.value.itemsPerPage);
    fetchTableData();
  }
);

watch(
  () => categoryStore.getSearchStatus,
  (val) => {
    if (
      val &&
      props.tab === categoryStore.getCategoryCurrentTab &&
      categoryStore.getCategoryView === CATEGORY_VIEW_MODE.LIST
    ) {
      fetchTableData();
      nextTick(() => {
        categoryStore.setSearchStatus(false);
      });
    }
  }
);

onBeforeMount(() => {
  fetchTableData();
});

onMounted(() => {
  window.addEventListener("resize", calculateWidth);
});

onUnmounted(() => {
  window.removeEventListener("resize", calculateWidth);
});

const fetchTableData = async () => {
  await categoryStore.getListOffer();
  const data = categoryStore.getListData;
  tableData.value.itemsPerPage = data.size;
  tableData.value.currentPage = data.page;
  tableData.value.totalItems = data.totalElements;
  tableData.value.pageSize = data.totalPages;
  tableData.value.optionItemPerPage = data.customRowSize?.map((item) => ({
    cmcdDetlId: item,
    cmcdDetlNm: `${item}`,
  }));
  tableData.value.items = data.elements.map((item, index) => ({
    ...item,
    no:
      (categoryStore.getSearchListParams.page - 1) *
        categoryStore.getSearchListParams.size +
      index +
      1,
  }));
};

const handleChangePage = (page) => {
  categoryStore.setSearchOfferPageSize(page, tableData.value.itemsPerPage);
  fetchTableData();
};

const highlightText = (text, search) => {
  if (!search || !text) return text;
  // eslint-disable-next-line security/detect-non-literal-regexp
  const regex = new RegExp(`(${search})`, "gi");
  return text.replace(regex, '<span class="bg-yellow">$1</span>');
};

const calculateWidth = (): string => {
  const element = document.getElementById("offerNm");
  if (!element) return "auto";
  return `${element.clientWidth - 2}px`;
};
</script>

<style scoped lang="scss">
:deep(.highlight) {
  background-color: yellow;
}

:deep(.table-total-result) {
  padding-top: 0 !important;
}
</style>
