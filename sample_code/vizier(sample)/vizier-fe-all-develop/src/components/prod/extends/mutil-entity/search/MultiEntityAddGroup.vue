<template>
  <SearchPane
    ref="searchPaneGroup"
    title="product_platform.groupSearch"
    container-class="rounded-lg col-span-1"
    :pane-type="SearchPaneType.Group"
    :pane-col="ColNumber.One"
    :item-height="62"
    :option-types="groupItemCodeList"
    :selected-item="selectedEntityGroup"
    :model-param="paramsSearchMultiGroup"
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
  paramsSearchMultiGroup,
  multiGroupList,
  multiGroupSearchNmCd,
  selectedEntityGroup,
  groupItemCodeList,
  dragType,
} = storeToRefs(
  props.category === DETAIL_CATEGORY.SEARCH
    ? multiEntitySearchStore
    : multiEntityCreateStore
);
const { getGroupList, resetParamListGroupSearch } =
  props.category === DETAIL_CATEGORY.SEARCH
    ? multiEntitySearchStore
    : multiEntityCreateStore;
const { isDragging } = storeToRefs(useDragStore());
const { search } = useGroupCode();
const { moveGroupSearchPage } = useRedirect();
const { handleDragUserPocket } = useDragUserPocket();

const selectedSubtype = ref();
const inputValue = ref();
const uniqueCodeList = ref();

const listItems = computed(() => multiGroupList.value.items);

const pagination = computed(() => multiGroupList.value.pagination);

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

const handleSearch = async (size, isClick = false, page = 1) => {
  paramsSearchMultiGroup.value.page = isClick ? 1 : page;
  paramsSearchMultiGroup.value.size = size;
  await getGroupList();
};

const onChooseCard = async (item) => {
  selectedEntityGroup.value = item;
};

const handleResetSearch = () => {
  resetParamListGroupSearch();
  selectedEntityGroup.value = null;
  inputValue.value = null;
  selectedSubtype.value = null;
};

const handleChangePage = async (page) => {
  paramsSearchMultiGroup.value.page = page;
  await getGroupList();
};

const handleDragStart = (params: any): void => {
  const { event, item } = params;
  isDragging.value = true;
  dragType.value = MULTI_ENTITY_DROP_TYPE.GROUP;
  handleDragUserPocket(event, { userPocketType: LargeItemCode.Group, ...item });
};

const onClose = () => {
  entityDisplayForm.value.groupSearch = false;
};

const getGroupTypeOption = async () => {
  if (groupItemCodeList.value?.length) {
    return;
  }
  const groupRes = await getListItemCodeApi({
    lItemCode: LargeItemCode.Group,
  });
  groupItemCodeList.value = groupRes.data;
};

watch(
  () => uniqueCodeList.value,
  async (val) => {
    await search(val);
  },
  { deep: true }
);

onMounted(() => {
  search(["G00043"]);
  getGroupTypeOption();
  if (multiGroupSearchNmCd.value === NM_CD_FIELDS[0].value) {
    inputValue.value = paramsSearchMultiGroup.value.offrGrpNm;
  } else {
    inputValue.value = paramsSearchMultiGroup.value.offrGrpCd;
  }
});
</script>
