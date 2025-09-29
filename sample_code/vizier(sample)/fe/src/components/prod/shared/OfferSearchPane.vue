<template>
  <SearchPane
    ref="searchPaneOffer"
    title="product_platform.offer_search"
    container-class="rounded-lg col-span-1"
    :pane-type="SearchPaneType.Offer"
    :pane-col="ColNumber.One"
    :item-height="62"
    :option-types="offerItemCodeList"
    :selected-item="
      props.category === SEARCH_CATEGORY.OFFER
        ? selectedItem
        : selectedTargetSearchItem
    "
    :model-param="
      props.category === SEARCH_CATEGORY.OFFER
        ? paramsExtendsFilterOfferSearch
        : paramsExtendsTargetSearchOffer
    "
    :model-list="listItems"
    :pagination="pagination"
    :type-select-require="typeSelectRequire"
    show-float-icon-left
    icon-left-class="right-0"
    :show-float-icon-right="showFloatIconRight"
    :icon-right-class="iconRightClass"
    :search-item-actions="listActions"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-click-item="handleClickOffer"
    @on-change-page="handleChangePage"
    @on-drag-start="handleDragStart"
    @on-drag-end="handleDragEnd"
    @on-click-float-left="handleClose"
    @on-click-float-right="handleClose"
  />
</template>

<script setup lang="ts">
import { getListItemCodeApi } from "@/api/prod/commonApi";
import useDragUserPocket from "@/composables/useDragUserPocket";
import {
  EXTENDS_VIEW,
  GRID_PARAMS_DEFAULT,
  SEARCH_CATEGORY,
  TARGET_TYPES,
} from "@/constants/extendsManager";
import { LARGE_ITEM_CODE } from "@/constants/offer";
import { ColNumber, LargeItemCode, SearchPaneType } from "@/enums";
import { ActionType } from "@/interfaces/prod";
import {
  useDragStore,
  useExtendManagerStore,
  useRelationManagerDuplicateStore,
  useSnackbarStore,
} from "@/store";
import { cloneDeep } from "lodash-es";
import { useI18n } from "vue-i18n";
import OpenInNewIcon from "../icons/OpenInNewIcon.vue";
import useRedirect from "@/composables/useRedirect";
import { TYPE_CATEGORY_OB_DRAG } from "@/constants/index";

const props = defineProps({
  typeSelectRequire: {
    type: Boolean,
    default: false,
  },
  onlyValidDtm: {
    type: Boolean,
    default: false,
  },
  showFloatIconLeft: {
    type: Boolean,
    default: false,
  },
  showFloatIconRight: {
    type: Boolean,
    default: false,
  },
  iconLeftClass: {
    type: String,
    default: "left-[-24px]",
  },
  iconRightClass: {
    type: String,
    default: "left-0",
  },
  showAction: {
    type: Boolean,
    default: false,
  },
  dragType: {
    type: String,
    default: "",
  },
  duplicateMode: {
    type: Boolean,
    default: false,
  },
  category: {
    type: String,
    default: SEARCH_CATEGORY.OFFER,
  },
});
const emits = defineEmits(["onClosePane", "onClose"]);

const searchPaneOffer = ref();
const extendManagerStore = useExtendManagerStore();
const relationManagerDuplicateStore = useRelationManagerDuplicateStore();
const useSnackbar = useSnackbarStore();
const selectedStore = computed(() =>
  props.duplicateMode ? relationManagerDuplicateStore : extendManagerStore
);

const {
  targetSearch,
  offerItemCodeList,
  paramsExtendsFilterOfferSearch,
  paramsExtendsTargetSearchOffer,
  extendOfferSearch,
  extendOfferTargetSearch,
  extendGroupTargetSearch,
  selectedItem,
  extendsView,
  listView,
  paramListView,
  allowOfferDrop,
  selectedTargetSearchItem,
} = storeToRefs(selectedStore.value);
const {
  getExtendsListOfferSearch,
  resetExtendParamFilterOfferSearch,
  resetParamsExtendsTargetSearchOffer,
  resetListTable,
  resetStructureActiveMap,
  resetDetailViewData,
  getGroupBySelectedItem,
  getRelationDataTable,
} = selectedStore.value;

const { dragOfferType, isDragging, categoryDrag } = storeToRefs(useDragStore());
const { handleDragUserPocket } = useDragUserPocket();
const { moveOfferSearchPage } = useRedirect();
const { t } = useI18n();

