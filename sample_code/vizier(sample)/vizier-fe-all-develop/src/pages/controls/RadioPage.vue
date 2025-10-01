<script setup lang="ts">

import CfRadio from "@/components/controls/CfRadio.vue";
import Prism from "vue-prism-component";
import cfRadioExample from "@/components/controls/examples/CfRadioExample";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfRadioExample);
const selectedValue = ref("");
const radioItems = [
  { key: "Radio One", value: "one" },
  { key: "Radio Two", value: "two" },
  { key: "Radio Three", value: "three" },
];
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Radio" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>
    <div class="flex gap-2 w-3/5">
      <v-container fluid>
        <p>{{ selectedValue }}</p>
        <cf-radio
          v-model="selectedValue"
          :radio-items="radioItems"
          label="key"
          value="value"
        ></cf-radio>
      </v-container>
    </div>
    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
