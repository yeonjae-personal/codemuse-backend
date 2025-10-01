<template>
  <div class="h-full">
    <v-data-table-virtual
      :headers="props.headers"
      fixed-header
      :items="computedData"
      :items-length="props.totalItems || 0"
      item-value="name"
      class="custom-table !text-text-base !font-size-base h-full"
      density="default"
      :row-height="40"
      last-icon=""
      first-icon=""
      hide-default-footer
      :loading="props.loading"
      hover
      @click:row="handleRowClick"
      @dblclick:row="handleRowDoubleClick"
    >
      <template #no-data>
        <div class="h-full w-full justify-center items-center !font-size-base">
          <no-data />
        </div>
      </template>
      <template #item="{ item }">
        <slot name="item" :item="item"> </slot>
      </template>
    </v-data-table-virtual>
    <div
      v-if="props.isShowPagination"
      class="flex justify-end h-[58px] !mt-0 !mx-[24px] !mb-[8px] flex-wrap"
      :class="classPagination"
    >
      <div class="flex align-center">
        <span class="inline-block mr-[12px] text-[12px] text-text-lighter"
          >{{ $t("product_platform.itemsPerPage") }}:</span
        >
        <BaseSelect
          v-model="itemsPerPage"
          density="compact"
          height="32px"
          list-item-height="32px"
          item-list-font-size="12px"
          class="table-select-box h-[32px] mr-[24px]"
          :items="OPTION_ITEMS_PER_PAGE"
          :item-title="'title'"
          :item-value="'value'"
          :menu-props="{
            contentClass: 'base-select-content select-page-size',
          }"
        />
        <span v-if="totalItems > 0" class="text-[12px] text-text-lighter"
          >{{ startRange }}-{{ endRange }} of {{ totalItems }}</span
        >
        <span v-else class="text-[12px] text-text-lighter">0-0 of 0</span>
      </div>
      <div class="flex align-center justify-space-between w-[80px]">
        <button
          class="w-[36px] h-[36px] disabled:text-text-lightest"
          :disabled="disabledDecreaseBtn"
          @click="
            () => {
              if (loading) return;
              localCurrentPage--;
            }
          "
        >
          <v-icon size="20">mdi-chevron-left</v-icon>
        </button>
        <button
          class="w-[36px] h-[36px] disabled:text-text-lightest"
          :disabled="disabledIncreaseBtn"
          @click="
            () => {
              if (loading) return;
              localCurrentPage++;
            }
          "
        >
          <v-icon size="20">mdi-chevron-right</v-icon>
        </button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { OPTION_ITEMS_PER_PAGE } from "@/constants/table";

const emit = defineEmits([
  "updateTable",
  "clickDetail",
  "update:pageSize",
  "update:currentPage",
  "doubleClicked",
]);

const props = defineProps({
  headers: {
    type: Array,
    default: () => {},
  },
  data: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
  totalItems: {
    type: Number,
    default: 0,
  },
  totalPages: {
    type: Number,
    default: 0,
  },
  currentPage: {
    type: Number,
    default: () => 1,
  },
  pageSize: {
    type: Number,
    default: 10,
  },
  classPagination: {
    type: String,
    default: "",
  },
  isShowPagination: {
    type: Boolean,
    default: true,
  },
});

const idSelected = ref(null);

const itemsPerPage = computed({
  get() {
    return props.pageSize;
  },
  set(newVal) {
    emit("update:pageSize", newVal);
  },
});

const localCurrentPage = computed({
  get() {
    return props.currentPage;
  },
  set(newVal) {
    emit("update:currentPage", newVal);
    updateTable();
  },
});

watch(itemsPerPage, () => {
  localCurrentPage.value = 1;
});

const startRange = computed(() => {
  return (localCurrentPage.value - 1) * itemsPerPage.value + 1;
});

const endRange = computed(() => {
  let end =
    (localCurrentPage.value - 1) * itemsPerPage.value + itemsPerPage.value;
  return props.totalItems < end ? props.totalItems : end;
});

const disabledDecreaseBtn = computed(() => {
  return localCurrentPage.value == 1;
});
const disabledIncreaseBtn = computed(() => {
  return localCurrentPage.value >= props.totalPages;
});

