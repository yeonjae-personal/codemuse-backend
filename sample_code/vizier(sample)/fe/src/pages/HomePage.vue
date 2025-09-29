<script setup lang="ts">
import { useUser } from "@/store";

const userStore = useUser();
const user = computed(() => {
  return userStore.user;
});
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <span>{{ user }}</span>
  </div>
</template>