const listItems = computed<any>(() => {
  if (props.category === SEARCH_CATEGORY.OFFER) {
    return extendOfferSearch.value.items.map((item: any) => ({
      ...item,
      showAppendIcon: props.showAction,
    }));
  } else {
    if (targetSearch.value === TARGET_TYPES.OFFER) {
      return extendOfferTargetSearch.value.items.map((item: any) => ({
        ...item,
        showAppendIcon: props.showAction,
      }));
    } else if (targetSearch.value === TARGET_TYPES.GROUP) {
      return extendGroupTargetSearch.value.items.map((item: any) => ({
        ...item,
        showAppendIcon: props.showAction,
      }));
    }
  }
});

const pagination = computed<any>(() => {
  if (props.category === SEARCH_CATEGORY.OFFER) {
    return extendOfferSearch.value.pagination;
  } else {
    if (targetSearch.value === TARGET_TYPES.OFFER) {
      return extendOfferTargetSearch.value.pagination;
    } else if (targetSearch.value === TARGET_TYPES.GROUP) {
      return extendGroupTargetSearch.value.pagination;
    }
  }
});

onBeforeMount(async () => {
  getOfferTypeOption();
});

const getOfferTypeOption = async () => {
  if (offerItemCodeList.value?.length) {
    return;
  }
  const offerRes = await getListItemCodeApi({
    lItemCode: LargeItemCode.Offer,
  });
  offerItemCodeList.value = offerRes.data;
  allowOfferDrop.value = offerRes.data.map((item) => item.itemCode);
};

const handleSearch = async (pageSize: number, isClick = false, page = 1) => {
  searchPaneOffer.value.validate();
  const isSubTypeNotAvailable = !paramsExtendsFilterOfferSearch.value.type;
  if (isSubTypeNotAvailable && props.category === SEARCH_CATEGORY.OFFER) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }
  try {
    paramsExtendsFilterOfferSearch.value = {
      ...paramsExtendsFilterOfferSearch.value,
      size: pageSize,
      page: isClick ? 1 : page,
    };
    paramsExtendsTargetSearchOffer.value = {
      ...paramsExtendsTargetSearchOffer.value,
      size: pageSize,
      page: isClick ? 1 : page,
    };
    await getListOffer();
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};

const handleResetSearch = () => {
  searchPaneOffer.value.resetValidate();
  selectedItem.value = null;
  props.category === SEARCH_CATEGORY.OFFER
    ? resetExtendParamFilterOfferSearch()
    : resetParamsExtendsTargetSearchOffer();
  resetListTable();
};

const handleClickOffer = (value) => {
  if (props.category === SEARCH_CATEGORY.OFFER) {
    resetStructureActiveMap();
    resetDetailViewData();
    selectedItem.value = value;
    getGroupBySelectedItem(extendsView.value === EXTENDS_VIEW.SIMPLE, false);
    listView.value.items = [];
    paramListView.value = cloneDeep(GRID_PARAMS_DEFAULT);
    paramListView.value.uuid = value.prodUuid;
    getRelationDataTable();
  } else {
    selectedTargetSearchItem.value = value;
  }
};

const getListOffer = async () => {
  try {
    await getExtendsListOfferSearch(props.category, props.onlyValidDtm);
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};

const handleChangePage = async (page) => {
  paramsExtendsFilterOfferSearch.value.page = page;
  paramsExtendsTargetSearchOffer.value.page = page;
  await getListOffer();
};

const handleDragStart = (params): void => {
  const { event, item } = params;
  const userPocketType = LARGE_ITEM_CODE.OFFER;
  dragOfferType.value = props.dragType || item.itemCode;
  isDragging.value = true;
  handleDragUserPocket(event, {
    userPocketType,
    ...item,
  });
  categoryDrag.value = TYPE_CATEGORY_OB_DRAG.OFFER;
};

const handleDragEnd = () => {
  isDragging.value = false;
  categoryDrag.value = "";
};

const handleClose = () => {
  emits("onClosePane");
  emits("onClose");
};

const listActions = (item: any): ActionType[] => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      moveOfferSearchPage({
        itemCode: item.objCode,
        objCode: item.objCode,
        objUuid: item.objUuid,
        offerType: item.itemCode,
      });
    },
  },
];
</script>

<style scoped></style>
