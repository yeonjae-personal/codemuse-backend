<template>
  <FourColumns>
    <PublishPackageSearch ref="publishPackageSearch" />
    <PublishPackageDetails
      v-if="publishSelected?.pubRqstTaskCode"
      :is-create-publish="isCreatePublish"
      @on-cancel-create="handleCancelCreate"
      @on-upset-success="handleUpsetSucces"
    />
    <ApprovalFlowSearch v-if="isEditStep3 || isCreateStep3" />
    <PublishItemSearch v-if="isEditStep2 || isCreateStep2" />
    <UnpublishedItemPopup v-model:open-popup="isOpenValidatePopup" />
  </FourColumns>
</template>
<script setup lang="ts">
import { getComponentSearchType } from "@/api/prod/componentApi";
import { getMultiEntitySearchInfo } from "@/api/prod/extendsApi";
import PublishPackageSearch from "@/components/prod/publish/PublishPackageSearch.vue";
import useRedirect from "@/composables/useRedirect";
import { PUBLISH_COMPOSE_ITEM_TYPE } from "@/constants/publish";
import { ComposeItem } from "@/interfaces/prod/publishInterface";
import { usePublishManagerStore } from "@/store";

type PublishPackageSearchInstance = InstanceType<typeof PublishPackageSearch>;
const publishManagerStore = usePublishManagerStore();
const {
  publishSelected,
  isCreatePublish,
  isCreateStep1,
  isCreateStep2,
  isEditStep2,
  isEditStep3,
  isCreateStep3,
  isOpenValidatePopup,
  paramFilterPublishSearch,
} = storeToRefs(publishManagerStore);

const {
  moveOfferSearchPage,
  moveComponentSearchPage,
  moveResourceSearchPage,
  moveGroupSearchPage,
  moveMultiEntitySearchPage,
  moveRelationManagerPage,
  moveRelationSearchPage,
  moveCategoryPage,
  moveMatrixBuilderPage,
  moveFactorSearchPage,
  moveTableStrucTurePage,
} = useRedirect();
const publishPackageSearch = ref<PublishPackageSearchInstance>();

const handleCancelCreate = async () => {
  paramFilterPublishSearch.value.size = paramFilterPublishSearch.value.size + 1;
  await publishManagerStore.getPublishSearch();
  if (isCreatePublish.value) {
    isCreatePublish.value = false;
    isCreateStep1.value = false;
    isCreateStep2.value = false;
  }
};

const handleUpsetSucces = async () => {
  await publishManagerStore.getPublishSearch();
};

const handleRedirect = async (item: ComposeItem) => {
  switch (item.chngDataTypeCode) {
    case PUBLISH_COMPOSE_ITEM_TYPE.OFFER:
    case PUBLISH_COMPOSE_ITEM_TYPE.OFFER_STRT:
      moveOfferSearchPage({
        itemCode: item.chngDataItemCode ?? "",
        objCode: item.chngDataCode,
        objUuid: item.chngDataObjUuid ?? "",
      });
      break;
    case PUBLISH_COMPOSE_ITEM_TYPE.OFFER_REL:
      moveRelationManagerPage({
        itemType: "O",
        offerType: item.chngDataItemCode,
        code: item.chngDataCode,
      });
      break;
    case PUBLISH_COMPOSE_ITEM_TYPE.COMPONENT:
    case PUBLISH_COMPOSE_ITEM_TYPE.COMPONENT_STRT:
      const { data } = await getComponentSearchType();
      const componentTypeDto = data.find(
        (type) => type.itemCode === item.chngDataItemCode
      );
      moveComponentSearchPage({
        code: item.chngDataCode,
        offerType: componentTypeDto?.itemCode,
        middleType: componentTypeDto?.middleItemCode,
        itemId: item.chngDataObjUuid ?? "",
        name: item.chngDataCodeName,
        subType: componentTypeDto?.itemCode,
        type: componentTypeDto?.middleItemCode,
      });
      break;
    case PUBLISH_COMPOSE_ITEM_TYPE.RESOURCE:
      moveResourceSearchPage({
        itemCode: item.chngDataItemCode ?? "",
        objCode: item.chngDataCode,
      });
      break;
    case PUBLISH_COMPOSE_ITEM_TYPE.GROUP:
    case PUBLISH_COMPOSE_ITEM_TYPE.GROUP_STRT:
      moveGroupSearchPage({
        itemId: item.chngDataObjUuid,
        type: item.chngDataItemCode,
        code: item.chngDataCode,
        name: item.chngDataCodeName,
      });
      break;
    case PUBLISH_COMPOSE_ITEM_TYPE.MTL_ENTITY:
      const res = await getMultiEntitySearchInfo();
      const entityTypeDto = res.data.filter((ent) => {
        return ent.subOptions.some(
          (type) => type.value === item.chngDataItemCode
        );
      });
      moveMultiEntitySearchPage({
        itemCode: entityTypeDto[0].value,
        entityTypeCode: item.chngDataItemCode ?? "",
        multiEntityCode: item.chngDataCode,
      });
      break;
    case PUBLISH_COMPOSE_ITEM_TYPE.RELATION:
      moveRelationSearchPage({ code: item.chngDataCode });
      break;
    case PUBLISH_COMPOSE_ITEM_TYPE.CATEGORY:
      moveCategoryPage({
        itemCode: item.chngDataItemCode ?? "",
        objUuid: item.chngDataObjUuid ?? "",
      });
      break;
    case PUBLISH_COMPOSE_ITEM_TYPE.MATRIX:
      moveMatrixBuilderPage({
        matrixCode: item.chngDataCode,
        matrixCodeName: item.chngDataCodeName,
      });
      break;
    case PUBLISH_COMPOSE_ITEM_TYPE.FACTOR:
      moveFactorSearchPage({
        factorCode: item.chngDataCode,
        factorName: item.chngDataCodeName,
        factorTypeCode: item.chngDataObjUuid ?? "",
      });
      break;
    case PUBLISH_COMPOSE_ITEM_TYPE.TABLE:
      moveTableStrucTurePage({
        tableTypeCode: item.chngDataCode,
        tableName: item.chngDataCodeName,
      });
      break;
  }
};

provide("handleRedirect", handleRedirect);
</script>
