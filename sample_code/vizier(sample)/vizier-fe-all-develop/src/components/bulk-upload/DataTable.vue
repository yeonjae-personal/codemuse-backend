<template>
  <div class="h-full w-full">
    <BaseTotalSearchResult
      v-if="isShowTotalSearch"
      class="text-[13px] flex justify-end pt-4 pb-3 table-total-result"
      :total-search="totalSearchItems"
    />
    <div
      v-if="headers.length"
      :class="['data-table w-full relative', tableContainerClass]"
    >
      <LocomotiveComponent
        :key="componentKey"
        :scroll-container-class="['!px-0', scrollContainerClass]"
        top-content-class="z-[1]"
        @scroll-container="handleScrollContainer"
      >
        <template #top-content-fixed>
          <table
            v-if="headers.length"
            ref="tableHeaderRef"
            class="data-table-header w-full"
          >
            <thead class="bg-[#f7f8fa]">
              <TableHeaderGroup v-if="hasChildren" :headers="headers" />
              <TableHeader
                v-else
                :headers="headers"
                :is-dynamic-table="isDynamicTable"
                :is-disabled-tooltip="isDisabledTooltip"
              />
            </thead>
          </table>
        </template>
        <table ref="tableBodyRef" class="data-table-body w-full">
          <tbody>
            <template v-if="filteredData?.length">
              <tr
                v-for="(item, index) in filteredData"
                :key="index"
                :class="[
                  'data-table-body__row d-flex',
                  { 'is-hover': isHoverRow },
                ]"
              >
                <slot name="row" :item="item">
                  <td
                    v-for="header in headers"
                    :key="header.key"
                    :style="{ width: header.width }"
                    :class="[
                      'data-table-body__col',
                      !header.width ? 'flex-1 text-truncate' : '',
                      { 'flex-1': isDynamicTable },
                      { '!flex-grow-0 !w-[80px]': header.key === 'no' },
                    ]"
                  >
                    <slot
                      :name="header.key"
                      :item="item"
                      :width="header.width"
                      :align="header.align"
                    >
                      <div
                        :class="[
                          'value inline-block text-truncate h-[52px] p-4',
                          { '!w-[80px] p-6 py-4': header.key === 'no' },
                        ]"
                        :style="{
                          maxWidth: header.width,
                          textAlign: header.align,
                        }"
                      >
                        <CustomTooltip
                          :content="item[header.key]"
                          :disabled="isDisabledTooltip || header.key === 'no'"
                        >
                          {{ item[header.key] }}
                        </CustomTooltip>
                      </div>
                    </slot>
                  </td>
                </slot>
              </tr>
            </template>
            <tr v-else class="data-table-body__row">
              <td class="data-table-body__col no-data">
                {{ t("product_platform.no_data") }}
              </td>
            </tr>
          </tbody>
        </table>
      </LocomotiveComponent>
      <div v-if="isShowFooter" class="data-table-footer">
        <BasePagination
          v-if="pagination.totalPages > 0"
          :pagination="pagination"
          class-name="!bottom-[13px]"
          :disable-change="disableChange"
          @on-change-page="handleChangePage"
        />
        <div class="d-flex align-center absolute right-6 bottom-[13px]">
          <span class="inline-block mr-[12px] text-[12px] text-text-lighter">
            {{ t("product_platform.itemsPerPage") }}:
          </span>
          <BaseSelectScroll
            v-model="itemsPerPage"
            :height="32"
            class="custom-select-pagination"
            :options="OPTION_ITEMS_PER_PAGE"
            :is-show-tooltip="false"
            :z-index="zIndex"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { OPTION_ITEMS_PER_PAGE } from "@/constants/table";
import type {
  TableColumnFilter,
  TableHeader,
  TableOptionColumnFilter,
} from "@/types/common";

type Props = {
  headers: TableHeader[];
  data: any[];
  pagination?: {
    currentPage: number;
    totalPages: number;
    pageSize: number;
  };
  pageSize?: number;
  className?: string;
  tableClassName?: string;
  isShowFooter?: boolean;
  disableChange?: boolean;
  zIndex?: number;
  totalSearchItems?: number;
  isShowTotalSearch?: boolean;
  isDisabledTooltip?: boolean;
  isHoverRow?: boolean;
  isDynamicTable?: boolean;
};

const props = withDefaults(defineProps<Props>(), {
  headers: () => [] as any[],
  data: () => [] as any[],
  isShowFooter: true,
  disableChange: false,
  pagination: () => ({
    currentPage: 1,
    totalPages: 0,
    pageSize: 10,
  }),
  pageSize: 10,
  className: "",
  tableClassName: "",
  zIndex: 10,
  totalSearchItems: 0,
  isShowTotalSearch: true,
  isDisabledTooltip: false,
  isHoverRow: true,
  isDynamicTable: false,
});

const emit = defineEmits([
  "on-change-page",
  "on-change-size",
  "update:model-value",
  "update:pageSize",
]);

