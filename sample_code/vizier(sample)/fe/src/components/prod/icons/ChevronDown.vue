<template>
  <svg
    :width="size"
    :height="size"
    viewBox="0 0 16 16"
    :fill="fill"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      fill-rule="evenodd"
      clip-rule="evenodd"
      d="M3.52827 5.52858C3.78862 5.26823 4.21073 5.26823 4.47108 5.52858L7.99967 9.05717L11.5283 5.52858C11.7886 5.26823 12.2107 5.26823 12.4711 5.52858C12.7314 5.78892 12.7314 6.21103 12.4711 6.47138L8.47108 10.4714C8.21073 10.7317 7.78862 10.7317 7.52827 10.4714L3.52827 6.47138C3.26792 6.21103 3.26792 5.78892 3.52827 5.52858Z"
    />
  </svg>
</template>
<script setup lang="ts">
defineProps({
  size: {
    type: String,
    default: "16",
  },
  fill: {
    type: String,
    default: "#6B6D70",
  },
});
</script>
