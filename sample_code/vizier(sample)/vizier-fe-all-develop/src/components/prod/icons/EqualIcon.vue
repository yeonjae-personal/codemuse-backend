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
      d="M1 3.5C1 3.22386 1.22386 3 1.5 3H10.5C10.7761 3 11 3.22386 11 3.5C11 3.77614 10.7761 4 10.5 4H1.5C1.22386 4 1 3.77614 1 3.5ZM1 8.5C1 8.22386 1.22386 8 1.5 8H10.5C10.7761 8 11 8.22386 11 8.5C11 8.77614 10.7761 9 10.5 9H1.5C1.22386 9 1 8.77614 1 8.5Z"
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
