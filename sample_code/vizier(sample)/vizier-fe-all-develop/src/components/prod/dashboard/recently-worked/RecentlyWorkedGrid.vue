<template>
  <DataTable
    v-model:pageSize="pageSize"
    table-class-name="!h-[647px]"
    :headers="headerColumns"
    :data="subscriberData"
    :pagination="{
      pageSize,
      currentPage: currentPageNum,
      totalPages: Math.ceil(totalItems / pageSize),
    }"
    :total-search-items="totalItems"
    :disable-change="isLoading"
    :z-index="9999"
    @on-change-page="handleChangePage"
  >
    <template #[`offerType`]="{ item, width, align }">
      <div :style="{ width: width, textAlign: align }" class="p-4">
        <CustomTooltip :content="OFFER_TYPES[item?.offerType]" />
      </div>
    </template>
    <template #itemName="{ item, align, width }">
      <div :style="{ width: width, textAlign: align }" class="p-4">
        <CustomTooltip :content="item?.itemName">
          <span
            v-html="
              offerName?.length > 0 && searchBy === 'name'
                ? highlightText(item?.itemName || '', offerName)
                : item?.itemName
            "
          />
        </CustomTooltip>
      </div>
    </template>
    <template #itemCode="{ item, width, align }">
      <div :style="{ width: width, textAlign: align }" class="p-4">
        <CustomTooltip :content="item?.itemCode">
          <span
            v-html="
              offerName?.length > 0 && searchBy === 'code'
                ? highlightText(item?.itemCode || '', offerName)
                : item?.itemCode
            "
          />
        </CustomTooltip>
      </div>
    </template>
    <template #work="{ item, align, width }">
      <div class="py-3" :style="{ width: width, textAlign: align }">
        <v-chip
          :color="
            item.workCode === '01'
              ? '#1570EF'
              : item.workCode === '04'
                ? '#6B6D70'
                : '#E04F16'
          "
          :text="item.work"
          size="small"
          label
        />
      </div>
    </template>
  </DataTable>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { OFFER_TYPES } from "@/constants/dashboard";
import { highlightText } from "@/utils/format-data";
import type { TableHeader } from "@/types/common";

const { t } = useI18n();
const props = defineProps({
  subscriberData: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
  searchBy: { type: String, default: "" },
  offerName: { type: String, default: "" },
  currentPage: { type: Number, default: 0 },
  totalItems: { type: Number, default: 0 },
  isLoading: { type: Boolean, default: false },
});
const emit = defineEmits(["pageSize", "currentPage"]);

const pageSize = ref<number>(10);
const currentPageNum = ref<number>(props.currentPage);

const headerColumns = ref<TableHeader[]>([
  {
    title: t("product_platform.dashboard.category"),
    align: "start",
    key: "category",
    width: "150px",
  },
  {
    title: t("product_platform.dashboard.type"),
    key: "type",
    align: "start",
    width: "150px",
  },
  {
    title: t("product_platform.dashboard.itemName"),
    key: "itemName",
    align: "start",
    width: "250px",
  },
  {
    title: t("product_platform.dashboard.itemCode"),
    key: "itemCode",
    align: "start",
    width: "150px",
  },
  {
    title: t("product_platform.dashboard.work"),
    key: "work",
    align: "start",
    width: "160px",
  },
  {
    title: t("product_platform.dashboard.responsibleDept"),
    key: "responsibleDept",
    align: "start",
    width: "160px",
  },
  {
    title: t("product_platform.dashboard.responsibleUser"),
    key: "responsibleUser",
    align: "start",
    width: "150px",
  },
  {
    title: t("product_platform.dashboard.dateTime"),
    key: "dateTime",
    align: "start",
    width: "150px",
  },
]);

const handleChangePage = (page) => {
  currentPageNum.value = page;
};

watch(pageSize, (newValue) => {
  emit("pageSize", newValue);
  currentPageNum.value = 1;
});

watch(currentPageNum, (newValue) => {
  emit("currentPage", newValue);
});

watch(
  () => props.currentPage,
  (newValue) => {
    currentPageNum.value = newValue;
  }
);
</script>

<style lang="scss" scoped>
:deep(.highlight) {
  background-color: yellow;
}
</style>
