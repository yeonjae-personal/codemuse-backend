<template>
  <div
    v-if="reChooseCategoryMode"
    class="flex justify-end w-full px-[24px] py-[18px] btn-select"
  >
    <BaseButton
      :color="ButtonColorType.Secondary"
      :disabled="
        !selectedCategoryTreeNode ||
        selectedCategoryTreeNode?.isLeafCategoryNode === RequiredYn.No
      "
      @click="onSelect"
    >
      {{ $t("product_platform.commonAdmin.select") }}
    </BaseButton>
  </div>
  <div v-else class="flex justify-space-between w-full px-[24px] py-[10px]">
    <div class="flex flex-wrap gap-[8px] w-full">
      <div class="flex gap-[8px]">
        <BaseSelectScroll
          v-model="categoryField"
          class="min-w-[120px] w-[120px]"
          :options="categoryFields"
          :height="48"
        />
        <BaseInputSearch
          v-model="searchCategoryText"
          label="categoryNode"
          class="!w-[240px]"
          density="compact"
          hide-details
          :height="'50px'"
          @handle-search="handleBtnSearch"
          @focus="saveLastInputBox(INPUT_NAME.CATEGORY)"
        />
      </div>
      <span
        class="flex align-end text-[#BDC1C7] px-[16px] py-[14px] text-[13px]"
        >{{ $t(`product_platform.OR`) }}</span
      >
      <div class="flex gap-[8px]">
        <BaseSelectScroll
          v-model="offerField"
          class="min-w-[120px] w-[120px] max-h-[50px]"
          :options="offerFields"
          :height="48"
        />
        <BaseInputSearch
          v-model="searchOfferText"
          label="Offer"
          class="!w-[240px]"
          density="compact"
          hide-details
          :height="'50px'"
          @handle-search="handleBtnSearch"
          @focus="saveLastInputBox(INPUT_NAME.OFFER)"
        />
      </div>
      <SearchAndRefreshButton
        class="ml-[6px] align-center h-[50px]"
        @handle-search="handleBtnSearch"
        @handle-refresh="handleBtnReset"
      />
    </div>
    <div class="flex align-center gap-2">
      <BaseButton
        v-if="tabView === CATEGORY_VIEW_MODE.LIST"
        :color="ButtonColorType.Gray"
        :width="WIDTH_BUTTON.EXCEL"
        @click="handleDownloadDataTable"
      >
        <DownloadIcon class="mr-[6px]" />
        {{ $t("product_platform.download") }}
      </BaseButton>

      <div v-if="tabView === CATEGORY_VIEW_MODE.TREE" class="flex gap-[8px]">
        <template v-if="isEdit">
          <BaseButton :color="ButtonColorType.Gray" @click="handleCancel()">
            {{ $t("product_platform.cancel") }}
          </BaseButton>

          <BaseButton :color="ButtonColorType.Secondary" @click="handleSave()">
            <SaveIcon class="mr-[6px]" />
            {{ $t("product_platform.save") }}
          </BaseButton>
        </template>
        <template v-else>
          <BaseButton
            :color="ButtonColorType.Secondary"
            @click="handleChangeEditView()"
          >
            <EditIcon class="mr-[6px]" />
            {{ $t("product_platform.edit") }}
          </BaseButton>
        </template>
      </div>
      <div>
        <CfSwitch
          :model="tabView"
          :items="CATEGORY_SWITCH_ITEM"
          @update:model="handleUpdateStatus"
        >
          <template #iconLeft>
            <ApplicationIcon />
          </template>
          <template #iconRight>
            <ListIcon />
          </template>
        </CfSwitch>
      </div>
    </div>
  </div>
  <base-popup
    v-model="openPopup"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.updatingConfirmSaved')"
    @on-close="closePopup"
    @on-submit="handleConfirm"
  />
  <base-popup
    v-model="openPopupCancel"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_cancel')"
    @on-close="closePopupCancel"
    @on-submit="handleConfirmCancel"
  />
  <base-popup
    v-model="openPopupSave"
    :icon="DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_update')"
    @on-close="closePopupSave"
    @on-submit="handleConfirmSave"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { DialogIconType, ButtonColorType, RequiredYn } from "@/enums";
