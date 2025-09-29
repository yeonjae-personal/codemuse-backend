<script setup lang="ts">
const props = defineProps({
  color: {
    type: String,
    default: undefined,
  },
  size: {
    type: String,
    default: undefined,
  },
  icon: {
    type: String,
    default: undefined,
  },
  image: {
    type: String,
    default: undefined,
  },
  text: {
    type: String,
    default: undefined,
  },
});
</script>

<template>
  <v-avatar
    :icon="props.icon"
    :image="props.image"
    :size="props.size"
    :color="props.color"
    class="cf-avatar"
  >
    <span>{{ props.text }}</span>
  </v-avatar>
</template>

<style scoped>
.cf-avatar :deep(img) {
  margin: 0px;
}
</style>
