const cfButtonExample = `<script lang="ts">
import CfButton from '@/components/controls/CfButton.vue';

const handleButtonClick = () => {
  console.log('Button clicked!');
}
</script>
<template>
  <div class="flex flex-col gap-4">
    <div class="flex flex-row gap-4">
      <cf-button label="Button Normal" @click="handleButtonClick" />
    </div>
    <div class="flex items-center gap-4">
      <cf-button label="Size normal" />
      <cf-button label="Size large" size="large" />
      <cf-button label="Size x-large" size="x-large" />
    </div>
    <div class="flex items-center gap-4">
      <cf-button label="Rounded 0" rounded="0" />
      <cf-button label="Rounded xs" rounded="xs" />
      <cf-button label="Rounded sm" rounded="sm" />
      <cf-button label="Button" />
      <cf-button label="Rounded lg" rounded="lg" />
      <cf-button label="Rounded xl" rounded="xl" />
    </div>
  </div>
</template>
`;

export default cfButtonExample;