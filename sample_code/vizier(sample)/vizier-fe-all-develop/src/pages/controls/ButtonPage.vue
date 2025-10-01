<script setup lang="ts">
import Prism from "vue-prism-component";
import CfButton from "../../components/controls/CfButton.vue";
import cfButtonExample from "../../components/controls/examples/CfButtonExample.ts";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfButtonExample);

const handleButtonClick = () => {
  //
};
</script>

<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Button" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>
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

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
