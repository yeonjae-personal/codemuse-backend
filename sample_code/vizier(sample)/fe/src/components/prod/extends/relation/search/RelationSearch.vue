<template>
  <SearchPane
    ref="searchPaneRef"
    title="product_platform.relation.relationSearchTitle"
    :model-list="extendRelationSearch.items || []"
    :pagination="extendRelationSearch?.pagination"
    :pane-col="ColNumber.Two"
    :show-float-icon-left="!isSearchOrCreate"
    :open-popup="isShowPopup"
    :popup-content="popupContent"
    icon-left-class="right-0"
    container-class="rounded-lg"
    :item-height="48"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-click-float-left="handleCloseRelationPane"
    @close-popup="handleClosePopup"
    @submit-popup="handleSubmitPopup"
    @on-change-page="handleChangePage"
  >
    <template #search-button-append>
      <SearchDetailButton
        v-if="isSearchOrCreate"
        class="bg-white ml-2"
        :is-active="isApplied"
        @click="handleSearchDetail"
      />
    </template>
    <template #custom-form>
      <div class="flex align-center gap-2 mt-2">
        <BaseSelectScroll
          v-model="selectedNmCdRelationSearch"
          class-name="min-w-[120px]"
          :height="48"
          :options="optionsSearchType"
          :default-item-select-all="false"
          @update:model-value="handleChangeRelationNmCd"
        />
        <BaseInputSearch
          v-model="initInput"
          density="comfortable"
          label="search"
          variant="solo"
          hide-details
          single-line
          rounded="4"
          @handle-search="handleEnterSearch"
          @update:model-value="handleChangeInput"
        />
      </div>
    </template>
    <template #custom-search-item="{ item }">
      <ExtendsAccordion
        v-model:expand="item.isExpand"
        type="normal"
        width="100%"
        class="!py-0"
        :active="item.objUuid === selectedRelation?.objUuid"
        :title="item.objName"
        :search-text="initInput"
        :search-field="
          selectedNmCdRelationSearch === 'objName' ? 'name' : 'code'
        "
        :draggable="
          page === RELATION_PAGE.SEARCH
            ? !isExpired(item?.validEndDtm) && true
            : isEdit
        "
        :is-show-list-product="false"
        :disable="isExpired(item?.validEndDtm)"
        :editable="page === RELATION_PAGE.SEARCH"
        :actions="listActions(item)"
        @dragstart="handleDragStart($event, item)"
        @dragend="handleDragEnd"
        @on-click="handleClickRelation($event, item)"
        @on-expand="handleExpandDetail($event, item)"
      >
        <template v-if="item?.detail">
          <ProductGrid :data="item.detail" :type="LARGE_ITEM_CODE.RELATION" />
        </template>
      </ExtendsAccordion>
    </template>
  </SearchPane>
  <AdvancedSearch
    v-if="isOpenSearchDetail"
    v-model="isOpenSearchDetail"
    class-custom="search-detail-relation"
    :model-list="advancedSearchList"
    @on-close="closePopupSearchDetail"
    @on-submit="applyPopupSearchDetail"
    @on-reset="handleResetSearchDetail"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import { RELATION_PAGE, SELECT_LIST_TYPE } from "@/constants/extendsManager";
import {
  useExtendManagerStore,
  useDragStore,
  useSnackbarStore,
  useRelationSearchStore,
  useHistoryTabStore,
  useRelationManagerDuplicateStore,
} from "@/store";
import { NM_CD_FIELDS } from "@/constants/impactAnalysis";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { getExtendsDependencyRelationDefinition } from "@/api/prod/extendsApi";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import { filterParamsAdvanced, isExpired } from "@/utils/format-data";
import { ColNumber } from "@/enums";
import { ITEM_ACTIONS_LIST_TYPE } from "@/constants/index";
import DuplicateIcon from "@/components/prod/icons/DuplicateIcon.vue";
import SearchPane from "@/components/prod/shared/SearchPane.vue";
import type { ActionType } from "@/interfaces/prod";

const props = defineProps({
  page: {
    type: String,
    default: RELATION_PAGE.MANAGER,
  },
});

const { t } = useI18n();

