<template>
  <FourColumns ref="container">
    <MultiEntityDetail
      :category="DETAIL_CATEGORY.CREATE"
      is-add
      @on-cancel="handleCancel"
    />
    <MultiEntityAddGroup
      v-if="entityDisplayForm.groupSearch"
      :category="DETAIL_CATEGORY.CREATE"
    />
    <MultiEntityAddOffer
      v-if="entityDisplayForm.offerSearch"
      :category="DETAIL_CATEGORY.CREATE"
    />
    <MultiEntityAddComponent
      v-if="entityDisplayForm.componentSearch"
      :category="DETAIL_CATEGORY.CREATE"
    />
    <MultiEntityAddResource
      v-if="entityDisplayForm.resourceSearch"
      :category="DETAIL_CATEGORY.CREATE"
    />
  </FourColumns>
</template>

<script setup lang="ts">
import { useMultiEntityCreateStore } from "@/store";
import { DETAIL_CATEGORY } from "@/constants/extendsManager";

const multiEntityCreateStore = useMultiEntityCreateStore();
const { entityDisplayForm, isEdit, isAdded } = storeToRefs(
  multiEntityCreateStore
);

const handleCancel = () => {
  multiEntityCreateStore.resetParamListGroupSearch();
  multiEntityCreateStore.resetParamListComponentSearch();
  multiEntityCreateStore.resetParamListOfferSearch();
  multiEntityCreateStore.resetParamListResourceSearch();
};

onMounted(() => {
  if (!isAdded.value) {
    isEdit.value = true;
  }
});
</script>
