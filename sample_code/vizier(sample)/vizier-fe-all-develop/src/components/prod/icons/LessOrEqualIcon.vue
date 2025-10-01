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
      d="M9.966 1.31879C10.0661 1.57616 9.93859 1.86593 9.68122 1.96601L2.87951 5.00001L9.68122 8.03401C9.93859 8.1341 10.0661 8.42387 9.966 8.68124C9.86592 8.9386 9.57614 9.0661 9.31878 8.96601L1.31878 5.46601C1.12659 5.39128 1 5.20622 1 5.00001C1 4.79381 1.12659 4.60875 1.31878 4.53401L9.31878 1.03401C9.57614 0.933923 9.86592 1.06142 9.966 1.31879ZM1 10.5C1 10.2239 1.22386 10 1.5 10H9.5C9.77614 10 10 10.2239 10 10.5C10 10.7762 9.77614 11 9.5 11H1.5C1.22386 11 1 10.7762 1 10.5Z"
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