import {
  CATEGORY_SWITCH_ITEM,
  CATEGORY_VIEW_MODE,
  CATEGORY_FIELD,
  OFFER_FIELD,
  WIDTH_BUTTON,
} from "@/constants/";
import { useCategoryStore, useMenuStore, useSnackbarStore } from "@/store";
import { useDownloadFile } from "@/composables/useDownloadFIle";
import { DOWNLOAD_EXCEL_CATEGORY_OFFER } from "@/api/prod/path";
import useOfferCreateProcessStore from "@/store/offerCreateProcess.store";
import { configPath, findMenuItem } from "@/utils/config-path";
import { MenuItemID } from "@/enums/redirect";

const props = defineProps({
  tab: {
    type: String,
    require: true,
    default: "",
  },
  isReset: {
    type: Boolean,
    require: true,
  },
  fetchSearchOffer: {
    type: Function as PropType<() => Promise<void>>,
    default: () => {},
  },
});

const emit = defineEmits(["resetStucture", "resetOfferListActiveStatus"]);

const { t, locale } = useI18n();

const { showSnackbar } = useSnackbarStore();
const categoryStore = useCategoryStore();
const {
  reChooseCategoryMode,
  categorySelected,
  isFormOfferEdit,
  categoryEditModeSelected,
  categoryDuplicateModeSelected,
  isFormOfferDuplicate,
} = storeToRefs(useOfferCreateProcessStore());
const { menuTree } = storeToRefs(useMenuStore());
const {
  categoryField,
  searchCategoryText,
  isRedirectFormUserPocket,
  tabs,
  offerItemFormUserPocket,
} = storeToRefs(categoryStore);

const categoryFields = ref(CATEGORY_FIELD);
const offerFields = ref(OFFER_FIELD);
const openPopup = ref(false);
const openPopupCancel = ref(false);
const openPopupSave = ref(false);
const offerField = ref(
  categoryStore.getSelectedOfferBoxValue || OFFER_FIELD[0].value
);
const searchMode = ref(categoryStore.setSelectedOfferBoxValue);
const searchOfferText = ref("");
const tabView = ref(categoryStore.getCategoryView);
const lastInputFocus = ref(null);
const INPUT_NAME = {
  CATEGORY: "category",
  OFFER: "offer",
};

const { downloadFile } = useDownloadFile();
const validStatus = ref(true);
const newNode = ref(null);
const closePopup = () => {
  openPopup.value = false;
};
const closePopupCancel = () => {
  openPopupCancel.value = false;
};
const closePopupSave = () => {
  openPopupSave.value = false;
};
const handleConfirm = () => {
  categoryStore.setEditSearch(true);
  handleSearch();
};

const selectedCategoryTreeNode = computed(
  () => categoryStore.getSelectedCategoryTreeNode
);

const handleSearch = async () => {
  emit("resetStucture");
  if (!searchCategoryText.value && !searchOfferText.value) {
    await handleSearchCategory();
    await handleSearchOffer();
  } else {
    if (lastInputFocus.value === INPUT_NAME.CATEGORY) {
      await handleSearchCategory();
    } else {
      await handleSearchOffer();
    }
  }
  openPopup.value = false;
};

const handleUpdateStatus = (event) => {
  categoryStore.setCategoryViewType(event);
  tabView.value = event;
};
const isEdit = computed(() => categoryStore.getIsEdit);

