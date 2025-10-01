<template>
  <svg
    :width="width"
    :height="height"
    viewBox="0 0 6 6"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <rect width="6" height="6" rx="3" fill="#B2DDFF" />
  </svg>
</template>
<script setup lang="ts">
defineProps({
  width: {
    type: String,
    default: "6",
  },
  height: {
    type: String,
    default: "6",
  },
});
</script>
