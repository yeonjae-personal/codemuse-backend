<template>
  <div class="!h-[calc(100vh_-_235px)]">
    <DataTable
      v-model:pageSize="listView.pageSize"
      :headers="tableColumnHeader"
      :data="listView.items"
      :pagination="{
        currentPage: listView.currentPage,
        pageSize: listView.pageSize,
        totalPages: Math.ceil(listView.totalItems / listView.pageSize),
      }"
      :disable-change="isLoading"
      :total-search-items="listView.totalItems"
      @on-change-page="handleChangePage"
      @on-change-size="handleChangePageSize"
    >
      <template #leaderCode="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.leaderCode">
            <span
              v-html="
                gridViewParams.type === 'code' &&
                ((gridViewParams.category === 'offer' &&
                  item.largeItemCode === 'O') ||
                  (gridViewParams.category === 'group' &&
                    item.largeItemCode === 'G') ||
                  gridViewParams.category === SPACE)
                  ? highlightText(item?.leaderCode || '', gridViewParams.value)
                  : item?.leaderCode
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #leaderName="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.leaderName">
            <span
              v-html="
                gridViewParams.type === 'name' &&
                ((gridViewParams.category === 'offer' &&
                  item.largeItemCode === 'O') ||
                  (gridViewParams.category === 'group' &&
                    item.largeItemCode === 'G') ||
                  gridViewParams.category === SPACE)
                  ? highlightText(item?.leaderName || '', gridViewParams.value)
                  : item?.leaderName
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #followerCode="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.followerCode">
            <span
              v-html="
                gridViewParams.type === 'code' &&
                (gridViewParams.category === 'offer' ||
                  gridViewParams.category === SPACE)
                  ? highlightText(
                      item?.followerCode || '',
                      gridViewParams.value
                    )
                  : item?.followerCode
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #followerName="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.followerName">
            <span
              v-html="
                gridViewParams.type === 'name' &&
                (gridViewParams.category === 'offer' ||
                  gridViewParams.category === SPACE)
                  ? highlightText(
                      item?.followerName || '',
                      gridViewParams.value
                    )
                  : item?.followerName
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #relationCode="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.relationCode">
            <span
              v-html="
                gridViewParams.type === 'code' &&
                (gridViewParams.category === 'relation' ||
                  gridViewParams.category === SPACE)
                  ? highlightText(
                      item?.relationCode || '',
                      gridViewParams.value
                    )
                  : item?.relationCode
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #relationName="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.relationName">
            <span
              v-html="
                gridViewParams.type === 'name' &&
                (gridViewParams.category === 'relation' ||
                  gridViewParams.category === SPACE)
                  ? highlightText(
                      item?.relationName || '',
                      gridViewParams.value
                    )
                  : item?.relationName
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #groupCode="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.groupCode">
            <span
              v-html="
                gridViewParams.type === 'code' &&
                (gridViewParams.category === 'group' ||
                  gridViewParams.category === SPACE)
                  ? highlightText(item?.groupCode || '', gridViewParams.value)
                  : item?.groupCode
              "
            />
          </CustomTooltip>
        </div>
      </template>
      <template #groupName="{ item, align, width }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.groupName">
            <span
              v-html="
                gridViewParams.type === 'name' &&
                (gridViewParams.category === 'group' ||
                  gridViewParams.category === SPACE)
                  ? highlightText(item?.groupName || '', gridViewParams.value)
                  : item?.groupName
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
import { useExtendManagerStore } from "@/store";
import { highlightText } from "@/utils/format-data";
import type { TableHeader } from "@/types/common";
import { SPACE } from "@/constants/index";

const { paramListView, listView, selectedItem, isLoading } = storeToRefs(
  useExtendManagerStore()
);
const { getRelationDataTable } = useExtendManagerStore();

const gridViewParams = inject("gridViewParams", {
  category: SPACE,
  value: "",
  type: "name",
});

const { t } = useI18n();

const tableColumnHeader = computed<TableHeader[]>(() => [
  {
    title: t("product_platform.impactAnalysis.no"),
    align: "start",
    key: "no",
    width: "80px",
  },
  {
    title: t("product_platform.leader_code"),
    key: "leaderCode",
    width: "160px",
    align: "start",
  },
  {
    title: t("product_platform.leader_name"),
    key: "leaderName",
    align: "start",
    width: "200px",
  },
  {
    title: t("product_platform.follower_code"),
    key: "followerCode",
    align: "start",
    width: "160px",
  },
  {
    title: t("product_platform.follower_name"),
    key: "followerName",
    align: "start",
    width: "200px",
  },
  {
    title: t("product_platform.relation_code"),
    key: "relationCode",
    align: "start",
    width: "160px",
  },
  {
    title: t("product_platform.relation_name"),
    key: "relationName",
    align: "start",
    width: "200px",
  },
  {
    title: t("product_platform.relation_start_date"),
    key: "relationStartDate",
    align: "start",
    width: "200px",
  },
  {
    title: t("product_platform.relation_end_date"),
    key: "relationEndDate",
    align: "start",
    width: "200px",
  },
  {
    title: t("product_platform.group_code"),
    key: "groupCode",
    align: "start",
    width: "160px",
  },
  {
    title: t("product_platform.group_name"),
    key: "groupName",
    align: "start",
    width: "200px",
  },
  {
    title: t("product_platform.group_start_date"),
    key: "groupStartDate",
    align: "start",
    width: "200px",
  },
  {
    title: t("product_platform.group_end_date"),
    key: "groupEndDate",
    align: "start",
    width: "200px",
  },
]);

const handleFetchDataTable = async () // isMounted: boolean = false
: Promise<void> => {
  if (!selectedItem.value || !selectedItem.value.prodUuid) return;
  await getRelationDataTable();
};

const handleChangePage = async (page: number): Promise<void> => {
  paramListView.value.page = page;
  await handleFetchDataTable();
};

const handleChangePageSize = async (size: number): Promise<void> => {
  paramListView.value.page = 1;
  paramListView.value.size = size;
  await getRelationDataTable();
};
</script>

<style scoped lang="scss">
:deep(.highlight) {
  background-color: yellow;
}
</style>
