const cfFileInputExample = `<script setup lang="ts">
import CfFileInput from '@/components/controls/CfFileInput.vue';

const detectChange = (event: any) => {
  console.log(event);
};
</script>
<template>
  <div class="flex flex-col gap-4">
    <cf-file-input label="Default"></cf-file-input>
    <div class="flex flex-wrap gap-4">
      <cf-file-input label="Variant outlined" variant="outlined"></cf-file-input>
      <cf-file-input label="Variant underlined" variant="underlined"></cf-file-input>
    </div>
    <div class="flex flex-wrap gap-4">
      <cf-file-input label="Variant solo" variant="solo"></cf-file-input>
      <cf-file-input label="Variant solo-filled" variant="solo-filled"></cf-file-input>
      <cf-file-input label="Variant solo-inverted" variant="solo-inverted"></cf-file-input>
    </div>
    <div class="flex flex-wrap gap-4">
      <cf-file-input label="Show icon clearable" variant="solo" clearable></cf-file-input>
      <cf-file-input label="Disabled File input" variant="solo" disabled></cf-file-input>
    </div>
    <div class="flex flex-wrap gap-4">
      <cf-file-input label="Accept type" variant="solo" accept="image/*"></cf-file-input>
      <cf-file-input
        label="Change prepend-icon"
        variant="solo"
        accept="image/*"
        prepend-icon="mdi-camera"
      ></cf-file-input>
    </div>
    <div class="flex flex-wrap gap-4">
      <cf-file-input
        label="Show size"
        variant="solo"
        show-size
        @update:model="detectChange"
      ></cf-file-input>
    </div>
  </div>
</template>
`;
export default cfFileInputExample;
