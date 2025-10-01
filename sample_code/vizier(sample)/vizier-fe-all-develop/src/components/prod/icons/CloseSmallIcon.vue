<template>
  <svg
    :width="width"
    :height="height"
    :viewBox="viewBox"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      fill-rule="evenodd"
      clip-rule="evenodd"
      d="M2.50105 2.50105C2.83579 2.16632 3.3785 2.16632 3.71323 2.50105L9 7.78782L14.2868 2.50105C14.6215 2.16632 15.1642 2.16632 15.4989 2.50105C15.8337 2.83579 15.8337 3.3785 15.4989 3.71323L10.2122 9L15.4989 14.2868C15.8337 14.6215 15.8337 15.1642 15.4989 15.4989C15.1642 15.8337 14.6215 15.8337 14.2868 15.4989L9 10.2122L3.71323 15.4989C3.3785 15.8337 2.83579 15.8337 2.50105 15.4989C2.16632 15.1642 2.16632 14.6215 2.50105 14.2868L7.78782 9L2.50105 3.71323C2.16632 3.3785 2.16632 2.83579 2.50105 2.50105Z"
      fill="#6B6D70"
    />
  </svg>
</template>
<script setup lang="ts">
defineProps({
  viewBox: {
    type: String,
    default: "0 0 18 18",
  },
  width: {
    type: String,
    default: "12",
  },
  height: {
    type: String,
    default: "12",
  },
});
</script>
