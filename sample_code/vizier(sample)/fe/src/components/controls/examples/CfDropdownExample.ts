const cfDropdownExample = `<script setup lang="ts">
import { ref } from 'vue';
import CfDropdown from '@/components/controls/CfDropdown.vue';

const variant = ref('underlined');
const option = ref('Option 2');

const handleModelUpdate = (option: any) => {
  variant.value = option;
}

</script>
<template>
  <div class="flex gap-2 w-3/5">
    <cf-dropdown
      label="variant"
      variant="solo"
      :items="[
        { key: 'default', value: 'Default' },
        { key: 'outlined', value: 'Outlined' },
        { key: 'underlined', value: 'Underlined' },
        { key: 'solo', value: 'Solo' },
        { key: 'solo-filled', value: 'Solo-filled' },
        { key: 'solo-inverted', value: 'Solo-inverted' },
      ]"
      item-title="value"
      item-value="key"
      :model="variant"
      @update:model="handleModelUpdate"
    ></cf-dropdown>
    <cf-dropdown
      label="Options"
      :clearable="true"
      :variant="variant.key"
      :items="['Option 1', 'Option 2', 'Option 3']"
      :model="option"
    ></cf-dropdown>
  </div>
</template>
`;

export default cfDropdownExample;
