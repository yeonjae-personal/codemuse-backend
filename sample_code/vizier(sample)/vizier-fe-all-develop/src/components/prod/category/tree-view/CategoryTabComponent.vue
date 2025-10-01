<template>
  <div class="view-detail-template">
    <div
      class="left-section"
      :class="
        categoryStore.getCategoryView === CATEGORY_VIEW_MODE.LIST &&
        '!max-w-[unset] w-full'
      "
    >
      <div ref="categorySearchForm">
        <CategorySearchForm
          :tab="props.tab"
          :is-reset="resetStatus"
          :fetch-search-offer="fetchSearchOffer"
          @reset-stucture="handleResetStructure"
          @reset-offer-list-active-status="resetOfferListActiveStatus"
        />
      </div>

      <CategoryTreeComponent
        v-if="
          props.tab === categoryStore.getCategoryCurrentTab &&
          categoryStore.getCategoryView === CATEGORY_VIEW_MODE.TREE
        "
        :tab="props.tab"
        :last-clicked-catg-uuid="lastClickedCatgUuid"
        :is-edit="categoryStore.getIsEdit"
        :selected-node-item="selectedCategoryTreeNode"
        :search-form-height="formHeight"
      />
      <CategoryListComponent
        v-else-if="
          props.tab === categoryStore.getCategoryCurrentTab &&
          categoryStore.getCategoryView === CATEGORY_VIEW_MODE.LIST
        "
        :tab="props.tab"
      />
    </div>
    <div
      v-if="categoryStore.getCategoryView === CATEGORY_VIEW_MODE.TREE"
      class="w-[420px] pt-[68px] pb-[12px] pr-[12px]"
    >
      <div v-if="openOfferPanel" class="offer-item-list bg-base rounded-lg">
        <div class="px-[10px]">
          <ExpansionPanel
            :title="$t('product_platform.Offers')"
            readonly
            :hide-action="true"
            header-bg-color="#E1EBFC"
            header-txt-color="#4054B2"
            :is-show-actions="
              selectedCategoryTreeNode?.isLeafCategoryNode === RequiredYn.Yes &&
              !reChooseCategoryMode
            "
            :list-actions="listActions()"
          />
        </div>
        <CardTreeList
          :offer-list="!isEmptyOffersList ? offerList : []"
          :node-show-offer="selectedCategoryTreeNode"
          @reset-offer-list-active-status="resetOfferListActiveStatus"
          @change-page-no="handleChangeOfferPageNo"
        />
      </div>
      <template v-if="shouldShowDetailSection">
        <SideDetailSection
          element-height="600px"
          element-width="420px"
          :item="selectedCategoryTreeNode"
          class="side-detail-section"
        />
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import { useWindowSize } from "@vueuse/core";
import useCategoryStore from "@/store/category.store";
import { CATEGORY_VIEW_MODE } from "@/constants/";
import useRedirect from "@/composables/useRedirect";
import useOfferCreateProcessStore from "@/store/offerCreateProcess.store";
import { RequiredYn } from "@/enums";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import type { ActionType } from "@/interfaces/prod";

const props = defineProps({
  tab: {
    type: String,
    default: "",
    require: true,
  },
});

const { t } = useI18n();
const { width } = useWindowSize();
const categoryStore = useCategoryStore();
const {
  offerCreateInCategoryMode,
  categorySelected,
  categoryNodeUuid,
  categoryTab,
  reChooseCategoryMode,
} = storeToRefs(useOfferCreateProcessStore());
const { moveCreateOfferPage } = useRedirect();

const resetStatus = ref(false);

const offerList = ref<any>();
const lastClickedCatgUuid = ref<any>();
const categorySearchForm = ref<any>(null);
const formHeight = ref<any>(68);

const selectedCategoryTreeNode = computed(
  () => categoryStore.getSelectedCategoryTreeNode
);

const isEmptyOffersList = computed(() => categoryStore.getIsEmptyOfferList);

const shouldShowDetailSection = computed({
  get() {
    return (
      selectedCategoryTreeNode.value &&
      categoryStore.getShowCategoryDetail &&
      categoryStore.getCategoryView !== CATEGORY_VIEW_MODE.LIST
    );
  },
  set(newVal) {
    categoryStore.setShowCategoryDetail(newVal);
  },
});

const openOfferPanel = computed({
  get() {
    return (
      categoryStore.getOpenOfferPanel &&
      categoryStore.getCategoryView !== CATEGORY_VIEW_MODE.LIST
    );
  },
  set(newVal) {
    categoryStore.setOpenOfferPanel(newVal);
  },
});

const listActions = (): ActionType[] => {
  return [
    {
      name: t("product_platform.createOffer"),
      icon: OpenInNewIcon,
      onClick: () => {
        offerCreateInCategoryMode.value = true;
        categoryTab.value = categoryStore.getCategoryCurrentTab;
        categorySelected.value = selectedCategoryTreeNode.value;
        categoryNodeUuid.value = selectedCategoryTreeNode.value?.ctgrNodeUuid;
        moveCreateOfferPage();
      },
    },
  ];
};

