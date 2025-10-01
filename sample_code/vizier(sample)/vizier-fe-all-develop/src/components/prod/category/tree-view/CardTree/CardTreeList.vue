<!-- eslint-disable vue/no-mutating-props -->
<template>
  <div
    v-if="props.offerList?.elements"
    class="flex flex-column flex-grow-1 offer-search"
  >
    <template v-if="props.offerList.elements.length">
      <LocomotiveComponent scroll-content-class="pt-[4px]">
        <VueDraggable
          v-model="props.offerList.elements"
          class="flex flex-column gap-[12px]"
        >
          <template
            v-for="offer in props.offerList.elements"
            :key="offer.prodCd"
          >
            <CardTreeListItem
              :offer="offer"
              :draggable="isDraggable"
              :active="offer.prodUuid === selectedOffer?.prodUuid"
              :style="{ 'z-index': 9999 }"
              @click.stop="setSelectedOfferItem(offer)"
              @dragstart="
                handleDragUserPocket($event, {
                  userPocketType: LARGE_ITEM_CODE.OFFER,
                  ...offer,
                })
              "
            />
          </template>
        </VueDraggable>
      </LocomotiveComponent>
      <div class="mt-[auto] py-[8px] px-[32px] relative">
        <BasePagination
          v-if="pagination.totalPages > 0"
          :pagination="pagination"
          class-name="mt-5 mb-5"
          @on-change-page="handleChangePage"
        />
      </div>
    </template>
    <template v-else>
      <NoData />
    </template>
  </div>
  <div v-else class="offer-search">
    <LocomotiveComponent scroll-content-class="pt-[4px]">
      <div class="flex flex-column gap-[12px]">
        <template
          v-for="offer in offerListModify[offerListPaging.currentPage]"
          :key="offer.prodCd"
        >
          <CardTreeListItem
            :offer="offer"
            :draggable="isDraggable"
            :active="offer.prodUuid === selectedOffer?.prodUuid"
            @click.stop="
              () => {
                selectedOffer = offer;
              }
            "
          />
        </template>
      </div>
    </LocomotiveComponent>
    <div
      v-if="offerListPaging.totalPages > 0"
      class="mt-[auto] px-[32px] relative"
    >
      <BasePagination
        :pagination="offerListPaging"
        class-name="mt-5 mb-5"
        @on-change-page="handleChangeOfferPage"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import chunk from "lodash-es/chunk";
import cloneDeep from "lodash-es/cloneDeep";
import { VueDraggable } from "vue-draggable-plus";
import useCategoryStore from "@/store/category.store";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";

const props = defineProps({
  offerList: {
    type: Object as PropType<any>,
    default: () => {},
  },
});

const categoryStore = useCategoryStore();
const { handleDragUserPocket } = useDragUserPocket();

const offerListPaging = ref({
  currentPage: 1,
  totalPages: 0,
});
const offerListModify = ref();
const selectedOffer = ref<any>(null);
const emit = defineEmits(["resetOfferListActiveStatus", "changePageNo"]);

const isDraggable = computed(() => categoryStore.getIsEdit);
const pagination = computed(() => {
  const { totalPages, size, totalElements, page } = props.offerList;
  let result = {
    totalSearchItems: totalElements,
    currentPage: page,
    pageSize: size,
    totalPages: totalPages,
  };

  return result;
});

const resetActiveStatus = (offer) => {
  if (!props.offerList?.elements) {
    offerListModify.value[offerListPaging.value.currentPage].forEach(
      (offer) => (offer.isActive = false)
    );
  } else {
    emit("resetOfferListActiveStatus", offer);
  }
};

const setSelectedOfferItem = (offer) => {
  resetActiveStatus(offer);
  selectedOffer.value = offer;
  categoryStore.setSearchCategoryFilterObjAction(false);
  categoryStore.setChildTreeViewStatus(true);
};

const handleChangePage = (pageNo) => {
  emit("changePageNo", pageNo);
};

const handleChangeOfferPage = (pageNo) => {
  offerListPaging.value.currentPage = pageNo;
};

watch(
  () => props.offerList,
  (val) => {
    if (!props.offerList?.elements) {
      const pageSize = 7;
      const list: any = cloneDeep(val);
      offerListModify.value = chunk(list, pageSize).reduce(
        (acc, group, index) => {
          acc[index + 1] = group;
          return acc;
        },
        {}
      );
      offerListPaging.value = {
        ...offerListPaging.value,
        totalPages: Math.ceil(props.offerList?.length / pageSize),
      };
    }
    offerListPaging.value.currentPage = 1;
  },
  { deep: true, immediate: true }
);
</script>

<style scoped>
:deep(.v-pagination__item, .v-pagination__prev, .v-pagination__next) {
  width: 32px;
  height: 32px;
  color: #6b6d70;
  display: flex;
  align-items: center;
}

:deep(.v-pagination__item--is-active) {
  background-color: #fee5e7;
  border: 1px solid #d9325a;
  border-radius: 8px;
  color: #d9325a;
}
:deep(.v-pagination .v-btn) {
  width: 32px;
  height: 32px;
  border-radius: 8px;
}
.pagination {
  max-width: 100%;
}
.offer-search {
  height: calc(100vh - 320px);
}
</style>
