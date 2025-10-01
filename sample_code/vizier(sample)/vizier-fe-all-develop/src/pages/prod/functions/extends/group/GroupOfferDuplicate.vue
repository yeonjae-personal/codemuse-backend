<template>
  <FourColumns ref="container">
    <GridColumn :span="2">
      <GroupList />
    </GridColumn>
    <GroupDetailDuplicate v-if="selectedGroup?.objUuid" />
  </FourColumns>
</template>

<script setup lang="ts">
import { MenuItemID } from "@/enums/redirect";
import GroupDetailDuplicate from "./GroupDetailDuplicate.vue";
import GroupList from "./GroupList.vue";
import { useOfferDuplicateProcessStore } from "@/store";

const offerDuplicate = useOfferDuplicateProcessStore();
const { selectedGroup } = storeToRefs(offerDuplicate);

onBeforeMount(() => {
  replaceTabName(MenuItemID.GroupDuplicate, "Extends Duplicate Group");
});

const replaceTabName = inject<any>("replaceTabName");
</script>

<style lang="scss"></style>
