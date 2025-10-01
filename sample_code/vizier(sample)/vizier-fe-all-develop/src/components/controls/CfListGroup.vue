<script setup lang="ts">
export interface ListGroup {
  title: string | number;
  subtitle?: string | number;
  prependAvatar?: string;
}

const props = defineProps({
  items: {
    type: Array<any>,
    default: [],
  },
  lines: {
    type: String as () => "one" | "two" | "three" | undefined,
    default: undefined,
  },
  title: {
    type: String,
    default: undefined,
  },
  subtitle: {
    type: String,
    default: undefined,
  },
  subheader: {
    type: String,
    default: undefined,
  },
});

defineEmits(["click"]);
</script>

<template>
  <v-list :lines="$props.lines" class="cf-list">
    <v-list-subheader v-if="props.subheader">{{
      props.subheader
    }}</v-list-subheader>
    <v-list-item
      v-for="(item, index) in props.items"
      :key="index"
      color="primary"
    >
      <template #prepend>
        <slot name="prepend" :data="{ item, index }"></slot>
      </template>
      <template #append>
        <slot name="append" :data="{ item, index }"></slot>
      </template>
    </v-list-item>
  </v-list>
</template>

<style scoped>
.cf-list :deep(img) {
  margin: 0;
}
.cf-list :deep(.v-list-item:hover) {
  cursor: pointer;
  background: #f6f6f6;
}
</style>
