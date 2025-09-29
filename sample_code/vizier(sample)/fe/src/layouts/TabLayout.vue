<script setup lang="ts">
defineProps({
  title: {
    type: String,
    default: "",
  },
  showEditButton: {
    type: Boolean,
    default: false,
  },
});
</script>

<template>
  <div class="h-full flex flex-col">
    <slot name="body"></slot>
    <slot name="footer"></slot>
  </div>
</template>
