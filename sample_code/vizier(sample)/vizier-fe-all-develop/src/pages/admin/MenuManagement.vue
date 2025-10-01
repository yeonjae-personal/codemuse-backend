<template>
  <div class="bg-base flex h-full gap-4">
    <TreeMenu :is-search="isSearch" @set-item-selected="onChangeItemSelected" />
    <DetailMenuContent
      :item="itemContent"
      @trick-search="handleSearch"
      @reset-menu-selected="onChangeItemSelected(null)"
    />
  </div>
</template>

<script setup>
import TreeMenu from "@/pages/admin/subs/menu/TreeMenu.vue";
import DetailMenuContent from "@/pages/admin/subs/menu/DetailMenuContent.vue";

const itemContent = ref(null);
const isSearch = ref(false);

const onChangeItemSelected = (item) => {
  itemContent.value = item;
};
const handleSearch = (value) => {
  isSearch.value = value || false;
};
onMounted(async () => {});
</script>

<style lang="scss"></style>
