<template>
  <FourColumns>
    <RelationDefinition
      :page="RELATION_PAGE.CREATE"
      is-add
      class="col-span-1"
    />
  </FourColumns>
</template>

<script setup lang="ts">
import { RELATION_PAGE } from "@/constants/extendsManager";
import { useRelationCreateStore } from "@/store";

const relationCreateStore = useRelationCreateStore();
const { relationDetail } = storeToRefs(relationCreateStore);
const { getExtendsDependencyRelationDefinitionDetail } = relationCreateStore;

onBeforeMount(() => {
  if (!relationDetail.value?.generalTab) {
    getExtendsDependencyRelationDefinitionDetail(null, false, true);
  }
});
</script>