onMounted(() => {
  if (tabView.value === CATEGORY_VIEW_MODE.LIST) {
    if (categoryStore.getSelectedOfferBoxValue.value == OFFER_FIELD[0].value) {
      searchOfferText.value = categoryStore.getSearchListParams.offerNm;
    } else {
      searchOfferText.value = categoryStore.getSearchListParams.offerCd;
    }
    searchCategoryText.value = categoryStore.getSearchListParams.ctgrNodeName;
  } else {
    if (categoryStore.getSelectedOfferBoxValue.value == OFFER_FIELD[0].value) {
      searchOfferText.value =
        categoryStore.getCategoryOfferTreeSearchParam.offerNm;
    } else {
      searchOfferText.value =
        categoryStore.getCategoryOfferTreeSearchParam.offerCd;
    }
    searchCategoryText.value =
      categoryStore.getSearchCategoryFilterObj.searchText;
  }
});
const handleSearchCategory = async () => {
  searchOfferText.value = "";
  if (tabView.value === CATEGORY_VIEW_MODE.LIST) {
    categoryStore.setSearchOfferName("");
  } else {
    if (!categoryStore.getIsEdit) {
      await categoryStore.getTreeCategory();
      categoryStore.setIsFetchData(true);
    }
    if (
      searchCategoryText.value &&
      !categoryStore.getSearchCategoryFilterObj.searchText
    ) {
      categoryStore.setSearchCategoryFilterObj(searchCategoryText.value);
    }
    categoryStore.setSearchOfferFilterObjValue("");
  }
  categoryStore.setSearchStatus(true);
};

const handleSearchOffer = async () => {
  categoryStore.setIsSearchProductOfNode(false);
  searchCategoryText.value = "";
  if (tabView.value === CATEGORY_VIEW_MODE.LIST) {
    categoryStore.setSearchCategoryName("");
    if (offerField._value === "offerCd") {
      categoryStore.setSearchOfferCode(searchOfferText.value);
      categoryStore.setSearchOfferName("");
    } else if (offerField._value === "offerNm") {
      categoryStore.setSearchOfferName(searchOfferText.value);
      categoryStore.setSearchOfferCode("");
    }
    categoryStore.setSearchStatus(true);
  } else {
    categoryStore.setCategoryOfferSearchParam(
      searchOfferText.value,
      offerField._value
    );
    categoryStore.setSearchCategoryFilterObj("");
    categoryStore.setSearchProductStatus(true);
    categoryStore.setOpenOfferPanel(true);
  }
};

const logicSearchForOffer = () => {
  if (searchMode.value === "offerNm") {
    categoryStore.setSearchOfferName(searchOfferText.value);
  } else if (searchMode.value === "offerCd") {
    categoryStore.setSearchOfferCode(searchOfferText.value);
  }
};

const handleBtnReset = async () => {
  categoryField.value = CATEGORY_FIELD[0].value;
  searchCategoryText.value = "";
  offerField.value = OFFER_FIELD[0].value;
  searchOfferText.value = "";
  categoryStore.setSearchCategoryName("");
  categoryStore.setSelectedOfferBoxValue(OFFER_FIELD[0]);
  categoryStore.setSearchOfferName("");
  categoryStore.setSearchOfferCode("");
  if (categoryStore.getCategoryView === CATEGORY_VIEW_MODE.LIST) {
    categoryStore.setSearchStatus(true);
  }
};

const handleBtnSearch = () => {
  validStatus.value = true;
  validationUpdateList(categoryStore.getTreeData);
  categoryStore.setIsEmptyOfferList(false);
  if (!validStatus.value && categoryStore.getIsEdit) {
    categoryStore.setSelectedCategoryTreeNode(newNode.value);
    openPopup.value = true;
  } else {
    handleSearch();
  }
};

const saveLastInputBox = (name) => {
  lastInputFocus.value = name;
};
const replaceTab = inject<any>("replaceTab");

