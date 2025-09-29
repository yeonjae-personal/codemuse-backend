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
      d="M9.94721 1.27638C10.0707 1.52337 9.9706 1.82371 9.72361 1.9472L2.61803 5.99999L9.72361 10.0528C9.9706 10.1763 10.0707 10.4766 9.94721 10.7236C9.82372 10.9706 9.52338 11.0707 9.27639 10.9472L1.27639 6.4472C1.107 6.3625 1 6.18937 1 5.99999C1 5.8106 1.107 5.63747 1.27639 5.55277L9.27639 1.05277C9.52338 0.929279 9.82372 1.02939 9.94721 1.27638Z"
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
