<template>
  <div class="category-detail-section" :style="{ width: elementWidth }">
    <div class="category-section-title">
      <p class="category-section-title-text">
        {{ title }}
      </p>
    </div>
    <div class="w-full flex-1 pl-4 pr-3">
      <BaseTabs
        v-model="currentTab"
        :tabs="catgTabs"
        :center-active="true"
        class="pr-2"
        class-tabs-bar="none"
        class-loco="!px-0"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import useCategoryStore from "@/store/category.store";
import CategoryGeneralTab from "@/components/prod/category/tree-view/tabs/CategoryGeneralTab.vue";
import CategoryOfferTab from "@/components/prod/category/tree-view/tabs/CategoryOfferTab.vue";
import { useI18n } from "vue-i18n";
import { OFFER_TABS_VALUE } from "@/constants/offer";

const props = defineProps({
  elementHeight: {
    type: String,
    default: "100%",
  },
  elementWidth: {
    type: String,
    default: "360px",
  },
  item: {
    type: Object,
    default: null,
  },
});

const categoryStore = useCategoryStore();
const { t } = useI18n();

const currentTab = computed({
  get: () => categoryStore.getCurrentTab,
  set: (value) => {
    categoryStore.setCurrentTab(value);
  },
});

const isEditMode = computed(() => categoryStore.getIsEdit);

const catgTabs = computed(() => {
  const listTabs = [
    {
      value: OFFER_TABS_VALUE.GENERAL,
      label: t("product_platform.general"),
      component: CategoryGeneralTab,
      props: {
        item: props.item,
      },
    },
  ];
  if (!isEditMode.value) {
    listTabs.push({
      value: OFFER_TABS_VALUE.OFFER,
      label: t("product_platform.offer_title"),
      component: CategoryOfferTab,
      props: {
        // category: props.category,
      },
    });
  }

  return listTabs;
});

const title = computed(() => {
  if (isEditMode.value) {
    return props.item?.isNew
      ? t(`product_platform.nodeCreate`)
      : t(`product_platform.nodeEdit`);
  }
  return t(`product_platform.categoryDetails`);
});

watch(
  () => isEditMode.value,
  (val) => {
    if (val) {
      categoryStore.setCurrentTab(OFFER_TABS_VALUE.GENERAL);
    }
  }
);
</script>

<style scoped>
:deep(.v-card-text) {
  padding: unset !important;
}
.category-content-content-text {
  margin-bottom: 12px;
  margin-top: 12px;
}

.category-information-item {
  display: flex;
}

.category-information-item-label,
.category-information-item-value {
  font-size: 13px;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
  text-align: left;
  color: #6b6d70;
}
.category-information-item-label {
  width: 40%;
  font-weight: 500;
}

.category-information-item-value {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 60%;
  font-weight: 400;
}

.category-detail-section {
  display: flex;
  flex-direction: column;
  position: relative;
}

.hide-button {
  position: absolute;
  top: 30%;
  left: -3px;
  height: 54px;
  width: 20px;
  border-bottom-right-radius: 40px;
  border-top-right-radius: 40px;
  display: flex;
  background: #5254571f;
  align-items: center;
  justify-content: center;
}

.hide-button:hover {
  cursor: pointer;
}

.hide-button-content {
  color: #f7f8fa;
}

.category-section-title-text {
  font-size: 15px;
  font-weight: 500;
  line-height: 24px;
  letter-spacing: 0.5px;
  text-align: left;
  color: #3a3b3d;
}

.category-overview-summary-content-text {
  color: #3a3b3d;
  font-size: 11px;
  font-weight: 400;
  line-height: 16.5px;
  letter-spacing: 0.25px;
  text-align: left;
}

.category-overview-header-content-text {
  font-size: 14px;
  font-weight: 500;
  line-height: 21px;
  letter-spacing: 0.25px;
  text-align: left;
  color: #3a3b3d;
}

.category-section-title {
  height: 48px;
  padding: 8px 16px;
}

.category-overview-content-text {
  background-color: #f7f8fa;
  padding: 12px 16px 20px 16px;
  border-bottom-left-radius: 12px;
  border-bottom-right-radius: 12px;
}

.category-overview-header-text {
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
}

.category-information {
  display: flex;
  flex-direction: column;
  padding-top: 16px;
  gap: 8px;
  height: calc(100vh - 370px);
  overflow-y: auto;
  scrollbar-width: thin;
}

.category-information-content-container {
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
  background-color: #f7f8fa;
  padding: 4px 16px;
}

.v-tab {
  text-transform: none !important;
}

.category-option-tab-item {
  color: #bdc1c7;
  border-bottom: 1px solid #bdc1c7;
}

.category-option-tab-item .v-btn__content {
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: center;
}

.result-items {
  height: calc(100vh - 520px);
}
</style>
