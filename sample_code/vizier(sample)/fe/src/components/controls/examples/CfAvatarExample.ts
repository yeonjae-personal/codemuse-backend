const cfAvatarExample = `<script setup lang="ts">
import CfAvatar from '@/components/controls/CfAvatar.vue';
</script>
<template>
  <div class="flex gap-4">
    <cf-avatar color="surface-variant"></cf-avatar>
    <cf-avatar color="primary" text="CF"></cf-avatar>
    <cf-avatar icon="$vuetify"></cf-avatar>
    <cf-avatar image="https://cdn.vuetifyjs.com/images/john.jpg"></cf-avatar>
    <cf-avatar image="https://cdn.vuetifyjs.com/images/john.jpg" size="50"></cf-avatar>
  </div>
</template>
`;
export default cfAvatarExample;