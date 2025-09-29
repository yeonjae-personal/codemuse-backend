<script setup lang="ts">
export interface ListGroup {
  title: string | number;
  subtitle?: string | number;
  prependAvatar?: string;
}

const props = defineProps({
  panels: {
    type: Array<any>,
    required: true,
  },
});
</script>

<template>
  <v-expansion-panels class="mb-6" variant="accordion">
    <v-expansion-panel v-for="(panel, index) in props.panels" :key="index">
      <v-expansion-panel-title expand-icon="mdi-menu-down">
        {{ panel.title }}
      </v-expansion-panel-title>
      <v-expansion-panel-text>
        <v-layout :panel-data="panel">
          <slot :panel="panel"></slot>
        </v-layout>
      </v-expansion-panel-text>
    </v-expansion-panel>
  </v-expansion-panels>
</template>

<style scoped></style>
