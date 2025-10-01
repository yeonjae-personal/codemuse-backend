const cfRadioExample = `<script setup lang="ts">

import CfRadio from "@/components/controls/CfRadio.vue";

const selectedValue = ref("");
const radioItems = [
  { key: "Radio One", value: "one" },
  { key: "Radio Two", value: "two" },
  { key: "Radio Three", value: "three" },
];
</script>
<template>
    <div class="flex gap-2 w-3/5">
      <v-container fluid>
        <p>{{ selectedValue }}</p>
        <cf-radio
          v-model="selectedValue"
          :radioItems="radioItems"
          label="key"
          value="value"
        ></cf-radio>
      </v-container>
    </div>
</template>
`;

export default cfRadioExample;