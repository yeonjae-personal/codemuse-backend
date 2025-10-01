<template>
  <SearchPane
    v-if="viewMode === VIEW_MODE.GRID"
    :key="componentKey"
    ref="searchPane"
    title="product_platform.groupSearch"
    :title-append="titleAppend"
    :container-class="containerClass"
    :model-list="groupList.items"
    :pagination="groupList.pagination"
    :pane-type="SearchPaneType.Group"
    :pane-col="displayForm.offerSearch ? ColNumber.One : ColNumber.Two"
    :model-param="paramsSearchGroup"
    :option-types="groupTypeList"
    :selected-item="selectedGroup"
    :type-select-require="checkSelectRequire()"
    :show-float-icon-left="displayForm.offerSearch"
    :show-float-icon-right="!displayForm.offerSearch"
    :search-item-actions="listActions"
    :open-popup="openPopup"
    :popup-content="popupContent"
    :item-height="62"
    @on-click-item="handleClickGroup"
    @on-search="handleSearch"
    @on-reset="handleResetSearch(true)"
    @on-click-float-left="onClose"
    @on-click-float-right="onOpen"
    @close-popup="closePopupSave"
    @submit-popup="handleNewGroupDetails"
    @on-change-page="handleChangePage"
    @on-drag-start="handleDragStart"
  >
    <template #search-button-append>
      <div
        v-if="!displayForm.offerSearch"
        class="!h-10 !w-[169px] bg-primary-lightest rounded-lg w-auto p-4 flex items-center justify-center gap-1 ms-2"
        @click="onSearchOffer"
      >
        <SearchIcon fill="#BA1642" />
        <span
          class="text-text-primary font-base font-size-base font-medium cursor-pointer"
          >{{
            !isOfferSearchGroup
              ? $t("product_platform.search_with_offer")
              : $t("product_platform.without_offer")
          }}
        </span>
      </div>
      <div
        v-if="groupList.items?.length && !displayForm.offerSearch"
        class="h-10 ml-2 py-4 flex items-center justify-center"
      >
        <SwitchViewTable
          v-model="viewMode"
          class="ms-auto"
          @update:model-value="handleChangeViewMode"
        />
      </div>
    </template>
  </SearchPane>
  <TablePane
    v-if="viewMode === VIEW_MODE.LIST"
    ref="tablePane"
    :pane-type="SearchPaneType.Group"
    :pane-col="ColNumber.Four"
    :model-list="groupList.items"
    :pagination="groupList.pagination"
    :model-param="paramsSearchGroup"
    :view-mode="viewMode"
    :is-loading="isLoading"
    :downloading="downloading"
    :option-types="groupTypeList"
    @update:view-mode="handleChangeViewMode"
    @on-search="handleSearch"
    @on-reset="handleResetSearch(true)"
    @on-change-page="handleChangePage"
    @on-download="handleDownload"
  />
</template>

<script setup lang="ts">
import { EXPORT_GROUP_EXCEL_PATH } from "@/api/prod/path";
import DuplicateIcon from "@/components/prod/icons/DuplicateIcon.vue";
import { useDownloadFile } from "@/composables/useDownloadFIle";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { SEARCH_CATEGORY } from "@/constants/extendsManager";
import { VIEW_MODE } from "@/constants/index";
import { ColNumber, LargeItemCode, SearchBy, SearchPaneType } from "@/enums";
import {
  useDragStore,
  useExtendSearchStore,
  useHistoryTabStore,
  useSnackbarStore,
} from "@/store";
import { useI18n } from "vue-i18n";
import SearchPane from "../../shared/SearchPane.vue";

const { downloading, downloadFile } = useDownloadFile();
const { handleDragUserPocket } = useDragUserPocket();
const { dragOfferType } = storeToRefs(useDragStore());
const { t, locale } = useI18n();
const historyStore = useHistoryTabStore();
const useSnackbar = useSnackbarStore();
const groupSearchStore = useExtendSearchStore();
const {
  isEdit,
  isDuplicate,
  displayForm,
  viewMode,
  paramsSearchGroup,
  isOfferSearchGroup,
  selectedGroup,
  groupList,
  groupTypeList,
  groupDetailData,
  inputValue,
  isResetValue,
  selectedOffer,
} = storeToRefs(groupSearchStore);
const {
  getGroupList,
  getGroupListView,
  getGroupDetailInfo,
  resetParamListOfferSearch,
  resetParamListGroupSearch,
} = groupSearchStore;

