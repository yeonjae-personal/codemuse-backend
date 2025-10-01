<template>
  <FourColumns>
    <RelationSearchPane
      v-if="sideDisplay.relationSearch"
      only-valid-dtm
      show-float-icon-left
      duplicate-mode
      icon-left-class="right-[0px]"
      @on-close-pane="handleCloseRelationPane"
    />
    <GroupSearchPane
      v-if="sideDisplay.targetSearch && targetSearch === TARGET_TYPES.GROUP"
      only-valid-dtm
      show-float-icon-left
      duplicate-mode
      icon-left-class="right-[0px]"
      @on-close-pane="handleCloseOfferPane"
    />
    <OfferSearchPane
      v-if="sideDisplay.targetSearch && targetSearch === TARGET_TYPES.OFFER"
      only-valid-dtm
      show-float-icon-left
      duplicate-mode
      icon-left-class="right-[0px]"
      :category="SEARCH_CATEGORY.TARGET"
      @on-close-pane="handleCloseOfferPane"
    />
    <RelationViewer
      :class="setRelationViewerClass"
      :offer-duplicate-mode="offerDuplicateInRelationMode"
    />
    <RelationDefinition
      v-if="sideDisplay.relationDetail"
      :page="RELATION_PAGE.DUPLICATE"
      class="col-span-1"
    />
    <ManagerGroupDetail
      v-if="sideDisplay.targetDetail"
      :offer-duplicate-mode="offerDuplicateInRelationMode"
      class="col-span-1"
    />
    <ManagerOfferDetail
      v-if="sideDisplay.offerDetail"
      :offer-duplicate-mode="offerDuplicateInRelationMode"
      class="col-span-1"
    />
  </FourColumns>
</template>

<script setup lang="ts">
import { getListItemCodeApi } from "@/api/prod/commonApi";
import GroupSearchPane from "@/components/prod/shared/GroupSearchPane.vue";
import OfferSearchPane from "@/components/prod/shared/OfferSearchPane.vue";
import {
  EXTENDS_VIEW,
  RELATION_PAGE,
  SEARCH_CATEGORY,
  TARGET_TYPES,
} from "@/constants/extendsManager";
import { LargeItemCode } from "@/enums";
import { MenuItemID } from "@/enums/redirect";
import {
  useOfferDuplicateProcessStore,
  useRelationManagerDuplicateStore,
} from "@/store";

const relationManagerDuplicateStore = useRelationManagerDuplicateStore();
const {
  sideDisplay,
  paramsExtendsFilterOfferSearch,
  isEdit,
  offerItemCodeList,
  groupItemCodeList,
  allowOfferDrop,
  allowGroupDrop,
  extendsView,
  targetSearch,
  isDuplicateInitData,
} = storeToRefs(relationManagerDuplicateStore);
const { offerDuplicateInRelationMode, offerBeClonedUuid, offerDuplicated } =
  storeToRefs(useOfferDuplicateProcessStore());

const handleCloseOfferPane = () => {
  sideDisplay.value.targetSearch = false;
};

const handleCloseRelationPane = () => {
  sideDisplay.value.relationSearch = false;
};

onMounted(async () => {
  getOfferTypeOption();
  getGroupTypeOption();
  if (
    offerDuplicateInRelationMode.value &&
    offerDuplicated.value &&
    !isDuplicateInitData.value
  ) {
    replaceTabName(MenuItemID.RelationDuplicate, "Catalog Duplicate Relation");
    sideDisplay.value.offerSearch = false;
    extendsView.value = EXTENDS_VIEW.DETAIL;
    isEdit.value = true;
    paramsExtendsFilterOfferSearch.value.prodItemCd =
      offerDuplicated.value.objCode;
    await relationManagerDuplicateStore.getOfferBeingDuplicated(
      offerBeClonedUuid.value
    );
  }
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
const replaceTabName = inject<any>("replaceTabName");
</script>
