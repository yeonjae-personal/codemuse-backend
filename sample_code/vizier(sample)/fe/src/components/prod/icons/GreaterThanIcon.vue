<template>
  <svg
    xmlns="http://www.w3.org/2000/svg"
    :width="size"
    :height="size"
    viewBox="0 0 12 12"
    fill="none"
  >
    <path
      fill-rule="evenodd"
      clip-rule="evenodd"
      d="M2.0529 1.27638C2.17639 1.02939 2.47673 0.929279 2.72372 1.05277L10.7237 5.55277C10.8931 5.63747 11.0001 5.8106 11.0001 5.99999C11.0001 6.18937 10.8931 6.3625 10.7237 6.4472L2.72372 10.9472C2.47673 11.0707 2.17639 10.9706 2.0529 10.7236C1.9294 10.4766 2.02951 10.1763 2.2765 10.0528L9.38208 5.99999L2.2765 1.9472C2.02951 1.82371 1.9294 1.52337 2.0529 1.27638Z"
      :fill="color"
    />
  </svg>
</template>

<script setup lang="ts">
type Props = {
  size?: number;
  color?: string;
};

withDefaults(defineProps<Props>(), {
  color: "#6B6D70",
  size: 12,
});
</script>
