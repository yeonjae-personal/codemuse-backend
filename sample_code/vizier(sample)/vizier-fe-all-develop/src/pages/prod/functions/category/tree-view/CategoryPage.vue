<template>
  <cf-tabs
    v-if="exTabs?.length"
    :tabs="exTabs"
    align-tabs="start"
    :tabs-class="'category-tabs'"
    mode="no-card"
    :selected="categoryStore.getCategoryCurrentTab"
    :is-trans="false"
    :is-disable-tab="isEdit"
    @tab-change="handleChangeTab"
    @tab-change-waring="handleChangeTab"
  >
    <template v-for="t in exTabs" :key="t.value" #[t.slot]>
      <CategoryTabComponent :tab="t.value" />
    </template>
  </cf-tabs>
  <base-popup
    v-model="openPopup"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.groupCancelEdit')"
    @on-close="closePopup"
    @on-submit="handleMoveTab"
  />
</template>

<script setup lang="ts">
import useCategoryStore from "@/store/category.store";
import { DialogIconType, LargeItemCode } from "@/enums";
import useOfferCreateProcessStore from "@/store/offerCreateProcess.store";
import { getListItemCodeApi } from "@/api/prod/commonApi";

const categoryStore = useCategoryStore();
const { setCategoryTab, fetchTabsCategory, offerTypes } = categoryStore;
const { tabs } = storeToRefs(categoryStore);
const {
  reChooseCategoryMode,
  categoryTab,
  offerCreateInCategoryMode,
  itemTypeOffer,
} = storeToRefs(useOfferCreateProcessStore());

const openPopup = ref(false);
const tabTemp = ref(null);

const closePopup = () => {
  tabTemp.value = null;
};
const handleMoveTab = () => {
  categoryStore.setIsEdit(false);
  setCategoryTab(tabTemp.value);
  openPopup.value = false;
};

const handleChangeTab = (event, isWarning = false) => {
  if (isWarning) {
    openPopup.value = true;
    tabTemp.value = event;
  } else {
    setCategoryTab(isEdit.value ? categoryStore.getCategoryCurrentTab : event);
  }
};

const isEdit = computed(() => categoryStore.getIsEdit);

const exTabs = computed(() => {
  let tabsCtg = tabs.value.map((i) => ({
    value: i.ctgrTabName.toUpperCase().replace("-", ""),
    label: i.ctgrTabName,
    slot: i.sortNo,
  }));

  if (tabsCtg?.length) {
    if (reChooseCategoryMode.value && offerCreateInCategoryMode.value) {
      return tabsCtg.filter((i) => i.value === categoryTab.value);
    }
    if (
      reChooseCategoryMode.value &&
      itemTypeOffer.value &&
      offerTypes.value?.length
    ) {
      return tabsCtg.filter(
        (i) =>
          i.value ===
          offerTypes.value
            .find((i) => i.cmcdDetlId === itemTypeOffer.value)
            ?.cmcdDetlNm?.toUpperCase()
            .replace("-", "")
      );
    }
    return tabsCtg;
  }
});

const getListOfferType = async () => {
  if (!offerTypes.value?.length) {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Offer,
    });
    offerTypes.value = data || [];
  }
};

watch(
  () => tabs.value,
  (val) => {
    if (val && val.length) {
      setCategoryTab(val[0]?.ctgrTabName.toUpperCase().replace("-", ""));
    }
  },
  { deep: true }
);

onMounted(async () => {
  await getListOfferType();
  if (!tabs.value?.length) {
    fetchTabsCategory();
  }
});
</script>
