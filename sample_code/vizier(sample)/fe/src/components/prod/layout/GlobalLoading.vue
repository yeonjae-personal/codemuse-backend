<template>
  <v-progress-linear
    v-if="loading"
    indeterminate
    color="primary"
    class="fixed-top"
  ></v-progress-linear>
</template>

<script setup lang="ts">
import { useLoadingStore } from "@/store";

const loadingStore = useLoadingStore();
const { loading } = storeToRefs(loadingStore);
</script>

<style scoped>
.fixed-top {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 9999;
}
</style>