const historyStore = useHistoryTabStore();
const extendManagerStore = useExtendManagerStore();
const relationSearchStore = useRelationSearchStore();
const relationManagerDuplicateStore = useRelationManagerDuplicateStore();

const selectedStore = computed<any>(() => {
  switch (props.page) {
    case RELATION_PAGE.SEARCH:
      return relationSearchStore;
    case RELATION_PAGE.DUPLICATE:
      return relationManagerDuplicateStore;
    default:
      return extendManagerStore;
  }
});

const {
  sideDisplay,
  paramsExtendsRelationSearch,
  selectedNmCdRelationSearch,
  extendRelationSearch,
  isEdit,
  isDuplicate,
  selectedRelation,
  isShowRelationDetail,
  advancedSearchList,
  isApplied,
  initInput,
} = storeToRefs(selectedStore.value);
const {
  getRelationSearch,
  resetRelationParamSearch,
  setAdvancedSearchList,
  getExtendsDependencyRelationDefinitionDetail,
} = selectedStore.value;
const { dragOfferType, isDragging } = storeToRefs(useDragStore());
const useSnackbar = useSnackbarStore();
const { handleDragUserPocket } = useDragUserPocket();

const searchPaneRef = ref<InstanceType<typeof SearchPane>>();
const isShowPopup = ref<boolean>(false);
const isOpenSearchDetail = ref<boolean>(false);
const popupData = ref<any>(null);
const actionType = ref<string>(ITEM_ACTIONS_LIST_TYPE.DETAIL);

const isSearchOrCreate = computed<boolean>(() =>
  [RELATION_PAGE.CREATE, RELATION_PAGE.SEARCH].includes(props.page)
);

const optionsSearchType = computed(() => NM_CD_FIELDS);

const popupContent = computed<string>(() => {
  if (isEdit.value) return t("product_platform.groupCancelEdit");
  if (isDuplicate.value) return t("product_platform.groupCancelDuplicate");
  return "";
});

const listActions = (item: any): ActionType[] => {
  return [
    {
      name: t("product_platform.duplicate"),
      icon: DuplicateIcon,
      onClick: async () => {
        actionType.value = ITEM_ACTIONS_LIST_TYPE.DUPLICATE;
        if (isDuplicate.value || isEdit.value) {
          isShowPopup.value = true;
          popupData.value = item;
          return;
        }
        handleDuplicate(item);
      },
    },
  ];
};

const closePopupSearchDetail = (): void => {
  isOpenSearchDetail.value = false;
};

const applyPopupSearchDetail = async (event: any): Promise<void> => {
  paramsExtendsRelationSearch.value.general = event.general;
  paramsExtendsRelationSearch.value.additional = event.additional;
  paramsExtendsRelationSearch.value.page = 1;
  isApplied.value = true;
  await getListRelation();
  isOpenSearchDetail.value = false;
};

const handleResetSearchDetail = async (): Promise<void> => {
  isApplied.value = false;
  paramsExtendsRelationSearch.value.general = [];
  paramsExtendsRelationSearch.value.additional = [];
  closePopupSearchDetail();
  await getListRelation();
};

const handleExpandDetail = async (event: any, rel: any): Promise<void> => {
  if (event && !rel?.detail) {
    const { data } = await getExtendsDependencyRelationDefinition(rel.objUuid);
    rel["detail"] = data;
  }
  rel["isExpand"] = event;
};

const handleDuplicate = async (item: any): Promise<void> => {
  selectedRelation.value = cloneDeep(item);
  await getExtendsDependencyRelationDefinitionDetail(item.objUuid, true);
  isDuplicate.value = true;
  isShowRelationDetail.value = true;
  popupData.value = null;
};

const handleClickRelation = async (event: any, rel: any): Promise<void> => {
  if (!isSearchOrCreate.value) {
    selectedRelation.value = rel;
    handleExpandDetail(event, rel);
  } else {
    actionType.value = ITEM_ACTIONS_LIST_TYPE.DETAIL;
    if (isDuplicate.value || isEdit.value) {
      isShowPopup.value = true;
      popupData.value = rel;
      return;
    }
    await getDetailRelation(rel);
    await historyStore.fetchHistory({
      objUuid: selectedRelation.value?.objUuid,
    });
  }
};

