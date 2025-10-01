<template>
  <SearchPane
    ref="searchPaneMultiEntity"
    title="product_platform.multiEntitySearch"
    container-class="rounded-lg col-span-1"
    :pane-type="SearchPaneType.MultiEntity"
    :pane-col="ColNumber.Two"
    :item-height="62"
    :option-types="multiEntityTypes"
    :option-sub-types="subTypeList"
    :selected-item="selectedEntity"
    :model-param="paramsMultiEntitySearch"
    :model-list="listItems"
    :pagination="pagination"
    :type-select-require="true"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-click-item="onChooseCard"
    @on-change-page="handleChangePage"
  />
  <BasePopup
    v-model="openPopup"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.groupCancelEdit')"
    @on-close="closePopupSave"
    @on-submit="handleNewGroupDetails"
  />
</template>

<script setup lang="ts">
import { OFFER_TABS_VALUE } from "@/constants/offer";
import { ColNumber, DialogIconType, SearchPaneType } from "@/enums";
import { useMultiEntitySearchStore, useSnackbarStore } from "@/store";
import { useI18n } from "vue-i18n";

const {
  isEdit,
  currentTab,
  entityList,
  entityDisplayForm,
  selectedEntity,
  multiEntityTypes,
  inputValue,
  selectedEntityDetails,
  paramsMultiEntitySearch,
} = storeToRefs(useMultiEntitySearchStore());

const {
  getEntityList,
  getEntityDetailInfo,
  getMultiEntityTypes,
  resetParamListEntitySearch,
  handleResetMultiEntitySearch,
} = useMultiEntitySearchStore();
const useSnackbar = useSnackbarStore();
const { t } = useI18n();
// Init varible
const openPopup = ref();
const preSelectGroupData = ref();
const searchPaneMultiEntity = ref();
// Define computed
const listItems = computed(() => entityList.value.items);
const pagination = computed(() => entityList.value.pagination);
const subTypeList = computed(() => {
  if (paramsMultiEntitySearch.value.type) {
    const list: any = multiEntityTypes.value.find(
      (item: any) => item.value === paramsMultiEntitySearch.value.type
    );
    return list?.subOptions;
  }
  return [];
});

// Define method
const handleSearch = async (
  size?: number,
  isClick: boolean = true,
  page = 1
) => {
  searchPaneMultiEntity.value.validate();
  if (!paramsMultiEntitySearch.value.type) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }
  if (isClick) {
    selectedEntityDetails.value = null;
    selectedEntity.value = null;
    isEdit.value = false;
  }
  paramsMultiEntitySearch.value.page = isClick ? 1 : page;
  paramsMultiEntitySearch.value.size = size;
  await getEntityList();
};

const onChooseCard = async (value) => {
  if (isEdit.value) {
    preSelectGroupData.value = value;
    openPopup.value = true;
  } else {
    handleSelectEntity(value);
  }
};

const handleNewGroupDetails = () => {
  isEdit.value = false;
  handleSelectEntity(preSelectGroupData.value);
  closePopupSave();
};

const handleSelectEntity = async (value) => {
  if (value?.entityCode !== selectedEntity.value?.entityCode) {
    selectedEntity.value = value;
    isEdit.value = false;
    await getEntityDetail();
    entityDisplayForm.value.entityDetail = true;
    currentTab.value = OFFER_TABS_VALUE.GENERAL;
  }
};

const getEntityDetail = async () => {
  await getEntityDetailInfo();
};

const handleResetSearch = () => {
  searchPaneMultiEntity.value.resetValidate();
  resetParamListEntitySearch();
  selectedEntity.value = null;
  inputValue.value = null;
  handleResetMultiEntitySearch();
};

const handleChangePage = async (page) => {
  paramsMultiEntitySearch.value.page = page;
  await getEntityList();
};

const closePopupSave = () => {
  openPopup.value = false;
  preSelectGroupData.value = null;
};

onMounted(() => {
  getMultiEntityTypes();
});
</script>
