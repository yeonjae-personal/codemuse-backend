<template>
  <svg
    width="32"
    height="32"
    viewBox="0 0 32 32"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      fill-rule="evenodd"
      clip-rule="evenodd"
      d="M20.9428 7.05703C21.4635 7.57773 21.4635 8.42195 20.9428 8.94265L13.8856 15.9998L20.9428 23.057C21.4635 23.5777 21.4635 24.4219 20.9428 24.9426C20.4221 25.4633 19.5778 25.4633 19.0572 24.9426L11.0572 16.9426C10.5365 16.4219 10.5365 15.5777 11.0572 15.057L19.0572 7.05703C19.5778 6.53633 20.4221 6.53633 20.9428 7.05703Z"
      fill="#3A3B3D"
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
    default: "#BA1642",
  },
});
</script>