const preSelectGroupData = ref<any>();
const popupContent = ref();
const actionType = ref();
const openPopup = ref(false);
const isLoading = ref(false);
const searchPane = ref<InstanceType<typeof SearchPane>>();
const tablePane = ref();
const componentKey = ref<number>(0);

const isGridMode = computed(() => viewMode.value === VIEW_MODE.GRID);
const containerClass = computed(() =>
  displayForm.value.offerSearch
    ? "rounded-r-lg border-l-[#E6E9ED] border-l-[1px]"
    : "rounded-lg"
);
const listActions = (item) => [
  {
    name: t("product_platform.duplicate"),
    icon: DuplicateIcon,
    onClick: () => {
      handleDuplicate(item);
    },
  },
];
const titleAppend = computed(() => {
  return displayForm.value.offerSearch
    ? ""
    : isOfferSearchGroup.value
      ? "product_platform.with_offer_mode"
      : "product_platform.groupPool";
});
const onSearchOffer = () => {
  displayForm.value.addOffer = false;
  isEdit.value = false;
  isDuplicate.value = false;
  if (isOfferSearchGroup.value) {
    isOfferSearchGroup.value = false;
    paramsSearchGroup.value.childOffrUuid = undefined;
    selectedOffer.value = null;
    isResetValue.value = false;
  } else {
    displayForm.value.offerSearch = true;
    displayForm.value.groupDetail = false;
  }
  handleResetSearch();
  resetParamListOfferSearch(SEARCH_CATEGORY.OFFER);
};

const handleChangeViewMode = (value) => {
  viewMode.value = value;
  selectedGroup.value = null;
  isEdit.value = false;
  displayForm.value.groupDetail = false;
  displayForm.value.addOffer = false;
  paramsSearchGroup.value.page = 1;
  paramsSearchGroup.value.size = isGridMode.value ? 14 : 10;
  groupList.value.items = [];
  searchGroupList();
};

const searchGroupList = async () => {
  if (!paramsSearchGroup.value?.type && !isOfferSearchGroup.value) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }
  try {
    isLoading.value = true;
    isGridMode.value ? await getGroupList() : await getGroupListView();
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
  } finally {
    isLoading.value = false;
  }
};

const handleSearch = async (
  pageSize: number,
  isClick: boolean = true,
  page = 1
) => {
  isResetValue.value = false;
  if (displayForm.value.offerSearch && !selectedOffer.value?.objUuid) return;
  if (!displayForm.value.offerSearch) {
    if (isGridMode.value) searchPane?.value?.validate();
    else tablePane?.value?.validate();
    if (!paramsSearchGroup.value?.type && !isOfferSearchGroup.value) {
      useSnackbar.showSnackbar(
        t("product_platform.required_field_missing"),
        "error"
      );
      return;
    }
    if (isClick) {
      paramsSearchGroup.value.page = isClick ? 1 : page;
    }
    paramsSearchGroup.value.size = displayForm.value.offerSearch
      ? pageSize
      : isGridMode.value
        ? pageSize
        : 10;
  }
  paramsSearchGroup.value.page = isClick ? 1 : page;
  paramsSearchGroup.value.size = displayForm.value.offerSearch
    ? pageSize
    : isGridMode.value
      ? pageSize
      : 10;
  if (isOfferSearchGroup.value) {
    paramsSearchGroup.value.childOffrUuid = selectedOffer.value?.objUuid;
  }
  await searchGroupList();
};

const handleChangePage = (page) => {
  paramsSearchGroup.value.page = page;
  searchGroupList();
};

const handleClickGroup = (value) => {
  actionType.value = "detail";
  if (isEdit.value || isDuplicate.value) {
    popupContent.value = !isEdit.value
      ? t("product_platform.groupCancelDuplicate")
      : t("product_platform.groupCancelEdit");
    preSelectGroupData.value = value;
    openPopup.value = true;
  } else {
    handleSelectGroup(value);
  }
};

const handleSelectGroup = async (value) => {
  groupDetailData.value.offerTabPagination.currentPage = 1;
  displayForm.value.groupDuplicate = false;
  displayForm.value.addOffer = false;
  isDuplicate.value = false;
  resetParamListOfferSearch("");
  selectedGroup.value = value;
  isEdit.value = false;
  await getGroupDetail();
  displayForm.value.groupDetail = true;
};

