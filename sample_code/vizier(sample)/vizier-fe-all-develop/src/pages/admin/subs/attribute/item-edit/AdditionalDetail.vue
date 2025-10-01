<template>
  <div class="additional-wrapper">
    <FieldItem v-for="item in additionalFields" :key="item.id" :item="item" />
  </div>
</template>
<script lang="ts" setup>
import attributeManagementStore from "@/store/admin/attributeManagement.store";
import FieldItem from "./FieldItem.vue";
const { itemDetail } = storeToRefs(attributeManagementStore());

const additionalFields = computed(() => {
  return itemDetail.value?.additionalInfo;
});
</script>
<style lang="scss" scoped>
.additional-wrapper {
  display: flex;
  flex-direction: column;
  row-gap: 16px;
}
</style>