onMounted(() => {
  if (categorySearchForm.value?.clientHeight) {
    formHeight.value = categorySearchForm.value.clientHeight;
  }
  if (categoryStore.getProductData.length) {
    offerList.value = cloneDeep(categoryStore.getProductData);
  }
  if (categoryStore.getCategoryOfferTreeSearchResultData?.list?.length) {
    offerList.value = cloneDeep(
      categoryStore.getCategoryOfferTreeSearchResultData
    );
  }
  lastClickedCatgUuid.value = categoryStore.getItemIdShowOffer;
});

watch(width, () => {
  if (categorySearchForm.value.clientHeight) {
    formHeight.value = categorySearchForm.value.clientHeight;
  }
});

watch(
  () => categoryStore.getProductData,
  (newVal) => {
    if (categoryStore.getIsSearchProductOfNode) {
      offerList.value = cloneDeep(newVal);
    }
  },
  { deep: true }
);

watch(
  () => categoryStore.getListOfferUpdateNotSave,
  (newVal) => {
    if (
      !categoryStore.getIsSearchProductOfNode &&
      newVal?.length &&
      offerList.value?.elements?.length
    ) {
      compareReplaceOfferUpdate(newVal);
    }
  },
  { deep: true }
);

const compareReplaceOfferUpdate = (compareArray) => {
  compareArray.forEach((item) => {
    let index = offerList.value.elements.findIndex(
      (offer) => offer.prodUuid === item.objUuid
    );
    if (index !== -1) {
      offerList.value.elements[index as number] = item;
    }
  });
};

const fetchProductList = async (ctgrNodeUuid) => {
  const params = {
    ctgrNodeUuid: ctgrNodeUuid,
    ctgrTabUuid: categoryStore.getUuidTab,
  };
  await categoryStore.getTreeProduct(params);
  offerList.value = cloneDeep(categoryStore.getProductData);
  categoryStore.setItemIdShowOffer(ctgrNodeUuid);
  lastClickedCatgUuid.value = ctgrNodeUuid;
  categoryStore.setOfferSearchData(null);
};

const fetchSearchOffer = async () => {
  await categoryStore.getCategoryOfferTreeSearchResult();
  offerList.value = cloneDeep(
    categoryStore.getCategoryOfferTreeSearchResultData
  );
  if (categoryStore.getListOfferUpdateNotSave?.length) {
    compareReplaceOfferUpdate(categoryStore.getListOfferUpdateNotSave);
  }
};

const handleChangeOfferPageNo = async (pageNo) => {
  categoryStore.setCategoryOfferSearchParamPageNo(pageNo);
  fetchSearchOffer();
};

const resetOfferListActiveStatus = (offerParam) => {
  if (offerParam?.isRedirect) {
    if (offerParam?.ctgrNodeUuid) {
      lastClickedCatgUuid.value = offerParam.ctgrNodeUuid;
      nextTick(() => {
        shouldShowDetailSection.value = false;
        openOfferPanel.value = true;
      });
    }
    return;
  }
  if (offerList.value?.elements?.length) {
    offerList.value?.elements.forEach((offer) => (offer.isActive = false));
  } else {
    offerList.value?.forEach((offer) => (offer.isActive = false));
  }
  if (offerParam?.ctgrNodeUuid) {
    categoryStore.setItemIdShowOffer(null);
    categoryStore.setItemIdShowOffer(offerParam.ctgrNodeUuid);
    lastClickedCatgUuid.value = offerParam.ctgrNodeUuid;
  }
};

const handleResetStructure = () => {
  lastClickedCatgUuid.value = null;
};

watch(
  () => categoryStore.getSearchProductStatus,
  (val) => {
    if (
      val &&
      props.tab === categoryStore.getCategoryCurrentTab &&
      categoryStore.getCategoryView === CATEGORY_VIEW_MODE.TREE
    ) {
      categoryStore.setCategoryOfferSearchParamPageNo(1);
      fetchSearchOffer();
      nextTick().then(() => {
        categoryStore.setSearchProductStatus(false);
      });
    }
  }
);

watch(
  () => categoryStore.getShowCategoryDetail,
  (val) => {
    if (val) {
      categoryStore.setOpenOfferPanel(false);
    }
  }
);

watch(
  () => categoryStore.getOpenOfferPanel,
  (val) => {
    if (val) {
      categoryStore.setShowCategoryDetail(false);
    }
  }
);
watch(
  () => categoryStore.getSelectedCategoryTreeNode,
  (val) => {
    if (!val) {
      offerList.value = [];
      categoryStore.setSearchProductStatus(false);
      categoryStore.setOpenOfferPanel(true);
    }
  }
);

provide("handleFetchOfferList", fetchProductList);
provide("handleResetOfferList", handleResetStructure);
</script>

<style scoped>
.view-detail-template {
  display: flex;
  width: 100%;
  height: 100%;
}

.left-section {
  max-width: calc(100vw - 548px);
  min-width: calc(100vw - 548px);
}

.side-detail-section {
  width: 420px;
}

.offer-item-list {
  padding: 0px 14px;
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
  gap: 12px;
}
</style>
