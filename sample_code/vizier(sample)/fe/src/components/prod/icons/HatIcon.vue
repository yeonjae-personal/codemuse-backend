<template>
  <svg
    width="11"
    height="11"
    viewBox="0 0 11 11"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path d="M0 11L11 0H6C2.68629 0 0 2.68629 0 6V11Z" :fill="fill" />
  </svg>
</template>
<script setup lang="ts">
defineProps({
  fill: {
    type: String,
    default: "#DCE0E5",
  },
});
</script>
