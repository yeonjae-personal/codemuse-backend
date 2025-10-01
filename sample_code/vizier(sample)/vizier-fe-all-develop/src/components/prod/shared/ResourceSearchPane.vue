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
    :model-param="paramsSearchResource"
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
    @on-drag-end="handleDragEnd"
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
import { TYPE_CATEGORY_OB_DRAG } from "@/constants/index";
import { MULTI_ENTITY_DROP_TYPE } from "@/constants/multiEntity";
import { ColNumber, LargeItemCode, SearchPaneType } from "@/enums";
import type { ActionType } from "@/interfaces/prod";
import { useDragStore, useResourceOBStore, useSnackbarStore } from "@/store";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const emits = defineEmits(["onClosePane", "onClose"]);
const resourceObStore = useResourceOBStore();
const { paramsSearchResource, resourceList, dragType } =
  storeToRefs(resourceObStore);
const { getListResourceSearch, resetParamListResourceSearch } = resourceObStore;
const useSnackbar = useSnackbarStore();
const { isDragging, categoryDrag } = storeToRefs(useDragStore());
const { search } = useGroupCode();
const { moveResourceSearchPage } = useRedirect();
const { handleDragUserPocket } = useDragUserPocket();

const inputValue = ref();
const selectedResource = ref();
const uniqueCodeList = ref();
const optionsType = ref<any[]>();

const listItems = computed(() => resourceList.value.items);

const pagination = computed(() => resourceList.value.pagination);

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
  paramsSearchResource.value.page = isClick ? 1 : page;
  paramsSearchResource.value.size = size;
  pagination.value.currentPage = 1;
  await getListResourceSearch();
};

const handleResetSearch = () => {
  resetParamListResourceSearch();
  inputValue.value = null;
};

const handleChangePage = async (page) => {
  paramsSearchResource.value.page = page;
  pagination.value.currentPage = page;
  await getListResourceSearch();
};

const onClose = () => {
  emits("onClose");
  //   entityDisplayForm.value.resourceSearch = false;
};

const handleDragStart = (params: any): void => {
  const { event, item } = params;
  isDragging.value = true;
  dragType.value = MULTI_ENTITY_DROP_TYPE.RESOURCE;
  handleDragUserPocket(event, {
    userPocketType: LargeItemCode.Resource,
    ...item,
  });
  categoryDrag.value = TYPE_CATEGORY_OB_DRAG.RESOURCE;
};

const handleDragEnd = () => {
  isDragging.value = false;
  categoryDrag.value = "";
};

watch(
  () => uniqueCodeList.value,
  async (val) => {
    await search(val);
  },
  { deep: true }
);

onMounted(async () => {
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
