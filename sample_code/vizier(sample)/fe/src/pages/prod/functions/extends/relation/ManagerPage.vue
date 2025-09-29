<template>
  <FourColumns>
    <OfferSearchPane
      v-if="sideDisplay.offerSearch"
      type-select-require
      show-float-icon-left
      icon-left-class="right-0"
      :category="SEARCH_CATEGORY.OFFER"
      @on-close-pane="handleCloseOfferPane"
    />
    <RelationSearchPane
      v-if="sideDisplay.relationSearch"
      show-float-icon-left
      only-valid-dtm
      icon-left-class="right-0"
      @on-close-pane="handleCloseRelationPane"
    />
    <RelationViewer :class="setRelationViewerClass" />
    <OfferSearchPane
      v-if="sideDisplay.targetSearch"
      show-float-icon-left
      icon-left-class="right-0"
      :type-select-require="false"
      :category="SEARCH_CATEGORY.TARGET"
      only-valid-dtm
      @on-close-pane="handleCloseTargetSearchPane"
    />
    <RelationDefinition v-if="sideDisplay.relationDetail" class="col-span-1" />
    <ManagerGroupDetail v-if="sideDisplay.targetDetail" class="col-span-1" />
    <ManagerOfferDetail v-if="sideDisplay.offerDetail" class="col-span-1" />
  </FourColumns>
</template>

<script setup lang="ts">
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { SEARCH_CATEGORY } from "@/constants/extendsManager";
import { SPACE } from "@/constants/index";
import { LargeItemCode } from "@/enums";
import { useExtendManagerStore } from "@/store";

const extendManagerStore = useExtendManagerStore();

const {
  sideDisplay,
  offerItemCodeList,
  groupItemCodeList,
  allowOfferDrop,
  allowGroupDrop,
} = storeToRefs(extendManagerStore);

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

const setRelationViewerClass = computed(() => {
  const numberPaneShow = Object.values(sideDisplay.value).filter(
    (value) => value
  ).length;
  switch (numberPaneShow) {
    case 0:
      return "col-span-4";
    case 1:
      return "col-span-3";
    case 2:
      return "col-span-2";
  }
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

const handleCloseOfferPane = () => {
  sideDisplay.value.offerSearch = false;
};

const handleCloseTargetSearchPane = () => {
  sideDisplay.value.targetSearch = false;
};

const handleCloseRelationPane = () => {
  sideDisplay.value.relationSearch = false;
};
</script>
