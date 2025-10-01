<template>
  <div
    :class="[
      'flex justify-center pagination mx-auto absolute bottom-0 left-1/2 transform -translate-x-1/2 w-full',
      className,
    ]"
  >
    <div class="flex align-center justify-space-between">
      <button
        class="flex justify-center align-center w-[32px] h-[32px]"
        :disabled="disabledDecreaseBtn || disableChange"
        @click="handlePageBackArrow()"
      >
        <ChevronRightIcon
          size="18"
          class="rotate-180"
          :disable="pagination.currentPage == 1"
        />
      </button>
      <button
        v-for="(pageNumber, index) in paginationNumberList"
        :key="index"
        :class="[
          'flex justify-center items-center w-[32px]',
          'h-[32px]',
          'text-[13px]',
          'btn__pagination-number',
          {
            'pagination_current-page':
              paginationNumberList?.[index] == pagination.currentPage,
          },
        ]"
        :disabled="disableChange"
        @click="setSelectPagination(pageNumber, index)"
      >
        {{ pageNumber }}
      </button>
      <button
        class="flex justify-center align-center w-[32px] h-[32px]"
        :disabled="disabledIncreaseBtn || disableChange"
        @click="handlePageNextArrow()"
      >
        <ChevronRightIcon
          size="18"
          :disable="pagination.currentPage == pagination.totalPages"
        />
      </button>
    </div>
  </div>
</template>

<!-- eslint-disable vue/no-mutating-props -->
<script setup lang="ts">
import { BasePaginationType } from "@/types/common";

const props = defineProps({
  pagination: {
    type: Object as () => BasePaginationType,
    required: true,
    default: () => ({
      currentPage: 1,
      totalPages: 1,
      pageSize: 10,
    }),
  },
  totalVisible: {
    type: Number,
    default: 5,
  },
  className: {
    type: String,
    default: "",
  },
  disableChange: {
    type: Boolean,
    default: false,
  },
});

const selectPagination = ref({
  iCheck: false,
  index: 0,
  value: 1,
  newListData: [] as any,
});

// Define emits
const emit = defineEmits(["on-change-page", "update:model-value"]);

const disabledDecreaseBtn = computed(() => {
  return props.pagination.currentPage <= 1;
});

const disabledIncreaseBtn = computed(() => {
  return props.pagination.currentPage >= props.pagination.totalPages;
});

const handlePageBackArrow = () => {
  if (props.pagination.currentPage <= 1) return;
  if (props.pagination.currentPage == paginationNumberList?.value?.[0]) {
    props.pagination.currentPage--;
    selectPagination.value.newListData = paginationNumberList?.value?.map(
      (i) => i - 1
    );
  } else {
    props.pagination.currentPage--;
    emit("on-change-page", props.pagination.currentPage);
  }
  selectPagination.value.iCheck = true;
};

const handlePageNextArrow = () => {
  if (props.pagination.currentPage >= props.pagination.totalPages) return;
  props.pagination.currentPage++;
  emit("on-change-page", props.pagination.currentPage);
  if (
    props.pagination.currentPage == paginationNumberList?.value?.[5] &&
    props.pagination.currentPage <= props.pagination.totalPages - 1
  ) {
    selectPagination.value.newListData = paginationNumberList?.value?.map(
      (i) => i + 1
    );
  }
  selectPagination.value.iCheck = true;
};

const setSelectPagination = (pageNumber, index) => {
  emit("on-change-page", pageNumber);
  selectPagination.value.index = index;
  selectPagination.value.value = pageNumber;
  selectPagination.value.iCheck = true;
};

const paginationNumberList = computed(() => {
  const currentPage = props.pagination.currentPage;
  const totalPage = props.pagination.totalPages;
  if (
    selectPagination.value.iCheck == true &&
    selectPagination.value.newListData.length > 0
  ) {
    selectPagination.value.iCheck = false;
    return selectPagination.value.newListData;
  }
  if (totalPage > 6) {
    if (currentPage > totalPage - 3) {
      return [
        totalPage - 5,
        totalPage - 4,
        totalPage - 3,
        totalPage - 2,
        totalPage - 1,
        totalPage,
      ];
    } else if (currentPage > 2 && currentPage <= totalPage - 3) {
      return [
        currentPage - 2,
        currentPage - 1,
        currentPage,
        currentPage + 1,
        currentPage + 2,
        currentPage + 3,
      ];
    } else {
      return Array.from({ length: 6 }, (_, i) => i + 1);
    }
  }

  if (props.pagination.totalPages <= 6)
    return Array.from({ length: props.pagination.totalPages }, (_, i) => i + 1);
});
</script>

<style scoped lang="scss">
:deep().v-pagination__item,
:deep().v-pagination__prev,
:deep() .v-pagination__next {
  width: 32px;
  height: 32px;
  color: #6b6d70;
  margin: 0px;
  display: flex;
  align-items: center;
}
:deep().v-pagination .v-btn {
  width: 32px;
  height: inherit !important;
  border-radius: 8px !important;
}
:deep().v-pagination__item--is-active .v-btn__overlay {
  background-color: transparent !important;
}

:deep().v-pagination__item--is-active {
  background-color: #fee5e7 !important;
  border: 1px solid #d9325a !important;
  border-radius: 8px !important;
  color: #d9325a !important;
}

:deep(

  )button.v-btn.v-btn--icon.v-theme--light.v-btn--density-default.v-btn--size-default.v-btn--variant-text {
  height: 32px;
}

:deep().v-btn__content {
  font-size: 13px;
}

button {
  color: #6b6d70;
}

button[disabled] {
  color: #bdc1c7;
}
</style>
