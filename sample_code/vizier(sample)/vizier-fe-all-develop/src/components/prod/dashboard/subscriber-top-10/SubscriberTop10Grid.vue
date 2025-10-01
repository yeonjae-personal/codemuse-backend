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
    <template #offerCode="{ item, width, align }">
      <div :style="{ width: width, textAlign: align }" class="p-4">
        <CustomTooltip :content="item?.offerCode">
          <span
            v-html="
              offerName?.length > 0 && searchBy === 'code'
                ? highlightText(item?.offerCode || '', offerName)
                : item?.offerCode
            "
          />
        </CustomTooltip>
      </div>
    </template>
    <template #offerName="{ item, width, align }">
      <div :style="{ width: width, textAlign: align }" class="p-4">
        <CustomTooltip :content="item?.offerName">
          <span
            v-html="
              offerName?.length > 0 && searchBy === 'name'
                ? highlightText(item?.offerName || '', offerName)
                : item?.offerName
            "
          />
        </CustomTooltip>
      </div>
    </template>
    <template #[`offerType`]="{ item, width, align }">
      <div :style="{ width: width, textAlign: align }" class="p-4">
        <CustomTooltip :content="OFFER_TYPES[item?.offerType]" />
      </div>
    </template>
    <template #status="{ item, align, width }">
      <div class="py-3" :style="{ width: width, textAlign: align }">
        <v-chip
          :color="!!item.status ? '#C7291D' : '#1570EF'"
          :text="
            !!item.status
              ? t('product_platform.dashboard.endOfSale')
              : t('product_platform.dashboard.onSale')
          "
          class="text-uppercase"
          size="small"
          label
        />
      </div>
    </template>
    <template #subscriber="{ item, align, width }">
      <div :style="{ width: width, textAlign: align }" class="p-4">
        <CustomTooltip :content="formatSubscribers(item?.subscriber)" />
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
const currentPageNum = ref<number>(1);

const headerColumns = ref<TableHeader[]>([
  {
    title: t("product_platform.dashboard.offerCode"),
    align: "start",
    key: "offerCode",
    width: "150px",
  },
  {
    title: t("product_platform.dashboard.offerType"),
    align: "start",
    key: "offerType",
    width: "150px",
  },
  {
    title: t("product_platform.dashboard.offerName"),
    key: "offerName",
    align: "start",
    width: "220px",
  },
  {
    title: t("product_platform.dashboard.noOfSubscriber"),
    key: "subscriber",
    align: "start",
    width: "200px",
  },
  {
    title: t("product_platform.dashboard.status"),
    key: "status",
    align: "start",
    width: "150px",
  },
  {
    title: t("product_platform.dashboard.startDate"),
    key: "startDate",
    align: "center",
    width: "150px",
  },
  {
    title: t("product_platform.dashboard.endDate"),
    key: "endDate",
    align: "center",
    width: "150px",
  },
  {
    title: t("product_platform.dashboard.durationDay"), //"Duration (days)"
    key: "duration",
    align: "end",
    width: "150px",
  },
]);

const handleChangePage = (page) => {
  currentPageNum.value = page;
};

const formatSubscribers = (subscriber) =>
  // eslint-disable-next-line security/detect-unsafe-regex
  subscriber?.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") ?? "";

watch(currentPageNum, (newValue) => {
  emit("currentPage", newValue);
});

watch(pageSize, (newValue) => {
  emit("pageSize", newValue);
  currentPageNum.value = 1;
});

watch(
  () => props.currentPage,
  (newValue) => {
    currentPageNum.value = newValue;
  }
);
</script>

<style scoped lang="scss">
:deep(.highlight) {
  background-color: yellow;
}
</style>