const computedData = computed(() => {
  return props.data?.map((item: any, index: number) => {
    return Object.keys(item).reduce(
      (acc, key) => {
        acc[key as string] =
          item[key as string] === null || item[key as string] === undefined
            ? "-"
            : item[key as string];
        return acc;
      },
      {
        ...item,
        no: (localCurrentPage.value - 1) * itemsPerPage.value + index + 1,
      }
    );
  });
});

const calcHeightItemFound = computed(() =>
  computedData.value.length > 0 ? "auto" : "100%"
);

const updateTable = () => {
  emit("updateTable");
};

const handleRowClick = (_item, row) => {
  idSelected.value = row.item;
  emit("clickDetail", row.item);
};

const handleRowDoubleClick = (_item, row) => {
  idSelected.value = row.item;
  emit("doubleClicked", row.item);
};
</script>

<style scoped>
.custom-table {
  font-size: 13px !important;
}
:deep(.v-table__wrapper::-webkit-scrollbar) {
  height: 6px;
  width: 6px;
}

:deep(.v-table__wrapper::-webkit-scrollbar-track) {
  background: #e6e9ed;
}

:deep(.v-table__wrapper::-webkit-scrollbar-thumb) {
  background: #bdc1c7;
  height: 4px;
  border-radius: 8px;
}

:deep(.v-table__wrapper::-webkit-scrollbar-thumb:hover) {
  background: #bdc1c7;
  border-radius: 8px;
}
:deep(.highlight) {
  background-color: yellow;
}
:deep(.v-data-table__thead > tr > th) {
  background-color: #f7f8fa !important;
  font-weight: 500 !important;
}
.custom-table {
  overflow-x: auto;
  white-space: nowrap;
}
.custom-table th,
.custom-table td {
  white-space: nowrap;
}
:deep() .v-data-table-footer {
  font-size: 12px !important;
  width: 100% !important;
  justify-content: flex-end !important;
  padding-left: 24px;
  padding-top: 12px !important;
  padding-bottom: 0 !important;
}
:deep(.v-data-table__tr):hover {
  color: #ba1642;
  background-color: #fff0f2;
  cursor: pointer;
}
:deep(.v-table) {
  height: 100% !important;
}
:deep(.v-data-table-footer__info) {
  color: #6b6d70 !important;
}
:deep().v-data-table-footer__items-per-page > span {
  font-size: 11px;
  padding-inline-end: 12px !important;
}
:deep() .v-field {
  height: 32px;
  display: flex;
  align-items: center;
  width: 59px;
}
:deep() .v-field__input {
  font-size: 11px;
  font-weight: 400;
  padding-inline-start: 12px;
  padding-left: 12px;
}
:deep()
  .v-data-table-footer__items-per-page
  .v-input
  .v-input__control
  .v-field {
  border: 1px solid #e6e9ed;
  outline: none;
  border-radius: 4px;
}
:deep() .v-field .v-field__append-inner .v-icon {
  height: 16px;
  width: 16px;
  min-width: 16px;
}
:deep().v-data-table-footer__items-per-page > .v-select {
  width: unset;
}
:deep().v-data-table-footer__info {
  min-width: unset;
  padding: 0px 24px;
  font-size: 11px;
  line-height: 16.5px;
}
:deep().v-pagination__prev button .v-btn__content .v-icon,
:deep().v-pagination__next button .v-btn__content .v-icon {
  width: 20px;
  height: 20px;
  min-width: 20px;
}
:deep().v-pagination__prev,
:deep().v-pagination__next {
  width: 36px;
  height: 36px;
}
.custom-table.v-list-item-title {
  font-size: 11px;
}
:deep() table {
  height: v-bind(calcHeightItemFound);
}

:deep().w-full.flex.justify-center.items-center.flex-wrap {
  height: 100%;
}

:deep(.v-data-table__thead > tr > th) {
  background-color: #f7f8fa !important;
}

:deep(
    .v-table.v-table--fixed-header > .v-table__wrapper > table > thead > tr > th
  ) {
  background-color: #f7f8fa !important;
}

:deep(.selected-row) {
  background-color: #fff0f2 !important;
}
:deep(.v-table__wrapper) {
  overflow-x: hidden !important;
}
</style>
