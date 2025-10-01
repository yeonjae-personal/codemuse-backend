<template>
  <div class="flex justify-between items-center h-[40px] gap-3 w-full my-2">
    <BaseInputSearch
      v-model="paramFilterDetail.factorName"
      density="comfortable"
      label="search"
      variant="solo"
      hide-details
      single-line
      rounded="4"
      @keyup.enter="handleSearch"
    />
    <SearchAndRefreshButton
      @handle-search="handleSearch"
      @handle-refresh="handleResetSearch"
    />
  </div>
  <BaseTotalSearchResult
    v-if="localFactor.pagination?.totalItems > 0"
    class-name="pt-2"
    :total-search="localFactor.pagination?.totalItems"
    :total-items="localFactor.pagination?.totalItems"
  />
  <div
    id="locoTypeSwapper"
    class="relative h-full w-full max-h-[calc(100vh-490px)]"
    :class="[localFactor.pagination?.totalItems ? 'mt-2' : 'mt-4']"
  >
    <LocomotiveComponent :scroll-container-class="scrollContainerClass">
      <div class="content h-full overflow-hidden">
        <div class="flex flex-col gap-4 items-center py-1 px-[10px]">
          <div
            v-for="(item, index) in localFactor?.factorLst"
            :key="item?.factorCode"
            ref="factorTypeItems"
            class="w-full"
          >
            <FactorItem
              :item="item"
              :title="item?.factorName"
              :active="setActive(item)"
              :disable="item?.useYn === RequiredYn.No"
              :is-new="item?.isAdded"
              :search-text="paramFilterDetail.factorName"
              :is-show-expand="false"
              @selected-item="handleClickFactor(item, index)"
            />
          </div>
        </div>
      </div>
    </LocomotiveComponent>
    <div
      v-if="!isEditFactorTypeDetail && localFactor.factorLst?.length === 0"
      class="flex-1 w-full flex align-center h-full"
    >
      <NoData />
    </div>

    <div
      v-if="isEditFactorTypeDetail && !isAddNewFactorChild && !showBtnTypeFixed"
      class="flex justify-center w-full mt-6"
    >
      <button class="add-new-button" @click="handleAddNewFactorTypeChild">
        <PlusLargeIcon />
      </button>
    </div>
    <button
      v-if="isEditFactorTypeDetail && !isAddNewFactorChild && showBtnTypeFixed"
      class="add-new-button float"
      @click="handleAddNewFactorTypeChild"
    >
      <PlusLargeIcon />
    </button>
  </div>
  <BasePagination
    v-if="localFactor?.pagination?.totalItems > 0"
    :pagination="localFactor?.pagination"
    :class-name="isEditFactorTypeDetail ? 'mb-[70px]' : 'mb-3'"
    @on-change-page="handleChangePage"
  />
  <base-popup
    v-model="openPopup"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.updatingConfirmSaved')"
    @on-close="closePopup"
    @on-submit="handleSearchPopup"
  />
</template>
<script setup lang="ts">
import { TABS_NAME_COLLECTION } from "@/constants/index";
import { RequiredYn, DialogIconType } from "@/enums";
import useFactorStore from "@/store/admin/factor.store";
import cloneDeep from "lodash-es/cloneDeep";
import { v4 as uuidv4 } from "uuid";

const {
  currentTab,
  paramFilterDetail,
  factorTypeSelected,
  isEditFactorTypeDetail,
  factorSelected,
  factorDetail,
  isCreateFactorDetail,
  isEditFactorDetail,
  factorTypeDetail,
  factorBeforeEditIndex,
  isAddNewFactorChild,
  factorDetailBeforeEdit,
  factorTypeDetailBeforeEdit,
  paginationFactorDetail,
} = storeToRefs(useFactorStore());
const { getDetailFactorType, resetParamFactorTypeDetail } = useFactorStore();

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => {},
  },
});
const openPopup = ref(false);
const isClickOtherFactor = ref(false);
const isClickAddChild = ref(false);
const popupData = ref<any>(null);
const popupDataIndex = ref(0);
const showBtnTypeFixed = ref(false);
const factorTypeItems = ref<any>();

