<template>
  <SearchPane
    ref="searchPaneRelation"
    title="product_platform.relation.relationSearchTitle"
    container-class="rounded-lg col-span-1"
    :pane-col="ColNumber.One"
    :item-height="46"
    :selected-item="selectedItem"
    :model-param="selectedNmCdRelationSearch"
    :model-list="extendRelationSearch.items"
    :pagination="extendRelationSearch.pagination"
    :show-float-icon-left="showFloatIconLeft"
    :show-float-icon-right="showFloatIconRight"
    :icon-left-class="iconLeftClass"
    :icon-right-class="iconRightClass"
    @on-search="handleRelationSearch"
    @on-reset="handleRelationResetSearch"
    @on-change-page="handleChangeRelationPage"
    @on-click-float-left="emits('onClosePane')"
    @on-click-float-right="emits('onClosePane')"
  >
    <template #custom-search-item="{ item }: any">
      <ExtendsAccordion
        v-model:expand="item.isExpand"
        type="normal"
        width="100%"
        class="!py-0"
        :active="item.objUuid === selectedRelation?.objUuid"
        :title="item.objName"
        :search-text="selectedNmCdRelationSearch.searchKey"
        :search-field="
          selectedNmCdRelationSearch.searchBy === 'objName' ? 'name' : 'code'
        "
        :draggable="draggable || isEdit"
        :is-show-list-product="false"
        :disable="isExpired(item?.validEndDtm)"
        :editable="false"
        @dragstart="handleDragStartRelation($event, item)"
        @dragend="handleDragEndRelation"
        @on-click="handleClickRelation($event, item)"
        @on-expand="handleExpandDetail($event, item)"
      >
        <template v-if="item?.detail">
          <ProductGrid :data="item.detail" :type="LARGE_ITEM_CODE.RELATION" />
        </template>
      </ExtendsAccordion>
    </template>
  </SearchPane>
</template>

<script setup lang="ts">
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { getExtendsDependencyRelationDefinition } from "@/api/prod/extendsApi";
import ExtendsAccordion from "@/components/prod/extends/relation/manager/relation-viewer/common/ExtendsAccordion.vue";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { SELECT_LIST_TYPE } from "@/constants/extendsManager";
import { NM_CD_FIELDS, SPACE } from "@/constants/index";
import { ColNumber, LargeItemCode } from "@/enums";
import {
  useDragStore,
  useExtendManagerStore,
  useRelationManagerDuplicateStore,
  useSnackbarStore,
} from "@/store";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import { isExpired } from "@/utils/format-data";

const props = defineProps({
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
  draggable: {
    type: Boolean,
    default: true,
  },
  duplicateMode: {
    type: Boolean,
    default: false,
  },
});
const emits = defineEmits(["onClosePane"]);

const extendManagerStore = useExtendManagerStore();
const relationManagerDuplicateStore = useRelationManagerDuplicateStore();

const selectedStore = computed(() =>
  props.duplicateMode ? relationManagerDuplicateStore : extendManagerStore
);

const {
  offerItemCodeList,
  groupItemCodeList,
  allowOfferDrop,
  allowGroupDrop,
  selectedItem,
  extendRelationSearch,
  selectedNmCdRelationSearch,
  paramsExtendsRelationSearch,
  selectedRelation,
  isEdit,
} = storeToRefs(selectedStore.value);

const { getRelationSearch, resetRelationParamSearch } = selectedStore.value;
const { dragOfferType, isDragging } = storeToRefs(useDragStore());
const { handleDragUserPocket } = useDragUserPocket();
const useSnackbar = useSnackbarStore();
const searchPaneRelation = ref();

const gridViewParams = reactive({
  category: SPACE,
  value: "",
  type: "name",
});

provide("gridViewParams", gridViewParams);

onMounted(async () => {
  getOfferTypeOption();
  getGroupTypeOption();
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

const getListRelation = async () => {
  selectedRelation.value = null;
  paramsExtendsRelationSearch.value = {
    ...paramsExtendsRelationSearch.value,
    onlyValidDtm: props.onlyValidDtm,
  };
  if (selectedNmCdRelationSearch.value.searchBy === NM_CD_FIELDS[0].value) {
    paramsExtendsRelationSearch.value.objName =
      selectedNmCdRelationSearch.value.searchKey || "";
    paramsExtendsRelationSearch.value.objCode = "";
  } else {
    paramsExtendsRelationSearch.value.objCode =
      selectedNmCdRelationSearch.value.searchKey || "";
    paramsExtendsRelationSearch.value.objName = "";
  }
  await getRelationSearch();
};

const handleRelationSearch = async (size, isClick: false, page = 1) => {
  try {
    paramsExtendsRelationSearch.value.page = isClick ? 1 : page;
    paramsExtendsRelationSearch.value.size = size;
    await getListRelation();
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};

const handleRelationResetSearch = () => {
  resetRelationParamSearch();
};

const handleChangeRelationPage = (page: number) => {
  paramsExtendsRelationSearch.value = {
    ...paramsExtendsRelationSearch.value,
    page,
  };
  getListRelation();
};

const handleDragStartRelation = (event: DragEvent, item: any) => {
  dragOfferType.value = SELECT_LIST_TYPE.RELATION;
  isDragging.value = true;
  handleDragUserPocket(event, {
    ...item,
    userPocketType: LARGE_ITEM_CODE.RELATION,
  });
};

const handleDragEndRelation = () => {
  isDragging.value = false;
};

const handleExpandDetail = async (event, rel) => {
  if (event && !rel?.detail) {
    const { data } = await getExtendsDependencyRelationDefinition(rel.objUuid);
    rel["detail"] = data;
  }
  rel["isExpand"] = event;
};

const handleClickRelation = async (event, rel) => {
  selectedRelation.value = rel;
};
</script>