const onSelect = () => {
  if (isFormOfferEdit.value) {
    categoryEditModeSelected.value = selectedCategoryTreeNode.value;
    const newRedirectObj = findMenuItem(menuTree.value, MenuItemID.OfferSearch);
    if (newRedirectObj) {
      newRedirectObj["path"] = configPath(newRedirectObj);
      isFormOfferEdit.value = false;
      replaceTab(newRedirectObj);
    }
  } else if (isFormOfferDuplicate.value) {
    categoryDuplicateModeSelected.value = selectedCategoryTreeNode.value;
    categoryEditModeSelected.value = null;
    const newRedirectObj = findMenuItem(
      menuTree.value,
      MenuItemID.OfferDuplicate
    );
    if (newRedirectObj) {
      newRedirectObj["path"] = configPath(newRedirectObj);
      isFormOfferDuplicate.value = false;
      replaceTab(newRedirectObj);
    }
  } else {
    categorySelected.value = selectedCategoryTreeNode.value;
    categoryEditModeSelected.value = null;
    categoryDuplicateModeSelected.value = null;
    const newRedirectObj = findMenuItem(menuTree.value, MenuItemID.OfferCreate);
    if (newRedirectObj) {
      newRedirectObj["path"] = configPath(newRedirectObj);
      isFormOfferEdit.value = false;
      replaceTab(newRedirectObj);
    }
  }
  reChooseCategoryMode.value = false;
  categoryStore.setSelectedCategoryTreeNode({});
  categoryStore.setTreeData({});
  categoryStore.setItemIdShowOffer("");
  categoryStore.setProductData([]);
};

const handleDownloadDataTable = async () => {
  const language: any = locale.value;
  const params: any = {
    ctgrTabUuid: categoryStore.getSearchListParams.ctgrTabUuid,
    offerCd: categoryStore.getSearchListParams.offerCd,
    offerNm: categoryStore.getSearchListParams.offerNm,
    ctgrNodeName: categoryStore.getSearchListParams.ctgrNodeName,
    language: language,
  };
  await downloadFile(DOWNLOAD_EXCEL_CATEGORY_OFFER, params, generateFileName());
};

const generateFileName = () => {
  return `TreeView(${categoryStore.getFileNameExport})`;
};

const handleChangeEditView = () => {
  categoryStore.setIsEdit(true);
};

const handleConfirmCancel = () => {
  categoryStore.setListOfferUpdateNotSave([]);
  refetchOfferList();
  resetLeafNode();
  categoryStore.setIsEdit(false);
  openPopupCancel.value = false;
};

const handleCancel = () => {
  openPopupCancel.value = true;
};

const handleConfirmSave = () => {
  categoryStore.setListOfferUpdateNotSave([]);
  refetchOfferList();
  resetLeafNode();
  categoryStore.setSaveStatus(true);
  openPopupSave.value = false;
};

const handleSave = () => {
  openPopupSave.value = true;
};

const refetchOfferList = () => {
  if (!categoryStore.getIsSearchProductOfNode) {
    handleSearchOffer();
  }
};

const validationUpdateList = (rootArray) => {
  if (rootArray?.length) {
    rootArray.forEach((catg) => {
      if (!catg?.ctgrNodeName && catg?.isNew) {
        validStatus.value = false;
        newNode.value = catg;
        return;
      }
      validationUpdateList(catg.children);
    });
  }
};