const localFactor = computed<any>(() => {
  let data = cloneDeep(props.modelValue);
  let startIndex =
    (props.modelValue.pagination.currentPage - 1) *
    props.modelValue.pagination.pageSize;
  let endIndex = startIndex + props.modelValue.pagination.pageSize;
  data.factorLst = data.factorLst?.filter(
    (_item, index) => index >= startIndex && index < endIndex
  );
  return data;
});
const scrollContainerClass = computed(() => {
  return [
    "!px-0 !h-auto",
    isEditFactorTypeDetail.value
      ? "max-h-[calc(100vh-510px)]"
      : "max-h-[calc(100vh-445px)]",
  ];
});
const setActive = (item) => {
  if (item?.isAdded) {
    return item?.hiddenUuid === factorSelected.value?.hiddenUuid;
  } else {
    return item?.factorCode === factorSelected.value?.factorCode;
  }
};

const handleChangePage = async (page) => {
  factorTypeDetail.value.pagination.currentPage = page;
};

const checkFactorValueListHeight = () => {
  const scrollCont = document.getElementById("locoTypeSwapper");
  if (scrollCont?.clientHeight && factorTypeItems.value?.length) {
    let itemHeight = 0;
    for (const el of factorTypeItems.value) {
      itemHeight += el.clientHeight;
    }
    if (itemHeight + 32 >= scrollCont?.clientHeight - 127) {
      showBtnTypeFixed.value = true;
    } else {
      showBtnTypeFixed.value = false;
    }
  } else {
    showBtnTypeFixed.value = false;
  }
};

const handleSearch = async () => {
  const existedItemNew = factorTypeDetail.value?.factorLst?.some(
    (factor) => factor?.isAdded || factor?.isEdit
  );
  if (
    isCreateFactorDetail.value ||
    isEditFactorDetail.value ||
    existedItemNew
  ) {
    openPopup.value = true;
  } else {
    await getDetailFactorType();
    factorTypeDetailBeforeEdit.value = cloneDeep(factorTypeDetail.value);
  }
};

const closePopup = () => {
  openPopup.value = false;
  resetPopupData();
};

const handleSearchPopup = async () => {
  openPopup.value = false;
  if (!isCreateFactorDetail.value && !isClickOtherFactor.value) {
    await getDetailFactorType();
    factorTypeDetailBeforeEdit.value = cloneDeep(factorTypeDetail.value);
  }
  factorDetail.value = null;
  factorSelected.value = null;
  isEditFactorDetail.value = false;
  if (isCreateFactorDetail.value) {
    factorTypeDetail.value.factorLst = factorTypeDetail.value.factorLst.filter(
      (item) => !item?.isNew
    );
    const itemIndexFind = factorTypeDetail.value.factorLst.findIndex(
      (item) => item?.factorCode === popupData.value?.factorCode
    );
    isAddNewFactorChild.value = false;
    isCreateFactorDetail.value = false;
    if (itemIndexFind !== -1) {
      factorBeforeEditIndex.value = itemIndexFind;
      handleGetFactorDetail(popupData.value, factorBeforeEditIndex.value);
    }
  } else if (isClickOtherFactor.value) {
    factorTypeDetail.value?.factorLst.forEach((factor) => {
      if (factor?.factorValueLst?.length) {
        factor.factorValueLst = factor.factorValueLst.filter(
          (value) => !value.isNew
        );
      }
    });
    factorTypeDetail.value.factorLst = factorTypeDetail.value.factorLst.map(
      (factor) => {
        if (factor?.currentSelect) {
          return cloneDeep(
            factorTypeDetailBeforeEdit.value?.factorLst.find(
              (clone) => clone?.factorCode === factor?.factorCode
            )
          );
        }
        return factor;
      }
    );
    if (isClickAddChild.value) {
      addChild();
      isClickAddChild.value = false;
    } else {
      factorBeforeEditIndex.value = popupDataIndex.value;
      handleGetFactorDetail(popupData.value, factorBeforeEditIndex.value);
    }
    isClickOtherFactor.value = false;
  }
  resetPopupData();
};

