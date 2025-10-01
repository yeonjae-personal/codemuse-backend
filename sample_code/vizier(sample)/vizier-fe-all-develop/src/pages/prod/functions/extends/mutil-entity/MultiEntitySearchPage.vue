<template>
  <FourColumns ref="container">
    <MultiEntitySearch />
    <MultiEntityDetail
      v-if="entityDisplayForm.entityDetail && selectedEntityDetails"
    />
    <MultiEntityAddGroup v-if="entityDisplayForm.groupSearch" />
    <MultiEntityAddOffer v-if="entityDisplayForm.offerSearch" />
    <MultiEntityAddComponent v-if="entityDisplayForm.componentSearch" />
    <MultiEntityAddResource v-if="entityDisplayForm.resourceSearch" />
  </FourColumns>
</template>

<script setup lang="ts">
import { useMultiEntitySearchStore } from "@/store";

const multiEntitySearchStore = useMultiEntitySearchStore();
const { entityDisplayForm, isEdit, selectedEntityDetails } = storeToRefs(
  multiEntitySearchStore
);
const { closeAllSearchPanel } = multiEntitySearchStore;
watch(
  () => isEdit.value,
  (val) => {
    if (!val) {
      closeAllSearchPanel();
    }
  }
);
</script>
