<script setup lang="ts">
const props = defineProps({
  content: {
    type: [String, Number],
    default: undefined,
  },
  color: {
    type: String,
    default: undefined,
  },
  dot: {
    type: Boolean,
    default: false,
  },
  floating: {
    type: Boolean,
    default: false,
  },
  inline: {
    type: Boolean,
    default: false,
  },
  icon: {
    type: String,
    default: undefined,
  },
  iconSize: {
    type: String,
    default: "x-large",
  },
});
</script>

<template>
  <v-badge
    :content="props.content"
    :color="props.color"
    :dot="props.dot"
    :floating="props.floating"
    :inline="props.inline"
  >
    <v-icon
      v-if="props.icon"
      :icon="props.icon"
      :size="props.iconSize"
    ></v-icon>
  </v-badge>
</template>

<style scoped></style>
