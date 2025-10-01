<template>
  <div :class="['px-[24px] pt-[24px]', isGridMode ? 'pb-[16px]' : '']">
    <div
      :class="[
        'flex justify-space-between align-center',
        isGridMode ? 'pb-[8px]' : '',
      ]"
    >
      <div
        v-if="isGridMode"
        class="font-medium text-base text-text-base tracking-[0.5px]"
      >
        {{
          offerDuplicateMode
            ? $t("product_platform.duplicateRelation")
            : $t("product_platform.relation.relationViewTitle")
        }}
      </div>
      <GridSearch v-else ref="selectTableScroll" />
      <div class="flex gap-[8px]">
        <FileAction
          v-if="!isGridMode"
          title="Upload Impact Analysis"
          description="Please upload Impact Analysis excel file that you have downloaded."
          :is-downloading="downloading"
          @upload-file="handleUploadRelationManager"
          @download-file="handleDownloadDataTable"
        />
        <template v-if="isEdit && isGridMode">
          <BaseButton
            v-if="isEdit && !offerDuplicateMode"
            :color="ButtonColorType.Gray"
            @click="handleCancel"
          >
            {{ $t("product_platform.cancel") }}
          </BaseButton>

          <BaseButton
            v-if="isEdit && offerDuplicateMode"
            :color="ButtonColorType.Gray"
            @click="handleCanceDuplicate"
          >
            {{ $t("product_platform.cancel") }}
          </BaseButton>

          <BaseButton :color="ButtonColorType.Secondary" @click="handleSave">
            <SaveIcon class="mr-[6px]" />
            {{ $t("product_platform.save") }}
          </BaseButton>
        </template>
        <template v-if="!isEdit && isGridMode">
          <ViewButton :offer-duplicate-mode="offerDuplicateMode" />
          <BaseButton
            v-if="extendsView === EXTENDS_VIEW.SIMPLE"
            :color="ButtonColorType.Secondary"
            @click="handleChangeEditView(true)"
          >
            <EditIcon class="mr-[6px]" />
            {{ $t("product_platform.edit") }}
          </BaseButton>
        </template>
        <SwitchViewTable
          v-if="!offerDuplicateMode"
          :model-value="viewMode"
          class="h-[43px] w-[82px]"
          @toggle-view-mode="handleSwitchView"
        >
        </SwitchViewTable>
      </div>
    </div>
    <v-form v-if="isGridMode" ref="form" @submit.prevent="">
      <v-row v-if="!isEdit" class="gap-2 align-center" no-gutters>
        <v-col style="max-width: 240px">
          <BaseSelectScroll
            ref="selectScroll"
            v-model="paramsHightlightSearch.type"
            :options="selectList"
            :placeholder="$t('product_platform.type')"
            :height="48"
            class="w-full"
            required
          />
        </v-col>
        <v-col style="max-width: 120px">
          <BaseSelectScroll
            v-model="paramsHightlightSearch.searchBy"
            :height="48"
            :options="NM_CD_FIELDS"
            :default-item-select-all="false"
          />
        </v-col>
        <v-col style="max-width: 240px">
          <BaseInputSearch
            v-model="inputValue"
            density="comfortable"
            label="search"
            variant="solo"
            hide-details
            single-line
            rounded="4"
            @handle-search="handleSearch"
          />
        </v-col>
        <SearchAndRefreshButton
          class="ml-[6px]"
          @handle-search="handleSearch"
          @handle-refresh="handleResetSearch"
        />
      </v-row>
    </v-form>
  </div>
  <BasePopup
    v-model="openPopup"
    :icon="isCancel ? DialogIconType.Warning : DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="
      isCancel
        ? $t('product_platform.desc_cancel')
        : $t('product_platform.desc_update')
    "
    @on-submit="handleSubmit"
  />
  <BasePopup
    v-model="openPopupCancelDlcRelation"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_cancel_dlc_relation')"
    @on-submit="handleSubmitCancelDlcRelation"
  />
</template>

<script setup lang="ts">
import {
  EXTENDS_VIEW,
  SELECT_LIST_DETAIL,
  SELECT_LIST_SIMPLE,
} from "@/constants/extendsManager";
import { ButtonColorType, DialogIconType, SearchBy } from "@/enums";
import {
  useSnackbarStore,
  useExtendManagerStore,
  useOfferDuplicateProcessStore,
  useProductsStore,
  useRelationManagerDuplicateStore,
  useDuplicateStructureStore,
} from "@/store";
import { useI18n } from "vue-i18n";
import { MenuItemID } from "@/enums/redirect";
import { useDownloadFile } from "@/composables/useDownloadFIle";
import { appProvider, AppProvider } from "@/types/common";
import useRedirect from "@/composables/useRedirect";
import { EXPORT_EXCEL_RELATION_MANAGER } from "@/api/prod/path";
import { NM_CD_FIELDS } from "@/constants/impactAnalysis";

