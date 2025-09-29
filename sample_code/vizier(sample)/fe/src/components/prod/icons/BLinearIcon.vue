<template>
  <svg
    :width="width"
    :height="height"
    viewBox="0 0 11 14"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      d="M0.666626 14V0H6.20619C8.88605 0 10.5048 1.47567 10.5048 3.87838C10.5048 5.37297 9.85727 6.47027 8.65224 7.01892C9.96519 7.47297 10.6666 8.55135 10.6666 10.1405C10.6666 12.6189 9.08389 14 6.20619 14H0.666626ZM5.90044 2.68649H3.58029V5.73243H5.90044C6.9436 5.73243 7.51914 5.18378 7.51914 4.18108C7.51914 3.1973 6.96159 2.68649 5.90044 2.68649ZM6.04432 8.30541H3.58029V11.3135H6.04432C7.12346 11.3135 7.68101 10.8027 7.68101 9.76216C7.68101 8.81622 7.10548 8.30541 6.04432 8.30541Z"
      fill="url(#paint0_linear_4422_24719)"
    />
    <defs>
      <linearGradient
        id="paint0_linear_4422_24719"
        x1="-0.333374"
        y1="5.16562e-07"
        x2="11.6666"
        y2="14"
        gradientUnits="userSpaceOnUse"
      >
        <stop stop-color="#8A20CB" />
        <stop offset="1" stop-color="#708FFF" />
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
