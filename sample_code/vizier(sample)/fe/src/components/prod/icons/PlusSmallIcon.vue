<template>
  <svg
    :width="size"
    :height="size"
    viewBox="0 0 13 13"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      fill-rule="evenodd"
      clip-rule="evenodd"
      d="M6.5 2.5C6.77614 2.5 7 2.72386 7 3V6H10C10.2761 6 10.5 6.22386 10.5 6.5C10.5 6.77614 10.2761 7 10 7H7V10C7 10.2761 6.77614 10.5 6.5 10.5C6.22386 10.5 6 10.2761 6 10V7H3C2.72386 7 2.5 6.77614 2.5 6.5C2.5 6.22386 2.72386 6 3 6H6V3C6 2.72386 6.22386 2.5 6.5 2.5Z"
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