const props = defineProps({
  offerDuplicateMode: {
    type: Boolean,
    default: false,
  },
});
const extendManagerStore = useExtendManagerStore();
const relationManagerDuplicateStore = useRelationManagerDuplicateStore();
const offerDuplicateProcessStore = useOfferDuplicateProcessStore();
const structureDuplicateStore = useDuplicateStructureStore();
const productStore = useProductsStore();
const route = useRoute();
const snackbarStore = useSnackbarStore();
const { t, locale } = useI18n();
const { offerDuplicateFinishProcess } = storeToRefs(productStore);
const { offerDuplicated } = storeToRefs(offerDuplicateProcessStore);
const selectedStore = computed(() =>
  props.offerDuplicateMode ? relationManagerDuplicateStore : extendManagerStore
);
const { moveOfferSearchPage } = useRedirect();

const {
  paramsHightlightSearch,
  isEdit,
  extendsView,
  sideDisplay,
  paramsExtendsTargetPost,
  detailViewData,
  expandLeaderItem,
  expandLeaderItemIndex,
  expandFollowerItem,
  expandFollowerItemIndex,
  selectedItem,
  viewMode,
  isGridMode,
  paramListView,
} = storeToRefs(selectedStore.value);
const {
  resetHightlightParamSearch,
  getLeaderList,
  getFollowerList,
  resetLeaderFollowerList,
  postExtendTarger,
  resetParamsExtendsTargetPost,
  resetStructureActiveMap,
  getGroupBySelectedItem,
  getRelationDataTable,
} = selectedStore.value;
const useSnackbar = useSnackbarStore();
const { downloading, downloadFile } = useDownloadFile();
const { onBulkUploadFile } = inject<AppProvider>(appProvider, {
  onBulkUploadFile: async () => {},
});

const inputValue = ref();
const form = ref();
const selectScroll = ref();
const selectTableScroll = ref();
const openPopup = ref(false);
const openPopupCancelDlcRelation = ref(false);
const isCancel = ref(false);

const selectList = computed(() => {
  if (extendsView.value === EXTENDS_VIEW.SIMPLE) {
    return SELECT_LIST_SIMPLE;
  } else {
    return SELECT_LIST_DETAIL;
  }
});

const handleSearch = async () => {
  selectScroll.value?.validate();
  const { valid } = await form.value?.validate();
  if (!valid || !paramsHightlightSearch.value?.type) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }
  resetStructureActiveMap();
  paramsHightlightSearch.value.keyword = inputValue.value;
};

// TODO
const handleUploadRelationManager = async (file: File): Promise<void> => {
  onBulkUploadFile("", file, route.path, getRelationDataTable);
};

