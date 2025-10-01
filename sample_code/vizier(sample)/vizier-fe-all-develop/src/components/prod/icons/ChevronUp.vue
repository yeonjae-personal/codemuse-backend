<template>
  <svg
    :width="size"
    :height="size"
    :fill="fill"
    viewBox="0 0 16 16"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      fill-rule="evenodd"
      clip-rule="evenodd"
      d="M7.52827 5.52858C7.78862 5.26823 8.21073 5.26823 8.47108 5.52858L12.4711 9.52858C12.7314 9.78892 12.7314 10.211 12.4711 10.4714C12.2107 10.7317 11.7886 10.7317 11.5283 10.4714L7.99967 6.94279L4.47108 10.4714C4.21073 10.7317 3.78862 10.7317 3.52827 10.4714C3.26792 10.211 3.26792 9.78892 3.52827 9.52858L7.52827 5.52858Z"
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
