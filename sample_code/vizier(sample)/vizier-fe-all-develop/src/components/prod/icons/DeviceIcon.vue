<template>
  <svg
    :width="width"
    :height="height"
    viewBox="0 0 14 18"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      fill-rule="evenodd"
      clip-rule="evenodd"
      d="M0.621094 0.928711H1.54773H12.4525H13.3791V1.85535V14.2911C13.3791 15.8264 12.1345 17.071 10.5992 17.071H3.40101C1.86571 17.071 0.621094 15.8264 0.621094 14.2911V1.85535V0.928711ZM2.47437 2.78199V8.99988H11.5259V2.78199H2.47437ZM2.47437 14.2911V11.068H11.5259V14.2911C11.5259 14.8029 11.111 15.2177 10.5992 15.2177H3.40101C2.88925 15.2177 2.47437 14.8029 2.47437 14.2911ZM8.03421 12.1019H5.96607V14.1701H8.03421V12.1019Z"
      fill="#2E90FA"
    />
  </svg>
</template>
<script setup lang="ts">
defineProps({
  width: {
    type: String,
    default: "14",
  },
  height: {
    type: String,
    default: "18",
  },
});
</script>
