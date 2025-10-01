const cfTextareaExample = `<script setup lang="ts">
import CfTextarea from '@/components/controls/CfTextarea.vue';

const description = ref('');

const handleUpdateModel = (val: string) => {
  console.log(val);
};
</script>
<template>
  <div class="flex flex-col gap-4">
    <div class="flex flex-wrap gap-4">
      <cf-textarea label="Label"></cf-textarea>
      <cf-textarea label="Label" variant="outlined"></cf-textarea>
      <cf-textarea label="Label" variant="underlined"></cf-textarea>
      <cf-textarea label="Label" variant="solo"></cf-textarea>
      <cf-textarea label="Label" variant="solo-filled"></cf-textarea>
      <cf-textarea label="Label" variant="solo-inverted"></cf-textarea>
    </div>
    <div class="flex flex-wrap gap-4">
      <cf-textarea label="Label" variant="solo" prepend-icon="$vuetify"></cf-textarea>
      <cf-textarea
        :model="description"
        label="Label"
        variant="solo"
        bg-color="amber-lighten-4"
        color="orange orange-darken-4"
        @update:model="handleUpdateModel"
      ></cf-textarea>
    </div>
  </div>
</template>
`;
export default cfTextareaExample;