const handleDownloadDataTable = async () => {
  try {
    if (!selectedItem.value || !selectedItem.value.prodUuid) return;
    // if (!paramListView.value?.category?.trim()) {
    //   selectTableScroll.value?.validateSelect();
    //   useSnackbar.showSnackbar(
    //     t("product_platform.required_field_missing"),
    //     "error"
    //   );
    //   return;
    // }
    const params: any = {
      ...paramListView.value,
      language: locale.value || "en",
    };
    await downloadFile(
      EXPORT_EXCEL_RELATION_MANAGER,
      params,
      "RelationManager"
    );
  } catch (err: any) {
    useSnackbar.showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const handleSwitchView = (value: string): void => {
  if (value === "LIST") {
    sideDisplay.value.targetSearch = false;
    sideDisplay.value.relationSearch = false;
    sideDisplay.value.relationDetail = false;
    sideDisplay.value.targetDetail = false;
    sideDisplay.value.offerDetail = false;
    paramListView.value.category = paramsHightlightSearch.value.type || " ";
    paramListView.value.type =
      paramsHightlightSearch.value.searchBy || SearchBy.Name;
    paramListView.value.value = inputValue.value || "";
    if (selectedItem.value || selectedItem.value?.prodUuid) {
      getRelationDataTable();
    }
  } else {
    paramsHightlightSearch.value.type =
      paramListView.value.category || undefined;
    paramsHightlightSearch.value.searchBy =
      paramListView.value.type || SearchBy.Name;
    paramsHightlightSearch.value.keyword =
      paramListView.value.value || undefined;
    if (paramListView.value.value) {
      inputValue.value = paramListView.value.value;
    }
  }
  viewMode.value = value;
};

const handleResetSearch = () => {
  selectScroll.value.resetValidate();
  form.value.resetValidation();
  resetHightlightParamSearch();
  inputValue.value = null;
};

const handleChangeEditView = (value) => {
  isEdit.value = value;
  if (extendsView.value === EXTENDS_VIEW.SIMPLE) {
    sideDisplay.value.offerSearch = !value;
  }
};

const handleSave = async () => {
  let isValid =
    !!paramsExtendsTargetPost.value.addOffrDpdcLst.length ||
    props.offerDuplicateMode;
  detailViewData.value.focusColumnFollowerList.forEach((prod) => {
    if (prod?.leaderList?.length) {
      prod?.leaderList.forEach((rel) => {
        if (rel.isAdded && (!rel.child || !rel.child?.length)) {
          isValid = false;
        }
      });
    }
  });
  detailViewData.value.focusColumnLeaderList.forEach((prod) => {
    if (prod?.followerList?.length) {
      prod?.followerList.forEach((rel) => {
        if (rel.isAdded && (!rel.child || !rel.child?.length)) {
          isValid = false;
        }
      });
    }
  });

  if (isValid) {
    isCancel.value = false;
    openPopup.value = true;
  } else {
    snackbarStore.showSnackbar(
      t("product_platform.relation.updateMessageInvalid"),
      "error"
    );
  }
};

const handleCancel = () => {
  isCancel.value = true;
  openPopup.value = true;
  // if (props.offerDuplicateMode) {
  //   finnishProcessDuplicate();
  // }
};
const handleCanceDuplicate = () => {
  openPopupCancelDlcRelation.value = true;
};
const handleSubmitCancelDlcRelation = () => {
  openPopupCancelDlcRelation.value = false;
  if (props.offerDuplicateMode) {
    finnishProcessDuplicate();
  }
};

const handleSubmit = async () => {
  if (isCancel.value) {
    isEdit.value = false;
    resetLeaderFollowerList();
    getLeaderList(
      extendsView.value === EXTENDS_VIEW.SIMPLE
        ? selectedItem.value?.objUuid
        : expandLeaderItem.value?.offerGroupUuid,
      props.offerDuplicateMode,
      extendsView.value === EXTENDS_VIEW.SIMPLE
        ? 0
        : expandFollowerItemIndex.value,
      extendsView.value === EXTENDS_VIEW.SIMPLE
    );
    getFollowerList(
      extendsView.value === EXTENDS_VIEW.SIMPLE
        ? selectedItem.value?.objUuid
        : expandFollowerItem.value?.offerGroupUuid,
      props.offerDuplicateMode,
      extendsView.value === EXTENDS_VIEW.SIMPLE
        ? 0
        : expandLeaderItemIndex.value,
      extendsView.value === EXTENDS_VIEW.SIMPLE
    );
    detailViewData.value.isShowFollowerCol = false;
    detailViewData.value.isShowLeaderCol = false;
    openPopup.value = false;
  } else {
    const res = await postExtendTarger(props.offerDuplicateMode);
    if (res && res?.status === 200) {
      snackbarStore.showSnackbar(
        t("product_platform.successfully_saved"),
        "success"
      );
      if (props.offerDuplicateMode) {
        finnishProcessDuplicate();
        return;
      }
      await getGroupBySelectedItem(
        extendsView.value === EXTENDS_VIEW.SIMPLE,
        props.offerDuplicateMode
      );
    } else {
      snackbarStore.showSnackbar(
        t("product_platform.something_went_wrong"),
        "error"
      );
    }
    resetParamsExtendsTargetPost();
    resetLeaderFollowerList();
    getLeaderList(
      expandFollowerItem.value?.offerGroupUuid,
      props.offerDuplicateMode,
      expandFollowerItemIndex.value,
      extendsView.value === EXTENDS_VIEW.SIMPLE
    );
    getFollowerList(
      expandLeaderItem.value?.offerGroupUuid,
      props.offerDuplicateMode,
      expandLeaderItemIndex.value,
      extendsView.value === EXTENDS_VIEW.SIMPLE
    );
    detailViewData.value.isShowFollowerCol = false;
    detailViewData.value.isShowLeaderCol = false;

    openPopup.value = false;
    isEdit.value = false;
  }
  detailViewData.value.isShowLeaderCol = !detailViewData.value.isShowLeaderCol;
  detailViewData.value.isShowFollowerCol =
    !detailViewData.value.isShowFollowerCol;
  nextTick(() => {
    detailViewData.value.isShowLeaderCol =
      !detailViewData.value.isShowLeaderCol;
    detailViewData.value.isShowFollowerCol =
      !detailViewData.value.isShowFollowerCol;
  });
};

const finnishProcessDuplicate = async () => {
  offerDuplicateFinishProcess.value = true;
  moveOfferSearchPage({
    itemCode: offerDuplicated.value.itemCode,
    objCode: offerDuplicated.value.objCode,
    objUuid: offerDuplicated.value.objUuid,
  });
  structureDuplicateStore.resetStructure();
  removeTab(MenuItemID.RelationDuplicate);

  // const newRedirectObj = findMenuItem(menuTree.value, MenuItemID.OfferSearch);
  // if (newRedirectObj) {
  //   inputText.value = offerDuplicated.value.objCode;
  //   selectedValue.value = "code";
  //   offerDuplicateProcessStore.resetProcess();
  //   newRedirectObj["path"] = configPath(newRedirectObj);
  //   replaceTab(newRedirectObj, true);
  // }
};

watch(
  () => extendsView.value,
  () => {
    inputValue.value = null;
    resetHightlightParamSearch();
    resetStructureActiveMap();
  }
);
const removeTab = inject<any>("removeTab");
</script>

<style lang="scss" scoped>
.btn-custom:deep(span) {
  margin-left: 0px;
  color: #6b6d70;
}

:deep(.table-total-result) {
  padding-top: 12px !important;
}
</style>
