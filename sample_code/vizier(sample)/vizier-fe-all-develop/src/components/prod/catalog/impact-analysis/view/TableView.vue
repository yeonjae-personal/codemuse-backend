<template>
  <div class="w-full !h-[calc(100vh_-_222px)] p-6 pt-0 pb-4">
    <DataTable
      v-model:pageSize="listView.pageSize"
      :headers="tableColumnHeader"
      :data="listView.items"
      :pagination="{
        currentPage: listView.currentPage,
        pageSize: listView.pageSize,
        totalPages: Math.ceil(listView.totalItems / listView.pageSize),
      }"
      :disable-change="loadingPagination"
      :total-search-items="listView.totalItems"
      is-dynamic-table
      @on-change-page="handleChangePage"
      @on-change-size="handleChangePageSize"
    >
      <template #offrCd="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.offrCd">
            <span
              v-html="
                selectedGridNmCd === SearchBy.Code &&
                (gridViewParams.lctgrItemCode === 'O' ||
                  gridViewParams.lctgrItemCode === SPACE)
                  ? highlightText(
                      item?.offrCd || '',
                      gridViewParams?.objCode || ''
                    )
                  : item?.offrCd
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #cmpCd="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.cmpCd">
            <span
              v-html="
                selectedGridNmCd === SearchBy.Code &&
                (gridViewParams.lctgrItemCode === 'C' ||
                  gridViewParams.lctgrItemCode === SPACE)
                  ? highlightText(
                      item?.cmpCd || '',
                      gridViewParams?.objCode || ''
                    )
                  : item?.cmpCd
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #svcCd="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.svcCd">
            <span
              v-html="
                selectedGridNmCd === SearchBy.Code &&
                (gridViewParams.lctgrItemCode === 'R' ||
                  gridViewParams.lctgrItemCode === SPACE)
                  ? highlightText(
                      item?.svcCd || '',
                      gridViewParams?.objCode || ''
                    )
                  : item?.svcCd
              "
            />
          </CustomTooltip>
        </div>
      </template>

      <template #offrNm="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.offrNm">
            <span
              v-html="
                selectedGridNmCd === SearchBy.Name &&
                (gridViewParams.lctgrItemCode === 'O' ||
                  gridViewParams.lctgrItemCode === SPACE)
                  ? highlightText(
                      item?.offrNm || '',
                      gridViewParams?.objName || ''
                    )
                  : item?.offrNm
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #cmpNm="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.cmpNm">
            <span
              v-html="
                selectedGridNmCd === SearchBy.Name &&
                (gridViewParams.lctgrItemCode === 'C' ||
                  gridViewParams.lctgrItemCode === SPACE)
                  ? highlightText(
                      item?.cmpNm || '',
                      gridViewParams?.objName || ''
                    )
                  : item?.cmpNm
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #svcNm="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.svcNm">
            <span
              v-html="
                selectedGridNmCd === SearchBy.Name &&
                (gridViewParams.lctgrItemCode === 'R' ||
                  gridViewParams.lctgrItemCode === SPACE)
                  ? highlightText(
                      item?.svcNm || '',
                      gridViewParams?.objName || ''
                    )
                  : item?.svcNm
              "
            />
          </CustomTooltip>
        </div>
      </template>
    </DataTable>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { useImpactAnalysisStore } from "@/store";
import { highlightText } from "@/utils/format-data";
import type { TableHeader } from "@/types/common";
import { SPACE } from "@/constants/index";
import { SearchBy } from "@/enums";

const { t } = useI18n();
const { setListViewPage, actionGetListView } = useImpactAnalysisStore();
const { listView, loadingPagination, selectedGridNmCd } = storeToRefs(
  useImpactAnalysisStore()
);

const gridViewParams = inject("gridViewParams", {
  lctgrItemCode: SPACE,
  objName: "",
  objCode: "",
});

const tableColumnHeader = computed<TableHeader[]>(() => [
  {
    title: t("product_platform.impactAnalysis.no"),
    align: "start",
    key: "no",
    width: "80px",
  },
  {
    title: t("product_platform.impactAnalysis.offrCd"),
    key: "offrCd",
    width: "140px",
    align: "start",
  },
  {
    title: t("product_platform.impactAnalysis.offrNm"),
    key: "offrNm",
    align: "start",
    width: "240px",
  },
  {
    title: t("product_platform.impactAnalysis.cmpCd"),
    key: "cmpCd",
    align: "start",
    width: "160px",
  },
  {
    title: t("product_platform.impactAnalysis.cmpNm"),
    key: "cmpNm",
    align: "start",
    width: "240px",
  },
  {
    title: t("product_platform.impactAnalysis.cmpValdStrtDtm"),
    key: "cmpValdStrtDtm",
    align: "start",
    width: "120px",
  },
  {
    title: t("product_platform.impactAnalysis.cmpValdEndDtm"),
    key: "cmpValdEndDtm",
    align: "start",
    width: "120px",
  },
  {
    title: t("product_platform.impactAnalysis.svcCd"),
    key: "svcCd",
    align: "start",
    width: "140px",
  },
  {
    title: t("product_platform.impactAnalysis.svcNm"),
    key: "svcNm",
    align: "start",
    width: "240px",
  },
  {
    title: t("product_platform.impactAnalysis.svcValdStrtDtm"),
    key: "svcValdStrtDtm",
    align: "start",
    width: "120px",
  },
  {
    title: t("product_platform.impactAnalysis.svcValdEndDtm"),
    key: "svcValdEndDtm",
    align: "start",
    width: "120px",
  },
]);

onBeforeMount(async () => {
  await actionGetListView();
});

const handleChangePage = async (page: number): Promise<void> => {
  setListViewPage(page, listView.value.pageSize);
  await actionGetListView();
};

const handleChangePageSize = async (size: number): Promise<void> => {
  setListViewPage(1, size);
  await actionGetListView();
};
</script>

<style lang="scss" scoped>
:deep(.table-total-result) {
  padding-top: 6px !important;
}

:deep(.highlight) {
  background-color: yellow !important;
}
</style>