const { t } = useI18n();

const tableBodyRef = ref<HTMLElement | null>(null);
const tableHeaderRef = ref<HTMLElement | null>(null);
const componentKey = ref<number>(0);
const optionFiltered = ref<Record<string, TableOptionColumnFilter[]>>({});

provide("optionFiltered", optionFiltered);

const itemsPerPage = computed<number>({
  get: () => props.pageSize,
  set: (newVal) => {
    emit("update:pageSize", newVal);
    emit("on-change-size", newVal);
  },
});

const hasChildren = computed<boolean>(() =>
  props.headers.some(
    (header) => !!header?.children && header?.children?.length > 0
  )
);

const scrollContainerClass = computed<string>(
  () =>
    `!px-0 ${props.isShowFooter ? "!h-[calc(100%_-_58px)]" : "h-full"} ${
      props.className
    } w-[calc(100%-(-1px))]`
);

const tableContainerClass = computed<string>(
  () =>
    `${props.isShowTotalSearch ? "h-[calc(100%_-_48px)]" : "h-full"} ${
      props.tableClassName
    }`
);

const isFilterData = computed<boolean>(() =>
  props.headers.some(({ filter }) => !!filter)
);

const columnFiltered = computed<TableHeader[]>(() => {
  const uniqueColumn = new Set(props.headers.filter(({ filter }) => filter));
  return [...uniqueColumn];
});

const isCheckedAll = computed<boolean>(() => {
  const allValue = Object.values(optionFiltered.value).flat() || [];
  return allValue.every(({ isChecked }) => isChecked);
});

const filteredData = computed<any[]>(() => {
  if (!isFilterData.value || isCheckedAll.value) {
    return props.data;
  }
  return props.data.filter((item) =>
    columnFiltered.value.every((column) => {
      const filters = optionFiltered.value[column.key] || [];
      const activeValues = filters
        .filter((filter) => filter.isChecked)
        .map((filter) => filter.value);
      return activeValues.includes(item[column.key]);
    })
  );
});

watch(
  () => props.data,
  (value) => {
    if (value.length) {
      const optionsMapper = {};
      columnFiltered.value.forEach((column) => {
        const options = props.data
          .filter((item) => column.key in item)
          .map((item) => item[column.key]);
        const uniqueOptions = new Set(options);
        const mapperOptions: TableColumnFilter[] = [...uniqueOptions].map(
          (value: string) => ({
            isChecked: true,
            name: ["-"].includes(value) ? "null" : value,
            value,
          })
        );
        optionsMapper[column.key] = mapperOptions;
      });
      optionFiltered.value = optionsMapper;
    }
  },
  { deep: true, immediate: true }
);

watch(
  () => props.disableChange,
  (value) => {
    if (value) {
      nextTick(() => {
        componentKey.value++;
      });
    } else {
      nextTick(() => {
        if (!tableBodyRef.value || !tableHeaderRef.value) return;
        tableBodyRef.value.style.width = `${tableHeaderRef.value.clientWidth}px`;
      });
    }
  }
);

const handleChangePage = (page: number): void => {
  emit("on-change-page", page);
};

const handleScrollContainer = (): void => {
  props.headers.forEach((item) => {
    if (item?.isActiveMenu) {
      item.isActiveMenu = false;
    }
  });
  handleResize();
};

const handleResize = (): void => {
  nextTick(() => {
    if (!tableBodyRef.value || !tableHeaderRef.value) return;
    tableBodyRef.value.style.width = `${tableHeaderRef.value.clientWidth}px`;
  });
};

onMounted(() => {
  document.addEventListener("scroll", handleResize);
  window.addEventListener("resize", handleResize);
});

onUnmounted(() => {
  document.removeEventListener("scroll", handleResize);
  window.removeEventListener("resize", handleResize);
});
</script>

<style lang="scss" scoped>
.data-table {
  border: 1px solid #e6e9ed;
  border-radius: 8px;
}

.data-table-header {
  border-collapse: collapse;

  tr {
    border-bottom: 1px solid #e6e9ed;
    border-radius: 8px 8px 0px 0px;
    overflow: hidden;
  }
}

.data-table-body {
  width: 100%;

  &__row {
    width: 100%;
    height: 52px;
    border-bottom: 1px solid #e6e9ed;

    &.is-hover {
      transition: all 0.2s ease;

      &:hover {
        background-color: rgba(0, 0, 0, 0.04);
      }
    }
  }

  &__col {
    font-family: Noto Sans KR;
    font-weight: 400;
    font-size: 13px;
    line-height: 20px;
    letter-spacing: 0.25px;
    color: #3a3b3d;

    &.no-data {
      color: rgb(58 59 61);
      padding-left: 16px;
    }

    & > .value {
      padding: 16px;
    }

    &:first-child {
      & > .value {
        padding: 16px 24px;
      }
    }
  }
}

:deep(.v-skeleton-loader__list-item) {
  height: 20px;
  margin: 0 !important;
}
</style>
