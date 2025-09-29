<template>
  <svg
    :width="width"
    :height="height"
    viewBox="0 0 11 14"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      d="M0.865182 4.17992C0.865182 1.73403 2.90489 0 5.77493 0C8.64496 0 10.45 1.60626 10.45 4.16167H7.54388C7.54388 3.21252 6.83991 2.62842 5.73883 2.62842C4.54749 2.62842 3.78937 3.17601 3.78937 4.0704C3.78937 4.89179 4.20453 5.29335 5.14316 5.49413L7.14677 5.91395C9.52944 6.40678 10.6666 7.55671 10.6666 9.69231C10.6666 12.3025 8.64496 14 5.57637 14C2.59803 14 0.666626 12.3755 0.666626 9.83833H3.57276C3.57276 10.824 4.29478 11.3716 5.59442 11.3716C6.91211 11.3716 7.74244 10.8422 7.74244 9.98435C7.74244 9.23598 7.39948 8.85267 6.515 8.67014L4.47529 8.25033C2.09262 7.7575 0.865182 6.38853 0.865182 4.17992Z"
      fill="url(#paint0_linear_4422_7239)"
    />
    <defs>
      <linearGradient
        id="paint0_linear_4422_7239"
        x1="-0.333374"
        y1="5.16562e-07"
        x2="11.6666"
        y2="14"
        gradientUnits="userSpaceOnUse"
      >
        <stop stop-color="#6D10A5" />
        <stop offset="1" stop-color="#26F6DD" />
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
