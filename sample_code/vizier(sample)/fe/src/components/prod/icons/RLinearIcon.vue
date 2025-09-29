<template>
  <svg
    :width="width"
    :height="height"
    viewBox="0 0 11 14"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      d="M3.48402 14H0.666626V0H5.64054C8.77097 0 10.5449 1.64595 10.5449 4.54054C10.5449 6.35676 9.83184 7.64324 8.40576 8.43784L10.6666 14H7.58837L5.65793 9.1H3.48402V14ZM3.48402 2.72432V6.41351H5.62315C6.85793 6.41351 7.57097 5.73243 7.57097 4.54054C7.57097 3.36757 6.89271 2.72432 5.64054 2.72432H3.48402Z"
      fill="url(#paint0_linear_4422_40269)"
    />
    <defs>
      <linearGradient
        id="paint0_linear_4422_40269"
        x1="-0.333374"
        y1="5.16562e-07"
        x2="11.6666"
        y2="14"
        gradientUnits="userSpaceOnUse"
      >
        <stop stop-color="#6D10A5" />
        <stop offset="1" stop-color="#FB6B8E" />
      </linearGradient>
    </defs>
  </svg>
</template>
<script setup lang="ts">
defineProps({
  width: {
    type: String,
    default: "11",
  },
  height: {
    type: String,
    default: "14",
  },
});
</script>