watch(
  isRedirectFormUserPocket,
  (value) => {
    if (value) {
      setTimeout(() => {
        const currentTab = tabs.value.find(
          ({ ctgrTabName }) =>
            ctgrTabName === offerItemFormUserPocket.value?.subType
        );
        categoryStore.setCategoryTab(
          currentTab.ctgrTabName.toUpperCase().replace("-", "")
        );
        nextTick(() => {
          emit("resetStucture");
          tabView.value = CATEGORY_VIEW_MODE.TREE;
          offerField.value = OFFER_FIELD[1].value;
          searchOfferText.value = offerItemFormUserPocket.value?.code || "";
          setTimeout(async () => {
            categoryStore.setIsEmptyOfferList(false);
            categoryStore.setSearchCategoryFilterObj("");
            categoryStore.setIsSearchProductOfNode(false);
            categoryStore.setCategoryOfferSearchParamPageNo(1);
            categoryStore.setOpenOfferPanel(true);
            await props.fetchSearchOffer();
            const data = categoryStore.getCategoryOfferTreeSearchResultData;
            nextTick(() => {
              categoryStore.setSearchCategoryFilterObjAction(false);
              categoryStore.setChildTreeViewStatus(true);
              if (data?.elements.length > 0) {
                emit("resetOfferListActiveStatus", {
                  ...data?.elements[0],
                  isRedirect: true,
                });
              } else {
                showSnackbar(
                  t("product_platform.there_is_no_corresponding_category"),
                  "error"
                );
              }
            });
            isRedirectFormUserPocket.value = false;
            offerItemFormUserPocket.value = null;
          }, 100);
        });
      }, 100);
    }
  },
  { immediate: true }
);

watch(
  () => categoryStore.getIsEdit,
  (newVal) => {
    if (!newVal) {
      searchOfferText.value = "";
      searchCategoryText.value = "";
      categoryStore.setSearchCategoryFilterObj("");
      categoryStore.setSearchOfferFilterObjValue("");
      handleSearchCategory();
    }
  }
);

watch(
  () => searchCategoryText.value,
  (value) => {
    if (tabView.value === CATEGORY_VIEW_MODE.LIST) {
      categoryStore.setSearchCategoryName(value);
    } else {
      categoryStore.setSearchCategoryFilterObj(value);
    }
  }
);

watch(
  () => searchOfferText.value,
  (value) => {
    if (tabView.value === CATEGORY_VIEW_MODE.LIST) {
      if (searchMode.value === "offerNm") {
        categoryStore.setSearchOfferName(value);
      } else {
        categoryStore.setSearchOfferCode(value);
      }
    } else {
      categoryStore.setCategoryOfferSearchParam(value, offerField._value);
    }
  }
);

watch(
  () => offerField.value,
  (value) => {
    categoryStore.setSelectedOfferBoxValue(value);
    if (tabView.value === CATEGORY_VIEW_MODE.LIST) {
      searchMode.value = value;
      logicSearchForOffer();
    } else {
      categoryStore.setSearchOfferFilterObjField(value);
    }
  }
);

watch(
  () => tabView.value,
  (val) => {
    if (val === CATEGORY_VIEW_MODE.LIST) {
      if (
        categoryStore.getSelectedOfferBoxValue.value == OFFER_FIELD[0].value
      ) {
        searchOfferText.value = categoryStore.getSearchListParams.offerNm;
      } else {
        searchOfferText.value = categoryStore.getSearchListParams.offerCd;
      }
      searchCategoryText.value = categoryStore.getSearchListParams.ctgrNodeName;
    } else {
      if (
        categoryStore.getSelectedOfferBoxValue.value == OFFER_FIELD[0].value
      ) {
        searchOfferText.value =
          categoryStore.getCategoryOfferTreeSearchParam.offerNm;
      } else {
        searchOfferText.value =
          categoryStore.getCategoryOfferTreeSearchParam.offerCd;
      }
      searchCategoryText.value =
        categoryStore.getSearchCategoryFilterObj.searchText;
    }
  }
);
watch(
  () => props.isReset,
  (val) => {
    if (val) {
      searchOfferText.value = "";
      searchCategoryText.value = "";
    }
  }
);

const resetLeafNode = inject<any>("handleResetOfferList");
</script>

<style scoped lang="scss">
.form-btn {
  :deep(.v-btn) {
    border-color: #dce0e5;
  }
}
.btn-select {
  :deep(.v-btn--disabled) {
    background-color: #f0f2f5 !important;
    opacity: 0.2 !important;
  }
}

:deep(.btn-custom span) {
  margin-left: unset !important;
}
:deep(.v-field__input) {
  min-height: 48px !important;
}
</style>
