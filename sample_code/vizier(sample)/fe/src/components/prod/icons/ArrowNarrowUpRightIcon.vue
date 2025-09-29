<template>
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="16"
    height="16"
    viewBox="0 0 16 16"
    fill="none"
  >
    <path
      fill-rule="evenodd"
      clip-rule="evenodd"
      d="M3.33398 4.00065C3.33398 3.63246 3.63246 3.33398 4.00065 3.33398H12.0007C12.3688 3.33398 12.6673 3.63246 12.6673 4.00065V12.0007C12.6673 12.3688 12.3688 12.6673 12.0007 12.6673C11.6325 12.6673 11.334 12.3688 11.334 12.0007V5.61013L4.47206 12.4721C4.21171 12.7324 3.7896 12.7324 3.52925 12.4721C3.2689 12.2117 3.2689 11.7896 3.52925 11.5292L10.3912 4.66732H4.00065C3.63246 4.66732 3.33398 4.36884 3.33398 4.00065Z"
      :fill="color"
    />
  </svg>
</template>

<script lang="ts" setup>
type Props = {
  color?: string;
};

withDefaults(defineProps<Props>(), {
  color: "#BA1642",
});
</script>
