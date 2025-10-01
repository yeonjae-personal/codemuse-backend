<template>
  <div class="report-container">
    <div class="d-flex justify-between gap-2 items-center mb-3">
      <div class="report-container__title">Execution Report</div>
      <BaseButton
        :color="ButtonColorType.Gray"
        :width="WIDTH_BUTTON.EXCEL"
        @click="onDownloadExcel"
      >
        <DownloadIcon class="mr-[6px]" />
        {{ $t("product_platform.download") }}
      </BaseButton>
    </div>
    <DataTable
      :headers="headers"
      :data="data"
      :is-show-footer="false"
      :is-show-total-search="false"
      class-name="min-h-[522px] max-h-[522px]"
    >
      <template #[`result`]="{ item, width, align }">
        <div class="px-4 py-[14px]" :style="{ width: width, textAlign: align }">
          <span
            v-if="item?.result === 'Success'"
            class="report-container__status--success"
          >
            {{ item?.result }}
          </span>
          <span
            v-if="item?.result === 'Fail'"
            class="report-container__status--fail"
          >
            {{ item?.result }}
          </span>
        </div>
      </template>
    </DataTable>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import type { TableHeader } from "@/types/common";
import { ButtonColorType } from "@/enums";
import { WIDTH_BUTTON } from "@/constants/index";
type Props = {
  data: any[];
  onDownloadExcel: () => void;
};

defineProps<Props>();

const { t } = useI18n();

const headers = ref<TableHeader[]>([
  {
    title: t("product_platform.no"),
    align: "start",
    sortable: false,
    key: "no",
    width: "74px",
  },
  {
    title: t("product_platform.multiEntityDetailData.itemCode"),
    key: "type",
    align: "start",
    sortable: false,
    width: "100px",
  },
  {
    title: t("product_platform.item_name"),
    key: "name",
    align: "start",
    sortable: false,
    width: "130px",
  },
  {
    title: t("product_platform.itemCode"),
    key: "code",
    align: "start",
    sortable: false,
    width: "115px",
  },
  {
    title: "Result",
    key: "result",
    align: "start",
    sortable: false,
    width: "98px",
    filter: true,
    isActiveMenu: false,
  },
  {
    title: t("product_platform.message"),
    key: "message",
    align: "start",
    sortable: false,
    width: "222px",
    filter: true,
    isActiveMenu: false,
  },
]);
</script>
