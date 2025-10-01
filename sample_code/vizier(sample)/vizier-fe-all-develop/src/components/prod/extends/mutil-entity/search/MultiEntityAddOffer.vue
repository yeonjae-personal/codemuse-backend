<template>
  <SearchPane
    ref="searchPaneOffer"
    title="product_platform.offer_search"
    container-class="rounded-lg col-span-1"
    :pane-type="SearchPaneType.Offer"
    :pane-col="ColNumber.One"
    :item-height="62"
    :option-types="offersType"
    :selected-item="selectedOffer"
    :model-param="paramsSearchMultiOffer"
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
  paramsSearchMultiOffer,
  multiOfferList,
  multiOfferSearchNmCd,
  dragType,
} = storeToRefs(
  props.category === DETAIL_CATEGORY.SEARCH
    ? multiEntitySearchStore
    : multiEntityCreateStore
);
const { getOfferList, resetParamListOfferSearch } =
  props.category === DETAIL_CATEGORY.SEARCH
    ? multiEntitySearchStore
    : multiEntityCreateStore;
const useSnackbar = useSnackbarStore();
const { isDragging } = storeToRefs(useDragStore());
const { search } = useGroupCode();
const { moveOfferSearchPage } = useRedirect();
const { handleDragUserPocket } = useDragUserPocket();

const inputValue = ref();
const selectedOffer = ref();
const uniqueCodeList = ref();
const offersType = ref([]);

const listItems = computed(() => multiOfferList.value.items);
const pagination = computed(() => multiOfferList.value.pagination);

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

const onChooseCard = async (value) => {
  selectedOffer.value = value;
};

const handleSearch = async (size, isClick = false, page = 1) => {
  paramsSearchMultiOffer.value.page = isClick ? 1 : page;
  paramsSearchMultiOffer.value.size = size;
  await getOfferList();
};

const handleResetSearch = () => {
  resetParamListOfferSearch();
  inputValue.value = null;
};

const handleChangePage = async (page) => {
  paramsSearchMultiOffer.value.page = page;
  await getOfferList();
};

const onClose = () => {
  entityDisplayForm.value.offerSearch = false;
};

const handleDragStart = (params: any): void => {
  const { event, item } = params;
  isDragging.value = true;
  dragType.value = MULTI_ENTITY_DROP_TYPE.OFFER;
  handleDragUserPocket(event, { userPocketType: LargeItemCode.Offer, ...item });
};

watch(
  () => uniqueCodeList.value,
  async (val) => {
    await search(val);
  },
  { deep: true }
);

onMounted(async () => {
  if (multiOfferSearchNmCd.value === NM_CD_FIELDS[0].value) {
    inputValue.value = paramsSearchMultiOffer.value.name;
  } else {
    inputValue.value = paramsSearchMultiOffer.value.code;
  }
  try {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Offer,
    });
    offersType.value = data;
  } catch (error: any) {
    useSnackbar.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
});
</script>
