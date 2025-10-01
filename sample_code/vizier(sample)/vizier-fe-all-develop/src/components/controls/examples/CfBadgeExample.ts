const cfBadgeExample = `<script setup lang="ts">
import CfBadge from '@/components/controls/CfBadge.vue';
</script>
<template>
  <div class="flex gap-6">
    <cf-badge icon="$vuetify" icon-size="x-large"></cf-badge>
    <cf-badge dot icon="$vuetify" icon-size="x-large"></cf-badge>
    <cf-badge floating icon="$vuetify" icon-size="x-large"></cf-badge>
    <cf-badge inline icon="$vuetify" icon-size="x-large"></cf-badge>
    <cf-badge content="99" icon="$vuetify" icon-size="x-large"></cf-badge>
    <cf-badge content="99" color="error" icon="$vuetify" icon-size="x-large"></cf-badge>
  </div>
</template>
`;
export default cfBadgeExample;
