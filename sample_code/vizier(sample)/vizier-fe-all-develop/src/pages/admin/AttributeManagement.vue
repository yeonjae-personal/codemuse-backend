<template>
  <FourColumns>
    <ItemsView />
    <ItemEdit v-if="selectedItem && itemDetail" />
    <LabelSearch v-if="isShowLabelSearch" />
    <AttributeCodeSearch v-if="isShowAttributeCodeSearch" />
  </FourColumns>
</template>

<script setup>
import attributeManagementStore from "@/store/admin/attributeManagement.store";
import ItemsView from "./subs/attribute/ItemsView.vue";
import ItemEdit from "./subs/attribute/ItemEdit.vue";
import LabelSearch from "./subs/attribute/item-edit/LabelSearch.vue";
import AttributeCodeSearch from "./subs/attribute/item-edit/AttributeCodeSearch.vue";
import FourColumns from "@/components/prod/layout/FourColumns.vue";

const {
  selectedItem,
  isShowLabelSearch,
  isShowAttributeCodeSearch,
  itemDetail,
} = storeToRefs(attributeManagementStore());
const { getListFieldTypes } = attributeManagementStore();

onBeforeMount(() => {
  getListFieldTypes();
});
</script>