const handleClosePopup = (): void => {
  isShowPopup.value = false;
};

const getDetailRelation = async (rel: any): Promise<void> => {
  if (selectedRelation.value?.objUuid !== rel?.objUuid) {
    selectedRelation.value = rel;
    if (props.page === RELATION_PAGE.SEARCH) {
      await getExtendsDependencyRelationDefinitionDetail(rel.objUuid);
    }
  }
  isShowRelationDetail.value = true;
};

const handleSearchDetail = async (): Promise<void> => {
  if (!isApplied.value) {
    await setAdvancedSearchList();
  }
  isOpenSearchDetail.value = true;
};

const handleSearch = async (
  pageSize?: number,
  isClick: boolean = false,
  page = 1
): Promise<void> => {
  try {
    paramsExtendsRelationSearch.value.page = isClick ? 1 : page;
    await getListRelation(pageSize, isClick);
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};

const handleChangePage = async (page: number): Promise<void> => {
  paramsExtendsRelationSearch.value = {
    ...paramsExtendsRelationSearch.value,
    page,
  };
  await getListRelation(undefined, false);
};

const getListRelation = async (
  pageSize?: number,
  isClick: boolean = true
): Promise<void> => {
  if (!pageSize) {
    searchPaneRef.value?.calcTotalItem();
  }
  const size = pageSize ? pageSize : searchPaneRef.value?.totalItem;
  if (isClick) {
    selectedRelation.value = null;
    isEdit.value = false;
  }
  if (!isSearchOrCreate.value) {
    paramsExtendsRelationSearch.value = {
      ...paramsExtendsRelationSearch.value,
      onlyValidDtm: true,
      size: size || 10,
    };
    await getRelationSearch();
    return;
  }
  if (isClick) {
    isShowRelationDetail.value = false;
  }
  if (isApplied.value) {
    paramsExtendsRelationSearch.value = filterParamsAdvanced(
      paramsExtendsRelationSearch.value
    );
  }
  paramsExtendsRelationSearch.value = {
    ...paramsExtendsRelationSearch.value,
    size: size || 20,
  };
  await getRelationSearch();
};

const handleSubmitPopup = (): void => {
  if (actionType.value === ITEM_ACTIONS_LIST_TYPE.DUPLICATE) {
    isEdit.value = false;
    isDuplicate.value = true;
    handleDuplicate(popupData.value);
  }
  if (actionType.value === ITEM_ACTIONS_LIST_TYPE.DETAIL) {
    isEdit.value = false;
    isDuplicate.value = false;
    getDetailRelation(popupData.value);
  }
  isShowPopup.value = false;
};

const handleChangeRelationNmCd = (): void => {
  if (selectedNmCdRelationSearch.value === NM_CD_FIELDS[0].value) {
    paramsExtendsRelationSearch.value.objCode = undefined;
    paramsExtendsRelationSearch.value.objName = initInput.value;
  } else {
    paramsExtendsRelationSearch.value.objCode = initInput.value;
    paramsExtendsRelationSearch.value.objName = undefined;
  }
};

const handleChangeInput = (value: string): void => {
  if (selectedNmCdRelationSearch.value === NM_CD_FIELDS[0].value) {
    paramsExtendsRelationSearch.value.objName = value;
  } else {
    paramsExtendsRelationSearch.value.objCode = value;
  }
};

const handleResetSearch = (): void => {
  initInput.value = null;
  isApplied.value = false;
  resetRelationParamSearch();
};

const handleDragStart = (event: DragEvent, item: any): void => {
  dragOfferType.value = SELECT_LIST_TYPE.RELATION;
  isDragging.value = true;
  handleDragUserPocket(event, {
    ...item,
    userPocketType: LARGE_ITEM_CODE.RELATION,
  });
};

const handleDragEnd = (): void => {
  isDragging.value = false;
};

const handleCloseRelationPane = (): void => {
  sideDisplay.value.relationSearch = false;
};

const handleEnterSearch = () => {
  searchPaneRef.value?.handleSearch();
};
</script>
