<template>
  <div
    ref="scrollContent"
    :class="['h-full w-full flex flex-col gap-3', containerClass]"
  >
    <div v-if="listItem?.length > 0" class="flex-1 h-full">
      <LocomotiveComponent
        :style="{ maxHeight: maxHeight }"
        :scroll-content-class="['gap-3 pt-2', scrollContentClass]"
        scroll-container-class="!px-6"
        is-stop-propagation-wheel
      >
        <template v-for="(item, index) in listItem" :key="index">
          <slot name="element" :item="item" :index="index"></slot>
        </template>
      </LocomotiveComponent>
    </div>
    <NoData v-else />
    <BasePagination
      v-if="pagination.totalPages > 0"
      class-name="!relative"
      :pagination="pagination"
      :disable-change="isLoading"
      @on-change-page="onChangePage"
    />
  </div>
</template>

<script setup lang="ts">
import { useWindowSize } from "@vueuse/core";
import debounce from "lodash-es/debounce";
import type { BasePaginationType } from "@/types/common";

const paginationHeight = 32;
const gapHeight = 12;

type Props = {
  listItem: any;
  pagination: BasePaginationType;
  itemHeight: number;
  itemWidth?: number;
  itemPerRow?: number;
  scrollContentClass?: Array<string> | string;
  containerClass?: Array<string> | string;
  isLoading?: boolean;
  onChangePage: (page: number) => Promise<void>;
  onSearch: (page: number, size: number) => Promise<void>;
};

const props = withDefaults(defineProps<Props>(), {
  itemWidth: 0,
  itemPerRow: 0,
  scrollContentClass: "",
  containerClass: "h-full",
  isLoading: false,
  onChangePage: async (_page: number) => {},
});

const { height, width } = useWindowSize();

const scrollContent = ref<HTMLDivElement | null>(null);
const maxHeight = ref<string>("500px");
const totalItem = ref<number>(props.pagination.pageSize);
const pageChange = ref<number>(props.pagination.currentPage);

watch(height, () => {
  calcLocomotiveMaxHeight();
  calcTotalItem();
  if (!props.listItem.length) return;
  searchDebounce(pageChange.value, totalItem.value);
});

watch(width, () => {
  if (!props.itemWidth) return;
  calcTotalItem();
  if (!props.listItem.length) return;
  searchDebounce(pageChange.value, totalItem.value);
});

onMounted(() => {
  calcLocomotiveMaxHeight();
});

const searchDebounce = debounce(props.onSearch, 500);

const calcLocomotiveMaxHeight = (): void => {
  if (!scrollContent.value) return;
  const scrollHeight = scrollContent.value.clientHeight;
  maxHeight.value = `${scrollHeight - paginationHeight - gapHeight}px`;
};

const calcTotalItem = (): void => {
  if (!scrollContent.value) return;
  const scrollHeight = scrollContent.value.clientHeight;
  const scrollWidth = scrollContent.value.clientWidth;
  const itemHeight = props.itemHeight ? props.itemHeight : 0;
  const itemWidth = props.itemWidth ? props.itemWidth : 0;
  const padding = 24;
  const itemPerWidth = Math.max(
    Math.floor(
      (scrollWidth + gapHeight - padding * 2) / (itemWidth + gapHeight)
    ),
    1
  );
  const itemPerRow = props.itemPerRow ? props.itemPerRow : itemPerWidth;
  const itemPerCol = Math.max(
    Math.floor(
      (scrollHeight - paginationHeight - 8) / (itemHeight + gapHeight)
    ),
    1
  );
  totalItem.value = itemPerCol * itemPerRow;
  if (props.pagination.totalItems && props.pagination.totalPages > 0) {
    pageChange.value =
      totalItem.value * props.pagination.currentPage >
      props.pagination.totalItems
        ? Math.ceil(props.pagination.totalItems / totalItem.value)
        : props.pagination.currentPage;
  }
};

defineExpose({ totalItem, pageChange, calcTotalItem });
</script>
