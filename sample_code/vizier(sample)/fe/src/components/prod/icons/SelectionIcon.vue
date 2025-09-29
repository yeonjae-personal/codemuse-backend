<template>
  <template v-if="selected">
    <svg
      :width="size"
      :height="size"
      viewBox="0 0 20 20"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M1.5 10C1.5 5.30558 5.30558 1.5 10 1.5C14.6944 1.5 18.5 5.30558 18.5 10C18.5 14.6944 14.6944 18.5 10 18.5C5.30558 18.5 1.5 14.6944 1.5 10Z"
        fill="white"
      />
      <path
        d="M1.5 10C1.5 5.30558 5.30558 1.5 10 1.5C14.6944 1.5 18.5 5.30558 18.5 10C18.5 14.6944 14.6944 18.5 10 18.5C5.30558 18.5 1.5 14.6944 1.5 10Z"
        stroke="#D9325A"
        stroke-width="3"
      />
      <circle cx="10" cy="10" r="3.5" fill="#D9325A" />
    </svg>
  </template>

  <template v-else>
    <svg
      :width="size"
      :height="size"
      viewBox="0 0 20 20"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M0.5 10C0.5 4.7533 4.7533 0.5 10 0.5C15.2467 0.5 19.5 4.7533 19.5 10C19.5 15.2467 15.2467 19.5 10 19.5C4.7533 19.5 0.5 15.2467 0.5 10Z"
        fill="white"
      />
      <path
        d="M0.5 10C0.5 4.7533 4.7533 0.5 10 0.5C15.2467 0.5 19.5 4.7533 19.5 10C19.5 15.2467 15.2467 19.5 10 19.5C4.7533 19.5 0.5 15.2467 0.5 10Z"
        stroke="#DCE0E6"
      />
    </svg>
  </template>
</template>
<script setup lang="ts">
defineProps({
  selected: {
    type: Boolean,
    default: false,
  },
  size: {
    type: String,
    default: "20",
  },
  fill: {
    type: String,
    default: "#BDC1C7",
  },
});
</script>
