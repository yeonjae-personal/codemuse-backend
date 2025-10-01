const cfInputExample = `<script setup lang="ts">
import CfInput from '@/components/controls/CfInput.vue';

const project = ref('CF');

const handleModelUpdate = (value: string) => {
  project.value = value;
}
</script>
<template>
  <div class="flex flex-col gap-4">
    <div class="flex gap-4 items-center">
      <div class="flex w-1/3">
        <cf-input label="Project Name" :model="project" @update:model="handleModelUpdate">
        </cf-input>
      </div>
      <span>Project Name: {{ project }}</span>
    </div>
    <div class="flex gap-2">
      <cf-input label="Style filled" variant="filled"></cf-input>
      <cf-input label="Style outlined" variant="outlined"></cf-input>
      <cf-input label="Style plain" variant="plain"></cf-input>
      <cf-input label="Style solo" variant="solo"></cf-input>
      <cf-input label="Style solo-inverted" variant="solo-inverted"></cf-input>
      <cf-input label="Style solo-filled" variant="solo-filled"></cf-input>
    </div>
  </div>
</template>
`;
export default cfInputExample;