<script setup lang="ts">
import CfButton from "./CfButton.vue";

const props = defineProps({
  title: {
    type: String,
    default: "",
  },
  subtitle: {
    type: String,
    default: "",
  },
  text: {
    type: String,
    default: "",
  },
  variant: {
    type: String as () =>
      | "flat"
      | "text"
      | "elevated"
      | "tonal"
      | "outlined"
      | "plain"
      | undefined,
    default: undefined,
  },
  buttons: {
    type: Array<any>,
    default: [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
  width: {
    type: Number,
    default: undefined,
  },
  color: {
    type: String,
    default: undefined,
  },
  hover: {
    type: Boolean,
    default: false,
  },
  prependIcon: {
    type: String,
    default: undefined,
  },
  appendIcon: {
    type: String,
    default: undefined,
  },
});

const emit = defineEmits(["click"]);
const handleButtonClick = (button: any) => {
  emit("click", button);
};
</script>

<template>
  <v-card
    :variant="props.variant"
    :loading="props.loading"
    :width="props.width"
    :color="props.color"
    :hover="props.hover"
    :prepend-icon="props.prependIcon"
    :append-icon="props.appendIcon"
  >
    <v-card-item v-if="props.title || props.subtitle">
      <v-card-title>{{ props.title }}</v-card-title>
      <v-card-subtitle>{{ props.subtitle }}</v-card-subtitle>
    </v-card-item>

    <v-card-text v-if="props.text">
      {{ props.text }}
    </v-card-text>

    <v-card-actions v-if="props.buttons.length > 0">
      <cf-button
        v-for="(button, index) in buttons"
        :key="index"
        :label="button.text"
        @click="handleButtonClick(button)"
      ></cf-button>
    </v-card-actions>
    <slot></slot>
  </v-card>
</template>

<style scoped></style>
