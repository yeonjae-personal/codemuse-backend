<template>
  <SearchPane
    ref="searchPaneGroup"
    title="product_platform.groupSearch"
    container-class="rounded-lg col-span-1"
    :pane-type="SearchPaneType.Group"
    :pane-col="ColNumber.One"
    :item-height="62"
    :option-types="groupItemCodeList"
    :selected-item="selectedTargetSearchItem"
    :model-param="paramsExtendsTargetSearchGroup"
    :model-list="listItems"
    :pagination="extendGroupTargetSearch.pagination"
    :type-select-require="typeSelectRequire"
    :show-float-icon-right="showFloatIconRight"
    :icon-right-class="iconRightClass"
    :search-item-actions="listActions"
    show-float-icon-left
    icon-left-class="right-0"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-click-item="handleClickGroup"
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
import { ColNumber, LargeItemCode, SearchPaneType } from "@/enums";
import { ActionType } from "@/interfaces/prod";
import {
  useDragStore,
  useExtendManagerStore,
  useRelationManagerDuplicateStore,
  useSnackbarStore,
} from "@/store";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
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
  duplicateMode: {
    type: Boolean,
    default: false,
  },
  searchItemActions: {
    type: Function,
    default: () => [],
  },
  showAction: {
    type: Boolean,
    default: false,
  },
  dragType: {
    type: String,
    default: "",
  },
});
const emits = defineEmits(["onClosePane", "onClose"]);

const extendManagerStore = useExtendManagerStore();
const relationManagerDuplicateStore = useRelationManagerDuplicateStore();
const { dragOfferType, isDragging, categoryDrag } = storeToRefs(useDragStore());
const { handleDragUserPocket } = useDragUserPocket();
const { moveGroupSearchPage } = useRedirect();

const { t } = useI18n();

const selectedStore = computed(() =>
  props.duplicateMode ? relationManagerDuplicateStore : extendManagerStore
);

const {
  paramsExtendsTargetSearchGroup,
  extendGroupTargetSearch,
  groupItemCodeList,
  selectedItem,
  selectedTargetSearchItem,
  allowGroupDrop,
} = storeToRefs(selectedStore.value);

const { getGroupListTargetSearch, resetParamsExtendsTargetSearchGroup } =
  selectedStore.value;

const useSnackbar = useSnackbarStore();
const itemSelected = ref();
const initInpup = ref();

const listItems = computed(() => {
  return extendGroupTargetSearch.value.items.map((item: any) => ({
    ...item,
    showAppendIcon: props.showAction,
  }));
});

const handleSearch = async (size, isClick = false, page = 1) => {
  try {
    paramsExtendsTargetSearchGroup.value = {
      ...paramsExtendsTargetSearchGroup.value,
      size: size,
      page: isClick ? 1 : page,
    };
    await getListGroup();
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};

const handleResetSearch = () => {
  itemSelected.value = null;
  initInpup.value = null;
  resetParamsExtendsTargetSearchGroup();
};

const getListGroup = async () => {
  try {
    await getGroupListTargetSearch(
      props.duplicateMode ? selectedItem.value.objUuid : "",
      props.onlyValidDtm
    );
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};

const handleClickGroup = (value) => {
  selectedTargetSearchItem.value = value;
};

const handleChangePage = (page) => {
  paramsExtendsTargetSearchGroup.value = {
    ...paramsExtendsTargetSearchGroup.value,
    page,
  };
  getListGroup();
};

const handleDragStart = (params): void => {
  const { event, item } = params;
  const userPocketType = LARGE_ITEM_CODE.GROUP;
  dragOfferType.value = props.dragType || item.itemCode;
  isDragging.value = true;
  handleDragUserPocket(event, {
    userPocketType,
    ...item,
  });
  categoryDrag.value = TYPE_CATEGORY_OB_DRAG.GROUP;
};

const handleDragEnd = () => {
  isDragging.value = false;
  categoryDrag.value = "";
};

const handleClose = () => {
  emits("onClosePane");
  emits("onClose");
};

const getGroupTypeOption = async () => {
  if (groupItemCodeList.value?.length) {
    return;
  }
  const groupRes = await getListItemCodeApi({
    lItemCode: LargeItemCode.Group,
  });
  groupItemCodeList.value = groupRes.data;
  allowGroupDrop.value = groupRes.data.map((item) => item.itemCode);
};

const listActions = (item: any): ActionType[] => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      moveGroupSearchPage({
        itemId: item.objUuid,
        type: item.itemCode,
        code: item.objCode,
        name: item.objName,
      });
    },
  },
];

onMounted(() => {
  getGroupTypeOption();
});
</script>

<style scoped>
.filter {
  display: grid;
  grid-template-columns: 1fr 2fr;
}
</style>
