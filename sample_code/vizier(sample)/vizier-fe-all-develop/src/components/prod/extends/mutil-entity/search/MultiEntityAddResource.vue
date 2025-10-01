<template>
  <SearchPane
    ref="searchPaneResource"
    title="product_platform.resource_search"
    container-class="rounded-lg col-span-1"
    :pane-type="SearchPaneType.Resource"
    :pane-col="ColNumber.One"
    :item-height="62"
    :option-types="optionsType"
    :selected-item="selectedResource"
    :model-param="paramsSearchMultiResource"
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
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import SearchPane from "@/components/prod/shared/SearchPane.vue";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { useGroupCode } from "@/composables/useGroupCode";
import useRedirect from "@/composables/useRedirect";
import { NM_CD_FIELDS } from "@/constants/";
import { DETAIL_CATEGORY } from "@/constants/extendsManager";
import { MULTI_ENTITY_DROP_TYPE } from "@/constants/multiEntity";
import { ColNumber, LargeItemCode, SearchPaneType } from "@/enums";
import type { ActionType } from "@/interfaces/prod";
import {
  useDragStore,
  useMultiEntityCreateStore,
  useMultiEntitySearchStore,
  useSnackbarStore,
} from "@/store";
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
  paramsSearchMultiResource,
  multiResourceList,
  multiResourceSearchNmCd,
  dragType,
} = storeToRefs(
  props.category === DETAIL_CATEGORY.SEARCH
    ? multiEntitySearchStore
    : multiEntityCreateStore
);
const { getListResourceSearch, resetParamListResourceSearch } =
  props.category === DETAIL_CATEGORY.SEARCH
    ? multiEntitySearchStore
    : multiEntityCreateStore;
const useSnackbar = useSnackbarStore();
const { isDragging } = storeToRefs(useDragStore());
const { search } = useGroupCode();
const { moveResourceSearchPage } = useRedirect();
const { handleDragUserPocket } = useDragUserPocket();

const inputValue = ref();
const selectedResource = ref();
const uniqueCodeList = ref();
const optionsType = ref<any[]>();

const listItems = computed(() => multiResourceList.value.items);

const pagination = computed(() => multiResourceList.value.pagination);

const listActions = (item: any): ActionType[] => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      moveResourceSearchPage({
        itemCode: item.itemCode,
        objCode: item.objCode,
      });
    },
  },
];

const onChooseCard = async (value) => {
  selectedResource.value = value;
};

const handleSearch = async (size, isClick = false, page = 1) => {
  paramsSearchMultiResource.value.page = isClick ? 1 : page;
  paramsSearchMultiResource.value.size = size;
  pagination.value.currentPage = 1;
  await getListResourceSearch();
};

const handleResetSearch = () => {
  resetParamListResourceSearch();
  inputValue.value = null;
};

const handleChangePage = async (page) => {
  paramsSearchMultiResource.value.page = page;
  pagination.value.currentPage = page;
  await getListResourceSearch();
};

const onClose = () => {
  entityDisplayForm.value.resourceSearch = false;
};

const handleDragStart = (params: any): void => {
  const { event, item } = params;
  isDragging.value = true;
  dragType.value = MULTI_ENTITY_DROP_TYPE.RESOURCE;
  handleDragUserPocket(event, {
    userPocketType: LargeItemCode.Resource,
    ...item,
  });
};

watch(
  () => uniqueCodeList.value,
  async (val) => {
    await search(val);
  },
  { deep: true }
);

onMounted(async () => {
  if (multiResourceSearchNmCd.value === NM_CD_FIELDS[0].value) {
    inputValue.value = paramsSearchMultiResource.value.name;
  } else {
    inputValue.value = paramsSearchMultiResource.value.code;
  }
  try {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Resource,
    });
    optionsType.value = data;
  } catch (error: any) {
    useSnackbar.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
});
</script>
