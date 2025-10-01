<template>
  <FourColumns>
    <RelationSearch :page="RELATION_PAGE.SEARCH" />
    <RelationDefinition
      v-if="isShowRelationDetail"
      :page="RELATION_PAGE.SEARCH"
      class="col-span-1"
    />
  </FourColumns>
</template>

<script setup lang="ts">
import { RELATION_PAGE } from "@/constants/extendsManager";
import { useRelationSearchStore } from "@/store";

const { isShowRelationDetail } = storeToRefs(useRelationSearchStore());
</script>