const handleClickFactor = async (item, index) => {
  if (item?.factorCode !== factorSelected.value?.factorCode || item?.isAdded) {
    popupDataIndex.value = index;
    popupData.value = item;
    if (isCreateFactorDetail.value) {
      openPopup.value = true;
      return;
    } else if (isEditFactorDetail.value) {
      isClickOtherFactor.value = true;
      openPopup.value = true;
      return;
    }
    handleGetFactorDetail(item, index);
  }
};

const resetPopupData = () => {
  popupDataIndex.value = 0;
  popupData.value = null;
};

const handleResetSearch = async () => {
  const existedItemNew = factorTypeDetail.value?.factorLst?.some(
    (factor) => factor.isAdded || factor?.isEdit
  );
  if (
    isCreateFactorDetail.value ||
    isEditFactorDetail.value ||
    existedItemNew
  ) {
    openPopup.value = true;
  } else {
    isEditFactorDetail.value = false;
    isAddNewFactorChild.value = false;
    isCreateFactorDetail.value = false;
    resetParamFactorTypeDetail();
    await getDetailFactorType();
    factorTypeDetailBeforeEdit.value = cloneDeep(factorTypeDetail.value);
  }
};

const handleGetFactorDetail = async (item, index) => {
  factorSelected.value = item;
  paginationFactorDetail.value.currentPage = 1;
  let indexMap =
    index +
    localFactor.value?.pagination?.pageSize *
      (localFactor.value?.pagination?.currentPage - 1);

  if (item?.isAdded) {
    factorDetail.value = factorTypeDetail.value.factorLst[indexMap as number];
  } else {
    factorBeforeEditIndex.value = indexMap;
    factorDetail.value = factorTypeDetail.value.factorLst[indexMap as number];
    factorDetail.value["currentSelect"] = true;
  }
  factorDetailBeforeEdit.value = cloneDeep(factorDetail.value);
};

const handleAddNewFactorTypeChild = (event) => {
  event.preventDefault();
  if (isEditFactorDetail.value || isCreateFactorDetail.value) {
    isClickOtherFactor.value = true;
    isClickAddChild.value = true;
    openPopup.value = true;
    return;
  }
  addChild();
};
const addChild = () => {
  const newFactorTypeChild = {
    hiddenUuid: uuidv4(),
    factorCode: "",
    factorName: "",
    factorTypeCode: factorTypeSelected.value?.factorTypeCode,
    useYn: "Y",
    factorValueLst: [],
    isNew: true,
  };
  factorTypeDetail.value?.factorLst.push(newFactorTypeChild);
  isCreateFactorDetail.value = true;
  isEditFactorDetail.value = true;
  factorSelected.value = newFactorTypeChild;
  factorDetail.value = newFactorTypeChild;
  isAddNewFactorChild.value = true;
  nextTick(() => {
    factorTypeDetail.value.pagination.currentPage =
      factorTypeDetail.value.pagination.totalPages;
  });
};

watch(
  () => currentTab.value,
  (val) => {
    if (val === TABS_NAME_COLLECTION.FACTOR) {
      nextTick(() => {
        checkFactorValueListHeight();
      });
    }
  },
  {
    immediate: true,
  }
);

onMounted(() => {
  checkFactorValueListHeight();
});
onUpdated(() => {
  checkFactorValueListHeight();
});
</script>

<style scoped lang="scss">
.add-new-button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow:
    2px 1px 6px 0px #1e265b3d,
    2px 1px 18px 0px #1518421c inset,
    -5px -2px 6px 0px #ffffffa3 inset;
}
.add-new-button.float {
  position: absolute;
  bottom: 0px;
  z-index: 999;
  left: 45.5%;
}
</style>