const handleDuplicate = async (value) => {
  actionType.value = "duplicate";
  if (isEdit.value || isDuplicate.value) {
    popupContent.value = !isEdit.value
      ? t("product_platform.groupCancelDuplicate")
      : t("product_platform.groupCancelEdit");
    preSelectGroupData.value = value;
    openPopup.value = true;
  } else {
    handleDuplicateGroup(value);
  }
};

const handleDuplicateGroup = async (value) => {
  displayForm.value.groupDetail = false;
  displayForm.value.addOffer = false;
  isEdit.value = false;
  resetParamListOfferSearch("");
  displayForm.value.groupDuplicate = false;
  selectedGroup.value = value;
  isDuplicate.value = true;
  await getGroupDetail(true);
  displayForm.value.groupDuplicate = true;
};

const getGroupDetail = async (isAdd = false) => {
  await getGroupDetailInfo(isAdd);
  historyStore.fetchHistory({
    objUuid: selectedGroup.value?.objUuid,
  });
};

const handleResetSearch = (isClick: boolean = false): void => {
  if (isGridMode.value) searchPane?.value?.resetValidate();
  else tablePane?.value?.resetValidate();
  resetParamListGroupSearch();
  displayForm.value.groupDuplicate = false;
  selectedGroup.value = null;
  inputValue.value = null;
  displayForm.value.groupDetail = false;
  if (isClick) isResetValue.value = true;
};

const handleCalculatePage = (newPageSize: number): number => {
  const page = paramsSearchGroup.value.page || 1;
  const size = paramsSearchGroup.value.size || 14;
  const currentTotal = (page - 1) * size + 1;
  const newPage = Math.ceil(currentTotal / newPageSize);
  return newPage ? newPage : 1;
};

const onClose = async () => {
  displayForm.value.offerSearch = false;
  if (isOfferSearchGroup.value) {
    if (isResetValue.value) return;
    nextTick(() => {
      searchPane.value?.calcTotalItem();
      const size = searchPane.value?.totalItem || 14;
      const page = handleCalculatePage(size);
      paramsSearchGroup.value.page = page;
      handleSearch(size, false);
      componentKey.value++;
    });
  }
};

const onOpen = async () => {
  displayForm.value.offerSearch = true;
  displayForm.value.addOffer = false;
  isEdit.value = false;
  isDuplicate.value = false;
  if (isOfferSearchGroup.value) {
    if (isResetValue.value) return;
    nextTick(() => {
      searchPane.value?.calcTotalItem();
      const size = searchPane.value?.totalItem || 7;
      const page = handleCalculatePage(size);
      paramsSearchGroup.value.page = page;
      handleSearch(size, false);
    });
  } else {
    handleResetSearch();
    displayForm.value.groupDetail = false;
  }
};

const closePopupSave = () => {
  openPopup.value = false;
  preSelectGroupData.value = null;
};

const handleNewGroupDetails = () => {
  if (actionType.value === "detail") {
    handleSelectGroup(preSelectGroupData.value);
  }
  if (actionType.value === "duplicate") {
    handleDuplicateGroup(preSelectGroupData.value);
  }
  closePopupSave();
};

const checkSelectRequire = () => {
  if (displayForm.value.offerSearch || isOfferSearchGroup.value) {
    return false;
  }
  return true;
};

const handleDownload = async () => {
  try {
    if (!paramsSearchGroup.value.type?.trim()) {
      tablePane.value?.validate();
      useSnackbar.showSnackbar(
        t("product_platform.required_field_missing"),
        "error"
      );
      return;
    }
    const params: any = {
      itemCode: paramsSearchGroup.value.type,
      offrGrpCd:
        paramsSearchGroup.value.searchBy === SearchBy.Code
          ? paramsSearchGroup.value.searchKey
          : null,
      offrGrpNm:
        paramsSearchGroup.value.searchBy === SearchBy.Name
          ? paramsSearchGroup.value.searchKey
          : null,
      page: paramsSearchGroup.value.page,
      size: paramsSearchGroup.value.size,
      offerGroupTypeCode: paramsSearchGroup.value.offerGroupTypeCode,
      childOffrUuid: paramsSearchGroup.value.childOffrUuid,
      language: locale.value || "en",
    };
    await downloadFile(EXPORT_GROUP_EXCEL_PATH, params, "Group");
  } catch (err: any) {
    useSnackbar.showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const handleDragStart = ({
  event,
  item,
}: {
  event: DragEvent;
  item: any;
}): void => {
  dragOfferType.value = item.itemType.charAt(0);
  handleDragUserPocket(event, { userPocketType: LargeItemCode.Group, ...item });
};
defineExpose({ searchGroupList });
</script>
