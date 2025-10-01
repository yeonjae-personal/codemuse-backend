<script setup lang="ts">

import CfDropdown from "@/components/controls/CfDropdown.vue";
import Prism from "vue-prism-component";
import cfDropdownExample from "@/components/controls/examples/CfDropdownExample";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfDropdownExample);
const variant = ref({ key: "solo", value: "Solo" });
const option = ref("Option 2");

const handleModelUpdate = (option: any) => {
  variant.value = option;
};
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Select" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>
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

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
