<template>
  <SearchPane
    ref="searchPaneComponent"
    title="product_platform.component_search"
    container-class="rounded-lg col-span-1"
    :pane-type="SearchPaneType.Component"
    :pane-col="ColNumber.One"
    :item-height="62"
    :option-types="optionsType"
    :option-sub-types="optionsSubType"
    :selected-item="selectedComponent"
    :model-param="paramsSearchMultiComponent"
    :model-list="listItems"
    :pagination="pagination"
    :type-select-require="false"
    :search-item-actions="listActions"
    show-float-icon-left
    icon-left-class="right-0"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-click-item="onChooseCard"
    @on-change-page="handleChangePage"
    @on-drag-start="handleDragStart"
    @on-click-float-left="onClose"
  />
</template>

<script setup lang="ts">
import { getListItemCodeApi } from "@/api/prod/commonApi";
import {
  getComponentDetailInfoApi,
  getComponentSearchType,
} from "@/api/prod/componentApi";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { useGroupCode } from "@/composables/useGroupCode";
import useRedirect from "@/composables/useRedirect";
import { DETAIL_CATEGORY } from "@/constants/extendsManager";
import { MULTI_ENTITY_DROP_TYPE } from "@/constants/multiEntity";
import { ColNumber, LargeItemCode, SearchBy, SearchPaneType } from "@/enums";
import type { ActionType } from "@/interfaces/prod";
import {
  useDragStore,
  useMultiEntityCreateStore,
  useMultiEntitySearchStore,
  useSnackbarStore,
} from "@/store";
import { GroupedItem, Item } from "@/types/catalog/component/ComponentSearch";
import { useI18n } from "vue-i18n";

const props = defineProps({
  category: {
    type: String,
    default: DETAIL_CATEGORY.SEARCH,
  },
});

const { t } = useI18n();
const multiEntitySearchStore = useMultiEntitySearchStore();
const multiEntityCreateStore = useMultiEntityCreateStore();
const {
  entityDisplayForm,
  paramsSearchMultiComponent,
  multiComponentList,
  dragType,
} = storeToRefs(
  props.category === DETAIL_CATEGORY.SEARCH
    ? multiEntitySearchStore
    : multiEntityCreateStore
);
const { resetParamListComponentSearch, getListComponentSearch } =
  props.category === DETAIL_CATEGORY.SEARCH
    ? multiEntitySearchStore
    : multiEntityCreateStore;
const { isDragging } = storeToRefs(useDragStore());
const { showSnackbar } = useSnackbarStore();
const { search } = useGroupCode();
const { moveComponentSearchPage } = useRedirect();
const { handleDragUserPocket } = useDragUserPocket();

const searchPaneComponent = ref();
const selectedComponent = ref();
const optionsType = ref<any[]>([]);
const optionsSubType = ref<any[]>([]);
const uniqueCodeList = ref();
const listItems = computed(() => multiComponentList.value.items);
const pagination = computed(() => multiComponentList.value.pagination);

const listActions = (item: any): ActionType[] => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      moveComponentSearchPage({
        middleType: item.itemLargeType,
        offerType: item.itemType,
        code: item.code,
        type: item.itemCode,
        itemId: item.uuid,
        name: item.name,
        subType: item.subType,
      });
    },
  },
];

const getListSubType = async (type) => {
  if (type?.trim()) {
    try {
      const { data } = await getListItemCodeApi({
        mItemCode: type,
      });
      optionsSubType.value = data;
    } catch (error: any) {
      showSnackbar(
        error?.errorMsg || t("product_platform.something_went_wrong"),
        "error"
      );
    }
  } else {
    optionsSubType.value = [];
  }
};

const onChooseCard = async (event, value, index) => {
  selectedComponent.value = value;
  if (event?.isShowDetail) {
    const { data } = await getComponentDetailInfoApi({
      objUuid: value.uuid,
    });
    if (data) {
      multiComponentList.value.items[index as number]["detail"] = data;
    }
  }
};

const handleSearch = (size, isClick = false, page = 1) => {
  paramsSearchMultiComponent.value.page = isClick ? 1 : page;
  paramsSearchMultiComponent.value.size = size;
  searchComponents();
};

const searchComponents = async () => {
  const { type, subType, searchBy, searchKey, onlyValidDtm, page, size } =
    paramsSearchMultiComponent.value;
  const params = {
    componentType: type,
    componentSubType: subType,
    componentName: searchBy === SearchBy.Name ? searchKey : undefined,
    componentCode: searchBy === SearchBy.Code ? searchKey : undefined,
    onlyValidDtm: onlyValidDtm,
    page,
    size,
  };
  await getListComponentSearch(params);
};

const handleResetSearch = () => {
  resetParamListComponentSearch();
};

const handleChangePage = async (page) => {
  paramsSearchMultiComponent.value.page = page;
  pagination.value.currentPage = page;
  await searchComponents();
};

const onClose = () => {
  entityDisplayForm.value.componentSearch = false;
};

const handleDragStart = (params): void => {
  const { event, item } = params;
  isDragging.value = true;
  dragType.value = MULTI_ENTITY_DROP_TYPE.COMPONENT;
  handleDragUserPocket(event, {
    userPocketType: LargeItemCode.Component,
    ...item,
  });
};

const groupByMiddleItemCode = (data: Item[]): GroupedItem[] => {
  const grouped = data.reduce((acc, item) => {
    let group: any = acc.find((group) => group.value === item.middleItemCode);
    if (!group) {
      group = {
        name: item.middleItemName,
        value: item.middleItemCode,
        cmcdDetlId: item.middleItemCode,
        cmcdDetlNm: item.middleItemName,
        sortNo: item.middleSortNo,
        children: [],
      };
      acc.push(group);
    }
    group.children.push({
      name: item.itemName,
      value: item.itemCode,
      cmcdDetlId: item.itemCode,
      cmcdDetlNm: item.itemName,
      sortNo: item.sortNo,
    });
    return acc;
  }, [] as GroupedItem[]);
  grouped.sort((after, before) => after.sortNo - before.sortNo);
  grouped.forEach((group) => {
    group.children.sort((after, before) => after.sortNo - before.sortNo);
  });
  return grouped;
};

watch(
  () => paramsSearchMultiComponent.value.type,
  (value) => {
    paramsSearchMultiComponent.value.subType = " ";
    getListSubType(value);
  }
);

watch(
  () => uniqueCodeList.value,
  async (val) => {
    await search(val);
  },
  { deep: true }
);

onMounted(async () => {
  const res = await getComponentSearchType();
  optionsType.value = groupByMiddleItemCode(res.data);
  getListSubType(paramsSearchMultiComponent.value.type);
});
</script>
