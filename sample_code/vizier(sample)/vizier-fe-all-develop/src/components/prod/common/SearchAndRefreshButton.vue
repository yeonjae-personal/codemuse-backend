<script setup lang="ts">
const emits = defineEmits(["handleSearch", "handleRefresh"]);

defineProps({
  isShowSearchButton: {
    type: Boolean,
    default: true,
  },
  isShowRefreshButton: {
    type: Boolean,
    default: true,
  },
  disabledRefreshButton: {
    type: Boolean,
    default: false,
  },
});
</script>

<template>
  <div class="flex gap-2 h-[40px]">
    <RefreshButton
      v-if="isShowRefreshButton"
      class="bg-white"
      :disabled="disabledRefreshButton"
      @click="emits('handleRefresh')"
    />
    <SearchButton
      v-if="isShowSearchButton"
      class="bg-white"
      @click="emits('handleSearch')"
    />
  </div>
</template>

<style scoped>
:deep(.v-btn__append) {
  display: none !important;
}
</style>
